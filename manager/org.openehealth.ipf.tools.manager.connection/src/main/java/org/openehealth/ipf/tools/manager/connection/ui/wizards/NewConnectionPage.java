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
package org.openehealth.ipf.tools.manager.connection.ui.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.openehealth.ipf.tools.manager.connection.Activator;
import org.openehealth.ipf.tools.manager.connection.AuthenticationCredentials;
import org.openehealth.ipf.tools.manager.connection.ConnectionConfigurationImpl;
import org.openehealth.ipf.tools.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.tools.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.tools.manager.connection.ui.utils.messages.Messages;

/**
 * Page for the new Connection wizard with validation.
 * 
 * @author Mitko Kolev
 */
public class NewConnectionPage extends WizardPage implements SelectionListener {

    private final NewConnectionPageUIDataStore store;

    private final IJMXConnectionManager jMXConnectionManager;

    private Text connectionHostText;

    private Text connectionPortText;

    private Text jmxURLPathText;

    private Text userNameText;

    private Text passwordText;

    private Text connectionNameText;

    private Button useAuthentication;

    private Button useCustomJMXProperties;

    private Button shouldConnectButton;

    private Text jmxConnectorPortText;

    private IConnectionConfiguration newConnection;

    private boolean shouldConnect = true;

    /**
     * Creates a new page which updates the wizard.
     * 
     * @param lastConnectionConfiguration
     */
    public NewConnectionPage() {
        super(Messages.getLabelString("NewConnectionPage.name"));
        store = new NewConnectionPageUIDataStore();
        jMXConnectionManager = Activator.getDefault().getJMXConnectionManager();
        setTitle(Messages.getLabelString("NewConnectionPage.title"));
        setDescription(Messages.getLabelString("NewConnectionPage.description"));
        newConnection = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
     * .Composite)
     */
    public void createControl(Composite parent) {
        Composite mainComposite = new Composite(parent, SWT.FILL);
        GridLayout mainLayout = new GridLayout();
        mainLayout.numColumns = 2;
        mainLayout.marginHeight = 0;
        mainLayout.marginWidth = 0;
        mainLayout.verticalSpacing = 5;
        mainComposite.setLayout(mainLayout);
        ConnectionPageData data = store.readState();

        createControls(mainComposite, data);
        this.setControl(mainComposite);

        setInitialValues(data);

        setEnabledDisabledStateAccordingTo(useCustomJMXProperties);
        setEnabledDisabledStateAccordingTo(useAuthentication);

        // check if we have a connection with that name in the repository
        if (data.getName() != null && !data.getName().trim().equals("")) {
            if (jMXConnectionManager.isConnectionNameInUse(data.getName())) {
                this.setPageComplete(false);
            } else {
                this.setPageComplete(true);
            }
        } else { // null or empty name
            this.setPageComplete(false);
        }
        // initially use no error message (Eclipse usability rule)
        this.setErrorMessage(null);

    }

    private void setInitialValues(ConnectionPageData data) {
        connectionNameText.setText(data.getName());
        connectionHostText.setText(data.getHost());
        connectionPortText.setText(String.valueOf(data.getPort()));
        useAuthentication.setSelection(data.isUseAuthentication());
        userNameText.setText(data.getUsername());
        passwordText.setText(data.getPassword());
        useCustomJMXProperties.setSelection(data.isUseCustomJMXSettings());
        jmxURLPathText.setText(data.getJMXPath());
        jmxConnectorPortText.setText(data.getJMXConnectorPort());
        shouldConnectButton.setSelection(data.isOpenConnectionOnFinish());
    }

    /**
     * Creates the UI contorls and sets their data according to the
     * <code> data</code> value object
     * 
     * @param parent
     *            The parent UI component
     * @param data
     *            the POJO with the component data, must not be null
     */
    private void createControls(Composite parent, ConnectionPageData data) {

        Composite mainGroup = new Composite(parent, SWT.NONE);
        GridLayout gl = new GridLayout();
        gl.numColumns = 2;
        mainGroup.setLayout(gl);
        mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
                2));
        CLabel l = new CLabel(mainGroup, SWT.NONE);
        l.setText(Messages.getLabelString("NewConnectionPage.purposeText")
                + "\n");
        l.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2));

        CLabel connectionName = new CLabel(mainGroup, SWT.LEFT);
        connectionName.setText(Messages
                .getLabelString("NewConnectionPage.name"));
        connectionName.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
                false, 1, 1));
        connectionNameText = new Text(mainGroup, SWT.BORDER);
        connectionNameText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        connectionNameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateAll();
            }

        });

        CLabel connectionHost = new CLabel(mainGroup, SWT.LEFT);
        connectionHost.setText(Messages
                .getLabelString("NewConnectionPage.connectionHost"));
        connectionHost.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
                false, 1, 1));
        connectionHostText = new Text(mainGroup, SWT.BORDER);
        connectionHostText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        connectionHostText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateAll();
            }

        });

        CLabel connectionPort = new CLabel(mainGroup, SWT.LEFT);
        connectionPort.setText(Messages
                .getLabelString("NewConnectionPage.connectionPort"));
        connectionPort.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
                false, 1, 1));
        connectionPortText = new Text(mainGroup, SWT.BORDER);
        connectionPortText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        connectionPortText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateAll();
            }
        });

        Group authentication = new Group(mainGroup, SWT.NONE);
        authentication.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 2, 2));
        authentication.setText("Authentication");
        authentication.setLayout(new GridLayout(2, false));

        useAuthentication = new Button(authentication, SWT.CHECK);
        useAuthentication.setText(Messages
                .getLabelString("NewConnectionPage.useAuthentication"));
        useAuthentication.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                false, 2, 1));
        useAuthentication.addSelectionListener(this);

        CLabel userName = new CLabel(authentication, SWT.LEFT);
        userName.setText(Messages.getLabelString("NewConnectionPage.userName"));
        userName.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,
                1, 1));
        userNameText = new Text(authentication, SWT.BORDER);
        userNameText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        userNameText.setEnabled(false);
        userNameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateAll();
            }
        });

        CLabel password = new CLabel(authentication, SWT.LEFT);
        password.setText(Messages.getLabelString("NewConnectionPage.password"));
        password.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,
                1, 1));
        passwordText = new Text(authentication, SWT.BORDER | SWT.PASSWORD);
        passwordText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        passwordText.setEnabled(false);
        passwordText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateAll();
            }

        });

        Group jmx = new Group(mainGroup, SWT.NONE);
        jmx.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
        jmx.setText(Messages
                .getLabelString("NewConnectionPage.advancedJMXProperties"));
        jmx.setLayout(new GridLayout(2, false));

        useCustomJMXProperties = new Button(jmx, SWT.CHECK);
        useCustomJMXProperties.setText(Messages
                .getLabelString("NewConnectionPage.customizeJMXURL"));
        useCustomJMXProperties.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
                true, false, 2, 1));
        useCustomJMXProperties.addSelectionListener(this);

        CLabel connectionPath = new CLabel(jmx, SWT.LEFT);
        connectionPath.setText(Messages
                .getLabelString("NewConnectionPage.path"));
        connectionPath.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
                false, 1, 1));
        jmxURLPathText = new Text(jmx, SWT.BORDER);
        jmxURLPathText.setEnabled(false);
        jmxURLPathText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        jmxURLPathText.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                validateAll();
            }
        });

        CLabel connectorPortLabel = new CLabel(jmx, SWT.LEFT);
        connectorPortLabel.setText(Messages
                .getLabelString("NewConnectionPage.connectorPort"));
        connectorPortLabel.setLayoutData(new GridData(SWT.FILL, SWT.NONE,
                false, false, 1, 1));
        jmxConnectorPortText = new Text(jmx, SWT.BORDER);
        jmxConnectorPortText.setLayoutData(new GridData(SWT.FILL, SWT.NONE,
                true, false, 1, 1));
        jmxConnectorPortText.setEnabled(false);
        jmxConnectorPortText.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                validateAll();
            }
        });

        Group shouldConnectGroup = new Group(mainGroup, SWT.NONE);
        shouldConnectGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 2, 2));
        shouldConnectGroup.setLayout(new GridLayout(2, false));

        shouldConnectButton = new Button(shouldConnectGroup, SWT.CHECK);
        shouldConnectButton.setSelection(true);
        shouldConnectButton.setText(Messages
                .getLabelString("NewConnectionPage.shouldConnect"));

        shouldConnectButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE,
                true, false, 2, 1));

    }

    protected boolean validateName() {
        String name = connectionNameText.getText();
        if (name == null || name.equals("")) {
            NewConnectionPage.this.setErrorMessage(null);
            return false;
        }
        if (jMXConnectionManager.isConnectionNameInUse(name)) {
            NewConnectionPage.this.setErrorMessage(Messages
                    .getLabelString("connection.name.is.already.used.error"));
            return false;
        }
        return true;

    }

    private boolean validateHost() {
        String host = connectionHostText.getText();
        if (host == null || host.trim().equals("")) {
            // do not allow null host
            NewConnectionPage.this.setErrorMessage(null);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateJMXPath() {
        String path = jmxURLPathText.getText();
        if (path == null || path.trim().equals("")) {
            // do not allow empty JMX path
            // just disable finish button, do not show error
            NewConnectionPage.this.setErrorMessage(null);
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePort() {
        String port = connectionPortText.getText();
        if (port == null || port.trim().equals("")) {
            NewConnectionPage.this
                    .setErrorMessage("connection.port.is.invalid.error");
            return false;
        }
        try {
            Short.parseShort(port);
            return true;
        } catch (NumberFormatException nfe) {
            NewConnectionPage.this.setErrorMessage(Messages
                    .getLabelString("connection.port.is.invalid.error"));
            return false;
        }
    }

    private boolean validateJMXConnectorPort() {
        String port = jmxConnectorPortText.getText();
        if (port == null || port.trim().equals("")) {
            // allow null connector port
            return true;
        }
        try {
            Short.parseShort(port);
            return true;
        } catch (NumberFormatException nfe) {
            NewConnectionPage.this.setErrorMessage(Messages
                    .getLabelString("connector.port.is.invalid.error"));
            return false;
        }
    }

    private boolean validateUsername() {
        boolean selected = useAuthentication.getSelection();
        if (selected) {
            String userName = userNameText.getText();
            if (userName == null || userName.trim().equals("")) {
                // do not allow null username
                NewConnectionPage.this.setErrorMessage(null);
                return false;
            }
        }
        return true;
    }

    private boolean validatePassword() {
        boolean selected = useAuthentication.getSelection();
        if (selected) {
            String password = passwordText.getText();
            if (password == null || password.trim().equals("")) {
                // do not allow null password
                NewConnectionPage.this.setErrorMessage(null);
                return false;
            }
        }
        return true;
    }

    private boolean validateAll() {
        boolean valid = false;
        try {
            if (!validateName()) {
                return false;
            }
            if (!validateHost()) {
                return false;
            }
            if (!validatePort()) {
                return false;
            }
            if (useAuthentication.getSelection()) {
                if (!validateUsername()) {
                    return false;
                }
                if (!validatePassword()) {
                    return false;
                }
            }
            if (useCustomJMXProperties.getSelection()) {
                if (!validateJMXPath()) {
                    return false;
                }
                if (!validateJMXConnectorPort()) {
                    return false;
                }
            }
            setErrorMessage(null);
            setPageComplete(true);
            valid = true;
        } finally { // if we have error somewhere, set the status to incomplete
            if (valid == false) {
                setPageComplete(false);
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
     * .events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e) {
        Object source = e.getSource();
        setEnabledDisabledStateAccordingTo(source);
        validateAll();
    }

    private void setEnabledDisabledStateAccordingTo(Object source) {
        if (source.equals(useAuthentication)) {
            boolean checked = useAuthentication.getSelection();
            if (checked) {
                userNameText.setEnabled(true);
                passwordText.setEnabled(true);

            } else {
                userNameText.setEnabled(false);
                passwordText.setEnabled(false);
            }
        } else if (source.equals(useCustomJMXProperties)) {
            boolean checked = useCustomJMXProperties.getSelection();
            if (checked) {
                jmxConnectorPortText.setEnabled(true);
                jmxURLPathText.setEnabled(true);
            } else {
                jmxConnectorPortText.setEnabled(false);
                jmxURLPathText.setEnabled(false);

            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
     * .swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    @Override
    public IWizardPage getNextPage() {
        this.shouldConnect = false;

        // General properties
        String sPort = connectionPortText.getText();
        String host = connectionHostText.getText();
        String name = connectionNameText.getText();
        String username = userNameText.getText();
        String password = passwordText.getText();
        String jmxConnectorPort = jmxConnectorPortText.getText() == null ? ""
                : jmxConnectorPortText.getText().trim();
        String path = jmxURLPathText.getText() == null ? "" : jmxURLPathText
                .getText().trim();

        boolean hasAuthentication = useAuthentication.getSelection();
        int port = Integer.parseInt(sPort);
        ConnectionConfigurationImpl connectionConfiguration;

        // Authentication properties
        if (!hasAuthentication) {
            connectionConfiguration = new ConnectionConfigurationImpl(name,
                    host, port);
        } else {

            connectionConfiguration = new ConnectionConfigurationImpl(name,
                    host, port, new AuthenticationCredentials(username,
                            password));
        }
        // Custom JMX properties
        if (useCustomJMXProperties.getSelection()) {
            if (!jmxConnectorPort.equals("")) {
                int connectorPort = Integer.parseInt(jmxConnectorPort);
                connectionConfiguration.setJMXConnectorPort(connectorPort);
            }

            if (!path.equals("")) {
                connectionConfiguration.setJMXURLPath(path);
            }
        }

        ConnectionPageData data = new ConnectionPageData();
        data.setName(name);
        data.setHost(host);
        data.setPort(port);
        data.setUseAuthentication(useAuthentication.getSelection());
        data.setUsername(username);
        data.setPassword(password);
        data.setUseCustomJMXSettings(useCustomJMXProperties.getSelection());
        data.setJMXPath(path);
        data.setJMXConnectorPort(jmxConnectorPort);
        data.setOpenConnectionOnFinish(shouldConnectButton.getSelection());
        store.saveState(data);
        // store the variables, so that they can be used from the wizard
        this.shouldConnect = shouldConnectButton.getSelection();
        this.newConnection = connectionConfiguration;
        return super.getNextPage();
    }

    /**
     * @return The newConnection of the page, set when the page has been
     *         finished
     */
    public IConnectionConfiguration getConnectionResult() {
        return newConnection;
    }

    /**
     * @return true if the wizard should open the connection when it finishes.
     */
    public boolean getShouldConnect() {
        return this.shouldConnect;
    }
}
