package com.javarockstars.mpp.datastructures.common;

import java.util.Map;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * Author: dedocibula
 * Created on: 5.10.2015.
 */
public class Node<K, V> implements Map.Entry<K, V> {
    final int hash;
    final K key;
    V value;
    AtomicMarkableReference<Node<K, V>> next;

    public Node(int hash, K key, V value) {
        this.hash = hash;
        this.key = key;
        this.value = value;
    }

    Node(int hash, K key, V value, Node<K, V> next) {
        this(hash, key, value);
        setNext(next);
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        throw new UnsupportedOperationException();
    }

    void setNext(Node<K, V> next) {
        this.next = new AtomicMarkableReference<>(next, false);
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    @Override
    public boolean equals(Object o) {
        Object k, v, u;
        Map.Entry<?, ?> e;
        return ((o instanceof Map.Entry) &&
                (k = (e = (Map.Entry<?, ?>) o).getKey()) != null &&
                (v = e.getValue()) != null &&
                (k == key || k.equals(key)) &&
                (v == (u = value) || v.equals(u)));
    }

    @Override
    public int hashCode() {
        return key.hashCode() ^ value.hashCode();
    }

    public AtomicMarkableReference<Node<K, V>> getNext() {
        return this.next;
    }
}
