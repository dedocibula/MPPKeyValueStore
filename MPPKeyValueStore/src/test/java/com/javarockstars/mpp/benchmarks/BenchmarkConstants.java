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
}
