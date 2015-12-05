package com.javarockstars.mpp.keyvaluestore.example;

import com.javarockstars.mpp.datastructures.api.LockFreeMap;
import com.javarockstars.mpp.datastructures.implementation.MichaelLockFreeHashMap;
import com.javarockstars.mpp.keyvaluestore.implementation.MPPLFMapCommandProcessor;
import com.javarockstars.mpp.keyvaluestore.server.MPPServer;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public final class ServerExample {
    public static void main(String[] args) throws Exception {
        LockFreeMap<String, Serializable> lockFreeMap = new MichaelLockFreeHashMap<>(100);
        MPPServer server = new MPPServer(new InetSocketAddress("localhost", 9999), () -> new MPPLFMapCommandProcessor(lockFreeMap));
        server.start();
        System.in.read();
        server.stop();
    }
}
