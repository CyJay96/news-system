package ru.clevertec.ecl.newsservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
@Schema(description = "Comment DTO Response")
public class CommentDtoResponse {

    Long id;

    OffsetDateTime time;

    String text;

    String username;

    Long newsId;
}
