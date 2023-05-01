//package ru.clevertec.ecl.newsservice.integration.repository;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import ru.clevertec.ecl.newsservice.integration.BaseIntegrationTest;
//import ru.clevertec.ecl.newsservice.model.entity.Comment;
//import ru.clevertec.ecl.newsservice.repository.CommentRepository;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
//class CommentRepositoryTest extends BaseIntegrationTest {
//
//    private final CommentRepository commentRepository;
//
//    @Nested
//    public class FindFirstByOrderByIdAscTest {
//        @Test
//        @DisplayName("Find first Comment order by ID ASC")
//        void checkFindFirstByOrderByIdAscShouldReturnNotEmptyCommentOptional() {
//            List<Comment> comments = commentRepository.findAll();
//
//            Comment expectedComment = comments.stream()
//                    .sorted(Comparator.comparing(Comment::getId))
//                    .limit(1)
//                    .toList()
//                    .get(0);
//
//            Comment actualComment = commentRepository.findFirstByOrderByIdAsc().get();
//
//            assertThat(actualComment.getId()).isEqualTo(expectedComment.getId());
//        }
//
//        @Test
//        @DisplayName("Find first Comment order by ID ASC; not found")
//        void checkFindFirstByOrderByIdAscShouldReturnCommentOptional() {
//            commentRepository.deleteAll();
//            Optional<Comment> actualCommentOptional = commentRepository.findFirstByOrderByIdAsc();
//            assertThat(actualCommentOptional.isEmpty()).isTrue();
//        }
//    }
//
//    @Nested
//    public class FindFirstByOrderByIdDescTest {
//        @Test
//        @DisplayName("Find first Order order by ID DESC")
//        void checkFindFirstByOrderByIdAscShouldReturnNotEmptyOrderOptional() {
//            List<Comment> comments = commentRepository.findAll();
//
//            Comment expectedComment = comments.stream()
//                    .sorted(Comparator.comparing(Comment::getId).reversed())
//                    .limit(1)
//                    .toList()
//                    .get(0);
//
//            Comment actualComment = commentRepository.findFirstByOrderByIdDesc().get();
//
//            assertThat(actualComment.getId()).isEqualTo(expectedComment.getId());
//        }
//
//        @Test
//        @DisplayName("Find first Comment order by ID DESC; not found")
//        void checkFindFirstByOrderByIdDescShouldReturnEmptyCommentOptional() {
//            commentRepository.deleteAll();
//            Optional<Comment> actualCommentOptional = commentRepository.findFirstByOrderByIdDesc();
//            assertThat(actualCommentOptional.isEmpty()).isTrue();
//        }
//    }
//}
