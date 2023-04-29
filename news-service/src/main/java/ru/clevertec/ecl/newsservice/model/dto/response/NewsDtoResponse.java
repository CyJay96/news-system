package ru.clevertec.ecl.newsservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Builder
@Schema(description = "News DTO Response")
public class NewsDtoResponse implements Serializable {

    Long id;

    OffsetDateTime time;

    String title;

    String text;

    PageResponse<CommentDtoResponse> comments;
}
