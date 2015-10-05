package com.javarockstars.mpp.structures;

import com.javarockstars.mpp.api.LockFreeMap;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: dedocibula
 * Created on: 5.10.2015.
 */
public class MichaelLockFreeHashMap<K, V> implements LockFreeMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int HASH_BITS = 0x7fffffff;

    private final int capacity;
    private AtomicInteger size;
    private Bucket<Node<K, V>, K, V>[] table;

    public MichaelLockFreeHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public MichaelLockFreeHashMap(int capacity) {
        if (capacity < DEFAULT_CAPACITY)
            capacity = DEFAULT_CAPACITY;
        this.capacity = capacity;
        this.size = new AtomicInteger(0);
        initializeTable();
    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public V get(K key) {
        Objects.requireNonNull(key);
        Node<K, V> node = table[getHash(key)].get(key);
        return node == null || node.next.isMarked() ? null : node.getValue();
    }

    @Override
    public boolean put(K key, V value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        int hash = getHash(key);
        Node<K, V> node = new Node<>(hash, key, value);
        if (table[hash].insert(node)) {
            size.incrementAndGet();
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(K key) {
        Objects.requireNonNull(key);
        return table[getHash(key)].contains(key);
    }

    @Override
    public boolean remove(K key) {
        Objects.requireNonNull(key);
        if (table[getHash(key)].delete(key)) {
            size.decrementAndGet();
            return true;
        }
        return false;
    }

    private void initializeTable() {
        //noinspection unchecked
        table = new LockFreeKVList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LockFreeKVList<>();
        }
    }

    private int getHash(Object key) {
        int hash;
        return (((hash = key.hashCode()) ^ (hash >>> 16)) & HASH_BITS) % capacity;
    }
}
