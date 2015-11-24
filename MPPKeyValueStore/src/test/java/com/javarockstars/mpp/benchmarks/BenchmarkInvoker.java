package com.javarockstars.mpp.benchmarks;

import com.javarockstars.mpp.benchmarks.datastructures.ConcurrentHashMapBenchmark;
import com.javarockstars.mpp.benchmarks.datastructures.ConcurrentSkipListHashMapBenchmark;
import com.javarockstars.mpp.benchmarks.datastructures.MichaelLockFreeHashMapBenchmark;
import com.javarockstars.mpp.benchmarks.datastructures.NonBlockingHashMapBenchmark;
import org.perfidix.Benchmark;
import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;

/**
 * This class is responsible for invoking benchmarking evaluation. This is
 * required for the environment that doesn't support the Perfidix plugin
 * directly.
 * 
 * @author shivam.maharshi
 *
 */
public class BenchmarkInvoker {
	public static void main(String[] args) {
		Benchmark benchmark = new Benchmark();
		benchmark.add(ConcurrentHashMapBenchmark.class);
		benchmark.add(MichaelLockFreeHashMapBenchmark.class);
		benchmark.add(ConcurrentSkipListHashMapBenchmark.class);
		benchmark.add(NonBlockingHashMapBenchmark.class);
		BenchmarkResult result = benchmark.run();
		new TabularSummaryOutput().visitBenchmark(result);
	}

}
