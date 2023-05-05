package ru.clevertec.ecl.newsservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.newsservice.model.criteria.NewsCriteria;
import ru.clevertec.ecl.newsservice.model.dto.request.NewsDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.NewsDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;

public interface NewsService {

    NewsDtoResponse save(NewsDtoRequest newsDtoRequest, String token);

    PageResponse<NewsDtoResponse> findAll(Pageable pageable);

    PageResponse<NewsDtoResponse> findAllByCriteria(NewsCriteria searchCriteria, Pageable pageable);

    NewsDtoResponse findById(Long id, Pageable pageable);

    NewsDtoResponse update(Long id, NewsDtoRequest newsDtoRequest, String token);

    void deleteById(Long id, String token);
}
