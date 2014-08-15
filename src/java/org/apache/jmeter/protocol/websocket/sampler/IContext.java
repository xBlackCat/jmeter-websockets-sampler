package org.apache.jmeter.protocol.websocket.sampler;

import java.util.regex.Pattern;

/**
 * 14.08.2014 17:19
 *
 * @author xBlackCat
 */
public interface IContext {
    Pattern getMessagePattern();

    boolean isCloseOnReceive();

    int getMessageBackLog();
}
