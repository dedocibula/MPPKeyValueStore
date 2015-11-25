package com.javarockstars.mpp.keyvaluestore.client;

import com.javarockstars.mpp.datastructures.api.LockFreeMap;
import com.javarockstars.mpp.keyvaluestore.api.KeyValueStore;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This class is responsible for exposing a custom key value server client by
 * implementing {@link KeyValueStore} class. It can utilize any map that
 * implements {@link LockFreeMap} interface. Different types of maps can be
 * evaluated as key value server using this wrapper.
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
	public boolean add(K key, V value) {
		/*
		 * TODO: Will form appropriate request and communicate with the
		 * CustomKeyValueStore server over socket connection for ADD operation.
		 */
		return false;
	}

	@Override
	public boolean delete(K key) {
		/*
		 * TODO: Will form appropriate request and communicate with the
		 * CustomKeyValueStore server over socket connection for DELETE
		 * operation.
		 */
		return false;
	}

}
