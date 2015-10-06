package com.javarockstars.mpp.structures;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Author: dedocibula
 * Created on: 6.10.2015.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
        LockFreeKVListTest.class,
        MichaelLockFreeHashMapTest.class
})
public class UnitTestSuite {
}
