package ru.clevertec.ecl.newsservice.builder.comment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.newsservice.builder.TestBuilder;
import ru.clevertec.ecl.newsservice.model.dto.response.CommentDtoResponse;

import java.time.OffsetDateTime;

import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_DATE;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_NUMBER;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentDtoResponse")
public class CommentDtoResponseTestBuilder implements TestBuilder<CommentDtoResponse> {

    private Long id = TEST_ID;

    private OffsetDateTime time = TEST_DATE;

    private String text = TEST_STRING;

    private String username = TEST_STRING;

    private Long newsId = TEST_NUMBER;

    @Override
    public CommentDtoResponse build() {
        return CommentDtoResponse.builder()
                .id(id)
                .time(time)
                .text(text)
                .username(username)
                .newsId(newsId)
                .build();
    }
}
