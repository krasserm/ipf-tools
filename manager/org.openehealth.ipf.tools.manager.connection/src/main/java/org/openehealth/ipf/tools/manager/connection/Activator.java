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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Hashtable;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openehealth.ipf.tools.manager.connection.impl.ConnectionConfigurationRepository;
import org.openehealth.ipf.tools.manager.connection.impl.ConnectionConfigurationRepositoryAbstractSerializer;
import org.openehealth.ipf.tools.manager.connection.impl.ConnectionPreferencesImpl;
import org.openehealth.ipf.tools.manager.connection.impl.ConnectionRepositoryObjectStreamSerializer;
import org.openehealth.ipf.tools.manager.connection.impl.JMXConnectionManagerImpl;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Activator for the IPF Manager connection plug-in
 * 
 * @author Mitko Kolev
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = Activator.class.getPackage()
            .getName();

    private static JMXConnectionManagerImpl jMXConnectionManager;

    private static Activator plugin;

    private final IConnectionPreferences connectionPreferences = new ConnectionPreferencesImpl();
    private ConnectionConfigurationRepositoryAbstractSerializer serializer;

    private ServiceRegistration jMXConnectionManagerServiceRegistration;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;

        serializer = new ConnectionRepositoryObjectStreamSerializer();
        info("Starting IPF Manager Connection bundle");
        ConnectionConfigurationRepository repository = serializer.read(null);
        jMXConnectionManager = new JMXConnectionManagerImpl(repository);

        jMXConnectionManagerServiceRegistration = context.registerService(
                IJMXConnectionManager.class.getName(), jMXConnectionManager,
                new Hashtable<String, String>());
        info("IJMXConnectionManager service registered.");
        connectionPreferences.updateSystemProperties();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);

        jMXConnectionManager.deleteObservers();
        jMXConnectionManager.closeAllJMXConnectionsSilently();
        serializer.write(jMXConnectionManager.getRepository(), null);
        jMXConnectionManagerServiceRegistration.unregister();

        info("ConnectionManager service unregistered.");
        plugin = null;

    }

    /**
     * @return the shared Connection plug-in instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    public IJMXConnectionManager getJMXConnectionManager() {
        return jMXConnectionManager;
    }

    public IConnectionPreferences getConnectionPreferences() {
        return connectionPreferences;
    }

    /**
     * Used internally only in this plugin!
     * 
     * @return
     */
    public static JMXConnectionManagerImpl getConnectionManager() {
        return jMXConnectionManager;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     * 
     * @param path
     *            the plug-in relative path
     * @return the image descriptor, provided by the Connection plug-in
     */
    public ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);

    }

    /**
     * Returns the file where, the "last" state has been saved
     * 
     * @return
     */
    public static File getStateFile() {
        IPath path = getDefault().getStateLocation();
        if (path == null) {
            return null;
        }
        path = path.append("Connection.xml");
        return path.toFile();
    }

    /**
     * Saves plug-in appropriate data of the XMLMemento.
     * 
     * @param memento
     */
    public static void saveMementoToFile(XMLMemento memento) {
        File stateFile = getStateFile();
        if (stateFile != null) {
            try {
                FileOutputStream stream = new FileOutputStream(stateFile);
                OutputStreamWriter writer = new OutputStreamWriter(stream,
                        "UTF-8"); //$NON-NLS-1$
                memento.save(writer);
                writer.close();
            } catch (IOException ioe) {
                stateFile.delete();
            }
        }
    }

    public void info(String msg) {
        info(msg, null);
    }

    public void info(String msg, Throwable e) {
        getLog().log(new Status(Status.INFO, PLUGIN_ID, Status.OK, msg, e));
    }

    public void warn(String msg) {
        warn(msg, null);
    }

    public void warn(String msg, Throwable e) {
        getLog().log(new Status(Status.WARNING, PLUGIN_ID, Status.OK, msg, e));
    }

    public void error(String msg) {
        error(msg, null);
    }

    public void error(String msg, Throwable e) {
        getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.OK, msg, e));
    }

}
