package ru.clevertec.ecl.newsservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import ru.clevertec.ecl.newsservice.builder.criteria.NewsCriteriaTestBuilder;
import ru.clevertec.ecl.newsservice.builder.news.NewsDtoRequestTestBuilder;
import ru.clevertec.ecl.newsservice.builder.news.NewsDtoResponseTestBuilder;
import ru.clevertec.ecl.newsservice.exception.EntityNotFoundException;
import ru.clevertec.ecl.newsservice.model.criteria.NewsCriteria;
import ru.clevertec.ecl.newsservice.model.dto.request.NewsDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.NewsDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;
import ru.clevertec.ecl.newsservice.service.NewsService;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_BEARER;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE_SIZE;

@ExtendWith(MockitoExtension.class)
public class NewsControllerTest {

    @InjectMocks
    private NewsController newsController;

    @Mock
    private NewsService newsService;

    @Captor
    ArgumentCaptor<NewsDtoRequest> newsDtoRequestCaptor;

    private final NewsDtoRequest newsDtoRequest = NewsDtoRequestTestBuilder.aNewsDtoRequest().build();
    private final NewsDtoResponse expectedNewsDtoResponse = NewsDtoResponseTestBuilder.aNewsDtoResponse().build();
    private final NewsCriteria searchCriteria = NewsCriteriaTestBuilder.aNewsCriteria().build();
    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);

    @BeforeEach
    void setUp() {
        newsController = new NewsController(newsService);
    }

    @Nested
    public class SaveTest {
        @Test
        @DisplayName("Save News")
        void checkSaveShouldReturnNewsDtoResponse() {
            doReturn(expectedNewsDtoResponse).when(newsService).save(newsDtoRequest, TEST_BEARER);

            var actualNews = newsController.save(TEST_BEARER, newsDtoRequest);

            verify(newsService).save(any(), anyString());

            assertAll(
                    () -> assertThat(actualNews.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                    () -> assertThat(Objects.requireNonNull(actualNews.getBody()).getData())
                            .isEqualTo(expectedNewsDtoResponse)
            );
        }

        @Test
        @DisplayName("Save News with Argument Captor")
        void checkSaveWithArgumentCaptorShouldReturnNewsDtoResponse() {
            doReturn(expectedNewsDtoResponse).when(newsService).save(newsDtoRequest, TEST_BEARER);

            newsController.save(TEST_BEARER, newsDtoRequest);

            verify(newsService).save(newsDtoRequestCaptor.capture(), anyString());

            assertThat(newsDtoRequestCaptor.getValue()).isEqualTo(newsDtoRequest);
        }
    }

    @Test
    @DisplayName("Find all News")
    void checkFindAllShouldReturnNewsPage() {
        PageResponse<NewsDtoResponse> pageResponse = PageResponse.<NewsDtoResponse>builder()
                .content(List.of(expectedNewsDtoResponse))
                .number(TEST_PAGE)
                .size(TEST_PAGE_SIZE)
                .numberOfElements(1)
                .build();

        doReturn(pageResponse).when(newsService).findAll(pageable);

        var actualNews = newsController.findAll(pageable);

        verify(newsService).findAll(any());

        assertAll(
                () -> assertThat(actualNews.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(actualNews.getBody()).getData().getContent().stream()
                        .anyMatch(actualNewsDtoResponse -> actualNewsDtoResponse.equals(expectedNewsDtoResponse))
                ).isTrue()
        );
    }

    @Test
    @DisplayName("Find all News by criteria")
    void checkFindAllByCriteriaShouldReturnNewsDtoResponsePage() {
        PageResponse<NewsDtoResponse> pageResponse = PageResponse.<NewsDtoResponse>builder()
                .content(List.of(expectedNewsDtoResponse))
                .number(TEST_PAGE)
                .size(TEST_PAGE_SIZE)
                .numberOfElements(1)
                .build();

        doReturn(pageResponse).when(newsService).findAllByCriteria(searchCriteria, pageable);

        var actualNews = newsController.findAllByCriteria(searchCriteria, pageable);

        verify(newsService).findAllByCriteria(any(), any());

        assertAll(
                () -> assertThat(actualNews.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(actualNews.getBody()).getData().getContent().stream()
                        .anyMatch(actualNewsDtoResponse -> actualNewsDtoResponse.equals(expectedNewsDtoResponse))
                ).isTrue()
        );
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find News by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnNewsDtoResponse(Long id) {
            doReturn(expectedNewsDtoResponse).when(newsService).findById(id, pageable);

            var actualNews = newsController.findById(id, pageable);

            verify(newsService).findById(anyLong(), any());

            assertAll(
                    () -> assertThat(actualNews.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualNews.getBody()).getData())
                            .isEqualTo(expectedNewsDtoResponse)
            );
        }

        @Test
        @DisplayName("Find by ID; not found")
        void checkFindByIdShouldThrowNewsNotFoundException() {
            doThrow(EntityNotFoundException.class).when(newsService).findById(anyLong(), any());

            assertThrows(EntityNotFoundException.class, () -> newsController.findById(TEST_ID, pageable));

            verify(newsService).findById(anyLong(), any());
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update News by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnNewsDtoResponse(Long id) {
            doReturn(expectedNewsDtoResponse).when(newsService).update(id, newsDtoRequest, TEST_BEARER);

            var actualNews = newsController.update(TEST_BEARER, id, newsDtoRequest);

            verify(newsService).update(anyLong(), any(), anyString());

            assertAll(
                    () -> assertThat(actualNews.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualNews.getBody()).getData())
                            .isEqualTo(expectedNewsDtoResponse)
            );
        }

        @DisplayName("Update News by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateWithArgumentCaptorShouldReturnNewsDtoResponse(Long id) {
            doReturn(expectedNewsDtoResponse).when(newsService).update(id, newsDtoRequest, TEST_BEARER);

            newsController.update(TEST_BEARER, id, newsDtoRequest);

            verify(newsService).update(anyLong(), newsDtoRequestCaptor.capture(), anyString());

            assertThat(newsDtoRequestCaptor.getValue()).isEqualTo(newsDtoRequest);
        }

        @Test
        @DisplayName("Update News by ID; not found")
        void checkUpdateShouldThrowNewsNotFoundException() {
            doThrow(EntityNotFoundException.class).when(newsService).update(anyLong(), any(), anyString());

            assertThrows(EntityNotFoundException.class,
                    () -> newsController.update(TEST_BEARER, TEST_ID, newsDtoRequest)
            );

            verify(newsService).update(anyLong(), any(), anyString());
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete News by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnVoid(Long id) {
            doNothing().when(newsService).deleteById(id, TEST_BEARER);

            var voidResponse = newsController.deleteById(TEST_BEARER, id);

            verify(newsService).deleteById(anyLong(), anyString());

            assertThat(voidResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Delete News by ID; not found")
        void checkDeleteByIdShouldThrowNewsNotFoundException() {
            doThrow(EntityNotFoundException.class).when(newsService).deleteById(anyLong(), anyString());

            assertThrows(EntityNotFoundException.class, () -> newsController.deleteById(TEST_BEARER, TEST_ID));

            verify(newsService).deleteById(anyLong(), anyString());
        }
    }
}
