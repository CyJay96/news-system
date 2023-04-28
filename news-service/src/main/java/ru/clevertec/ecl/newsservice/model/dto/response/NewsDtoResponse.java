package ru.clevertec.ecl.newsservice.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class NewsDtoResponse {

    Long id;

    OffsetDateTime time;

    String title;

    String text;

    PageResponse<CommentDtoResponse> comments;
}
