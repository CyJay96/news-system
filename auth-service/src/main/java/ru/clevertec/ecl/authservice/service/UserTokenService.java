package ru.clevertec.ecl.authservice.service;

import ru.clevertec.ecl.authservice.model.dto.response.UserDtoResponse;

public interface UserTokenService {

    UserDtoResponse findUserByToken(String token);
}
