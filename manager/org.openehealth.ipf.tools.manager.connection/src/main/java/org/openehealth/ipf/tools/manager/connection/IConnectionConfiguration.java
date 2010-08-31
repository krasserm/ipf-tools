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

import java.io.Serializable;

/**
 * Interface representing a connection resource. In general, the connection is
 * not limited to a JMX connection.
 * 
 * @see IJMXConnectionManager
 * 
 * @author Mitko Kolev
 */
public interface IConnectionConfiguration extends Serializable, Cloneable,
        Comparable<IConnectionConfiguration> {

    /**
     * @return the authentication credentials of the connection configuration.
     *         Returns always a not-null object. If no credentials are used, the
     *         {@link AuthenticationCredentials#isValid()} method will return
     *         false
     */
    public AuthenticationCredentials getAuthenticationCredentials();

    /**
     * @return the host of the management target
     */
    public String getHost();

    /**
     * @return the descriptive name for the connection configuration. Returns
     *         always a not-null String
     */
    public String getName();

    /**
     * @return the JMX JNDI port of the management target
     */
    public int getPort();

    /**
     * @return the path of the JXM URL
     */
    public String getJMXURLPath();

    /**
     * The JMX Connector port
     * 
     * @return 0, if the port is not used, otherwise the port number
     */
    public int getJMXRMIConnectorPort();

    /**
     * @return a URL String with the data from current connection configuration
     */
    public String buildJMXURL();

    /**
     * @return a deep copy of this object.
     */
    public Object clone();
}
