package ru.clevertec.ecl.newsservice.service.searcher;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.newsservice.model.criteria.CommentCriteria;
import ru.clevertec.ecl.newsservice.model.entity.Comment;
import ru.clevertec.ecl.newsservice.model.specification.CommentSpecification;
import ru.clevertec.ecl.newsservice.repository.CommentRepository;
import ru.clevertec.ecl.newsservice.util.search.SearchUtil;

import java.util.Objects;
import java.util.function.Function;

/**
 * Comment Searcher for search by criteria
 *
 * @author Konstantin Voytko
 */
@Service
@RequiredArgsConstructor
public class CommentSearcher {

    private static final String COMMENT_ID_FIELD = "id";

    private final CommentRepository commentRepository;

    /**
     * Functional interface for Comment Specification search by criteria
     */
    private final Function<CommentCriteria, Specification<Comment>> toSpecification =
            searchCriteria -> {
                Specification<Comment> specification = null;

                if (Objects.nonNull(searchCriteria.getText())) {
                    specification = SearchUtil.append(specification,
                            CommentSpecification.matchText(searchCriteria.getText()));
                }
                if (Objects.nonNull(searchCriteria.getUsername())) {
                    specification = SearchUtil.append(specification,
                            CommentSpecification.matchUsername(searchCriteria.getUsername()));
                }

                return specification;
            };

    /**
     * Searches for all Comments entities by search criteria
     *
     * @param searchCriteria Comments search criteria to find
     * @return found Comment page by search criteria
     */
    public Page<Comment> getCommentByCriteria(CommentCriteria searchCriteria) {
        return toSpecification
                .andThen(specification -> commentRepository.findAll(specification, PageRequest.of(searchCriteria.getPage(),
                        searchCriteria.getSize(), Sort.by(Sort.Direction.ASC, COMMENT_ID_FIELD))))
                .apply(searchCriteria);
    }
}
