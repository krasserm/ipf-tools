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
package org.openehealth.ipf.tools.ide.wizard.plugin.test;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.openehealth.ipf.tools.ide.wizard.plugin.PluginFieldData;

import junit.framework.TestCase;

/**
 * 
 * @author Boris Stanojevic
 *
 */
public class PluginFieldDataTest extends TestCase{

	public void testDefaultSourcePath(){
		PluginFieldData data = new PluginFieldData();
		List<IPath> list = data.getSourcePaths();
		assertEquals(6, list.size());
		IPath path = list.get(0);
		String delimiter = System.getProperty("file.separator");
		assertEquals("src" + delimiter + "main" + delimiter + "java" + delimiter, path.toOSString());
	}
	
	public void testDefaultTestRouteName(){
		PluginFieldData data = new PluginFieldData();
		String route = data.getTestRouteName();
		assertNotNull(route);
		assertEquals("Test.java", route);
		
		data.setRouteBuilderName("MyBuilder.groovy");
		
		route = data.getTestRouteName();
		assertNotNull(route);
		assertEquals("MyBuilderTest.java", route);
	}
	
}
