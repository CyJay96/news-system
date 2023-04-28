package ru.clevertec.ecl.newsservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.newsservice.model.dto.request.NewsDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.NewsDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;

public interface NewsService {

    NewsDtoResponse save(NewsDtoRequest newsDtoRequest);

    PageResponse<NewsDtoResponse> findAll(Pageable pageable);

    NewsDtoResponse findById(Long id, Pageable pageable);

    NewsDtoResponse update(Long id, NewsDtoRequest newsDtoRequest);

    void deleteById(Long id);
}
