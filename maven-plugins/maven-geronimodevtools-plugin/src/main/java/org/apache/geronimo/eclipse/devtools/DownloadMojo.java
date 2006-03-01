package org.apache.geronimo.eclipse.devtools;

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
import java.io.File;
import java.net.URL;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.install.InstallFileMojo;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Get;
import org.apache.tools.ant.taskdefs.Untar;

/**
 * @goal download
 * @description Downloads eclipse project distributions into local repository.
 */
public class DownloadMojo extends AbstractMojo {

	private static final String ECLIPSE_INSTALL_PATH = "eclipse/";
	private static final String DISTRO_PATH = ECLIPSE_INSTALL_PATH
			+ "distributions/";

	/**
	 * @parameter expression="${project}"
	 * @required
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${settings.localRepository}"
	 */
	private String localRepoLoc;

	/**
	 * @parameter
	 */
	private URL[] urls;

	private File distributionDir;
	private File installDir;

	public DownloadMojo() {
		super();
	}

	private void init() {
		distributionDir = new File(localRepoLoc + File.separator + DISTRO_PATH);
		installDir = new File(localRepoLoc + File.separator
				+ ECLIPSE_INSTALL_PATH);
		getLog().info("Distribution directory set to: "
				+ distributionDir.getAbsolutePath());
		getLog().info("Installation directory set to: "
				+ installDir.getAbsolutePath());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		init();

		if (urls == null) {
			getLog().info("No URL's specified in configuration.  Nothing to download.");
			return;
		}

		if (!distributionDir.exists())
			distributionDir.mkdirs();

		for (int i = 0; i < urls.length; i++) {
			File distro = getRepositoryDestination(urls[i]);
			if (!distro.exists()) {
				download(urls[i]);
			} else {
				getLog().info(distro.getAbsolutePath()
						+ " already exists in local repository");
			}
			install(distro);
		}
	}

	private File getRepositoryDestination(URL url) {
		File file = new File(url.getFile());
		return new File(localRepoLoc + File.separator + DISTRO_PATH
				+ file.getName());
	}

	private void download(URL url) {
		Get getTask = new Get();
		getTask.setProject(new Project());
		getTask.setSrc(url);
		getTask.setDest(getRepositoryDestination(url));
		getLog().info("Downloading " + url.toExternalForm() + "...");
		getTask.execute();
	}

	private void install(File file) {
		if (file.getName().endsWith(".zip")) {
			Expand expandTask = new Expand();
			expandTask.setProject(new Project());
			expandTask.setSrc(file);
			expandTask.setDest(installDir);
			getLog().info("Extracting " + file.getAbsolutePath());
			expandTask.execute();
		}
	}

}
