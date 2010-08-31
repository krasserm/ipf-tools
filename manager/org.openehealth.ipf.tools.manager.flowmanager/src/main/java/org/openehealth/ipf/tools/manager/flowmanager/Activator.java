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
package org.openehealth.ipf.tools.manager.flowmanager;

import java.util.Observable;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openehealth.ipf.tools.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.tools.manager.flowmanager.impl.FlowManagerApplicationControllerImpl;
import org.openehealth.ipf.tools.manager.flowmanager.impl.FlowManagerRepositorylImpl;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Activator for the Flow Manager plug-in
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

    private IFlowManagerApplicationController flowManagerApplicationController;

    /**
     * The constructor
     */
    public Activator() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(BundleContext context) throws Exception {
        info("Starting Flow Manager plug-in");
        plugin = this;
        super.start(context);

        jMXConnectionManagerServiceTracker = new ServiceTracker(context,
                IJMXConnectionManager.class.getName(), null);
        jMXConnectionManagerServiceTracker.open();

        jMXConnectionManager = (IJMXConnectionManager) jMXConnectionManagerServiceTracker
                .getService();

        if (jMXConnectionManager == null) {
            error("Cannot find a connection manager service!");
        }

        FlowManagerRepositorylImpl repository = new FlowManagerRepositorylImpl(
                jMXConnectionManager);

        flowManagerApplicationController = new FlowManagerApplicationControllerImpl(
                jMXConnectionManager, repository);

        // add the observer here and remove it on stop()
        jMXConnectionManager.addObserver(flowManagerApplicationController);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        info("Removing FlowManager Repository from ConnectionController's list of listener.");
        jMXConnectionManager.deleteObserver(flowManagerApplicationController);

        if (flowManagerApplicationController.getFlowManagerRepository() instanceof Observable) {
            Observable om = (Observable) flowManagerApplicationController
                    .getFlowManagerRepository();
            if (om.countObservers() > 0) {
                error("FlowManagerRepository: not all observers are unregistered!");
            }
        }
        jMXConnectionManagerServiceTracker.close();
        jMXConnectionManagerServiceTracker = null;
        jMXConnectionManager = null;

        plugin = null;

    }

    public IFlowManagerApplicationController getFlowManagerApplicationController() {
        return flowManagerApplicationController;
    }

    /**
     * @return the shared Flow manager plug-in instance
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
     * @return the image descriptor, provided by the Flow Manager plug-in
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
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
