package ru.clevertec.ecl.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.newsservice.exception.EntityNotFoundException;
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

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentSearcher commentSearcher;
    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final CommentMapper commentMapper;

    @Override
    @CacheEvict(value = "comments", allEntries = true)
    public CommentDtoResponse save(Long newsId, CommentDtoRequest commentDtoRequest) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException(News.class, newsId));

        Comment comment = commentMapper.toComment(commentDtoRequest);
        comment.setNews(news);

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toCommentDtoResponse(savedComment);
    }

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

    @Override
    @Cacheable(value = "comments")
    public CommentDtoResponse findById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::toCommentDtoResponse)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, id));
    }

    @Override
    @Cacheable(value = "comments")
    public CommentDtoResponse update(Long id, CommentDtoRequest commentDtoRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, id));
        commentMapper.updateComment(commentDtoRequest, comment);
        return commentMapper.toCommentDtoResponse(commentRepository.save(comment));
    }

    @Override
    @CacheEvict(value = "comments", allEntries = true)
    public void deleteById(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException(Comment.class, id);
        }
        commentRepository.deleteById(id);
    }
}
