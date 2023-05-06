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
import ru.clevertec.ecl.newsservice.model.criteria.CommentCriteria;
import ru.clevertec.ecl.newsservice.model.dto.request.CommentDtoRequest;
import ru.clevertec.ecl.newsservice.model.dto.response.CommentDtoResponse;
import ru.clevertec.ecl.newsservice.model.dto.response.PageResponse;
import ru.clevertec.ecl.newsservice.model.entity.Comment;
import ru.clevertec.ecl.newsservice.model.entity.News;
import ru.clevertec.ecl.newsservice.repository.CommentRepository;
import ru.clevertec.ecl.newsservice.repository.NewsRepository;
import ru.clevertec.ecl.newsservice.service.CommentService;
import ru.clevertec.ecl.newsservice.service.searcher.CommentSearcher;

import java.util.List;

/**
 * Comment Service to work with the Comment entity
 *
 * @author Konstantin Voytko
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentSearcher commentSearcher;
    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final CommentMapper commentMapper;
    private final UserHelper userHelper;

    /**
     * Save a new Comment entity by News ID. Uses the Redis-cache implementation
     *
     * @param token user JWT to verify user authorization
     * @param newsId News ID to save Comment entity
     * @param commentDtoRequest Comment DTO to save
     * @throws NoPermissionsException if there are no permissions to save the Comment entity
     * @return saved Comment DTO
     */
    @Override
    @CacheEvict(value = "comments", allEntries = true)
    public CommentDtoResponse save(Long newsId, CommentDtoRequest commentDtoRequest, String token) {
        if (!userHelper.isAdmin(token) && !userHelper.isJournalist(token) && !userHelper.isSubscriber(token)) {
            throw new NoPermissionsException();
        }

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException(News.class, newsId));

        Comment comment = commentMapper.toComment(commentDtoRequest);
        comment.setNews(news);

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toCommentDtoResponse(savedComment);
    }

    /**
     * Find all Comment entities info. Uses the Redis-cache implementation
     *
     * @param pageable page number & page size values to find
     * @return all Comment DTOs
     */
    @Override
    @Cacheable(value = "comments")
    public PageResponse<CommentDtoResponse> findAll(Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAll(pageable);

        List<CommentDtoResponse> commentDtoResponses = commentPage.stream()
                .map(commentMapper::toCommentDtoResponse)
                .toList();

        return PageResponse.<CommentDtoResponse>builder()
                .content(commentDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(commentDtoResponses.size())
                .build();
    }

    /**
     * Find all Comments entities info by criteria. Uses the Redis-cache implementation
     *
     * @param searchCriteria Comments search criteria to find
     * @param pageable page number & page size values to find
     * @return all Comment DTOs by criteria
     */
    @Override
    public PageResponse<CommentDtoResponse> findAllByCriteria(CommentCriteria searchCriteria, Pageable pageable) {
        searchCriteria.setPage(pageable.getPageNumber());
        searchCriteria.setSize(pageable.getPageSize());

        Page<Comment> commentPage = commentSearcher.getCommentByCriteria(searchCriteria);

        List<CommentDtoResponse> commentDtoResponses = commentPage.stream()
                .map(commentMapper::toCommentDtoResponse)
                .toList();

        return PageResponse.<CommentDtoResponse>builder()
                .content(commentDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(commentDtoResponses.size())
                .build();
    }

    /**
     * Find Comment entity info by ID. Uses the Redis-cache implementation
     *
     * @param id Comment ID to find
     * @throws EntityNotFoundException if the Comment entity with ID doesn't exist
     * @return found Comment DTO by ID
     */
    @Override
    @Cacheable(value = "comments")
    public CommentDtoResponse findById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::toCommentDtoResponse)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, id));
    }

    /**
     * Update an existing Comment entity info by ID. Uses the Redis-cache implementation
     *
     * @param token user JWT to verify user authorization
     * @param id Comment ID to update
     * @param commentDtoRequest Comment DTO to update
     * @throws NoPermissionsException if there are no permissions to update the Comment entity
     * @throws EntityNotFoundException if the Comment entity with ID doesn't exist
     * @return updated Comment DTO by ID
     */
    @Override
    @CacheEvict(value = "comments", allEntries = true)
    public CommentDtoResponse update(Long id, CommentDtoRequest commentDtoRequest, String token) {
        if (!userHelper.isAdmin(token) && !userHelper.isJournalist(token) && !userHelper.isSubscriber(token)) {
            throw new NoPermissionsException();
        }

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, id));
        commentMapper.updateComment(commentDtoRequest, comment);
        return commentMapper.toCommentDtoResponse(commentRepository.save(comment));
    }

    /**
     * Delete a Comment entity by ID. Uses the Redis-cache implementation
     *
     * @param token user JWT to verify user authorization
     * @param id Comment ID to delete
     * @throws NoPermissionsException if there are no permissions to delete the Comment entity
     * @throws EntityNotFoundException if the Comment entity with ID doesn't exist
     */
    @Override
    @CacheEvict(value = "comments", allEntries = true)
    public void deleteById(Long id, String token) {
        if (!userHelper.isAdmin(token) && !userHelper.isJournalist(token) && !userHelper.isSubscriber(token)) {
            throw new NoPermissionsException();
        }

        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException(Comment.class, id);
        }

        commentRepository.deleteById(id);
    }
}
