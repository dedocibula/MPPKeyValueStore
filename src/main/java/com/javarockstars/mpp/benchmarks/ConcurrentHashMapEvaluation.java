package com.javarockstars.mpp.benchmarks;

import java.util.concurrent.ConcurrentHashMap;

import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.Bench;

import com.javarockstars.mpp.api.LockFreeMap;
import com.javarockstars.mpp.implementation.JavaConcurrentHashMap;
import com.javarockstars.mpp.implementation.MichaelLockFreeHashMap;
import com.javarockstars.mpp.utills.Constants;

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

	@BeforeBenchClass
	public void init() {
		javaConcurrentHashMap = new JavaConcurrentHashMap<String, String>(new ConcurrentHashMap<>());
		michaelLockFreeHashMap = new MichaelLockFreeHashMap<String, String>();
	}

	@Bench
	public void evaluatePut_JavaMap() {
		for (int i = 0; i < 100000; i++) {
			/*
			 * Convert integer to string to generate hash code and thereby
			 * increasing the chances of collisions. Hence replicating real life
			 * scenarios.
			 */
			javaConcurrentHashMap.put(i + "", Constants.DUMMY_VALUE);
		}
	}
	
	@Bench
	public void evaluatePut_MichaelsMap() {
		for (int i = 0; i < 100000; i++) {
			/*
			 * Convert integer to string to generate hash code and thereby
			 * increasing the chances of collisions. Hence replicating real life
			 * scenarios.
			 */
			michaelLockFreeHashMap.put(i + "", Constants.DUMMY_VALUE);
		}
	}

	@AfterBenchClass
	public void destroy() {
		javaConcurrentHashMap = null;
		michaelLockFreeHashMap = null;
	}

}
