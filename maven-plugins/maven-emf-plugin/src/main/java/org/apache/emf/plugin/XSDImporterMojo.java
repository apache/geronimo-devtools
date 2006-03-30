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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

/**
 * @goal xsd2genmodel
 */
public class XSDImporterMojo extends LaunchOSGIMojo {

	public static final String APPLICATION_ID = "org.eclipse.xsd.ecore.importer.XSD2GenModel";

	/**
	 * @parameter
	 * @required
	 */
	protected File[] schemas;

	/**
	 * @parameter
	 * @required
	 */
	protected File genmodel;

	/**
	 * @parameter
	 */
	protected File modelProject;

	/**
	 * @parameter
	 */
	protected File editProject;

	/**
	 * @parameter
	 */
	protected File editorProject;

	/**
	 * @parameter
	 * @required
	 */
	protected String src;

	/**
	 * @parameter
	 */
	protected String[] packages;

	/**
	 * @parameter
	 */
	protected Map packagemap;

	/**
	 * @paramter expression="false"
	 */
	protected boolean reload;

	protected Artifact[] vmargs;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.emf.plugin.LaunchOSGIMojo#getArguments()
	 */
	protected String[] getArguments() {
		return getArguments(processParameters());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.emf.plugin.LaunchOSGIMojo#getApplicationID()
	 */
	protected String getApplicationID() {
		return APPLICATION_ID;
	}

	protected StringBuffer processParameters() {
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < schemas.length; i++) {
			buffer.append(schemas[i].getAbsolutePath()).append(SPACE);
		}

		buffer.append(genmodel.getAbsolutePath()).append(SPACE);
		if (reload)
			buffer.append("-reload").append(SPACE);

		if (modelProject != null)
			buffer.append("-modelProject").append(SPACE).append(modelProject).append(SPACE).append(src).append(SPACE);
		if (editProject != null)
			buffer.append("-editProject").append(SPACE).append(editProject).append(SPACE).append(src).append(SPACE);
		if (editorProject != null)
			buffer.append("-editorProject").append(SPACE).append(editorProject).append(SPACE).append(src).append(SPACE);

		if (packages != null) {
			buffer.append("-packages").append(SPACE);
			for (int i = 0; i < packages.length; i++) {
				buffer.append(packages[i]).append(SPACE);
			}
		}

		if (packagemap != null) {
			buffer.append("-packagemap").append(SPACE);
			Set keys = packagemap.keySet();
			for (Iterator i = keys.iterator(); i.hasNext();) {
				String mapping = (String) i.next(); // org.apache...
				String pkg = (String) packagemap.get(mapping); // http://...
				buffer.append(pkg).append(SPACE).append(mapping).append(SPACE);
			}
		}
		return buffer;
	}
}
