package com.javarockstars.mpp.keyvaluestore;

/**
 * Author: dedocibula
 * Created on: 23.11.2015.
 */
public interface Constants {
    // Timeout in ms for Spy Memcached client for add operation.
    int TIMEOUT = 3600;
    // The thread pool size for our custom key value server server.
    int THREAD_POOL_SIZE = 50;

    int KB = 1024;

    int READ_BUFFER_SIZE = 4 * KB;

    int WRITE_BUFFER_SIZE = 4 * KB;
}
