package ru.clevertec.ecl.authservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Authentication DTO Response")
public class AuthDtoResponse {

    private String token;
}
