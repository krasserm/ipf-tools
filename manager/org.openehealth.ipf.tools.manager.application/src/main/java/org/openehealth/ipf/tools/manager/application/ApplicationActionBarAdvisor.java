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
package org.openehealth.ipf.tools.manager.application;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * Adds the standard eclipse actions and menus. The contribution actions are
 * defined in the plug-in manifest file project.xml.
 * 
 * @author Mitko Kolev
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private IWorkbenchAction about;

    private IWorkbenchAction close;

    private IWorkbenchAction preferences;

    private IWorkbenchAction resetPerspective;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    @Override
    protected void makeActions(IWorkbenchWindow window) {

        about = ActionFactory.ABOUT.create(window);
        register(about);

        close = ActionFactory.QUIT.create(window);
        register(close);

        preferences = ActionFactory.PREFERENCES.create(window);
        register(preferences);

        resetPerspective = ActionFactory.RESET_PERSPECTIVE.create(window);
        register(resetPerspective);

        // editActionsSet = ActionFactory.EDIT_ACTION_SETS.create(window);
        // register(editActionsSet);

    }

    @Override
    protected void fillMenuBar(IMenuManager menuBar) {

        MenuManager fileMenu = new MenuManager("&File",
                IWorkbenchActionConstants.M_FILE);

        MenuManager windowMenu = new MenuManager("&Window",
                IWorkbenchActionConstants.M_WINDOW);

        MenuManager helpMenu = new MenuManager("&Help",
                IWorkbenchActionConstants.M_HELP);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);
        menuBar.add(helpMenu);

        // Fill the File Menu with the standard actions
        // fileMenu.add(save);
        fileMenu.add(new Separator());
        fileMenu.add(close);

        // Fill the window menu with the standard actions
        windowMenu.add(resetPerspective);
        windowMenu.add(preferences);

        // Fill the Help menu with the standard actions
        helpMenu.add(about);

    }

    @Override
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));

    }

    @Override
    protected void fillStatusLine(IStatusLineManager statusLine) {
        // statusLine.add(preferences);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
