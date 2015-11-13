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
	public static final int CONCURRENCY = 30000;
}
