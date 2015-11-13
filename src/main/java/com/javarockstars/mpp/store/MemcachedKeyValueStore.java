package com.javarockstars.mpp.store;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

import com.javarockstars.mpp.store.api.KeyValueStore;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

/**
 * 
 * @author shivam.maharshi
 */
public class MemcachedKeyValueStore<java.lang.String, V> implements KeyValueStore<String, V> {

	private MemcachedClient client;

	public MemcachedKeyValueStore(String hostname, int port) {
		try {
			client = new MemcachedClient(new InetSocketAddress(hostname, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void test() {
		try {

			MemcachedClient client = new MemcachedClient(new InetSocketAddress("localhost", 11211));
			OperationFuture<Boolean> future = client.add("key", 3600, new String("helloShivam"));
			Object getfuture = client.get("key");
			OperationFuture<Boolean> deletefuture = client.delete("key");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MemcachedKeyValueStore client = new MemcachedKeyValueStore();
		client.test();
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(String key) {
		return (V) client.get(key);
	}

	@Override
	public Boolean add(String key) {
		OperationFuture<Boolean> future = client.add(key, 3600, new String("helloShivam"));
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean delete(String key) {
		OperationFuture<Boolean> future = client.delete((String)key);
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}

}
