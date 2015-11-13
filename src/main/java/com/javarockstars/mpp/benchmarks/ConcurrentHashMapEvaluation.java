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
@BenchClass(runs = 5)
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

	@Bench(beforeEachRun = "clearMapJavaMap")
	public void evaluatePut_JavaMap() {
		execute(jmrList, Method.PUT);
	}

	@Bench(beforeEachRun = "populateMapJavaMap")
	public void evaluateGet_JavaMap() {
		execute(jmrList, Method.GET);
	}

	@Bench(beforeEachRun = "populateMapJavaMap")
	public void evaluateRemove_JavaMap() {
		execute(jmrList, Method.DELETE);
	}

	@Bench(beforeEachRun = "clearMapHighScalableMap")
	public void evaluatePut_HighScalableMap() {
		execute(hsmList, Method.PUT);
	}

	@Bench(beforeEachRun = "populateMapHighScalableMap")
	public void evaluateGet_HighScalableMap() {
		execute(hsmList, Method.GET);
	}

	@Bench(beforeEachRun = "populateMapHighScalableMap")
	public void evaluateRemove_HighScalableMap() {
		execute(hsmList, Method.DELETE);
	}

	@Bench(beforeEachRun = "clearMapJavaSkipListMap")
	public void evaluatePut_JavaSkipListMap() {
		execute(jslList, Method.PUT);
	}

	@Bench(beforeEachRun = "populateMapJavaSkipListMap")
	public void evaluateGet_JavaSkipListMap() {
		execute(jslList, Method.GET);
	}

	@Bench(beforeEachRun = "populateMapJavaSkipListMap")
	public void evaluateRemove_JavaSkipListMap() {
		execute(jslList, Method.DELETE);
	}

	@Bench(beforeEachRun = "clearMapMichaelMap")
	public void evaluatePut_MichaelMap() {
		execute(mmrList, Method.PUT);
	}

	@Bench(beforeEachRun = "populateMapMichaelMap")
	public void evaluateGet_MichaelMap() {
		execute(mmrList, Method.GET);
	}

	@Bench(beforeEachRun = "populateMapMichaelMap")
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

	public void clearMapJavaMap() {
		clearMap(javaConcurrentHashMap);
	}

	public void populateMapJavaMap() {
		populateMap(javaConcurrentHashMap);
	}

	public void clearMapHighScalableMap() {
		clearMap(highlyScalableHashMap);
	}

	public void populateMapHighScalableMap() {
		populateMap(highlyScalableHashMap);
	}

	public void clearMapJavaSkipListMap() {
		clearMap(javaSkipListHashMap);
	}

	public void populateMapJavaSkipListMap() {
		populateMap(javaSkipListHashMap);
	}

	public void clearMapMichaelMap() {
		clearMap(michaelLockFreeHashMap);
	}

	public void populateMapMichaelMap() {
		populateMap(michaelLockFreeHashMap);
	}

	private void clearMap(LockFreeMap<String, String> hashMap) {
		for (int i = 0; i < Constants.MAX_KEY; i++) {
			hashMap.remove(i + "");
		}
		assert (hashMap.size() == 0);
	}

	private void populateMap(LockFreeMap<String, String> hashMap) {
		for (int i = 0; i < Constants.MAX_KEY; i++) {
			hashMap.put(i + "", Constants.DUMMY_VALUE);
		}
		assert (hashMap.size() == Constants.MAX_KEY);
	}

}
