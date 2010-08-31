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
package org.openehealth.ipf.tools.manager.connection.test;

import static org.openehealth.ipf.tools.manager.connection.mock.JMXConnectionManagerImplMock.JMX_MANAGEMENT_PORT;
import static org.openehealth.ipf.tools.manager.connection.test.ConnectionUtils.createConnection;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.management.MBeanServerConnection;

import junit.framework.TestCase;

import org.openehealth.ipf.tools.manager.connection.ConnectionConfigurationImpl;
import org.openehealth.ipf.tools.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.tools.manager.connection.impl.ConnectionConfigurationRepository;
import org.openehealth.ipf.tools.manager.connection.impl.JMXRMIConnectionAdapterImpl;
import org.openehealth.ipf.tools.manager.connection.mock.JMXConnectionManagerImplMock;

/**
 * 
 * @author Mitko Kolev
 */
public class JMXConnectionManagerImplTest extends TestCase {

    private JMXConnectionManagerImplMock connectionManager;

    @Override
    public void setUp() {

        connectionManager = new JMXConnectionManagerImplMock();
    }

    public void testAddConnection() throws Exception {

        // the port is in the ConnectionRepositoryMock
        IConnectionConfiguration connectionConfiguration = createConnection(JMX_MANAGEMENT_PORT);
        connectionManager.addConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationAdded(connectionConfiguration);

        connectionManager
                .addConnectionConfiguration((IConnectionConfiguration) null);

    }

    public void testAddConnectionRealAdapter() {
        IConnectionConfiguration connectionConfiguration = createConnection(JMX_MANAGEMENT_PORT);
        connectionManager.addConnectionAdapter(new JMXRMIConnectionAdapterImpl(
                connectionConfiguration));
        assertConnectionConfigurationAdded(connectionConfiguration);
    }

    private void assertConnectionConfigurationAdded(
            IConnectionConfiguration connectionConfiguration) {
        assertTrue(connectionManager.getConnectionConfigurations().contains(
                connectionConfiguration));
        // call open connection
        assertTrue(connectionManager
                .isConnectionNameInUse(connectionConfiguration.getName()));

        assertFalse(connectionManager.isOpen(connectionConfiguration));
        // should throw IOException here
        try {
            connectionManager
                    .getMBeanServerConnectionConfigurationAdapter(connectionConfiguration);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    public void testRemoveConnection() throws Exception {

        IConnectionConfiguration connectionConfiguration = createConnection(JMX_MANAGEMENT_PORT);
        connectionManager.addConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationAdded(connectionConfiguration);

        connectionManager
                .removeConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationRemoved(connectionConfiguration);

        // must not yield exception
        connectionManager.removeConnectionConfiguration(null);

    }

    private void assertConnectionConfigurationRemoved(
            IConnectionConfiguration connectionConfiguration) {
        assertFalse(connectionManager.getConnectionConfigurations().contains(
                connectionConfiguration));
        // call open connection
        assertFalse(connectionManager
                .isConnectionNameInUse(connectionConfiguration.getName()));

        assertFalse(connectionManager.isOpen(connectionConfiguration));

        try {
            connectionManager
                    .getMBeanServerConnectionConfigurationAdapter(connectionConfiguration);
            fail();
        } catch (IOException e) {
            // expected
            return;
        }
        fail();
    }

    public void testOpenAllConnections() throws Exception {
        List<IConnectionConfiguration> connectionConfigurations = connectionManager
                .getConnectionConfigurations();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            connectionManager.openJMXConnection(connectionConfiguration);
            assertConnectionConfigurationOpened(connectionConfiguration);

        }
    }

    public void testOpenConnection() throws Exception {

        // the port is in the ConnectionRepositoryMock
        IConnectionConfiguration connectionConfiguration = createConnection(JMX_MANAGEMENT_PORT);
        connectionManager.addConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationAdded(connectionConfiguration);

        connectionManager.openJMXConnection(connectionConfiguration);
        assertConnectionConfigurationOpened(connectionConfiguration);

    }

    public void testOpenConnectionRealAdapter() throws Exception {
        IConnectionConfiguration connectionConfiguration = createConnection(JMX_MANAGEMENT_PORT);
        connectionManager.addConnectionAdapter(new JMXRMIConnectionAdapterImpl(
                connectionConfiguration));
        assertConnectionConfigurationAdded(connectionConfiguration);
        try {
            connectionManager.openJMXConnection(connectionConfiguration);
            fail();
        } catch (IOException ioe) {
            // expected
        } catch (Throwable t) {
            fail();
        }
        assertConnectionConfigurationClosed(connectionConfiguration);

    }

    private void assertConnectionConfigurationOpened(
            IConnectionConfiguration connectionConfiguration)
            throws IOException {
        assertTrue(connectionManager.isOpen(connectionConfiguration));
        MBeanServerConnection mbsc = connectionManager
                .getMBeanServerConnectionConfigurationAdapter(connectionConfiguration);
        assertNotNull(mbsc);
    }

    public void testAddOpenCloseConnection() throws Exception {

        // the port is in the ConnectionRepositoryMock
        IConnectionConfiguration connectionConfiguration = createConnection(JMX_MANAGEMENT_PORT);
        connectionManager.addConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationAdded(connectionConfiguration);
        assertConnectionConfigurationClosed(connectionConfiguration);

        // one open and close
        connectionManager.openJMXConnection(connectionConfiguration);
        assertConnectionConfigurationOpened(connectionConfiguration);
        connectionManager.closeJMXConnection(connectionConfiguration);
        assertConnectionConfigurationClosed(connectionConfiguration);

        // 2 times close
        connectionManager.openJMXConnection(connectionConfiguration);
        assertConnectionConfigurationOpened(connectionConfiguration);
        connectionManager.closeJMXConnection(connectionConfiguration);
        assertConnectionConfigurationClosed(connectionConfiguration);
        connectionManager.closeJMXConnection(connectionConfiguration);
        assertConnectionConfigurationClosed(connectionConfiguration);

    }

    public void testCloseConnection2() throws Exception {
        List<IConnectionConfiguration> connectionConfigurations = connectionManager
                .getConnectionConfigurations();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            connectionManager.openJMXConnection(connectionConfiguration);
            assertConnectionConfigurationOpened(connectionConfiguration);
        }
        IConnectionConfiguration connectionConfigurationNonExisting = new ConnectionConfigurationImpl(
                "testOpenConnectionNotAdded", "localhost",
                JMXConnectionManagerImplMock.JMX_MANAGEMENT_PORT);
        // nothing should be closed
        connectionManager
                .closeJMXConnection(connectionConfigurationNonExisting);

        for (IConnectionConfiguration connectionConfiguration : connectionManager
                .getConnectionConfigurations()) {
            connectionManager.openJMXConnection(connectionConfiguration);
            assertConnectionConfigurationOpened(connectionConfiguration);
        }

        connectionManager.closeJMXConnection(null);

        for (IConnectionConfiguration connectionConfiguration : connectionManager
                .getConnectionConfigurations()) {
            connectionManager.openJMXConnection(connectionConfiguration);
            assertConnectionConfigurationOpened(connectionConfiguration);
        }
    }

    private void assertConnectionConfigurationClosed(
            IConnectionConfiguration connectionConfiguration) {
        assertFalse(connectionManager.isOpen(connectionConfiguration));
        try {
            connectionManager
                    .getMBeanServerConnectionConfigurationAdapter(connectionConfiguration);
            fail();
        } catch (IOException ioe) {
            // expected
            return;
        }
        fail();
    }

    public void testCloseAllConnectionsSilently() throws Exception {
        List<IConnectionConfiguration> connectionConfigurations = connectionManager
                .getConnectionConfigurations();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            connectionManager.openJMXConnection(connectionConfiguration);
            assertConnectionConfigurationOpened(connectionConfiguration);
        }
        connectionManager.closeAllJMXConnectionsSilently();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            assertConnectionConfigurationClosed(connectionConfiguration);
        }
        // do it once again
        connectionManager.closeAllJMXConnectionsSilently();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            assertConnectionConfigurationClosed(connectionConfiguration);
        }
    }

    public void testConstructor1() {
        ConnectionConfigurationRepository repo = new ConnectionConfigurationRepository();
        IConnectionConfiguration connectionConfiguration = createConnection(JMX_MANAGEMENT_PORT);
        repo.addConnectionConfiguration(connectionConfiguration);
        
        connectionManager = new JMXConnectionManagerImplMock(repo);
        assertEquals(1, connectionManager.getConnectionConfigurations().size());
        assertEquals(connectionManager
                .getConnectionConfigurationForName(connectionConfiguration
                        .getName()), connectionConfiguration);
        assertConnectionConfigurationClosed(connectionConfiguration);

    }

    public void testConstructor2() {
        IConnectionConfiguration connectionConfiguration = createConnection(JMX_MANAGEMENT_PORT);
        Set<IConnectionConfiguration> connections = new TreeSet<IConnectionConfiguration>();
        connections.add(connectionConfiguration);

        connectionManager = new JMXConnectionManagerImplMock(connections);
        assertEquals(1, connectionManager.getConnectionConfigurations().size());
        assertEquals(connectionManager
                .getConnectionConfigurationForName(connectionConfiguration
                        .getName()), connectionConfiguration);
        assertConnectionConfigurationClosed(connectionConfiguration);
    }

    public void testIsConnectionNameInUse() {
        List<IConnectionConfiguration> connectionConfigurations = connectionManager
                .getConnectionConfigurations();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            assertTrue(connectionManager
                    .isConnectionNameInUse(connectionConfiguration.getName()));
        }
        assertFalse(connectionManager.isConnectionNameInUse("not-in-use"));
        assertFalse(connectionManager.isConnectionNameInUse(""));
        assertFalse(connectionManager.isConnectionNameInUse(null));
    }

    public void testGetMBeanServerConnection() throws Exception {
        List<IConnectionConfiguration> connectionConfigurations = connectionManager
                .getConnectionConfigurations();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            connectionManager.openJMXConnection(connectionConfiguration);
        }
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            assertNotNull(connectionManager
                    .getMBeanServerConnectionConfigurationAdapter(connectionConfiguration));
        }
        connectionManager.closeAllJMXConnectionsSilently();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            try {
                connectionManager
                        .getMBeanServerConnectionConfigurationAdapter(connectionConfiguration);
                fail();
            } catch (IOException e) {
                // expected
            } catch (Exception e) {
                fail();
            }
        }
    }

    public void testGetMBeanServerNullConnection() throws Exception {
        try {
            connectionManager
                    .getMBeanServerConnectionConfigurationAdapter(null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        } catch (Exception e) {
            fail();
        }
    }
}
