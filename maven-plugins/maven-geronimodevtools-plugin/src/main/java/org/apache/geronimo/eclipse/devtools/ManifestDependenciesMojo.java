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
package org.apache.geronimo.eclipse.devtools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

/**
 * @goal manifestbundles
 * @description Adds dependencies from plugin manfiest at build time
 *
 * @version $Rev$ $Date$
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
	 * @parameter expression="${component.org.apache.maven.artifact.factory.ArtifactFactory}"
	 * @required
	 * @readonly
	 */
	private ArtifactFactory artifactFactory;

	/**
	 * @parameter expression="${localRepository}"
	 * @required
	 * @readonly
	 */
	protected ArtifactRepository localRepository;

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

		getLog().debug("Bundle Entries: " + bundleEntries.toString());
		getLog().debug("Exported Entries: " + exportedEntries.toString());

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

	private void addRequiredBundles(Manifest manifest, boolean exportedEntriesOnly) {
		String requiredBundles = getRequiredBundles(manifest);
		try {
			ManifestElement[] elements = ManifestElement.parseHeader(Constants.REQUIRE_BUNDLE, requiredBundles);
			if (elements != null) {
				for (int i = 0; i < elements.length; i++) {
					ManifestElement element = elements[i];
					String bundleId = element.getValue();
					if (exportedEntriesOnly) {
						String visibility = element.getDirective(Constants.VISIBILITY_DIRECTIVE);
						if (Constants.VISIBILITY_REEXPORT.equals(visibility)) {
							exportedEntries.add(bundleId);
						}
					} else {
						bundleEntries.add(bundleId);
					}
				}
			}
		} catch (BundleException e) {
			e.printStackTrace();
		}
	}

	private String getRequiredBundles(Manifest manifest) {
		return manifest.getMainAttributes().getValue(Constants.REQUIRE_BUNDLE);
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
				// check POM dependencies
				Iterator i = project.getDependencies().iterator();
				while (i.hasNext()) {
					Dependency dependency = (Dependency) i.next();
					if (dependency.getArtifactId().equals(bundleId)) {
						Artifact artifact = artifactFactory.createArtifact(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), null, "jar");
						File file = new File(localRepository.getBasedir(), localRepository.pathOf(artifact));
						manifest = new JarFile(file).getManifest();
						break;
					}
				}
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
