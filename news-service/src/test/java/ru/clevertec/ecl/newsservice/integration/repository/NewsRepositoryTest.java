package ru.clevertec.ecl.newsservice.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.newsservice.integration.BaseIntegrationTest;
import ru.clevertec.ecl.newsservice.model.entity.News;
import ru.clevertec.ecl.newsservice.repository.NewsRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class NewsRepositoryTest extends BaseIntegrationTest {

    private final NewsRepository newsRepository;

    @Nested
    public class FindFirstByOrderByIdAscTest {
        @Test
        @DisplayName("Find first News order by ID ASC")
        void checkFindFirstByOrderByIdAscShouldReturnNotEmptyNewsOptional() {
            List<News> news = newsRepository.findAll();

            News expectedNews = news.stream()
                    .sorted(Comparator.comparing(News::getId))
                    .limit(1)
                    .toList()
                    .get(0);

            News actualNews = newsRepository.findFirstByOrderByIdAsc().get();

            assertThat(actualNews.getId()).isEqualTo(expectedNews.getId());
        }

        @Test
        @DisplayName("Find first News order by ID ASC; not found")
        void checkFindFirstByOrderByIdAscShouldReturnNewsOptional() {
            newsRepository.deleteAll();
            Optional<News> actualNewsOptional = newsRepository.findFirstByOrderByIdAsc();
            assertThat(actualNewsOptional.isEmpty()).isTrue();
        }
    }

    @Nested
    public class FindFirstByOrderByIdDescTest {
        @Test
        @DisplayName("Find first Order order by ID DESC")
        void checkFindFirstByOrderByIdAscShouldReturnNotEmptyOrderOptional() {
            List<News> news = newsRepository.findAll();

            News expectedNews = news.stream()
                    .sorted(Comparator.comparing(News::getId).reversed())
                    .limit(1)
                    .toList()
                    .get(0);

            News actualNews = newsRepository.findFirstByOrderByIdDesc().get();

            assertThat(actualNews.getId()).isEqualTo(expectedNews.getId());
        }

        @Test
        @DisplayName("Find first News order by ID DESC; not found")
        void checkFindFirstByOrderByIdDescShouldReturnEmptyNewsOptional() {
            newsRepository.deleteAll();
            Optional<News> actualNewsOptional = newsRepository.findFirstByOrderByIdDesc();
            assertThat(actualNewsOptional.isEmpty()).isTrue();
        }
    }
}
