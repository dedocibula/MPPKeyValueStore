package com.javarockstars.mpp.benchmarks;

import java.io.File;

import org.perfidix.Benchmark;
import org.perfidix.ouput.CSVOutput;
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
		benchmark.add(ConcurrentHashMapEvaluation.class);
		BenchmarkResult result = benchmark.run();
		// To store results in CSV for analysis.
		new CSVOutput(new File("C:\\Users\\Sam\\Workspace\\MPPKeyValueStore\\output")).visitBenchmark(result);
		new TabularSummaryOutput().visitBenchmark(result);
	}

}
