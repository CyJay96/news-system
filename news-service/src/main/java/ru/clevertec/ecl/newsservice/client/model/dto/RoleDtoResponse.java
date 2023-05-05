package ru.clevertec.ecl.newsservice.client.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Role DTO Response")
public class RoleDtoResponse implements Serializable {

    private Long id;

    private String name;
}
