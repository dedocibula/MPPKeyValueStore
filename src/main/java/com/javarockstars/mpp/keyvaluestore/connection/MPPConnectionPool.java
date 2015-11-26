package com.javarockstars.mpp.keyvaluestore.connection;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public final class MPPConnectionPool {
    private static ConcurrentLinkedQueue<MPPConnection> pool = new ConcurrentLinkedQueue<>();

    public static MPPConnection getConnection(final AsynchronousSocketChannel forClient) {
        Objects.requireNonNull(forClient);
        MPPConnection poll = pool.poll();
        if (poll != null)
            poll.setSocketAddress(forClient);
        else
            poll = new MPPConnection(forClient);
        return poll;
    }

    public static void retrieveConnection(final MPPConnection connection) {
        Objects.requireNonNull(connection);
        connection.setSocketAddress(null);
        connection.setRead();
        connection.getReadWriteBuffer().clear();
        pool.offer(connection);
    }
}
