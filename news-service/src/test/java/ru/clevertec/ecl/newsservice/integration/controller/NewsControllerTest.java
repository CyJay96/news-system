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
//import ru.clevertec.ecl.newsservice.builder.news.NewsDtoRequestTestBuilder;
//import ru.clevertec.ecl.newsservice.integration.BaseIntegrationTest;
//import ru.clevertec.ecl.newsservice.model.criteria.NewsCriteria;
//import ru.clevertec.ecl.newsservice.model.dto.request.NewsDtoRequest;
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
//import static ru.clevertec.ecl.newsservice.controller.NewsController.NEWS_API_PATH;
//import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE;
//import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE_SIZE;
//
//@AutoConfigureMockMvc
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
//class NewsControllerTest extends BaseIntegrationTest {
//
//    private final MockMvc mockMvc;
//    private final ObjectMapper objectMapper;
//    private final NewsRepository newsRepository;
//
//    private final NewsDtoRequest newsDtoRequest = NewsDtoRequestTestBuilder
//            .aNewsDtoRequest()
//            .build();
//    private final NewsCriteria searchCriteria = NewsCriteria.builder()
//            .text("apple")
//            .build();
//
//    @Test
//    @DisplayName("Save News")
//    void checkSaveShouldReturnNewsDtoResponse() throws Exception {
//        mockMvc.perform(post(NEWS_API_PATH)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newsDtoRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data").isNotEmpty())
//                .andExpect(jsonPath("$.data.title").value(newsDtoRequest.getTitle()));
//    }
//
//    @Test
//    @DisplayName("Find all News")
//    void checkFindAllShouldReturnNewsDtoResponsePage() throws Exception {
//        int expectedNewsSize = (int) newsRepository.count();
//        mockMvc.perform(get(NEWS_API_PATH)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("page", String.valueOf(TEST_PAGE))
//                        .param("size", String.valueOf(TEST_PAGE_SIZE)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content").isNotEmpty())
//                .andExpect(jsonPath("$.data.content.size()").value(expectedNewsSize));
//    }
//
//    @Test
//    @DisplayName("Find all News by criteria")
//    void checkFindAllByCriteriaShouldReturnNewsDtoResponsePage() throws Exception {
//        int expectedNewsSize = 1;
//        mockMvc.perform(get(NEWS_API_PATH + "/criteria")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(searchCriteria))
//                        .param("page", String.valueOf(TEST_PAGE))
//                        .param("size", String.valueOf(TEST_PAGE_SIZE)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content").isNotEmpty())
//                .andExpect(jsonPath("$.data.content.size()").value(expectedNewsSize));
//    }
//
//    @Nested
//    public class FindByIdTest {
//        @DisplayName("Find News by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkFindByIdShouldReturnNewsDtoResponse(Long id) throws Exception {
//            mockMvc.perform(get(NEWS_API_PATH + "/{id}", id)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data").isNotEmpty())
//                    .andExpect(jsonPath("$.data.id").value(id));
//        }
//
//        @Test
//        @DisplayName("Find News by ID; not found")
//        void checkFindByIdShouldThrowNewsNotFoundException() throws Exception {
//            long doesntExistNewsId = new Random()
//                    .nextLong(newsRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
//            mockMvc.perform(get(NEWS_API_PATH + "/{id}", doesntExistNewsId)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    public class UpdateTest {
//        @DisplayName("Update News by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkUpdateShouldReturnNewsDtoResponse(Long id) throws Exception {
//            mockMvc.perform(put(NEWS_API_PATH + "/{id}", id)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(newsDtoRequest)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data").isNotEmpty())
//                    .andExpect(jsonPath("$.data.id").value(id));
//        }
//
//        @DisplayName("Partial Update News by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkUpdatePartiallyShouldReturnNewsDtoResponse(Long id) throws Exception {
//            mockMvc.perform(patch(NEWS_API_PATH + "/{id}", id)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(newsDtoRequest)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data").isNotEmpty())
//                    .andExpect(jsonPath("$.data.id").value(id));
//        }
//
//        @Test
//        @DisplayName("Update News by ID; not found")
//        void checkUpdateShouldThrowNewsNotFoundException() throws Exception {
//            long doesntExistNewsId = new Random()
//                    .nextLong(newsRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
//            mockMvc.perform(put(NEWS_API_PATH + "/{id}", doesntExistNewsId)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(newsDtoRequest)))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("Partial Update News by ID; not found")
//        void checkUpdatePartiallyShouldThrowNewsNotFoundException() throws Exception {
//            long doesntExistNewsId = new Random()
//                    .nextLong(newsRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
//            mockMvc.perform(patch(NEWS_API_PATH + "/{id}", doesntExistNewsId)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(newsDtoRequest)))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    public class DeleteByIdTest {
//        @DisplayName("Delete News by ID")
//        @ParameterizedTest
//        @ValueSource(longs = {1L, 2L, 3L})
//        void checkDeleteByIdShouldReturnNewsDtoResponse(Long id) throws Exception {
//            mockMvc.perform(delete(NEWS_API_PATH + "/{id}", id)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isNoContent());
//        }
//
//        @Test
//        @DisplayName("Delete News by ID; not found")
//        void checkDeleteByIdShouldThrowNewsNotFoundException() throws Exception {
//            long doesntExistNewsId = new Random()
//                    .nextLong(newsRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
//            mockMvc.perform(delete(NEWS_API_PATH + "/{id}", doesntExistNewsId)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isNotFound());
//        }
//    }
//}
