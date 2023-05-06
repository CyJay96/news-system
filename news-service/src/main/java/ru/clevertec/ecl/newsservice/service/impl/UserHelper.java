package ru.clevertec.ecl.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.newsservice.client.controller.UserFeignClient;
import ru.clevertec.ecl.newsservice.client.model.dto.RoleDtoResponse;
import ru.clevertec.ecl.newsservice.client.model.dto.UserDtoResponse;

import static ru.clevertec.ecl.newsservice.client.model.enums.Role.ROLE_ADMIN;
import static ru.clevertec.ecl.newsservice.client.model.enums.Role.ROLE_JOURNALIST;
import static ru.clevertec.ecl.newsservice.client.model.enums.Role.ROLE_SUBSCRIBER;

/**
 * Service for sending requests to auth-service and verifying the users roles
 *
 * @author Konstantin Voytko
 */
@Service
@RequiredArgsConstructor
public class UserHelper {

    private final UserFeignClient userFeignClient;

    /**
     * Sends a request to the auth-service to get a User DTO and checks whether current user has the Admin role
     *
     * @param token user JWT to verify user authorization
     * @return boolean value, whether current user has the Admin role
     */
    public boolean isAdmin(String token) {
        UserDtoResponse user = userFeignClient.findByToken(token.split(" ")[1]).getBody().getData();
        return user.getRoles().stream()
                .map(RoleDtoResponse::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_ADMIN.name()));
    }

    /**
     * Sends a request to the auth-service to get a User DTO and checks whether current user has the Journalist role
     *
     * @param token user JWT to verify user authorization
     * @return boolean value, whether current user has the Journalist role
     */
    public boolean isJournalist(String token) {
        UserDtoResponse user = userFeignClient.findByToken(token.split(" ")[1]).getBody().getData();
        return user.getRoles().stream()
                .map(RoleDtoResponse::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_JOURNALIST.name()));
    }

    /**
     * Sends a request to the auth-service to get a User DTO and checks whether current user has the Subscriber role
     *
     * @param token user JWT to verify user authorization
     * @return boolean value, whether current user has the Subscriber role
     */
    public boolean isSubscriber(String token) {
        UserDtoResponse user = userFeignClient.findByToken(token.split(" ")[1]).getBody().getData();
        return user.getRoles().stream()
                .map(RoleDtoResponse::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_SUBSCRIBER.name()));
    }
}
