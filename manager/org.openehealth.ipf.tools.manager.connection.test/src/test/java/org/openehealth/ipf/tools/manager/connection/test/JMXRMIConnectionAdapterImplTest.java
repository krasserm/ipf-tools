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

import java.io.IOException;

import javax.management.remote.JMXConnectionNotification;

import junit.framework.TestCase;

import org.openehealth.ipf.tools.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.tools.manager.connection.impl.JMXRMIConnectionAdapterImpl;
import org.openehealth.ipf.tools.manager.connection.mock.JMXRMIConnectionAdapterImplMock;

import static org.openehealth.ipf.tools.manager.connection.mock.JMXConnectionManagerImplMock.JMX_MANAGEMENT_PORT;
import static org.openehealth.ipf.tools.manager.connection.test.ConnectionUtils.createConnection;

/**
 * @author Mitko Kolev
 */
public class JMXRMIConnectionAdapterImplTest extends TestCase {
    JMXRMIConnectionAdapterImpl adapter;
    IConnectionConfiguration connectionConfiguration;

    @Override
    public void setUp() {
        connectionConfiguration = createConnection(JMX_MANAGEMENT_PORT);
        adapter = new JMXRMIConnectionAdapterImplMock(connectionConfiguration);
    }

    @Override
    public void tearDown() {
        adapter.closeConnection();
    }

    public void testConstructor() {
        assertNotNull(adapter);
        assertNull(adapter.getConnector());
        assertEquals(adapter.getConnectionConfiguration(),
                connectionConfiguration);
        try {
            adapter.getMBeanServerConnection();
            fail();
        } catch (IOException ioe) {
            assert (true);
        }
        try {
            adapter = new JMXRMIConnectionAdapterImpl(null);
        } catch (IllegalArgumentException ie) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    public void testURLCreation() {
        adapter = new JMXRMIConnectionAdapterImpl(connectionConfiguration);
        String url = connectionConfiguration.buildJMXURL();
        StringBuilder builder = new StringBuilder();
        builder.append("service:jmx:rmi:///jndi/rmi://");
        builder.append(connectionConfiguration.getHost());
        builder.append(":");
        builder.append(connectionConfiguration.getPort());
        builder.append("/jmxrmi");
        assertTrue(builder.toString().equals(url));
        // test authentication credentials changed
        adapter.getConnectionConfiguration().getAuthenticationCredentials()
                .setUserName("asdfasdf");
        adapter.getConnectionConfiguration().getAuthenticationCredentials()
                .setPassword("asdfasdf");
     
        assertEquals(builder.toString(),url);
    }

    public void testOpenConnection() throws Exception {
        adapter.openConnection();
        assertAdapterConnected();
    }

    private void assertAdapterConnected() throws Exception {
        assertNotNull(adapter.getConnector());
        assertNotNull(adapter.getMBeanServerConnection());
    }

    public void testCloseConnection() throws Exception {
        adapter.openConnection();
        assertAdapterConnected();
        adapter.closeConnection();
        assertAdapterConnectionClosed();
    }

    private void assertAdapterConnectionClosed() throws Exception {
        assertNull(adapter.getConnector());
        try {
            assertTrue(adapter.getMBeanServerConnection() != null);
        } catch (IOException ioe) {
            //expected
        } catch (Exception e) {
            fail();
        }
    }

    public void testAddNotificationListener1() throws Exception {
        adapter.openConnection();
        assertAdapterConnected();
        // connection failed
        adapter.handleNotification(new JMXConnectionNotification(
                JMXConnectionNotification.FAILED, new Object(), "1234", 0L,
                "msg", new Object()), new Object());
        assertAdapterConnectionClosed();
    }

    public void testAddNotificationListener2() throws Exception {
        adapter.openConnection();
        assertAdapterConnected();
        // connection failed
        adapter.handleNotification(new JMXConnectionNotification(
                JMXConnectionNotification.CLOSED, new Object(), "1234", 0L,
                "msg", new Object()), new Object());
        assertAdapterConnectionClosed();
    }

    public void testAddNotificationListener3() throws Exception {
        adapter.openConnection();
        assertAdapterConnected();
        // connection failed
        adapter.handleNotification(new JMXConnectionNotification(
                JMXConnectionNotification.NOTIFS_LOST, new Object(), "1234",
                0L, "msg", new Object()), new Object());
        assertAdapterConnectionClosed();
    }

    public void testAddNotificationListener4() throws Exception {
        adapter.openConnection();
        assertAdapterConnected();
        // connection failed
        adapter.handleNotification(new JMXConnectionNotification(
                JMXConnectionNotification.OPENED, new Object(), "1234", 0L,
                "msg", new Object()), new Object());
        assertAdapterConnected();
    }
}
