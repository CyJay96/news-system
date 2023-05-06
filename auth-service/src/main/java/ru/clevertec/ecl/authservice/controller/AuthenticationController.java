package ru.clevertec.ecl.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.authservice.exception.UserExistenceException;
import ru.clevertec.ecl.authservice.model.dto.request.LoginRequestDto;
import ru.clevertec.ecl.authservice.model.dto.request.RegisterRequestDto;
import ru.clevertec.ecl.authservice.model.dto.response.APIResponse;
import ru.clevertec.ecl.authservice.model.dto.response.AuthDtoResponse;
import ru.clevertec.ecl.authservice.service.AuthenticationService;

import static ru.clevertec.ecl.authservice.controller.AuthenticationController.AUTH_API_PATH;

/**
 * Authentication API
 *
 * @author Konstantin Voytko
 */
@RestController
@Validated
@RequestMapping(value = AUTH_API_PATH)
@RequiredArgsConstructor
@Tag(name = "AuthenticationController", description = "Authentication API")
public class AuthenticationController {

    public static final String AUTH_API_PATH = "/api/v0/auth";

    private final AuthenticationService authenticationService;

    /**
     * POST /api/v0/auth : Register a new User
     *
     * @param registerRequestDto Register Request DTO to save a new User (required)
     * @throws UserExistenceException if User with such name or email already exists
     * @return authentication DTO with JWT
     */
    @Operation(summary = "Register User", tags = "AuthenticationController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registered User"),
            @ApiResponse(responseCode = "409", description = "User with such name or email already exists", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
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
     * @param loginRequestDto Login Request DTO to authorize (required)
     * @throws BadCredentialsException if the User entered invalid username or password
     * @return authentication DTO with JWT
     */
    @Operation(summary = "Authorize User", tags = "AuthenticationController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authorized User"),
            @ApiResponse(responseCode = "400", description = "Invalid username or password", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
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
