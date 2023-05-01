package ru.clevertec.ecl.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.authservice.exception.EntityNotFoundException;
import ru.clevertec.ecl.authservice.exception.UserExistenceException;
import ru.clevertec.ecl.authservice.messaging.RabbitMqSender;
import ru.clevertec.ecl.authservice.model.dto.request.LoginRequestDto;
import ru.clevertec.ecl.authservice.model.dto.request.RegisterRequestDto;
import ru.clevertec.ecl.authservice.model.dto.response.AuthDtoResponse;
import ru.clevertec.ecl.authservice.model.entity.Role;
import ru.clevertec.ecl.authservice.model.entity.User;
import ru.clevertec.ecl.authservice.repository.RoleRepository;
import ru.clevertec.ecl.authservice.repository.UserRepository;
import ru.clevertec.ecl.authservice.security.jwt.JwtTokenProvider;
import ru.clevertec.ecl.authservice.service.AuthenticationService;
import ru.clevertec.ecl.authservice.service.UserService;

import java.time.OffsetDateTime;
import java.util.List;

import static ru.clevertec.ecl.authservice.model.enums.Role.ROLE_USER;
import static ru.clevertec.ecl.authservice.model.enums.Status.ACTIVE;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RabbitMqSender rabbitMqSender;

    @Override
    public AuthDtoResponse register(final RegisterRequestDto registerRequestDto) {
        try {
            final User user = User.builder()
                    .username(registerRequestDto.getUsername())
                    .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                    .email(registerRequestDto.getEmail())
                    .roles(List.of(
                            roleRepository.findByName(ROLE_USER.name())
                                    .orElseThrow(() -> new EntityNotFoundException(Role.class))
                    ))
                    .createDate(OffsetDateTime.now())
                    .lastUpdateDate(OffsetDateTime.now())
                    .status(ACTIVE)
                    .build();
            userRepository.save(user);

            return login(LoginRequestDto.builder()
                    .username(registerRequestDto.getUsername())
                    .password(registerRequestDto.getPassword())
                    .build());
        } catch (DataIntegrityViolationException e) {
            throw new UserExistenceException(registerRequestDto.getUsername(), registerRequestDto.getEmail());
        }
    }

    @Override
    public AuthDtoResponse login(final LoginRequestDto loginRequestDto) {
        try {
            final String username = loginRequestDto.getUsername();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, loginRequestDto.getPassword()));

            final User user = userService.getEntityByUsername(username);

            final String token = jwtTokenProvider.createToken(user);

            rabbitMqSender.send(user.getUsername());

            return AuthDtoResponse.builder()
                    .token(token)
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
