package com.javarockstars.mpp.benchmark.threads;

import java.util.concurrent.Callable;

import com.javarockstars.mpp.api.LockFreeMap;
import com.javarockstars.mpp.utills.Constants;
import com.javarockstars.mpp.utills.Method;

/**
 * This class is responsible for simulating the multiple users case which leads
 * to congestion. These threads are designed specifically to benchmark different
 * methods in varying load. All the parameters are easily configurable and
 * design is extensible.
 * 
 * @author shivam-maharshi
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CustomTask implements Callable<Void> {
	private Method method;
	private final LockFreeMap concurrentMap; 

	public void setMethod(Method evaluationMethod) {
		method = evaluationMethod;
	}

	public CustomTask(LockFreeMap concurrentMap) {
		this.concurrentMap = concurrentMap;
	}

	private void evaluatePut() {
		for (int i = 0; i < Constants.MAX_KEY; i++) {
			/*
			 * Convert integer to string to generate hash code and thereby
			 * increasing the chances of collisions. Hence replicating real life
			 * scenarios.
			 */
			concurrentMap.put(i + "", Constants.DUMMY_VALUE);
		}
	}

	private void evaluateGet() {
		for (int i = 0; i < Constants.MAX_KEY; i++) {
			concurrentMap.get(i + "");
		}
	}

	private void evaluateRemove() {
		for (int i = 0; i < Constants.MAX_KEY; i++) {
			concurrentMap.remove(i + "");
		}
	}

	@Override
	public Void call() throws Exception {
		switch (method) {
		case PUT:
			evaluatePut();
			break;
		case GET:
			evaluateGet();
			break;
		case DELETE:
			evaluateRemove();
			break;
		default:
			throw new RuntimeException("Invalid method value.");
		}
		return null;
	}

}