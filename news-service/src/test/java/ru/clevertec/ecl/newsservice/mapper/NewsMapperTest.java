package ru.clevertec.ecl.newsservice.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.newsservice.builder.news.NewsDtoRequestTestBuilder;
import ru.clevertec.ecl.newsservice.builder.news.NewsDtoResponseTestBuilder;
import ru.clevertec.ecl.newsservice.builder.news.NewsTestBuilder;
import ru.clevertec.ecl.newsservice.model.dto.request.NewsDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.NewsDtoResponse;
import ru.clevertec.ecl.newsservice.model.entity.News;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class NewsMapperTest {

    private NewsMapper newsMapper;

    private final NewsDtoRequest expectedNewsDtoRequest = NewsDtoRequestTestBuilder.aNewsDtoRequest().build();
    private final NewsDtoResponse expectedNewsDtoResponse = NewsDtoResponseTestBuilder.aNewsDtoResponse().build();
    private final News expectedNews = NewsTestBuilder.aNews().build();

    @BeforeEach
    void setUp() {
        newsMapper = Mappers.getMapper(NewsMapper.class);
    }

    @Test
    @DisplayName("Map News DTO to Entity")
    void checkToNewsShouldReturnNews() {
        News actualNews = newsMapper.toNews(expectedNewsDtoRequest);
        assertThat(actualNews).isEqualTo(expectedNews);
    }

    @Test
    @DisplayName("Map News Entity to DTO")
    void checkToNewsDtoResponseShouldReturnNewsDtoResponse() {
        NewsDtoResponse actualNewsDtoResponse = newsMapper.toNewsDtoResponse(expectedNews);
        assertAll(
                () -> assertThat(actualNewsDtoResponse.getId()).isEqualTo(expectedNewsDtoResponse.getId()),
                () -> assertThat(actualNewsDtoResponse.getTime()).isEqualTo(expectedNewsDtoResponse.getTime()),
                () -> assertThat(actualNewsDtoResponse.getTitle()).isEqualTo(expectedNewsDtoResponse.getTitle()),
                () -> assertThat(actualNewsDtoResponse.getText()).isEqualTo(expectedNewsDtoResponse.getText())
        );
    }
}
