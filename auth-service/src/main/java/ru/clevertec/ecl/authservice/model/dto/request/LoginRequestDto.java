package ru.clevertec.ecl.authservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@Builder
public class LoginRequestDto implements Serializable {

    @NotBlank(message = "Username cannot be empty")
    @Length(max = 255, message = "Username is too long")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Length(max = 255, message = "Password is too long")
    private String password;
}
