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
package org.openehealth.ipf.tools.ide.wizard.util.test;

import org.openehealth.ipf.tools.ide.wizard.util.PluginUtils;

import junit.framework.TestCase;

/**
 * 
 * @author Boris Stanojevic 
 *
 */
public class PluginUtilsTest extends TestCase{

	public void testProviderValidator(){
		String provider = PluginUtils.getValidProvider("");
		assertNotNull(provider);
		assertEquals("", provider);
		
		provider = PluginUtils.getValidProvider("com");
		assertNotNull(provider);
		assertEquals("", provider);
		
		provider = PluginUtils.getValidProvider("com.abc.test");
		assertNotNull(provider);
		assertEquals("ABC", provider);
		
		provider = PluginUtils.getValidProvider("com.bca.def.test");
		assertNotNull(provider);
		assertEquals("BCA", provider);
	}
	
	public void testNameValidator(){
		String name = PluginUtils.getValidName("", "");
		assertNotNull(name);
		assertEquals("", name);
		
		name = PluginUtils.getValidName("abc", "name");
		assertNotNull(name);
		assertEquals("Abc name", name);
	}
	
	public void testIDv3_0Validator(){
		boolean valid = PluginUtils.isValidCompositeID3_0("");
		assertFalse(valid);
		
		valid = PluginUtils.isValidCompositeID3_0("com.test.abc");
		assertTrue(valid);
		
		valid = PluginUtils.isValidCompositeID3_0("com.test/abc");
		assertFalse(valid);
		
		valid = PluginUtils.isValidCompositeID3_0("COM.test-abc");
		assertTrue(valid);
		
		valid = PluginUtils.isValidCompositeID3_0("com.test&abc");
		assertFalse(valid);
	}
}
