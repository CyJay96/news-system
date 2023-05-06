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
import ru.clevertec.ecl.newsservice.util.search.SearchUtil;

import java.util.Objects;
import java.util.function.Function;

/**
 * News Searcher for search by criteria
 *
 * @author Konstantin Voytko
 */
@Service
@RequiredArgsConstructor
public class NewsSearcher {

    private static final String NEWS_ID_FIELD = "id";

    private final NewsRepository newsRepository;

    /**
     * Functional interface for News Specification search by criteria
     */
    private final Function<NewsCriteria, Specification<News>> toSpecification =
            searchCriteria -> {
                Specification<News> specification = null;

                if (Objects.nonNull(searchCriteria.getTitle())) {
                    specification = SearchUtil.append(specification,
                            NewsSpecification.matchTitle(searchCriteria.getTitle()));
                }
                if (Objects.nonNull(searchCriteria.getText())) {
                    specification = SearchUtil.append(specification,
                            NewsSpecification.matchText(searchCriteria.getText()));
                }

                return specification;
            };

    /**
     * Searches for all News entities by search criteria
     *
     * @param searchCriteria News search criteria to find
     * @return found News page by search criteria
     */
    public Page<News> getNewsByCriteria(NewsCriteria searchCriteria) {
        return toSpecification
                .andThen(specification -> newsRepository.findAll(specification, PageRequest.of(searchCriteria.getPage(),
                        searchCriteria.getSize(), Sort.by(Sort.Direction.ASC, NEWS_ID_FIELD))))
                .apply(searchCriteria);
    }
}
