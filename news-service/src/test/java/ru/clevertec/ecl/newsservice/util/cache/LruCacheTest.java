package ru.clevertec.ecl.newsservice.util.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.clevertec.ecl.newsservice.util.cache.impl.LfuCache;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LruCacheTest {

    private Cache<String, Integer> cache;

    @BeforeEach
    void setUp() {
        int capacity = 2;
        cache = new LfuCache<>(capacity);
    }

    @Test
    @DisplayName("Put to the cache")
    void checkPutShouldPutValueToCache() {
        cache.put("one", 1);
        cache.put("two", 2);
        cache.put("three", 2);

        assertAll(
                () -> assertThat(cache.size()).isEqualTo(2),
                () -> assertThat(cache.containsKey("one")).isFalse(),
                () -> assertThat(cache.containsKey("two")).isTrue(),
                () -> assertThat(cache.containsKey("three")).isTrue()
        );
    }

    @Test
    @DisplayName("Get from the cache")
    void checkGetShouldReturnCacheValue() {
        cache.put("one", 1);

        Integer value = cache.get("one");

        assertThat(value).isEqualTo(1);
    }

    @Test
    @DisplayName("Remove from the cache")
    void checkRemoveShouldRemoveCacheValue() {
        cache.put("one", 1);
        cache.put("two", 2);

        cache.remove("one");

        assertAll(
                () -> assertThat(cache.size()).isEqualTo(1),
                () -> assertThat(cache.containsKey("one")).isFalse(),
                () -> assertThat(cache.containsKey("two")).isTrue()
        );
    }

    @Test
    @DisplayName("Get cache values collection")
    void checkValuesShouldReturnCacheValuesCollection() {
        cache.put("one", 1);
        cache.put("two", 2);

        Collection<Integer> values = cache.values();

        assertThat(values.size()).isEqualTo(2);
    }

    @Nested
    class CacheContainsKey {
        @Test
        @DisplayName("Check cache contains key case 1")
        void checkContainsKeyShouldReturnTrue() {
            cache.put("one", 1);
            cache.put("two", 2);
            cache.put("three", 3);

            boolean checkContainsKey = cache.containsKey("two");

            assertThat(checkContainsKey).isTrue();
        }

        @Test
        @DisplayName("Check cache contains key case 2")
        void checkContainsKeyShouldReturnFalse() {
            cache.put("one", 1);
            cache.put("two", 2);
            cache.put("three", 3);

            boolean checkContainsKey = cache.containsKey("one");

            assertThat(checkContainsKey).isFalse();
        }
    }

    @Test
    @DisplayName("Get cache size")
    void checkSizeShouldReturnCacheSize() {
        cache.put("one", 1);
        cache.put("two", 2);
        cache.put("three", 3);

        int cacheSize = cache.size();

        assertThat(cacheSize).isEqualTo(2);
    }

    @Nested
    class IsCacheEmpty {
        @Test
        @DisplayName("Check is cache empty case 1")
        void checkIsEmptyShouldReturnTrue() {
            boolean checkIsCacheEmpty = cache.isEmpty();

            assertThat(checkIsCacheEmpty).isTrue();
        }

        @Test
        @DisplayName("Check is cache empty case 2")
        void checkIsEmptyShouldReturnFalse() {
            cache.put("one", 1);
            cache.put("two", 2);

            boolean checkIsCacheEmpty = cache.isEmpty();

            assertThat(checkIsCacheEmpty).isFalse();
        }
    }

    @Test
    @DisplayName("Clear cache")
    void checkClearShouldClearCache() {
        cache.put("one", 1);
        cache.put("two", 2);

        cache.clear();

        boolean checkIsCacheEmpty = cache.isEmpty();

        assertThat(checkIsCacheEmpty).isTrue();
    }
}