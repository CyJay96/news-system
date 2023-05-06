package ru.clevertec.ecl.newsservice.util.cache;

import java.util.Collection;

public interface Cache<K, V> {

    void put(K key, V value);

    V get(K key);

    void remove(K key);

    Collection<V> values();

    boolean containsKey(K key);

    int size();

    boolean isEmpty();

    void clear();
}
