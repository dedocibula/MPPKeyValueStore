package com.javarockstars.mpp.keyvaluestore.example;

import com.javarockstars.mpp.keyvaluestore.command.MPPCommandProcessor;
import com.javarockstars.mpp.keyvaluestore.server.MPPServer;

import java.net.InetSocketAddress;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public class ServerExample {
    public static void main(String[] args) throws Exception {
        MPPCommandProcessor echoProcessor = command -> {
            System.out.format("Received command %s\n", command.getType());
            switch (command.getType()) {
                case GET:
                    return command.getKey();
                case PUT:
                    return true;
                case REMOVE:
                    return true;
                default:
                    return null;
            }
        };
        MPPServer server = new MPPServer(new InetSocketAddress("localhost", 9999), () -> echoProcessor, null);
        server.start();
        System.in.read();
        server.stop();
    }
}
