package com.javarockstars.mpp.benchmarks;

import java.util.concurrent.ConcurrentHashMap;

import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.Bench;

import com.javarockstars.mpp.api.LockFreeMap;
import com.javarockstars.mpp.benchmark.threads.CustomThread;
import com.javarockstars.mpp.implementation.JavaConcurrentHashMap;
import com.javarockstars.mpp.implementation.MichaelLockFreeHashMap;
import com.javarockstars.mpp.utills.Constants;
import com.javarockstars.mpp.utills.Method;
import com.javarockstars.mpp.utills.PoolType;

/**
 * This class is responsible for generating the benchmarking results for
 * {@link ConcurrentHashMap} and {@link MichaelLockFreeHashMap} under varying
 * scenarios. We use this data to compare their performance against each other.
 * 
 * @author shivam-maharshi
 */
public class ConcurrentHashMapEvaluation {
	private static LockFreeMap<String, String> javaConcurrentHashMap;
	private static LockFreeMap<String, String> michaelLockFreeHashMap;
	private static CustomThread[] poolA = new CustomThread[Constants.CONCURRENCY];
	private static CustomThread[] poolB = new CustomThread[Constants.CONCURRENCY];

	@BeforeBenchClass
	public void init() {
		javaConcurrentHashMap = new JavaConcurrentHashMap<String, String>(new ConcurrentHashMap<>());
		michaelLockFreeHashMap = new MichaelLockFreeHashMap<String, String>();
		CustomThread.init(javaConcurrentHashMap, michaelLockFreeHashMap);
		// Initiating the thread pool.
		for (int i = 0; i < Constants.CONCURRENCY; i++) {
			CustomThread aThread = new CustomThread(PoolType.JAVA);
			poolA[i] = aThread;
			CustomThread bThread = new CustomThread(PoolType.MICHAEL);
			poolB[i] = bThread;
		}
	}

	@Bench
	public void evaluatePut_JavaMap() {
		CustomThread.setMethod(Method.PUT);
		for (int i = 0; i < Constants.CONCURRENCY; i++) {
			poolA[i].run();
		}
	}
	
	@Bench
	public void evaluateGet_JavaMap() {
		CustomThread.setMethod(Method.GET);
		for (int i = 0; i < Constants.CONCURRENCY; i++) {
			poolA[i].run();
		}
	}
	
	@Bench(runs=1)
	public void evaluateRemove_JavaMap() {
		CustomThread.setMethod(Method.DELETE);
		for (int i = 0; i < Constants.CONCURRENCY; i++) {
			poolA[i].run();
		}
	}
	
	@Bench
	public void evaluatePut_MichaelMap() {
		CustomThread.setMethod(Method.PUT);
		for (int i = 0; i < Constants.CONCURRENCY; i++) {
			poolB[i].run();
		}
	}
	
	@Bench
	public void evaluateGet_MichaelMap() {
		CustomThread.setMethod(Method.GET);
		for (int i = 0; i < Constants.CONCURRENCY; i++) {
			poolB[i].run();
		}
	}
	
	@Bench(runs=1)
	public void evaluateRemove_MichaelMap() {
		CustomThread.setMethod(Method.DELETE);
		for (int i = 0; i < Constants.CONCURRENCY; i++) {
			poolB[i].run();
		}
	}

	@AfterBenchClass
	public void destroy() {
		javaConcurrentHashMap = null;
		michaelLockFreeHashMap = null;
	}

}
