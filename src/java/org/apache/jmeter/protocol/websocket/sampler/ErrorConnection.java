package org.apache.jmeter.protocol.websocket.sampler;

import java.io.IOException;

/**
 * 8/19/2014 10:50 AM
 *
 * @author xBlackCat
 */
public class ErrorConnection implements IConnection {
    private final String message;
    private final int errorCode;

    public ErrorConnection(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public boolean sendMessageSync(String payloadMessage, int responseTimeout) throws IOException, InterruptedException {
        return false;
    }

    @Override
    public void log(String s) {
    }

    @Override
    public String getResponseMessage() {
        return null;
    }

    @Override
    public int getError() {
        return errorCode;
    }

    @Override
    public String getLogMessage() {
        return message;

    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
