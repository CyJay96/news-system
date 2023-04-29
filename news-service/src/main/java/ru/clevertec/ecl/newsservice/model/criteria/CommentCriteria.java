package ru.clevertec.ecl.newsservice.model.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Comment Criteria DTO")
public class CommentCriteria {

    @JsonProperty("text")
    String text;

    @JsonProperty("username")
    String username;

    @JsonProperty("page")
    Integer page;

    @JsonProperty("size")
    Integer size;
}
