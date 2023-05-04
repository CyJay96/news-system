package ru.clevertec.ecl.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.newsservice.client.controller.UserFeignClient;
import ru.clevertec.ecl.newsservice.client.model.dto.RoleDtoResponse;
import ru.clevertec.ecl.newsservice.client.model.dto.UserDtoResponse;
import ru.clevertec.ecl.newsservice.exception.EntityNotFoundException;
import ru.clevertec.ecl.newsservice.model.entity.Username;
import ru.clevertec.ecl.newsservice.repository.UsernameRepository;

import static ru.clevertec.ecl.newsservice.client.model.enums.Role.ROLE_ADMIN;
import static ru.clevertec.ecl.newsservice.client.model.enums.Role.ROLE_JOURNALIST;
import static ru.clevertec.ecl.newsservice.client.model.enums.Role.ROLE_SUBSCRIBER;

@Service
@RequiredArgsConstructor
public class UserHelper {

    private final UsernameRepository usernameRepository;
    private final UserFeignClient userFeignClient;

    public boolean isAdmin() {
        Username username = usernameRepository.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new EntityNotFoundException(Username.class));

        UserDtoResponse user = userFeignClient.findUserByUsername(username.getUsername()).getBody().getData();

        return user.getRoles().stream()
                .map(RoleDtoResponse::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_ADMIN.name()));
    }

    public boolean isJournalist() {
        Username username = usernameRepository.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new EntityNotFoundException(Username.class));

        UserDtoResponse user = userFeignClient.findUserByUsername(username.getUsername()).getBody().getData();

        return user.getRoles().stream()
                .map(RoleDtoResponse::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_JOURNALIST.name()));
    }

    public boolean isSubscriber() {
        Username username = usernameRepository.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new EntityNotFoundException(Username.class));

        UserDtoResponse user = userFeignClient.findUserByUsername(username.getUsername()).getBody().getData();

        return user.getRoles().stream()
                .map(RoleDtoResponse::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_SUBSCRIBER.name()));
    }
}
