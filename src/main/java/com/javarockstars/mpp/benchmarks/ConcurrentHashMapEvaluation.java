package com.javarockstars.mpp.benchmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.Bench;
import org.perfidix.annotation.BenchClass;

import com.javarockstars.mpp.api.LockFreeMap;
import com.javarockstars.mpp.benchmark.threads.CustomTask;
import com.javarockstars.mpp.implementation.JavaConcurrentHashMap;
import com.javarockstars.mpp.implementation.MichaelLockFreeHashMap;
import com.javarockstars.mpp.utills.Constants;
import com.javarockstars.mpp.utills.Method;

/**
 * This class is responsible for generating the benchmarking results for
 * {@link ConcurrentHashMap} and {@link MichaelLockFreeHashMap} under varying
 * scenarios. We use this data to compare their performance against each other.
 * 
 * @author shivam-maharshi
 */
@BenchClass(runs = 1)
public class ConcurrentHashMapEvaluation {
	private LockFreeMap<String, String> javaConcurrentHashMap;
	private LockFreeMap<String, String> michaelLockFreeHashMap;
	private LockFreeMap<String, String> javaSkipListHashMap;
	private LockFreeMap<String, String> highlyScalableHashMap;
	private List<CustomTask> jmrList;
	private List<CustomTask> mmrList;
	private List<CustomTask> jslList;
	private List<CustomTask> hsmList;
	private ExecutorService executorService = Executors.newFixedThreadPool(Constants.CONCURRENCY);

	@BeforeBenchClass
	public void init() {
		
		javaConcurrentHashMap = new JavaConcurrentHashMap<String, String>(new ConcurrentHashMap<>(10000));
		javaSkipListHashMap = new JavaConcurrentHashMap<String, String>(new ConcurrentSkipListMap<>());
		highlyScalableHashMap = new JavaConcurrentHashMap<String, String>(new NonBlockingHashMap<>(10000));
		michaelLockFreeHashMap = new MichaelLockFreeHashMap<String, String>();
		jmrList = new ArrayList<>();
		mmrList = new ArrayList<>();
		jslList = new ArrayList<>();
		hsmList = new ArrayList<>();
		for (int i = 0; i < Constants.CONCURRENCY; i++) {
			jmrList.add(new CustomTask(javaConcurrentHashMap));
			mmrList.add(new CustomTask(michaelLockFreeHashMap));
			jslList.add(new CustomTask(javaSkipListHashMap));
			hsmList.add(new CustomTask(highlyScalableHashMap));
		}

	}

	@Bench(beforeEachRun="evaluateRemove_JavaMap")
	public void evaluatePut_JavaMap() {
		execute(jmrList, Method.PUT);
	}

	@Bench(beforeEachRun="evaluatePut_JavaMap")
	public void evaluateGet_JavaMap() {
		execute(jmrList, Method.GET);
	}

	@Bench(beforeEachRun="evaluatePut_JavaMap")
	public void evaluateRemove_JavaMap() {
		execute(jmrList, Method.DELETE);
	}
	
	@Bench(beforeEachRun="evaluateRemove_HighSalableMap")
	public void evaluatePut_HighSalableMap() {
		execute(hsmList, Method.PUT);
	}

	@Bench(beforeEachRun="evaluatePut_HighSalableMap")
	public void evaluateGet_HighSalableMap() {
		execute(hsmList, Method.GET);
	}

	@Bench(beforeEachRun="evaluatePut_HighSalableMap")
	public void evaluateRemove_HighSalableMap() {
		execute(hsmList, Method.DELETE);
	}
	
	@Bench(beforeEachRun="evaluateRemove_JavaSkipListMap")
	public void evaluatePut_JavaSkipListMap() {
		execute(jslList, Method.PUT);
	}

	@Bench(beforeEachRun="evaluatePut_JavaSkipListMap")
	public void evaluateGet_JavaSkipListMap() {
		execute(jslList, Method.GET);
	}

	@Bench(beforeEachRun="evaluatePut_JavaSkipListMap")
	public void evaluateRemove_JavaSkipListMap() {
		execute(jslList, Method.DELETE);
	}

	@Bench(beforeEachRun="evaluateRemove_MichaelMap")
	public void evaluatePut_MichaelMap() {
		execute(mmrList, Method.PUT);
	}

	@Bench(beforeEachRun="evaluatePut_MichaelMap")
	public void evaluateGet_MichaelMap() {
		execute(mmrList, Method.GET);
	}

	@Bench(beforeEachRun="evaluatePut_MichaelMap")
	public void evaluateRemove_MichaelMap() {
		execute(mmrList, Method.DELETE);
	}

	@SuppressWarnings("unused")
	private void execute(List<CustomTask> runnables, Method method) {
		for (CustomTask runnable : runnables) {
			runnable.setMethod(method);
		}
		try {
		List<Future<Void>> futures = executorService.invokeAll(runnables);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@AfterBenchClass
	public void destroy() {
		executorService.shutdown();
		javaConcurrentHashMap = null;
		michaelLockFreeHashMap = null;
	}

}
