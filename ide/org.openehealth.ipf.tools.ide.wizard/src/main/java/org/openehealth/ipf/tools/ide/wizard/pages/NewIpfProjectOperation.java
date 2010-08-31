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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.openehealth.ipf.tools.ide.wizard.plugin.PluginFieldData;
import org.openehealth.ipf.tools.ide.wizard.codegen.model.BuildProperties;
import org.openehealth.ipf.tools.ide.wizard.codegen.model.GroovyPrefs;
import org.openehealth.ipf.tools.ide.wizard.codegen.model.ManifestMF;
import org.openehealth.ipf.tools.ide.wizard.codegen.model.RouteContextXML;
import org.openehealth.ipf.tools.ide.wizard.codegen.model.RouteOsgiContextXML;
import org.openehealth.ipf.tools.ide.wizard.codegen.model.SampleExtensionGroovy;
import org.openehealth.ipf.tools.ide.wizard.codegen.model.SampleRouteBuilderGroovy;
import org.openehealth.ipf.tools.ide.wizard.codegen.model.SampleRouteTestJava;
import org.openehealth.ipf.tools.ide.wizard.codegen.model.TestContextXML;

import static org.openehealth.ipf.tools.ide.wizard.resources.IWizardConstants.*;

/**
 * Provides all necessary steps to create all required 
 * resources to the New IPF Project
 * 
 * @author Boris Stanojevic
 * 
 */
public class NewIpfProjectOperation extends WorkspaceModifyOperation {

    private PluginFieldData fData;
    private IProject fProject;
    private IProjectDescription fDescription;


    /**
     * Constructor which sets the required Data Objects
     *  
     * @param fDescription
     * @param fProject
     * @param fData
     */
    public NewIpfProjectOperation(IProjectDescription fDescription,
            IProject fProject, PluginFieldData fData) {
        this.fDescription = fDescription;
        this.fProject = fProject;
        this.fData = fData;
    }

    /**
     * Create Project execution. It is potentially a long running process
     * therefore the provided monitor is used.
     */
    @Override
    protected void execute(IProgressMonitor monitor) throws CoreException,
            InvocationTargetException, InterruptedException {
        createProject(fDescription, fProject, monitor, fData);
    }

    /**
     * 
     * @param description
     *            New Projects Decription
     * @param proj
     *            New Projects instance
     * @param monitor
     *            ProgressMonitor for long running processes
     * @param pluginData
     *            Data Object with data collected from the Wizard
     * @throws CoreException
     * @throws OperationCanceledException
     */
    void createProject(IProjectDescription description, IProject proj,
            IProgressMonitor monitor, PluginFieldData pluginData)
            throws CoreException, OperationCanceledException {

        try {
            monitor.beginTask("", 2000);

            proj.create(description, new SubProgressMonitor(monitor, 1000));

            if (monitor.isCanceled()) {
                throw new OperationCanceledException();
            }
            proj.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(
                    monitor, 1000));

            addNatureToProject(description, proj, monitor);
            addBuilderToProject(description, proj, monitor);

            IContainer container = (IContainer) proj;

            addClasspath(container, monitor, pluginData);
            addProjectResources(container, monitor, pluginData);

        } catch (IOException ioe) {
            throwCoreException(ioe.getLocalizedMessage());
        } finally {
            monitor.done();
        }
    }

    private void addClasspath(IContainer container, IProgressMonitor monitor,
            PluginFieldData pluginData) throws CoreException, IOException {
        List<IClasspathEntry> entries = new LinkedList<IClasspathEntry>();
        for (IPath path : pluginData.getSourcePaths()) {
            createFolder(container, monitor, path);
            entries.add(JavaCore.newSourceEntry(container.getFullPath().append(
                    path)));
        }

        IJavaProject ijp = JavaCore.create(container.getProject());
        entries.add(JavaRuntime.getDefaultJREContainerEntry());
        entries.add(JavaCore.newContainerEntry(new Path(PLUGINS_CLASSPATH)));
        entries.add(JavaCore.newContainerEntry(new Path(JUNIT4_CLASSPATH)));

        ijp.setRawClasspath(entries
                .toArray(new IClasspathEntry[entries.size()]), monitor);
    }

    private void addProjectResources(IContainer container,
            IProgressMonitor monitor, PluginFieldData pluginData)
            throws CoreException, IOException {

        String manifest = new ManifestMF().generate(pluginData);
        createResource(METAINF_NAME, META_FOLDER, manifest.getBytes(),
                container, monitor);

        String buildProps = new BuildProperties().generate(pluginData);
        createResource(BUILD_PROPERTIES_NAME, "", buildProps.getBytes(),
                container, monitor);

        String contextRoute = new RouteContextXML().generate(pluginData);
        createResource(ROUTE_CONTEXT_NAME, SPRING_FOLDER, contextRoute
                .getBytes(), container, monitor);

        String contextRouteOsgi = new RouteOsgiContextXML()
                .generate(pluginData);
        createResource(ROUTE_OSGI_CONTEXT_NAME, SPRING_FOLDER, contextRouteOsgi
                .getBytes(), container, monitor);

        if (pluginData.isUseExtension()) {
            String sampleExtension = new SampleExtensionGroovy()
                    .generate(pluginData);
            createResource(pluginData.getExtensionName(), GROOVY_MAIN_FOLDER
                    + pluginData.getExtensionPackage(), sampleExtension
                    .getBytes(), container, monitor);
        }

        String sampleRouteBuilder = new SampleRouteBuilderGroovy()
                .generate(pluginData);
        createResource(pluginData.getRouteBuilderName(), GROOVY_MAIN_FOLDER
                + pluginData.getRouteBuilderPackage(), sampleRouteBuilder
                .getBytes(), container, monitor);
       
        String sampleRouteTest = new SampleRouteTestJava().generate(pluginData);
        createResource(pluginData.getTestRouteName(), JAVA_TEST_FOLDER
                + pluginData.getRouteBuilderPackage(), sampleRouteTest
                .getBytes(), container, monitor);
        
        String testContext = new TestContextXML().generate(pluginData);
        createResource(TEST_CONTEXT_NAME, RESOURCES_TEST_FOLDER,
               testContext.getBytes(), container, monitor);
        
        String groovyPrefs = new GroovyPrefs().generate(pluginData);
        createResource(GROOVY_PREFS_NAME, SETTINGS_FOLDER,
        		groovyPrefs.getBytes(), container, monitor);
               
    }

    private void createResource(String name, String path, byte[] content,
            IContainer container, IProgressMonitor monitor)
            throws CoreException {
        IPath destination = new Path(path);
        if (!destination.toFile().exists()) {
            createFolder(container, monitor, destination);
        }
        if (!path.endsWith(System.getProperty("file.separator"))) {
            path = path + System.getProperty("file.separator");
        }
        addFileToProject(container, new Path(path + name),
                new ByteArrayInputStream(content), monitor);
    }

    /**
     * helper to create a subsequent folders from the path segments e.g
     * /src/main/java will add all three segments sequentially
     * 
     * @param container
     * @param monitor
     * @param path
     * @throws CoreException
     */
    private void createFolder(IContainer container, IProgressMonitor monitor,
            IPath path) throws CoreException {
        String pathParent = "";
        for (String segment : path.segments()) {
            pathParent += segment + System.getProperty("file.separator");
            if (!container.getFolder(new Path(pathParent)).exists()) {
                container.getFolder(new Path(pathParent)).create(true, true,
                        monitor);
            }
        }
    }

    /**
     * Adds a new file to the project.
     * 
     * @param container
     * @param path
     * @param contentStream
     * @param monitor
     * @throws CoreException
     */
    private void addFileToProject(IContainer container, Path path,
            InputStream contentStream, IProgressMonitor monitor)
            throws CoreException {

        final IFile file = container.getFile(path);
        if (file.exists()) {
            file.setContents(contentStream, true, true, monitor);
        } else {
            file.create(contentStream, true, monitor);
        }
    }

    /**
     * Adds the required Nature(s) to the Project description
     * 
     * @param description
     * @param proj
     * @param monitor
     * @throws CoreException
     */
    private void addNatureToProject(IProjectDescription description,
            IProject proj, IProgressMonitor monitor) throws CoreException {

        if (!proj.hasNature(JavaCore.NATURE_ID)) {
            doAddNature(description, proj, monitor, JavaCore.NATURE_ID);
        }
        if (!proj.hasNature(GROOVY_NATURE_ID)
                && proj.getWorkspace().getNatureDescriptor(GROOVY_NATURE_ID) != null) {
            doAddNature(description, proj, monitor, GROOVY_NATURE_ID);
        }
        if (!proj.hasNature(PLUGIN_NATURE_ID)) {
            doAddNature(description, proj, monitor, PLUGIN_NATURE_ID);
        }
    }

    /**
     * Adds the required Builders to the Project description
     * 
     * @param description
     * @param proj
     * @param monitor
     * @throws CoreException
     */
    private void addBuilderToProject(IProjectDescription description,
            IProject proj, IProgressMonitor monitor) throws CoreException {

        if (!hasBuilder(description, JavaCore.BUILDER_ID)) {
            doAddBuilder(description, proj, monitor, JavaCore.BUILDER_ID);
        }
//        if (!hasBuilder(description, GROOVY_BUILDER_ID)) {
//            doAddBuilder(description, proj, monitor, GROOVY_BUILDER_ID);
//        }
        if (!proj.hasNature(MANIFEST_BUILDER_ID)) {
            doAddBuilder(description, proj, monitor, MANIFEST_BUILDER_ID);
        }
        if (!proj.hasNature(PLUGIN_NATURE_ID)) {
            doAddBuilder(description, proj, monitor, PLUGIN_NATURE_ID);
        }

    }

    private void doAddNature(IProjectDescription description, IProject proj,
            IProgressMonitor monitor, String natureName) throws CoreException {

        String[] prevNatures = proj.getDescription().getNatureIds();
        String[] newNatures = new String[prevNatures.length + 1];
        if (!proj.hasNature(natureName)) {
            System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
            newNatures[prevNatures.length] = natureName;
            description.setNatureIds(newNatures);
            proj.setDescription(description, monitor);
        }
    }

    private void doAddBuilder(IProjectDescription description, IProject proj,
            IProgressMonitor monitor, String builderName) throws CoreException {

        ICommand[] prevBuilders = description.getBuildSpec();
        ICommand[] newBuilders = new ICommand[prevBuilders.length + 1];

        System.arraycopy(prevBuilders, 0, newBuilders, 0, prevBuilders.length);
        ICommand newCommand = description.newCommand();
        newCommand.setBuilderName(builderName);
        newBuilders[prevBuilders.length] = newCommand;
        description.setBuildSpec(newBuilders);
        proj.setDescription(description, monitor);
    }

    /**
     * 
     * 
     * @param description
     * @param builderName
     * @return
     */
    private boolean hasBuilder(IProjectDescription description,
            String builderName) {
        for (ICommand command : description.getBuildSpec()) {
            if (command.getBuilderName().equals(builderName)) {
                return true;
            }
        }
        return false;
    }

    private void throwCoreException(String message) throws CoreException {
        IStatus status = new Status(IStatus.ERROR,
                "org.openehealth.ipf.tools.ide.wizard", IStatus.OK,
                message, null);
        throw new CoreException(status);
    }
}
