package com.javarockstars.mpp.benchmarks.keyvaluestore;

import com.javarockstars.mpp.benchmarks.BenchmarkConstants;
import com.javarockstars.mpp.benchmarks.keyvaluestore.KeyValueStoreOperationGenerator.KeyValueStoreOperation;
import com.javarockstars.mpp.benchmarks.keyvaluestore.KeyValueStoreOperationGenerator.Pair;
import com.javarockstars.mpp.keyvaluestore.api.KeyValueStoreClient;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Responsible for creating a java request using the client to the various data stores.
 *
 * @author shivam.maharshi
 */
public abstract class KeyValueStoreBenchmark {
    private final KeyValueStoreType storeType;
    private final InetSocketAddress address;

    private ExecutorService executorService;
    private Collection<KeyValueStoreClient> clients;
    private Collection<Callable<List<Void>>> operations;
    private AtomicInteger counter;

    public KeyValueStoreBenchmark(final KeyValueStoreType storeType, final InetSocketAddress address) {
        this.storeType = storeType;
        this.address = address;
    }

    protected void setupBenchmark() {
        executorService = Executors.newFixedThreadPool(BenchmarkConstants.CLIENTS);
        clients = new ArrayList<>();
        operations = new ArrayList<>();
        counter = new AtomicInteger(0);
    }

    protected void tearDownBenchmark() throws Exception {
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    protected void executeOperations() {
        try {
            executorService.invokeAll(operations);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setupOperations(int gets, int adds, int deletes) {
        final List<Pair<KeyValueStoreOperation, String>> list = KeyValueStoreOperationGenerator.retrieve(gets, adds, deletes);
        IntStream.range(0, BenchmarkConstants.CLIENTS)
                .mapToObj(i -> newClient())
                .map(client -> (Callable<List<Void>>) () -> IntStream.range(0, BenchmarkConstants.OPERATIONS)
                        .mapToObj(i -> executeNext(list, client))
                        .collect(Collectors.toList()))
                .forEach(operations::add);
    }

    protected void cleanupOperations() {
        clients.forEach(this::closeSilently);
        clients.clear();
        operations.clear();
        counter.set(0);
    }

    private KeyValueStoreClient newClient() {
        try {
            KeyValueStoreClient client = storeType.getClient(address);
            clients.add(client);
            return client;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void closeSilently(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Void executeNext(List<Pair<KeyValueStoreOperation, String>> operations, KeyValueStoreClient client) {
        Pair<KeyValueStoreOperation, String> pair = operations.get(counter.getAndIncrement());
        switch (pair.first) {
            case GET:
                client.get(pair.second, String.class);
                break;
            case ADD:
                client.add(pair.second, BenchmarkConstants.DUMMY_VALUE);
                break;
            case DELETE:
                client.delete(pair.second);
                break;
            default:
        }
        return null;
    }
}
