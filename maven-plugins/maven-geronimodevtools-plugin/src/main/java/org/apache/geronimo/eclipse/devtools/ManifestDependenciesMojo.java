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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
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

	public static String MANIFEST_REXPORT_DEPENDENCY = "visibility:=reexport";

	public static String MANIFEST_PATH = "META-INF" + File.separator
			+ "MANIFEST.MF";

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

	/**
	 * @parameter expression="${eclipseHome}"
	 */
	private File eclipseHome;

	private Set bundleEntries = new HashSet();
	private Set exportedEntries = new HashSet();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {

		addRequiredBundles(getManifestFromFile(manifestFile), false);

		Iterator i = bundleEntries.iterator();
		while (i.hasNext()) {
			addExportedBundles((String) i.next());
		}

		bundleEntries.addAll(exportedEntries);

		List excludeList = new ArrayList();
		if (excludePOMDependencies) {
			i = project.getDependencies().iterator();
			while (i.hasNext())
				excludeList.add(((Dependency) i.next()).getArtifactId());
		}

		i = bundleEntries.iterator();
		while (i.hasNext()) {
			String artifactId = (String) i.next();
			if (!excludeList.contains(artifactId)) {
				project.getDependencies().add(createDependency(artifactId));
			}
		}
	}

	private void addRequiredBundles(Manifest manifest,
			boolean exportedEntriesOnly) {
		String requiredBundles = getRequiredBundles(manifest);
		if (requiredBundles != null) {
			StringTokenizer st = new StringTokenizer(requiredBundles);
			while (st.hasMoreTokens()) {
				String nextToken = st.nextToken(",");
				String bundleId = getBundleId(nextToken);
				if (exportedEntriesOnly) {
					if (export(nextToken)
							&& !exportedEntries.contains(bundleId)) {
						getLog().info("Adding exported bundle entry: "
								+ bundleId);
						exportedEntries.add(bundleId);
					}
				} else if (!bundleEntries.contains(bundleId)) {
					getLog().info("Adding required bundle entry: " + bundleId);
					bundleEntries.add(bundleId);
				}
			}
		}
	}

	private String getRequiredBundles(Manifest manifest) {
		return manifest.getMainAttributes().getValue("Require-Bundle");
	}

	private Manifest getManifestFromFile(File file) {
		Manifest manifest = new Manifest();
		try {
			FileInputStream fis = new FileInputStream(manifestFile);
			manifest.read(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return manifest;
	}

	private String getBundleId(String element) {
		String[] entry = element.split(";");
		return entry[0].trim();
	}

	private boolean export(String element) {
		String[] entry = element.split(";");
		return entry.length > 1
				&& entry[1].trim().equals(MANIFEST_REXPORT_DEPENDENCY);
	}

	private void addExportedBundles(String bundleId) {
		File bundle = findBundleForDependency(bundleId);
		Manifest manifest = null;
		try {
			if (bundle != null) {
				if (bundle.isFile()) {
					manifest = new JarFile(bundle).getManifest();
				} else {
					File manifestFile = new File(bundle + File.separator
							+ MANIFEST_PATH);
					manifest = getManifestFromFile(manifestFile);
				}
			} else {
				// check exclude list
			}
			if (manifest != null) {
				addRequiredBundles(manifest, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Dependency createDependency(String artifactId) {
		Dependency dependency = new Dependency();
		dependency.setGroupId("org.eclipse.plugins");
		dependency.setArtifactId(artifactId);
		return dependency;
	}

	private File findBundleForDependency(String artifactId) {
		File pluginsDir = new File(eclipseHome + File.separator + "plugins");
		File[] members = pluginsDir.listFiles();
		for (int i = 0; i < members.length; i++) {
			String bundleName = InstallPluginDependenciesMojo.getBundleName(members[i]);
			if (artifactId.equals(bundleName))
				return members[i];
		}
		return null;
	}
}
