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
package org.openehealth.ipf.tools.manager.application.test;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.presentations.AbstractPresentationFactory;

/**
 * Mock class for workbench configurer.
 * 
 * @author Mitko Kolev
 */
public class WorkbenchWindowConfigurerMock implements
        IWorkbenchWindowConfigurer {
    private boolean showCoolbar = false;
    private int shellStyle;
    private boolean showMenuBar;
    private boolean showPerspectiveBar;
    private boolean showProgressIndicator;
    private boolean showStatusLine;
    private boolean showFastViewBars;
    private String title;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#addEditorAreaTransfer
     * (org.eclipse.swt.dnd.Transfer)
     */
    public void addEditorAreaTransfer(Transfer transfer) {
        throw new UnsupportedOperationException("operation not supported");

    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.eclipse.ui.application.IWorkbenchWindowConfigurer#
     * configureEditorAreaDropListener(org.eclipse.swt.dnd.DropTargetListener)
     */
    public void configureEditorAreaDropListener(
            DropTargetListener dropTargetListener) {
        throw new UnsupportedOperationException("operation not supported");

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#createCoolBarControl
     * (org.eclipse.swt.widgets.Composite)
     */
    public Control createCoolBarControl(Composite parent) {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#createMenuBar()
     */
    public Menu createMenuBar() {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#createPageComposite
     * (org.eclipse.swt.widgets.Composite)
     */
    public Control createPageComposite(Composite parent) {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#createStatusLineControl
     * (org.eclipse.swt.widgets.Composite)
     */
    public Control createStatusLineControl(Composite parent) {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getActionBarConfigurer
     * ()
     */
    public IActionBarConfigurer getActionBarConfigurer() {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getData(java.lang
     * .String)
     */
    public Object getData(String key) {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getInitialSize()
     */
    public Point getInitialSize() {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getPresentationFactory
     * ()
     */
    public AbstractPresentationFactory getPresentationFactory() {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getShellStyle()
     */
    public int getShellStyle() {
        return shellStyle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getShowCoolBar()
     */
    public boolean getShowCoolBar() {
        return showCoolbar;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getShowFastViewBars
     * ()
     */
    public boolean getShowFastViewBars() {
        return this.showFastViewBars;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getShowMenuBar()
     */
    public boolean getShowMenuBar() {
        return showMenuBar;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getShowPerspectiveBar
     * ()
     */
    public boolean getShowPerspectiveBar() {
        return showPerspectiveBar;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.eclipse.ui.application.IWorkbenchWindowConfigurer#
     * getShowProgressIndicator()
     */
    public boolean getShowProgressIndicator() {
        return showProgressIndicator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getShowStatusLine()
     */
    public boolean getShowStatusLine() {
        return showStatusLine;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IWorkbenchWindowConfigurer#getTitle()
     */
    public String getTitle() {
        return title;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IWorkbenchWindowConfigurer#getWindow()
     */
    public IWorkbenchWindow getWindow() {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getWorkbenchConfigurer
     * ()
     */
    static WorkbenchConfigurerMock configurer;

    public IWorkbenchConfigurer getWorkbenchConfigurer() {
        if (configurer == null)
            configurer = new WorkbenchConfigurerMock();
        return configurer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#saveState(org.eclipse
     * .ui.IMemento)
     */
    public IStatus saveState(IMemento memento) {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setData(java.lang
     * .String, java.lang.Object)
     */
    public void setData(String key, Object data) {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setInitialSize(
     * org.eclipse.swt.graphics.Point)
     */
    public void setInitialSize(Point initialSize) {
        throw new UnsupportedOperationException("operation not supported");

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setPresentationFactory
     * (org.eclipse.ui.presentations.AbstractPresentationFactory)
     */
    public void setPresentationFactory(AbstractPresentationFactory factory) {
        throw new UnsupportedOperationException("operation not supported");

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setShellStyle(int)
     */
    public void setShellStyle(int shellStyle) {
        this.shellStyle = shellStyle;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setShowCoolBar(
     * boolean)
     */
    public void setShowCoolBar(boolean show) {
        this.showCoolbar = show;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setShowFastViewBars
     * (boolean)
     */
    public void setShowFastViewBars(boolean enable) {
        this.showFastViewBars = enable;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setShowMenuBar(
     * boolean)
     */
    public void setShowMenuBar(boolean show) {
        this.showMenuBar = show;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setShowPerspectiveBar
     * (boolean)
     */
    public void setShowPerspectiveBar(boolean show) {
        this.showPerspectiveBar = show;

    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.eclipse.ui.application.IWorkbenchWindowConfigurer#
     * setShowProgressIndicator(boolean)
     */
    public void setShowProgressIndicator(boolean show) {
        this.showProgressIndicator = show;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setShowStatusLine
     * (boolean)
     */
    public void setShowStatusLine(boolean show) {
        this.showStatusLine = show;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setTitle(java.lang
     * .String)
     */
    public void setTitle(String title) {
        this.title = title;

    }

}
