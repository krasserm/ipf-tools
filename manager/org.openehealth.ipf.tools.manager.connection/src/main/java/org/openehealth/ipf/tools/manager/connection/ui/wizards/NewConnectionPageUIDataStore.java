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
package org.openehealth.ipf.tools.manager.connection.ui.wizards;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.openehealth.ipf.tools.manager.connection.Activator;
import org.openehealth.ipf.tools.manager.connection.ui.utils.encoding.Base64Utils;

/**
 * Stores the data from the UI elements in the ConnectionPage via
 * {@link ConnectionPageData} objects
 * 
 * @author Mitko Kolev
 */
public class NewConnectionPageUIDataStore {

    private final static String CONNECTIONS_MEMENTO__KEY = "connections";

    private final static String lastConnectionHostKey = "lastConnectionHost";

    private final static String lastConnectionPortKey = "lastConnectionPort";

    private final static String lastIsUsingAuthenticationKey = "lastUseAuthentication";

    private final static String lastConnectionNameKey = "lasConnectionName";

    private final static String lastUsernameKey = "lastConnection1";

    private final static String lastPasswordKey = "lastConnection2";

    private final static String lastUseCustomJXMSettingsKey = "lastUseCustomJXMSettings";

    private final static String lastURLPathKey = "lastURLPath";

    private final static String lastConnectorPortKey = "lastConnectorPort";

    private final static String lastOpenConnectionOnFinishKey = "lastOpenConnectionOnFinish";

    private ConnectionPageData readComponentState(IMemento memento) {
        ConnectionPageData data = new ConnectionPageData();
        data.setName(readMementoStringChecked(memento, lastConnectionNameKey));
        data.setHost(readMementoStringChecked(memento, lastConnectionHostKey));
        data.setPort(memento.getInteger(lastConnectionPortKey));
        data.setJMXPath(readMementoStringChecked(memento, lastURLPathKey));

        String usingAuthentication = readMementoStringChecked(memento,
                lastIsUsingAuthenticationKey);

        data.setUseAuthentication("true".equals(usingAuthentication));

        String userNameEncoded = readMementoStringChecked(memento,
                lastUsernameKey);
        String passwordEncoded = readMementoStringChecked(memento,
                lastPasswordKey);

        String lastConnectionUserNameDecoded = Base64Utils
                .decode(userNameEncoded);
        String lastConnectionPasswordDecoded = new String(Base64Utils
                .decode(passwordEncoded));
        data.setUsername(lastConnectionUserNameDecoded);
        data.setPassword(lastConnectionPasswordDecoded);

        String useCustomJMXSettings = readMementoStringChecked(memento,
                lastUseCustomJXMSettingsKey);
        data.setUseCustomJMXSettings("true".equals(useCustomJMXSettings));

        data.setJMXPath(readMementoStringChecked(memento, lastURLPathKey));
        data.setJMXConnectorPort(readMementoStringChecked(memento,
                lastConnectorPortKey));

        String openConnectionOnFinish = readMementoStringChecked(memento,
                lastOpenConnectionOnFinishKey);
        data.setOpenConnectionOnFinish("true".equals(openConnectionOnFinish));
        return data;

    }

    private String readMementoStringChecked(IMemento memento, String key) {
        String value = memento.getString(key);
        return value == null ? "" : value;

    }

    private void writeComponentState(IMemento memento, ConnectionPageData data) {
        memento.putString(lastConnectionNameKey, data.getName());
        memento.putString(lastConnectionHostKey, data.getHost());
        memento.putInteger(lastConnectionPortKey, data.getPort());
        memento.putInteger(lastConnectionPortKey, data.getPort());
        memento.putString(lastIsUsingAuthenticationKey, data
                .isUseAuthentication() ? "true" : "false");
        memento.putString(lastUsernameKey, Base64Utils.encode(data
                .getUsername()));
        memento.putString(lastPasswordKey, Base64Utils.encode(data
                .getPassword()));
        memento.putString(lastUseCustomJXMSettingsKey, data
                .isUseCustomJMXSettings() ? "true" : "false");
        memento.putString(lastURLPathKey, data.getJMXPath());
        memento.putString(lastConnectorPortKey, data.getJMXConnectorPort());
        memento.putString(lastOpenConnectionOnFinishKey, data
                .isOpenConnectionOnFinish() ? "true" : "false");
    }

    /**
     * Saves the connection state data using Eclipse mechanisms
     * 
     * @param data
     */
    public void saveState(ConnectionPageData data) {
        XMLMemento memento = XMLMemento
                .createWriteRoot(CONNECTIONS_MEMENTO__KEY);
        IMemento child = memento.createChild(CONNECTIONS_MEMENTO__KEY);
        writeComponentState(child, data);
        Activator.saveMementoToFile(memento);
    }

    /**
     * @return the POJO with the connection page state data
     */
    public ConnectionPageData readState() {
        try {
            XMLMemento memento = XMLMemento.createReadRoot(new BufferedReader(
                    new FileReader(Activator.getStateFile())));
            IMemento thisMemento = memento.getChild(CONNECTIONS_MEMENTO__KEY);
            if (thisMemento != null) {
                return readComponentState(thisMemento);
            }
        } catch (WorkbenchException we) {
            // ignore it
        } catch (FileNotFoundException fnfe) {
            // ignore it, it could be the first time
        }
        // we have not saved the data at all, this is the first time
        return new ConnectionPageData();
    }

}
