package ru.clevertec.ecl.newsservice.util.cache.impl;

import ru.clevertec.ecl.newsservice.exception.EntityNotFoundException;
import ru.clevertec.ecl.newsservice.util.cache.Cache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * An LFU (Least Frequently Used) cache implementation that uses a ConcurrentHashMap
 * to store key-value pairs and frequencies and a ConcurrentSkipListSet to store ordered keys.
 * Concurrent library provides thread-safety.
 *
 * @param <K> the type of keys stored in the cache
 * @param <V> the type of values stored in the cache
 *
 * @author Konstantin Voytko
 */
public class LfuCache<K, V> implements Cache<K, V> {

    // Maximum capacity of the cache
    private final int capacity;

    // Map to store the key-value pairs
    private final Map<K, V> cache;

    // Map to store the frequency of each key
    private final Map<K, Integer> frequencies;

    // ConcurrentSkipListSet to store the ordered keys
    private final ConcurrentSkipListSet<K> orderedKeys;

    /**
     * Constructs a new LFU cache with the specified capacity.
     *
     * @param capacity the maximum number of key-value pairs that can be stored in the cache
     * @throws IllegalArgumentException if the capacity is negative
     */
    public LfuCache(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity in LFU cache is negative");
        }
        this.capacity = capacity;
        cache = new ConcurrentHashMap<>(capacity);
        frequencies = new ConcurrentHashMap<>(capacity);
        orderedKeys = new ConcurrentSkipListSet<>((k1, k2) -> {
            int freq1 = frequencies.getOrDefault(k1, 0);
            int freq2 = frequencies.getOrDefault(k2, 0);
            return freq1 == freq2 ? k1.hashCode() - k2.hashCode() : freq1 - freq2;
        });
    }

    /**
     * Associates the specified value with the specified key in this cache. If the cache previously contained a
     * mapping for the key, the old value is replaced, and it's frequency is incremented. If adding the new mapping
     * would cause the cache to exceed its capacity, the least frequently used mapping is evicted.
     *
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @throws NullPointerException if the key or value is null
     */
    @Override
    public void put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("Key or Value in LFU cache is null");
        }
        if (cache.containsKey(key)) {
            int frequency = frequencies.get(key);
            frequencies.replace(key, frequency + 1);
            orderedKeys.remove(key);
            cache.replace(key, value);
        } else {
            while (cache.size() >= capacity) {
                K evictedKey = orderedKeys.pollFirst();
                cache.remove(evictedKey);
                frequencies.remove(evictedKey);
            }
            frequencies.put(key, 1);
            cache.put(key, value);
        }
        orderedKeys.add(key);
    }

    /**
     * Returns the value to which the specified key is mapped.
     * If the key and value exist in the cache, their frequency is incremented.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped
     * @throws NullPointerException if the key is null
     * @throws EntityNotFoundException if the value is null
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        V value = cache.get(key);
        if (value == null) {
            throw new EntityNotFoundException("Key");
        }
        int frequency = frequencies.get(key);
        frequencies.put(key, frequency + 1);
        orderedKeys.remove(key);
        orderedKeys.add(key);
        return value;
    }

    /**
     * Removes the value to which the specified key is mapped.
     *
     * @param key the key whose associated value is to be removed
     * @throws NullPointerException if the key is null
     */
    @Override
    public void remove(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        cache.remove(key);
        frequencies.remove(key);
        orderedKeys.remove(key);
    }

    /**
     * Returns a collection of cache values.
     *
     * @return a collection of cache values
     */
    @Override
    public Collection<V> values() {
        return cache.values();
    }

    /**
     * Checks if this key is in the cache.
     *
     * @return a Boolean value of whether the key is in the cache
     */
    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    /**
     * Returns the current size of cache.
     *
     * @return the current size of cache
     */
    @Override
    public int size() {
        return cache.size();
    }

    /**
     * Checks if the current cache is empty.
     *
     * @return a Boolean value of whether the current cache is empty
     */
    @Override
    public boolean isEmpty() {
        return orderedKeys.isEmpty();
    }

    /**
     * Clears the current cache.
     */
    @Override
    public void clear() {
        cache.clear();
        frequencies.clear();
        orderedKeys.clear();
    }

    /**
     * Returns the contents of the cache.
     *
     * @return the contents of the cache
     */
    @Override
    public String toString() {
        return "LfuCache{" +
                "cache=" + cache +
                '}';
    }
}
