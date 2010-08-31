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
package org.openehealth.ipf.tools.manager.connection.mock;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;

import javax.naming.AuthenticationException;

import org.openehealth.ipf.tools.manager.connection.AuthenticationCredentials;
import org.openehealth.ipf.tools.manager.connection.ConnectionConfigurationImpl;
import org.openehealth.ipf.tools.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.tools.manager.connection.impl.ConnectionConfigurationRepository;
import org.openehealth.ipf.tools.manager.connection.impl.IJMXConnectionAdapter;
import org.openehealth.ipf.tools.manager.connection.impl.JMXConnectionManagerImpl;

/**
 * Creates a number of mock connections
 * 
 * @author Mitko Kolev
 */
public class JMXConnectionManagerImplMock extends JMXConnectionManagerImpl {

    private final static int CONNECTIONS_COUNT = 12;

    public final static int JMX_MANAGEMENT_PORT = 9999;
    
    public JMXConnectionManagerImplMock(ConnectionConfigurationRepository repository) {
        super(repository);
    }

    public JMXConnectionManagerImplMock(Set<IConnectionConfiguration> connections) {
        super(connections);
    }
    
    public JMXConnectionManagerImplMock() {
        super(new ConnectionConfigurationRepository());
        this.createMockConnections();
    }

    /**
     * Mock adapter which connects to the java JMX server of the process.
     * 
     * @see JMXRMIConnectionAdapterImplMock
     * @param adapter
     */
    @Override
    public void addConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration != null) {

            IJMXConnectionAdapter adapter = new JMXRMIConnectionAdapterImplMock(
                    connectionConfiguration);
            super.addConnectionAdapter(adapter);
        } else {
            super.addConnectionConfiguration((IConnectionConfiguration) null);
        }
    }

    private void createMockConnections() {
        for (int t = 0; t < CONNECTIONS_COUNT; t++) {
            String host = "localhost";
            int port = 1801 + t * 10;
            IConnectionConfiguration connectionConfiguration;
            if (t % 2 == 0) {
                connectionConfiguration = new ConnectionConfigurationImpl(host
                        + String.valueOf(port), host, port, getCredentials(t));
            } else {
                connectionConfiguration = new ConnectionConfigurationImpl(host
                        + String.valueOf(port), host, port);
            }
            this.addConnectionConfiguration(connectionConfiguration);

        }
    }

    private static AuthenticationCredentials getCredentials(final int t) {
        AuthenticationCredentials credentials = new AuthenticationCredentials(
                "tech" + (t == 0 ? "" : String.valueOf(t)), "tech");
        return credentials;
    }

    @Override
    public void closeJMXConnection(
            IConnectionConfiguration connectionConfiguration) {
        super.closeJMXConnection(connectionConfiguration);
    }

    @Override
    public void openJMXConnection(
            IConnectionConfiguration connectionConfiguration)
            throws MalformedURLException, IOException, AuthenticationException {
        super.openJMXConnection(connectionConfiguration);
    }

    @Override
    public void removeConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration) {
        super.removeConnectionConfiguration(connectionConfiguration);
    }

    @Override
    public boolean isOpen(IConnectionConfiguration connectionConfiguration) {
        return super.isOpen(connectionConfiguration);
    }

    @Override
    public List<IConnectionConfiguration> getConnectionConfigurations() {
        return super.getConnectionConfigurations();
    }

}
