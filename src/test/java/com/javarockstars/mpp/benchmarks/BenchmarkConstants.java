package com.javarockstars.mpp.benchmarks;

/**
 * This class is responsible for holding all the constant values.
 *
 * @author shivam-maharshi
 */
public interface BenchmarkConstants {

    // Dummy map value.
    String DUMMY_VALUE = "This is suppose to be a big large data. Two things are infinite: the universe and human stupidity; and I'm not sure about the universe. Science without religion is lame, religion without science is blind";

    // Maximum key value present in the map.
    int MAX_KEY = 10000;

    // Number of threads to be spawned for the same operation.
    int THREAD_CONCURRENCY = 100;

    // Initial map capacity.
    int INITIAL_CAPACITY = 10000;

    // Number of key-value store clients.
    int CLIENTS = 10;

    // Number of operations per client.
    int OPERATIONS = 1000;

    String SERVER_HOSTNAME = "localhost";

    int MPP_PORT = 9999;

    int REDIS_PORT = 6379;

    int MEMCACHED_PORT = 11211;
}
