package ru.clevertec.ecl.newsservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDtoRequest {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Text cannot be empty")
    private String text;
}
