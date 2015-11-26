package com.javarockstars.mpp.keyvaluestore;

/**
 * Author: dedocibula
 * Created on: 23.11.2015.
 */
public interface Constants {
    // Timeout in seconds for Spy Memcached client for add operation.
    int TIMEOUT = 24 * 60 * 60;

    // Size of a connection read-write buffer (bytes) in MPP client.
    int BUFFER_SIZE = 2 * 1024;

    // Status of successful Redis operation.
    String STATUS_OK = "OK";
}
