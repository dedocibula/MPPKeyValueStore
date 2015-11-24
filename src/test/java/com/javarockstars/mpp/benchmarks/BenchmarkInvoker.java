package com.javarockstars.mpp.benchmarks;

import com.javarockstars.mpp.benchmarks.datastructures.ConcurrentHashMapBenchmark;
import com.javarockstars.mpp.benchmarks.datastructures.ConcurrentSkipListHashMapBenchmark;
import com.javarockstars.mpp.benchmarks.datastructures.MichaelLockFreeHashMapBenchmark;
import com.javarockstars.mpp.benchmarks.datastructures.NonBlockingHashMapBenchmark;
import org.perfidix.Benchmark;
import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;

import java.io.*;
import java.util.Objects;

/**
 * This class is responsible for invoking benchmarking evaluation. This is
 * required for the environment that doesn't support the Perfidix plugin
 * directly.
 *
 * @author shivam.maharshi
 */
public class BenchmarkInvoker {
    public static void main(String[] args) throws IOException {
        Benchmark benchmark = new Benchmark();
        benchmark.add(ConcurrentHashMapBenchmark.class);
        benchmark.add(MichaelLockFreeHashMapBenchmark.class);
        benchmark.add(ConcurrentSkipListHashMapBenchmark.class);
        benchmark.add(NonBlockingHashMapBenchmark.class);
        BenchmarkResult result = benchmark.run();
        new TabularSummaryOutput(toFile("benchmark.txt")).visitBenchmark(result);
    }

    private static PrintStream toFile(String fileName) throws IOException {
        Objects.requireNonNull(fileName);
        File currentDirectory = new File(System.getProperty("user.dir"));
        File newFile = new File(currentDirectory, fileName);
        if (newFile.exists())
            return (newFile.isDirectory() || !newFile.canWrite()) ? System.out : new PrintStream(new FileOutputStream(newFile, true));
        else
            return newFile.createNewFile() ? new PrintStream(newFile) : System.out;
    }
}
