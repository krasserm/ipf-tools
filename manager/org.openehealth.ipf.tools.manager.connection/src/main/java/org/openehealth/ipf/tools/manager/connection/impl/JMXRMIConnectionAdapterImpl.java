/*
x * Copyright 2008 the original author or authors.
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
import java.util.HashMap;

import javax.management.InstanceNotFoundException;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectionNotification;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.AuthenticationException;

import org.openehealth.ipf.tools.manager.connection.Activator;
import org.openehealth.ipf.tools.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.tools.manager.connection.IJMXConnectionManager;

/**
 * Facade for opening and closing a connection. Creates RMI connection to a
 * contained connection configuration. The class holds state.
 * 
 * @see IJMXConnectionAdapter
 * @author Mitko Kolev
 */
public class JMXRMIConnectionAdapterImpl implements IJMXConnectionAdapter,
        NotificationListener {

    private final static long serialVersionUID = 5936363367842548992L;

    private transient JMXConnector connector;

    /**
     * The connection associated with this adapter.
     */
    private final IConnectionConfiguration connectionConfiguration;

    /**
     * IMPORTANT: sometimes the notification listener notifies infinitely! This
     * flag is updated when the connection is opened or closed.
     */
    private volatile boolean connectionAlreadyClosed;

    public JMXRMIConnectionAdapterImpl(
            IConnectionConfiguration connectionConfigurationContext) {
        if (connectionConfigurationContext == null)
            throw new IllegalArgumentException("Connection can not be null!");
        this.connectionConfiguration = connectionConfigurationContext;
        this.connectionAlreadyClosed = false;

    }

    /**
     * Establishes a remote connection to a JMX server.
     * 
     * @throws MalformedURLException
     *             if the host is wrong
     * @throws IOException
     *             if the connection can not be established.
     * @throws AuthenticationException
     *             if the JMX server requires authentication.
     */
    public synchronized void openConnection() throws MalformedURLException,
            IOException, AuthenticationException {

        this.connector = createJMXConnector(connectionConfiguration);

        try {
            Activator.getDefault().info(
                    "Connecting to " + connectionConfiguration);
            connector.connect();
            addConnectionNotificationListener();
            connectionAlreadyClosed = false;
        } catch (IllegalArgumentException ile) {
            Activator.getDefault().info("Authentication failed", ile);
            throw new AuthenticationException();
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable e) {
            Activator.getDefault().error(
                    "Unknown exception while opening connection ", e);
            throw new IOException(e.getCause());
        }
    }

    public synchronized boolean closeConnection() {
        if (connector == null) {
            Activator.getDefault().error(
                    "Cannot close a connection wiht a null connector");
            return false;
        }
        try {
            connector.close();
            connector = null;
        } catch (Throwable ioe) {
            Activator.getDefault().error("Cannot close the connection", ioe);
        }
        // everything necessary has been done.
        return true;
    }

    public JMXConnector createJMXConnector(IConnectionConfiguration connection)
            throws MalformedURLException, IOException {
        String urlString = connection.buildJMXURL();
        Activator.getDefault().info("Using connection url : " + urlString);
        JMXServiceURL url = new JMXServiceURL(urlString);
        HashMap<String, Object> env = new HashMap<String, Object>();
        boolean isUsiingAuthentication = connection
                .getAuthenticationCredentials().isValid();
        if (isUsiingAuthentication) {
            // put the username and password credentials
            env.put(JMXConnector.CREDENTIALS, connection
                    .getAuthenticationCredentials().toStringArray());
            Activator.getDefault().info(
                    "Creating JMX connection with credentials.");
        }
        if (isUsiingAuthentication) {
            Activator.getDefault().info(
                    "Creating JMX connection with authentification.");
            return JMXConnectorFactory.newJMXConnector(url, env);
        } else {
            Activator.getDefault().info(
                    "Creating JMX connection with NO authentification.");
            return JMXConnectorFactory.newJMXConnector(url, null);
        }
    }

    public <T, N extends NotificationListener> T loadProxyForClass(
            String nameURI, Class<T> proxyClass, N notificationListener)
            throws IOException, MalformedObjectNameException,
            InstanceNotFoundException {
        if (connector == null) {
            return null;
        }

        Activator.getDefault().info(
                "Requesting MBean for connection " + connectionConfiguration);
        if (nameURI == null || nameURI.length() == 0) {
            Activator.getDefault().error(
                    "Requesting MBean, but the nameURI is null!");
            return null;
        }
        T proxyInstance = null;
        // cache the listener and use it for later use, will be used when the
        // connection is closed
        ObjectName objectName = new ObjectName(nameURI);

        MBeanServerConnection mbeanConnection = connector
                .getMBeanServerConnection();
        if (notificationListener != null) {
            mbeanConnection.addNotificationListener(objectName,
                    notificationListener, null, null);
            connector.addConnectionNotificationListener(notificationListener,
                    null, null);
        }
        proxyInstance = JMX.newMBeanProxy(mbeanConnection, objectName,
                proxyClass, true);
        return proxyInstance;
    }

    public MBeanServerConnection getMBeanServerConnection() throws IOException {
        if (connector == null) {
            throw new IOException("The connection is not connected");
        } else {
            return connector.getMBeanServerConnection();
        }
    }

    public synchronized void handleNotification(Notification notification,
            Object handback) {
        // the connection has gone offline. remove it from the repository
        if (notification instanceof JMXConnectionNotification) {
            if (notification.getType().equals(JMXConnectionNotification.FAILED)) {
                Activator
                        .getDefault()
                        .info(
                                "Receive JMX notification JMXConnectionNotification.FAILED!");
            } else if (notification.getType().equals(
                    JMXConnectionNotification.CLOSED)) {
                Activator
                        .getDefault()
                        .info(
                                "Receive JMX notification JMXConnectionNotification.CLOSED!");
            } else if (notification.getType().equals(
                    JMXConnectionNotification.NOTIFS_LOST)) {
                Activator
                        .getDefault()
                        .info(
                                "Receive JMX notification JMXConnectionNotification.NOTIFS_LOST!");
            } else if (notification.getType().equals(
                    JMXConnectionNotification.OPENED)) {
                Activator
                        .getDefault()
                        .info(
                                "Receive JMX notification JMXConnectionNotification.OPENED!");
                return;
            }
            if (connectionAlreadyClosed == false) {
                connectionAlreadyClosed = true;
                // provide the connection manager with notification
                getJMXConnectionManager().closeJMXConnection(
                        connectionConfiguration);

            }
        }
    }

    private void addConnectionNotificationListener() throws IOException,
            RuntimeException, MalformedObjectNameException,
            InstanceNotFoundException {
        ObjectName runtimeObjectName = new ObjectName("java.lang:type=Memory");
        MBeanServerConnection mbeanConnection = connector
                .getMBeanServerConnection();
        mbeanConnection.addNotificationListener(runtimeObjectName, this, null,
                null);
        connector.addConnectionNotificationListener(this, null, null);
    }

    public IConnectionConfiguration getConnectionConfiguration() {
        return connectionConfiguration;
    }

    public JMXConnector getConnector() {
        return connector;
    }

    public IJMXConnectionManager getJMXConnectionManager() {
        return Activator.getConnectionManager();
    }
}
