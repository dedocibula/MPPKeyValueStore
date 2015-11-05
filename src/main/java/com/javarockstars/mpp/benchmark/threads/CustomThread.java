package com.javarockstars.mpp.benchmark.threads;

import com.javarockstars.mpp.api.LockFreeMap;
import com.javarockstars.mpp.utills.Constants;
import com.javarockstars.mpp.utills.Method;
import com.javarockstars.mpp.utills.PoolType;

/**
 * This class is responsible for simulating the multiple users case which leads
 * to congestion. These threads are designed specifically to benchmark different
 * methods in varying load. All the parameters are easily configurable and
 * design is extensible.
 * 
 * @author shivam-maharshi
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CustomThread implements Runnable {
	private static Method METHOD;
	private static LockFreeMap javaConcurrentHashMap;
	private static LockFreeMap michaelLockFreeHashMap;
	private PoolType poolType;

	public static void init(LockFreeMap javaMap, LockFreeMap michaelMap) {
		javaConcurrentHashMap = javaMap;
		michaelLockFreeHashMap = michaelMap;
	}

	public static void setMethod(Method evaluationMethod) {
		METHOD = evaluationMethod;
	}

	public CustomThread(PoolType poolType) {
		super();
		this.poolType = poolType;
	}

	@Override
	public void run() {
		if (METHOD == Method.PUT) {
			if (poolType == PoolType.JAVA) {
				evaluatePut_JavaMap();
			} else {
				evaluatePut_MichaelsMap();
			}
		} else if (METHOD == Method.GET) {
			if (poolType == PoolType.JAVA) {
				evaluateGet_JavaMap();
			} else {
				evaluateGet_MichaelsMap();
			}
		} else if (METHOD == Method.DELETE) {
			if (poolType == PoolType.JAVA) {
				evaluateRemove_JavaMap();
			} else {
				evaluateRemove_MichaelsMap();
			}
		}
	}

	private void evaluatePut_JavaMap() {
		for (int i = 0; i < Constants.MAX_KEY; i++) {
			/*
			 * Convert integer to string to generate hash code and thereby
			 * increasing the chances of collisions. Hence replicating real life
			 * scenarios.
			 */
			javaConcurrentHashMap.put(i + "", Constants.DUMMY_VALUE);
		}
	}

	private void evaluateGet_JavaMap() {
		for (int i = 0; i < Constants.MAX_KEY; i++) {
			javaConcurrentHashMap.get(i + "");
		}
	}

	private void evaluateRemove_JavaMap() {
		for (int i = 0; i < Constants.MAX_KEY; i++) {
			javaConcurrentHashMap.remove(i + "");
		}
	}

	private void evaluatePut_MichaelsMap() {
		for (int i = 0; i < Constants.MAX_KEY; i++) {
			/*
			 * Convert integer to string to generate hash code and thereby
			 * increasing the chances of collisions. Hence replicating real life
			 * scenarios.
			 */
			michaelLockFreeHashMap.put(i + "", Constants.DUMMY_VALUE);
		}
	}

	private void evaluateGet_MichaelsMap() {
		for (int i = 0; i < Constants.MAX_KEY; i++) {
			michaelLockFreeHashMap.get(i + "");
		}
	}

	private void evaluateRemove_MichaelsMap() {
		for (int i = 0; i < Constants.MAX_KEY; i++) {
			michaelLockFreeHashMap.remove(i + "");
		}
	}

}