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
package org.openehealth.ipf.tools.manager.connection.ui.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;
import org.openehealth.ipf.tools.manager.connection.Activator;
import org.openehealth.ipf.tools.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.tools.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.tools.manager.connection.ui.jobs.OpenJMXConnectionJob;
import org.openehealth.ipf.tools.manager.connection.ui.utils.ConnectionUtils;

/**
 * Handles the OPEN (CONNECT) command of a connection.
 * 
 * @see IConnectionConfiguration
 * 
 * @author Mitko Kolev
 */
public class OpenJMXConnectionHandler extends AbstractHandler {

    public Object execute(ExecutionEvent arg0) throws ExecutionException {
        ISelection selection = HandlerUtil.getCurrentSelection(arg0);
        List<IConnectionConfiguration> connectionConfigurations = ConnectionUtils
                .getConnectionConfigurations(selection);
        IJMXConnectionManager manager = Activator.getDefault()
                .getJMXConnectionManager();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            OpenJMXConnectionJob openJMXConnectionJob = new OpenJMXConnectionJob(
                    Display.getCurrent(), manager, connectionConfiguration);
            openJMXConnectionJob.schedule();
        }
        // this method should return null by convention
        return null;
    }

}
