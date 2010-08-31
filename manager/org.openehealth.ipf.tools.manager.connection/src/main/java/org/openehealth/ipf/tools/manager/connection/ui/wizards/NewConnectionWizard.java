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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.openehealth.ipf.tools.manager.connection.Activator;
import org.openehealth.ipf.tools.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.tools.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.tools.manager.connection.ui.jobs.OpenJMXConnectionJob;
import org.openehealth.ipf.tools.manager.connection.ui.perspective.DefaultPerspective;
import org.openehealth.ipf.tools.manager.connection.ui.utils.messages.Messages;

/**
 * 
 * Wizard for creating new connections
 * 
 * @author Mitko Kolev
 */
public class NewConnectionWizard extends Wizard implements INewWizard {

    private NewConnectionPage newConnectionPage;

    private static final String WIZARD_TITLE_KEY = "NewConnectionPage.title";

    private static final String IMG_CONNECTION_KEY = "icons/connection/computer_add_64x64.png";

    private Image connectionImage;

    private ImageDescriptor connectionImageDescriptor;

    /**
     * Creates a new Connection wizard.
     * 
     * @param connectionConfiguration
     *            The last stored connection that will provide the initial data.
     */
    public NewConnectionWizard() {
        this.setWindowTitle(Messages.getLabelString(WIZARD_TITLE_KEY));
    }

    private void loadImages() {
        connectionImageDescriptor = Activator.getDefault().getImageDescriptor(
                IMG_CONNECTION_KEY);
        if (connectionImageDescriptor != null)
            connectionImage = connectionImageDescriptor.createImage();
    }

 
    @Override
    public boolean performFinish() {
        IJMXConnectionManager manager = Activator.getDefault()
                .getJMXConnectionManager();

        IWizardPage page = newConnectionPage.getNextPage();
        if (page == null) {// no more pages
            IConnectionConfiguration newConnection = newConnectionPage
                    .getConnectionResult();
            if (newConnection != null) {
                manager.addConnectionConfiguration(newConnection);
            }
            if (newConnectionPage.getShouldConnect()) {
                new OpenJMXConnectionJob(Display.getCurrent(), manager,
                        newConnection).schedule();
            }
        }

        try {
            PlatformUI.getWorkbench().showPerspective(
                    DefaultPerspective.class.getName(),
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow());
        } catch (WorkbenchException e) {
            Activator.getDefault().error("Can not open the management perspective "
                    + "from the new connection wizard", e);
        }
        return true;
    }

    @Override
    public Image getDefaultPageImage() {
        loadImages();
        if (connectionImage == null) {
            connectionImage = JFaceResources.getResources()
                    .createImageWithDefault(connectionImageDescriptor);
        }
        return connectionImage;
    }

   
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        //do nothing
    }

    @Override
    public void addPages() {
        newConnectionPage = new NewConnectionPage();
        this.addPage(newConnectionPage);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (connectionImage != null) {
            JFaceResources.getResources().destroyImage(
                    connectionImageDescriptor);
            connectionImage = null;
        }
    }

}
