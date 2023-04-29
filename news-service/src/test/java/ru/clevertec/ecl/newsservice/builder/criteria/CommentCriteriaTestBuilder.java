package ru.clevertec.ecl.newsservice.builder.criteria;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.newsservice.builder.TestBuilder;
import ru.clevertec.ecl.newsservice.model.criteria.CommentCriteria;

import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE_SIZE;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentCriteria")
public class CommentCriteriaTestBuilder implements TestBuilder<CommentCriteria> {

    private String text = TEST_STRING;

    private String username = TEST_STRING;

    private Integer page = TEST_PAGE;

    private Integer size = TEST_PAGE_SIZE;

    @Override
    public CommentCriteria build() {
        return CommentCriteria.builder()
                .text(text)
                .username(username)
                .page(page)
                .size(size)
                .build();
    }
}
