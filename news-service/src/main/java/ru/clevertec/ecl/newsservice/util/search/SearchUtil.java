package ru.clevertec.ecl.newsservice.util.search;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

/**
 * Util Service for Specification
 *
 * @author Konstantin Voytko
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchUtil {

    /**
     * Appends a new one to the current Specification
     *
     * @param base Current available Specification
     * @param specification new Specification
     * @return found Comment page by search criteria
     */
    public static <T> Specification<T> append(Specification<T> base, Specification<T> specification) {
        if (Objects.isNull(base)) {
            return Specification.where(specification);
        }
        return base.and(specification);
    }
}
