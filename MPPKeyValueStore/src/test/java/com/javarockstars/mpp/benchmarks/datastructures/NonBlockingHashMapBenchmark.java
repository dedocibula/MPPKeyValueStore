package com.javarockstars.mpp.benchmarks.datastructures;

import com.javarockstars.mpp.benchmarks.BenchmarkConstants;
import com.javarockstars.mpp.datastructures.util.ConcurrentMapAdapter;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.perfidix.annotation.*;

/**
 * This class is responsible for generating the benchmarking results for
 * {@link NonBlockingHashMap} under varying scenarios.
 *
 * @author shivam-maharshi
 */
public class NonBlockingHashMapBenchmark extends LockFreeMapBenchmark {
    public NonBlockingHashMapBenchmark() {
        super(new ConcurrentMapAdapter<>(new NonBlockingHashMap<>(BenchmarkConstants.INITIAL_CAPACITY)));
    }

    @Override
    @BeforeBenchClass
    public void setupBenchmark() {
        super.setupBenchmark();
    }

    @Bench(beforeEachRun = "prepareGet")
    public void get() {
        executeActions();
    }

    @Bench(beforeEachRun = "preparePut")
    public void put() {
        executeActions();
    }

    @Bench(beforeEachRun = "prepareRemove")
    public void remove() {
        executeActions();
    }

    @SkipBench
    public void prepareGet() {
        fillMap();
        setupActions(this::getValues);
    }

    @SkipBench
    public void preparePut() {
        clearMap();
        setupActions(this::fillMap);
    }

    @SkipBench
    public void prepareRemove() {
        fillMap();
        setupActions(this::clearMap);
    }

    @Override
    @AfterBenchClass
    public void tearDownBenchmark() throws Exception {
        super.tearDownBenchmark();
    }
}
