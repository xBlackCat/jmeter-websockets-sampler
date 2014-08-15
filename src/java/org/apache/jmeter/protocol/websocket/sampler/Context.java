package org.apache.jmeter.protocol.websocket.sampler;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.regex.Pattern;

/**
 * 14.08.2014 17:42
 *
 * @author xBlackCat
 */
class Context implements IContext {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private final Pattern response;
    private final boolean closeOnReceive;
    private final int messageBacklog;

    public Context(WebSocketSampler sampler) {
        response = buildPattern(sampler.getResponsePattern());
        closeOnReceive = sampler.getCloseConnectionOnReceive();
        messageBacklog = sampler.getMessageBacklog();
    }

    @Override
    public Pattern getMessagePattern() {
        return response;
    }

    @Override
    public boolean isCloseOnReceive() {
        return closeOnReceive;
    }

    @Override
    public int getMessageBackLog() {
        return messageBacklog;
    }

    private Pattern buildPattern(String patternRegex) {
        final String pattern = new CompoundVariable(patternRegex).execute();

        try {
            return pattern.isEmpty() ? null : Pattern.compile(pattern);
        } catch (Exception ex) {
            log.error("Invalid response regular expression pattern: " + ex.getLocalizedMessage());
            return null;
        }
    }

}
