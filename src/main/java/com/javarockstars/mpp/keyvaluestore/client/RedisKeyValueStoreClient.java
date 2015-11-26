package com.javarockstars.mpp.keyvaluestore.client;

import com.javarockstars.mpp.keyvaluestore.Constants;
import com.javarockstars.mpp.keyvaluestore.api.KeyValueStoreClient;
import com.javarockstars.mpp.keyvaluestore.util.SerializationHelper;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * Author: dedocibula
 * Created on: 26.11.2015.
 */
public final class RedisKeyValueStoreClient implements KeyValueStoreClient {
    private Jedis client;

    public RedisKeyValueStoreClient(final InetSocketAddress address) {
        Objects.requireNonNull(address);
        this.client = new Jedis(address.getHostName(), address.getPort());
    }

    @Override
    public <V extends Serializable> V get(final String key, final Class<V> valueType) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(valueType);
        return SerializationHelper.deserialize(client.get(SerializationHelper.serialize(key)), valueType);
    }

    @Override
    public <V extends Serializable> boolean add(final String key, final V value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return Objects.equals(Constants.STATUS_OK,
                client.set(SerializationHelper.serialize(key), SerializationHelper.serialize(value)));
    }

    @Override
    public boolean delete(final String key) {
        Objects.requireNonNull(key);
        return client.del(SerializationHelper.serialize(key)) > 0;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
