package ru.clevertec.ecl.newsservice.integration.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.newsservice.builder.news.NewsDtoRequestTestBuilder;
import ru.clevertec.ecl.newsservice.exception.EntityNotFoundException;
import ru.clevertec.ecl.newsservice.integration.BaseIntegrationTest;
import ru.clevertec.ecl.newsservice.model.criteria.NewsCriteria;
import ru.clevertec.ecl.newsservice.model.dto.request.NewsDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.NewsDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;
import ru.clevertec.ecl.newsservice.repository.NewsRepository;
import ru.clevertec.ecl.newsservice.service.NewsService;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_BEARER;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE_SIZE;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class NewsServiceTest extends BaseIntegrationTest {

    private final NewsService newsService;
    private final NewsRepository newsRepository;
    private final EntityManager entityManager;

    private final NewsDtoRequest newsDtoRequest = NewsDtoRequestTestBuilder.aNewsDtoRequest().build();
    private final NewsCriteria searchCriteria = NewsCriteria.builder()
            .text("apple")
            .build();
    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);

    @Test
    @DisplayName("Save News")
    void checkSaveShouldReturnNewsDtoResponse() {
        NewsDtoResponse actualNews = newsService.save(newsDtoRequest, TEST_BEARER);
        assertThat(actualNews.getTitle()).isEqualTo(newsDtoRequest.getTitle());
    }

    @Test
    @DisplayName("Find all News")
    void checkFindAllShouldReturnNewsDtoResponsePage() {
        int expectedNewsSize = (int) newsRepository.count();
        PageResponse<NewsDtoResponse> actualNews = newsService.findAll(pageable);
        assertThat(actualNews.getContent()).hasSize(expectedNewsSize);
    }

    @Test
    @DisplayName("Find all News by criteria")
    void checkFindAllByCriteriaShouldReturnNewsDtoResponsePage() {
        int expectedNewsSize = 1;
        PageResponse<NewsDtoResponse> actualNews = newsService.findAllByCriteria(searchCriteria, pageable);
        assertThat(actualNews.getContent()).hasSize(expectedNewsSize);
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find News by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnNewsDtoResponse(Long id) {
            NewsDtoResponse actualNews = newsService.findById(id, pageable);
            assertThat(actualNews.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Find News by ID; not found")
        void checkFindByIdShouldThrowNewsNotFoundException() {
            Long doesntExistNewsId = new Random()
                    .nextLong(newsRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> newsService.findById(doesntExistNewsId, pageable));
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update News by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnNewsDtoResponse(Long id) {
            NewsDtoResponse actualNews = newsService.update(id, newsDtoRequest, TEST_BEARER);
            assertThat(actualNews.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Update News by ID; not found")
        void checkUpdateShouldThrowNewsNotFoundException() {
            Long doesntExistNewsId = new Random()
                    .nextLong(newsRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class,
                    () -> newsService.update(doesntExistNewsId, newsDtoRequest, TEST_BEARER)
            );
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete News by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnVoid(Long id) {
            newsService.deleteById(id, TEST_BEARER);
            entityManager.flush();
        }

        @Test
        @DisplayName("Delete News by ID; not found")
        void checkDeleteByIdShouldThrowNewsNotFoundException() {
            Long doesntExistNewsId = new Random()
                    .nextLong(newsRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> {
                newsService.deleteById(doesntExistNewsId, TEST_BEARER);
                entityManager.flush();
            });
        }
    }
}
