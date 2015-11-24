package com.javarockstars.mpp.datastructures;

import com.javarockstars.mpp.datastructures.common.LockFreeKVListTest;
import com.javarockstars.mpp.datastructures.implementation.MichaelLockFreeHashMapTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * JUnit Test Suite (Gradle won't work)
 * <p>
 * Author: dedocibula
 * Created on: 6.10.2015.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
        LockFreeKVListTest.class,
        MichaelLockFreeHashMapTest.class
})
public class JUnitTestSuite {
}
