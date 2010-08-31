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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.openehealth.ipf.tools.ide.wizard.plugin.PluginFieldData;

/**
 * 
 * Second page of the wizard. Controls the usage of specific IPF modules.  
 * 
 * @author Boris Stanojevic 
 *
 */
public class NewIpfProjectWizardPageTwo extends WizardPage {

	private PluginFieldData fData;
	
	private Button useExtension;
	private Button useJms;
	private Button useFlowManagement;
	private Button useHL7;
	
	private static String DESCRIPTION = Messages.getString("IpfNewProjectWizardPageTwo.title"); //$NON-NLS-1$
	private static String TITLE = Messages.getString("IpfNewProjectWizardPageTwo.description"); //$NON-NLS-1$
	
	public NewIpfProjectWizardPageTwo(String pageName,
				PluginFieldData fieldData) {
		super("ipfApplicationWizard"); //$NON-NLS-1$
		setTitle(TITLE);
		setDescription(DESCRIPTION);
		this.fData = fieldData;
	}

   /**
     * creates a group of Controls needed for optional usage of
     * IPF specific modules
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(Composite)
     */	
	@Override
	public void createControl(Composite parent) {	
	    Composite container = new Composite(parent, SWT.NULL);
	    GridLayout layout = new GridLayout();
	    layout.verticalSpacing = 10;
	    container.setLayout(layout);
	    
	    createIpfPropertiesGroup(container);
	}
	
	private void createIpfPropertiesGroup(Composite control){
	    Group pluginInfo = new Group(control, SWT.NONE);
	    pluginInfo.setText(Messages.getString("IpfNewProjectWizardPageOne.properties")); //$NON-NLS-1$
	        
	    GridLayout gl = new GridLayout();
	    gl.numColumns = 1;
	    pluginInfo.setLayout(gl);
	    pluginInfo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        
	    GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
	    gridData.horizontalSpan = 2;
	    control.setLayoutData(gridData);
	    pluginInfo.setLayoutData(gridData);
	        
	    useExtension = new Button(pluginInfo, SWT.CHECK | SWT.RIGHT);
	    useExtension.setSelection(true);
	    useExtension.setText(Messages.getString("IpfNewProjectWizardPageTwo.useExtension"));

	    useJms = new Button(pluginInfo, SWT.CHECK | SWT.RIGHT);
	    useJms.setText(Messages.getString("IpfNewProjectWizardPageTwo.useJms"));

	    useFlowManagement = new Button(pluginInfo, SWT.CHECK | SWT.RIGHT);
	    useFlowManagement.setText(Messages.getString("IpfNewProjectWizardPageTwo.useFlowManagement"));

	    useHL7 = new Button(pluginInfo, SWT.CHECK | SWT.RIGHT);
	    useHL7.setText(Messages.getString("IpfNewProjectWizardPageTwo.useHL7Modules"));
	    
	    setControl(control);
        Dialog.applyDialogFont(control);
	}
	
	/**
	 * Picks up the user input values and sets the corresponding
	 * Data Object attribute values.
	 */
	public void updateData(){
	    fData.setUseFlowManagement(useFlowManagement.getSelection());
	    fData.setUseHL7Modules(useHL7.getSelection());
	    fData.setUseJms(useJms.getSelection());
	    fData.setUseExtension(useExtension.getSelection());
	    fData.setExtensionPackage(fData.getProjectName()
	    					.replaceAll("\\.", "\\/") + "/extension");
	    fData.setRouteBuilderPackage(fData.getProjectName()
				.replaceAll("\\.", "\\/") + "/route");
	    
	    String temp = fData.getProjectName()
	    	.substring(fData.getProjectName().lastIndexOf(".")+1);
	    temp = temp.substring(0,1).toUpperCase() + temp.substring(1);
	    String extensionName = temp + "Extension.groovy";
	    String routeBuilderName = temp + "RouteBuilder.groovy";
	    fData.setExtensionName(extensionName);
	    fData.setRouteBuilderName(routeBuilderName);
	    
	}
	
	
}