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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.openehealth.ipf.tools.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.tools.manager.connection.impl.ConnectionConfigurationRepository;

import static org.openehealth.ipf.tools.manager.connection.test.ConnectionUtils.createConnection;

public class ConnectionConfigurationRepositoryTest extends TestCase {

    private ConnectionConfigurationRepository connectionConfigurationRepository;

    @Override
    public void setUp() throws IOException {
        connectionConfigurationRepository = new ConnectionConfigurationRepository();
    }

    @Override
    public void tearDown() throws IOException {
    }

    public void testConstructor() {
        assertNotNull(connectionConfigurationRepository
                .getConnectionConfigurations());
        Set<IConnectionConfiguration> set = new TreeSet<IConnectionConfiguration>();
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = createConnection(t);
            set.add(connectionConfiguration);
        }

        connectionConfigurationRepository = new ConnectionConfigurationRepository(
                set);
        for (IConnectionConfiguration connectionConfiguration : set) {
            assertTrue(connectionConfigurationRepository
                    .getConnectionConfigurations().contains(
                            connectionConfiguration));
        }

        connectionConfigurationRepository = new ConnectionConfigurationRepository(
                null);
        assertNotNull(connectionConfigurationRepository
                .getConnectionConfigurations());
    }

    public void testAddConnection() {
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = createConnection(t);
            connectionConfigurationRepository
                    .addConnectionConfiguration(connectionConfiguration);
        }
        for (int t = 0; t < 10; t++) {
            assertTrue(connectionConfigurationRepository
                    .isConnectionNameInUse("connection" + t));
        }

        // add once again the same
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = createConnection(t);
            connectionConfigurationRepository
                    .addConnectionConfiguration(connectionConfiguration);
        }
        assertEquals(10, connectionConfigurationRepository
                .getConnectionConfigurations().size());

    }

    public void testAddNullConnection() {
        int sizeBefore = connectionConfigurationRepository
                .getConnectionConfigurations().size();
        connectionConfigurationRepository.addConnectionConfiguration(null);
        assertEquals(sizeBefore, connectionConfigurationRepository
                .getConnectionConfigurations().size());
    }

    public void testAddRemoveConnection() {
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = createConnection(t);
            connectionConfigurationRepository
                    .addConnectionConfiguration(connectionConfiguration);
        }
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = createConnection(t);
            connectionConfigurationRepository
                    .removeConnectionConfiguration(connectionConfiguration);
        }
        for (int t = 0; t < 10; t++) {
            assertFalse(connectionConfigurationRepository
                    .isConnectionNameInUse("connection" + t));
        }

        IConnectionConfiguration connectionConfigurationNonExisting = createConnection("connectionNonExisitng");
        connectionConfigurationRepository
                .removeConnectionConfiguration(connectionConfigurationNonExisting);

    }

    public void testGetConnections() {
        List<IConnectionConfiguration> added = new ArrayList<IConnectionConfiguration>();
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = createConnection(t);
            added.add(connectionConfiguration);
            connectionConfigurationRepository
                    .addConnectionConfiguration(connectionConfiguration);
        }
        List<IConnectionConfiguration> connectionConfigurations = connectionConfigurationRepository
                .getConnectionConfigurations();
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = connectionConfigurations
                    .get(t);
            assertEquals("connection" + t, connectionConfiguration.getName());
            // assert that we do not become a repository object.
            assertFalse(added == connectionConfiguration);
        }
    }

    public void testNullAware() {
        assertFalse(connectionConfigurationRepository
                .isConnectionConfigurationExisting(null));
        assertFalse(connectionConfigurationRepository
                .isConnectionNameInUse(null));
        assertFalse(connectionConfigurationRepository.isConnectionNameInUse(""));

        IConnectionConfiguration connectionConfigurationNonExisting = createConnection("connectionNonExisitng");
        assertFalse(connectionConfigurationRepository
                .isConnectionNameInUse(connectionConfigurationNonExisting
                        .getName()));

        assertFalse(connectionConfigurationRepository
                .isConnectionNameInUse(null));
        assertFalse(connectionConfigurationRepository.isConnectionNameInUse(""));

        assertFalse(connectionConfigurationRepository
                .isConnectionConfigurationExisting(connectionConfigurationNonExisting));
        assertNull(connectionConfigurationRepository
                .getConnectionConfigurationForName("non-existing-connection"));
        // test remove null
        connectionConfigurationRepository.removeConnectionConfiguration(null);

        assertNull(connectionConfigurationRepository
                .getConnectionConfigurationForName(null));
        assertNull(connectionConfigurationRepository
                .getConnectionConfigurationForName(""));

        connectionConfigurationRepository.addAll(null);
    }

    public void testGetConnectionForName() {
        List<IConnectionConfiguration> added = new ArrayList<IConnectionConfiguration>();
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = createConnection(t);
            added.add(connectionConfiguration);
            connectionConfigurationRepository
                    .addConnectionConfiguration(connectionConfiguration);
        }
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = added.get(t);
            IConnectionConfiguration repoConnectionConfiguration = connectionConfigurationRepository
                    .getConnectionConfigurationForName(connectionConfiguration
                            .getName());
            assertEquals(repoConnectionConfiguration, connectionConfiguration);
            assertFalse(repoConnectionConfiguration == connectionConfiguration);
        }

    }

    public void testRemoveAll() {
        List<IConnectionConfiguration> added = new ArrayList<IConnectionConfiguration>();
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = createConnection(t);
            added.add(connectionConfiguration);
            connectionConfigurationRepository
                    .addConnectionConfiguration(connectionConfiguration);
        }
        connectionConfigurationRepository.removeAll();
        assertEquals(0, connectionConfigurationRepository
                .getConnectionConfigurations().size());
    }

    public void testAddSet() {
        Set<IConnectionConfiguration> added = new TreeSet<IConnectionConfiguration>();
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = createConnection(t);
            added.add(connectionConfiguration);
        }
        connectionConfigurationRepository.addAll(added);
        List<IConnectionConfiguration> connectionConfigurations = connectionConfigurationRepository
                .getConnectionConfigurations();
        assertEquals(connectionConfigurations.size(), added.size());
        for (int t = 0; t < connectionConfigurations.size(); t++) {
            assertTrue(added.contains(connectionConfigurations.get(t)));
        }
    }

}
