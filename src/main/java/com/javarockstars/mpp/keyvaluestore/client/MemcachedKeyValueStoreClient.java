package com.javarockstars.mpp.keyvaluestore.client;

import com.javarockstars.mpp.keyvaluestore.Constants;
import com.javarockstars.mpp.keyvaluestore.api.KeyValueStoreClient;
import com.javarockstars.mpp.keyvaluestore.util.SerializationHelper;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

/**
 * This class is responsible for providing a wrapper around the famous spy
 * clients for Memcached. This wrapper helps us use our custom standard key
 * value server interface for benchmarking.
 *
 * @author shivam.maharshi
 */
public final class MemcachedKeyValueStoreClient implements KeyValueStoreClient {
	private MemcachedClient client;

	public MemcachedKeyValueStoreClient(String hostname, int port) throws Exception {
		client = new MemcachedClient(new InetSocketAddress(hostname, port));
	}

	@Override
	public <V> V get(String key, Class<V> valueType) {
		return SerializationHelper.deserialize((byte[]) client.get(key), valueType);
	}

	@Override
	public <V> boolean add(String key, V value) {
		boolean result = false;
		try {
			result = client.add(key, Constants.TIMEOUT, SerializationHelper.serialize(value)).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean delete(String key) {
		boolean result = false;
		try {
			result = client.delete(key).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void close() throws IOException {
		client.shutdown();
	}
}
