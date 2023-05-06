package ru.clevertec.ecl.newsservice.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Cache configuration depending on the profile
 *
 * @author Konstantin Voytko
 */
@Configuration
public class CacheConfig {

    @Profile("prod")
    @EnableCaching
    @Configuration
    public static class ProdConfig {
    }

    @Profile("!prod")
    @Configuration
    public static class DefaultConfig {
        @Bean
        public NoOpCacheManager cacheManager() {
            return new NoOpCacheManager();
        }
    }
}
