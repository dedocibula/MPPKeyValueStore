package com.javarockstars.mpp.keyvaluestore.example;

import com.javarockstars.mpp.datastructures.util.Constants;
import com.javarockstars.mpp.keyvaluestore.client.MPPClient;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Author: dedocibula Created on: 25.11.2015.
 */
public class ClientExample {
	public static final String DUMMY_DATA = "This is suppose to be a big large data. Two things are infinite: the universe and human stupidity; and I'm not sure about the universe. Science without religion is lame, religion without science is blind";

	public static void main(String[] args) throws Exception {
		Executors.newSingleThreadExecutor().submit(() -> {
			try {
				ServerExample.main(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		MPPClient client = new MPPClient(new InetSocketAddress("localhost", 9988));
		for (int i = 0; i < 10000; i++) {
			String key = "" + (int) (Math.random() * Constants.MAX_KEY);
			System.out.println("Key : " + key);
			System.out.println(client.add(key, DUMMY_DATA));
		}
		// System.out.println(client.add("Dude", "Blabla"));
		// System.out.println(client.add("Dude", "Trololo"));
		// System.out.println(client.get("Dude", String.class));
		// System.out.println(client.delete("Dude"));
		// System.out.println(client.get("Dude", String.class));
		// System.out.println(client.delete("Dude"));
		client.close();
	}
}
