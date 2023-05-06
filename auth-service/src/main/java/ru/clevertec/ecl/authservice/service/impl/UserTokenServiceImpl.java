package ru.clevertec.ecl.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.authservice.exception.TokenExpirationException;
import ru.clevertec.ecl.authservice.exception.TokenValidationException;
import ru.clevertec.ecl.authservice.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.authservice.security.jwt.JwtTokenProvider;
import ru.clevertec.ecl.authservice.service.UserService;
import ru.clevertec.ecl.authservice.service.UserTokenService;

/**
 * User Token Service to work with the User entity & JWT
 *
 * @author Konstantin Voytko
 */
@Service
@RequiredArgsConstructor
public class UserTokenServiceImpl implements UserTokenService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Find User entity info by JWT
     *
     * @param token JWT to find
     * @throws TokenExpirationException if the User token was expired
     * @throws TokenValidationException if the User token is not valid
     * @return found User DTO by JWT
     */
    @Override
    public UserDtoResponse findUserByToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new TokenValidationException();
        }
        String username = jwtTokenProvider.getUsername(token);
        return userService.findByUsername(username);
    }
}
