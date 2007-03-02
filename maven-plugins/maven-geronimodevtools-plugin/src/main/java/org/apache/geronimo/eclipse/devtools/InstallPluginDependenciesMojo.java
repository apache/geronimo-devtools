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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.installer.ArtifactInstallationException;
import org.apache.maven.artifact.installer.ArtifactInstaller;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.artifact.ProjectArtifactMetadata;
import org.codehaus.plexus.util.IOUtil;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.Constants;

import sun.util.BuddhistCalendar;

/**
 * This maven plugin installs to the local maven repository eclipse plugin
 * dependencies for a pom from an eclipse distribution.
 *
 * Plugins dependencies are defined with the "org.eclipse.plugins" groupId.
 *
 * The artifactId is the bundle id. If the bundle is a directory, then all jars
 * inside the bundle will be installed. The bundle id can be appendend with "." +
 * the name of the jar inside the bundle, excluding the ".jar" extension in
 * order to explicitly define a jar dependency.
 *
 * @goal install
 *
 * @version $Rev$ $Date$
 */
public class InstallPluginDependenciesMojo extends AbstractMojo {

	public static final String GROUP_ID = "org.eclipse.plugins";

	/**
	 * @parameter expression="${project}"
	 */
	private MavenProject project;

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

	/**
	 * @parameter expression="${eclipseHome}"
	 */
	private File eclipseHome;

	/**
	 * @parameter expression="${useDistributionVersions}"
	 */
	protected boolean useDistributionVersion = true;

	private List removeList = new ArrayList();

	private List addList = new ArrayList();

	private int depth = 0;

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
			throw new MojoFailureException("Eclipse home directory is not valid. " + eclipseHome);

		processDependencies();
		project.getDependencies().removeAll(removeList);
		project.getDependencies().addAll(addList);
	}

	protected void processDependencies() {
		List dependencies = project.getDependencies();
		Iterator i = dependencies.iterator();
		while (i.hasNext()) {
			Dependency dependency = (Dependency) i.next();
			if (GROUP_ID.equals(dependency.getGroupId())) {
				getLog().debug("\n");
				getLog().debug("========== Processing Plug-in Dependency: " + dependency.getArtifactId() + "==========");
				if ("org.eclipse.swt".equals(dependency.getArtifactId())) {
					Dependency fragment = getSWTFragmentDependency(dependency);
					processDependency(fragment);
					addList.add(fragment);
				}
				processDependency(dependency);
			}
		}
	}

	private void processDependency(Dependency dependency) {
		File bundle = findBundleForDependency(dependency);
		getLog().debug("Matching bundle: " + bundle);
		if (bundle != null) {
			process(bundle, dependency);
			if (bundle.isDirectory() && getBundleName(bundle).equals(dependency.getArtifactId())) {
				getLog().info("Removing bundle directory dependency: " + dependency.getArtifactId());
				removeList.add(dependency);
			}
		} else {
			getLog().error("Bundle for dependency not found: " + dependency.getArtifactId());
		}
	}

	private Dependency getSWTFragmentDependency(Dependency swtDependency) {
		Dependency fragment = new Dependency();
		fragment.setGroupId(swtDependency.getGroupId());
		fragment.setType(swtDependency.getType());
		fragment.setVersion(swtDependency.getVersion());
		String id = swtDependency.getArtifactId();
		String platform = System.getProperty("os.name");
		String arch = System.getProperty("os.arch");
		if (platform.startsWith("Windows")) {
			fragment.setArtifactId(id.concat(".win32.win32.x86"));
		} else if (platform.startsWith("Linux")) {
			if (arch.equalsIgnoreCase("x86_64")
					|| arch.equalsIgnoreCase("amd64"))
				fragment.setArtifactId(id.concat(".gtk.linux.x86_64"));
			else if (arch.startsWith("ppc"))
				fragment.setArtifactId(id.concat(".gtk.linux.ppc"));
			else
				fragment.setArtifactId(id.concat(".gtk.linux.x86"));
		} else if (platform.startsWith("Mac")) {
			fragment.setArtifactId(id.concat(".carbon.macosx"));
		} else if (platform.startsWith("SunOS")) {
			if (arch.startsWith("x86") || arch.startsWith("amd"))
				fragment.setArtifactId(id.concat(".gtk.solaris.x86"));
			else
				fragment.setArtifactId(id.concat(".gtk.solaris.sparc"));
		} else if (platform.startsWith("AIX")) {
			fragment.setArtifactId(id.concat(".motif.aix.ppc"));
		}
		getLog().info("SWTFragment:  " + fragment.toString());
		return fragment;
	}

	protected boolean isValid() {
		return eclipseHome != null && eclipseHome.isDirectory();
	}

	private File findBundleForDependency(Dependency dependency) {
		File pluginsDir = new File(eclipseHome + File.separator + "plugins");
		File[] members = pluginsDir.listFiles();
		for (int i = 0; i < members.length; i++) {
			if (isBundleForDependency(dependency, members[i]))
				return members[i];
		}
		return null;
	}

	private boolean isBundleForDependency(Dependency dependency, File bundle) {
		String bundleName = getBundleName(bundle);
		boolean match = dependency.getArtifactId().equals(bundleName);
		if(match) {
			getLog().debug("Found match using bundle name: " + bundleName);
		}
		return match;
	}

	protected void process(File file, Dependency dependency) {
		if (file.isDirectory()) {
			depth++;
			File[] members = file.listFiles();
			for (int i = 0; i < members.length; i++) {
				process(members[i], dependency);
			}
			depth--;
		} else {
			if (file.getName().endsWith(".jar")) {
				File bundle = getBundle(file);
				install(file, bundle);
				if (useDistributionVersion)
					dependency.setVersion(getBundleVersion(bundle));
			}
		}
	}

	protected void install(File artifact, File bundle) {

		String artifactId = getArtifactID(artifact, bundle);
		String version = getBundleVersion(bundle);

		if (!useDistributionVersion)
			version = fixVersion(version);

		if (bundle.isDirectory())
			addList.add(createDependency(artifactId, version));

		try {
			doIt(artifact, GROUP_ID, artifactId, version, "jar");
		} catch (MojoExecutionException e) {
			e.printStackTrace();
		} catch (MojoFailureException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Converts eclipse qualifier convention to maven convention.
	 *
	 * major.minor.revision.qualifier is converted to major.minor.revision-build
	 * where build is the eclipse qualifier with all non-numeric characters
	 * removed.
	 *
	 * @param version
	 * @return
	 */
	public static String fixVersion(String version) {
		int qualifierIndex = version.indexOf(".", 5);
		if (qualifierIndex == -1)
			return version; // has no qualifier
		String eclipseQualifier = version.substring(qualifierIndex + 1);
		String newQualifier = eclipseQualifier.replaceAll("[^\\d]", "");
		return version.substring(0, qualifierIndex) + "-" + newQualifier;
	}

	protected File getBundle(File file) {
		File bundle = file;
		if (depth > 0) {
			bundle = file.getParentFile();
			for (int i = depth - 1; i > 0; i--) {
				bundle = bundle.getParentFile();
			}
		}
		return bundle;
	}

	public static String getBundleNameFromFileName(File bundle) {
		String id = removeJarExtension(bundle);
		if (id.lastIndexOf("_") != -1)
			id = id.substring(0, id.lastIndexOf("_"));
		return id;
	}
	
	public static String getBundleName(File bundle) {
		try {
			Manifest manifest = null;
			if(bundle.isFile()) {
				JarFile jar = new JarFile(bundle);
				manifest = jar.getManifest();
			} else {
				FileInputStream fis = new FileInputStream(new File(bundle, "META-INF/MANIFEST.MF"));
				manifest = new Manifest(fis);
				fis.close();
			}
			if(manifest != null) {
				String id = manifest.getMainAttributes().getValue(Constants.BUNDLE_SYMBOLICNAME);
				if(id != null) {
					ManifestElement[] elements = ManifestElement.parseHeader(Constants.BUNDLE_SYMBOLICNAME, id);
					if (elements != null) {
						return elements[0].getValue();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static String getBundleVersion(File bundle) {
		String id = removeJarExtension(bundle);
		return id.substring(id.indexOf("_") + 1, id.length());
	}

	public static String getArtifactID(File artifact, File bundle) {
		String artifactId = getBundleName(bundle);
		if (bundle.isDirectory())
			artifactId = artifactId + "." + removeJarExtension(artifact);
		return artifactId;
	}

	private void doIt(File file, String groupId, String artifactId, String version, String packaging) throws MojoExecutionException, MojoFailureException {
		getLog().debug("Installing " + file + ": " + groupId + " " + artifactId + " " + version + " ");
		
		Artifact artifact = artifactFactory.createArtifact(groupId, artifactId, version, null, packaging);
		generatePOM(artifact, groupId, artifactId, version);

		try {
			String localPath = localRepository.pathOf(artifact);
			File destination = new File(localRepository.getBasedir(), localPath);

			if (destination.exists()) {
				getLog().info(artifactId + " : " + version + " already exists in local repository.");
				return;
			}

			if (!file.getPath().equals(destination.getPath())) {
				installer.install(file, artifact, localRepository);
			} else {
				throw new MojoFailureException("Cannot install artifact. Artifact is already in the local repository.\n\nFile in question is: " + file + "\n");
			}
		} catch (ArtifactInstallationException e) {
			throw new MojoExecutionException("Error installing artifact '" + artifact.getDependencyConflictId() + "': " + e.getMessage(), e);
		}
	}

	private void generatePOM(Artifact artifact, String groupId, String artifactId, String version) throws MojoExecutionException {

		FileWriter fw = null;
		try {
			File tempFile = File.createTempFile("mvninstall", ".pom");
			tempFile.deleteOnExit();

			Model model = new Model();
			model.setModelVersion("4.0.0");
			model.setGroupId(groupId);
			model.setArtifactId(artifactId);
			model.setVersion(version);
			model.setPackaging(".jar");
			model.setDescription("POM was created from install:install-file");
			fw = new FileWriter(tempFile);
			tempFile.deleteOnExit();
			new MavenXpp3Writer().write(fw, model);
			ArtifactMetadata metadata = new ProjectArtifactMetadata(artifact, tempFile);
			artifact.addMetadata(metadata);
		} catch (IOException e) {
			throw new MojoExecutionException("Error writing temporary pom file: " + e.getMessage(), e);
		} finally {
			IOUtil.close(fw);
		}

	}

	public static String removeJarExtension(File file) {
		String fileName = file.getName();
		if (fileName.endsWith(".jar"))
			return fileName.substring(0, fileName.lastIndexOf(".jar"));
		return fileName;
	}

	private Dependency createDependency(String artifactId, String version) {
		Dependency dependency = new Dependency();
		dependency.setGroupId("org.eclipse.plugins");
		dependency.setArtifactId(artifactId);
		dependency.setVersion(version);
		return dependency;
	}
}