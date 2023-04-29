package ru.clevertec.ecl.authservice.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDtoResponse {

    private UserDtoResponse user;

    private String token;
}