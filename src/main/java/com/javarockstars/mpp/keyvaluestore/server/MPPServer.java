package com.javarockstars.mpp.keyvaluestore.server;

import com.javarockstars.mpp.keyvaluestore.command.MPPCommand;
import com.javarockstars.mpp.keyvaluestore.command.MPPCommandProcessor;
import com.javarockstars.mpp.keyvaluestore.connection.MPPConnection;
import com.javarockstars.mpp.keyvaluestore.connection.MPPConnectionPool;
import com.javarockstars.mpp.keyvaluestore.util.SerializationHelper;

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
import java.util.function.Supplier;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public final class MPPServer {
    private final Supplier<MPPCommandProcessor> processorSupplier;
    private final InetSocketAddress address;
    private final AsynchronousChannelGroup group;
    private AsynchronousServerSocketChannel serverChannel;

    public MPPServer(final InetSocketAddress address,
                     final Supplier<MPPCommandProcessor> processorSupplier) throws Exception {
        this(address, processorSupplier, null);
    }

    public MPPServer(final InetSocketAddress address,
                     final Supplier<MPPCommandProcessor> processorSupplier,
                     final ExecutorService service) throws Exception {
        Objects.requireNonNull(address);
        Objects.requireNonNull(processorSupplier);
        this.address = address;
        this.processorSupplier = processorSupplier;
        this.group = service == null ? null : AsynchronousChannelGroup.withCachedThreadPool(service, 5);
    }

    public void start() throws Exception {
        synchronized (address) {
            if (serverChannel == null) {
                System.out.format("Starting server. Server is listening at %s%n\n", address);
                serverChannel = AsynchronousServerSocketChannel.open(group).bind(address);
                serverChannel.accept(serverChannel, new MPPConnectionHandler(processorSupplier));
            }
        }
    }

    public void stop() throws Exception {
        synchronized (address) {
            if (group != null) {
                System.out.println("Stopping server.");
                group.shutdown();
                group.awaitTermination(10, TimeUnit.SECONDS);
            } else if (serverChannel != null && serverChannel.isOpen()) {
                System.out.println("Stopping server.");
                serverChannel.close();
            }
            serverChannel = null;
        }
    }

    private static final class MPPConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
        private final Supplier<MPPCommandProcessor> processorSupplier;

        public MPPConnectionHandler(Supplier<MPPCommandProcessor> processorSupplier) {
            this.processorSupplier = processorSupplier;
        }

        @Override
        public void completed(AsynchronousSocketChannel client, AsynchronousServerSocketChannel server) {
            try {
                SocketAddress clientAddr = client.getRemoteAddress();
                System.out.format("Accepted a connection from %s%n", clientAddr);
                server.accept(server, this);
                MPPConnection connection = MPPConnectionPool.getConnection(client);
                client.read(connection.getReadWriteBuffer(), connection, new MPPRequestHandler(processorSupplier.get()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable e, AsynchronousServerSocketChannel server) {
            if (server.isOpen()) {
                System.out.println("Failed to accept a connection.");
                e.printStackTrace();
            }
        }
    }

    private static final class MPPRequestHandler implements CompletionHandler<Integer, MPPConnection> {
        private final MPPCommandProcessor processor;

        private MPPRequestHandler(MPPCommandProcessor processor) {
            this.processor = processor;
        }

        @Override
        public void completed(Integer result, MPPConnection connection) {
            AsynchronousSocketChannel client = connection.getClient();
            if (result == -1 || !client.isOpen()) {
                dispose(connection);
                return;
            }

            if (connection.isRead()) {
                handleRequest(connection.getReadWriteBuffer());
                connection.setWrite();
                client.write(connection.getReadWriteBuffer(), connection, this);
            } else {
                connection.setRead();
                connection.getReadWriteBuffer().clear();
                client.read(connection.getReadWriteBuffer(), connection, this);
            }
        }

        @Override
        public void failed(Throwable exc, MPPConnection connection) {
            System.out.println("Client disconnected.");
            MPPConnectionPool.retrieveConnection(connection);
            exc.printStackTrace();
        }

        private void handleRequest(ByteBuffer buffer) {
            buffer.flip();
            byte[] bytes = new byte[buffer.limit()];
            buffer.get(bytes);
            MPPCommand command = SerializationHelper.deserialize(bytes, MPPCommand.class);
            bytes = SerializationHelper.serialize(processor.processCommand(command));
            buffer.clear();
            buffer.put(bytes).flip();
        }

        private void dispose(MPPConnection connection) {
            AsynchronousSocketChannel client = connection.getClient();
            try {
                if (client.isOpen()) {
                    System.out.format("Stopped listening to the client %s%n", client.getRemoteAddress());
                    client.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            MPPConnectionPool.retrieveConnection(connection);
        }
    }
}
