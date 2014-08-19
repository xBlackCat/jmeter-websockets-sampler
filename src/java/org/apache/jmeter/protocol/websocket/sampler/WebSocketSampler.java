package org.apache.jmeter.protocol.websocket.sampler;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.http.util.EncoderCache;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 14.08.2014 10:09
 *
 * @author xBlackCat
 */
public class WebSocketSampler extends AbstractSampler implements TestStateListener, ThreadListener {
    public static int DEFAULT_CONNECTION_TIMEOUT = 20000; //20 sec
    public static int DEFAULT_RESPONSE_TIMEOUT = 20000; //20 sec
    public static int MESSAGE_BACKLOG_COUNT = 3;

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String ARG_VAL_SEP = "="; // $NON-NLS-1$
    private static final String QRY_SEP = "&"; // $NON-NLS-1$
    private static final String WS_PREFIX = "ws://"; // $NON-NLS-1$
    private static final String WSS_PREFIX = "wss://"; // $NON-NLS-1$
    private static final String DEFAULT_PROTOCOL = "ws";

    private static final ThreadLocal<SocketManager> socketManager = new ThreadLocal<SocketManager>() {
        @Override
        protected SocketManager initialValue() {
            return new SocketManager();
        }
    };

    public WebSocketSampler() {
        log.debug("Created " + this);
    }

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.setSampleLabel(getName());
        sampleResult.setDataEncoding(getContentEncoding());

        //This StringBuilder will track all exceptions related to the protocol processing
        StringBuilder errorList = new StringBuilder();
        errorList.append("\n\n[Problems]\n");

        //Set the message payload in the Sampler
        String payloadMessage = getRequestPayload();
        sampleResult.setSamplerData(payloadMessage);

        //Could improve precision by moving this closer to the action
        sampleResult.sampleStart();

        try {
            try (IConnection connection = socketManager.get().getConnection(this)) {
                if (connection.isConnected()) {
                    //Send message only if it is not empty
                    if (!payloadMessage.isEmpty()) {
                        final int timeout = getResponseTimeout();
                        if (!connection.sendMessageSync(payloadMessage, timeout == 0 ? DEFAULT_RESPONSE_TIMEOUT : timeout)) {
                            connection.log("Response timeout is reached");
                        }
                    }

                    //If no response is received set code 204; actually not used...needs to do something else
                    final String responseMessage = connection.getResponseMessage();
                    if (responseMessage == null || responseMessage.isEmpty()) {
                        sampleResult.setResponseCode("204");
                    }

                    //Set sampler response code
                    if (connection.getError() != 0) {
                        sampleResult.setSuccessful(false);
                        sampleResult.setResponseCode(String.valueOf(connection.getError()));
                    } else {
                        sampleResult.setResponseCodeOK();
                        sampleResult.setSuccessful(true);
                    }
                    //set sampler response
                    sampleResult.setResponseData(responseMessage, getContentEncoding());
                } else {
                    //Couldn't open a connection, set the status and exit
                    sampleResult.setResponseCode(String.valueOf(connection.getError()));
                    sampleResult.setSuccessful(false);
                    errorList.append(" - Connection couldn't be opened").append("\n");
                }

                sampleResult.sampleEnd();
                sampleResult.setResponseMessage(connection.getLogMessage() + errorList);
                return sampleResult;
            }
        } catch (URISyntaxException e) {
            errorList.append(" - Invalid URI syntax: ").append(e.getMessage()).append("\n");
        } catch (IOException e) {
            errorList.append(" - IO Exception: ").append(e.getMessage()).append("\n");
        } catch (InterruptedException e) {
            errorList.append(" - Execution interrupted: ").append(e.getMessage()).append("\n");
        } catch (Exception e) {
            errorList.append(" - Unexpected error: ").append(e.getMessage()).append("\n");
        }

        sampleResult.sampleEnd();
        sampleResult.setSuccessful(false);
        sampleResult.setResponseMessage(errorList.toString());
        return sampleResult;
    }

    @Override
    public void testStarted() {
    }

    @Override
    public void testStarted(String host) {
        testEnded();
    }

    @Override
    public void testEnded() {
    }

    @Override
    public void testEnded(String host) {
        testEnded();
    }

    @Override
    public void threadStarted() {

    }

    @Override
    public void threadFinished() {
        socketManager.get().shutdown();
        socketManager.remove();
    }

    public URI getUri() throws URISyntaxException {
        String path = this.getContextPath();
        // Hack to allow entire URL to be provided in host field
        if (path.startsWith(WS_PREFIX)
                || path.startsWith(WSS_PREFIX)) {
            return new URI(path);
        }
        String domain = getServerAddress();
        String protocol = getProtocol();
        // HTTP URLs must be absolute, allow file to be relative
        if (!path.startsWith("/")) { // $NON-NLS-1$
            path = "/" + path; // $NON-NLS-1$
        }

        String queryString = getQueryString(getContentEncoding());
        return new URI(protocol, null, domain, getServerPort(), path, queryString, null);
    }

    public int getServerPort() {
        return getPropertyAsInt("serverPort", -1);
    }

    public void setServerPort(int port) {
        setProperty("serverPort", port);
    }

    public int getResponseTimeout() {
        return getPropertyAsInt("responseTimeout", 0);
    }

    public void setResponseTimeout(int responseTimeout) {
        setProperty("responseTimeout", responseTimeout);
    }

    public int getConnectionTimeout() {
        return getPropertyAsInt("connectionTimeout", 0);
    }

    public void setConnectionTimeout(int connectionTimeout) {
        setProperty("connectionTimeout", connectionTimeout);
    }

    public void setProtocol(String protocol) {
        setProperty("protocol", protocol);
    }

    public String getProtocol() {
        String protocol = getPropertyAsString("protocol");
        if (protocol == null || protocol.isEmpty()) {
            return DEFAULT_PROTOCOL;
        }
        return protocol;
    }

    public void setServerAddress(String serverAddress) {
        setProperty("serverAddress", serverAddress);
    }

    public String getServerAddress() {
        return getPropertyAsString("serverAddress");
    }

    public void setContextPath(String contextPath) {
        setProperty("contextPath", contextPath);
    }

    public String getContextPath() {
        return getPropertyAsString("contextPath");
    }

    public void setContentEncoding(String contentEncoding) {
        setProperty("contentEncoding", contentEncoding);
    }

    public String getContentEncoding() {
        return getPropertyAsString("contentEncoding", "UTF-8");
    }

    public void setRequestPayload(String requestPayload) {
        setProperty("requestPayload", requestPayload);
    }

    public String getRequestPayload() {
        return getPropertyAsString("requestPayload");
    }

    public void setIgnoreSslErrors(Boolean ignoreSslErrors) {
        setProperty("ignoreSslErrors", ignoreSslErrors);
    }

    public Boolean isIgnoreSslErrors() {
        return getPropertyAsBoolean("ignoreSslErrors");
    }

    public void setStreamingConnection(Boolean streamingConnection) {
        setProperty("streamingConnection", streamingConnection);
    }

    public Boolean isStreamingConnection() {
        return getPropertyAsBoolean("streamingConnection");
    }

    public void setConnectionId(String connectionId) {
        setProperty("connectionId", connectionId);
    }

    public String getConnectionId() {
        return getPropertyAsString("connectionId");
    }

    public void setResponsePattern(String responsePattern) {
        setProperty("responsePattern", responsePattern);
    }

    public String getResponsePattern() {
        return getPropertyAsString("responsePattern");
    }

    public void setCloseConnectionOnReceive(boolean closeConnectionOnReceive) {
        setProperty("closeConnectionOnReceive", closeConnectionOnReceive);
    }

    public boolean getCloseConnectionOnReceive() {
        return getPropertyAsBoolean("closeConnectionOnReceive");
    }

    public void setProxyAddress(String proxyAddress) {
        setProperty("proxyAddress", proxyAddress);
    }

    public String getProxyAddress() {
        return getPropertyAsString("proxyAddress");
    }

    public void setProxyPassword(String proxyPassword) {
        setProperty("proxyPassword", proxyPassword);
    }

    public String getProxyPassword() {
        return getPropertyAsString("proxyPassword");
    }

    public void setProxyPort(int proxyPort) {
        setProperty("proxyPort", proxyPort);
    }

    public int getProxyPort() {
        return getPropertyAsInt("proxyPort");
    }

    public void setProxyUsername(String proxyUsername) {
        setProperty("proxyUsername", proxyUsername);
    }

    public String getProxyUsername() {
        return getPropertyAsString("proxyUsername");
    }

    public void setMessageBacklog(int messageBacklog) {
        setProperty("messageBacklog", messageBacklog);
    }

    public int getMessageBacklog() {
        return getPropertyAsInt("messageBacklog", MESSAGE_BACKLOG_COUNT);
    }

    public String getQueryString(String contentEncoding) {
        // Check if the sampler has a specified content encoding
        if (JOrphanUtils.isBlank(contentEncoding)) {
            // We use the encoding which should be used according to the HTTP spec, which is UTF-8
            contentEncoding = EncoderCache.URL_ARGUMENT_ENCODING;
        }
        StringBuilder buf = new StringBuilder();
        PropertyIterator i = getQueryStringParameters().iterator();
        boolean first = true;
        while (i.hasNext()) {
            HTTPArgument item;
            Object objectValue = i.next().getObjectValue();
            try {
                item = (HTTPArgument) objectValue;
            } catch (ClassCastException e) {
                item = new HTTPArgument((Argument) objectValue);
            }
            final String encodedName = item.getEncodedName();
            if (encodedName.length() == 0) {
                continue; // Skip parameters with a blank name (allows use of optional variables in parameter lists)
            }
            if (!first) {
                buf.append(QRY_SEP);
            } else {
                first = false;
            }
            buf.append(encodedName);
            if (item.getMetaData() == null) {
                buf.append(ARG_VAL_SEP);
            } else {
                buf.append(item.getMetaData());
            }

            // Encode the parameter value in the specified content encoding
            try {
                buf.append(item.getEncodedValue(contentEncoding));
            } catch (UnsupportedEncodingException e) {
                log.warn("Unable to encode parameter in encoding " + contentEncoding + ", parameter value not included in query string");
            }
        }
        return buf.toString();
    }

    public void setQueryStringParameters(Arguments queryStringParameters) {
        setProperty(new TestElementProperty("queryStringParameters", queryStringParameters));
    }

    public Arguments getQueryStringParameters() {
        return (Arguments) getProperty("queryStringParameters").getObjectValue();
    }


}
