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
package org.openehealth.ipf.tools.manager.connection.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationListener;
import javax.naming.AuthenticationException;

import org.openehealth.ipf.tools.manager.connection.Activator;
import org.openehealth.ipf.tools.manager.connection.ConnectionEvent;
import org.openehealth.ipf.tools.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.tools.manager.connection.IJMXConnectionManager;

/**
 * IConnection manager implementation. The implementation is synchronized,
 * because the JMX notifications are generally fired from an own thread.
 * 
 * @author Mitko Kolev
 */
public class JMXConnectionManagerImpl extends java.util.Observable implements
        IJMXConnectionManager {

    private final List<IConnectionConfiguration> connectedConnectionConfigurations;

    private final TreeMap<IConnectionConfiguration, IJMXConnectionAdapter> adapters;

    private final ConnectionConfigurationRepository repository;

    public JMXConnectionManagerImpl(ConnectionConfigurationRepository repository) {
        // make not copy at this time
        this.repository = repository;
        this.connectedConnectionConfigurations = new Vector<IConnectionConfiguration>();
        this.adapters = new TreeMap<IConnectionConfiguration, IJMXConnectionAdapter>();
        this.addJMXAdapters(repository.getConnectionConfigurations());
    }

    /**
     * Constructs a connection Manager with predefined list of connections.
     * 
     * @param connectionConfigurationsSorted
     */
    public JMXConnectionManagerImpl(
            Set<IConnectionConfiguration> connectionConfigurationsSorted) {
        this(new ConnectionConfigurationRepository());
        List<IConnectionConfiguration> connectionConfigurationsAsList = new ArrayList<IConnectionConfiguration>();
        connectionConfigurationsAsList.addAll(connectionConfigurationsSorted);
        addJMXAdapters(connectionConfigurationsAsList);
        repository.addAll(connectionConfigurationsSorted);
    }

    private void addJMXAdapters(
            List<IConnectionConfiguration> connectionConfigurationsSorted) {
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurationsSorted) {
            adapters.put(connectionConfiguration,
                    new JMXRMIConnectionAdapterImpl(connectionConfiguration));
        }
    }

    public synchronized List<IConnectionConfiguration> getConnectionConfigurations() {
        return repository.getConnectionConfigurations();
    }

    /**
     * Currently used only for mock purposes.
     * 
     * @param connectionConfiguration
     * @param adapter
     */
    public synchronized void addConnectionAdapter(
            IJMXConnectionAdapter connectionAdapter) {
        IConnectionConfiguration connectionConfiguration = connectionAdapter
                .getConnectionConfiguration();
        if (connectionConfiguration == null) {
            return;
        }
        repository.addConnectionConfiguration(connectionConfiguration);

        if (!adapters.containsKey(connectionConfiguration)) {
            // do not save the given connection, save a copy instead
            adapters.put((IConnectionConfiguration) connectionConfiguration
                    .clone(), connectionAdapter);
            Activator.getDefault().info(
                    "Connection " + connectionConfiguration
                            + " added to the connection repository.");

        }
        ConnectionEvent event = new ConnectionEvent(connectionConfiguration,
                ConnectionEvent.CONNECTION_ADDED);
        // marks that the repository has been changed, we will have to notify
        // the listeners then
        this.setChanged();
        Activator.getDefault()
                .info("Firing Connection CONNECTION_ADDED event.");
        this.notifyObservers(event);
    }

    public synchronized void addConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration != null) {
            IJMXConnectionAdapter connectionAdapter = new JMXRMIConnectionAdapterImpl(
                    connectionConfiguration);
            addConnectionAdapter(connectionAdapter);
        }
    }

    public synchronized void removeConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration != null) {
            if (!repository
                    .isConnectionConfigurationExisting(connectionConfiguration)) {
                return;
            }
            this.repository
                    .removeConnectionConfiguration(connectionConfiguration);
            if (connectedConnectionConfigurations
                    .contains(connectionConfiguration)) {
                connectedConnectionConfigurations
                        .remove(connectionConfiguration);
            }

            if (adapters.containsKey(connectionConfiguration)) {
                adapters.remove(connectionConfiguration);
            }

            IConnectionConfiguration connectionConfigurationClone = (IConnectionConfiguration) connectionConfiguration
                    .clone();

            if (isOpen(connectionConfiguration)) {
                // does not throw event
                closeJMXConnection(connectionConfigurationClone);
            }
            ConnectionEvent event = new ConnectionEvent(
                    connectionConfigurationClone,
                    ConnectionEvent.CONNECTION_REMOVED);
            // marks that the repository has been changed, we will have to
            // notify
            // the
            // listeners then
            this.setChanged();
            Activator.getDefault().info(
                    "Firing Connection CONNECTION_REMOVED event.");
            this.notifyObservers(event);
        }
    }

    private void connectionConfigurationStatusChanged(
            IConnectionConfiguration connectionConfiguration, boolean connected) {

        ConnectionEvent event;
        // do not send this connection. send only a clone so that the repository
        // does not expose its real data.
        IConnectionConfiguration connectionConfigurationClone = (IConnectionConfiguration) connectionConfiguration
                .clone();
        if (connected) {
            event = new ConnectionEvent(connectionConfigurationClone,
                    ConnectionEvent.JMX_CONNECTION_OPEN);
            // cleanup
            if (connectedConnectionConfigurations
                    .contains(connectionConfiguration)) {
                Activator
                        .getDefault()
                        .error(
                                "Connected Connections already contains the connection with status connected!");
            } else {
                this.connectedConnectionConfigurations
                        .add((IConnectionConfiguration) connectionConfiguration
                                .clone());
            }
        } else {
            event = new ConnectionEvent(connectionConfigurationClone,
                    ConnectionEvent.JMX_CONNECTION_CLOSED);
            // cleanup
            if (connectedConnectionConfigurations
                    .contains(connectionConfiguration)) {
                connectedConnectionConfigurations
                        .remove(connectionConfiguration);
            }
        }

        this.setChanged();
        this.notifyObservers(event);
    }

    public synchronized boolean isOpen(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration == null) {
            return false;
        }
        return connectedConnectionConfigurations
                .contains(connectionConfiguration);
    }

    public synchronized boolean isConnectionNameInUse(String connectionName) {
        if (connectionName == null || connectionName.length() == 0) {
            return false;
        }
        return repository.isConnectionNameInUse(connectionName);
    }

    public synchronized void openJMXConnection(
            IConnectionConfiguration connectionConfiguration)
            throws MalformedURLException, IOException, AuthenticationException {

        if (connectionConfiguration == null) {
            Activator.getDefault().error(
                    "openJMXConnection received a null connection");
            return;
        }

        try {
            IJMXConnectionAdapter adapter = adapters
                    .get(connectionConfiguration);
            if (adapter == null) {
                Activator.getDefault().error(
                        "The connection has no registered adapter");
                return;
            }
            adapter.openConnection();
            this.connectionConfigurationStatusChanged(connectionConfiguration,
                    true);
        } catch (AuthenticationException t) {
            throw t;
        } catch (MalformedURLException mfe) {
            throw mfe;
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable t) {
            Activator.getDefault().error("Cannot handle exception", t);
            Activator.getDefault().error(
                    "Closing connection " + connectionConfiguration);
            closeJMXConnection(connectionConfiguration);
        }
    }

    public synchronized void closeJMXConnection(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration == null) {
            Activator.getDefault().error(
                    "Received null connection on closeConnectionConfiguration");
            return;
        }
        // make the check in the method
        if (closeSilently(connectionConfiguration)) {
            // throw event
            this.connectionConfigurationStatusChanged(connectionConfiguration,
                    false);
        }

    }

    public synchronized void closeAllJMXConnectionsSilently() {
        // avoid concurrent modifications of the connected connections list
        for (int t = connectedConnectionConfigurations.size() - 1; t >= 0; t--) {
            this.closeSilently(connectedConnectionConfigurations.get(t));
        }
        // WARNING!!!
        // if the connection configuration has already been deleted from
        // the JMX notification forwarder thread, do not delete it.
        for (int t = connectedConnectionConfigurations.size() - 1; t >= 0; t--) {
            connectedConnectionConfigurations.remove(t);
        }
    }

    /**
     * Delegates the close to the connection adapter.
     * 
     * @param connectionConfigura
     *            tion
     * @return true if the operation succeeds, false otherwise
     */
    private boolean closeSilently(
            IConnectionConfiguration connectionConfiguration) {
        IJMXConnectionAdapter adapter = adapters.get(connectionConfiguration);
        if (adapter == null) {
            Activator.getDefault().error(
                    "The connection has no registered adapter");
            return false;
        }
        return adapter.closeConnection();

    }

    public synchronized <T, N extends NotificationListener> T loadProxyForClass(
            IConnectionConfiguration connectionConfiguration, String nameURI,
            Class<T> proxyClass, N notificationListener) throws IOException,
            MalformedObjectNameException, InstanceNotFoundException {

        if (connectionConfiguration == null) {
            Activator.getDefault().error(
                    "LoadProxyforClass received a null connection");
            return null;
        }
        IJMXConnectionAdapter adapter = adapters.get(connectionConfiguration);
        if (adapter == null) {
            throw new IOException(
                    "Cannot load proxy for connection configuration"
                            + connectionConfiguration);
        }
        return adapter.loadProxyForClass(nameURI, proxyClass,
                notificationListener);
    }

    public synchronized MBeanServerConnection getMBeanServerConnectionConfigurationAdapter(
            IConnectionConfiguration connectionConfiguration)
            throws IOException {
        if (connectionConfiguration == null) {
            String msg = "Cannot reach the MBean server of a null connection configuration";
            Activator.getDefault().error(msg);
            throw new IllegalArgumentException(msg);
        }
        IJMXConnectionAdapter connectionAdapter = adapters
                .get(connectionConfiguration);
        if (connectionAdapter == null) {
            String msg = "The connection adapter does not exist. The connection is probably removed!";
            throw new IOException(msg);
        }
        return connectionAdapter.getMBeanServerConnection();

    }

    public synchronized IConnectionConfiguration getConnectionConfigurationForName(
            String connectionName) {
        return repository.getConnectionConfigurationForName(connectionName);
    }

    public ConnectionConfigurationRepository getRepository() {
        return repository;
    }

}
