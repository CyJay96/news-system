package ru.clevertec.ecl.newsservice.aop.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.newsservice.model.entity.News;
import ru.clevertec.ecl.newsservice.util.cache.Cache;
import ru.clevertec.ecl.newsservice.util.factory.Factory;
import ru.clevertec.ecl.newsservice.util.factory.impl.CacheFactory;

import java.util.Optional;

@Aspect
@Component
@Profile("dev")
public class NewsCacheAspect {

    private final Cache<Long, News> cache;

    public NewsCacheAspect(
            @Value("${app.cache.algorithm}") String cacheType,
            @Value("${app.cache.capacity}") int cacheCapacity
    ) {
        Factory<Long, News> factory = new CacheFactory<>();
        cache = factory.getCache(cacheType, cacheCapacity);
    }

    @Around("execution(* ru.clevertec.ecl.newsservice.repository.NewsRepository.save(..))")
    public News aroundSaveMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        News news = (News) joinPoint.proceed();
        cache.put(news.getId(), news);
        return news;
    }

    @Around("execution(* ru.clevertec.ecl.newsservice.repository.NewsRepository.findById(..))")
    public Optional<News> aroundFindByIdMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        if (cache.containsKey(id)) {
            return Optional.of(cache.get(id));
        }
        Optional<News> newsOptional = (Optional<News>) joinPoint.proceed();
        newsOptional.ifPresent(receipt -> cache.put(receipt.getId(), receipt));
        return newsOptional;
    }

    @Around("execution(* ru.clevertec.ecl.newsservice.repository.NewsRepository.deleteById(..))")
    public Object aroundDeleteByIdMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        if (cache.containsKey(id)) {
            cache.remove(id);
        }
        return joinPoint.proceed();
    }
}
