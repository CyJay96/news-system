package ru.clevertec.ecl.newsservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.newsservice.model.criteria.CommentCriteria;
import ru.clevertec.ecl.newsservice.model.dto.request.CommentDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.CommentDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;

public interface CommentService {

    CommentDtoResponse save(Long newsId, CommentDtoRequest commentDtoRequest, String token);

    PageResponse<CommentDtoResponse> findAll(Pageable pageable);

    PageResponse<CommentDtoResponse> findAllByCriteria(CommentCriteria searchCriteria, Pageable pageable);

    CommentDtoResponse findById(Long id);

    CommentDtoResponse update(Long id, CommentDtoRequest commentDtoRequest, String token);

    void deleteById(Long id, String token);
}
