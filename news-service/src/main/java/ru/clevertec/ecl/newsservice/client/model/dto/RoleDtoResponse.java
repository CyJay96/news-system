package ru.clevertec.ecl.newsservice.client.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@Schema(description = "Role DTO Response")
public class RoleDtoResponse implements Serializable {

    private Long id;

    private String name;
}
