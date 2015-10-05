package com.javarockstars.mpp.structures;

import java.util.Objects;

/**
 * Author: dedocibula
 * Created on: 5.10.2015.
 */
class LockFreeKVList<K, V> implements Bucket<Node<K, V>> {
    private Node<K, V> head;

    LockFreeKVList() {
        this.head = new Node<>(-1, null, null);
        this.head.setNext(new Node<>(-1, null, null));
    }

    @Override
    public boolean insert(Node<K, V> entry) {
        while (true) {
            Window<K, V> window = find(entry.key);
            Node<K, V> prev = window.prev, curr = window.curr;
            if (entry.key.equals(curr.key))
                return false;
            entry.setNext(curr);
            if (prev.next.compareAndSet(curr, entry, false, false))
                return true;
        }
    }

    @Override
    public boolean delete(Object key) {
        boolean snip;
        while (true) {
            Window<K, V> window = find(key);
            Node<K, V> prev = window.prev, curr = window.curr;
            if (!key.equals(curr.key))
                return false;
            Node<K, V> succ = curr.next.getReference();
            snip = curr.next.compareAndSet(succ, succ, false, true);
            if (!snip)
                continue;
            prev.next.compareAndSet(curr, succ, false, false);
            return true;
        }
    }

    @Override
    public boolean contains(Object key) {
        boolean[] marked = {false};
        Node<K, V> curr = head.next.get(marked);
        while (curr.key != null && !curr.key.equals(key))
            curr = curr.next.get(marked);
        return (curr.key != null && curr.key.equals(key) && !marked[0]);
    }

    private Window<K, V> find(final Object key) {
        Node<K, V> prev, curr, succ;
        boolean[] marked = {false};
        boolean snip;
        retry:
        while (true) {
            prev = head;
            curr = prev.next.getReference();
            while (true) {
                succ = curr.next.get(marked);
                while (marked[0]) {
                    snip = prev.next.compareAndSet(curr, succ, false, false);
                    if (!snip) continue retry;
                    curr = succ;
                    succ = curr.next.get(marked);
                }
                if (curr.key == null || curr.key.equals(key))
                    return new Window<K, V>(prev, curr);
                prev = curr;
                curr = succ;
            }
        }
    }

    private static class Window<K, V> {
        final Node<K, V> prev;
        final Node<K, V> curr;

        private Window(Node<K, V> prev, Node<K, V> curr) {
            this.prev = prev;
            this.curr = curr;
        }
    }
}
