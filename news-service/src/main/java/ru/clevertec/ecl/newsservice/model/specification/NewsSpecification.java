package ru.clevertec.ecl.newsservice.model.specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.ecl.newsservice.model.entity.News;

/**
 * News Specification for search by criteria
 *
 * @author Konstantin Voytko
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NewsSpecification {

    /**
     * Searches the database for all News entities that have the specified title
     *
     * @param title for search News entities
     * @return News specification with the specified title
     */
    public static Specification<News> matchTitle(String title) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
    }

    /**
     * Searches the database for all News entities that have the specified text
     *
     * @param text for search News entities
     * @return News specification with the specified text
     */
    public static Specification<News> matchText(String text) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("text")), "%" + text.toLowerCase() + "%"));
    }
}
