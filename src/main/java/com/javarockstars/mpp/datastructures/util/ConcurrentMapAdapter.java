package com.javarockstars.mpp.datastructures.util;

import com.javarockstars.mpp.datastructures.api.LockFreeMap;

import java.util.concurrent.ConcurrentMap;

/**
 * Author: dedocibula, shivam-maharshi
 * Created on: 4.11.2015.
 */
public final class ConcurrentMapAdapter<K, V> implements LockFreeMap<K, V> {

    private final ConcurrentMap<K, V> map;

    public ConcurrentMapAdapter(ConcurrentMap<K, V> map) {
        this.map = map;
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public boolean put(K key, V value) {
        return map.put(key, value) == null;
    }

    @Override
    public boolean remove(K key) {
        return map.remove(key) != null;
    }

    @Override
    public boolean contains(K key) {
        return map.containsKey(key);
    }

    @Override
    public int size() {
        return map.size();
    }
}
