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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.GUnzip;
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
	 * @parameter expression="${settings.localRepository}"
	 */
	private String localRepoLoc;

	/**
	 * @parameter
	 */
	private URL[] urls;

	private File distributionDir;
	private File installDir;
	private File pluginsDir;
	private File propsFile;
	
	private Properties props;

	public DownloadMojo() {
		super();
	}

	private void init() {
		distributionDir = new File(localRepoLoc + File.separator + DISTRO_PATH);
		installDir = new File(localRepoLoc + File.separator
				+ ECLIPSE_INSTALL_PATH);
		pluginsDir = new File(installDir.getAbsolutePath() + File.separator
				+ "eclipse" + File.separator + "plugins");
		propsFile = new File(installDir.getAbsolutePath() + File.separator
				+ "install.props");
		getLog().debug("Distribution directory: "
				+ distributionDir.getAbsolutePath());
		getLog().debug("Installation directory: "
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

		load();
		for (int i = 0; i < urls.length; i++) {
			File distro = getRepositoryDestination(urls[i]);
			if (!distro.exists()) {
				download(urls[i]);
			} else {
				getLog().info(distro.getName()
						+ " already exists in local repository");
			}
			if (shouldExtract())
				install(distro);
		}
		setModified();
		save();
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

		if (file.getName().endsWith("tar.gz")) {
			String path = file.getAbsolutePath();
			File tarDist = new File(path.substring(0, path.indexOf(".gz")));
			if (!tarDist.exists()) {
				GUnzip task = new GUnzip();
				task.setProject(new Project());
				task.setSrc(file);
				task.setDest(file.getParentFile());
				task.execute();
			}
			file = tarDist;
		}

		Expand expandTask = null;
		if (file.getName().endsWith(".zip")) {
			expandTask = new Expand();
		} else if (file.getName().endsWith("tar")) {
			expandTask = new Untar();
		}

		if (expandTask != null) {
			expandTask.setProject(new Project());
			expandTask.setSrc(file);
			expandTask.setDest(installDir);
			getLog().info("Extracting " + file.getAbsolutePath());
			expandTask.execute();
		}
	}

	private boolean shouldExtract() {
		return distributionDir.lastModified() > getModified()
				|| !pluginsDir.exists()
				|| pluginsDir.lastModified() > getModified();
	}

	private void setModified() {
		long tds = System.currentTimeMillis();
		props.put("tds", Long.toString(tds));
	}

	private long getModified() {
		String tds = (String) props.get("tds");
		if (tds != null)
			return Long.parseLong(tds);
		return 0;
	}

	private Properties load() {
		if (props == null) {
			props = new Properties();
			FileInputStream fis;
			try {
				fis = new FileInputStream(propsFile);
				props.load(fis);
			} catch (FileNotFoundException e) {
				// ignore
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return props;
	}

	private void save() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(propsFile);
			props.store(fos, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}