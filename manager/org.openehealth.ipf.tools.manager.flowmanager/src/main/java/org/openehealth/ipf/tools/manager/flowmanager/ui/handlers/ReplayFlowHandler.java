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
package org.openehealth.ipf.tools.manager.flowmanager.ui.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.openehealth.ipf.tools.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.tools.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.tools.manager.flowmanager.ui.editor.FlowManagerEditor;
import org.openehealth.ipf.tools.manager.flowmanager.ui.jobs.ReplayFlowJob;

/**
 * Starts a new UI Job. Works only in the context of an FlowManager editor.
 * 
 * @author Mitko Kolev
 */
public class ReplayFlowHandler extends AbstractHandler {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
     * .ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEditorPart editor = HandlerUtil.getActiveEditorChecked(event);
        if (editor == null) {
            return null;
        }
        if (editor instanceof FlowManagerEditor) {
            ISelection selection = HandlerUtil.getCurrentSelection(event);
            // should neve happen
            if (editor.getEditorInput() == null) {
                return null;
            }

            if (selection != null) {

                List<IFlowInfo> flowInfos = SelectionUtils.getFlows(selection);
                IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) editor
                        .getEditorInput().getAdapter(
                                IConnectionConfiguration.class);
                ReplayFlowJob replayFlowJob = new ReplayFlowJob(Display
                        .getCurrent(), connectionConfiguration, flowInfos);
                replayFlowJob.schedule();
            }
        }
        return null;
    }

}
