package ru.clevertec.ecl.newsservice.client.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.ecl.newsservice.client.model.dto.UserDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.APIResponse;

@FeignClient(value = "${app.user-feign-client.host}")
public interface UserFeignClient {

    @GetMapping("/api/v0/users/byUsername/{username}")
    ResponseEntity<APIResponse<UserDtoResponse>> findUserByUsername(@PathVariable String username);
}
