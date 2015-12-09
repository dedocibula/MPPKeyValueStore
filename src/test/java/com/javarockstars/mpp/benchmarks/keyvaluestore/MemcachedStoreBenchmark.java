package com.javarockstars.mpp.benchmarks.keyvaluestore;

import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.Bench;
import org.perfidix.annotation.SkipBench;

import java.net.InetSocketAddress;

import static com.javarockstars.mpp.benchmarks.BenchmarkConstants.MEMCACHED_PORT;
import static com.javarockstars.mpp.benchmarks.BenchmarkConstants.SERVER_HOSTNAME;

/**
 * Author: dedocibula
 * Created on: 9.12.2015.
 */
public class MemcachedStoreBenchmark extends KeyValueStoreBenchmark {
    public MemcachedStoreBenchmark() {
        super(KeyValueStoreType.MEMCACHED, new InetSocketAddress(SERVER_HOSTNAME, MEMCACHED_PORT));
    }

    @Override
    @BeforeBenchClass
    public void setupBenchmark() {
        super.setupBenchmark();
    }

    @Bench(beforeEachRun = "prepare_100_0_0", afterEachRun = "cleanup")
    public void get_100_add_0_delete_0() {
        executeOperations();
    }

    @Bench(beforeEachRun = "prepare_50_50_0", afterEachRun = "cleanup")
    public void get_50_add_50_delete_0() {
        executeOperations();
    }

    @Bench(beforeEachRun = "prepare_80_10_10", afterEachRun = "cleanup")
    public void get_80_add_10_delete_10() {
        executeOperations();
    }

    @SkipBench
    public void prepare_100_0_0() {
        setupOperations(100, 0, 0);
    }

    @SkipBench
    public void prepare_50_50_0() {
        setupOperations(50, 50, 0);
    }

    @SkipBench
    public void prepare_80_10_10() {
        setupOperations(80, 10, 10);
    }

    @SkipBench
    public void cleanup() {
        cleanupOperations();
    }

    @Override
    @AfterBenchClass
    public void tearDownBenchmark() throws Exception {
        super.tearDownBenchmark();
    }
}
