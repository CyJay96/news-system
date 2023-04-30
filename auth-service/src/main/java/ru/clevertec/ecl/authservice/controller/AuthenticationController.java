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
import ru.clevertec.ecl.authservice.model.dto.request.SignInRequestDto;
import ru.clevertec.ecl.authservice.model.dto.request.SignUpRequestDto;
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
     * @param userRequestDto Sign Up Request object to create (required)
     */
    @PostMapping("/signup")
    public ResponseEntity<APIResponse<AuthDtoResponse>> signUp(@RequestBody @Valid SignUpRequestDto userRequestDto) {
        AuthDtoResponse authResponse = authenticationService.signUp(userRequestDto);
        return APIResponse.of(
                "User with ID " + authResponse.getUser().getId() + " was created",
                AUTH_API_PATH,
                HttpStatus.CREATED,
                authResponse
        );
    }

    /**
     * POST /api/v0/auth : Authorize a User
     *
     * @param userRequestDto Sign Up Request object to authorize (required)
     */
    @PostMapping("/signin")
    public ResponseEntity<APIResponse<AuthDtoResponse>> signIn(@RequestBody @Valid SignInRequestDto userRequestDto) {
        AuthDtoResponse authResponse = authenticationService.signIn(userRequestDto);
        return APIResponse.of(
                "User with ID " + authResponse.getUser().getId() + " was authorized",
                AUTH_API_PATH,
                HttpStatus.OK,
                authResponse
        );
    }
}
