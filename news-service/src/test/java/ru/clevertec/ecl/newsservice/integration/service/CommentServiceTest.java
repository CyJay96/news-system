package ru.clevertec.ecl.newsservice.integration.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.newsservice.builder.comment.CommentDtoRequestTestBuilder;
import ru.clevertec.ecl.newsservice.exception.EntityNotFoundException;
import ru.clevertec.ecl.newsservice.integration.BaseIntegrationTest;
import ru.clevertec.ecl.newsservice.model.criteria.CommentCriteria;
import ru.clevertec.ecl.newsservice.model.dto.request.CommentDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.CommentDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;
import ru.clevertec.ecl.newsservice.repository.CommentRepository;
import ru.clevertec.ecl.newsservice.repository.NewsRepository;
import ru.clevertec.ecl.newsservice.service.CommentService;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE_SIZE;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class CommentServiceTest extends BaseIntegrationTest {

    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final EntityManager entityManager;

    private final CommentDtoRequest commentDtoRequest = CommentDtoRequestTestBuilder.aCommentDtoRequest().build();
    private final CommentCriteria searchCriteria = CommentCriteria.builder()
            .text("github")
            .build();
    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);

    @Test
    @DisplayName("Save Comment")
    void checkSaveShouldReturnCommentDtoResponse() {
        Long expectedNewsId = newsRepository.findFirstByOrderByIdAsc().get().getId();
        CommentDtoResponse actualComment = commentService.save(expectedNewsId, commentDtoRequest);
        assertThat(actualComment.getNewsId()).isEqualTo(expectedNewsId);
    }

    @Test
    @DisplayName("Find all Comments")
    void checkFindAllShouldReturnCommentDtoResponsePage() {
        int expectedCommentsSize = (int) commentRepository.count();
        PageResponse<CommentDtoResponse> actualComments = commentService.findAll(pageable);
        assertThat(actualComments.getContent()).hasSize(expectedCommentsSize);
    }

    @Test
    @DisplayName("Find all Gift Certificates by criteria")
    void checkFindAllByCriteriaShouldReturnCommentDtoResponsePage() {
        int expectedCommentsSize = 2;
        PageResponse<CommentDtoResponse> actualComments = commentService.findAllByCriteria(searchCriteria, pageable);
        assertThat(actualComments.getContent()).hasSize(expectedCommentsSize);
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Comment by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnCommentDtoResponse(Long id) {
            CommentDtoResponse actualComment = commentService.findById(id);
            assertThat(actualComment.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Find Comment by ID; not found")
        void checkFindByIdShouldThrowCommentNotFoundException() {
            Long doesntExistCommentId = new Random()
                    .nextLong(commentRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> commentService.findById(doesntExistCommentId));
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Comment by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnCommentDtoResponse(Long id) {
            CommentDtoResponse actualComment = commentService.update(id, commentDtoRequest);
            assertThat(actualComment.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Update Comment by ID; not found")
        void checkUpdateShouldThrowCommentNotFoundException() {
            Long doesntExistCommentId = new Random()
                    .nextLong(commentRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> commentService.update(doesntExistCommentId, commentDtoRequest));
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Comment by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnVoid(Long id) {
            commentService.deleteById(id);
            entityManager.flush();
        }

        @Test
        @DisplayName("Delete Comment by ID; not found")
        void checkDeleteByIdShouldThrowCommentNotFoundException() {
            Long doesntExistCommentId = new Random()
                    .nextLong(commentRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> {
                commentService.deleteById(doesntExistCommentId);
                entityManager.flush();
            });
        }
    }
}
