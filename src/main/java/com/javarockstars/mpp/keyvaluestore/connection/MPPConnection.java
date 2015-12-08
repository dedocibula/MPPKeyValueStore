package com.javarockstars.mpp.keyvaluestore.connection;

import com.javarockstars.mpp.keyvaluestore.Constants;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Objects;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public final class MPPConnection {
    private AsynchronousSocketChannel client;
    private boolean isRead;
    private final ByteBuffer readWriteBuffer;

    MPPConnection(final AsynchronousSocketChannel client) {
        Objects.requireNonNull(client);
        this.client = client;
        this.readWriteBuffer = ByteBuffer.allocateDirect(Constants.BUFFER_SIZE);
        this.isRead = true;
    }

    void setSocketAddress(AsynchronousSocketChannel newClient) {
        this.client = newClient;
    }

    public AsynchronousSocketChannel getClient() {
        return client;
    }

    public ByteBuffer getReadWriteBuffer() {
        return readWriteBuffer;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead() {
        isRead = true;
    }

    public void setWrite() {
        isRead = false;
    }
}
