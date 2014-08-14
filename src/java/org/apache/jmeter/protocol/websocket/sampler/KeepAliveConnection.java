package org.apache.jmeter.protocol.websocket.sampler;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.util.regex.Pattern;

@WebSocket(maxTextMessageSize = 256 * 1024 * 1024)
public class KeepAliveConnection extends AConnection {
    protected final SocketManager parent;
    private final String connectionId;

    public KeepAliveConnection(
            SocketManager parent,
            String connectionId,
            WebSocketClient client,
            String responsePattern,
            String closeConnectionPattern,
            int messageBacklog
    ) {
        super(client, messageBacklog, responsePattern, closeConnectionPattern);
        this.parent = parent;
        this.connectionId = connectionId;
    }

    public String getConnectionId() {
        return connectionId;
    }

    @Override
    public void close() {
    }
}
