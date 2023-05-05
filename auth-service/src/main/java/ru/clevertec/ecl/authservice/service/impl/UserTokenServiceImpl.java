package ru.clevertec.ecl.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.authservice.exception.TokenValidationException;
import ru.clevertec.ecl.authservice.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.authservice.security.jwt.JwtTokenProvider;
import ru.clevertec.ecl.authservice.service.UserService;
import ru.clevertec.ecl.authservice.service.UserTokenService;

@Service
@RequiredArgsConstructor
public class UserTokenServiceImpl implements UserTokenService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDtoResponse findUserByToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new TokenValidationException();
        }
        String username = jwtTokenProvider.getUsername(token);
        return userService.findByUsername(username);
    }
}
