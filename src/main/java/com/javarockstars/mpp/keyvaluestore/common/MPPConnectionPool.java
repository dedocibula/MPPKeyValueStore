package com.javarockstars.mpp.keyvaluestore.common;

import java.net.SocketAddress;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public final class MPPConnectionPool {
    private static ConcurrentLinkedQueue<MPPConnection> pool = new ConcurrentLinkedQueue<>();

    public static MPPConnection getConnection(final SocketAddress forAddress) {
        Objects.requireNonNull(forAddress);
        MPPConnection poll = pool.poll();
        if (poll != null)
            poll.setSocketAddress(forAddress);
        else
            poll = new MPPConnection(forAddress);
        return poll;
    }

    public static void retrieveConnection(final MPPConnection connection) {
        Objects.requireNonNull(connection);
        connection.setSocketAddress(null);
        pool.offer(connection);
    }
}
