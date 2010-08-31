/*
 * Copyright 2010 the original author or authors.
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

import org.eclipse.jface.preference.IPreferenceStore;
import org.openehealth.ipf.tools.manager.connection.Activator;
import org.openehealth.ipf.tools.manager.connection.IConnectionPreferences;

/**
 * TODO: Try to set the properties in the TrustManagerFactory. In this case no
 * restart will be needed. Check out
 * java.sun.com/j2se/1.5.0/docs/guide/security/jsse/JSSERefGuide.html
 * 
 * @author Mitko Kolev
 * @see java.sun.com/j2se/1.5.0/docs/guide/security/jsse/JSSERefGuide.html
 */
public class ConnectionPreferencesImpl implements IConnectionPreferences {

    public String readTrustStorePassword() {
        IPreferenceStore preferences = Activator.getDefault()
                .getPreferenceStore();
        return preferences.getString(TRUSTSTORE_PASSWORD_KEY);

    }

    public String readTrustStorePath() {
        IPreferenceStore preferences = Activator.getDefault()
                .getPreferenceStore();
        return preferences.getString(TRUST_STORE_PATH_KEY);
    }

    public void storeTrustStorePassword(String password) {
        IPreferenceStore preferences = Activator.getDefault()
                .getPreferenceStore();

        preferences.setValue(IConnectionPreferences.TRUSTSTORE_PASSWORD_KEY,
                password);

    }

    public void storeTrustStorePath(String trustStorePath) {
        IPreferenceStore preferences = Activator.getDefault()
                .getPreferenceStore();
        preferences.setValue(IConnectionPreferences.TRUST_STORE_PATH_KEY,
                trustStorePath);
    }

    public void updateSystemProperties() {
        String trustStorePath = readTrustStorePath();
        String password = readTrustStorePassword();

        setOrClearIfEmpty("javax.net.ssl.trustStore", trustStorePath);
        setOrClearIfEmpty("javax.net.ssl.trustStorePassword", password);

        // do some logging
        Activator
                .getDefault()
                .info(
                        "javax.net.ssl.trustStore set to " + trustStorePath == null ? ""
                                : trustStorePath);
    }

    private void setOrClearIfEmpty(String property, String value) {
        if (value == null || "".equals(value)) {
            System.clearProperty(property);
            Activator.getDefault().info("Using no " + property);
        } else {
            System.setProperty(property, value);
            Activator.getDefault().info("Using " + property);
        }
    }
}
