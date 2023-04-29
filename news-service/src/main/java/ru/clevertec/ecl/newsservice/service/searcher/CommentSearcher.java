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

import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CommentSearcher {

    private static final String COMMENT_ID_FIELD = "id";

    private final CommentRepository commentRepository;

    private final Function<CommentCriteria, Specification<Comment>> toSpecification =
            searchCriteria -> {
                Specification<Comment> specification = null;

                if (Objects.nonNull(searchCriteria.getText())) {
                    specification = append(specification, CommentSpecification.matchText(searchCriteria.getText()));
                }
                if (Objects.nonNull(searchCriteria.getUsername())) {
                    specification = append(specification, CommentSpecification.matchUsername(searchCriteria.getUsername()));
                }

                return specification;
            };

    public Page<Comment> getCommentByCriteria(CommentCriteria searchCriteria) {
        return toSpecification
                .andThen(specification -> commentRepository.findAll(specification, PageRequest.of(searchCriteria.getPage(),
                        searchCriteria.getSize(), Sort.by(Sort.Direction.ASC, COMMENT_ID_FIELD))))
                .apply(searchCriteria);
    }

    private <T> Specification<T> append(Specification<T> base, Specification<T> specification) {
        if (Objects.isNull(base)) {
            return Specification.where(specification);
        }
        return base.and(specification);
    }
}
