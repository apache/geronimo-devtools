/**
 * Copyright 2004, 2005 The Apache Software Foundation or its licensors, as
 * applicable
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.geronimo.eclipse.devtools;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.installer.ArtifactInstallationException;
import org.apache.maven.artifact.installer.ArtifactInstaller;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * @goal install
 */
public class InstallPluginDependenciesMojo extends AbstractMojo {

	private static final String GROUP_ID = "org.eclipse.plugins";

	/**
	 * @parameter expression="${project}"
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${eclipse.home}"
	 */
	private File eclipsehome;

	/**
	 * @parameter expression="${component.org.apache.maven.artifact.factory.ArtifactFactory}"
	 * @required
	 * @readonly
	 */
	private ArtifactFactory artifactFactory;

	/**
	 * @parameter expression="${component.org.apache.maven.artifact.installer.ArtifactInstaller}"
	 * @required
	 * @readonly
	 */
	protected ArtifactInstaller installer;

	/**
	 * @parameter expression="${localRepository}"
	 * @required
	 * @readonly
	 */
	protected ArtifactRepository localRepository;

	public InstallPluginDependenciesMojo() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {

		if (!isValid())
			throw new MojoFailureException("Eclipse home directory is not valid. "
					+ eclipsehome);

		File pluginsDir = new File(eclipsehome.getAbsolutePath().concat(File.separator
				+ "plugins"));
		
		processDependenciesOnly(pluginsDir);
	}

	protected void processDependenciesOnly(File pluginsDir) {
		List dependencies = project.getDependencies();
		Iterator i = dependencies.iterator();
		while (i.hasNext()) {
			Dependency dependency = (Dependency) i.next();
			if (GROUP_ID.equals(dependency.getGroupId())) {
				getLog().info("Eclipse dependency: " + dependency.toString());
				process(pluginsDir, 0, dependency.getArtifactId());
			}
		}
	}

	protected boolean isValid() {
		return eclipsehome != null && eclipsehome.isDirectory();
	}

	protected void process(File file, int depth, String artifactId) {
		getLog().debug("processFile() depth = " + Integer.toString(depth) + " "
				+ file.getAbsolutePath());
		if (file.isDirectory()) {
			depth++;
			File[] members = file.listFiles();
			for (int i = 0; i < members.length; i++) {
				process(members[i], depth, artifactId);
			}
			depth--;
		} else {
			if (shouldInstall(artifactId, file)) {
				install(file, depth);
			}
		}
	}

	protected boolean shouldInstall(String artifactId, File file) {
		if (!file.getName().endsWith(".jar"))
			return false;
		return artifactId == null || getBundleName(file).equals(artifactId);
	}

	protected void install(File file, int depth) {
		String artifactId;
		String version;
		if (depth > 1) {
			File bundleDir = getBundleDir(file, depth);
			artifactId = getBundleName(bundleDir) + "." + getArtifactID(file);
			version = getBundleVersion(bundleDir);
		} else {
			artifactId = getBundleName(file);
			version = getBundleVersion(file);
		}

		try {
			doIt(file, GROUP_ID, artifactId, version, "jar");
		} catch (MojoExecutionException e) {
			e.printStackTrace();
		} catch (MojoFailureException e) {
			e.printStackTrace();
		}
	}

	protected File getBundleDir(File file, int depth) {
		File pluginDir = file.getParentFile();
		for (int i = depth - 1; i > 1; i--) {
			pluginDir = pluginDir.getParentFile();
		}
		return pluginDir;
	}

	public static String getBundleName(File bundle) {
		String id = getArtifactID(bundle);
		if(id.indexOf("_") != -1)
			id = id.substring(0, id.indexOf("_"));
		return id;
	}

	public static String getBundleVersion(File bundle) {
		String id = getArtifactID(bundle);
		return id.substring(id.indexOf("_") + 1, id.length());
	}

	public static String getArtifactID(File file) {
		String name = file.getName();
		if (file.isFile())
			name = name.substring(0, name.lastIndexOf(".jar"));
		return name;
	}

	private void doIt(File file, String groupId, String artifactId,
			String version, String packaging) throws MojoExecutionException,
			MojoFailureException {
		
		Artifact artifact = artifactFactory.createArtifact(groupId, artifactId, version, null, packaging);

		try {
			String localPath = localRepository.pathOf(artifact);
			File destination = new File(localRepository.getBasedir(), localPath);
			
			if(destination.exists()) {
				getLog().info(artifactId + " " + version + " already exists in local repository.");
				return;
			}
			
			if (!file.getPath().equals(destination.getPath())) {
				installer.install(file, artifact, localRepository);
			} else {
				throw new MojoFailureException("Cannot install artifact. Artifact is already in the local repository.\n\nFile in question is: "
						+ file + "\n");
			}
		} catch (ArtifactInstallationException e) {
			throw new MojoExecutionException("Error installing artifact '"
					+ artifact.getDependencyConflictId() + "': "
					+ e.getMessage(), e);
		}
	}

}
