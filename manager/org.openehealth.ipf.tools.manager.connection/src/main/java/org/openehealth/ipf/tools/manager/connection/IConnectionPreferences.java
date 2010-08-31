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
package org.openehealth.ipf.tools.manager.connection;

/**
 * Represents the preferences, relevant for connections
 * 
 * @author Mitko Kolev
 */
public interface IConnectionPreferences {

    public static final String TRUST_STORE_PATH_KEY = "truststore.path.key";

    public static final String TRUSTSTORE_PASSWORD_KEY = "truststore.password.key";

    /**
     * Read the trust store path, from the preferences. If no trust store path
     * is configured, the returned value is an empty <code>String</code>.
     * 
     * @return the trust store path, from the preferences.
     */
    String readTrustStorePath();

    /**
     * Read the trust store password, from the preferences. If no trust store
     * password is configured, the returned value is an empty
     * <code>String</code>.
     * 
     * @return the trust store password, from the preferences.
     */
    String readTrustStorePassword();

    /**
     * Stores the trust store path in the Eclipse preferences
     * 
     * @param path
     *            the trust store path to store
     */
    void storeTrustStorePath(String path);

    /**
     * Stores the trust store password for the trust store
     * {@link #readTrustStorePath()} in the Eclipse preferences
     * 
     * @param password
     *            the password for the {@link #readTrustStorePath()}
     */
    void storeTrustStorePassword(String password);

    /**
     * Updates the relevant system properties according to the preferences
     */
    void updateSystemProperties();
}
