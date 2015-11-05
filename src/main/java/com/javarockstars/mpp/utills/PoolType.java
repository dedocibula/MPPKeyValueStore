package com.javarockstars.mpp.utills;

import com.javarockstars.mpp.implementation.JavaConcurrentHashMap;
import com.javarockstars.mpp.implementation.MichaelLockFreeHashMap;

/**
 * Pool type to distinguish between the thread evaluating the
 * {@link JavaConcurrentHashMap} or {@link MichaelLockFreeHashMap}
 * 
 * @author shivam-maharshi
 */
public enum PoolType {

	JAVA, MICHAEL;

}
