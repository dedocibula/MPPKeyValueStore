package com.javarockstars.mpp.store.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.javarockstars.mpp.api.LockFreeMap;
import com.javarockstars.mpp.store.api.KeyValueStore;

/**
 * This class is responsible for exposing a custom key value store client by
 * implementing {@link KeyValueStore} class. It can utilize any map that
 * implements {@link LockFreeMap} interface. Different types of maps can be
 * evaluated as key value store using this wrapper.
 * 
 * @author shivam.maharshi
 */
public class CustomKeyValueStore<K, V> implements KeyValueStore<K, V> {
	private Socket socket;
	// Receive response from server.
	private DataInputStream input;
	// Send request to the server.
	private DataOutputStream output;

	public CustomKeyValueStore(String hostname, int port) {
		try {
			this.socket = new Socket(hostname, port);
			this.input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public V get(K key) {
		/*
		 * TODO: Will form appropriate request and communicate with the
		 * CustomKeyValueStore server over socket connection for GET operation.
		 */
		return null;
	}

	@Override
	public Boolean add(K key, V value) {
		/*
		 * TODO: Will form appropriate request and communicate with the
		 * CustomKeyValueStore server over socket connection for ADD operation.
		 */
		return null;
	}

	@Override
	public Boolean delete(K key) {
		/*
		 * TODO: Will form appropriate request and communicate with the
		 * CustomKeyValueStore server over socket connection for DELETE
		 * operation.
		 */
		return null;
	}

}