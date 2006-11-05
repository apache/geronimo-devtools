/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.emf.plugin;

import java.io.File;

import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal generate
 *
 * @version $Rev$ $Date$
 */
public class GeneratorMojo extends LaunchOSGIMojo {

	public static final String APPLICATION_ID = "org.eclipse.emf.codegen.ecore.Generator";
	
	public static final String GOAL_NAME = "generate";

	/**
	 * @parameter
	 */
	private File projectRootDirectory;

	/**
	 * @parameter
	 * @required
	 */
	private File genmodel;

	/**
	 * @parameter
	 * @required
	 */
	private String type;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.emf.plugin.LaunchOSGIMojo#getApplicationID()
	 */
	protected String getApplicationID() {
		return APPLICATION_ID;
	}
	
	protected String getGoalName() {
		return GOAL_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.emf.plugin.LaunchOSGIMojo#getArguments()
	 */
	protected String[] getArguments() {
		return getArguments(processParameters());
	}

	protected StringBuffer processParameters() {
		StringBuffer buffer = new StringBuffer();
		if (projectRootDirectory != null) {
			buffer.append("-projects").append(SPACE).append(projectRootDirectory.getAbsolutePath()).append(SPACE);
		}

		buffer.append("-").append(type).append(SPACE);
        buffer.append("-autoBuild").append(SPACE).append("false").append(SPACE);
		buffer.append(genmodel.getAbsolutePath()).append(SPACE);

		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.emf.plugin.LaunchOSGIMojo#validate()
	 */
	protected void validate() throws MojoFailureException {
		if (!("model".equalsIgnoreCase(type) || "edit".equalsIgnoreCase(type) || "editor".equalsIgnoreCase(type))) {
			throw new MojoFailureException("<type> must be set to either 'model', 'edit', or 'editor'");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.emf.plugin.LaunchOSGIMojo#cleanup()
	 */
	protected void cleanup() {
		//FIXME workaround for bugzilla 147936
		new File(mavenProject.getBasedir().getAbsolutePath() + File.separator + ".classpath").delete();
	}
}
