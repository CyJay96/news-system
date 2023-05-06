package ru.clevertec.ecl.authservice.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

/**
 * JWT Filtering Service
 *
 * @author Konstantin Voytko
 */
@Service
@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Validate the JWT and puts the current authorized Use to SecurityContextHolder
     *
     * @param request User servlet request
     * @param response User servlet response
     * @param chain filter invocation chain of a filtered request for a resource
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        if (Objects.nonNull(token) && jwtTokenProvider.validateToken(token)) {
            final Authentication authentication = jwtTokenProvider.getAuthentication(token);

            if (Objects.nonNull(authentication)) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}
