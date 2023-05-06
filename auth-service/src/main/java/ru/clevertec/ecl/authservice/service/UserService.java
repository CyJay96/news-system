package ru.clevertec.ecl.authservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.authservice.model.dto.request.UserDtoRequest;
import ru.clevertec.ecl.authservice.model.dto.response.PageResponse;
import ru.clevertec.ecl.authservice.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.authservice.model.entity.User;

public interface UserService {

    PageResponse<UserDtoResponse> findAll(Pageable pageable);

    UserDtoResponse findById(Long id);

    UserDtoResponse findByUsername(String username);

    User findEntityByUsername(String username);

    UserDtoResponse update(Long id, UserDtoRequest userDtoRequest);

    UserDtoResponse block(Long id);

    UserDtoResponse unblock(Long id);

    UserDtoResponse deleteById(Long id);
}
