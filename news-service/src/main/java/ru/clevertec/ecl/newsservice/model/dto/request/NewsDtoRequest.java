package ru.clevertec.ecl.newsservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@Builder
@Schema(description = "News DTO Request")
public class NewsDtoRequest implements Serializable {

    @NotBlank(message = "Title cannot be empty")
    @Length(max = 255, message = "Title is too long")
    private String title;

    @NotBlank(message = "Text cannot be empty")
    @Length(max = 2048, message = "Text is too long")
    private String text;
}
