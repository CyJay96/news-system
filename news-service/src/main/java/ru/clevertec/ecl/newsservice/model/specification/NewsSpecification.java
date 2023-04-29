package ru.clevertec.ecl.newsservice.model.specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.ecl.newsservice.model.entity.News;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NewsSpecification {

    public static Specification<News> matchTitle(String title) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
    }

    public static Specification<News> matchText(String text) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("text")), "%" + text.toLowerCase() + "%"));
    }
}
