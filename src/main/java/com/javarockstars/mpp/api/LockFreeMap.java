package com.javarockstars.mpp.api;

/**
 * Author: dedocibula
 * Created on: 5.10.2015.
 */
public interface LockFreeMap<K, V> {

    /**
     * Fetches value for passed key. If the map doesn't contain the key null is returned.
     *
     * @param key key
     * @return value
     */
    V get(final K key);

    /**
     * Inserts given key/value pair.
     *
     * @param key key
     * @param value value
     * @return true if insertion succeeded, false otherwise
     */
    boolean put(final K key, final V value);

    /**
     * Removes value for given key.
     *
     * @param key key
     * @return true if deletion succeeded, false otherwise
     */
    boolean remove(final K key);

    /**
     * Returns true if map contains given key.
     *
     * @param key key
     * @return true if the key was found
     */
    boolean contains(final K key);

    /**
     * Returns a number of keys in the map.
     *
     * @return size
     */
    int size();
}
