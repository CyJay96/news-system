package ru.clevertec.ecl.newsservice.util.factory;

import ru.clevertec.ecl.newsservice.util.cache.Cache;

public interface Factory<K, V> {

    Cache<K, V> getCache(String cacheType, int capacity);
}
