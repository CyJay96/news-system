package ru.clevertec.ecl.newsservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
@Schema(description = "News DTO Response")
public class NewsDtoResponse {

    Long id;

    OffsetDateTime time;

    String title;

    String text;

    PageResponse<CommentDtoResponse> comments;
}
