package ru.clevertec.ecl.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.authservice.exception.EntityNotFoundException;
import ru.clevertec.ecl.authservice.mapper.UserMapper;
import ru.clevertec.ecl.authservice.model.dto.request.UserDtoRequest;
import ru.clevertec.ecl.authservice.model.dto.response.PageResponse;
import ru.clevertec.ecl.authservice.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.authservice.model.entity.User;
import ru.clevertec.ecl.authservice.model.enums.Status;
import ru.clevertec.ecl.authservice.repository.UserRepository;
import ru.clevertec.ecl.authservice.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public PageResponse<UserDtoResponse> getAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserDtoResponse> userDtoResponses = userPage.stream()
                .map(userMapper::toUserDtoResponse)
                .toList();

        return PageResponse.<UserDtoResponse>builder()
                .content(userDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(userDtoResponses.size())
                .build();
    }

    @Override
    public UserDtoResponse getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDtoResponse)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    @Override
    public User getEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName()));
    }

    @Override
    public UserDtoResponse update(Long id, UserDtoRequest userDtoRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        userMapper.updateUser(userDtoRequest, user);
        return userMapper.toUserDtoResponse(userRepository.save(user));
    }

    @Override
    public UserDtoResponse block(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        user.setStatus(Status.BANNED);
        return userMapper.toUserDtoResponse(userRepository.save(user));
    }

    @Override
    public UserDtoResponse unblock(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        user.setStatus(Status.ACTIVE);
        return userMapper.toUserDtoResponse(userRepository.save(user));
    }

    @Override
    public UserDtoResponse deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        user.setStatus(Status.DELETED);
        return userMapper.toUserDtoResponse(userRepository.save(user));
    }
}
