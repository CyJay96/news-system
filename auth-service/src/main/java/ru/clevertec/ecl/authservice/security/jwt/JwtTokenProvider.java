package ru.clevertec.ecl.authservice.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.authservice.exception.TokenExpirationException;
import ru.clevertec.ecl.authservice.model.entity.Role;
import ru.clevertec.ecl.authservice.model.entity.User;

import java.util.Base64;
import java.util.Date;
import java.util.Objects;

/**
 * Service for working with a JWT token
 *
 * @author Konstantin Voytko
 */
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private Long validityInMs;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String USER_ROLES_FIELD = "roles";

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    /**
     * Create a new JWT by User entity
     *
     * @param user User entity to create a new JWT
     * @return User JWT
     */
    public String createToken(final User user) {
        final Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put(USER_ROLES_FIELD, user.getRoles().stream()
                .map(Role::getName)
                .toList());

        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * Put UserDetails in Authentication by JWT
     *
     * @param token User JWT to put UserDetails in Authentication
     * @return Authentication object
     */
    public Authentication getAuthentication(final String token) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    /**
     * Get username from User JWT
     *
     * @param token User JWT to parse username
     * @return username
     */
    public String getUsername(final String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Add "Bearer " to JWT
     *
     * @param request current request
     * @return final JWT
     */
    public String resolveToken(HttpServletRequest request) {
        final String bearerToken = request.getHeader(AUTHORIZATION);
        if (Objects.nonNull(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }

    /**
     * Validate JWT by secret key & expiration date
     *
     * @param token current JWT
     * @return boolean value, whether the JWT is valid
     */
    public boolean validateToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenExpirationException();
        }
    }
}
