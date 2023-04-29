package ru.clevertec.ecl.newsservice.builder.news;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.newsservice.builder.TestBuilder;
import ru.clevertec.ecl.newsservice.model.dto.request.NewsDtoRequest;

import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aNewsDtoRequest")
public class NewsDtoRequestTestBuilder implements TestBuilder<NewsDtoRequest> {

    private String title = TEST_STRING;

    private String text = TEST_STRING;

    @Override
    public NewsDtoRequest build() {
        return NewsDtoRequest.builder()
                .title(title)
                .text(text)
                .build();
    }
}
