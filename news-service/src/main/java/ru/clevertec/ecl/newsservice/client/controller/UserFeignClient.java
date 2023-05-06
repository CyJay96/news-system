package ru.clevertec.ecl.newsservice.client.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.ecl.newsservice.client.model.dto.UserDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.APIResponse;

/**
 * Client for sending requests to auth-service
 *
 * @author Konstantin Voytko
 */
@FeignClient(value = "${app.user-feign-client.host}")
public interface UserFeignClient {

    /**
     * Request to receive a user by his JWT token
     *
     * @param token user JWT to verify user authorization
     * @return current authorized user info
     */
    @GetMapping("/api/v0/users/byToken/{token}")
    ResponseEntity<APIResponse<UserDtoResponse>> findByToken(@PathVariable String token);
}
