package ru.clevertec.ecl.newsservice.model.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
