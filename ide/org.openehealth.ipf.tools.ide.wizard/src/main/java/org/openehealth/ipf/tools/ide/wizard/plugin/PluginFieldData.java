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
package org.openehealth.ipf.tools.ide.wizard.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.openehealth.ipf.tools.ide.wizard.osgi.NewIpfProjectWizardActivator;
import org.openehealth.ipf.tools.ide.wizard.resources.IWizardConstants;

/**
 * 
 * @see org.openehealth.ipf.eclipse.tooling.wizard.plugin.AbstractPluginFieldData
 * 
 * @author Boris Stanojevic
 *
 */
public class PluginFieldData extends AbstractPluginFieldData{

    private String fProjectName;
    
	private List<String> fExportPackageNames = new ArrayList<String>();	
	private List<String> fImportPackageNames;
	
	private List<IPath> fSourcePaths;
	
    private boolean fUseExtension;
	private String fExtensionName;
	private String fExtensionPackage;

	private String fRouteBuilderName;
	private String fRouteBuilderPackage;
	
	private String fTestRouteName;
	
	private boolean fUseJms;
	private boolean fUseFlowManagement;
	private boolean fUseHL7Modules;
	
	
    public String getProjectName() {
        return fProjectName;
    }
    
    public void setProjectName(String projectName) {
        fProjectName = projectName;
    }

    public List<String> getExportPackageNames() {
		return fExportPackageNames;
	}
	
	public void setExportPackageNames(List<String> exportPackageNames) {
		fExportPackageNames = exportPackageNames;
	}
	
	public List<String> getImportPackageNames() {
		if (fImportPackageNames != null){
			return fImportPackageNames;
		}else{
			return defaultImportPackages();	
		}		
	}
	
	public void setImportPackageNames(List<String> importPackageNames) {
		fImportPackageNames = importPackageNames;
	}
	
	public List<IPath> getSourcePaths() {
		if (fSourcePaths != null){
			return fSourcePaths;
		}else{
			return defaultSourcePath();
		}
	}
	
	public void setSourcePaths(List<IPath> sourcePaths) {
		fSourcePaths = sourcePaths;
	}
	
    public boolean isUseExtension() {
        return fUseExtension;
    }
    
    public void setUseExtension(boolean useExtension) {
        fUseExtension = useExtension;
    }
    
    public String getExtensionName() {
        return fExtensionName;
    }
    
    public void setExtensionName(String extensionName) {
        fExtensionName = extensionName;
    }
	
    public String getExtensionPackage() {
		return fExtensionPackage;
	}

	public void setExtensionPackage(String extensionPackage) {
		fExtensionPackage = extensionPackage;
	}

	public String getRouteBuilderName() {
		return fRouteBuilderName;
	}

	public void setRouteBuilderName(String routeBuilderName) {
		fRouteBuilderName = routeBuilderName;
	}

	public String getTestRouteName() {
	    if (fTestRouteName != null){
	        return fTestRouteName;
	    }
        return defaultTestRouteName();
    }

    public void setTestRouteName(String testRouteName) {
        fTestRouteName = testRouteName;
    }

    public String getRouteBuilderPackage() {
		return fRouteBuilderPackage;
	}

	public void setRouteBuilderPackage(String routeBuilderPackage) {
		fRouteBuilderPackage = routeBuilderPackage;
	}

	public boolean isUseJms() {
        return fUseJms;
    }
    
    public void setUseJms(boolean useJms) {
        fUseJms = useJms;
    }
    
    public boolean isUseFlowManagement() {
        return fUseFlowManagement;
    }
    
    public void setUseFlowManagement(boolean useFlowManagement) {
        fUseFlowManagement = useFlowManagement;
    }
    
    public boolean isUseHL7Modules() {
        return fUseHL7Modules;
    }
    
    public void setUseHL7Modules(boolean useHL7Modules) {
        fUseHL7Modules = useHL7Modules;
    }

    private List<IPath> defaultSourcePath(){   	
		List<IPath> paths = new ArrayList<IPath>();
		paths.add(new Path(IWizardConstants.JAVA_MAIN_FOLDER));
		paths.add(new Path(IWizardConstants.JAVA_TEST_FOLDER));
		paths.add(new Path(IWizardConstants.GROOVY_MAIN_FOLDER));
		paths.add(new Path(IWizardConstants.GROOVY_TEST_FOLDER));
		paths.add(new Path(IWizardConstants.RESOURCES_MAIN_FOLDER));
		paths.add(new Path(IWizardConstants.RESOURCES_TEST_FOLDER));
		return paths;
	}
    
    private List<String> defaultImportPackages(){
		List<String> imports = new ArrayList<String>();
		
		addImportsFromProperties("imports.default", imports);
		
		if (isUseFlowManagement()){
			addImportsFromProperties("imports.flow", imports);
		}
		
		if (isUseHL7Modules()){
			addImportsFromProperties("imports.HL7", imports);
		}
		
		return imports;
	}
    
    private void addImportsFromProperties(String property, List<String> imports){
		String importsString = (String)NewIpfProjectWizardActivator.getDefault().getProperties().get(property);
		StringTokenizer tok = new StringTokenizer(importsString, ",");
		while (tok.hasMoreTokens()){
			String token = tok.nextToken();
			imports.add(token.trim());
		}    	
    }
    
    private String defaultTestRouteName(){
        if (getRouteBuilderName() != null){
            return getRouteBuilderName().substring(0, getRouteBuilderName().lastIndexOf(".groovy")) + "Test.java";
        }
        return "Test.java";
    }

}
