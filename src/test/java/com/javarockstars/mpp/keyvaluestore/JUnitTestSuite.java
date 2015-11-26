package com.javarockstars.mpp.keyvaluestore;

import com.javarockstars.mpp.keyvaluestore.client.MPPClientTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * JUnit Test Suite (Gradle won't work).
 * <p>
 * Author: dedocibula
 * Created on: 26.11.2015.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        MPPClientTest.class
})
public class JUnitTestSuite {
}
