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
package org.openehealth.ipf.tools.ide.launch.osgi.test;

import java.util.Properties;

import junit.framework.TestCase;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.openehealth.ipf.tools.ide.launch.osgi.IpfLaunchActivator;
import org.openehealth.ipf.tools.ide.launch.test.Activator;
import org.osgi.framework.BundleContext;
/**
 * 
 * @author Boris Stanojevic 
 *
 */
@SuppressWarnings("restriction")
public class IPFLaunchActivatorTest extends TestCase{
	
	public void testStartAndStop() throws Exception{
		startLaunchActivator();
		
		Properties props = IpfLaunchActivator.getDefault().getProperties();
		assertNotNull(props);
		assertNotNull(props.getProperty("launch.bundles"));
		assertTrue(props.getProperty("launch.bundles")
				.contains("org.apache.camel.camel-jetty@default:true"));
		assertTrue(props.getProperty("launch.bundles")
				.contains("org.openehealth.ipf.osgi.osgi-config-log@default"));
		
		assertNotNull(props.getProperty("launch.vmargs"));
		assertTrue(props.getProperty("launch.vmargs")
				.contains("-Declipse.ignoreApp=true"));

	}
	
	public void startLaunchActivator() throws Exception{
    	BundleContext bc = null;
    	IpfLaunchActivator activator = null;
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
		activator = new IpfLaunchActivator();		
		activator.start(bc);    	
    }
}
