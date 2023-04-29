package ru.clevertec.ecl.newsservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Comment DTO Request")
public class CommentDtoRequest {

    @NotBlank(message = "Text cannot be empty")
    private String text;

    @NotBlank(message = "Username cannot be empty")
    private String username;
}
