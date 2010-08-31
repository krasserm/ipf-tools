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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.openehealth.ipf.tools.manager.connection.AuthenticationCredentials;
import org.openehealth.ipf.tools.manager.connection.ConnectionConfigurationImpl;
import org.openehealth.ipf.tools.manager.connection.IConnectionConfiguration;

public class ConnectionConfigurationImplTest extends TestCase {

    public void testConnectionConfigurationEqualsAndHashcode() {
        List<IConnectionConfiguration> added = new ArrayList<IConnectionConfiguration>();
        for (int t = 0; t < 20; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", t + 123);
            assertFalse(added.contains(connectionConfiguration));
            assertEquals(connectionConfiguration, connectionConfiguration);
            IConnectionConfiguration connectionConfiguration2 = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", t + 123);

            assertEquals(connectionConfiguration, connectionConfiguration2);
            assertEquals(connectionConfiguration2, connectionConfiguration);
            assertEquals(connectionConfiguration2.hashCode(),
                    connectionConfiguration.hashCode());
            added.add(connectionConfiguration);
        }

    }

    public void testConnectionConfigurationConstructor() {
        IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                "connection", "localhost", 123);
        assertEquals(connectionConfiguration.getHost(), "localhost");
        assertEquals(connectionConfiguration.getPort(), 123);
        assertEquals(connectionConfiguration.getName(), "connection");
    }

    public void testDefaultJMXPath() {
        IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                "connection", "localhost", 123);
        assertEquals("jmxrmi", connectionConfiguration.getJMXURLPath());
    }

    public void testSetJMXPath() {
        ConnectionConfigurationImpl connectionConfiguration = new ConnectionConfigurationImpl(
                "connection", "localhost", 123);
        String newPath = "newPath";
        connectionConfiguration.setJMXURLPath(newPath);
        assertEquals(newPath, connectionConfiguration.getJMXURLPath());
    }

    public void testBuildJMXURL() {
        String host = "localhost";
        String name = "name";
        int port = 123;
        ConnectionConfigurationImpl connectionConfiguration = new ConnectionConfigurationImpl(
                name, host, port);
        assertEquals("service:jmx:rmi:///jndi/rmi://" + host + ":" + port
                + "/jmxrmi", connectionConfiguration.buildJMXURL());
    }

    public void testBuildJMXURLCustomJMXPath() {
        String host = "localhost";
        String name = "name";
        int port = 123;
        String jmxPath = "jmxrmi/ipf";
        ConnectionConfigurationImpl connectionConfiguration = new ConnectionConfigurationImpl(
                name, host, port);

        connectionConfiguration.setJMXURLPath(jmxPath);
        assertEquals("service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/"
                + jmxPath, connectionConfiguration.buildJMXURL());
    }

    public void testBuildJMXURLCustomJMXPathAuthentication() {
        String host = "localhost";
        String name = "name";
        int port = 123;
        String jmxPath = "jmxrmi/ipf";
        ConnectionConfigurationImpl connectionConfiguration = new ConnectionConfigurationImpl(
                name, host, port, "userName", "password");

        connectionConfiguration.setJMXURLPath(jmxPath);
        assertEquals("service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/"
                + jmxPath, connectionConfiguration.buildJMXURL());
    }

    public void testConstructor2() {
        for (int t = 0; t < 20; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", t + 123, "user" + t,
                    "password" + t);
            assertEquals(connectionConfiguration.getHost(), "localhost");
            assertEquals(connectionConfiguration.getPort(), t + 123);
            assertEquals(connectionConfiguration.getName(), "connection" + t);
            assertEquals(connectionConfiguration.getAuthenticationCredentials()
                    .getUserName(), "user" + t);
            assertEquals(connectionConfiguration.getAuthenticationCredentials()
                    .getPassword(), "password" + t);
            assertTrue(connectionConfiguration.getAuthenticationCredentials()
                    .toStringArray() != null);
            String[] array = connectionConfiguration
                    .getAuthenticationCredentials().toStringArray();
            assertEquals(array[0], "user" + t);
            assertEquals(array[1], "password" + t);

        }

    }

    public void testAuthenticationCredentialsHashCodeAndEquals() {
        AuthenticationCredentials credenatials = new AuthenticationCredentials(
                "user", "password");
        AuthenticationCredentials credenatials2 = new AuthenticationCredentials(
                "user", "password");
        assertEquals(credenatials, credenatials2);
        assertEquals(credenatials2, credenatials);
        assertEquals(credenatials2.hashCode(), credenatials.hashCode());
    }

    public void testAuthenticationCredentialsNullUsername() {
        AuthenticationCredentials credenatials = new AuthenticationCredentials(
                "user", "password");
        credenatials.setUserName(null);
        assertFalse(credenatials.isValid());
    }

    public void testAuthenticationCredentialsNullPassword() {
        AuthenticationCredentials credenatials = new AuthenticationCredentials(
                "user", "password");
        credenatials.setPassword(null);
        assertFalse(credenatials.isValid());
    }

    public void testAuthenticationCredentialsEmptyUserNameAndPassword() {
        AuthenticationCredentials credenatials = new AuthenticationCredentials(
                "user", "password");
        credenatials.setUserName("");
        credenatials.setPassword("");
        assertFalse(credenatials.isValid());
    }

    public void testAuthenticationCredentialsValid() {
        AuthenticationCredentials credenatials = new AuthenticationCredentials(
                "user", "password");
        credenatials.setUserName("1");
        credenatials.setPassword("1");
        assertTrue(credenatials.isValid());

    }
}
