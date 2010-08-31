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
package org.openehealth.ipf.tools.ide.wizard.util;

import java.util.StringTokenizer;

/**
 * Validation Utilities of required Bundle informations
 * 
 * @author Boris Stanojevic
 */
public class PluginUtils {

    /**
     * composes a valid Bundle Name based
     * on a Bundle ID value
     * 
     * @param id provided Bundle ID
     * @param nameFieldQualifier additional name qualifier
     * @return valid Bundle name
     */
	public static String getValidName(String id, String nameFieldQualifier) {
		StringTokenizer tok = new StringTokenizer(id, "\\.");
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if (!tok.hasMoreTokens()) {
				String name = Character.toUpperCase(token.charAt(0)) + ((token.length() > 1) ? token.substring(1) : "");
				return name + " " + nameFieldQualifier;
			}
		}
		return "";
	}
	
	/**
	 * only if id begins with "com." it composes the
	 * provider name from the next token, otherwise
	 * return empty string.
	 * 
	 * @param id provided Bundle ID
	 * @return valid bundle provider name
	 */
	public static String getValidProvider(String id) {
		StringTokenizer tok = new StringTokenizer(id, ".");
		int count = tok.countTokens();
		if (count > 2 && tok.nextToken().equals("com"))
			return tok.nextToken().toUpperCase();
		return "";
	}
	
	/**
	 * validates the provided bundle ID 
	 * 
	 * @param name bundle ID
	 * @return false if validation failed otherwise true
	 */
	public static boolean isValidCompositeID3_0(String name) {
		if (name.length() <= 0) {
			return false;
		}
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if ((c < 'A' || 'Z' < c)
					&& (c < 'a' || 'z' < c)
					&& (c < '0' || '9' < c)
					&& c != '_' && c != '-') {
				if (i == 0 || i == name.length() - 1 || c != '.') {
					return false;
				}
			}
		}
		return true;
	}
	
}
