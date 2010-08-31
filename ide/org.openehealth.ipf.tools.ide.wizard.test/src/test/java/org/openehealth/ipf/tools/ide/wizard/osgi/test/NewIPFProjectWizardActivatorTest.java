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
package org.openehealth.ipf.tools.ide.wizard.osgi.test;

import java.util.Properties;

import junit.framework.TestCase;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.openehealth.ipf.tools.ide.wizard.osgi.NewIpfProjectWizardActivator;
import org.openehealth.ipf.tools.ide.wizard.test.Activator;
import org.osgi.framework.BundleContext;
/**
 * 
 * @author Boris Stanojevic 
 *
 */
@SuppressWarnings("restriction")
public class NewIPFProjectWizardActivatorTest extends TestCase{
	
	
	public void testStartAndStop() throws Exception{
		startIPFProjectWizardActivator();
		Properties props = NewIpfProjectWizardActivator.getDefault().getProperties();
		assertNotNull(props);
		assertNotNull(props.getProperty("imports.default"));
		assertTrue(props.getProperty("imports.default")
				.contains("groovy.lang"));
		
		assertNotNull(props.getProperty("imports.HL7"));
		assertTrue(props.getProperty("imports.HL7")
				.contains("ca.uhn.hl7v2.validation"));

	}
	

	public void startIPFProjectWizardActivator() throws Exception{
    	BundleContext bc = null;
    	NewIpfProjectWizardActivator activator = null;
    	if (Activator.getDefault() != null
    		&& Activator.getDefault().getContext() != null){
    		bc = Activator.getDefault().getContext();
    	}else{
	        Properties frameworkProperties = new Properties();
	    
	        frameworkProperties.put("osgi.clean", "true");
	        frameworkProperties.put("osgi.console", "true");
	    
	        EclipseStarter.setInitialProperties(frameworkProperties);
	    
	        bc = EclipseStarter.startup(new String[]{"-console"}, null);
    	}
		activator = new NewIpfProjectWizardActivator();		
		activator.start(bc);
    }
	
}
