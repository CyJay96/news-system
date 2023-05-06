package ru.clevertec.ecl.authservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Role DTO for responses
 *
 * @author Konstantin Voytko
 */
@Data
@Builder
@Schema(description = "Role DTO Response")
public class RoleDtoResponse implements Serializable {

    private Long id;

    private String name;
}
