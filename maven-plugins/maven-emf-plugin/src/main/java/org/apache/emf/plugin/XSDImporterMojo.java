package org.apache.emf.plugin;

/**
 * Copyright 2004, 2005 The Apache Software Foundation or its licensors, as applicable
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
import java.io.File;
import java.util.Map;

/**
 * @goal xsd2java
 */
public class XSDImporterMojo extends LaunchOSGIMojo {

	public static final String APPLICATION_ID = "org.eclipse.xsd.ecore.importer.XSD2GenModel";

	/**
	 * @parameter
	 */
	protected Map packageMap;

	/**
	 * @parameter
	 * 
	 */
	protected File schema;

	/**
	 * @parameter
	 * 
	 */
	protected File genModel;

	/**
	 * @parameter
	 * 
	 */
	protected File project;

	/**
	 * @parameter
	 * 
	 */
	protected String projectId;

	/**
	 * @parameter
	 * 
	 */
	protected String type;

	/* (non-Javadoc)
	 * @see org.apache.emf.plugin.LaunchOSGIMojo#getArguments()
	 */
	protected String[] getArguments() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.emf.plugin.LaunchOSGIMojo#getApplicationID()
	 */
	protected String getApplicationID() {
		return APPLICATION_ID;
	}

	protected StringBuffer processParameters() {
		StringBuffer buffer = new StringBuffer();
		return buffer;
	}

}
