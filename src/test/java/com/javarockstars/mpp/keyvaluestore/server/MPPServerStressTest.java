package com.javarockstars.mpp.keyvaluestore.server;

import com.javarockstars.mpp.datastructures.api.LockFreeMap;
import com.javarockstars.mpp.datastructures.implementation.MichaelLockFreeHashMap;
import com.javarockstars.mpp.keyvaluestore.client.MPPClient;
import com.javarockstars.mpp.keyvaluestore.implementation.MPPLFMapCommandProcessor;
import org.junit.*;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Author: dedocibula
 * Created on: 26.11.2015.
 */
public class MPPServerStressTest extends Assert {
    private static ExecutorService executorService;
    private static MPPServer server;
    private static LockFreeMap<String, Serializable> lockFreeMap;
    private static Collection<MPPClient> clients;
    private static InetSocketAddress address = new InetSocketAddress("localhost", 1234);

    @BeforeClass
    public static void setUpClass() throws Exception {
        executorService = Executors.newFixedThreadPool(10);
        lockFreeMap = new MichaelLockFreeHashMap<>();
        server = new MPPServer(address, () -> new MPPLFMapCommandProcessor(lockFreeMap), Executors.newFixedThreadPool(10));
        server.start();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        server.stop();
        server = null;
    }

    @Before
    public void setUp() throws Exception {
        lockFreeMap = new MichaelLockFreeHashMap<>();
        clients = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            clients.add(new MPPClient(address));
        }
    }

    @After
    public void tearDown() throws Exception {
        for (MPPClient client : clients) {
            client.close();
        }
        clients = null;
        lockFreeMap = null;
    }

    @Test
    public void testAdd() throws InterruptedException {
        assertThat(lockFreeMap.size(), is(0));
        List<Future<List<Boolean>>> futures = executorService.invokeAll(generateCallables((key, c) -> c.add(key, "Some random value")));
        futures.forEach(f -> assertThat(f.isDone(), is(true)));
        assertThat(lockFreeMap.size(), is(not(0)));
        assertThat(lockFreeMap.size(), is(10000));
        IntStream.range(0, 10000).forEach(i -> assertThat(lockFreeMap.contains(Integer.toString(i)), is(true)));
    }

    @Test
    public void testDelete() throws InterruptedException {
        fill(lockFreeMap, 5000);
        assertThat(lockFreeMap.size(), is(5000));
        List<Future<List<Boolean>>> futures = executorService.invokeAll(generateCallables((key, c) -> c.delete(key)));
        futures.forEach(f -> assertThat(f.isDone(), is(true)));
        assertThat(lockFreeMap.size(), is(not(5000)));
        assertThat(lockFreeMap.size(), is(0));
        IntStream.range(0, 10000).forEach(i -> assertThat(lockFreeMap.contains(Integer.toString(i)), is(false)));
    }

    private void fill(LockFreeMap<String, Serializable> map, int bound) {
        IntStream.range(0, bound).forEach(i -> map.put(Integer.toString(i), "Some random value"));
    }

    private List<Callable<List<Boolean>>> generateCallables(BiFunction<String, MPPClient, Boolean> function) {
        return clients.stream()
                .map(client -> (Callable<List<Boolean>>) () -> IntStream.range(0, 10000)
                        .mapToObj(i -> function.apply(Integer.toString(i), client))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}