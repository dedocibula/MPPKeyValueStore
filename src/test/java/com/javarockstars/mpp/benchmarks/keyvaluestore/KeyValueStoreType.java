package com.javarockstars.mpp.benchmarks.keyvaluestore;

import java.net.InetSocketAddress;
import java.util.Objects;

import com.javarockstars.mpp.keyvaluestore.api.KeyValueStoreClient;
import com.javarockstars.mpp.keyvaluestore.client.MPPClient;
import com.javarockstars.mpp.keyvaluestore.client.MemcachedKeyValueStoreClient;
import com.javarockstars.mpp.keyvaluestore.client.RedisKeyValueStoreClient;

/**
 * This class stores the different key value store type. This is a util class
 * required for benchmarking.
 *
 * @author shivam.maharshi
 */
public enum KeyValueStoreType {
    MEMCACHED,
    REDIS,
    MPP;

    public KeyValueStoreClient getClient(final InetSocketAddress address) throws Exception {
        Objects.requireNonNull(address);
        switch (this) {
            case MEMCACHED:
                return new MemcachedKeyValueStoreClient(address);
            case REDIS:
                return new RedisKeyValueStoreClient(address);
            case MPP:
                return new MPPClient(address);
            default:
                throw new IllegalStateException("Invalid key value store type.");
        }
    }

}
