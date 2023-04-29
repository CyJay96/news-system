package ru.clevertec.ecl.newsservice.service.searcher;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.newsservice.model.criteria.NewsCriteria;
import ru.clevertec.ecl.newsservice.model.entity.News;
import ru.clevertec.ecl.newsservice.model.specification.NewsSpecification;
import ru.clevertec.ecl.newsservice.repository.NewsRepository;

import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class NewsSearcher {

    private static final String NEWS_ID_FIELD = "id";

    private final NewsRepository newsRepository;

    private final Function<NewsCriteria, Specification<News>> toSpecification =
            searchCriteria -> {
                Specification<News> specification = null;

                if (Objects.nonNull(searchCriteria.getTitle())) {
                    specification = append(specification, NewsSpecification.matchTitle(searchCriteria.getTitle()));
                }
                if (Objects.nonNull(searchCriteria.getText())) {
                    specification = append(specification, NewsSpecification.matchText(searchCriteria.getText()));
                }

                return specification;
            };

    public Page<News> getNewsByCriteria(NewsCriteria searchCriteria) {
        return toSpecification
                .andThen(specification -> newsRepository.findAll(specification, PageRequest.of(searchCriteria.getPage(),
                        searchCriteria.getSize(), Sort.by(Sort.Direction.ASC, NEWS_ID_FIELD))))
                .apply(searchCriteria);
    }

    private <T> Specification<T> append(Specification<T> base, Specification<T> specification) {
        if (Objects.isNull(base)) {
            return Specification.where(specification);
        }
        return base.and(specification);
    }
}
