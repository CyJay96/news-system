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
import ru.clevertec.ecl.newsservice.model.criteria.NewsCriteria;
import ru.clevertec.ecl.newsservice.model.dto.request.NewsDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.APIResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.NewsDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;
import ru.clevertec.ecl.newsservice.service.NewsService;

import java.util.Objects;

import static ru.clevertec.ecl.newsservice.controller.NewsController.NEWS_API_PATH;

/**
 * News API
 *
 * @author Konstantin Voytko
 */
@Log
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = NEWS_API_PATH)
@Tag(name = "NewsController", description = "News API")
public class NewsController {

    public static final String NEWS_API_PATH = "/api/v0/news";

    private final NewsService newsService;

    /**
     * POST /api/v0/news : Create a new News
     *
     * @param newsDtoRequest News object to create (required)
     */
    @Operation(summary = "Save News", tags = "NewsController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Saved News")
    })
    @PostMapping
    public ResponseEntity<APIResponse<NewsDtoResponse>> save(@RequestBody @Valid NewsDtoRequest newsDtoRequest) {
        NewsDtoResponse news = newsService.save(newsDtoRequest);

        return APIResponse.of(
                "News with ID " + news.getId() + " were created",
                NEWS_API_PATH,
                HttpStatus.CREATED,
                news
        );
    }

    /**
     * GET /api/v0/news : Find News info
     *
     * @param pageable page number & page size values to return (not required)
     */
    @Operation(summary = "Find all News", tags = "NewsController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all News")
    })
    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<NewsDtoResponse>>> findAll(Pageable pageable) {
        PageResponse<NewsDtoResponse> news = newsService.findAll(pageable);

        return APIResponse.of(
                "All News: page_number: " + pageable.getPageNumber() +
                        "; page_size: " + pageable.getPageSize(),
                NEWS_API_PATH,
                HttpStatus.OK,
                news
        );
    }

    /**
     * GET /api/v0/news : Find News info by criteria
     *
     * @param searchCriteria News searchCriteria to return (not required)
     * @param pageable page number & page size values to return (not required)
     */
    @Operation(summary = "Find all News by Criteria", tags = "NewsController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all News by Criteria")
    })
    @GetMapping("/criteria")
    public ResponseEntity<APIResponse<PageResponse<NewsDtoResponse>>> findAllByCriteria(
            @RequestBody(required = false) NewsCriteria searchCriteria,
            Pageable pageable) {
        searchCriteria = Objects.requireNonNullElse(searchCriteria, NewsCriteria.builder().build());
        PageResponse<NewsDtoResponse> news = newsService.findAllByCriteria(searchCriteria, pageable);

        return APIResponse.of(
                "News by criteria: title: " + searchCriteria.getTitle() +
                        "; text: " + searchCriteria.getText() +
                        "; page_number: " + pageable.getPageNumber() +
                        "; page_size: " + pageable.getPageSize(),
                NEWS_API_PATH + "/criteria",
                HttpStatus.OK,
                news
        );
    }

    /**
     * GET /api/v0/news/{id} : Find News info
     *
     * @param id News ID to return (required)
     * @param pageable page number & page size values to return (not required)
     * @throws EntityNotFoundException if the News with ID don't exist
     */
    @Operation(summary = "Find News by ID", tags = "NewsController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found News by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<NewsDtoResponse>> findById(
            @PathVariable @NotNull @PositiveOrZero Long id,
            Pageable pageable) {
        NewsDtoResponse news = newsService.findById(id, pageable);

        return APIResponse.of(
                "News with ID " + news.getId() + " were found: page_number: " +
                        pageable.getPageNumber() + "; page_size: " + pageable.getPageSize(),
                NEWS_API_PATH + "/" + id,
                HttpStatus.OK,
                news
        );
    }

    /**
     * PUT /api/v0/news/{id} : Update an existing News info
     *
     * @param id News ID to return (required)
     * @param newsDtoRequest News object to update (required)
     * @throws EntityNotFoundException if the News with ID don't exist
     */
    @Operation(summary = "Update News by ID", tags = "NewsController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated News by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<NewsDtoResponse>> update(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody @Valid NewsDtoRequest newsDtoRequest) {
        NewsDtoResponse news = newsService.update(id, newsDtoRequest);

        return APIResponse.of(
                "Changes were applied to the News with ID " + id,
                NEWS_API_PATH + "/" + id,
                HttpStatus.OK,
                news
        );
    }

    /**
     * PATCH /api/v0/news/{id} : Partial Update an existing News info
     *
     * @param id News ID to return (required)
     * @param newsDtoRequest News object to update (required)
     * @throws EntityNotFoundException if News with ID don't exist
     */
    @Operation(summary = "Partial Update News by ID", tags = "NewsController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partial Updated News by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PatchMapping("/{id}")
    public ResponseEntity<APIResponse<NewsDtoResponse>> updatePartially(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody NewsDtoRequest newsDtoRequest) {
        NewsDtoResponse news = newsService.update(id, newsDtoRequest);

        return APIResponse.of(
                "Partial changes were applied to the News with ID " + id,
                NEWS_API_PATH + "/" + id,
                HttpStatus.OK,
                news
        );
    }

    /**
     * DELETE /api/v0/news/{id} : Delete a News
     *
     * @param id News ID to return (required)
     * @throws EntityNotFoundException if the News with ID don't exist
     */
    @Operation(summary = "Delete News by ID", tags = "NewsController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted News by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteById(@PathVariable @NotNull @PositiveOrZero Long id) {
        newsService.deleteById(id);

        return APIResponse.of(
                "News with ID " + id + " were deleted",
                NEWS_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                null
        );
    }
}
