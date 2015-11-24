package com.javarockstars.mpp.datastructures.common;

/**
 * Author: dedocibula
 * Created on: 5.10.2015.
 */
public interface Bucket<T extends Node<K, V>, K, V> {
    /**
     * Inserts entry to the bucket.
     *
     * @param entry entry
     * @return true if insertion succeeded, false otherwise
     */
    boolean insert(final T entry);

    /**
     * Deletes entry associated with given node.
     *
     * @param key key
     * @return true if deletion succeeded, false otherwise
     */
    boolean delete(final K key);

    /**
     * Returns true if bucket contains given key.
     *
     * @param key key
     * @return true if the key was found
     */
    boolean contains(final K key);

    /**
     * Returns entry for given key. If the map doesn't contain the key null is returned.
     *
     * @param key key
     * @return entry
     */
    T get(final K key);
}
