package ru.clevertec.ecl.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.authservice.exception.EntityNotFoundException;
import ru.clevertec.ecl.authservice.exception.TokenExpirationException;
import ru.clevertec.ecl.authservice.exception.TokenValidationException;
import ru.clevertec.ecl.authservice.model.dto.request.UserDtoRequest;
import ru.clevertec.ecl.authservice.model.dto.response.APIResponse;
import ru.clevertec.ecl.authservice.model.dto.response.PageResponse;
import ru.clevertec.ecl.authservice.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.authservice.service.UserService;
import ru.clevertec.ecl.authservice.service.UserTokenService;

import static ru.clevertec.ecl.authservice.controller.UserController.USER_API_PATH;

/**
 * User API
 *
 * @author Konstantin Voytko
 */
@RestController
@Validated
@RequestMapping(value = USER_API_PATH)
@RequiredArgsConstructor
@Tag(name = "UserController", description = "User API")
public class UserController {

    public static final String USER_API_PATH = "/api/v0/users";

    private final UserService userService;
    private final UserTokenService userTokenService;

    /**
     * GET /api/v0/users : Find all User entities info
     *
     * @param pageable page number & page size values to find (not required)
     * @return all User DTOs
     */
    @Operation(summary = "Find all Users", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all Users")
    })
    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<UserDtoResponse>>> findAll(Pageable pageable) {
        PageResponse<UserDtoResponse> users = userService.findAll(pageable);

        return APIResponse.of(
                "All Users: page_number: " + pageable.getPageNumber() +
                        "; page_size: " + pageable.getPageSize(),
                USER_API_PATH,
                HttpStatus.OK,
                users
        );
    }

    /**
     * GET /api/v0/users/{id} : Find User entity info by ID
     *
     * @param id User ID to find (required)
     * @throws EntityNotFoundException if the User entity with ID doesn't exist
     * @return found User DTO by ID
     */
    @Operation(summary = "Find User by ID", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found User by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserDtoResponse>> findById(@PathVariable @NotNull @PositiveOrZero Long id) {
        UserDtoResponse user = userService.findById(id);

        return APIResponse.of(
                "User with ID " + user.getId() + " was found",
                USER_API_PATH + "/" + id,
                HttpStatus.OK,
                user
        );
    }

    /**
     * GET /api/v0/users/byUsername/{username} : Find User entity info by username
     *
     * @param username User username to find (required)
     * @throws EntityNotFoundException if the User entity with username doesn't exist
     * @return found User DTO by username
     */
    @Operation(summary = "Find User by Username", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found User by Username"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @GetMapping("/byUsername/{username}")
    public ResponseEntity<APIResponse<UserDtoResponse>> findByUsername(@PathVariable @NotBlank String username) {
        UserDtoResponse user = userService.findByUsername(username);

        return APIResponse.of(
                "User with username " + user.getUsername() + " was found",
                USER_API_PATH + "/byUsername/" + username,
                HttpStatus.OK,
                user
        );
    }

    /**
     * GET /api/v0/users/byToken/{token} : Find User entity info by JWT
     *
     * @param token JWT to find (required)
     * @throws TokenExpirationException if the User token was expired
     * @throws TokenValidationException if the User token is not valid
     * @throws EntityNotFoundException if the User entity with username doesn't exist
     * @return found User DTO by JWT
     */
    @Operation(summary = "Find User by token", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found User by Username"),
            @ApiResponse(responseCode = "401", description = "Token was expired", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Token is not valid", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @GetMapping("/byToken/{token}")
    public ResponseEntity<APIResponse<UserDtoResponse>> findByToken(@PathVariable @NotBlank String token) {
        UserDtoResponse user = userTokenService.findUserByToken(token);

        return APIResponse.of(
                "User with username " + user.getUsername() + " was found",
                USER_API_PATH + "/byToken",
                HttpStatus.OK,
                user
        );
    }

    /**
     * PUT /api/v0/users/{id} : Update an existing User entity info by ID
     *
     * @param id User ID to update (required)
     * @param userDtoRequest User DTO to update (required)
     * @throws EntityNotFoundException if the User entity with ID doesn't exist
     * @return updated User DTO by ID
     */
    @Operation(summary = "Update User by ID", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated User by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))}),
            @ApiResponse(responseCode = "409", description = "User with such name or email already exists", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<UserDtoResponse>> update(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody @Valid UserDtoRequest userDtoRequest) {
        UserDtoResponse user = userService.update(id, userDtoRequest);

        return APIResponse.of(
                "Changes were applied to the User with ID " + id,
                USER_API_PATH + "/" + id,
                HttpStatus.OK,
                user
        );
    }

    /**
     * PATCH /api/v0/users/{id} : Partial Update an existing User entity info by ID
     *
     * @param id User ID to partial update (required)
     * @param userDtoRequest User DTO to partial update (required)
     * @throws EntityNotFoundException if User entity with ID doesn't exist
     * @return partial updated User DTO by ID
     */
    @Operation(summary = "Partial Update User by ID", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partial Updated User by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))}),
            @ApiResponse(responseCode = "409", description = "User with such name or email already exists", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PatchMapping("/{id}")
    public ResponseEntity<APIResponse<UserDtoResponse>> updatePartially(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody UserDtoRequest userDtoRequest) {
        UserDtoResponse user = userService.update(id, userDtoRequest);

        return APIResponse.of(
                "Partial changes were applied to the User with ID " + id,
                USER_API_PATH + "/" + id,
                HttpStatus.OK,
                user
        );
    }

    /**
     * PATCH /api/v0/users/{id} : Block a User entity by ID
     *
     * @param id User ID to block (required)
     * @throws EntityNotFoundException if the User entity with ID doesn't exist
     * @return blocked User DTO by ID
     */
    @Operation(summary = "Block User by ID", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blocked User by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PatchMapping("/block/{id}")
    public ResponseEntity<APIResponse<UserDtoResponse>> blockById(@PathVariable @NotNull @PositiveOrZero Long id) {
        UserDtoResponse user = userService.block(id);

        return APIResponse.of(
                "User with ID " + id + " was blocked",
                USER_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                user
        );
    }

    /**
     * PATCH /api/v0/users/{id} : Unblock a User entity by ID
     *
     * @param id User ID to unblock (required)
     * @throws EntityNotFoundException if the User entity with ID doesn't exist
     * @return unblocked User DTO by ID
     */
    @Operation(summary = "Unblock User by ID", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unblocked User by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PatchMapping("/unblock/{id}")
    public ResponseEntity<APIResponse<UserDtoResponse>> unblockById(@PathVariable @NotNull @PositiveOrZero Long id) {
        UserDtoResponse user = userService.unblock(id);

        return APIResponse.of(
                "User with ID " + id + " was unblocked",
                USER_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                user
        );
    }

    /**
     * DELETE /api/v0/users/{id} : Delete a User entity by ID
     *
     * @param id User ID to delete (required)
     * @throws EntityNotFoundException if the User entity with ID doesn't exist
     * @return deleted User DTO by ID
     */
    @Operation(summary = "Delete User by ID", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted User by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<UserDtoResponse>> deleteById(@PathVariable @NotNull @PositiveOrZero Long id) {
        UserDtoResponse user = userService.deleteById(id);

        return APIResponse.of(
                "User with ID " + id + " was deleted",
                USER_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                user
        );
    }
}
