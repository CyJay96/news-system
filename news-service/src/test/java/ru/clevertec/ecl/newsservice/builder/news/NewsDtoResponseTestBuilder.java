package ru.clevertec.ecl.newsservice.builder.news;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.newsservice.builder.TestBuilder;
import ru.clevertec.ecl.newsservice.model.dto.response.CommentDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.NewsDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;

import java.time.OffsetDateTime;

import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_DATE;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aNewsDtoResponse")
public class NewsDtoResponseTestBuilder implements TestBuilder<NewsDtoResponse> {

    private Long id = TEST_ID;

    private OffsetDateTime time = TEST_DATE;

    private String title = TEST_STRING;

    private String text = TEST_STRING;

    private PageResponse<CommentDtoResponse> comments = PageResponse.<CommentDtoResponse>builder().build();

    @Override
    public NewsDtoResponse build() {
        return NewsDtoResponse.builder()
                .id(id)
                .time(time)
                .title(title)
                .text(text)
                .comments(comments)
                .build();
    }
}
