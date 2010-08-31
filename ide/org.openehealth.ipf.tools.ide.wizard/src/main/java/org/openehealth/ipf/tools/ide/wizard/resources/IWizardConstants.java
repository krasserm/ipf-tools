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
package org.openehealth.ipf.tools.ide.wizard.resources; 

/**
 * provides the constants needed for creation of the new
 * IPF project 
 * 
 * @author Boris Stanojevic
 */
public interface IWizardConstants {

    /** groovy builder ID. */
    public static final String GROOVY_BUILDER_ID = "org.codehaus.groovy.eclipse.groovyBuilder";
    /** manifest builder ID. */
    public static final String MANIFEST_BUILDER_ID = "org.eclipse.pde.ManifestBuilder";
    /** schema builder ID. */
    public static final String SCHEMA_BUILDER_ID = "org.eclipse.pde.SchemaBuilder";    
    /** groovy nature ID. */
    public static final String GROOVY_NATURE_ID = "org.eclipse.jdt.groovy.core.groovyNature";
    /** plugin nature ID. */
    public static final String PLUGIN_NATURE_ID = "org.eclipse.pde.PluginNature";
    /** plugins classpath ID */
    public static final String PLUGINS_CLASSPATH = "org.eclipse.pde.core.requiredPlugins";
    /** junit4 libs classpath ID */
    public static final String JUNIT4_CLASSPATH = "org.eclipse.jdt.junit.JUNIT_CONTAINER/4";
    
    /** specific IPF Project constants */
    public static final String MAIN_FOLDER = "src/main/";
    
    public static final String JAVA_MAIN_FOLDER = MAIN_FOLDER + "java/";
    
    public static final String GROOVY_MAIN_FOLDER = MAIN_FOLDER + "groovy/";
    
    public static final String RESOURCES_MAIN_FOLDER = MAIN_FOLDER + "resources/";
    
    public static final String TEST_FOLDER = "src/test/";
    
    public static final String JAVA_TEST_FOLDER = TEST_FOLDER + "java/";
    
    public static final String GROOVY_TEST_FOLDER = TEST_FOLDER + "groovy/";
    
    public static final String RESOURCES_TEST_FOLDER = TEST_FOLDER + "resources/";
    
    public static final String SETTINGS_FOLDER = ".settings/";
    
    public static final String GROOVY_CLASS_FOLDER = "bin-groovy/";
    
    public static final String META_FOLDER = "META-INF/";
    
    public static final String METAINF_NAME = "MANIFEST.MF";
    
    public static final String SPRING_FOLDER = META_FOLDER + "spring/";
    
    public static final String BUILD_PROPERTIES_NAME = "build.properties";
    
    public static final String ROUTE_CONTEXT_NAME = "route-context.xml";
    
    public static final String ROUTE_OSGI_CONTEXT_NAME = "route-osgi-context.xml";
    
    public static final String TEST_CONTEXT_NAME = "test-context.xml";
    
    public static final String GROOVY_PREFS_NAME = "org.codehaus.groovy.eclipse.preferences.prefs";
    
}
 