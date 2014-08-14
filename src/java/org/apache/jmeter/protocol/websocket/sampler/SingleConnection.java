package org.apache.jmeter.protocol.websocket.sampler;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.util.regex.Pattern;

@WebSocket(maxTextMessageSize = 256 * 1024 * 1024)
public class SingleConnection extends AConnection {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public SingleConnection(
            WebSocketClient client,
            String responsePattern,
            String closeConnectionPattern,
            int messageBacklog
    ) {
        super(client, messageBacklog,responsePattern,closeConnectionPattern);
    }

    @Override
    public void close() {
        close(StatusCode.NORMAL, "JMeter closed session.");
    }
}
