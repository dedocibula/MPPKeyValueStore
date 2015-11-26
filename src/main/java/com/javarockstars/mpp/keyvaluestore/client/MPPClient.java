package com.javarockstars.mpp.keyvaluestore.client;

import com.javarockstars.mpp.keyvaluestore.api.KeyValueStoreClient;
import com.javarockstars.mpp.keyvaluestore.command.MPPCommand;
import com.javarockstars.mpp.keyvaluestore.command.MPPCommandFactory;
import com.javarockstars.mpp.keyvaluestore.connection.MPPConnection;
import com.javarockstars.mpp.keyvaluestore.connection.MPPConnectionPool;
import com.javarockstars.mpp.keyvaluestore.util.SerializationHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public final class MPPClient implements KeyValueStoreClient {
	private AsynchronousSocketChannel clientChannel;

	public MPPClient(final InetSocketAddress address) throws Exception {
		Objects.requireNonNull(address);
		this.clientChannel = AsynchronousSocketChannel.open();
		clientChannel.connect(address).get();
	}

	@Override
	public <V> V get(final String key, final Class<V> valueType) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(valueType);
		V result = null;
		try {
			result = executeAsync(MPPCommandFactory.newGetCommand(key), valueType).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public <V> boolean add(String key, V value) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		boolean result = false;
		try {
			result = executeAsync(MPPCommandFactory.newPutCommand(key, value), Boolean.TYPE).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean delete(String key) {
		Objects.requireNonNull(key);
		boolean result = false;
		try {
			result = executeAsync(MPPCommandFactory.newRemoveCommand(key), Boolean.TYPE).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}

	private <T> Future<T> executeAsync(MPPCommand command, Class<T> resultType) {
		MPPConnection connection = MPPConnectionPool.getConnection(clientChannel);
		connection.setWrite();
		connection.getReadWriteBuffer().put(SerializationHelper.serialize(command)).flip();
		MPPResponseHandler<T> handler = new MPPResponseHandler<>(resultType);
		clientChannel.write(connection.getReadWriteBuffer(), connection, handler);
		return handler.getResponse();
	}

	@Override
	public void close() throws IOException {
		if (clientChannel.isOpen())
			clientChannel.close();
	}

	private static final class MPPResponseHandler<T> implements CompletionHandler<Integer, MPPConnection> {
		private final Class<T> resultType;
		private final MPPResponseFuture<T> future;

		private MPPResponseHandler(Class<T> resultType) {
			this.resultType = resultType;
			this.future = new MPPResponseFuture<>();
		}

		@Override
		public void completed(Integer result, MPPConnection connection) {
			AsynchronousSocketChannel client = connection.getClient();
			if (connection.isRead()) {
				handleResponse(connection.getReadWriteBuffer());
				MPPConnectionPool.retrieveConnection(connection);
			} else {
				connection.setRead();
				connection.getReadWriteBuffer().clear();
				client.read(connection.getReadWriteBuffer(), connection, this);
			}
		}

		@Override
		public void failed(Throwable exc, MPPConnection connection) {
			System.out.println("Operation failed.");
			future.setException(exc);
			MPPConnectionPool.retrieveConnection(connection);
			exc.printStackTrace();
		}

		public Future<T> getResponse() {
			return future;
		}

		private void handleResponse(ByteBuffer buffer) {
			buffer.flip();
			byte[] bytes = new byte[buffer.limit()];
			buffer.get(bytes);
			future.setResult(SerializationHelper.deserialize(bytes, resultType));
		}
	}

	private static final class MPPResponseFuture<T> implements Future<T> {
		private final CountDownLatch latch;
		private boolean done = false;
		private T result;
		private Throwable exception;

		private MPPResponseFuture() {
			this.latch = new CountDownLatch(1);
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isCancelled() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isDone() {
			return done;
		}

		@Override
		public T get() throws InterruptedException, ExecutionException {
			latch.await();
			if (exception != null)
				throw new ExecutionException(exception);
			return result;
		}

		@Override
		public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			latch.await(timeout, unit);
			if (exception != null)
				throw new ExecutionException(exception);
			return result;
		}

		private void setResult(T result) {
			synchronized (latch) {
				if (!done && latch.getCount() > 0) {
					this.result = result;
					done = true;
					latch.countDown();
				}
			}
		}

		private void setException(Throwable exception) {
			synchronized (latch) {
				if (!done && latch.getCount() > 0) {
					this.exception = exception;
					done = true;
					latch.countDown();
				}
			}
		}
	}
}
