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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * @requiresDependencyResolution
 */
public abstract class PackagingMojo extends AbstractMojo {
	
	/**
	 * @parameter expression="${project}"
	 * @required
	 */
	protected MavenProject project;

	/**
	 * @parameter expression="${project.dependencies}
	 * @required
	 * @readonly
	 */
	private Collection dependencies;

	/** 
	 * @parameter expression="${project.build.outputDirectory}/plugins"
	 * @required
	 */
	private File pluginsDestination;

	/**
	 * @parameter expression="${project.build.outputDirectory}/features"
	 * @required
	 */
	private File featuresDestination;

	/* (non-Javadoc)
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		Set artifacts = project.getDependencyArtifacts();
		Iterator i = artifacts.iterator();
		while (i.hasNext()) {
			Artifact artifact = (Artifact) i.next();
			if (isDirectDependency(artifact)) {
				try {
					URL[] urls = { artifact.getFile().toURL() };
					URLClassLoader cl = new URLClassLoader(urls);
					if (cl.findResource("feature.xml") != null) {
						processFeature(artifact);
					} else {
						processPlugin(artifact);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	abstract void processPlugin(Artifact artifact) throws Exception;
	
	abstract void processFeature(Artifact artifact) throws Exception;
	
	protected boolean isDirectDependency(Artifact artifact) {
		Iterator iterator = dependencies.iterator();
		while (iterator.hasNext()) {
			Dependency dependency = (Dependency) iterator.next();
			if (dependency.getGroupId().equals(artifact.getGroupId())
					&& dependency.getArtifactId().equals(artifact.getArtifactId())) {
				return true;
			}
		}
		return false;
	}
	
	public static File getDestinationFile(Artifact artifact, File toDir) {
		String fileName = artifact.getFile().getName().replaceFirst("-", "_");
		return new File(toDir + File.separator + fileName);
	}

	public File getFeaturesDestination() {
		return featuresDestination;
	}

	public File getPluginsDestination() {
		return pluginsDestination;
	}

}
