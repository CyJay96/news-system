package ru.clevertec.ecl.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
import ru.clevertec.ecl.newsservice.service.NewsService;
import ru.clevertec.ecl.newsservice.service.searcher.NewsSearcher;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsSearcher newsSearcher;
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final NewsMapper newsMapper;
    private final CommentMapper commentMapper;
    private final UserHelper userHelper;

    /**
     * Save a new News entity. Uses the Redis-cache implementation
     *
     * @param token user JWT to verify user authorization
     * @param newsDtoRequest News DTO to save
     * @throws NoPermissionsException if there are no permissions to save the News entity
     * @return saved News DTO
     */
    @Override
    @CacheEvict(value = "news", allEntries = true)
    public NewsDtoResponse save(NewsDtoRequest newsDtoRequest, String token) {
        if (!userHelper.isAdmin(token) && !userHelper.isJournalist(token)) {
            throw new NoPermissionsException();
        }

        News news = newsMapper.toNews(newsDtoRequest);
        return newsMapper.toNewsDtoResponse(newsRepository.save(news));
    }

    /**
     * Find all News entities info. Uses the Redis-cache implementation
     *
     * @param pageable page number & page size values to find
     * @return all News DTOs
     */
    @Override
    @Cacheable(value = "news")
    public PageResponse<NewsDtoResponse> findAll(Pageable pageable) {
        Page<News> newsPage = newsRepository.findAll(pageable);

        List<NewsDtoResponse> newsDtoResponses = newsPage.stream()
                .map(newsMapper::toNewsDtoResponse)
                .toList();

        return PageResponse.<NewsDtoResponse>builder()
                .content(newsDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(newsDtoResponses.size())
                .build();
    }

    /**
     * Find all News entities info by criteria. Uses the Redis-cache implementation
     *
     * @param searchCriteria News search criteria to find
     * @param pageable page number & page size values to find
     * @return all News DTOs by criteria
     */
    @Override
    public PageResponse<NewsDtoResponse> findAllByCriteria(NewsCriteria searchCriteria, Pageable pageable) {
        searchCriteria.setPage(pageable.getPageNumber());
        searchCriteria.setSize(pageable.getPageSize());

        Page<News> newsPage = newsSearcher.getNewsByCriteria(searchCriteria);

        List<NewsDtoResponse> newsDtoResponses = newsPage.stream()
                .map(newsMapper::toNewsDtoResponse)
                .toList();

        return PageResponse.<NewsDtoResponse>builder()
                .content(newsDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(newsDtoResponses.size())
                .build();
    }

    /**
     * Find News entity info by ID. Uses the Redis-cache implementation
     *
     * @param id News ID to find
     * @throws EntityNotFoundException if the News entity with ID doesn't exist
     * @return found News DTO by ID
     */
    @Override
    @Cacheable(value = "news")
    public NewsDtoResponse findById(Long id, Pageable pageable) {
        NewsDtoResponse newsDtoResponse = newsRepository.findById(id)
                .map(newsMapper::toNewsDtoResponse)
                .orElseThrow(() -> new EntityNotFoundException(News.class, id));

        Page<Comment> commentPage = commentRepository.findAllByNewsId(id, pageable);
        List<CommentDtoResponse> commentDtoResponses = commentPage.stream()
                .map(commentMapper::toCommentDtoResponse)
                .toList();
        PageResponse<CommentDtoResponse> comments = PageResponse.<CommentDtoResponse>builder()
                .content(commentDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(commentDtoResponses.size())
                .build();

        newsDtoResponse.setComments(comments);
        return newsDtoResponse;
    }

    /**
     * Update an existing News entity info by ID. Uses the Redis-cache implementation
     *
     * @param token user JWT to verify user authorization
     * @param id News ID to update
     * @param newsDtoRequest News DTO to update
     * @throws NoPermissionsException if there are no permissions to update the News entity
     * @throws EntityNotFoundException if the News entity with ID doesn't exist
     * @return updated News DTO by ID
     */
    @Override
    @CacheEvict(value = "news", allEntries = true)
    public NewsDtoResponse update(Long id, NewsDtoRequest newsDtoRequest, String token) {
        if (!userHelper.isAdmin(token) && !userHelper.isJournalist(token)) {
            throw new NoPermissionsException();
        }

        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(News.class, id));
        newsMapper.updateNews(newsDtoRequest, news);
        return newsMapper.toNewsDtoResponse(newsRepository.save(news));
    }

    /**
     * Delete a News entity by ID. Uses the Redis-cache implementation
     *
     * @param token user JWT to verify user authorization
     * @param id News ID to delete
     * @throws NoPermissionsException if there are no permissions to delete the News entity
     * @throws EntityNotFoundException if the News entity with ID doesn't exist
     */
    @Override
    @CacheEvict(value = "news", allEntries = true)
    public void deleteById(Long id, String token) {
        if (!userHelper.isAdmin(token) && !userHelper.isJournalist(token)) {
            throw new NoPermissionsException();
        }

        if (!newsRepository.existsById(id)) {
            throw new EntityNotFoundException(News.class, id);
        }
        newsRepository.deleteById(id);
    }
}
