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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.openehealth.ipf.tools.manager.connection.IConnectionConfiguration;

/**
 * Manages a repository of connection configurations, that are used as
 * definitions for JMX connections. Every connection in the repository must have
 * a repository-unique name.
 * 
 * @author Mitko Kolev
 */
public class ConnectionConfigurationRepository {

    private final List<IConnectionConfiguration> connectionConfigurations;

    public ConnectionConfigurationRepository() {
        this.connectionConfigurations = new ArrayList<IConnectionConfiguration>();
    }

    /**
     * Initializes the repository with some connections.
     * 
     * @param connectionConfigurationsParams
     */
    public ConnectionConfigurationRepository(
            Set<IConnectionConfiguration> connectionConfigurationsParams) {
        this();
        if (connectionConfigurationsParams != null) {
            for (IConnectionConfiguration connectionConfiguration : connectionConfigurationsParams) {
                connectionConfigurations
                        .add((IConnectionConfiguration) connectionConfiguration
                                .clone());
            }
        }
    }

    /**
     * Returns all available connections in the connection repository.
     * 
     * @return
     */
    public List<IConnectionConfiguration> getConnectionConfigurations() {
        List<IConnectionConfiguration> connectionConfigurationClones = new ArrayList<IConnectionConfiguration>();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            connectionConfigurationClones
                    .add((IConnectionConfiguration) connectionConfiguration
                            .clone());
        }
        return Collections.unmodifiableList(connectionConfigurationClones);
    }

    /**
     * Returns if a connection with name connectionName exists in the
     * repository.
     * 
     * @param connectionName
     * @return
     */
    public boolean isConnectionNameInUse(String connectionName) {
        // do not allow connections with empty names
        if (connectionName == null || connectionName.length() == 0) {
            return false;
        }
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            if (connectionConfiguration.getName().equals(connectionName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the connection configuration with the given name from the repository
     * 
     * @param connectionName
     *            The name of the connection configuration to get
     * @return null if no connection configurations with the given name exist
     */
    public IConnectionConfiguration getConnectionConfigurationForName(
            String connectionName) {
        if (connectionName == null || connectionName.length() == 0) {
            return null;
        }
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            if (connectionConfiguration.getName().equals(connectionName)) {
                return (IConnectionConfiguration) connectionConfiguration
                        .clone();
            }
        }
        //TODO throw an exception here
        return null;
    }

    /**
     * Adds a connection with a default IJMXConnectionAdapter implementation to
     * the repository.
     * 
     * @param observer
     */

    public void addConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration == null) {
            return;
        }
        if (connectionConfigurations.contains(connectionConfiguration)) {
            connectionConfigurations.remove(connectionConfiguration);
        }
        connectionConfigurations.add(connectionConfiguration);
    }

    public boolean isConnectionConfigurationExisting(
            IConnectionConfiguration connectionConfiguration) {
        return connectionConfigurations.contains(connectionConfiguration);
    }

    /**
     * Delegates the method to the repository. Notifies the listeners.
     * IOException is thrown, because the close method is called.
     * 
     * @param observer
     */
    public void removeConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration) {

        if (connectionConfigurations.contains(connectionConfiguration)) {
            connectionConfigurations.remove(connectionConfiguration);
        }
    }

    /**
     * Adss the connection set to the repository.
     * 
     * @param connectionConfigurations
     */
    public void addAll(Set<IConnectionConfiguration> connectionConfigurations) {
        if (connectionConfigurations != null) {
            for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
                this.addConnectionConfiguration(connectionConfiguration);
            }
        }
    }

    /**
     * Adss the connection set to the repository.
     * 
     * @param connectionConfigurations
     */
    public void removeAll() {
        this.connectionConfigurations.clear();
    }
}
