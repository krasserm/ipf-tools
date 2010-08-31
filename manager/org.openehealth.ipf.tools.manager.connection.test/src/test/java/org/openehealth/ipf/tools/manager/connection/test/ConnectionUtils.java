/*
 * Copyright 2009 the original author or authors.
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

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.openehealth.ipf.tools.manager.connection.ConnectionConfigurationImpl;

/**
 * @author Mitko Kolev
 */
public class ConnectionUtils {
    /**
     * Creates a test connection configuration with
     * <code>connectionNumber</code> at the end of its name
     * 
     * @param port
     *            the port of the connection. The port will also be appended at
     *            the end of the name
     * @return a IConnectionConfiguration instance
     */
    public static ConnectionConfigurationImpl createConnection(int port) {
        return new ConnectionConfigurationImpl("connection" + port,
                "localhost", port);
    }

    /**
     * Create a test connection configuration with the given <code>name</code>
     * 
     * @param name
     *            the name of the connection configuration
     * @return a IConnectionConfiguration instance
     */
    public static ConnectionConfigurationImpl createConnection(String name) {
        return new ConnectionConfigurationImpl(name, "localhost", 123);
    }
    
    /** 
     * @return the JMX URL of the current platform MBean server of the current java process 
     */
    public static String getPlatformMbeanServerJMXURL() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://");
            JMXConnectorServer cs = JMXConnectorServerFactory
                    .newJMXConnectorServer(url, null, mbs);
            cs.start();
            return cs.getAddress().toString();
        } catch (Exception ioe) {
            return "URL not available";
        }
    }
}
