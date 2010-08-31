/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.tools.manager.connection.ui.wizards;

/**
 * POJO holder for the data in the new connection wizard page
 * 
 * @author Mitko Kolev
 */
public class ConnectionPageData {

    private String name;
    private String host;
    private int port;
    private boolean useAuthentication;
    private String username;
    private String password;
    private boolean useCustomJMXSettings;
    private String jmxPath;
    private String jmxConnectorPort;
    private boolean openConnectionOnFinish;

    /**
     * Creates the new connection wizard page data with default values
     */
    public ConnectionPageData() {
        name = "";
        host = "";
        port = 1801;
        useAuthentication = false;
        username = "";
        password = "";
        useCustomJMXSettings = false;
        jmxPath = "jmxrmi";
        jmxConnectorPort = "";
        openConnectionOnFinish = true;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isUseAuthentication() {
        return useAuthentication;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isUseCustomJMXSettings() {
        return useCustomJMXSettings;
    }

    public String getJMXPath() {
        return jmxPath;
    }

    public String getJMXConnectorPort() {
        return jmxConnectorPort;
    }

    public boolean isOpenConnectionOnFinish() {
        return openConnectionOnFinish;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUseAuthentication(boolean useAuthentication) {
        this.useAuthentication = useAuthentication;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUseCustomJMXSettings(boolean useCustomJMXSettings) {
        this.useCustomJMXSettings = useCustomJMXSettings;
    }

    public void setJMXPath(String path) {
        this.jmxPath = path;
    }

    public void setJMXConnectorPort(String connectorPort) {
        this.jmxConnectorPort = connectorPort;
    }

    public void setOpenConnectionOnFinish(boolean openConnectionOnFinish) {
        this.openConnectionOnFinish = openConnectionOnFinish;
    }

}
