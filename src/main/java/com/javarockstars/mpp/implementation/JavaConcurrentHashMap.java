package com.javarockstars.mpp.implementation;

import com.javarockstars.mpp.api.LockFreeMap;

import java.util.concurrent.ConcurrentMap;

/**
 * Author: dedocibula
 * Created on: 4.11.2015.
 */
public class JavaConcurrentHashMap<K, V> implements LockFreeMap<K, V> {

    private final ConcurrentMap<K, V> map;

    public JavaConcurrentHashMap(ConcurrentMap<K, V> map) {
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
