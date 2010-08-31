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

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * Standard implementation of ApplicationWorkbenchAdvisor, which saves the
 * Workbench state.
 * 
 * @author Mitko Kolev
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    /**
     * The field has a fixed value to the connection's perspective.
     */
    public static final String PERSPECTIVE_ID = "org.openehealth.ipf.tools.manager.connection.ui.perspective.DefaultPerspective";

    public ApplicationWorkbenchAdvisor() {
        super();
    }

    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
            IWorkbenchWindowConfigurer configurer) {
        ApplicationWorkbenchWindowAdvisor windowAdviser = new ApplicationWorkbenchWindowAdvisor(
                configurer);
        return windowAdviser;
    }

    @Override
    public String getInitialWindowPerspectiveId() {
        return PERSPECTIVE_ID;
    }

    @Override
    public void initialize(IWorkbenchConfigurer configurer) {
        super.initialize(configurer);
        // restore the perspectives state
        configurer.setSaveAndRestore(true);
    }

}
