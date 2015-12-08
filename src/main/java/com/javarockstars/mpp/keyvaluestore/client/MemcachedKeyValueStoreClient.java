package com.javarockstars.mpp.keyvaluestore.client;

import com.javarockstars.mpp.keyvaluestore.Constants;
import com.javarockstars.mpp.keyvaluestore.api.KeyValueStoreClient;
import com.javarockstars.mpp.keyvaluestore.util.SerializationHelper;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Objects;
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

    public MemcachedKeyValueStoreClient(final InetSocketAddress address) throws Exception {
        Objects.requireNonNull(address);
        client = new MemcachedClient(address);
    }

    @Override
    public <V extends Serializable> V get(final String key, final Class<V> valueType) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(valueType);
        return SerializationHelper.deserialize((byte[]) client.get(key), valueType);
    }

    @Override
    public <V extends Serializable> boolean add(final String key, final V value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        boolean result = false;
        try {
            result = client.add(key, Constants.TIMEOUT, SerializationHelper.serialize(value)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delete(final String key) {
        Objects.requireNonNull(key);
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
