package org.apache.jmeter.protocol.websocket.gui;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.protocol.websocket.sampler.WebSocketSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.awt.*;

/**
 * 14.08.2014 15:27
 *
 * @author xBlackCat
 */
public class WebSocketSamplerGui extends AbstractSamplerGui {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private final WebSocketSamplerPanel webSocketSamplerPanel;

    public WebSocketSamplerGui() {
        super();
        webSocketSamplerPanel = new WebSocketSamplerPanel();
        webSocketSamplerPanel.init();

        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);
        add(webSocketSamplerPanel, BorderLayout.CENTER);
    }

    @Override
    public String getStaticLabel() {
        return "WebSocket Sampler";
    }

    @Override
    public String getLabelResource() {
        throw new IllegalStateException("This shouldn't be called"); //$NON-NLS-1$
    }

    @Override
    public TestElement createTestElement() {
        final WebSocketSampler sampler = new WebSocketSampler();
        configure(sampler);
        return sampler;
    }

    @Override
    public void clearGui() {
        super.clearGui();
        webSocketSamplerPanel.init();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof WebSocketSampler) {
            WebSocketSampler webSocketSamplerTestElement = (WebSocketSampler) element;
            webSocketSamplerPanel.setServerAddress(webSocketSamplerTestElement.getServerAddress());
            webSocketSamplerPanel.setServerPort(webSocketSamplerTestElement.getServerPort());
            webSocketSamplerPanel.setProtocol(webSocketSamplerTestElement.getProtocol());
            webSocketSamplerPanel.setContextPath(webSocketSamplerTestElement.getContextPath());
            webSocketSamplerPanel.setContentEncoding(webSocketSamplerTestElement.getContentEncoding());
            webSocketSamplerPanel.setRequestPayload(webSocketSamplerTestElement.getRequestPayload());
            webSocketSamplerPanel.setResponseTimeout(webSocketSamplerTestElement.getResponseTimeout());
            webSocketSamplerPanel.setConnectionTimeout(webSocketSamplerTestElement.getConnectionTimeout());
            webSocketSamplerPanel.setIgnoreSslErrors(webSocketSamplerTestElement.isIgnoreSslErrors());
            webSocketSamplerPanel.setStreamingConnection(webSocketSamplerTestElement.isStreamingConnection());
            webSocketSamplerPanel.setConnectionId(webSocketSamplerTestElement.getConnectionId());
            webSocketSamplerPanel.setResponsePattern(webSocketSamplerTestElement.getResponsePattern());
            webSocketSamplerPanel.setCloseConnectionOnReceive(webSocketSamplerTestElement.getCloseConnectionOnReceive());
            webSocketSamplerPanel.setProxyAddress(webSocketSamplerTestElement.getProxyAddress());
            webSocketSamplerPanel.setProxyPassword(webSocketSamplerTestElement.getProxyPassword());
            webSocketSamplerPanel.setProxyPort(webSocketSamplerTestElement.getProxyPort());
            webSocketSamplerPanel.setProxyUsername(webSocketSamplerTestElement.getProxyUsername());
            webSocketSamplerPanel.setMessageBacklog(webSocketSamplerTestElement.getMessageBacklog());

            Arguments queryStringParameters = webSocketSamplerTestElement.getQueryStringParameters();
            if (queryStringParameters != null) {
                webSocketSamplerPanel.getAttributePanel().configure(queryStringParameters);
            }
        }
    }


    @Override
    public void modifyTestElement(TestElement te) {
        configureTestElement(te);
        if (te instanceof WebSocketSampler) {
            WebSocketSampler sampler = (WebSocketSampler) te;
            sampler.setServerAddress(webSocketSamplerPanel.getServerAddress());
            sampler.setServerPort(webSocketSamplerPanel.getServerPort());
            sampler.setProtocol(webSocketSamplerPanel.getProtocol());
            sampler.setContextPath(webSocketSamplerPanel.getContextPath());
            sampler.setContentEncoding(webSocketSamplerPanel.getContentEncoding());
            sampler.setRequestPayload(webSocketSamplerPanel.getRequestPayload());
            sampler.setConnectionTimeout(webSocketSamplerPanel.getConnectionTimeout());
            sampler.setResponseTimeout(webSocketSamplerPanel.getResponseTimeout());
            sampler.setIgnoreSslErrors(webSocketSamplerPanel.isIgnoreSslErrors());
            sampler.setStreamingConnection(webSocketSamplerPanel.isStreamingConnection());
            sampler.setConnectionId(webSocketSamplerPanel.getConnectionId());
            sampler.setResponsePattern(webSocketSamplerPanel.getResponsePattern());
            sampler.setCloseConnectionOnReceive(webSocketSamplerPanel.getCloseConnectionOnReceive());
            sampler.setProxyAddress(webSocketSamplerPanel.getProxyAddress());
            sampler.setProxyPassword(webSocketSamplerPanel.getProxyPassword());
            sampler.setProxyPort(webSocketSamplerPanel.getProxyPort());
            sampler.setProxyUsername(webSocketSamplerPanel.getProxyUsername());
            sampler.setMessageBacklog(webSocketSamplerPanel.getMessageBacklog());

            ArgumentsPanel queryStringParameters = webSocketSamplerPanel.getAttributePanel();
            if (queryStringParameters != null) {
                sampler.setQueryStringParameters((Arguments) queryStringParameters.createTestElement());
            }
        }
    }
}
