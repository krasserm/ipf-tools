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
package org.openehealth.ipf.tools.manager.connection.ui.preferences;

import java.io.File;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.openehealth.ipf.tools.manager.connection.Activator;
import org.openehealth.ipf.tools.manager.connection.IConnectionPreferences;
import org.openehealth.ipf.tools.manager.connection.ui.utils.messages.Messages;

public class ConnectionPreferences extends PreferencePage implements
        IWorkbenchPreferencePage {

    private Text truststorePath;

    private Text truststorePassword;

    public ConnectionPreferences() {
    }

    public ConnectionPreferences(String title) {
        super(title);
    }

    public ConnectionPreferences(String title, ImageDescriptor image) {
        super(title, image);
    }

    @Override
    protected Control createContents(Composite parent) {
        IConnectionPreferences preferences = Activator.getDefault()
                .getConnectionPreferences();
        String trustStorePath = preferences.readTrustStorePath();
        String password = preferences.readTrustStorePassword();

        Composite options = new Composite(parent, SWT.NONE);

        options.setLayout(new GridLayout(2, false));

        CLabel label = new CLabel(options, SWT.NONE);
        label.setText(Messages.getLabelString("trust.store.path"));
        label
                .setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false,
                        1, 1));

        label.setToolTipText(Messages
                .getLabelString("trust.store.path.tooltip"));
        truststorePath = new Text(options, SWT.SINGLE | SWT.BORDER);
        truststorePath.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        truststorePath.setText(String.valueOf(trustStorePath));
        truststorePath.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                File truststore = new File(truststorePath.getText());
                if (truststorePath.getText() == null
                        || "".equals(truststorePath.getText())) {
                    setValid(true);
                    setErrorMessage(null);

                } else if (!truststore.exists()) {
                    setValid(false);
                    setErrorMessage(truststorePath.getText()
                            + " "
                            + Messages
                                    .getLabelString("trust.store.path.is.not.valid"));
                } else if (truststore.isDirectory()) {
                    setValid(false);
                    setErrorMessage(truststorePath.getText()
                            + " "
                            + Messages
                                    .getLabelString("trust.store.path.is.not.valid"));
                } else { // in any other case
                    setValid(true);
                    setErrorMessage(null);
                }
            }
        });

        CLabel passwordLabel = new CLabel(options, SWT.NONE);
        passwordLabel.setText(Messages.getLabelString("trust.store.password"));
        passwordLabel.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false,
                false, 1, 1));

        truststorePassword = new Text(options, SWT.SINGLE | SWT.BORDER
                | SWT.PASSWORD);
        truststorePassword.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        truststorePassword.setTextLimit(80);
        truststorePassword.setText(password);
        return options;
    }

    public void init(IWorkbench workbench) {
    }

    @Override
    protected void performApply() {
        // do nothing
    }

    @Override
    public boolean performCancel() {
        return true;
    }

    @Override
    protected void performDefaults() {
        super.performDefaults();
        truststorePath.setText("");
        truststorePassword.setText("");
    }

    @Override
    public boolean performOk() {
        String trustStorePath = truststorePath.getText();
        String trustStorePassword = truststorePassword.getText();
        try {
            IConnectionPreferences preferences = Activator.getDefault()
                    .getConnectionPreferences();
            preferences.storeTrustStorePassword(trustStorePassword);
            preferences.storeTrustStorePath(trustStorePath);
            preferences.updateSystemProperties();
            // do nothing
        } catch (Throwable t) {
            Activator.getDefault().error(
                    "Throwable on ok for connection preferences", t);
        }

        if (checkShouldRestart()) {
            return PlatformUI.getWorkbench().restart();
        } else {
            return super.performOk();
        }
    }

    private boolean checkShouldRestart() {
        MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getShell(), SWT.ICON_QUESTION
                | SWT.YES | SWT.NO);
        messageBox.setMessage("Restart IPF Manager to apply the settings?");
        messageBox.setText("Confirm restart");
        int response = messageBox.open();
        if (response == SWT.YES) {
            return true;
        } else {
            return false;
        }
    }
}
