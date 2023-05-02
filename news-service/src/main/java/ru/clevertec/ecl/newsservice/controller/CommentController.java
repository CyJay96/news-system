package ru.clevertec.ecl.newsservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.newsservice.aop.annotation.Log;
import ru.clevertec.ecl.newsservice.exception.EntityNotFoundException;
import ru.clevertec.ecl.newsservice.model.criteria.CommentCriteria;
import ru.clevertec.ecl.newsservice.model.dto.request.CommentDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.APIResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.CommentDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;
import ru.clevertec.ecl.newsservice.service.CommentService;

import java.util.Objects;

import static ru.clevertec.ecl.newsservice.controller.CommentController.COMMENT_API_PATH;

/**
 * Comment API
 *
 * @author Konstantin Voytko
 */
@Log
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = COMMENT_API_PATH)
@Tag(name = "CommentController", description = "Comment API")
public class CommentController {

    private final CommentService commentService;

    public static final String COMMENT_API_PATH = "/api/v0/comments";

    /**
     * POST /api/v0/comments : Create a new Comment
     *
     * @param newsId News ID to return (required)
     * @param commentDtoRequest Comment object to create (required)
     */
    @Operation(summary = "Save Comment", tags = "CommentController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Saved Comment")
    })
    @PostMapping("/{newsId}")
    public ResponseEntity<APIResponse<CommentDtoResponse>> saveByNewsId(
            @PathVariable @NotNull @PositiveOrZero Long newsId,
            @RequestBody @Valid CommentDtoRequest commentDtoRequest) {
        CommentDtoResponse comment = commentService.save(newsId, commentDtoRequest);

        return APIResponse.of(
                "Comment with ID " + comment.getId() + " was created",
                COMMENT_API_PATH,
                HttpStatus.CREATED,
                comment
        );
    }

    /**
     * GET /api/v0/comments : Find Comments info
     *
     * @param pageable page number & page size values to return (not required)
     */
    @Operation(summary = "Find all Comments", tags = "CommentController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all Comments")
    })
    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<CommentDtoResponse>>> findAll(Pageable pageable) {
        PageResponse<CommentDtoResponse> comments = commentService.findAll(pageable);

        return APIResponse.of(
                "All Comments: page_number: " + pageable.getPageNumber() +
                        "; page_size: " + pageable.getPageSize(),
                COMMENT_API_PATH,
                HttpStatus.OK,
                comments
        );
    }

    /**
     * GET /api/v0/comments : Find Comments info by criteria
     *
     * @param searchCriteria Comments searchCriteria to return (not required)
     * @param pageable page number & page size values to return (not required)
     */
    @Operation(summary = "Find all Comments by Criteria", tags = "CommentController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all Comments by Criteria")
    })
    @GetMapping("/criteria")
    public ResponseEntity<APIResponse<PageResponse<CommentDtoResponse>>> findAllByCriteria(
            @RequestBody(required = false) CommentCriteria searchCriteria,
            Pageable pageable) {
        searchCriteria = Objects.requireNonNullElse(searchCriteria, CommentCriteria.builder().build());
        PageResponse<CommentDtoResponse> comments = commentService.findAllByCriteria(searchCriteria, pageable);

        return APIResponse.of(
                "Comments by criteria: text: " + searchCriteria.getText() +
                        "; username: " + searchCriteria.getUsername() +
                        "; page_number: " + pageable.getPageNumber() +
                        "; page_size: " + pageable.getPageSize(),
                COMMENT_API_PATH + "/criteria",
                HttpStatus.OK,
                comments
        );
    }

    /**
     * GET /api/v0/comments/{id} : Find Comment info
     *
     * @param id Comment ID to return (required)
     * @throws EntityNotFoundException if the Comment with ID doesn't exist
     */
    @Operation(summary = "Find Comment by ID", tags = "CommentController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Comment by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CommentDtoResponse>> findById(@PathVariable @NotNull @PositiveOrZero Long id) {
        CommentDtoResponse comment = commentService.findById(id);

        return APIResponse.of(
                "Comment with ID " + comment.getId() + " was found",
                COMMENT_API_PATH + "/" + id,
                HttpStatus.OK,
                comment
        );
    }

    /**
     * PUT /api/v0/comments/{id} : Update an existing Comment info
     *
     * @param id Comment ID to return (required)
     * @param commentDtoRequest Comment object to update (required)
     * @throws EntityNotFoundException if the Comment with ID doesn't exist
     */
    @Operation(summary = "Update Comment by ID", tags = "CommentController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Comment by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<CommentDtoResponse>> update(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody @Valid CommentDtoRequest commentDtoRequest) {
        CommentDtoResponse comment = commentService.update(id, commentDtoRequest);

        return APIResponse.of(
                "Changes were applied to the Comment with ID " + id,
                COMMENT_API_PATH + "/" + id,
                HttpStatus.OK,
                comment
        );
    }

    /**
     * PATCH /api/v0/comments/{id} : Partial Update an existing Comment info
     *
     * @param id Comment ID to return (required)
     * @param commentDtoRequest Comment object to update (required)
     * @throws EntityNotFoundException if Comment with ID doesn't exist
     */
    @Operation(summary = "Partial Update Comment by ID", tags = "CommentController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partial Updated Comment by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PatchMapping("/{id}")
    public ResponseEntity<APIResponse<CommentDtoResponse>> updatePartially(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody CommentDtoRequest commentDtoRequest) {
        CommentDtoResponse comment = commentService.update(id, commentDtoRequest);

        return APIResponse.of(
                "Partial changes were applied to the Comment with ID " + id,
                COMMENT_API_PATH + "/" + id,
                HttpStatus.OK,
                comment
        );
    }

    /**
     * DELETE /api/v0/comments/{id} : Delete a Comment
     *
     * @param id Comment ID to return (required)
     * @throws EntityNotFoundException if the Comment with ID doesn't exist
     */
    @Operation(summary = "Delete Comment by ID", tags = "CommentController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted Comment by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteById(@PathVariable @NotNull @PositiveOrZero Long id) {
        commentService.deleteById(id);

        return APIResponse.of(
                "Comment with ID " + id + " was deleted",
                COMMENT_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                null
        );
    }
}
