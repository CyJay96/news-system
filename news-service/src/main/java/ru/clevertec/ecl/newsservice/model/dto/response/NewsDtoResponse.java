package ru.clevertec.ecl.newsservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * News DTO for responses
 *
 * @author Konstantin Voytko
 */
@Data
@Builder
@Schema(description = "News DTO Response")
public class NewsDtoResponse implements Serializable {

    private Long id;

    private OffsetDateTime time;

    private String title;

    private String text;

    private PageResponse<CommentDtoResponse> comments;
}
