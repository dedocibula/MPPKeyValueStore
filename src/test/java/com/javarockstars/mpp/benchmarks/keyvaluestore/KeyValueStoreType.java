package com.javarockstars.mpp.benchmarks.keyvaluestore;

import java.net.InetSocketAddress;

import com.javarockstars.mpp.keyvaluestore.api.KeyValueStoreClient;
import com.javarockstars.mpp.keyvaluestore.client.MPPClient;
import com.javarockstars.mpp.keyvaluestore.client.MemcachedKeyValueStoreClient;
import com.javarockstars.mpp.keyvaluestore.client.RedisKeyValueStoreClient;

/**
 * This class stores the different key value store type. This is a util class
 * required for benchmarking.
 * 
 * @author shivam.maharshi
 *
 */
public enum KeyValueStoreType {

	MEMCACHED, REDIS, MPP ;

	public static KeyValueStoreType getEnum(String type) {
		for (KeyValueStoreType t : KeyValueStoreType.values()) {
			if (t.name().equalsIgnoreCase(type)) {
				return t;
			}
		}
		throw new RuntimeException("Invalid type");
	}

	public KeyValueStoreClient getClient(InetSocketAddress add) throws Exception {
		switch (this) {
		case MEMCACHED:
			return new MemcachedKeyValueStoreClient(add);
		case REDIS:
			return new RedisKeyValueStoreClient(add);
		case MPP:
			return new MPPClient(add);
		default:
			throw new RuntimeException("Invalid key value store type.");
		}
	}

}
