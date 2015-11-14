package com.javarockstars.mpp.store.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.javarockstars.mpp.api.LockFreeMap;

/**
 * Performs client tasks on the {@link CustomKeyValueStoreServer}
 * asynchronously.
 * 
 * @author shivam.maharshi
 *
 * @param <K>
 * @param <V>
 */
public class ClientTask<K, V> implements Runnable {
	private Socket socket;
	private LockFreeMap<K, V> map;
	// Receive request from the client.
	private DataInputStream input;
	// Send response to the client.
	private DataOutputStream output;

	public ClientTask(Socket socket, LockFreeMap<K, V> map) {
		this.socket = socket;
		this.map = map;
		try {
			this.input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		/*
		 * TODO: Add the operation ADD/GET/DELETE to be performed to the map
		 * object passed by the server and the result to be returned.
		 */
	}

}
