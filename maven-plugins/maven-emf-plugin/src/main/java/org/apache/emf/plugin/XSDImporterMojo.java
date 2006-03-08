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
	 * @required
	 */
	protected String type;

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
		
		for(int i = 0; i < schemas.length; i++) {
			buffer.append(schemas[i].getAbsolutePath()).append(SPACE);
		}
	
		buffer.append(genmodel.getAbsolutePath()).append(SPACE);
		if(reload)
			buffer.append("-reload").append(SPACE);
		
		if ("model".equals(type)) {
			buffer.append("-modelProject");
		} else if ("edit".equals(type)) {
			buffer.append("-editProject");
		} else if ("editor".equals(type)) {
			buffer.append("-editorProject");
		}
		buffer.append(SPACE);
		buffer.append(project.getBasedir()).append(SPACE);
		buffer.append(getRelativeSrcDir()).append(SPACE);
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

	private String getRelativeSrcDir() {
		String src = project.getBuild().getSourceDirectory();
		src = src.split(project.getBasedir().getAbsolutePath())[1];
		if (src.startsWith(File.separator))
			src = src.substring(1);
		return src;
	}

}
