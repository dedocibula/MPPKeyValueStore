package com.javarockstars.mpp.store;

import java.net.InetSocketAddress;

import net.spy.memcached.MemcachedClient;

public class MemcachedSpyClientTest {

	public void test() {
		try {
			MemcachedClient client = new MemcachedClient(new InetSocketAddress("localhost", 11211));
			client.add("key", 3600, new String("helloShivam"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MemcachedSpyClientTest client = new MemcachedSpyClientTest();
		client.test();
	}

}
