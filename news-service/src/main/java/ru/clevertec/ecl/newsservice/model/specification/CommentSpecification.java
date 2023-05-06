package ru.clevertec.ecl.newsservice.model.specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.ecl.newsservice.model.entity.Comment;

/**
 * Comment Specification for search by criteria
 *
 * @author Konstantin Voytko
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentSpecification {

    /**
     * Searches the database for all Comment entities that have the specified text
     *
     * @param text for search Comment entities
     * @return Comment specification with the specified text
     */
    public static Specification<Comment> matchText(String text) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("text")), "%" + text.toLowerCase() + "%"));
    }

    /**
     * Searches the database for all Comment entities that have the specified username
     *
     * @param username for search Comment entities
     * @return Comment specification with the specified username
     */
    public static Specification<Comment> matchUsername(String username) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
    }
}
