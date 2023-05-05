package ru.clevertec.ecl.authservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@Schema(description = "Authentication DTO Response")
public class AuthDtoResponse implements Serializable {

    private String token;
}
