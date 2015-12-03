package com.javarockstars.mpp.keyvaluestore.client;

import com.javarockstars.mpp.keyvaluestore.AbstractKVStoreTest;
import com.javarockstars.mpp.keyvaluestore.command.MPPCommandProcessor;
import com.javarockstars.mpp.keyvaluestore.server.MPPServer;
import org.junit.*;

import java.net.InetSocketAddress;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Author: dedocibula
 * Created on: 26.11.2015.
 */
public class MPPClientTest extends AbstractKVStoreTest {
    private static MPPServer echoServer;
    private static InetSocketAddress address = uniqueAddress();

    private MPPClient client;

    @BeforeClass
    public static void setupClass() throws Exception {
        MPPCommandProcessor echoProcessor = command -> {
            System.out.format("Received command %s\n", command.getType());
            switch (command.getType()) {
                case GET:
                    return command.getKey();
                case PUT:
                    return command.getKey().equals("key");
                case REMOVE:
                    return command.getKey().equals("key");
                default:
                    return null;
            }
        };
        echoServer = new MPPServer(address, () -> echoProcessor);
        echoServer.start();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        echoServer.stop();
        echoServer = null;
    }

    @Before
    public void setUp() throws Exception {
        client = new MPPClient(address);
    }

    @After
    public void tearDown() throws Exception {
        client.close();
        client = null;
    }

    @Test
    public void testGet() {
        String key = client.get("key", String.class);
        assertThat(key, notNullValue());
        assertThat(key, is(equalTo("key")));
        key = client.get("anotherKey", String.class);
        assertThat(key, notNullValue());
        assertThat(key, is(equalTo("anotherKey")));
    }

    @Test
    public void testAdd() {
        assertThat(client.add("key", "value"), is(true));
        assertThat(client.add("anotherKey", "value"), is(false));
    }

    @Test
    public void testDelete() {
        assertThat(client.delete("key"), is(true));
        assertThat(client.delete("anotherKey"), is(false));
    }
}