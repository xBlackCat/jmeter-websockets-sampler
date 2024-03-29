package org.apache.jmeter.protocol.websocket.sampler;

import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 14.08.2014 10:42
 *
 * @author xBlackCat
 */
public class SocketManager {
    private final ConcurrentHashMap<String, KeepAliveConnection> sockets = new ConcurrentHashMap<>();

    public void shutdown() {
        sockets.values().forEach(c -> c.close(StatusCode.NORMAL, "Shutting down pool"));
        sockets.clear();
    }

    public IConnection getConnection(WebSocketSampler sampler) throws Exception {
        final String connectionId = sampler.getConnectionId();
        if (connectionId != null) {
            KeepAliveConnection c = sockets.get(connectionId);
            if (c != null) {
                c.borrow();
                c.init(" - Reusing existing connection\n");
                c.log("Connections in pool: " + sockets.size() + "\n");
                c.setContext(new Context(sampler));
                return c;
            }
        }

        final Boolean trustAll = sampler.isIgnoreSslErrors();

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setTrustAll(trustAll);
        WebSocketClient webSocketClient = new WebSocketClient(sslContextFactory);

        AConnection connection;
        if (connectionId != null) {
            connection = sockets.computeIfAbsent(
                    connectionId,
                    s -> new KeepAliveConnection(this, connectionId, webSocketClient)
            );
        } else {
            connection = new SingleConnection(webSocketClient);
        }
        connection.init(" - Creating a new connection\n");

        connection.log("Connections in pool: " + sockets.size() + "\n");

        int timeout = sampler.getConnectionTimeout() == 0 ? WebSocketSampler.DEFAULT_CONNECTION_TIMEOUT : sampler.getConnectionTimeout();
        if (!connection.isConnected()) {
            webSocketClient.setConnectTimeout(timeout);
            webSocketClient.start();
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            webSocketClient.connect(connection, sampler.getUri(), request);
        }

        if (connection.awaitOpen(timeout, TimeUnit.MILLISECONDS)) {
            if (!connection.isConnected() && connectionId != null) {
                sockets.remove(connectionId);
                connection.log("Failed to connect to server");
                return new ErrorConnection(connection.getLogMessage(), connection.getError());
            }

            connection.setContext(new Context(sampler));
            return connection;
        } else {
            connection.log("Connection timeout is reached.");
            return new ErrorConnection(connection.getLogMessage(), 408);
        }
    }

    public void remove(String connectionId) {
        sockets.remove(connectionId);
    }
}
