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
package org.apache.geronimo.eclipse.devtools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public abstract class VelocityMojo extends AbstractMojo {

	/**
	 * @parameter
	 * @required
	 */
	private String templateFile;

	/**
	 * @parameter
	 * @required
	 */
	private File destFile;
	
	/**
	 * @parameter expression="${project}"
	 * @required
	 */
	private MavenProject project;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {

			Velocity.setProperty("file.resource.loader.path", project.getBasedir().getAbsolutePath());
			Velocity.init();

			VelocityContext context = new VelocityContext();
			configureContext(context);

			Template template = Velocity.getTemplate(templateFile);
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(destFile));
			template.merge(context, writer);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage());
		}
	}
	
	abstract public void configureContext(VelocityContext context);

}
