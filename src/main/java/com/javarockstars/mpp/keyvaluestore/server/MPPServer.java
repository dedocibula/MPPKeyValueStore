package com.javarockstars.mpp.keyvaluestore.server;

import com.javarockstars.mpp.keyvaluestore.common.MPPConnection;
import com.javarockstars.mpp.keyvaluestore.common.MPPConnectionPool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public final class MPPServer {
    private InetSocketAddress address;
    private AsynchronousChannelGroup group;
    private AsynchronousServerSocketChannel serverChannel;

    public MPPServer(final InetSocketAddress address, final ExecutorService service) throws Exception {
        Objects.requireNonNull(address);
        this.address = address;
        this.group = service == null ? null : AsynchronousChannelGroup.withCachedThreadPool(service, 5);
    }

    public synchronized void start() throws Exception {
        if (serverChannel == null) {
            serverChannel = AsynchronousServerSocketChannel.open(group).bind(address);
            serverChannel.accept(null, new MPPConnectionHandler());
        }
    }

    public synchronized void stop() throws Exception {
        if (group != null) {
            group.shutdown();
            group.awaitTermination(10, TimeUnit.SECONDS);
        } else if (serverChannel != null && serverChannel.isOpen()) {
            serverChannel.close();
        }
        serverChannel = null;
    }

    private static final class MPPConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
        @Override
        public void completed(AsynchronousSocketChannel client, Void object) {
            try {
                SocketAddress clientAddr = client.getRemoteAddress();
                System.out.format("Accepted a connection from %s%n", clientAddr);
                MPPConnection connection = MPPConnectionPool.getConnection(clientAddr);
                client.read(connection.getReadBuffer(), connection, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable e, Void object) {
            System.out.println("Failed to accept a connection.");
            e.printStackTrace();
        }
    }
}
