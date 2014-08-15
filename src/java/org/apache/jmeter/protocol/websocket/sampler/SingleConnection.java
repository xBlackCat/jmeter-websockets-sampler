package org.apache.jmeter.protocol.websocket.sampler;

import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;

@WebSocket(maxTextMessageSize = 256 * 1024 * 1024)
public class SingleConnection extends AConnection {
    public SingleConnection(WebSocketClient client) {
        super(client);
    }

    @Override
    public void close() {
        close(StatusCode.NORMAL, "JMeter closed session.");
    }
}
