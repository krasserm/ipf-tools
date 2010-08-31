/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.tools.manager.connection;

/**
 * Configuration of a management connection to a IPF instance. Connection
 * configurations are distinguished by their names.
 * 
 * @author Mitko Kolev
 */
public class ConnectionConfigurationImpl implements IConnectionConfiguration {

    private static final long serialVersionUID = -5662511690282050211L;

    private final AuthenticationCredentials credentials;

    private final String host;

    private final int jmxJNDIPort;

    private final String name;

    private String jmxURLPath;

    private int jmxConnectorPort;

    /**
     * Creates a connection instance.
     * 
     * @param name
     *            the repository unique name of the connection
     * @param host
     *            the host of the management target.
     * @param jmxJNDIPort
     *            the jmxJNDIPort of the management target.
     */
    public ConnectionConfigurationImpl(String name, String host, int jmxJNDIPort) {
        this(name, host, jmxJNDIPort, new AuthenticationCredentials("", ""));
    }

    public ConnectionConfigurationImpl(String name, String host,
            int jmxJNDIPort, String userName, String password) {
        this(name, host, jmxJNDIPort, new AuthenticationCredentials(userName,
                password));
    }

    public ConnectionConfigurationImpl(String name, String host,
            int jmxJNDIPort, AuthenticationCredentials credentials) {
        this.name = name;
        this.host = host;
        this.jmxJNDIPort = jmxJNDIPort;
        this.credentials = (AuthenticationCredentials) credentials.clone();
        this.jmxURLPath = "jmxrmi";
    }

    public AuthenticationCredentials getAuthenticationCredentials() {
        return credentials;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return jmxJNDIPort;
    }

    public String getJMXURLPath() {
        return jmxURLPath;
    }

    public void setJMXURLPath(String jmxURLPath) {
        this.jmxURLPath = jmxURLPath;
    }

    public String getName() {
        return name;
    }

    public int getJMXRMIConnectorPort() {
        return jmxConnectorPort;
    }

    public void setJMXConnectorPort(int jmxConnectorPort) {
        this.jmxConnectorPort = jmxConnectorPort;
    }

    public String buildJMXURL() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("service:jmx:rmi://");
        if (getJMXRMIConnectorPort() != 0) {
            // we have set a JMX RMI connector jmxJNDIPort
            buffer.append(getHost() + ":" + getJMXRMIConnectorPort());
        }
        buffer.append("/jndi/rmi://");
        buffer.append(getHost());
        buffer.append(":");
        buffer.append(getPort());
        String jmxPath = getJMXURLPath();
        buffer.append("/").append(jmxPath == null ? "jmxrmi" : jmxPath);
        return buffer.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConnectionConfigurationImpl other = (ConnectionConfigurationImpl) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public int compareTo(IConnectionConfiguration o) {
        if (o == null) {
            return -1;
        }
        if (o.getName() == null) {
            return -1;
        }
        return this.name.compareTo(o.getName());
    }

    @Override
    public String toString() {
        StringBuilder connection = new StringBuilder("");
        if (this.getAuthenticationCredentials().isValid()) {
            String userName = this.getAuthenticationCredentials().getUserName();
            if (userName != null) {
                connection.append(
                        this.getAuthenticationCredentials().getUserName())
                        .append("@");
            }
        }
        if (host != null) {
            connection.append(this.getHost()).append(":")
                    .append(this.getPort());
        }
        return connection.toString();
    }

    @Override
    public Object clone() {
        ConnectionConfigurationImpl connection = new ConnectionConfigurationImpl(
                this.name, this.host, this.jmxJNDIPort, this.credentials);
        connection.setJMXURLPath(jmxURLPath);
        connection.setJMXConnectorPort(this.getJMXRMIConnectorPort());
        return connection;
    }

}
