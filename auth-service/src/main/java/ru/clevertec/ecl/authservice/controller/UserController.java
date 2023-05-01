package ru.clevertec.ecl.authservice.controller;

import jakarta.validation.Valid;
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
import ru.clevertec.ecl.authservice.model.dto.request.UserDtoRequest;
import ru.clevertec.ecl.authservice.model.dto.response.APIResponse;
import ru.clevertec.ecl.authservice.model.dto.response.PageResponse;
import ru.clevertec.ecl.authservice.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.authservice.service.UserService;

import static ru.clevertec.ecl.authservice.controller.UserController.USER_API_PATH;

@RestController
@Validated
@RequestMapping(value = USER_API_PATH)
@RequiredArgsConstructor
public class UserController {

    public static final String USER_API_PATH = "/v0/users";

    private final UserService userService;

    /**
     * GET /api/v0/users : Find Users info
     *
     * @param pageable page number & page size values to return (not required)
     */
    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<UserDtoResponse>>> findAll(Pageable pageable) {
        PageResponse<UserDtoResponse> users = userService.getAll(pageable);

        return APIResponse.of(
                "All Users: page_number: " + pageable.getPageNumber() +
                        "; page_size: " + pageable.getPageSize(),
                USER_API_PATH,
                HttpStatus.OK,
                users
        );
    }

    /**
     * GET /api/v0/users/{id} : Find User info
     *
     * @param id User ID to return (required)
     * @throws EntityNotFoundException if the User with ID doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserDtoResponse>> findById(@PathVariable @NotNull @PositiveOrZero Long id) {
        UserDtoResponse user = userService.getById(id);

        return APIResponse.of(
                "User with ID " + user.getId() + " was found",
                USER_API_PATH + "/" + id,
                HttpStatus.OK,
                user
        );
    }

    /**
     * GET /api/v0/users/byUsername/{username} : Find User info
     *
     * @param username User username to return (required)
     * @throws EntityNotFoundException if the User with username doesn't exist
     */
    @GetMapping("/byUsername/{username}")
    public ResponseEntity<APIResponse<UserDtoResponse>> findById(@PathVariable @NotNull String username) {
        UserDtoResponse user = userService.getByUsername(username);

        return APIResponse.of(
                "User with username " + user.getUsername() + " was found",
                USER_API_PATH + "/" + username,
                HttpStatus.OK,
                user
        );
    }

    /**
     * PUT /api/v0/users/{id} : Update an existing User info
     *
     * @param id User ID to return (required)
     * @param userDtoRequest User object to update (required)
     * @throws EntityNotFoundException if the User with ID doesn't exist
     */
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
     * PATCH /api/v0/users/{id} : Partial Update an existing User info
     *
     * @param id User ID to return (required)
     * @param userDtoRequest User object to update (required)
     * @throws EntityNotFoundException if User with ID doesn't exist
     */
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
     * PATCH /api/v0/users/{id} : Block a User
     *
     * @param id User ID to return (required)
     * @throws EntityNotFoundException if the User with ID doesn't exist
     */
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
     * PATCH /api/v0/users/{id} : Unblock a User
     *
     * @param id User ID to return (required)
     * @throws EntityNotFoundException if the User with ID doesn't exist
     */
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
     * DELETE /api/v0/users/{id} : Delete a User
     *
     * @param id User ID to return (required)
     * @throws EntityNotFoundException if the User with ID doesn't exist
     */
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
