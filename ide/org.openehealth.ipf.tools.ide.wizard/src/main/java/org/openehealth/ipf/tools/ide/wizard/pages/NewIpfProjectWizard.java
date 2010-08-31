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
package org.openehealth.ipf.tools.ide.wizard.pages;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import org.openehealth.ipf.tools.ide.wizard.plugin.PluginFieldData;
/**
 * The main Wizard object. It controls the order of showing the pages
 * and performs the Finish operation.  
 * 
 * @author Boris Stanojevic
 *
 */
public class NewIpfProjectWizard extends Wizard implements INewWizard {
    
    /**
     * First wizard page
     */
	private NewIpfProjectWizardPageOne pageOne;
	/**
	 * Second wizard page
	 */
	private NewIpfProjectWizardPageTwo pageTwo;
	/**
	 * Reflects the content and structure of the extension section
	 * within the declaring plugin.xml file
	 */
    private IConfigurationElement config;
    /**
     * An instance single workspace Project
     */
	private IProject project;
	/**
	 * Data Object which holds all data relevant for the project creation.
	 */
	private PluginFieldData fPluginData;


	/**
	 * Constructor for IpfWizard. Initiates the Data Object
	 * which holds all data relevant for the project creation. 
	 */
	public NewIpfProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
		ImageDescriptor image = org.eclipse.ui.plugin.AbstractUIPlugin
		.imageDescriptorFromPlugin("org.openehealth.ipf.tools.ide.wizard",
								   "images/oehf.png");
		setDefaultPageImageDescriptor(image);
		fPluginData = new PluginFieldData();
	}
	
	/**
	 * Adds required Wizard pages
	 */
	public void addPages() {
		pageOne = new NewIpfProjectWizardPageOne("IPF Project", fPluginData);
		addPage(pageOne);
	    pageTwo = new NewIpfProjectWizardPageTwo("IPF Project", fPluginData);
	    addPage(pageTwo);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. It creates an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		pageOne.updateData();
		pageTwo.updateData();
		
        if (project != null) {
            return true; 
        }

        final IProject projectHandle = pageOne.getProjectHandle();

        URI projectURI = (!pageOne.useDefaults()) ? pageOne.getLocationURI() : null;
   
        IWorkspace workspace = ResourcesPlugin.getWorkspace();

        final IProjectDescription desc = workspace
                .newProjectDescription(projectHandle.getName());

        desc.setLocationURI(projectURI);
        
        try {
            getContainer().run(true, true, new NewIpfProjectOperation(desc, projectHandle, fPluginData));
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error", realException
                    .getMessage());
            return false;
        }

        project = projectHandle;
        if (project == null) {
            return false;
        }

        BasicNewProjectResourceWizard.updatePerspective(config);
        return true;
	}

	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {		
	}

}