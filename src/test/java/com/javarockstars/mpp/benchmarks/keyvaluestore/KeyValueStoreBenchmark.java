package com.javarockstars.mpp.benchmarks.keyvaluestore;

import com.javarockstars.mpp.benchmarks.BenchmarkConstants;
import com.javarockstars.mpp.keyvaluestore.api.KeyValueStoreClient;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;

import static com.javarockstars.mpp.benchmarks.BenchmarkConstants.*;

/**
 * Responsible for creating a java request using the client to the various
 * {@link KeyValueStoreBenchmark} which can then be invoked by configurable numbers
 * of threads in different workloads via Apache JMeter.
 *
 * @author shivam.maharshi
 */
public abstract class KeyValueStoreBenchmark extends AbstractJavaSamplerClient implements Serializable {
    private static final long serialVersionUID = 1L;

    // Set up default arguments for the JMeter GUI.
    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument(PORT, "9988");
        defaultParameters.addArgument(HOST, "127.0.0.1");
        defaultParameters.addArgument(STORE_TYPE, "mpp");
        defaultParameters.addArgument(OPERATION, "get");
        return defaultParameters;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        String host = context.getParameter(HOST);
        int port = Integer.valueOf(context.getParameter(PORT));
        InetSocketAddress add = new InetSocketAddress(host, port);
        KeyValueStoreType storeType = KeyValueStoreType.valueOf(context.getParameter(STORE_TYPE).toUpperCase());
        try (KeyValueStoreClient client = storeType.getClient(add)) {
            return executeOperation(client, context.getParameter(OPERATION));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    /**
     * Default is Get.
     *
     * @param client
     * @param operation
     * @return {@link SampleResult}
     * @throws IOException
     */
    private SampleResult executeOperation(KeyValueStoreClient client, String operation) throws IOException {
        String key = "" + (int) (Math.random() * BenchmarkConstants.MAX_KEY);
        if (operation.equalsIgnoreCase(PUT)) {
            return executePutOperation(client, key, DUMMY_DATA);
        } else if (operation.equalsIgnoreCase(DELETE)) {
            return executeDeleteOperation(client, key);
        }
        // Default is GET.
        return executeGetOperation(client, key);
    }

    /**
     * Benchmarks GET operation.
     *
     * @param client
     * @param key
     * @return
     * @throws IOException
     */
    private SampleResult executeGetOperation(KeyValueStoreClient client, String key) throws IOException {
        SampleResult result = new SampleResult();
        result.sampleStart();
        String response = client.get(key, String.class);
        result.setResponseData(response != null ? response : "Key absent !", null);
        result.sampleEnd();
        result.setSuccessful(true);
        result.setResponseMessage(SUCCESS_MESSAGE);
        result.setResponseCodeOK();
        return result;
    }

    /**
     * Benchmarks PUT operation.
     *
     * @param client
     * @param key
     * @param data
     * @return
     */
    private SampleResult executePutOperation(KeyValueStoreClient client, String key, String data) {
        SampleResult result = new SampleResult();
        result.sampleStart();
        client.add(key, data);
        result.sampleEnd();
        result.setSuccessful(true);
        result.setResponseMessage(SUCCESS_MESSAGE);
        result.setResponseCodeOK();
        return result;
    }

    /**
     * Benchmarks DELETE operation.
     *
     * @param client
     * @param key
     * @return
     */
    private SampleResult executeDeleteOperation(KeyValueStoreClient client, String key) {
        SampleResult result = new SampleResult();
        result.sampleStart();
        client.delete(key);
        result.sampleEnd();
        result.setSuccessful(true);
        result.setResponseMessage(SUCCESS_MESSAGE);
        result.setResponseCodeOK();
        return result;
    }

    /**
     * Handle exception.
     *
     * @param e
     * @return {@link SampleResult}
     */
    private SampleResult handleException(Exception e) {
        SampleResult result = new SampleResult();
        result.setSuccessful(false);
        result.setResponseMessage(ExceptionUtils.getStackTrace(e));
        java.io.StringWriter stringWriter = new java.io.StringWriter();
        e.printStackTrace(new java.io.PrintWriter(stringWriter));
        result.setResponseData(stringWriter.toString(), null);
        result.setDataType(org.apache.jmeter.samplers.SampleResult.TEXT);
        result.setResponseCode("500");
        return result;
    }
}
