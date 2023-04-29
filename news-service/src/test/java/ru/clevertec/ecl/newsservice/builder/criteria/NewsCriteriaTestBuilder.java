package ru.clevertec.ecl.newsservice.builder.criteria;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.newsservice.builder.TestBuilder;
import ru.clevertec.ecl.newsservice.model.criteria.NewsCriteria;

import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE_SIZE;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aNewsCriteria")
public class NewsCriteriaTestBuilder implements TestBuilder<NewsCriteria> {

    private String title = TEST_STRING;

    private String text = TEST_STRING;

    private Integer page = TEST_PAGE;

    private Integer size = TEST_PAGE_SIZE;

    @Override
    public NewsCriteria build() {
        return NewsCriteria.builder()
                .title(title)
                .text(text)
                .page(page)
                .size(size)
                .build();
    }
}
