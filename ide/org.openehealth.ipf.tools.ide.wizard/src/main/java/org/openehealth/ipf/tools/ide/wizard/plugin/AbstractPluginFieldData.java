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

import org.eclipse.pde.ui.IFieldData;

/**
 * Data Object used to provide information of captured data in the 
 * 'New IPF Project' wizard pages as entered by the user.
 * 
 * @see org.eclipse.pde.ui.IFieldData
 * 
 * @author Boris Stanojevic
 */
public class AbstractPluginFieldData implements IFieldData{
	
	private String fId;
	private String fVersion;
	private String fName;
	private String fProvider;
	private boolean fLegacy;
	private String fLibraryName;
	private String fSourceFolderName;
	private String fOutputFolderName;
	private boolean fHasBundleStructure;
	private boolean fSimple;
	
	/**
	 * @see org.eclipse.pde.ui.IFieldData#getId()
	 */
	@Override
	public String getId() {
		return fId;
	}
	
	public void setId(String id) {
		this.fId = id;
	}

   /**
     * @see org.eclipse.pde.ui.IFieldData#getLibraryName()
     */
	@Override
	public String getLibraryName() {
		return fLibraryName;
	}
	
	public void setLibraryName(String libraryName) {
		this.fLibraryName = libraryName;
	}

   /**
     * @see org.eclipse.pde.ui.IFieldData#getName()
     */	
	@Override
	public String getName() {
		return fName;
	}
	
	public void setName(String name) {
		this.fName = name;
	}

   /**
     * @see org.eclipse.pde.ui.IFieldData#getOutputFolderName()
     */
	@Override
	public String getOutputFolderName() {
		return fOutputFolderName;
	}
	
	public void setOutputFolderName(String outputFolderName) {
		this.fOutputFolderName = outputFolderName;
	}

   /**
     * @see org.eclipse.pde.ui.IFieldData#getProvider()
     */
	@Override
	public String getProvider() {
		return fProvider;
	}
	
	public void setProvider(String provider) {
		this.fProvider = provider;
	}

   /**
     * @see org.eclipse.pde.ui.IFieldData#getSourceFolderName()
     */
	@Override
	public String getSourceFolderName() {
		return fSourceFolderName;
	}
	
	public void setSourceFolderName(String sourceFolderName) {
		this.fSourceFolderName = sourceFolderName;
	}

   /**
     * @see org.eclipse.pde.ui.IFieldData#getVersion()
     */
	@Override
	public String getVersion() {
		return fVersion;
	}
	
	public void setVersion(String version) {
		this.fVersion = version;
	}

   /**
     * @see org.eclipse.pde.ui.IFieldData#hasBundleStructure()
     */
	@Override
	public boolean hasBundleStructure() {
		return fHasBundleStructure;
	}
	
	public void setBundleStructure(boolean hasBundleStructure) {
		this.fHasBundleStructure = hasBundleStructure;
	}

   /**
     * @see org.eclipse.pde.ui.IFieldData#isLegacy()
     */
	@Override
	public boolean isLegacy() {
		return fLegacy;
	}
	
	public void setLegacy(boolean legacy) {
		this.fLegacy = legacy;
	}

   /**
     * @see org.eclipse.pde.ui.IFieldData#isSimple()
     */
	@Override
	public boolean isSimple() {
		return fSimple;
	}

	public void setSimple(boolean simple) {
		this.fSimple = simple;
	}

}
