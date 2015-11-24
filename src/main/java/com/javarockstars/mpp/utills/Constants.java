package com.javarockstars.mpp.utills;

/**
 * This class is responsible for holding all the constant values.
 * 
 * @author shivam-maharshi
 */
public final class Constants {

	private Constants() {
		// Avoid instantiation.
	}

	public static final String DUMMY_VALUE = "This is a big long text to mirror real life scenarios for concurrent map evaluations.";
	// Maximum key value present in the map.
	public static final int MAX_KEY = 10000;
	// Number of threads to be spawned for the same operation.
	public static final int CONCURRENCY = 10;
	// Timeout in ms for Spy Memcached client for add operation.
	public static final int TIMEOUT = 3600;
	// The thread pool size for our custom key value server server.
	public static final int THREAD_POOL_SIZE = 50;
}
