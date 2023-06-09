package ru.clevertec.ecl.newsservice.model.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * News search criteria DTO
 *
 * @author Konstantin Voytko
 */
@Data
@Builder
@Schema(description = "News search criteria DTO")
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
