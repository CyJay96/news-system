package ru.clevertec.ecl.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.authservice.model.dto.request.LoginRequestDto;
import ru.clevertec.ecl.authservice.model.dto.request.RegisterRequestDto;
import ru.clevertec.ecl.authservice.model.dto.response.APIResponse;
import ru.clevertec.ecl.authservice.model.dto.response.AuthDtoResponse;
import ru.clevertec.ecl.authservice.service.AuthenticationService;

import static ru.clevertec.ecl.authservice.controller.AuthenticationController.AUTH_API_PATH;

@RestController
@Validated
@RequestMapping(value = AUTH_API_PATH)
@RequiredArgsConstructor
public class AuthenticationController {

    public static final String AUTH_API_PATH = "/v0/auth";

    private final AuthenticationService authenticationService;

    /**
     * POST /api/v0/auth : Create a new User
     *
     * @param registerRequestDto Register Request object to create (required)
     */
    @PostMapping("/register")
    public ResponseEntity<APIResponse<AuthDtoResponse>> register(@RequestBody @Valid RegisterRequestDto registerRequestDto) {
        AuthDtoResponse authResponse = authenticationService.register(registerRequestDto);
        return APIResponse.of(
                "User was created",
                AUTH_API_PATH,
                HttpStatus.CREATED,
                authResponse
        );
    }

    /**
     * POST /api/v0/auth : Authorize a User
     *
     * @param loginRequestDto Login Request object to authorize (required)
     */
    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthDtoResponse>> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        AuthDtoResponse authResponse = authenticationService.login(loginRequestDto);
        return APIResponse.of(
                "User was authorized",
                AUTH_API_PATH,
                HttpStatus.OK,
                authResponse
        );
    }
}
