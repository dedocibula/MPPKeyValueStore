package com.javarockstars.mpp.keyvaluestore;

import org.junit.Assert;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Author: dedocibula
 * Created on: 3.12.2015.
 */
public abstract class AbstractKVStoreTest extends Assert {
    private static int port = 1234;

    protected static synchronized InetSocketAddress uniqueAddress() {
        String hostName = "localhost";
        while (true) {
            try (Socket ignored = new Socket(hostName, port)) {
                port++;
            } catch (IOException e) {
                break;
            }
        }
        return new InetSocketAddress(hostName, port++);
    }
}
