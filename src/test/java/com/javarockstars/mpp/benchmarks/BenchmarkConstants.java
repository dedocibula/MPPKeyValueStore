package com.javarockstars.mpp.benchmarks;

/**
 * This class is responsible for holding all the constant values.
 *
 * @author shivam-maharshi
 */
public interface BenchmarkConstants {

    // Dummy map value.
    String DUMMY_VALUE = "This is a big long text to mirror real life scenarios for concurrent map evaluations.";

    // Maximum key value present in the map.
    int MAX_KEY = 10000;

    // Number of threads to be spawned for the same operation.
    int CONCURRENCY = 100;

    // Initial map capacity.
    int INITIAL_CAPACITY = 10000;
    
    public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String STORE_TYPE = "store";
	public static final String OPERATION = "op";
	public static final String GET = "get";
	public static final String PUT = "put";
	public static final String DELETE = "delete";
	public static final String DUMMY_DATA = "This is suppose to be a big large data. Two things are infinite: the universe and human stupidity; and I'm not sure about the universe. Science without religion is lame, religion without science is blind";
	public static final String SUCCESS_MESSAGE = "Successfully performed action.";
}
