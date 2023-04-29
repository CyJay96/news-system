package ru.clevertec.ecl.newsservice.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
@Builder
public class PageResponse<T> implements Serializable {

    @JsonProperty("number")
    Integer number;

    @JsonProperty("size")
    Integer size;

    @JsonProperty("numberOfElements")
    Integer numberOfElements;

    @JsonProperty("content")
    List<T> content;
}
