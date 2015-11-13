package com.javarockstars.mpp.store.api;

public interface KeyValueStore<K,V> {
	
	public V get(K key);
	
	public Boolean add(K key);
	
	public Boolean delete(K key);

}
