package ru.clevertec.ecl.newsservice.aop.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.newsservice.model.entity.Comment;
import ru.clevertec.ecl.newsservice.util.cache.Cache;
import ru.clevertec.ecl.newsservice.util.factory.Factory;
import ru.clevertec.ecl.newsservice.util.factory.impl.CacheFactory;

import java.util.Optional;

@Aspect
@Component
public class CommentCacheAspect {

    private final Cache<Long, Comment> cache;

    public CommentCacheAspect(
            @Value("${app.cache.algorithm}") String cacheType,
            @Value("${app.cache.capacity}") int cacheCapacity
    ) {
        Factory<Long, Comment> factory = new CacheFactory<>();
        cache = factory.getCache(cacheType, cacheCapacity);
    }

    @Around("execution(* ru.clevertec.ecl.newsservice.repository.CommentRepository.save(..))")
    public Comment aroundSaveMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Comment comment = (Comment) joinPoint.proceed();
        cache.put(comment.getId(), comment);
        return comment;
    }

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

    @Around("execution(* ru.clevertec.ecl.newsservice.repository.CommentRepository.deleteById(..))")
    public Object aroundDeleteByIdMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        if (cache.containsKey(id)) {
            cache.remove(id);
        }
        return joinPoint.proceed();
    }
}
