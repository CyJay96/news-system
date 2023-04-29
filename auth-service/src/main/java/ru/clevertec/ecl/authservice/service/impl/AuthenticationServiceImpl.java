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
import ru.clevertec.ecl.authservice.mapper.UserMapper;
import ru.clevertec.ecl.authservice.model.dto.request.SignInRequestDto;
import ru.clevertec.ecl.authservice.model.dto.request.SignUpRequestDto;
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

import static ru.clevertec.ecl.authservice.model.enums.Role.ROLE_SUBSCRIBER;
import static ru.clevertec.ecl.authservice.model.enums.Status.ACTIVE;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public AuthDtoResponse signUp(final SignUpRequestDto userRequestDto) {
        try {
            final User user = User.builder()
                    .username(userRequestDto.getUsername())
                    .password(passwordEncoder.encode(userRequestDto.getPassword()))
                    .email(userRequestDto.getEmail())
                    .roles(List.of(
                            roleRepository.findByName(ROLE_SUBSCRIBER.name())
                                    .orElseThrow(() -> new EntityNotFoundException(Role.class))
                    ))
                    .createDate(OffsetDateTime.now())
                    .lastUpdateDate(OffsetDateTime.now())
                    .status(ACTIVE)
                    .build();
            userRepository.save(user);

            return signIn(SignInRequestDto.builder()
                    .username(userRequestDto.getUsername())
                    .password(userRequestDto.getPassword())
                    .build());
        } catch (DataIntegrityViolationException e) {
            throw new UserExistenceException(userRequestDto.getUsername(), userRequestDto.getEmail());
        }
    }

    @Override
    public AuthDtoResponse signIn(final SignInRequestDto userRequestDto) {
        try {
            final String username = userRequestDto.getUsername();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, userRequestDto.getPassword()));

            final User user = userService.getEntityByUsername(username);

            final String token = jwtTokenProvider.createToken(user);

            return AuthDtoResponse.builder()
                    .user(userMapper.toUserDtoResponse(user))
                    .token(token)
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
