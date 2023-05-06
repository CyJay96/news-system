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

/**
 * User Service to work with the User entity
 *
 * @author Konstantin Voytko
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Find all User entities info
     *
     * @param pageable page number & page size values to find
     * @return all User DTOs
     */
    @Override
    public PageResponse<UserDtoResponse> findAll(Pageable pageable) {
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

    /**
     * Find User entity info by ID
     *
     * @param id User ID to find
     * @throws EntityNotFoundException if the User entity with ID doesn't exist
     * @return found User DTO by ID
     */
    @Override
    public UserDtoResponse findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDtoResponse)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    /**
     * Find User entity info by username
     *
     * @param username User username to find
     * @return found User DTO by username
     */
    @Override
    public UserDtoResponse findByUsername(String username) {
        return userMapper.toUserDtoResponse(findEntityByUsername(username));
    }

    /**
     * Find User entity info by username
     *
     * @param username User username to find
     * @throws EntityNotFoundException if the User entity with username doesn't exist
     * @return found User entity by username
     */
    @Override
    public User findEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName()));
    }

    /**
     * Update an existing User entity info by ID
     *
     * @param id User ID to update
     * @param userDtoRequest User DTO to update
     * @throws EntityNotFoundException if the User entity with ID doesn't exist
     * @return updated User DTO by ID
     */
    @Override
    public UserDtoResponse update(Long id, UserDtoRequest userDtoRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        userMapper.updateUser(userDtoRequest, user);
        return userMapper.toUserDtoResponse(userRepository.save(user));
    }

    /**
     * Block a User entity by ID
     *
     * @param id User ID to block
     * @throws EntityNotFoundException if the User entity with ID doesn't exist
     * @return blocked User DTO by ID
     */
    @Override
    public UserDtoResponse block(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        user.setStatus(Status.BANNED);
        return userMapper.toUserDtoResponse(userRepository.save(user));
    }

    /**
     * Unblock a User entity by ID
     *
     * @param id User ID to unblock
     * @throws EntityNotFoundException if the User entity with ID doesn't exist
     * @return unblocked User DTO by ID
     */
    @Override
    public UserDtoResponse unblock(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        user.setStatus(Status.ACTIVE);
        return userMapper.toUserDtoResponse(userRepository.save(user));
    }

    /**
     * Delete a User entity by ID
     *
     * @param id User ID to delete
     * @throws EntityNotFoundException if the User entity with ID doesn't exist
     * @return deleted User DTO by ID
     */
    @Override
    public UserDtoResponse deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        user.setStatus(Status.DELETED);
        return userMapper.toUserDtoResponse(userRepository.save(user));
    }
}
