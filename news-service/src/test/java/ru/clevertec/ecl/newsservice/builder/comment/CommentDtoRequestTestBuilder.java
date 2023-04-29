package ru.clevertec.ecl.newsservice.builder.comment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.newsservice.builder.TestBuilder;
import ru.clevertec.ecl.newsservice.model.dto.request.CommentDtoRequest;

import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentDtoRequest")
public class CommentDtoRequestTestBuilder implements TestBuilder<CommentDtoRequest> {

    private String text = TEST_STRING;

    private String username = TEST_STRING;

    @Override
    public CommentDtoRequest build() {
        return CommentDtoRequest.builder()
                .text(text)
                .username(username)
                .build();
    }
}
