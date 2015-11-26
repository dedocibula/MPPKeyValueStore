package com.javarockstars.mpp.keyvaluestore.implementation;

import com.javarockstars.mpp.keyvaluestore.api.KeyValueStoreClient;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * This class is responsible for providing a wrapper around the famous spy
 * clients for Memcached. This wrapper helps us use our custom standard key
 * value server interface for benchmarking.
 *
 * @author shivam.maharshi
 */
public class MemcachedKeyValueStoreClient implements KeyValueStoreClient {

    private MemcachedClient client;

    public MemcachedKeyValueStoreClient(String hostname, int port) {
        try {
            client = new MemcachedClient(new InetSocketAddress(hostname, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <K, V> V get(K key, Class<V> valueType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, V> boolean add(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K> boolean delete(K key) {
        throw new UnsupportedOperationException();
    }
}
