package com.javarockstars.mpp.keyvaluestore.common;

import com.javarockstars.mpp.keyvaluestore.Constants;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public final class MPPConnection {
    private SocketAddress clientAddress;
    private final ByteBuffer writeBuffer;
    private final ByteBuffer readBuffer;

    MPPConnection(final SocketAddress clientAddress) {
        Objects.requireNonNull(clientAddress);
        this.clientAddress = clientAddress;
        this.writeBuffer = ByteBuffer.allocateDirect(Constants.WRITE_BUFFER_SIZE);
        this.readBuffer = ByteBuffer.allocateDirect(Constants.READ_BUFFER_SIZE);
    }

    void setSocketAddress(SocketAddress newClientAddress) {
        Objects.requireNonNull(newClientAddress);
        this.clientAddress = newClientAddress;
    }

    public SocketAddress getClientAddress() {
        return clientAddress;
    }

    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }
}
