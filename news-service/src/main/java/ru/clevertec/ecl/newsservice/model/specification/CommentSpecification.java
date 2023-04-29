package ru.clevertec.ecl.newsservice.model.specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.ecl.newsservice.model.entity.Comment;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentSpecification {

    public static Specification<Comment> matchText(String text) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("text")), "%" + text.toLowerCase() + "%"));
    }

    public static Specification<Comment> matchUsername(String username) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
    }
}
