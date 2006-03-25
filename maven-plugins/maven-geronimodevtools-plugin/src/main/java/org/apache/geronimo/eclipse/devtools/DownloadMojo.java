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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Delete;
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
	 * @required
	 */
	private URL[] urls;

	/**
	 * @parameter
	 * @required
	 */
	private URL platformUrl;

	private File distributionsDir; // ../repository/eclipse/distributions
	private File installToDir; // ../repository/eclipse/
	private File pluginsDir; // ../repository/eclipse/plugins
	private File propsFile; // ../repository/eclipse/install.props

	private Properties props;

	public DownloadMojo() {
		super();
	}

	private void init() {
		distributionsDir = new File(localRepoLoc + File.separator + DISTRO_PATH);
		installToDir = new File(localRepoLoc + File.separator
				+ ECLIPSE_INSTALL_PATH);
		pluginsDir = new File(installToDir.getAbsolutePath() + File.separator
				+ "eclipse" + File.separator + "plugins");
		propsFile = new File(installToDir.getAbsolutePath() + File.separator
				+ "install.props");
		getLog().debug("Distribution directory: "
				+ distributionsDir.getAbsolutePath());
		getLog().debug("Installation directory: "
				+ installToDir.getAbsolutePath());
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

		if (!distributionsDir.exists())
			distributionsDir.mkdirs();

		load();

		URL[] allUrls = urls;
		if (platformUrl != null) {
			String platformDriver = platformUrl.toExternalForm() + "-"
					+ getPlatformUrlSuffix();
			allUrls = new URL[urls.length + 1];
			try {
				URL url = new URL(platformDriver);
				System.arraycopy(urls, 0, allUrls, 0, urls.length);
				allUrls[allUrls.length - 1] = url;
			} catch (MalformedURLException e) {
				throw new MojoFailureException(e.getMessage());
			}
		}

		List images = new ArrayList();
		for (int i = 0; i < allUrls.length; i++) {
			File distro = getRepositoryDestination(allUrls[i]);
			images.add(distro);
			if (!distro.exists()) {
				download(allUrls[i]);
			} else {
				getLog().info(distro.getName()
						+ " already exists in local repository");
			}
		}

		if (shouldExtract()) {
			clean();
			Iterator i = images.iterator();
			while (i.hasNext())
				install((File) i.next());
		}

		setModified();
		save();
	}

	private void clean() {
		Delete deleteTask = new Delete();
		deleteTask.setProject(new Project());
		deleteTask.setDir(pluginsDir.getParentFile());
		deleteTask.execute();
	}

	private String getPlatformUrlSuffix() {
		String os = System.getProperty("os.name");
		if (os.startsWith("Windows")) {
			return "win32.zip";
		} else if (os.startsWith("Linux")) {
			return "linux-gtk.tar.gz";
		} else if (os.startsWith("Mac")) {
			return "macosx-carbon.tar.gz";
		}
		return "win32.zip";
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
			expandTask.setDest(installToDir);
			getLog().info("Extracting " + file.getAbsolutePath());
			expandTask.execute();
		}
	}

	private boolean shouldExtract() {
		return distributionsDir.lastModified() > getModified()
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