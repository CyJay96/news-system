package ru.clevertec.ecl.newsservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.ecl.newsservice.model.dto.request.NewsDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.NewsDtoResponse;
import ru.clevertec.ecl.newsservice.model.entity.News;

@Mapper
public interface NewsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "time", expression = "java(java.time.OffsetDateTime.now())")
    News toNews(NewsDtoRequest newsDtoRequest);

    @Mapping(target = "comments", ignore = true)
    NewsDtoResponse toNewsDtoResponse(News news);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "time", ignore = true)
    void updateNews(NewsDtoRequest newsDtoRequest, @MappingTarget News news);
}
