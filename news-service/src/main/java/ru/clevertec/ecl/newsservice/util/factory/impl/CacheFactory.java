package ru.clevertec.ecl.newsservice.util.factory.impl;

import ru.clevertec.ecl.newsservice.util.cache.Cache;
import ru.clevertec.ecl.newsservice.util.cache.impl.LfuCache;
import ru.clevertec.ecl.newsservice.util.cache.impl.LruCache;
import ru.clevertec.ecl.newsservice.util.factory.Factory;

import static ru.clevertec.ecl.newsservice.util.Constants.LFU_CACHE;
import static ru.clevertec.ecl.newsservice.util.Constants.LRU_CACHE;

public class CacheFactory<K, V> implements Factory<K, V> {

    @Override
    public Cache<K, V> getCache(String cacheType, int capacity) {
        return switch (cacheType) {
            case LRU_CACHE -> new LruCache<>(capacity);
            case LFU_CACHE -> new LfuCache<>(capacity);
            default -> throw new IllegalArgumentException("Unknown cache type");
        };
    }
}
