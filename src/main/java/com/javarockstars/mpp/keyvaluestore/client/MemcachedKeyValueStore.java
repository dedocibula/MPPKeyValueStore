package com.javarockstars.mpp.keyvaluestore.client;

import com.javarockstars.mpp.keyvaluestore.Constants;
import com.javarockstars.mpp.keyvaluestore.api.KeyValueStore;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

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
    public boolean add(String key, V value) {
        OperationFuture<Boolean> future = client.add(key, Constants.TIMEOUT, value.toString());
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String key) {
        OperationFuture<Boolean> future = client.delete(key);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

}
