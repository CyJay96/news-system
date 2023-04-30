package ru.clevertec.ecl.newsservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@Builder
@Schema(description = "Comment DTO Request")
public class CommentDtoRequest implements Serializable {

    @Length(max = 255, message = "Text is too long")
    @NotBlank(message = "Text cannot be empty")
    private String text;

    @Length(max = 2048, message = "Username is too long")
    @NotBlank(message = "Username cannot be empty")
    private String username;
}
