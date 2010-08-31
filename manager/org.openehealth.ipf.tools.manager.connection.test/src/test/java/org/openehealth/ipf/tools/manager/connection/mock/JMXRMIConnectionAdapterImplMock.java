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
package org.openehealth.ipf.tools.manager.connection.mock;

import static org.openehealth.ipf.tools.manager.connection.test.ConnectionUtils.getPlatformMbeanServerJMXURL;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.openehealth.ipf.tools.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.tools.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.tools.manager.connection.impl.ConnectionConfigurationRepository;
import org.openehealth.ipf.tools.manager.connection.impl.JMXConnectionManagerImpl;
import org.openehealth.ipf.tools.manager.connection.impl.JMXRMIConnectionAdapterImpl;
/**
 * @see JMXRmiConnectionAdapterImpl
 * 
 * @author Mitko Kolev
 */
public class JMXRMIConnectionAdapterImplMock extends
        JMXRMIConnectionAdapterImpl {

    private static final long serialVersionUID = 4971934396142861172L;

    public JMXRMIConnectionAdapterImplMock(
            IConnectionConfiguration connectionConfiguration) {
        super(connectionConfiguration);
    }

    @Override
    public IJMXConnectionManager getJMXConnectionManager() {
        IJMXConnectionManager manager = new JMXConnectionManagerImpl(
                new ConnectionConfigurationRepository()) {
            @Override
            public void closeJMXConnection(
                    IConnectionConfiguration connectionConfiguration) {
                JMXRMIConnectionAdapterImplMock.this.closeConnection();
            }
        };
        manager.addConnectionConfiguration(getConnectionConfiguration());
        return manager;
    }
    
    
    /** 
     * Returns the JMX connector of the virtual machine
     */
    @Override
    public JMXConnector createJMXConnector(IConnectionConfiguration connection) throws MalformedURLException,
            IOException {
        return JMXConnectorFactory.newJMXConnector(new JMXServiceURL(getPlatformMbeanServerJMXURL()), null);
    }

}
