package com.javarockstars.mpp.keyvaluestore.example;

import com.javarockstars.mpp.keyvaluestore.client.MPPClient;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public class ClientExample {
    public static void main(String[] args) throws Exception {
        MPPClient client = new MPPClient(new InetSocketAddress("localhost", 9999));
        System.out.println(client.add("Dude", "Blabla"));
        System.out.println(client.get("Dude", String.class));
        System.out.println(client.delete("Dude"));
        client.close();
    }
}
