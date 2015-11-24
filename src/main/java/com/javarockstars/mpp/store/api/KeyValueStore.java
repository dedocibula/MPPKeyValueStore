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

	public V get(K key);

	public Boolean add(K key, V value);

	public Boolean delete(K key);

}
