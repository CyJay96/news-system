package ru.clevertec.ecl.newsservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.ecl.newsservice.model.dto.request.CommentDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.CommentDtoResponse;
import ru.clevertec.ecl.newsservice.model.entity.Comment;

/**
 * Mapper for Comment entities & DTOs
 *
 * @author Konstantin Voytko
 */
@Mapper
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "news", ignore = true)
    @Mapping(target = "time", expression = "java(java.time.OffsetDateTime.now())")
    Comment toComment(CommentDtoRequest commentDtoRequest);

    @Mapping(
            target = "newsId",
            expression = "java(java.util.Objects.nonNull(comment.getNews()) ? " +
                    "comment.getNews().getId() : null)")
    CommentDtoResponse toCommentDtoResponse(Comment comment);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "news", ignore = true)
    @Mapping(target = "time", ignore = true)
    void updateComment(CommentDtoRequest commentDtoRequest, @MappingTarget Comment comment);
}
