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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Get;

/**
 * @goal getg
 */
public class GetGMojo extends AbstractMojo {

	/**
	 * @parameter expression="http://www.apache.org/dist/geronimo"
	 */
	private URL siteRoot;

	/**
	 * @parameter expression="${settings.localRepository}"
	 */
	private String localRepo;

	/**
	 * @parameter expression="${project.build.outputDirectory}/zips"
	 */
	private File target;

	/**
	 * @parameter
	 * @required
	 */
	private String distribution;

	/**
	 * @parameter
	 * @required
	 */
	private String version;

	/**
	 * @parameter expression="zip"
	 */
	private String type;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		String fileName = distribution + "-" + version + "." + type;
		File gDistro = new File(localRepo + File.separator + "geronimo"
				+ File.separator + "distributions" + File.separator + fileName);
		
		gDistro.getParentFile().mkdirs();

		if (!gDistro.exists()) {
			try {
				URL gUrl = new URL(siteRoot.toExternalForm() + "/" + version
						+ "/" + fileName);
				Get getTask = new Get();
				getTask.setProject(new Project());
				getTask.setDest(gDistro);
				getTask.setSrc(gUrl);
				getLog().info("Downloading " + fileName + "...");
				getTask.execute();
			} catch (MalformedURLException e) {
				throw new MojoFailureException(e.getMessage());
			}
		} 
		
		Copy copyTask = new Copy();
		copyTask.setProject(new Project());
		copyTask.setFile(gDistro);
		copyTask.setOverwrite(false);
		copyTask.setTodir(target);
		copyTask.execute();
	}
}
