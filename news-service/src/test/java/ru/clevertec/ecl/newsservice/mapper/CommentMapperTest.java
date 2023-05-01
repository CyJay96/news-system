//package ru.clevertec.ecl.newsservice.mapper;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mapstruct.factory.Mappers;
//import ru.clevertec.ecl.newsservice.builder.comment.CommentDtoRequestTestBuilder;
//import ru.clevertec.ecl.newsservice.builder.comment.CommentDtoResponseTestBuilder;
//import ru.clevertec.ecl.newsservice.builder.comment.CommentTestBuilder;
//import ru.clevertec.ecl.newsservice.model.dto.request.CommentDtoRequest;
//import ru.clevertec.ecl.newsservice.model.dto.response.CommentDtoResponse;
//import ru.clevertec.ecl.newsservice.model.entity.Comment;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class CommentMapperTest {
//
//    private CommentMapper commentMapper;
//
//    private final CommentDtoRequest expectedCommentDtoRequest = CommentDtoRequestTestBuilder.aCommentDtoRequest().build();
//    private final CommentDtoResponse expectedCommentDtoResponse = CommentDtoResponseTestBuilder.aCommentDtoResponse().build();
//    private final Comment expectedComment = CommentTestBuilder.aComment().build();
//
//    @BeforeEach
//    void setUp() {
//        commentMapper = Mappers.getMapper(CommentMapper.class);
//    }
//
//    @Test
//    @DisplayName("Map Comment DTO to Entity")
//    void checkToCommentShouldReturnComment() {
//        Comment actualComment = commentMapper.toComment(expectedCommentDtoRequest);
//        assertThat(actualComment).isEqualTo(expectedComment);
//    }
//
//    @Test
//    @DisplayName("Map Comment Entity to DTO")
//    void checkToCommentDtoResponseShouldReturnCommentDtoResponse() {
//        CommentDtoResponse actualCommentDtoResponse = commentMapper.toCommentDtoResponse(expectedComment);
//        assertThat(actualCommentDtoResponse).isEqualTo(expectedCommentDtoResponse);
//    }
//}
