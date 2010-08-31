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
package org.openehealth.ipf.tools.ide.launch.delegate;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import org.eclipse.pde.ui.launcher.BundlesTab;
import org.eclipse.pde.ui.launcher.OSGiSettingsTab;
import org.eclipse.pde.ui.launcher.TracingTab;
import org.openehealth.ipf.tools.ide.launch.osgi.IpfLaunchActivator;

/**
 * 
 * Creates the required Tabs for this Tab group and sets the required 
 * initial values
 * 
 * @author Boris Stanojevic
 *
 */
public class IpfTabGroup extends AbstractLaunchConfigurationTabGroup  {

    /**
     * creates the required Tabs for this Tab group
     */
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {		
		
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
			 new BundlesTab(),			 
			 new JavaArgumentsTab(){				 
				@Override
				public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
					super.setDefaults(configuration);
					configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, 
						IpfLaunchActivator.getDefault().getProperties().getProperty("launch.progargs"));
					configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
						IpfLaunchActivator.getDefault().getProperties().getProperty("launch.vmargs"));
					configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY,
						IpfLaunchActivator.getDefault().getProperties().getProperty("launch.working.dir"));
				}				 
			 },
			 new OSGiSettingsTab(),
			 new TracingTab(),
			 new EnvironmentTab(),
			 new CommonTab()
		};
		setTabs(tabs);
	}
	
	/**
	 * set the initial values of the selected bundles for the BundlesTab
	 */
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		String bundles = IpfLaunchActivator.getDefault().getProperties().getProperty("launch.bundles");
		String autostart = IpfLaunchActivator.getDefault().getProperties().getProperty("launch.default.autostart");
		configuration.setAttribute("target_bundles", bundles);
		configuration.setAttribute("default_auto_start", Boolean.getBoolean(autostart));
		super.setDefaults(configuration);
	}
	
}