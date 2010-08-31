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

import java.io.File;

import junit.framework.TestCase;

import org.openehealth.ipf.tools.manager.connection.impl.ConnectionConfigurationRepositoryAbstractSerializer;
import org.openehealth.ipf.tools.manager.connection.mock.ConnectionConfigurationRepositorylObjectStreamSerializerMock;

/**
 * @author Mitko Kolev
 */
public class ConnectionConfiguartionRepositoryAbstractSerializerTest extends
        TestCase {

    ConnectionConfigurationRepositoryAbstractSerializer serializer;

    @Override
    public void setUp() {
        serializer = new ConnectionConfigurationRepositorylObjectStreamSerializerMock();
    }

    public void testConnectionRepositoryAbstractSerializerPathWithNoSeparatorAtTheEnd() {
        String folder = "usr" + File.separator + "home" + File.separator
                + "eclipse" + File.separator + "ipm";

        System.setProperty("osgi.install.area", folder);
        String globalPath = serializer.getSerializationGlobalPath();
        assertTrue(globalPath
                .contains(folder
                        + File.separator
                        + ConnectionConfigurationRepositoryAbstractSerializer.CONNECTIONS_LOCAL_FOLDER));
    }

    public void testConnectionRepositoryAbstractSerializerPathWithSeparatorAtTheEnd() {
        String folder = "usr" + File.separator + "home" + File.separator
                + "eclipse" + File.separator + "ipm" + File.separator;

        System.setProperty("osgi.install.area", folder);
        String globalPath = serializer.getSerializationGlobalPath();
        assertTrue(globalPath
                .contains(folder
                        + ConnectionConfigurationRepositoryAbstractSerializer.CONNECTIONS_LOCAL_FOLDER));
    }

    public void testConnectionRepositoryAbstractSerializerPathWithSeparatorAtTheEndWindows() {
        String folder = "file:/C:/eclipse/ipm";

        System.setProperty("osgi.install.area", folder);
        String globalPath = serializer.getSerializationGlobalPath();
        assertTrue(globalPath
                .contains("C:"
                        + File.separator
                        + "eclipse"
                        + File.separator
                        + "ipm"
                        + File.separator
                        + ConnectionConfigurationRepositoryAbstractSerializer.CONNECTIONS_LOCAL_FOLDER));
    }

    public void testConnectionRepositoryAbstractSerializerPathWithNoFile() {
        String folder = "C:/eclipse/ipm";

        System.setProperty("osgi.install.area", folder);
        String globalPath = serializer.getSerializationGlobalPath();
        assertTrue(globalPath
                .contains("C:"
                        + File.separator
                        + "eclipse"
                        + File.separator
                        + "ipm"
                        + File.separator
                        + ConnectionConfigurationRepositoryAbstractSerializer.CONNECTIONS_LOCAL_FOLDER));
    }

    public void testSeparatorExistsAtTheBeginning() {
        // Under Linux, the file separator at the beginning is a must
        String globalPath = serializer.getSerializationGlobalPath();
        assertTrue(globalPath.startsWith(File.separator));
    }

    public void testConnectionRepositoryAbstractSerializerPathWithFileSeparator() {
        String folder = "C:" + File.separator + "eclipse" + File.separator
                + "ipm";

        System.setProperty("osgi.install.area", folder);
        String globalPath = serializer.getSerializationGlobalPath();
        assertTrue(globalPath
                .contains("C:"
                        + File.separator
                        + "eclipse"
                        + File.separator
                        + "ipm"
                        + File.separator
                        + ConnectionConfigurationRepositoryAbstractSerializer.CONNECTIONS_LOCAL_FOLDER));
    }
}
