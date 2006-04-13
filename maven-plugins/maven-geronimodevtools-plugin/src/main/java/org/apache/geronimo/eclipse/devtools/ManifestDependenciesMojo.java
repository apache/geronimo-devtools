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
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.Manifest;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * @goal manifestbundles
 * @description Adds dependencies from plugin manfiest at build time
 */
public class ManifestDependenciesMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @required
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${project.basedir}/META-INF/MANIFEST.MF"
	 * @required
	 */
	private File manifestFile;

	/**
	 * @parameter expression="true"
	 */
	private boolean excludePOMDependencies;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			FileInputStream fis = new FileInputStream(manifestFile);
			Manifest manifest = new Manifest();
			manifest.read(fis);
			fis.close();
			String bundles = manifest.getMainAttributes().getValue("Require-Bundle");

			StringTokenizer st = new StringTokenizer(bundles);
			List bundleEntries = new ArrayList();
			while (st.hasMoreTokens()) {
				String bundle = st.nextToken(",");
				int extraInfoIndex = bundle.indexOf(";");
				if(extraInfoIndex != -1)
					bundle = bundle.substring(0, extraInfoIndex);
				bundleEntries.add(bundle);
			}

			List excludeList = new ArrayList();
			if (excludePOMDependencies) {
				Iterator i = project.getDependencies().iterator();
				while (i.hasNext())
					excludeList.add(((Dependency) i.next()).getArtifactId());
			}

			Iterator i = bundleEntries.iterator();
			while (i.hasNext()) {
				String artifactId = (String) i.next();
				if (!excludeList.contains(artifactId)) {
					Dependency dependency = createDependency(artifactId);
					project.getDependencies().add(dependency);
				}
			}
		} catch (Exception e) {
			throw new MojoFailureException(e.getMessage());
		}
	}

	private Dependency createDependency(String artifactId) {
		Dependency dependency = new Dependency();
		dependency.setGroupId("org.eclipse.plugins");
		dependency.setArtifactId(artifactId);
		return dependency;
	}

}
