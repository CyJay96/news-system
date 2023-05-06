package ru.clevertec.ecl.authservice.service;

import ru.clevertec.ecl.authservice.model.dto.request.LoginRequestDto;
import ru.clevertec.ecl.authservice.model.dto.request.RegisterRequestDto;
import ru.clevertec.ecl.authservice.model.dto.response.AuthDtoResponse;

public interface AuthenticationService {

    AuthDtoResponse register(final RegisterRequestDto registerRequestDto);

    AuthDtoResponse login(final LoginRequestDto loginRequestDto);
}
