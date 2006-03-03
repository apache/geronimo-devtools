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
import java.util.StringTokenizer;

/**
 * @goal xsd2java
 */
public class XSDImporterMojo extends LaunchOSGIMojo {

	public static final String APPLICATION_ID = "org.eclipse.xsd.ecore.importer.XSD2GenModel";

	/**
	 * @parameter
	 */
	protected Map packagemap;

	/**
	 * @parameter
	 */
	protected String[] packages;

	/**
	 * @parameter
	 * @required
	 */
	protected File schema;

	/**
	 * @parameter
	 * @required
	 */
	protected File genmodel;

	/**
	 * @parameter
	 * @required
	 */
	protected String type;

	protected String projectId;

	public static final String SPACE = " ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.emf.plugin.LaunchOSGIMojo#getArguments()
	 */
	protected String[] getArguments() {
		String params = processParameters().toString();
		getLog().info(params.toString());
		StringTokenizer st = new StringTokenizer(params);
		String[] args = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			args[i++] = st.nextToken();
		}
		return args;
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
		buffer.append(schema.getAbsolutePath() + SPACE);
		buffer.append(genmodel.getAbsolutePath() + SPACE);
		if ("model".equals(type)) {
			buffer.append("-modelProject");
		} else if ("edit".equals(type)) {
			buffer.append("-editProject");
		} else if ("editor".equals(type)) {
			buffer.append("-editorProject");
		}
		buffer.append(SPACE);
		buffer.append(project.getBasedir() + SPACE);
		buffer.append(getRelativeSrcDir() + SPACE);
		if (packages != null) {
			buffer.append("-packages" + SPACE);
			for (int i = 0; i < packages.length; i++) {
				buffer.append(packages[i] + SPACE);
			}
		}

		if (packagemap != null) {
			buffer.append("-packagemap" + SPACE);
			Set keys = packagemap.keySet();
			for (Iterator i = keys.iterator(); i.hasNext();) {
				String pkg = (String) i.next();
				String mapping = (String) packagemap.get(pkg);
				buffer.append(pkg + SPACE + mapping + SPACE);
			}
		}
		return buffer;
	}
	
	private String getRelativeSrcDir() {
		String src = project.getBuild().getSourceDirectory();
		src = src.split(project.getBasedir().getAbsolutePath())[1];
		if(src.startsWith(File.separator))
			src = src.substring(1);
		return src;
	}

}
