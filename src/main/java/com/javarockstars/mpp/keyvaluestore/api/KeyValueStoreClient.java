package com.javarockstars.mpp.keyvaluestore.api;

import java.io.Closeable;
import java.io.Serializable;

/**
 * This class represents the operations offered by our basic key value server.
 * 
 * @author shivam.maharshi
 *
 */
public interface KeyValueStoreClient extends Closeable {

	<V extends Serializable> V get(final String key, final Class<V> valueType);

	<V extends Serializable> boolean add(final String key, final V value);

	boolean delete(final String key);
}
