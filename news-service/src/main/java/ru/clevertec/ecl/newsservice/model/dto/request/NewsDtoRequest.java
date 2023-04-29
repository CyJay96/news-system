package ru.clevertec.ecl.newsservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "News DTO Request")
public class NewsDtoRequest {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Text cannot be empty")
    private String text;
}
