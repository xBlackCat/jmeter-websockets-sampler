package org.apache.jmeter.protocol.websocket.sampler;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * 14.08.2014 14:42
 *
 * @author xBlackCat
 */
public abstract class AConnection implements IConnection {
    protected static final Logger log = LoggingManager.getLoggerForClass();

    private final Lock lock = new ReentrantLock();

    protected WebSocketClient client;
    protected Queue<String> responseBacklog = new ConcurrentLinkedQueue<>();
    protected AtomicInteger error = new AtomicInteger();
    protected StringBuffer logMessage = new StringBuffer();
    protected final CountDownLatch openLatch = new CountDownLatch(1);
    protected final ResettableCountDownLatch messageLatch = new ResettableCountDownLatch(1);
    protected AtomicInteger messageCounter = new AtomicInteger(0);

    private IContext context = null;
    protected Session session = null;

    public AConnection(WebSocketClient client) {
        this.client = client;

        logMessage.append("\n\n[Execution Flow]\n");
        logMessage.append(" - Opening new connection\n");
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        log.debug("Received message: " + msg);
        String length = " (" + msg.length() + " bytes)";
        logMessage.append(" - Received message #").append(messageCounter).append(length);
        addResponseMessage("[Message " + (messageCounter.incrementAndGet()) + "]\n" + msg + "\n\n");

        final Pattern responsePattern;
        final boolean disconnectOnReceive;
        lock.lock();
        try {
            responsePattern = context.getMessagePattern();
            disconnectOnReceive = context.isCloseOnReceive();
        } finally {
            lock.unlock();
        }

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
        log.debug("Connect " + session.isOpen());
        lock.lock();
        try {
            this.session = session;
            openLatch.countDown();
        } finally {
            lock.unlock();
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        if (statusCode != 1000) {
            log.error("Disconnect " + statusCode + ": " + reason);
            logMessage.append(" - WebSocket connection closed unexpectedly by the server: [");
            logMessage.append(statusCode);
            logMessage.append("] ");
            logMessage.append(reason);
            logMessage.append("\n");
            error.set(statusCode);
        } else {
            logMessage.append(" - WebSocket connection has been successfully closed by the server").append("\n");
            log.debug("Disconnect " + statusCode + ": " + reason);
        }

        openLatch.countDown();
        messageLatch.countDown();

        try {
            client.stop();
            logMessage.append(" - WebSocket client closed by the client").append("\n");
        } catch (Exception e) {
            logMessage.append(" - WebSocket client wasn't started (...that's odd)").append("\n");
        }

        onClose();
    }

    protected void onClose() {
    }

    @OnWebSocketError
    public void onError(Session session, Throwable e) {
        log("Got error in session: " + e.getMessage() + "\n");
        error.set(500);
        openLatch.countDown();
        messageLatch.countDown();
    }

    /**
     * @return response message made of messages saved in the responseBacklog cache
     */
    public String getResponseMessage() {
        StringBuilder responseMessage = new StringBuilder();

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
            onClose();
        }

        return res;
    }

    /**
     * @return the session
     */
    public Session getSession() {
        lock.lock();
        try {
            return session;
        } finally {
            lock.unlock();
        }

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

    public void close(int statusCode, String statusText) {
        //Closing WebSocket session
        if (getSession() != null) {
            getSession().close(statusCode, statusText);
            logMessage.append(" - WebSocket session closed by the client").append("\n");
        } else {
            logMessage.append(" - WebSocket session wasn't started (...that's odd)").append("\n");
        }
    }

    /**
     * @return the error
     */
    public int getError() {
        return error.get();
    }

    /**
     * @return the logMessage
     */
    public String getLogMessage() {
        logMessage.append("\n\n[Variables]\n");
        logMessage.append(" - Message count: ").append(messageCounter.get()).append("\n");

        return logMessage.toString();
    }

    public void log(String message) {
        logMessage.append(message);
    }

    /**
     * @return the connected
     */
    @Override
    public boolean isConnected() {
        return getSession() != null;
    }

    public void setContext(IContext ctx) {
        messageLatch.reset();
        responseBacklog.clear();
        lock.lock();
        try {
            context = ctx;
        } finally {
            lock.unlock();
        }
    }

    public void init() {
        logMessage = new StringBuffer();
        logMessage.append("\n\n[Execution Flow]\n");
        logMessage.append(" - Reusing existing connection\n");
        error.set(0);
    }

    private void addResponseMessage(String message) {
        final int messageBacklog;
        lock.lock();
        try {
            messageBacklog = context.getMessageBackLog();
        } finally {
            lock.unlock();
        }
        while (responseBacklog.size() >= messageBacklog) {
            responseBacklog.poll();
        }
        responseBacklog.add(message);
    }
}
