package com.javarockstars.mpp.store.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

import com.javarockstars.mpp.store.api.KeyValueStore;
import com.javarockstars.mpp.utills.Constants;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

/**
 * This class is responsible for providing a wrapper around the famous spy
 * clients for Memcached. This wrapper helps us use our custom standard key
 * value server interface for benchmarking.
 * 
 * @author shivam.maharshi
 */
public class MemcachedKeyValueStore<V> implements KeyValueStore<String, V> {

	private MemcachedClient client;

	public MemcachedKeyValueStore(String hostname, int port) {
		try {
			client = new MemcachedClient(new InetSocketAddress(hostname, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(String key) {
		return (V) client.get(key);
	}

	@Override
	public Boolean add(String key, V value) {
		OperationFuture<Boolean> future = client.add(key, Constants.TIMEOUT, value.toString());
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean delete(String key) {
		OperationFuture<Boolean> future = client.delete((String) key);
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}

}
