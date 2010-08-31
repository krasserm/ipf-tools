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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Observer;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationListener;
import javax.naming.AuthenticationException;

/**
 * Acts as a JMX Facade, that transforms a <code>ConnectionConfiguration</code>
 * instances to JMX connections and manages a repository of connection
 * configurations. Should operate on a central repository for connection
 * configurations.
 * 
 * @see IConnectionConfiguration
 * 
 * @author Mitko Kolev
 */
public interface IJMXConnectionManager {

    /**
     * @return all available connection configurations, on which the manager
     *         operates.
     */
    public List<IConnectionConfiguration> getConnectionConfigurations();

    /**
     * Returns if the manager has opened a JMX connection with the given
     * <code>connectionConfiguration</code>.
     * 
     * @param connectionConfiguration
     *            A not-null connection configuration object
     * @return true if the is a open connection by this manager, false otherwise
     */
    public boolean isOpen(IConnectionConfiguration connectionConfiguration);

    /**
     * Every connection has its adapter. The method does not return null
     * 
     * @param connectionConfiguration
     *            the connection configuration for which the adapter is
     *            requested
     * @return the adapter of the connection
     */
    public MBeanServerConnection getMBeanServerConnectionConfigurationAdapter(
            IConnectionConfiguration connectionConfiguration)
            throws IOException;

    /**
     * @param connectionName
     *            the name to be checked
     * @return true if a connection configuration with name connectionName
     *         exists in the repository, false otherwise
     */
    public boolean isConnectionNameInUse(String connectionName);

    /**
     * Returns the connection for the given <code>connectionName</code>. A
     * connection name is considered unique in the repository.
     * 
     * @param connectionName
     * @return null if a connection with the given <code>connectionName</code>
     *         does not exist. If a connection configuration with the given name
     *         exists, returns that connection.
     */
    public IConnectionConfiguration getConnectionConfigurationForName(
            String connectionName);

    /**
     * Loads a MBean proxy for the
     * <code>proxyClass<code> and registers the JMX notification listener, if a not-null one is is given.
     * 
     * @param <T>
     * @param <N>
     * @param connectionConfiguration
     * @param nameURI
     * @param proxyClass
     * @param notificationListener
     *            a JXM notification listener to register
     * @return
     * @throws IOException
     * @throws MalformedObjectNameException
     * @throws InstanceNotFoundException
     */
    public <T, N extends NotificationListener> T loadProxyForClass(
            IConnectionConfiguration connectionConfiguration, String nameURI,
            Class<T> proxyClass, N notificationListener) throws IOException,
            MalformedObjectNameException, InstanceNotFoundException;

    /**
     * Adds a connection to be managed.
     * 
     * @param connectionConfiguration
     *            a not - null <code>ConnectionConfiguration</code> instance
     */

    public void addConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration);

    /**
     * Removes the given <code>connectionConfiguration</code> from the
     * repository of managed connection configurations. Also closes the JMX
     * connection, if such has been open.
     * 
     * @param connectionConfiguration
     *            a not - null <code>ConnectionConfiguration</code> instance
     */
    public void removeConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration);

    /**
     * Opens a JMX connection, with the given
     * <code>connectionConfiguration</code>. The given
     * <code>connectionConfiguration</code> must exist in the repository.
     * 
     * @param connectionConfiguration
     *            a not - null <code>ConnectionConfiguration</code> instance
     * @throws MalformedURLException
     * @throws IOException
     *             If the JMX connection can not be opened
     * @throws AuthenticationException
     *             If the JMX connection requires authentication, and the
     *             connection configuration does not use authentication.
     */
    public void openJMXConnection(
            IConnectionConfiguration connectionConfiguration)
            throws MalformedURLException, IOException, AuthenticationException;

    /**
     * Closes an already opened JMX connection, that has the given
     * <code>connectionConfiguration</code>
     * 
     * @param connectionConfiguration
     *            a not - null <code>ConnectionConfiguration</code> instance
     * @throws IOException
     *             Propagates the IOException, if any.
     */
    public void closeJMXConnection(
            IConnectionConfiguration connectionConfiguration);

    /**
     * The added Observer will be notified with a ConnectionEvent, if a JMX
     * connection is opened, closed, deleted or added.
     * 
     * @param observer
     *            an <code>Observer</code> to be notified for events.
     */
    public void addObserver(Observer observer);

    /**
     * Deletes the observer
     * 
     * @param observer
     *            an already added <code>Observer</code> with
     *            {@link #addObserver(Observer)}, that will be deleted.
     */
    public void deleteObserver(Observer observer);
}
