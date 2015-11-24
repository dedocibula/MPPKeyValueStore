package com.javarockstars.mpp.store.api;

/**
 * This class represents the operations offered by our basic key value server.
 * 
 * @author shivam.maharshi
 *
 * @param <K>
 * @param <V>
 */
public interface KeyValueStore<K, V> {

	V get(K key);

	boolean add(K key, V value);

	boolean delete(K key);
}
