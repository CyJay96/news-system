package ru.clevertec.ecl.authservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * Login DTO for requests
 *
 * @author Konstantin Voytko
 */
@Data
@Builder
@Schema(description = "Login DTO Request")
public class LoginRequestDto implements Serializable {

    @NotBlank(message = "Username cannot be empty")
    @Length(max = 255, message = "Username is too long")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Length(max = 255, message = "Password is too long")
    private String password;
}
