package ru.clevertec.ecl.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.newsservice.client.controller.UserFeignClient;
import ru.clevertec.ecl.newsservice.client.model.dto.RoleDtoResponse;
import ru.clevertec.ecl.newsservice.client.model.dto.UserDtoResponse;

import static ru.clevertec.ecl.newsservice.client.model.enums.Role.ROLE_ADMIN;
import static ru.clevertec.ecl.newsservice.client.model.enums.Role.ROLE_JOURNALIST;
import static ru.clevertec.ecl.newsservice.client.model.enums.Role.ROLE_SUBSCRIBER;

@Service
@RequiredArgsConstructor
public class UserHelper {

    private final UserFeignClient userFeignClient;

    public boolean isAdmin(String token) {
        UserDtoResponse user = userFeignClient.findByToken(token.split(" ")[1]).getBody().getData();
        return user.getRoles().stream()
                .map(RoleDtoResponse::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_ADMIN.name()));
    }

    public boolean isJournalist(String token) {
        UserDtoResponse user = userFeignClient.findByToken(token.split(" ")[1]).getBody().getData();
        return user.getRoles().stream()
                .map(RoleDtoResponse::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_JOURNALIST.name()));
    }

    public boolean isSubscriber(String token) {
        UserDtoResponse user = userFeignClient.findByToken(token.split(" ")[1]).getBody().getData();
        return user.getRoles().stream()
                .map(RoleDtoResponse::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_SUBSCRIBER.name()));
    }
}
