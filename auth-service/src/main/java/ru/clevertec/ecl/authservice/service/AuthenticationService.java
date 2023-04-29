package ru.clevertec.ecl.authservice.service;

import ru.clevertec.ecl.authservice.model.dto.request.SignInRequestDto;
import ru.clevertec.ecl.authservice.model.dto.request.SignUpRequestDto;
import ru.clevertec.ecl.authservice.model.dto.response.AuthDtoResponse;

public interface AuthenticationService {

    AuthDtoResponse signUp(final SignUpRequestDto requestDto);

    AuthDtoResponse signIn(final SignInRequestDto requestDto);
}
