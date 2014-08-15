package org.apache.jmeter.protocol.websocket.sampler;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;

@WebSocket(maxTextMessageSize = 256 * 1024 * 1024)
public class KeepAliveConnection extends AConnection {
    protected final SocketManager parent;
    private final String connectionId;
    private final ResettableCountDownLatch usingLatch = new ResettableCountDownLatch(1);

    public KeepAliveConnection(SocketManager parent, String connectionId, WebSocketClient client) {
        super(client);
        this.parent = parent;
        this.connectionId = connectionId;
        usingLatch.countDown();
    }

    public void borrow() throws InterruptedException {
        usingLatch.await();
        usingLatch.reset();
    }

    public String getConnectionId() {
        return connectionId;
    }

    @Override
    public void close() {
        usingLatch.countDown();
    }

    @Override
    public void onClose() {
        parent.remove(connectionId);
    }
}
