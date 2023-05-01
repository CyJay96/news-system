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

    @Override
    @CacheEvict(value = "news", allEntries = true)
    public NewsDtoResponse save(NewsDtoRequest newsDtoRequest) {
        if (!userHelper.isAdmin() && !userHelper.isJournalist()) {
            throw new NoPermissionsException();
        }

        News news = newsMapper.toNews(newsDtoRequest);
        return newsMapper.toNewsDtoResponse(newsRepository.save(news));
    }

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

    @Override
    @CacheEvict(value = "news", allEntries = true)
    public NewsDtoResponse update(Long id, NewsDtoRequest newsDtoRequest) {
        if (!userHelper.isAdmin() && !userHelper.isJournalist()) {
            throw new NoPermissionsException();
        }

        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(News.class, id));
        newsMapper.updateNews(newsDtoRequest, news);
        return newsMapper.toNewsDtoResponse(newsRepository.save(news));
    }

    @Override
    @CacheEvict(value = "news", allEntries = true)
    public void deleteById(Long id) {
        if (!userHelper.isAdmin() && !userHelper.isJournalist()) {
            throw new NoPermissionsException();
        }

        if (!newsRepository.existsById(id)) {
            throw new EntityNotFoundException(News.class, id);
        }
        newsRepository.deleteById(id);
    }
}
