//package ru.clevertec.ecl.newsservice.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import ru.clevertec.ecl.newsservice.builder.comment.CommentDtoRequestTestBuilder;
//import ru.clevertec.ecl.newsservice.builder.comment.CommentDtoResponseTestBuilder;
//import ru.clevertec.ecl.newsservice.builder.comment.CommentTestBuilder;
//import ru.clevertec.ecl.newsservice.builder.news.NewsTestBuilder;
//import ru.clevertec.ecl.newsservice.exception.EntityNotFoundException;
//import ru.clevertec.ecl.newsservice.mapper.CommentMapper;
//import ru.clevertec.ecl.newsservice.model.criteria.CommentCriteria;
//import ru.clevertec.ecl.newsservice.model.dto.request.CommentDtoRequest;
//import ru.clevertec.ecl.newsservice.model.dto.response.CommentDtoResponse;
//import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;
//import ru.clevertec.ecl.newsservice.model.entity.Comment;
//import ru.clevertec.ecl.newsservice.model.entity.News;
//import ru.clevertec.ecl.newsservice.repository.CommentRepository;
//import ru.clevertec.ecl.newsservice.repository.NewsRepository;
//import ru.clevertec.ecl.newsservice.service.impl.CommentServiceImpl;
//import ru.clevertec.ecl.newsservice.service.searcher.CommentSearcher;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.verify;
//import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_ID;
//import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE;
//import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE_SIZE;
//
//@ExtendWith(MockitoExtension.class)
//class CommentServiceTest {
//
//    private CommentService commentService;
//
//    @Mock
//    private CommentSearcher commentSearcher;
//
//    @Mock
//    private CommentRepository commentRepository;
//
//    @Mock
//    private NewsRepository newsRepository;
//
//    @Mock
//    private CommentMapper commentMapper;
//
//    @Captor
//    ArgumentCaptor<Comment> commentCaptor;
//
//    private final CommentDtoRequest commentDtoRequest = CommentDtoRequestTestBuilder.aCommentDtoRequest().build();
//    private final CommentDtoResponse expectedCommentDtoResponse = CommentDtoResponseTestBuilder.aCommentDtoResponse().build();
//    private final Comment expectedComment = CommentTestBuilder.aComment().build();
//    private final CommentCriteria searchCriteria = CommentCriteria.builder().build();
//    private final News news = NewsTestBuilder.aNews().build();
//    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);
//
//    @BeforeEach
//    void setUp() {
//        commentService = new CommentServiceImpl(commentSearcher, commentRepository, newsRepository, commentMapper);
//    }
//
//    @Nested
//    public class SaveTest {
//        @Test
//        @DisplayName("Save Comment")
//        void checkSaveShouldReturnCommentDtoResponse() {
//            doReturn(Optional.of(news)).when(newsRepository).findById(TEST_ID);
//            doReturn(expectedComment).when(commentMapper).toComment(commentDtoRequest);
//            doReturn(expectedComment).when(commentRepository).save(expectedComment);
//            doReturn(expectedCommentDtoResponse).when(commentMapper).toCommentDtoResponse(expectedComment);
//
//            CommentDtoResponse actualComment = commentService.save(TEST_ID, commentDtoRequest);
//
//            verify(newsRepository).findById(anyLong());
//            verify(commentMapper).toComment(any());
//            verify(commentRepository).save(any());
//            verify(commentMapper).toCommentDtoResponse(any());
//
//            assertThat(actualComment).isEqualTo(expectedCommentDtoResponse);
//        }
//
//        @Test
//        @DisplayName("Save Comment with Argument Captor")
//        void checkSaveWithArgumentCaptorShouldReturnCommentDtoResponse() {
//            doReturn(Optional.of(news)).when(newsRepository).findById(TEST_ID);
//            doReturn(expectedComment).when(commentMapper).toComment(commentDtoRequest);
//            doReturn(expectedComment).when(commentRepository).save(expectedComment);
//            doReturn(expectedCommentDtoResponse).when(commentMapper).toCommentDtoResponse(expectedComment);
//
//            commentService.save(TEST_ID, commentDtoRequest);
//
//            verify(newsRepository).findById(anyLong());
//            verify(commentMapper).toComment(any());
//            verify(commentRepository).save(commentCaptor.capture());
//            verify(commentMapper).toCommentDtoResponse(any());
//
//            assertThat(commentCaptor.getValue()).isEqualTo(expectedComment);
//        }
//    }
//
//    @Test
//    @DisplayName("Find all Comments")
//    void checkFindAllShouldReturnCommentDtoResponsePage() {
//        doReturn(new PageImpl<>(List.of(expectedComment))).when(commentRepository).findAll(pageable);
//        doReturn(expectedCommentDtoResponse).when(commentMapper).toCommentDtoResponse(expectedComment);
//
//        PageResponse<CommentDtoResponse> actualComments = commentService.findAll(pageable);
//
//        verify(commentRepository).findAll(eq(pageable));
//        verify(commentMapper).toCommentDtoResponse(any());
//
//        assertThat(actualComments.getContent().stream()
//                .anyMatch(actualCommentDtoResponse -> actualCommentDtoResponse.equals(expectedCommentDtoResponse))
//        ).isTrue();
//    }
//
//    @Test
//    @DisplayName("Find all Comments by criteria")
//    void checkFindAllByCriteriaShouldReturnCommentDtoResponsePage() {
//        doReturn(new PageImpl<>(List.of(expectedComment))).when(commentSearcher).getCommentByCriteria(searchCriteria);
//        doReturn(expectedCommentDtoResponse).when(commentMapper).toCommentDtoResponse(expectedComment);
//
//        PageResponse<CommentDtoResponse> actualComments = commentService.findAllByCriteria(searchCriteria, pageable);
//
//        verify(commentSearcher).getCommentByCriteria(any());
//        verify(commentMapper).toCommentDtoResponse(any());
//
//        assertThat(actualComments.getContent().stream()
//                .anyMatch(actualCommentDtoResponse -> actualCommentDtoResponse.equals(expectedCommentDtoResponse))
//        ).isTrue();
//    }
//
//    @Nested
//    public class FindByIdTest {
//        @DisplayName("Find Comment by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkFindByIdShouldReturnCommentDtoResponse(Long id) {
//            doReturn(Optional.of(expectedComment)).when(commentRepository).findById(id);
//            doReturn(expectedCommentDtoResponse).when(commentMapper).toCommentDtoResponse(expectedComment);
//
//            CommentDtoResponse actualComment = commentService.findById(id);
//
//            verify(commentRepository).findById(anyLong());
//            verify(commentMapper).toCommentDtoResponse(any());
//
//            assertThat(actualComment).isEqualTo(expectedCommentDtoResponse);
//        }
//
//        @Test
//        @DisplayName("Find Comment by ID; not found")
//        void checkFindByIdShouldThrowCommentNotFoundException() {
//            doThrow(EntityNotFoundException.class).when(commentRepository).findById(anyLong());
//
//            assertThrows(EntityNotFoundException.class, () -> commentService.findById(TEST_ID));
//
//            verify(commentRepository).findById(anyLong());
//        }
//    }
//
//    @Nested
//    public class UpdateTest {
//        @DisplayName("Update Comment by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkUpdateShouldReturnCommentDtoResponse(Long id) {
//            doReturn(Optional.of(expectedComment)).when(commentRepository).findById(id);
//            doNothing().when(commentMapper).updateComment(commentDtoRequest, expectedComment);
//            doReturn(expectedComment).when(commentRepository).save(expectedComment);
//            doReturn(expectedCommentDtoResponse).when(commentMapper).toCommentDtoResponse(expectedComment);
//
//            CommentDtoResponse actualComment = commentService.update(id, commentDtoRequest);
//
//            verify(commentRepository).findById(anyLong());
//            verify(commentMapper).updateComment(any(), any());
//            verify(commentRepository).save(any());
//            verify(commentMapper).toCommentDtoResponse(any());
//
//            assertThat(actualComment).isEqualTo(expectedCommentDtoResponse);
//        }
//
//        @DisplayName("Update Comment by ID with Argument Captor")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkUpdateWithArgumentCaptorShouldReturnCommentDtoResponse(Long id) {
//            doReturn(Optional.of(expectedComment)).when(commentRepository).findById(id);
//            doNothing().when(commentMapper).updateComment(commentDtoRequest, expectedComment);
//            doReturn(expectedComment).when(commentRepository).save(expectedComment);
//            doReturn(expectedCommentDtoResponse).when(commentMapper).toCommentDtoResponse(expectedComment);
//
//            commentService.update(id, commentDtoRequest);
//
//            verify(commentRepository).findById(anyLong());
//            verify(commentMapper).updateComment(any(), any());
//            verify(commentRepository).save(commentCaptor.capture());
//            verify(commentMapper).toCommentDtoResponse(any());
//
//            assertThat(commentCaptor.getValue()).isEqualTo(expectedComment);
//        }
//
//        @Test
//        @DisplayName("Update Comment by ID; not found")
//        void checkUpdateShouldThrowCommentNotFoundException() {
//            doThrow(EntityNotFoundException.class).when(commentRepository).findById(anyLong());
//
//            assertThrows(EntityNotFoundException.class, () -> commentService.update(TEST_ID, commentDtoRequest));
//
//            verify(commentRepository).findById(anyLong());
//        }
//    }
//
//    @Nested
//    public class DeleteByIdTest {
//        @DisplayName("Delete Comment by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkDeleteByIdShouldReturnCommentDtoResponse(Long id) {
//            doReturn(true).when(commentRepository).existsById(id);
//            doNothing().when(commentRepository).deleteById(id);
//
//            commentService.deleteById(id);
//
//            verify(commentRepository).existsById(anyLong());
//            verify(commentRepository).deleteById(anyLong());
//        }
//
//        @Test
//        @DisplayName("Delete Comment by ID; not found")
//        void checkDeleteByIdShouldThrowCommentNotFoundException() {
//            doReturn(false).when(commentRepository).existsById(anyLong());
//
//            assertThrows(EntityNotFoundException.class, () -> commentService.deleteById(TEST_ID));
//
//            verify(commentRepository).existsById(anyLong());
//        }
//    }
//}
