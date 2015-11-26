package com.javarockstars.mpp.keyvaluestore;

/**
 * Author: dedocibula
 * Created on: 23.11.2015.
 */
public interface Constants {
    // Timeout in ms for Spy Memcached client for add operation.
    int TIMEOUT = 3600;

    // Size of a connection read-write buffer (bytes) in MPP client.
    int BUFFER_SIZE = 2 * 1024;
}
