package ru.clevertec.ecl.newsservice.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class CommentDtoResponse {

    Long id;

    OffsetDateTime time;

    String text;

    String username;

    Long newsId;
}
