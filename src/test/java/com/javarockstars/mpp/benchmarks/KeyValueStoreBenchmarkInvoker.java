package com.javarockstars.mpp.benchmarks;

import com.javarockstars.mpp.benchmarks.extensions.TabularCSVOutput;
import com.javarockstars.mpp.benchmarks.extensions.TabularCSVOutput.Grouping;
import com.javarockstars.mpp.benchmarks.keyvaluestore.KeyValueStoreOperationGenerator;
import com.javarockstars.mpp.benchmarks.keyvaluestore.KeyValueStoreOperationGenerator.Pair;
import com.javarockstars.mpp.benchmarks.keyvaluestore.MPPStoreBenchmark;
import com.javarockstars.mpp.benchmarks.keyvaluestore.MemcachedStoreBenchmark;
import com.javarockstars.mpp.benchmarks.keyvaluestore.RedisStoreBenchmark;
import org.perfidix.Benchmark;
import org.perfidix.result.BenchmarkResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

/**
 * Author: dedocibula
 * Created on: 4.12.2015.
 */
public class KeyValueStoreBenchmarkInvoker {
    public static void main(String[] args) throws IOException {
        generateRandomOperations();

        Benchmark benchmark = new Benchmark(new BenchmarkConfig(1));
        benchmark.add(MPPStoreBenchmark.class);
        benchmark.add(RedisStoreBenchmark.class);
        benchmark.add(MemcachedStoreBenchmark.class);
        BenchmarkResult result = benchmark.run();
        TabularCSVOutput.toStream(printStream("stores-benchmark.csv")).groupBy(Grouping.METHOD).visitBenchmark(result);
    }

    private static void generateRandomOperations() {
        KeyValueStoreOperationGenerator.generate(100, 0, 0);
        dump(KeyValueStoreOperationGenerator.retrieve(100, 0, 0));

        KeyValueStoreOperationGenerator.generate(50, 50, 0);
        dump(KeyValueStoreOperationGenerator.retrieve(50, 50, 0));

        KeyValueStoreOperationGenerator.generate(80, 10, 10);
        dump(KeyValueStoreOperationGenerator.retrieve(80, 10, 10));
    }

    private static void dump(final List<Pair<KeyValueStoreOperationGenerator.KeyValueStoreOperation, String>> pairs) {
        Objects.requireNonNull(pairs);
        pairs.forEach(p -> System.out.println(p.first + " " + p.second));
        System.out.println();
    }

    private static PrintStream printStream(final String fileName) throws IOException {
        Objects.requireNonNull(fileName);
        File currentDirectory = new File(System.getProperty("user.dir"));
        File newFile = new File(currentDirectory, fileName);
        if (newFile.exists())
            return (newFile.isDirectory() || !newFile.canWrite()) ? System.out : new PrintStream(new FileOutputStream(newFile, true));
        else
            return newFile.createNewFile() ? new PrintStream(newFile) : System.out;
    }
}
