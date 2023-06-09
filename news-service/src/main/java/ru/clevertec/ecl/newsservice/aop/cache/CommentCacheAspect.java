package ru.clevertec.ecl.newsservice.aop.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.newsservice.model.entity.Comment;
import ru.clevertec.ecl.newsservice.util.cache.Cache;
import ru.clevertec.ecl.newsservice.util.factory.Factory;
import ru.clevertec.ecl.newsservice.util.factory.impl.CacheFactory;

import java.util.Optional;

/**
 * Aspect for caching the Comment entity in AOP style
 *
 * @author Konstantin Voytko
 */
@Aspect
@Component
@Profile("dev")
public class CommentCacheAspect {

    private final Cache<Long, Comment> cache;

    public CommentCacheAspect(
            @Value("${app.cache.algorithm}") String cacheType,
            @Value("${app.cache.capacity}") int cacheCapacity
    ) {
        Factory<Long, Comment> factory = new CacheFactory<>();
        cache = factory.getCache(cacheType, cacheCapacity);
    }

    /**
     * Caches the Comment entity when saving to the database
     *
     * @param joinPoint A point of observation, joining the code, where the introduction of functionality is required
     */
    @Around("execution(* ru.clevertec.ecl.newsservice.repository.CommentRepository.save(..))")
    public Comment aroundSaveMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Comment comment = (Comment) joinPoint.proceed();
        cache.put(comment.getId(), comment);
        return comment;
    }

    /**
     * When calling the JPA findById method, first looks the Comment entity in the cache and returns,
     * otherwise it takes it out of the database, stores it in the cache and returns
     *
     * @param joinPoint A point of observation, joining the code, where the introduction of functionality is required
     */
    @Around("execution(* ru.clevertec.ecl.newsservice.repository.CommentRepository.findById(..))")
    public Optional<Comment> aroundFindByIdMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        if (cache.containsKey(id)) {
            return Optional.of(cache.get(id));
        }
        Optional<Comment> commentOptional = (Optional<Comment>) joinPoint.proceed();
        commentOptional.ifPresent(product -> cache.put(product.getId(), product));
        return commentOptional;
    }

    /**
     * When calling the JPA deleteById method, first, if the Comment entity
     * is available in the cache, deletes in it, and then from the database
     *
     * @param joinPoint A point of observation, joining the code, where the introduction of functionality is required
     */
    @Around("execution(* ru.clevertec.ecl.newsservice.repository.CommentRepository.deleteById(..))")
    public Object aroundDeleteByIdMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        if (cache.containsKey(id)) {
            cache.remove(id);
        }
        return joinPoint.proceed();
    }
}
