package com.javarockstars.mpp.store.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.javarockstars.mpp.api.LockFreeMap;
import com.javarockstars.mpp.utills.Constants;

/**
 * This class is responsible for instantiating a our custom key value server
 * server analogous to Memcached. It can utilize any map that implements
 * {@link LockFreeMap} interface to server key value pairs. The performance of
 * different types of maps can be evaluated while serving as a key value server
 * server, using this implementation.
 * 
 * @author shivam.maharshi
 *
 * @param <K>
 * @param <V>
 */
public class CustomKeyValueStoreServer<K, V> {
	private LockFreeMap<K, V> map;
	private ServerSocket serverSocket;
	private ExecutorService executorService = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);

	public CustomKeyValueStoreServer(LockFreeMap<K, V> map, int port) {
		this.map = map;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startServer() {
		try {
			receiveConnections();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receiveConnections() throws IOException {
		while (true) {
			Socket socket = serverSocket.accept();
			ClientTask<K, V> tasks = new ClientTask<>(socket, map);
			/*
			 * Delegates the client tasks to a thread from thread pool for
			 * asynchronous execution.
			 */
			executorService.execute(tasks);
		}
	}

}
