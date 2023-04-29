package ru.clevertec.ecl.newsservice.controller;

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
import ru.clevertec.ecl.newsservice.model.dto.response.ApiResponse;
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
public class CommentController {

    private final CommentService commentService;

    public static final String COMMENT_API_PATH = "/v0/comments";

    /**
     * POST /api/v0/comments : Create a new Comment
     *
     * @param newsId News ID to return (required)
     * @param commentDtoRequest Comment object to create (required)
     */
    @PostMapping("/{newsId}")
    public ResponseEntity<ApiResponse<CommentDtoResponse>> saveByNewsId(
            @PathVariable @NotNull @PositiveOrZero Long newsId,
            @RequestBody @Valid CommentDtoRequest commentDtoRequest) {
        CommentDtoResponse comment = commentService.save(newsId, commentDtoRequest);

        return ApiResponse.of(
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
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CommentDtoResponse>>> findAll(Pageable pageable) {
        PageResponse<CommentDtoResponse> comments = commentService.findAll(pageable);

        return ApiResponse.of(
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
    @GetMapping("/criteria")
    public ResponseEntity<ApiResponse<PageResponse<CommentDtoResponse>>> findAllByCriteria(
            @RequestBody(required = false) CommentCriteria searchCriteria,
            Pageable pageable) {
        searchCriteria = Objects.requireNonNullElse(searchCriteria, CommentCriteria.builder().build());
        PageResponse<CommentDtoResponse> comments = commentService.findAllByCriteria(searchCriteria, pageable);

        return ApiResponse.of(
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
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDtoResponse>> findById(@PathVariable @NotNull @PositiveOrZero Long id) {
        CommentDtoResponse comment = commentService.findById(id);

        return ApiResponse.of(
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
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDtoResponse>> update(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody @Valid CommentDtoRequest commentDtoRequest) {
        CommentDtoResponse comment = commentService.update(id, commentDtoRequest);

        return ApiResponse.of(
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
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDtoResponse>> updatePartially(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody CommentDtoRequest commentDtoRequest) {
        CommentDtoResponse comment = commentService.update(id, commentDtoRequest);

        return ApiResponse.of(
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable @NotNull @PositiveOrZero Long id) {
        commentService.deleteById(id);

        return ApiResponse.of(
                "Comment with ID " + id + " was deleted",
                COMMENT_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                null
        );
    }
}
