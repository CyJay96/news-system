package ru.clevertec.ecl.newsservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.newsservice.builder.comment.CommentDtoResponseTestBuilder;
import ru.clevertec.ecl.newsservice.builder.comment.CommentTestBuilder;
import ru.clevertec.ecl.newsservice.builder.news.NewsDtoRequestTestBuilder;
import ru.clevertec.ecl.newsservice.builder.news.NewsDtoResponseTestBuilder;
import ru.clevertec.ecl.newsservice.builder.news.NewsTestBuilder;
import ru.clevertec.ecl.newsservice.exception.EntityNotFoundException;
import ru.clevertec.ecl.newsservice.exception.NoPermissionsException;
import ru.clevertec.ecl.newsservice.mapper.CommentMapper;
import ru.clevertec.ecl.newsservice.mapper.NewsMapper;
import ru.clevertec.ecl.newsservice.model.criteria.NewsCriteria;
import ru.clevertec.ecl.newsservice.model.dto.request.NewsDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.CommentDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.NewsDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;
import ru.clevertec.ecl.newsservice.model.entity.Comment;
import ru.clevertec.ecl.newsservice.model.entity.News;
import ru.clevertec.ecl.newsservice.repository.CommentRepository;
import ru.clevertec.ecl.newsservice.repository.NewsRepository;
import ru.clevertec.ecl.newsservice.service.impl.NewsServiceImpl;
import ru.clevertec.ecl.newsservice.service.impl.UserHelper;
import ru.clevertec.ecl.newsservice.service.searcher.NewsSearcher;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_BEARER;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE;
import static ru.clevertec.ecl.newsservice.util.TestConstants.TEST_PAGE_SIZE;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    private NewsService newsService;

    @Mock
    private NewsSearcher newsSearcher;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserHelper userHelper;

    @Captor
    ArgumentCaptor<News> newsCaptor;

    private final NewsDtoRequest newsDtoRequest = NewsDtoRequestTestBuilder.aNewsDtoRequest().build();
    private final NewsDtoResponse expectedNewsDtoResponse = NewsDtoResponseTestBuilder.aNewsDtoResponse().build();
    private final News expectedNews = NewsTestBuilder.aNews().build();
    private final NewsCriteria searchCriteria = NewsCriteria.builder().build();
    private final CommentDtoResponse commentDtoResponse = CommentDtoResponseTestBuilder.aCommentDtoResponse().build();
    private final Comment comment = CommentTestBuilder.aComment().build();
    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);

    @BeforeEach
    void setUp() {
        newsService = new NewsServiceImpl(newsSearcher, newsRepository, commentRepository,
                newsMapper, commentMapper, userHelper);
    }

    @Nested
    public class SaveTest {
        @Test
        @DisplayName("Save News")
        void checkSaveShouldReturnNewsDtoResponse() {
            doReturn(true).when(userHelper).isAdmin(TEST_BEARER);

            doReturn(expectedNews).when(newsMapper).toNews(newsDtoRequest);
            doReturn(expectedNews).when(newsRepository).save(expectedNews);
            doReturn(expectedNewsDtoResponse).when(newsMapper).toNewsDtoResponse(expectedNews);

            NewsDtoResponse actualNews = newsService.save(newsDtoRequest, TEST_BEARER);

            verify(userHelper).isAdmin(anyString());

            verify(newsMapper).toNews(any());
            verify(newsRepository).save(any());
            verify(newsMapper).toNewsDtoResponse(any());

            assertThat(actualNews).isEqualTo(expectedNewsDtoResponse);
        }

        @Test
        @DisplayName("Save News with Argument Captor")
        void checkSaveWithArgumentCaptorShouldReturnNewsDtoResponse() {
            doReturn(true).when(userHelper).isAdmin(TEST_BEARER);

            doReturn(expectedNews).when(newsMapper).toNews(newsDtoRequest);
            doReturn(expectedNews).when(newsRepository).save(expectedNews);
            doReturn(expectedNewsDtoResponse).when(newsMapper).toNewsDtoResponse(expectedNews);

            newsService.save(newsDtoRequest, TEST_BEARER);

            verify(userHelper).isAdmin(anyString());

            verify(newsMapper).toNews(any());
            verify(newsRepository).save(newsCaptor.capture());
            verify(newsMapper).toNewsDtoResponse(any());

            assertThat(newsCaptor.getValue()).isEqualTo(expectedNews);
        }

        @Test
        @DisplayName("Check permissions when save news")
        void checkSaveShouldThrowNoPermissionsException() {
            doReturn(false).when(userHelper).isAdmin(TEST_BEARER);
            doReturn(false).when(userHelper).isJournalist(TEST_BEARER);

            assertThrows(NoPermissionsException.class, () -> newsService.save(newsDtoRequest, TEST_BEARER));

            verify(userHelper).isAdmin(anyString());
            verify(userHelper).isJournalist(anyString());
        }
    }

    @Test
    @DisplayName("Find all News")
    void checkFindAllShouldReturnNewsDtoResponsePage() {
        doReturn(new PageImpl<>(List.of(expectedNews))).when(newsRepository).findAll(pageable);
        doReturn(expectedNewsDtoResponse).when(newsMapper).toNewsDtoResponse(expectedNews);

        PageResponse<NewsDtoResponse> actualNews = newsService.findAll(pageable);

        verify(newsRepository).findAll(eq(pageable));
        verify(newsMapper).toNewsDtoResponse(any());

        assertThat(actualNews.getContent().stream()
                .anyMatch(actualNewsDtoResponse -> actualNewsDtoResponse.equals(expectedNewsDtoResponse))
        ).isTrue();
    }

    @Test
    @DisplayName("Find all News by criteria")
    void checkFindAllByCriteriaShouldReturnNewsDtoResponsePage() {
        doReturn(new PageImpl<>(List.of(expectedNews))).when(newsSearcher).getNewsByCriteria(searchCriteria);
        doReturn(expectedNewsDtoResponse).when(newsMapper).toNewsDtoResponse(expectedNews);

        PageResponse<NewsDtoResponse> actualNews = newsService.findAllByCriteria(searchCriteria, pageable);

        verify(newsSearcher).getNewsByCriteria(any());
        verify(newsMapper).toNewsDtoResponse(any());

        assertThat(actualNews.getContent().stream()
                .anyMatch(actualNewsDtoResponse -> actualNewsDtoResponse.equals(expectedNewsDtoResponse))
        ).isTrue();
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find News by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnNewsDtoResponse(Long id) {
            doReturn(Optional.of(expectedNews)).when(newsRepository).findById(id);
            doReturn(expectedNewsDtoResponse).when(newsMapper).toNewsDtoResponse(expectedNews);
            doReturn(new PageImpl<>(List.of(comment))).when(commentRepository).findAllByNewsId(id, pageable);
            doReturn(commentDtoResponse).when(commentMapper).toCommentDtoResponse(comment);

            NewsDtoResponse actualNews = newsService.findById(id, pageable);

            verify(newsRepository).findById(anyLong());
            verify(newsMapper).toNewsDtoResponse(any());
            verify(commentRepository).findAllByNewsId(anyLong(), any());
            verify(commentMapper).toCommentDtoResponse(any());

            assertThat(actualNews).isEqualTo(expectedNewsDtoResponse);
        }

        @Test
        @DisplayName("Find News by ID; not found")
        void checkFindByIdShouldThrowNewsNotFoundException() {
            doThrow(EntityNotFoundException.class).when(newsRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> newsService.findById(TEST_ID, pageable));

            verify(newsRepository).findById(anyLong());
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update News by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnNewsDtoResponse(Long id) {
            doReturn(true).when(userHelper).isAdmin(TEST_BEARER);

            doReturn(Optional.of(expectedNews)).when(newsRepository).findById(id);
            doNothing().when(newsMapper).updateNews(newsDtoRequest, expectedNews);
            doReturn(expectedNews).when(newsRepository).save(expectedNews);
            doReturn(expectedNewsDtoResponse).when(newsMapper).toNewsDtoResponse(expectedNews);

            NewsDtoResponse actualNews = newsService.update(id, newsDtoRequest, TEST_BEARER);

            verify(userHelper).isAdmin(anyString());

            verify(newsRepository).findById(anyLong());
            verify(newsMapper).updateNews(any(), any());
            verify(newsRepository).save(any());
            verify(newsMapper).toNewsDtoResponse(any());

            assertThat(actualNews).isEqualTo(expectedNewsDtoResponse);
        }

        @DisplayName("Update News by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateWithArgumentCaptorShouldReturnNewsDtoResponse(Long id) {
            doReturn(true).when(userHelper).isAdmin(TEST_BEARER);

            doReturn(Optional.of(expectedNews)).when(newsRepository).findById(id);
            doNothing().when(newsMapper).updateNews(newsDtoRequest, expectedNews);
            doReturn(expectedNews).when(newsRepository).save(expectedNews);
            doReturn(expectedNewsDtoResponse).when(newsMapper).toNewsDtoResponse(expectedNews);

            newsService.update(id, newsDtoRequest, TEST_BEARER);

            verify(userHelper).isAdmin(anyString());

            verify(newsRepository).findById(anyLong());
            verify(newsMapper).updateNews(any(), any());
            verify(newsRepository).save(newsCaptor.capture());
            verify(newsMapper).toNewsDtoResponse(any());

            assertThat(newsCaptor.getValue()).isEqualTo(expectedNews);
        }

        @Test
        @DisplayName("Check permissions when update news")
        void checkUpdateShouldThrowNoPermissionsException() {
            doReturn(false).when(userHelper).isAdmin(TEST_BEARER);
            doReturn(false).when(userHelper).isJournalist(TEST_BEARER);

            assertThrows(NoPermissionsException.class, () -> newsService.update(TEST_ID, newsDtoRequest, TEST_BEARER));

            verify(userHelper).isAdmin(anyString());
            verify(userHelper).isJournalist(anyString());
        }

        @Test
        @DisplayName("Update News by ID; not found")
        void checkUpdateShouldThrowNewsNotFoundException() {
            doReturn(true).when(userHelper).isAdmin(TEST_BEARER);
            doThrow(EntityNotFoundException.class).when(newsRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> newsService.update(TEST_ID, newsDtoRequest, TEST_BEARER));

            verify(userHelper).isAdmin(anyString());
            verify(newsRepository).findById(anyLong());
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete News by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnNewsDtoResponse(Long id) {
            doReturn(true).when(userHelper).isAdmin(TEST_BEARER);

            doReturn(true).when(newsRepository).existsById(id);
            doNothing().when(newsRepository).deleteById(id);

            newsService.deleteById(id, TEST_BEARER);

            verify(userHelper).isAdmin(anyString());

            verify(newsRepository).existsById(anyLong());
            verify(newsRepository).deleteById(anyLong());
        }

        @Test
        @DisplayName("Check permissions when delete news by ID")
        void checkDeleteByIdShouldThrowNoPermissionsException() {
            doReturn(false).when(userHelper).isAdmin(TEST_BEARER);
            doReturn(false).when(userHelper).isJournalist(TEST_BEARER);

            assertThrows(NoPermissionsException.class, () -> newsService.deleteById(TEST_ID, TEST_BEARER));

            verify(userHelper).isAdmin(anyString());
            verify(userHelper).isJournalist(anyString());
        }

        @Test
        @DisplayName("Delete News by ID; not found")
        void checkDeleteByIdShouldThrowNewsNotFoundException() {
            doReturn(true).when(userHelper).isAdmin(TEST_BEARER);
            doReturn(false).when(newsRepository).existsById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> newsService.deleteById(TEST_ID, TEST_BEARER));

            verify(userHelper).isAdmin(anyString());
            verify(newsRepository).existsById(anyLong());
        }
    }
}
