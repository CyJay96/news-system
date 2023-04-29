package ru.clevertec.ecl.newsservice.model.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsCriteria {

    @JsonProperty("title")
    String title;

    @JsonProperty("text")
    String text;

    @JsonProperty("page")
    Integer page;

    @JsonProperty("size")
    Integer size;
}
