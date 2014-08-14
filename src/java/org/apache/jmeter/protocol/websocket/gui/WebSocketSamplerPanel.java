package org.apache.jmeter.protocol.websocket.gui;

import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.protocol.http.gui.HTTPArgumentsPanel;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

/**
 * 14.08.2014 15:36
 *
 * @author xBlackCat
 */
public class WebSocketSamplerPanel extends JPanel {
    public void init() {
    }

    private HTTPArgumentsPanel attributePanel;

    /**
     * Creates new form WebSocketSamplerPanel
     */
    public WebSocketSamplerPanel() {
        initComponents();

        attributePanel = new HTTPArgumentsPanel();
        queryStringAttributesPanel.add(attributePanel);
    }

    private void initComponents() {

        JPanel paneWebServer = new JPanel();
        JLabel jLabel1 = new JLabel();
        serverAddressTextField = new JTextField();
        JLabel jLabel2 = new JLabel();
        serverPortTextField = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));
        JPanel paneTimeout = new JPanel();
        JLabel lConnection = new JLabel("Connection:");
        connectionTimeoutTextField = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));
        JLabel lResponse = new JLabel("Response:");
        responseTimeoutTextField = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));
        JPanel paneRequest = new JPanel();
        JLabel lProtocol = new JLabel("Protocol [ws/wss]:");
        JLabel lPath = new JLabel("Path:");
        JLabel lContentEncoding = new JLabel("Content encoding:");
        contextPathTextField = new JTextField();
        protocolTextField = new JTextField();
        contentEncodingTextField = new JTextField();
        JLabel lConnectionId = new JLabel("Connection Id:");
        connectionIdTextField = new JTextField();
        queryStringAttributesPanel = new JPanel();
        ignoreSslErrorsCheckBox = new JCheckBox();
        JScrollPane jScrollPane1 = new JScrollPane();
        requestPayloadEditorPane = new JEditorPane();
        JLabel lRequestData = new JLabel("Request data");
        streamingConnectionCheckBox = new JCheckBox();
        JPanel jPanel5 = new JPanel();
        JLabel jLabel7 = new JLabel();
        responsePatternTextField = new JTextField();
        JLabel jLabel9 = new JLabel();
        closeConnectionPatternTextField = new JTextField();
        JLabel jLabel16 = new JLabel();
        messageBacklogTextField = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));
        JPanel jPanel6 = new JPanel();
        JLabel jLabel10 = new JLabel();
        proxyAddressTextField = new JTextField();
        JLabel jLabel11 = new JLabel();
        proxyPortTextField = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));
        JLabel jLabel12 = new JLabel();
        proxyUsernameTextField = new JTextField();
        JLabel jLabel13 = new JLabel();
        proxyPasswordTextField = new JTextField();

        paneWebServer.setBorder(BorderFactory.createTitledBorder("Web Server"));

        jLabel1.setText("Server Name or IP:");

        jLabel2.setText("Port Number:");

        GroupLayout paneWebServerLayout = new GroupLayout(paneWebServer);
        paneWebServer.setLayout(paneWebServerLayout);
        paneWebServerLayout.setHorizontalGroup(
                paneWebServerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                paneWebServerLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(serverAddressTextField)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel2)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                serverPortTextField,
                                                GroupLayout.PREFERRED_SIZE,
                                                43,
                                                GroupLayout.PREFERRED_SIZE
                                        )
                                        .addContainerGap()
                        )
        );
        paneWebServerLayout.setVerticalGroup(
                paneWebServerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                paneWebServerLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                paneWebServerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel1)
                                                        .addComponent(
                                                                serverAddressTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE
                                                        )
                                                        .addComponent(jLabel2)
                                                        .addComponent(
                                                                serverPortTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE
                                                        )
                                        )
                                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
        );

        paneTimeout.setBorder(BorderFactory.createTitledBorder("Timeout (milliseconds)"));

        GroupLayout paneTimeoutLayout = new GroupLayout(paneTimeout);
        paneTimeout.setLayout(paneTimeoutLayout);
        paneTimeoutLayout.setHorizontalGroup(
                paneTimeoutLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                paneTimeoutLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(lConnection)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(connectionTimeoutTextField)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lResponse)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(responseTimeoutTextField)
                                        .addContainerGap()
                        )
        );
        paneTimeoutLayout.setVerticalGroup(
                paneTimeoutLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                paneTimeoutLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                paneTimeoutLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lConnection)
                                                        .addComponent(
                                                                connectionTimeoutTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE
                                                        )
                                                        .addComponent(lResponse)
                                                        .addComponent(
                                                                responseTimeoutTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE
                                                        )
                                        )
                                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
        );

        paneRequest.setBorder(BorderFactory.createTitledBorder("WebSocket Request"));

        protocolTextField.setToolTipText("");

        queryStringAttributesPanel.setLayout(new javax.swing.BoxLayout(queryStringAttributesPanel, javax.swing.BoxLayout.LINE_AXIS));

        ignoreSslErrorsCheckBox.setText("Ignore SSL certificate errors");

        jScrollPane1.setViewportView(requestPayloadEditorPane);

        streamingConnectionCheckBox.setText("Streaming connection");

        GroupLayout paneRequestLayout = new GroupLayout(paneRequest);
        paneRequest.setLayout(paneRequestLayout);
        paneRequestLayout.setHorizontalGroup(
                paneRequestLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                paneRequestLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                paneRequestLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(
                                                                queryStringAttributesPanel,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE
                                                        )
                                                        .addComponent(jScrollPane1)
                                                        .addGroup(
                                                                paneRequestLayout.createSequentialGroup()
                                                                        .addComponent(lProtocol)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(
                                                                                protocolTextField,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                40,
                                                                                GroupLayout.PREFERRED_SIZE
                                                                        )
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(lContentEncoding)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(
                                                                                contentEncodingTextField,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                40,
                                                                                GroupLayout.PREFERRED_SIZE
                                                                        )
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(lConnectionId)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(connectionIdTextField)
                                                        )
                                                        .addGroup(
                                                                paneRequestLayout.createSequentialGroup()
                                                                        .addGroup(
                                                                                paneRequestLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(lRequestData)
                                                                                        .addGroup(
                                                                                                paneRequestLayout.createSequentialGroup()
                                                                                                        .addComponent(
                                                                                                                ignoreSslErrorsCheckBox
                                                                                                        )
                                                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                        .addComponent(
                                                                                                                streamingConnectionCheckBox
                                                                                                        )
                                                                                        )
                                                                        )
                                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        )
                                                        .addGroup(
                                                                paneRequestLayout.createSequentialGroup()
                                                                        .addComponent(lPath)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(contextPathTextField)
                                                        )
                                        )
                                        .addContainerGap()
                        )
        );
        paneRequestLayout.setVerticalGroup(
                paneRequestLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                paneRequestLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(
                                                paneRequestLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lProtocol)
                                                        .addComponent(
                                                                protocolTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE
                                                        )
                                                        .addComponent(lContentEncoding)
                                                        .addComponent(
                                                                contentEncodingTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE
                                                        )
                                                        .addComponent(lConnectionId)
                                                        .addComponent(
                                                                connectionIdTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE
                                                        )
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                                paneRequestLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lPath)
                                                        .addComponent(
                                                                contextPathTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE
                                                        )
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(
                                                paneRequestLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(ignoreSslErrorsCheckBox)
                                                        .addComponent(streamingConnectionCheckBox)
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                queryStringAttributesPanel,
                                                GroupLayout.DEFAULT_SIZE,
                                                102,
                                                Short.MAX_VALUE
                                        )
                                        .addGap(8, 8, 8)
                                        .addComponent(lRequestData)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                                        .addContainerGap()
                        )
        );

        jPanel5.setBorder(BorderFactory.createTitledBorder("WebSocket Response"));

        jLabel7.setText("Response pattern:");

        jLabel9.setText("Close connection pattern:");

        jLabel16.setText("Message backlog:");

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel5Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(
                                                                jPanel5Layout.createSequentialGroup()
                                                                        .addComponent(jLabel7)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(responsePatternTextField)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(jLabel16)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(
                                                                                messageBacklogTextField,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                40,
                                                                                GroupLayout.PREFERRED_SIZE
                                                                        )
                                                        )
                                                        .addGroup(
                                                                GroupLayout.Alignment.TRAILING,
                                                                jPanel5Layout.createSequentialGroup()
                                                                        .addComponent(jLabel9)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(closeConnectionPatternTextField)
                                                        )
                                        )
                                        .addContainerGap()
                        )
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel5Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(
                                                                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabel16)
                                                                        .addComponent(
                                                                                messageBacklogTextField,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE
                                                                        )
                                                        )
                                                        .addGroup(
                                                                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabel7)
                                                                        .addComponent(
                                                                                responsePatternTextField,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE
                                                                        )
                                                        )
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel9)
                                                        .addComponent(
                                                                closeConnectionPatternTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE
                                                        )
                                        )
                                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
        );

        jPanel6.setBorder(BorderFactory.createTitledBorder("Proxy Server (currently not supported by Jetty)"));

        jLabel10.setText("Server Name or IP:");

        proxyAddressTextField.setEnabled(false);

        jLabel11.setText("Port Number:");

        proxyPortTextField.setEnabled(false);

        jLabel12.setText("Username:");

        proxyUsernameTextField.setEnabled(false);

        jLabel13.setText("Password:");

        proxyPasswordTextField.setEnabled(false);

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel6Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(proxyAddressTextField)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel11)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                proxyPortTextField,
                                                GroupLayout.PREFERRED_SIZE,
                                                39,
                                                GroupLayout.PREFERRED_SIZE
                                        )
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel12)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                proxyUsernameTextField,
                                                GroupLayout.PREFERRED_SIZE,
                                                64,
                                                GroupLayout.PREFERRED_SIZE
                                        )
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                proxyPasswordTextField,
                                                GroupLayout.PREFERRED_SIZE,
                                                64,
                                                GroupLayout.PREFERRED_SIZE
                                        )
                                        .addContainerGap()
                        )
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel6Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(
                                                                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(
                                                                                proxyUsernameTextField,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE
                                                                        )
                                                                        .addComponent(jLabel12)
                                                        )
                                                        .addGroup(
                                                                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabel11)
                                                                        .addComponent(
                                                                                proxyPortTextField,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE
                                                                        )
                                                        )
                                                        .addGroup(
                                                                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabel10)
                                                                        .addComponent(
                                                                                proxyAddressTextField,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE
                                                                        )
                                                                        .addComponent(jLabel13)
                                                                        .addComponent(
                                                                                proxyPasswordTextField,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE
                                                                        )
                                                        )
                                        )
                                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(
                                                                paneRequest,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE
                                                        )
                                                        .addComponent(
                                                                jPanel5,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE
                                                        )
                                                        .addGroup(
                                                                layout.createSequentialGroup()
                                                                        .addComponent(
                                                                                paneWebServer,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE
                                                                        )
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(
                                                                                paneTimeout,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE
                                                                        )
                                                        )
                                                        .addComponent(
                                                                jPanel6,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE
                                                        )
                                        )
                                        .addContainerGap()
                        )
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(
                                                                paneTimeout,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE
                                                        )
                                                        .addComponent(
                                                                paneWebServer,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE
                                                        )
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                paneRequest,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                jPanel5,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                jPanel6,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE
                                        )
                                        .addContainerGap()
                        )
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTextField closeConnectionPatternTextField;
    private JTextField connectionIdTextField;
    private JTextField connectionTimeoutTextField;
    private JTextField contentEncodingTextField;
    private JTextField contextPathTextField;
    private JCheckBox ignoreSslErrorsCheckBox;
    private JTextField messageBacklogTextField;
    private JTextField protocolTextField;
    private JTextField proxyAddressTextField;
    private JTextField proxyPasswordTextField;
    private JTextField proxyPortTextField;
    private JTextField proxyUsernameTextField;
    private JPanel queryStringAttributesPanel;
    private JEditorPane requestPayloadEditorPane;
    private JTextField responsePatternTextField;
    private JTextField responseTimeoutTextField;
    private JTextField serverAddressTextField;
    private JTextField serverPortTextField;
    private JCheckBox streamingConnectionCheckBox;
    // End of variables declaration//GEN-END:variables

    public void setCloseConnectionPattern(String closeConnectionPattern) {
        closeConnectionPatternTextField.setText(closeConnectionPattern);
    }

    public String getCloseConnectionPattern() {
        return closeConnectionPatternTextField.getText();
    }

    public void setConnectionId(String connectionId) {
        connectionIdTextField.setText(connectionId);
    }

    public String getConnectionId() {
        return connectionIdTextField.getText();
    }

    public void setContentEncoding(String contentEncoding) {
        contentEncodingTextField.setText(contentEncoding);
    }

    public String getContentEncoding() {
        return contentEncodingTextField.getText();
    }

    public void setContextPath(String contextPath) {
        contextPathTextField.setText(contextPath);
    }

    public String getContextPath() {
        return contextPathTextField.getText();
    }

    public void setProtocol(String protocol) {
        protocolTextField.setText(protocol);
    }

    public String getProtocol() {
        return protocolTextField.getText();
    }

    public void setProxyAddress(String proxyAddress) {
        proxyAddressTextField.setText(proxyAddress);
    }

    public String getProxyAddress() {
        return proxyAddressTextField.getText();
    }

    public void setProxyPassword(String proxyPassword) {
        proxyPasswordTextField.setText(proxyPassword);
    }

    public String getProxyPassword() {
        return proxyPasswordTextField.getText();
    }

    public void setProxyPort(int proxyPort) {
        proxyPortTextField.setText(proxyPort == -1 ? "" : String.valueOf(proxyPort));
    }

    public int getProxyPort() {
        final String s = proxyPortTextField.getText();
        if (s == null || s.length() == 0) {
            return -1;
        } else {
            return Integer.parseInt(s);
        }
    }

    public void setProxyUsername(String proxyUsername) {
        proxyUsernameTextField.setText(proxyUsername);
    }

    public String getProxyUsername() {
        return proxyUsernameTextField.getText();
    }

    public void setResponsePattern(String responsePattern) {
        responsePatternTextField.setText(responsePattern);
    }

    public String getResponsePattern() {
        return responsePatternTextField.getText();
    }

    public void setResponseTimeout(int responseTimeout) {
        responseTimeoutTextField.setText(responseTimeout == 0 ? "" : String.valueOf(responseTimeout));
    }

    public int getResponseTimeout() {
        final String s = responseTimeoutTextField.getText();
        if (s == null || s.length() == 0) {
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }

    public void setConnectionTimeout(int connectionTimeout) {
        connectionTimeoutTextField.setText(connectionTimeout == 0 ? "" : String.valueOf(connectionTimeout));
    }

    public int getConnectionTimeout() {
        final String s = connectionTimeoutTextField.getText();
        if (s == null || s.length() == 0) {
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }

    public void setServerAddress(String serverAddress) {
        serverAddressTextField.setText(serverAddress);
    }

    public String getServerAddress() {
        return serverAddressTextField.getText();
    }

    public void setServerPort(int serverPort) {
        serverPortTextField.setText(serverPort == -1 ? "" : String.valueOf(serverPort));
    }

    public int getServerPort() {
        final String s = serverPortTextField.getText();
        if (s == null || s.length() == 0) {
            return -1;
        } else {
            return Integer.parseInt(s);
        }
    }

    public void setRequestPayload(String requestPayload) {
        requestPayloadEditorPane.setText(requestPayload);
    }

    public String getRequestPayload() {
        return requestPayloadEditorPane.getText();
    }

    public void setStreamingConnection(Boolean streamingConnection) {
        streamingConnectionCheckBox.setSelected(streamingConnection);
    }

    public Boolean isStreamingConnection() {
        return streamingConnectionCheckBox.isSelected();
    }

    public void setIgnoreSslErrors(boolean ignoreSslErrors) {
        ignoreSslErrorsCheckBox.setSelected(ignoreSslErrors);
    }

    public boolean isIgnoreSslErrors() {
        return ignoreSslErrorsCheckBox.isSelected();
    }

    public void setMessageBacklog(int messageBacklog) {
        messageBacklogTextField.setText(messageBacklog == 0 ? "" : String.valueOf(messageBacklog));
    }

    public int getMessageBacklog() {
        final String s = messageBacklogTextField.getText();
        if (s == null || s.length() == 0) {
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }

    /**
     * @return the attributePanel
     */
    public ArgumentsPanel getAttributePanel() {
        return attributePanel;
    }
}
