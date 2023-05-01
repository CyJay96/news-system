//package ru.clevertec.ecl.newsservice.controller;
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
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import ru.clevertec.ecl.newsservice.builder.comment.CommentDtoRequestTestBuilder;
//import ru.clevertec.ecl.newsservice.builder.comment.CommentDtoResponseTestBuilder;
//import ru.clevertec.ecl.newsservice.exception.EntityNotFoundException;
//import ru.clevertec.ecl.newsservice.model.dto.request.CommentDtoRequest;
//import ru.clevertec.ecl.newsservice.model.dto.response.CommentDtoResponse;
//import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;
//import ru.clevertec.ecl.newsservice.service.CommentService;
//
//import java.util.List;
//import java.util.Objects;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.verify;
//import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_ID;
//import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE;
//import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE_SIZE;
//
//@ExtendWith(MockitoExtension.class)
//class CommentControllerTest {
//
//    @InjectMocks
//    private CommentController commentController;
//
//    @Mock
//    private CommentService commentService;
//
//    @Captor
//    ArgumentCaptor<CommentDtoRequest> commentDtoRequestCaptor;
//
//    private final CommentDtoRequest commentDtoRequest = CommentDtoRequestTestBuilder.aCommentDtoRequest().build();
//    private final CommentDtoResponse expectedCommentDtoResponse = CommentDtoResponseTestBuilder.aCommentDtoResponse().build();
//    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);
//
//    @BeforeEach
//    void setUp() {
//        commentController = new CommentController(commentService);
//    }
//
//    @Nested
//    public class SaveByNewsIdTest {
//        @Test
//        @DisplayName("Save Comment by News ID")
//        void checkSaveByNewsIdShouldReturnCommentDtoResponse() {
//            doReturn(expectedCommentDtoResponse).when(commentService).save(TEST_ID, commentDtoRequest);
//
//            var actualComment = commentController.saveByNewsId(TEST_ID, commentDtoRequest);
//
//            verify(commentService).save(anyLong(), any());
//
//            assertAll(
//                    () -> assertThat(actualComment.getStatusCode()).isEqualTo(HttpStatus.CREATED),
//                    () -> assertThat(Objects.requireNonNull(actualComment.getBody()).getData())
//                            .isEqualTo(expectedCommentDtoResponse)
//            );
//        }
//
//        @Test
//        @DisplayName("Save Comment by News ID with Argument Captor")
//        void checkSaveByNewsIdWithArgumentCaptorShouldReturnCommentDtoResponse() {
//            doReturn(expectedCommentDtoResponse).when(commentService).save(TEST_ID, commentDtoRequest);
//
//            commentController.saveByNewsId(TEST_ID, commentDtoRequest);
//
//            verify(commentService).save(anyLong(), commentDtoRequestCaptor.capture());
//
//            assertThat(commentDtoRequestCaptor.getValue()).isEqualTo(commentDtoRequest);
//        }
//    }
//
//    @Test
//    @DisplayName("Find all Comments")
//    void checkFindAllShouldReturnCommentPage() {
//        PageResponse<CommentDtoResponse> pageResponse = PageResponse.<CommentDtoResponse>builder()
//                .content(List.of(expectedCommentDtoResponse))
//                .number(TEST_PAGE)
//                .size(TEST_PAGE_SIZE)
//                .numberOfElements(1)
//                .build();
//
//        doReturn(pageResponse).when(commentService).findAll(pageable);
//
//        var actualComments = commentController.findAll(pageable);
//
//        verify(commentService).findAll(any());
//
//        assertAll(
//                () -> assertThat(actualComments.getStatusCode()).isEqualTo(HttpStatus.OK),
//                () -> assertThat(Objects.requireNonNull(actualComments.getBody()).getData().getContent().stream()
//                        .anyMatch(actualCommentDtoResponse -> actualCommentDtoResponse.equals(expectedCommentDtoResponse))
//                ).isTrue()
//        );
//    }
//
//    @Nested
//    public class FindByIdTest {
//        @DisplayName("Find Comment by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkFindByIdShouldReturnCommentDtoResponse(Long id) {
//            doReturn(expectedCommentDtoResponse).when(commentService).findById(id);
//
//            var actualComment = commentController.findById(id);
//
//            verify(commentService).findById(anyLong());
//
//            assertAll(
//                    () -> assertThat(actualComment.getStatusCode()).isEqualTo(HttpStatus.OK),
//                    () -> assertThat(Objects.requireNonNull(actualComment.getBody()).getData())
//                            .isEqualTo(expectedCommentDtoResponse)
//            );
//        }
//
//        @Test
//        @DisplayName("Find by ID; not found")
//        void checkFindByIdShouldThrowCommentNotFoundException() {
//            doThrow(EntityNotFoundException.class).when(commentService).findById(anyLong());
//
//            assertThrows(EntityNotFoundException.class, () -> commentController.findById(TEST_ID));
//
//            verify(commentService).findById(anyLong());
//        }
//    }
//
//    @Nested
//    public class UpdateTest {
//        @DisplayName("Update Comment by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkUpdateShouldReturnCommentDtoResponse(Long id) {
//            doReturn(expectedCommentDtoResponse).when(commentService).update(id, commentDtoRequest);
//
//            var actualComment = commentController.update(id, commentDtoRequest);
//
//            verify(commentService).update(anyLong(), any());
//
//            assertAll(
//                    () -> assertThat(actualComment.getStatusCode()).isEqualTo(HttpStatus.OK),
//                    () -> assertThat(Objects.requireNonNull(actualComment.getBody()).getData())
//                            .isEqualTo(expectedCommentDtoResponse)
//            );
//        }
//
//        @DisplayName("Update Comment by ID with Argument Captor")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkUpdateWithArgumentCaptorShouldReturnCommentDtoResponse(Long id) {
//            doReturn(expectedCommentDtoResponse).when(commentService).update(id, commentDtoRequest);
//
//            commentController.update(id, commentDtoRequest);
//
//            verify(commentService).update(anyLong(), commentDtoRequestCaptor.capture());
//
//            assertThat(commentDtoRequestCaptor.getValue()).isEqualTo(commentDtoRequest);
//        }
//
//        @Test
//        @DisplayName("Update Comment by ID; not found")
//        void checkUpdateShouldThrowCommentNotFoundException() {
//            doThrow(EntityNotFoundException.class).when(commentService).update(anyLong(), any());
//
//            assertThrows(EntityNotFoundException.class, () -> commentController.update(TEST_ID, commentDtoRequest));
//
//            verify(commentService).update(anyLong(), any());
//        }
//    }
//
//    @Nested
//    public class DeleteByIdTest {
//        @DisplayName("Delete Comment by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkDeleteByIdShouldReturnVoid(Long id) {
//            doNothing().when(commentService).deleteById(id);
//
//            var voidResponse = commentController.deleteById(id);
//
//            verify(commentService).deleteById(anyLong());
//
//            assertThat(voidResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//        }
//
//        @Test
//        @DisplayName("Delete Comment by ID; not found")
//        void checkDeleteByIdShouldThrowCommentNotFoundException() {
//            doThrow(EntityNotFoundException.class).when(commentService).deleteById(anyLong());
//
//            assertThrows(EntityNotFoundException.class, () -> commentController.deleteById(TEST_ID));
//
//            verify(commentService).deleteById(anyLong());
//        }
//    }
//}
