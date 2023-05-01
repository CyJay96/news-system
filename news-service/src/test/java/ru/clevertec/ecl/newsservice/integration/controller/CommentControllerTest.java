//package ru.clevertec.ecl.newsservice.integration.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.clevertec.ecl.newsservice.builder.comment.CommentDtoRequestTestBuilder;
//import ru.clevertec.ecl.newsservice.integration.BaseIntegrationTest;
//import ru.clevertec.ecl.newsservice.model.criteria.CommentCriteria;
//import ru.clevertec.ecl.newsservice.model.dto.request.CommentDtoRequest;
//import ru.clevertec.ecl.newsservice.repository.CommentRepository;
//import ru.clevertec.ecl.newsservice.repository.NewsRepository;
//
//import java.util.Random;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static ru.clevertec.ecl.newsservice.controller.CommentController.COMMENT_API_PATH;
//import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE;
//import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE_SIZE;
//
//@AutoConfigureMockMvc
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
//class CommentControllerTest extends BaseIntegrationTest {
//
//    private final MockMvc mockMvc;
//    private final ObjectMapper objectMapper;
//    private final CommentRepository commentRepository;
//    private final NewsRepository newsRepository;
//
//    private final CommentDtoRequest commentDtoRequest = CommentDtoRequestTestBuilder
//            .aCommentDtoRequest()
//            .build();
//    private final CommentCriteria searchCriteria = CommentCriteria.builder()
//            .text("github")
//            .build();
//
//    @Test
//    @DisplayName("Save Comment")
//    void checkSaveShouldReturnCommentDtoResponse() throws Exception {
//        Long expectedNewsId = newsRepository.findFirstByOrderByIdAsc().get().getId();
//        mockMvc.perform(post(COMMENT_API_PATH + "/{newsId}", expectedNewsId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(commentDtoRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data").isNotEmpty())
//                .andExpect(jsonPath("$.data.newsId").value(expectedNewsId));
//    }
//
//    @Test
//    @DisplayName("Find all Comments")
//    void checkFindAllShouldReturnCommentDtoResponsePage() throws Exception {
//        int expectedCommentsSize = (int) commentRepository.count();
//        mockMvc.perform(get(COMMENT_API_PATH)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("page", String.valueOf(TEST_PAGE))
//                        .param("size", String.valueOf(TEST_PAGE_SIZE)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content").isNotEmpty())
//                .andExpect(jsonPath("$.data.content.size()").value(expectedCommentsSize));
//    }
//
//    @Test
//    @DisplayName("Find all Comments by criteria")
//    void checkFindAllByCriteriaShouldReturnCommentDtoResponsePage() throws Exception {
//        int expectedCommentsSize = 2;
//        mockMvc.perform(get(COMMENT_API_PATH + "/criteria")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(searchCriteria))
//                        .param("page", String.valueOf(TEST_PAGE))
//                        .param("size", String.valueOf(TEST_PAGE_SIZE)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content").isNotEmpty())
//                .andExpect(jsonPath("$.data.content.size()").value(expectedCommentsSize));
//    }
//
//    @Nested
//    public class FindByIdTest {
//        @DisplayName("Find Comment by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkFindByIdShouldReturnCommentDtoResponse(Long id) throws Exception {
//            mockMvc.perform(get(COMMENT_API_PATH + "/{id}", id)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data").isNotEmpty())
//                    .andExpect(jsonPath("$.data.id").value(id));
//        }
//
//        @Test
//        @DisplayName("Find Comment by ID; not found")
//        void checkFindByIdShouldThrowCommentNotFoundException() throws Exception {
//            long doesntExistCommentId = new Random()
//                    .nextLong(commentRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
//            mockMvc.perform(get(COMMENT_API_PATH + "/{id}", doesntExistCommentId)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    public class UpdateByIdTest {
//        @DisplayName("Update Comment by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkUpdateShouldReturnCommentDtoResponse(Long id) throws Exception {
//            mockMvc.perform(put(COMMENT_API_PATH + "/{id}", id)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(commentDtoRequest)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data").isNotEmpty())
//                    .andExpect(jsonPath("$.data.id").value(id));
//        }
//
//        @DisplayName("Partial Update Comment by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkUpdatePartiallyShouldReturnCommentDtoResponse(Long id) throws Exception {
//            mockMvc.perform(patch(COMMENT_API_PATH + "/{id}", id)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(commentDtoRequest)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data").isNotEmpty())
//                    .andExpect(jsonPath("$.data.id").value(id));
//        }
//
//        @Test
//        @DisplayName("Update Comment by ID; not found")
//        void checkUpdateShouldThrowCommentNotFoundException() throws Exception {
//            long doesntExistCommentId = new Random()
//                    .nextLong(commentRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
//            mockMvc.perform(put(COMMENT_API_PATH + "/{id}", doesntExistCommentId)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(commentDtoRequest)))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("Partial Update Comment by ID; not found")
//        void checkUpdatePartiallyShouldThrowCommentNotFoundException() throws Exception {
//            long doesntExistCommentId = new Random()
//                    .nextLong(commentRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
//            mockMvc.perform(patch(COMMENT_API_PATH + "/{id}", doesntExistCommentId)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(commentDtoRequest)))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    public class DeleteByIdTest {
//        @DisplayName("Delete Comment by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkDeleteByIdShouldReturnVoid(Long id) throws Exception {
//            mockMvc.perform(delete(COMMENT_API_PATH + "/{id}", id)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isNoContent());
//        }
//
//        @Test
//        @DisplayName("Delete Comment by ID; not found")
//        void checkDeleteByIdShouldThrowCommentNotFoundException() throws Exception {
//            long doesntExistCommentId = new Random()
//                    .nextLong(commentRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
//            mockMvc.perform(delete(COMMENT_API_PATH + "/{id}", doesntExistCommentId)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isNotFound());
//        }
//    }
//}
