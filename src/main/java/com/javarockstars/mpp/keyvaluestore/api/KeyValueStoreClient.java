package com.javarockstars.mpp.keyvaluestore.api;

import java.io.Closeable;

/**
 * This class represents the operations offered by our basic key value server.
 * 
 * @author shivam.maharshi
 *
 */
public interface KeyValueStoreClient extends Closeable {

	<V> V get(final String key, final Class<V> valueType);

	<V> boolean add(final String key, final V value);

	boolean delete(final String key);
}
