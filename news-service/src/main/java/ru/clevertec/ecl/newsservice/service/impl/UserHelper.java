package ru.clevertec.ecl.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.newsservice.exception.EntityNotFoundException;
import ru.clevertec.ecl.newsservice.model.entity.Username;
import ru.clevertec.ecl.newsservice.openfeign.controller.UserClient;
import ru.clevertec.ecl.newsservice.openfeign.model.dto.RoleDtoResponse;
import ru.clevertec.ecl.newsservice.openfeign.model.dto.UserDtoResponse;
import ru.clevertec.ecl.newsservice.repository.UsernameRepository;

import static ru.clevertec.ecl.newsservice.openfeign.model.enums.Role.ROLE_ADMIN;
import static ru.clevertec.ecl.newsservice.openfeign.model.enums.Role.ROLE_JOURNALIST;
import static ru.clevertec.ecl.newsservice.openfeign.model.enums.Role.ROLE_SUBSCRIBER;

@Service
@RequiredArgsConstructor
public class UserHelper {

    private final UsernameRepository usernameRepository;
    private final UserClient userClient;

    public boolean isAdmin() {
        Username username = usernameRepository.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new EntityNotFoundException(Username.class));

        UserDtoResponse user = userClient.findUserByUsername(username.getUsername()).getBody().getData();

        return user.getRoles().stream()
                .map(RoleDtoResponse::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_ADMIN.name()));
    }

    public boolean isJournalist() {
        Username username = usernameRepository.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new EntityNotFoundException(Username.class));

        UserDtoResponse user = userClient.findUserByUsername(username.getUsername()).getBody().getData();

        return user.getRoles().stream()
                .map(RoleDtoResponse::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_JOURNALIST.name()));
    }

    public boolean isSubscriber() {
        Username username = usernameRepository.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new EntityNotFoundException(Username.class));

        UserDtoResponse user = userClient.findUserByUsername(username.getUsername()).getBody().getData();

        return user.getRoles().stream()
                .map(RoleDtoResponse::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_SUBSCRIBER.name()));
    }
}
