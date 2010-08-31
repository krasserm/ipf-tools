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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.openehealth.ipf.tools.ide.wizard.plugin.PluginFieldData;
import org.openehealth.ipf.tools.ide.wizard.util.PluginUtils;

/**
 * First page of the wizard. Controls the input values used to
 * create a new Ipf Project. Additionally validates and
 * updates provided user data
 * 
 * @author Boris Stanojevic 
 *
 */
public class NewIpfProjectWizardPageOne extends WizardNewProjectCreationPage {

	private PluginFieldData fData;
	
	private Text pluginId;
	private Text pluginVersion;
	private Text pluginName;
	private Text pluginProvider;
	
	private static String DESCRIPTION = Messages.getString("IpfNewProjectWizardPageOne.title"); //$NON-NLS-1$
	private static String TITLE = Messages.getString("IpfNewProjectWizardPageOne.description"); //$NON-NLS-1$
	
	
	public NewIpfProjectWizardPageOne(String pageName, PluginFieldData fieldData) {
		super("ipfApplicationWizard"); //$NON-NLS-1$
		setTitle(TITLE);
		setDescription(DESCRIPTION);
		this.fData = fieldData;
	}
	
	protected ModifyListener propertiesListener = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
            boolean valid = validatePage();
            setPageComplete(valid);
		}
	};
	
	protected FocusListener selectionListener = new FocusListener() {
		@Override
		public void focusGained(FocusEvent e) {
			setMessage(Messages.getString("IpfNewProjectWizardPageOne.insertProperties"));
			suggestPluginProperties();
		}

		@Override
		public void focusLost(FocusEvent e) {		
		}
	};

   /**
     * creates a group of Controls needed to provide additional
     * new Plug-in informations
     * 
     * @see org.eclipse.ui.dialogs.WizardNewProjectCreationPage#createControl(Composite)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(Composite)
     */ 
	@Override
	public void createControl(Composite parent) {	
		super.createControl(parent);
		
		Composite control = (Composite)getControl();
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 10;
		control.setLayout(layout);

		createPluginPropertiesGroup(control);
	}
	
	private void createPluginPropertiesGroup(Composite control){
		Group pluginInfo = new Group(control, SWT.NONE);
		pluginInfo.setText(Messages.getString("IpfNewProjectWizardPageOne.properties")); //$NON-NLS-1$
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		pluginInfo.setLayout(gl);
		pluginInfo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		gridData.horizontalSpan = 2;
		control.setLayoutData(gridData);
		pluginInfo.setLayoutData(gridData);
		
		new Label(pluginInfo, SWT.NONE).setText(Messages.getString("IpfNewProjectWizardPageOne.id")); //$NON-NLS-1$
		pluginId = createText(pluginInfo, propertiesListener);

		new Label(pluginInfo, SWT.NONE).setText(Messages.getString("IpfNewProjectWizardPageOne.version")); //$NON-NLS-1$
		pluginVersion = createText(pluginInfo, propertiesListener);

		new Label(pluginInfo, SWT.NONE).setText(Messages.getString("IpfNewProjectWizardPageOne.name")); //$NON-NLS-1$
		pluginName = createText(pluginInfo, propertiesListener);

		new Label(pluginInfo, SWT.NONE).setText(Messages.getString("IpfNewProjectWizardPageOne.provider")); //$NON-NLS-1$
		pluginProvider = createText(pluginInfo, propertiesListener);
		
        setControl(control);
        Dialog.applyDialogFont(control);
	}
	
	protected Text createText(Composite parent, ModifyListener listener){
		Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);
		text.addModifyListener(listener);
		text.addFocusListener(selectionListener);
		text.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		return text;
	}
	
    /**
     * Picks up the user input values and sets the corresponding
     * Data Object attribute values.
     */	
	public void updateData(){
		fData.setProjectName(getProjectName());
		fData.setId(pluginId.getText());
		fData.setVersion(pluginVersion.getText());
		fData.setName(pluginName.getText());
		fData.setProvider(pluginProvider.getText());
	}
	
	
	@Override
	protected boolean validatePage(){
		if (!super.validatePage()){
			return false;
		}
		
		//setMessage(null);
		setErrorMessage(null);
		
		String errorMessage = null;

		errorMessage = validateId();
		if (errorMessage != null) {
			setErrorMessage(errorMessage);
			return false;
		}

		errorMessage = validateVersion();
		if (errorMessage != null) {
			setErrorMessage(errorMessage);
			return false;
		}

		errorMessage = validateName();
		if (errorMessage != null) {
			setErrorMessage(errorMessage);
			return false;
		}

		setMessage(null);
		setErrorMessage(null);
		return true;
	}
	
	
	protected String validateVersion() {
		if (pluginVersion.getText().trim().length() == 0) {
			return Messages.getString("IpfNewProjectWizardPageOne.versionRequired"); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * @return warning message if validation fails
	 */
	private String validateName() {
		if (pluginName.getText().trim().length() == 0) {
			return Messages.getString("IpfNewProjectWizardPageOne.nameRequired"); //$NON-NLS-1$
		}
		return null;
	}
	

	private String validateId() {
		String id = pluginId.getText().trim();
		if (id.length() == 0)
			return Messages.getString("IpfNewProjectWizardPageOne.idRequired"); //$NON-NLS-1$

		if (!PluginUtils.isValidCompositeID3_0(id)) {
			return Messages.getString("IpfNewProjectWizardPageOne.invalidId"); //$NON-NLS-1$
		}
		return null;
	}
	
	
	private void suggestPluginProperties(){
		if (!"".equals(getProjectName())
				&& "".equals(pluginId.getText())
				&& "".equals(pluginVersion.getText())
				&& "".equals(pluginName.getText())
				&& "".equals(pluginProvider.getText())){
			pluginId.setText(getProjectName().replaceAll("[^a-zA-Z0-9\\._]", "_"));
			pluginVersion.setText("1.0.0");
			pluginName.setText(PluginUtils.getValidName(pluginId.getText(), 
			        Messages.getString("IpfNewProjectWizardPageOne.plugin"))); //$NON-NLS-1$
			//pluginProvider.setText(PluginUtils.getValidProvider(pluginId.getText()));
		}
	}
}