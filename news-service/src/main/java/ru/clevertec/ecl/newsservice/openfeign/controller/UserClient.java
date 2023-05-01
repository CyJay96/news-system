package ru.clevertec.ecl.newsservice.openfeign.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.ecl.newsservice.model.dto.response.APIResponse;
import ru.clevertec.ecl.newsservice.openfeign.model.dto.UserDtoResponse;

@FeignClient(value = "auth-service")
public interface UserClient {

    @GetMapping("/api/v0/users/byUsername/{username}")
    ResponseEntity<APIResponse<UserDtoResponse>> findUserByUsername(@PathVariable String username);
}
