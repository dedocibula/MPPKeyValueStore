package com.javarockstars.mpp.keyvaluestore.api;

/**
 * This class represents the operations offered by our basic key value server.
 * 
 * @author shivam.maharshi
 *
 */
public interface KeyValueStoreClient {

	<K, V> V get(final K key, final Class<V> valueType);

	<K, V> boolean add(final K key, final V value);

	<K> boolean delete(final K key);
}
