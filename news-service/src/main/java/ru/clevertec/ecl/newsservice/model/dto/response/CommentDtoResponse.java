package ru.clevertec.ecl.newsservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Builder
@Schema(description = "Comment DTO Response")
public class CommentDtoResponse implements Serializable {

    private Long id;

    private OffsetDateTime time;

    private String text;

    private String username;

    private Long newsId;
}
