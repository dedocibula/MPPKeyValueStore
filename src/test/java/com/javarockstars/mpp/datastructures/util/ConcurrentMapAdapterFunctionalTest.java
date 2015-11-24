package com.javarockstars.mpp.datastructures.util;

import com.javarockstars.mpp.datastructures.api.LockFreeMap;
import com.javarockstars.mpp.datastructures.implementation.MichaelLockFreeHashMap;
import org.junit.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Author: dedocibula
 * Created on: 24.11.2015.
 */
public class ConcurrentMapAdapterFunctionalTest extends Assert {
    private static ExecutorService executorService;
    private static Random random;

    private LockFreeMap<Integer, String> javaMap;
    private LockFreeMap<Integer, String> michaelMap;

    @BeforeClass
    public static void setUpClass() {
        executorService = Executors.newFixedThreadPool(10);
        random = new Random();
    }

    @AfterClass
    public static void tearDownClass() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        random = null;
    }

    @Before
    public void setUp() {
        javaMap = new ConcurrentMapAdapter<>(new ConcurrentHashMap<>());
        michaelMap = new MichaelLockFreeHashMap<>();
    }

    @After
    public void tearDown() {
        javaMap = null;
        michaelMap = null;
    }

    @Test
    public void testPut() throws InterruptedException {
        List<Future<List<Boolean>>> javaMapFutures = executorService.invokeAll(generatePutCallables(javaMap));
        List<Future<List<Boolean>>> michaelMapFutures = executorService.invokeAll(generatePutCallables(michaelMap));
        assertThat(javaMapFutures.size(), is(equalTo(michaelMapFutures.size())));
        javaMapFutures.forEach(f -> assertThat(f.isDone(), is(true)));
        michaelMapFutures.forEach(f -> assertThat(f.isDone(), is(true)));
        int[] javaResults = evaluate(javaMapFutures);
        int[] michaelResults = evaluate(javaMapFutures);
        assertThat(javaResults[0], is(equalTo(michaelResults[0])));
        assertThat(javaResults[1], is(equalTo(michaelResults[1])));
    }

    @Test
    public void testRemove() throws InterruptedException {
        fill(javaMap, 5000);
        fill(michaelMap, 5000);
        List<Future<List<Boolean>>> javaMapFutures = executorService.invokeAll(generateRemoveCallables(javaMap));
        List<Future<List<Boolean>>> michaelMapFutures = executorService.invokeAll(generateRemoveCallables(michaelMap));
        assertThat(javaMapFutures.size(), is(equalTo(michaelMapFutures.size())));
        javaMapFutures.forEach(f -> assertThat(f.isDone(), is(true)));
        michaelMapFutures.forEach(f -> assertThat(f.isDone(), is(true)));
        int[] javaResults = evaluate(javaMapFutures);
        int[] michaelResults = evaluate(javaMapFutures);
        assertThat(javaResults[0], is(equalTo(michaelResults[0])));
        assertThat(javaResults[1], is(equalTo(michaelResults[1])));
    }

    private int[] evaluate(List<Future<List<Boolean>>> futures) {
        int success = 0, failure = 0;
        for (Future<List<Boolean>> future : futures) {
            try {
                List<Boolean> value = future.get();
                for (Boolean result : value) {
                    if (result.equals(true))
                        success++;
                    else if (result.equals(false))
                        failure++;
                    else
                        fail();
                }
            } catch (InterruptedException | ExecutionException e) {
                fail();
            }
        }
        return new int[]{success, failure};
    }

    private void fill(LockFreeMap<Integer, String> map, int bound) {
        IntStream.range(0, bound).forEach(i -> map.put(i, "Lol"));
    }

    private List<Callable<List<Boolean>>> generatePutCallables(LockFreeMap<Integer, String> map) {
        return IntStream.range(0, 10)
                .mapToObj(ingored -> (Callable<List<Boolean>>) () -> IntStream.range(0, 10000)
                        .mapToObj(withDelay(i -> map.put(i, "Lol")))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private List<Callable<List<Boolean>>> generateRemoveCallables(LockFreeMap<Integer, String> map) {
        return IntStream.range(0, 10)
                .mapToObj(ingored -> (Callable<List<Boolean>>) () -> IntStream.range(0, 10000)
                        .mapToObj(withDelay(map::remove))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private IntFunction<Boolean> withDelay(IntFunction<Boolean> function) {
        return (value) -> {
            boolean result = function.apply(value);
            if (result)
                sleep();
            return result;
        };
    }

    private void sleep() {
        try {
            Thread.sleep(Math.abs(random.nextLong()) % 3);
        } catch (InterruptedException ignored) {
        }
    }
}