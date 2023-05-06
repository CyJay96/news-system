package ru.clevertec.ecl.newsservice.builder.news;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.newsservice.builder.TestBuilder;
import ru.clevertec.ecl.newsservice.model.entity.Comment;
import ru.clevertec.ecl.newsservice.model.entity.News;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_DATE;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aNews")
public class NewsTestBuilder implements TestBuilder<News> {

    private Long id = TEST_ID;

    private OffsetDateTime time = TEST_DATE;

    private String title = TEST_STRING;

    private String text = TEST_STRING;

    private List<Comment> comments = new ArrayList<>();

    @Override
    public News build() {
        return News.builder()
                .id(id)
                .time(time)
                .title(title)
                .text(text)
                .comments(comments)
                .build();
    }
}
