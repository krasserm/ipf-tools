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
package org.openehealth.ipf.tools.manager.jmxexplorer;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openehealth.ipf.tools.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.tools.manager.jmxexplorer.impl.JMXExplorerMediatorImpl;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Activator for the JMX Explorer functionality.
 * 
 * @author Mitko Kolev
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = Activator.class.getPackage()
            .getName();

    // The shared instance
    private static Activator plugin;

    private IJMXConnectionManager jMXConnectionManager;

    private ServiceTracker jMXConnectionManagerServiceTracker;

    private JMXExplorerMediatorImpl jmxExplorerMediator;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        jMXConnectionManagerServiceTracker = new ServiceTracker(context,
                IJMXConnectionManager.class.getName(), null);
        jMXConnectionManagerServiceTracker.open();

        jMXConnectionManager = (IJMXConnectionManager) jMXConnectionManagerServiceTracker
                .getService();

        if (jMXConnectionManager == null) {
            error("Cannot find a connection manager service!");
        }
        jmxExplorerMediator = new JMXExplorerMediatorImpl(jMXConnectionManager);
        jMXConnectionManager.addObserver(jmxExplorerMediator);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        jMXConnectionManager.deleteObserver(jmxExplorerMediator);
        jmxExplorerMediator.deleteObservers();
        // close the connection service tracker
        jMXConnectionManagerServiceTracker.close();
        jMXConnectionManagerServiceTracker = null;
        jMXConnectionManager = null;
        plugin = null;
    }

    /**
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     * 
     * @param path
     *            the plug-in relative path
     * @return the image descriptor, provided by the JMX explorer plug-in
     */
    public ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public IJMXExplorerMediator getJMXExplorerMediator() {
        return jmxExplorerMediator;
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
