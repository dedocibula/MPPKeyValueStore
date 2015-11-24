package com.javarockstars.mpp.benchmarks.datastructures;

import com.javarockstars.mpp.benchmarks.BenchmarkConstants;
import com.javarockstars.mpp.datastructures.api.LockFreeMap;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Author: dedocibula
 * Created on: 23.11.2015.
 */
public abstract class LockFreeMapBenchmark {
    private LockFreeMap<String, String> lockFreeMap;

    private ExecutorService executorService;
    private Collection<Callable<Void>> actions;
    private Random random;

    public LockFreeMapBenchmark(LockFreeMap<String, String> lockFreeMap) {
        this.lockFreeMap = lockFreeMap;
    }

    protected void setupBenchmark() {
        executorService = Executors.newFixedThreadPool(BenchmarkConstants.CONCURRENCY);
        actions = new ArrayList<>();
        random = new Random();
    }

    protected void tearDownBenchmark() throws Exception {
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    protected void fillMap() {
        for (int i = 0; i < BenchmarkConstants.MAX_KEY; i++) {
            /*
             * Convert integer to string to generate hash code and thereby
             * increasing the chances of collisions. Hence replicating real life
             * scenarios.
             */
            delayOnSuccess(lockFreeMap.put(Integer.toString(i), BenchmarkConstants.DUMMY_VALUE));
        }
    }

    protected void getValues() {
        for (int i = 0; i < BenchmarkConstants.MAX_KEY; i++) {
            lockFreeMap.get(Integer.toString(i));
        }
    }

    protected void clearMap() {
        for (int i = 0; i < BenchmarkConstants.MAX_KEY; i++) {
            delayOnSuccess(lockFreeMap.remove(Integer.toString(i)));
        }
    }

    protected void executeActions() {
        try {
            executorService.invokeAll(actions);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setupActions(Runnable action) {
        actions.clear();
        IntStream.range(0, BenchmarkConstants.CONCURRENCY)
                .mapToObj(i -> (Callable) Executors.callable(action))
                .forEach(actions::add);
    }

    private void delayOnSuccess(boolean success) {
        try {
            if (success)
                Thread.sleep(Duration.ofNanos(Math.abs(random.nextInt(200))).toMillis());
        } catch (InterruptedException ignored) {
        }
    }
}
