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
import java.util.Iterator;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Move;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;

/**
 * @goal importresources
 * @requiresDependencyResolution
 */
public class ImportResourcesMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @required
	 */
	private MavenProject project;

	/**
	 * @parameter
	 * @required
	 */
	private File target;

	/**
	 * @parameter expression="true"
	 */
	private boolean flatten;

	/**
	 * @parameter
	 */
	private String[] includes;

	private File importDestination;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {

		Set dependencies = project.getDependencyArtifacts();
		Iterator i = dependencies.iterator();

		if (dependencies.size() > 0) {
			
			getLog().debug("ImportDestination: " + getImportDestination());
			getLog().debug("TargetDestination: " + target);
			getLog().debug("Flatten: " + flatten);
			
			while (i.hasNext()) {
				Artifact artifact = (Artifact) i.next();
				Expand expandTask = new Expand();
				expandTask.setProject(new Project());
				expandTask.setSrc(artifact.getFile());
				expandTask.setDest(getImportDestination());
				if (includes != null) {
					PatternSet set = new PatternSet();
					for (int j = 0; j < includes.length; j++) {
						set.setIncludes(includes[j]);
					}
					expandTask.addPatternset(set);
				}
				getLog().info("Importing specified resources from "
						+ artifact.getGroupId() + ":"
						+ artifact.getArtifactId());
				expandTask.execute();
			}

			if (flatten) {
				flatten();
				cleanup();
			}
		}
	}

	private void flatten() {
		Move moveTask = new Move();
		moveTask.setProject(new Project());
		moveTask.setTodir(target);
		moveTask.setFlatten(true);

		if (includes != null) {
			FileSet fileSet = new FileSet();
			fileSet.setDir(getImportDestination());
			for (int j = 0; j < includes.length; j++) {
				fileSet.setIncludes(includes[j]);
			}
			moveTask.addFileset(fileSet);
		}

		moveTask.execute();
	}

	private void cleanup() {
		Delete deleteTask = new Delete();
		deleteTask.setProject(new Project());
		deleteTask.setDir(getImportDestination());
		deleteTask.execute();
	}

	private File getImportDestination() {
		if (importDestination == null) {
			String base = project.getBasedir().getAbsolutePath();
			if (flatten) {
				importDestination = new File(base + File.separator + "temp");
			} else {
				importDestination = target;
			}
		}
		return importDestination;
	}
}
