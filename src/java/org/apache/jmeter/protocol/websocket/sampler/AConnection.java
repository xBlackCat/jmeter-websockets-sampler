package org.apache.jmeter.protocol.websocket.sampler;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 * 14.08.2014 14:42
 *
 * @author xBlackCat
 */
public abstract class AConnection implements IConnection {
    protected static final Logger log = LoggingManager.getLoggerForClass();

    protected final int messageBacklog;
    private final boolean disconnectOnReceive;
    protected WebSocketClient client;
    protected Queue<String> responseBacklog = new ConcurrentLinkedQueue<>();
    protected Integer error = 0;
    protected StringBuffer logMessage = new StringBuffer();
    protected CountDownLatch openLatch = new CountDownLatch(1);
    protected CountDownLatch messageLatch = new CountDownLatch(1);
    protected final AtomicReference<Session> session = new AtomicReference<>();
    protected int messageCounter = 1;
    protected final Pattern responsePattern;

    public AConnection(
            WebSocketClient client,
            int messageBacklog,
            String responsePattern,
            boolean disconnectOnReceive
    ) {
        this.client = client;
        this.messageBacklog = messageBacklog;
        this.disconnectOnReceive = disconnectOnReceive;

        logMessage.append("\n\n[Execution Flow]\n");
        logMessage.append(" - Opening new connection\n");

        this.responsePattern = buildPattern(responsePattern, "response message");
    }

    private Pattern buildPattern(String patternTmpl, String title) {
        final String pattern = new CompoundVariable(patternTmpl).execute();

        try {
            logMessage.append(" - Using ").append(title).append(" pattern \"").append(pattern).append("\"\n");
            return pattern.isEmpty() ? null : Pattern.compile(pattern);
        } catch (Exception ex) {
            logMessage.append(" - Invalid ").append(title)
                    .append(" regular expression pattern: ")
                    .append(ex.getLocalizedMessage()).append("\n");
            log.error("Invalid " + title + " regular expression pattern: " + ex.getLocalizedMessage());
            return null;
        }
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        log.debug("Received message: " + msg);
        String length = " (" + msg.length() + " bytes)";
        logMessage.append(" - Received message #").append(messageCounter).append(length);
        addResponseMessage("[Message " + (messageCounter++) + "]\n" + msg + "\n\n");

        if (responsePattern == null || responsePattern.matcher(msg).find()) {
            logMessage.append("; matched response pattern");
            if (responsePattern != null) {
                logMessage.append(responsePattern.pattern());
            }
            logMessage.append("\n");
            messageLatch.countDown();
            if (disconnectOnReceive) {
                close(StatusCode.NORMAL, "Closed on receive");
            }
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        logMessage.append(" - WebSocket connection has been opened").append("\n");
        KeepAliveConnection.log.debug("Connect " + session.isOpen());
        this.session.set(session);
        openLatch.countDown();
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        if (statusCode != 1000) {
            KeepAliveConnection.log.error("Disconnect " + statusCode + ": " + reason);
            logMessage.append(" - WebSocket conection closed unexpectedly by the server: [");
            logMessage.append(statusCode);
            logMessage.append("] ");
            logMessage.append(reason);
            logMessage.append("\n");
            error = statusCode;
        } else {
            logMessage.append(" - WebSocket conection has been successfully closed by the server").append("\n");
            KeepAliveConnection.log.debug("Disconnect " + statusCode + ": " + reason);
        }

        //Notify connection opening and closing latches of the closed connection
        openLatch.countDown();
        messageLatch.countDown();
    }

    /**
     * @return response message made of messages saved in the responseBacklog cache
     */
    public String getResponseMessage() {
        StringBuilder responseMessage = new StringBuilder();

        //Iterate through response messages saved in the responseBacklog cache
        responseBacklog.forEach(responseMessage::append);

        return responseMessage.toString();
    }

    /**
     * Await for establishing of connection
     *
     * @param duration connection timeout duration
     * @param unit     time unit
     * @return true if the connection is established and false if timeout is reached
     * @throws InterruptedException
     */
    public boolean awaitOpen(int duration, TimeUnit unit) throws InterruptedException {
        logMessage.append(" - Waiting for the server connection for ").append(duration).append(" ").append(unit.toString()).append("\n");
        boolean res = this.openLatch.await(duration, unit);

        if (getSession() != null) {
            logMessage.append(" - Connection established").append("\n");
        } else {
            logMessage.append(" - Cannot connect to the remote server").append("\n");
        }

        return res;
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session.get();
    }

    public boolean sendMessageSync(String message, int responseTimeout) throws IOException, InterruptedException {
        final Session session = getSession();
        session.getRemote().sendString(message);

        //Wait for any of the following:
        // - Response matching response pattern is received
        // - Response matching connection closing pattern is received
        // - Timeout is reached
        log(" - Waiting for messages for " + responseTimeout + " " + TimeUnit.MILLISECONDS.toString() + "\n");
        return messageLatch.await(responseTimeout, TimeUnit.MILLISECONDS);
    }


    public abstract void close();

    public void close(int statusCode, String statusText) {
        //Closing WebSocket session
        if (getSession() != null) {
            getSession().close(statusCode, statusText);
            logMessage.append(" - WebSocket session closed by the client").append("\n");
        } else {
            logMessage.append(" - WebSocket session wasn't started (...that's odd)").append("\n");
        }


        //Stopping WebSocket client; thanks m0ro
        try {
            client.stop();
            logMessage.append(" - WebSocket client closed by the client").append("\n");
        } catch (Exception e) {
            logMessage.append(" - WebSocket client wasn't started (...that's odd)").append("\n");
        }
    }

    /**
     * @return the error
     */
    public int getError() {
        return error;
    }

    /**
     * @return the logMessage
     */
    public String getLogMessage() {
        logMessage.append("\n\n[Variables]\n");
        logMessage.append(" - Message count: ").append(messageCounter - 1).append("\n");

        return logMessage.toString();
    }

    public void log(String message) {
        logMessage.append(message);
    }

    /**
     * @return the connected
     */
    public boolean isConnected() {
        return getSession() != null;
    }

    public void initialize() {
        logMessage = new StringBuffer();
        logMessage.append("\n\n[Execution Flow]\n");
        logMessage.append(" - Reusing exising connection\n");
        error = 0;

        this.messageLatch = new CountDownLatch(1);
    }

    private void addResponseMessage(String message) {
        while (responseBacklog.size() >= messageBacklog) {
            responseBacklog.poll();
        }
        responseBacklog.add(message);
    }
}
