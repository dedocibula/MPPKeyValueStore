package com.javarockstars.mpp.structures;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Author: dedocibula
 * Created on: 5.10.2015.
 */
public class MichaelLockFreeHashMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_CAPACITY = 16;

    private final int capacity;
    private Bucket<Node<K, V>>[] table;

    public MichaelLockFreeHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public MichaelLockFreeHashMap(int capacity) {
        if (capacity < DEFAULT_CAPACITY)
            capacity = DEFAULT_CAPACITY;
        this.capacity = capacity;
        initializeTable();
    }

    @Override
    public int size() {
        // TODO
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        Objects.requireNonNull(key);
        return table[getHash(key)].contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(Object key) {
        // TODO
        return null;
    }

    @Override
    public V put(K key, V value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        int hash = getHash(key);
        Node<K, V> node = new Node<>(hash, key, value);
        if (table[hash].insert(node))
            return value;
        return null;
    }

    @Override
    public V remove(Object key) {
        Objects.requireNonNull(key);
        // TODO extract
        if (table[getHash(key)].delete(key))
            return null;
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        // TODO
    }

    @Override
    public void clear() {
        // TODO
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    private void initializeTable() {
        //noinspection unchecked
        table = new LockFreeKVList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LockFreeKVList<>();
        }
    }

    private int getHash(Object key) {
        // TODO naive implementation
        return key.hashCode() % capacity;
    }
}
