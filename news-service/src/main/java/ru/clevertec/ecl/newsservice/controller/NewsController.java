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
import ru.clevertec.ecl.newsservice.exception.EntityNotFoundException;
import ru.clevertec.ecl.newsservice.model.dto.request.NewsDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.ApiResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.NewsDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;
import ru.clevertec.ecl.newsservice.service.NewsService;

import static ru.clevertec.ecl.newsservice.controller.NewsController.NEWS_API_PATH;

/**
 * News API
 *
 * @author Konstantin Voytko
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = NEWS_API_PATH)
public class NewsController {

    public static final String NEWS_API_PATH = "/v0/news";

    private final NewsService newsService;

    /**
     * POST /api/v0/news : Create a new News
     *
     * @param newsDtoRequest News object to create (required)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<NewsDtoResponse>> save(@RequestBody @Valid NewsDtoRequest newsDtoRequest) {
        NewsDtoResponse news = newsService.save(newsDtoRequest);

        return ApiResponse.of(
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
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NewsDtoResponse>>> findAll(Pageable pageable) {
        PageResponse<NewsDtoResponse> news = newsService.findAll(pageable);

        return ApiResponse.of(
                "All News: page_number: " + pageable.getPageNumber() +
                        "; page_size: " + pageable.getPageSize(),
                NEWS_API_PATH,
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
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsDtoResponse>> findById(
            @PathVariable @NotNull @PositiveOrZero Long id,
            Pageable pageable) {
        NewsDtoResponse news = newsService.findById(id, pageable);

        return ApiResponse.of(
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
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsDtoResponse>> update(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody @Valid NewsDtoRequest newsDtoRequest) {
        NewsDtoResponse news = newsService.update(id, newsDtoRequest);

        return ApiResponse.of(
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
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsDtoResponse>> updatePartially(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody NewsDtoRequest newsDtoRequest) {
        NewsDtoResponse news = newsService.update(id, newsDtoRequest);

        return ApiResponse.of(
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable @NotNull @PositiveOrZero Long id) {
        newsService.deleteById(id);

        return ApiResponse.of(
                "News with ID " + id + " were deleted",
                NEWS_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                null
        );
    }
}
