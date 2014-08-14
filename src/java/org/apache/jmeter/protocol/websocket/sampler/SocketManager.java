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
            IConnection c = sockets.get(connectionId);
            if (c != null) {
                return c;
            }
        }

        final Boolean trustAll = sampler.isIgnoreSslErrors();

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setTrustAll(trustAll);
        WebSocketClient webSocketClient = new WebSocketClient(sslContextFactory);

        AConnection socket;
        if (connectionId != null) {
            socket = getSocketFromPool(sampler, connectionId, webSocketClient);
        } else {
            socket = new SingleConnection(
                    webSocketClient,
                    sampler.getResponsePattern(),
                    sampler.getCloseConnectionOnReceive(),
                    sampler.getMessageBacklog()
            );
        }

        int timeout = sampler.getConnectionTimeout() == 0 ? WebSocketSampler.DEFAULT_CONNECTION_TIMEOUT : sampler.getConnectionTimeout();
        if (!socket.isConnected()) {
            webSocketClient.setConnectTimeout(timeout);
            webSocketClient.start();
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            webSocketClient.connect(socket, sampler.getUri(), request);
        }

        if (socket.awaitOpen(timeout, TimeUnit.MILLISECONDS)) {
            return socket;
        } else {
            return null;
        }
    }

    private KeepAliveConnection getSocketFromPool(WebSocketSampler sampler, String connectionId, WebSocketClient webSocketClient) {
        return sockets.computeIfAbsent(
                connectionId, s -> new KeepAliveConnection(
                        this,
                        connectionId,
                        webSocketClient,
                        sampler.getResponsePattern(),
                        sampler.getCloseConnectionOnReceive(),
                        sampler.getMessageBacklog()
                )
        );
    }
}
