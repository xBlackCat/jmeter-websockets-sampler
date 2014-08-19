package org.apache.jmeter.protocol.websocket.sampler;

import java.io.IOException;

/**
 * 14.08.2014 14:32
 *
 * @author xBlackCat
 */
public interface IConnection extends AutoCloseable {
    @Override
    void close() throws IOException;

    boolean sendMessageSync(String payloadMessage, int responseTimeout) throws IOException, InterruptedException;

    void log(String s);

    String getResponseMessage();

    int getError();

    String getLogMessage();

    boolean isConnected();
}
