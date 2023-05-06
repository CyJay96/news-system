package ru.clevertec.ecl.newsservice.builder.comment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.newsservice.builder.TestBuilder;
import ru.clevertec.ecl.newsservice.builder.news.NewsTestBuilder;
import ru.clevertec.ecl.newsservice.model.entity.Comment;
import ru.clevertec.ecl.newsservice.model.entity.News;

import java.time.OffsetDateTime;

import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_DATE;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aComment")
public class CommentTestBuilder implements TestBuilder<Comment> {

    private Long id = TEST_ID;

    private OffsetDateTime time = TEST_DATE;

    private String text = TEST_STRING;

    private String username = TEST_STRING;

    private News news = NewsTestBuilder.aNews().build();

    @Override
    public Comment build() {
        return Comment.builder()
                .id(id)
                .time(time)
                .text(text)
                .username(username)
                .news(news)
                .build();
    }
}
