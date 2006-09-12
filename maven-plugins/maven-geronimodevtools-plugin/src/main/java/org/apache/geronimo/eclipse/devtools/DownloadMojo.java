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

	private static final String TDS = "tds";
	private static final String IDENTIFIER = "identifier";

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

	/**
	 * @parameter expression="${settings.localRepository}/eclipse/distributions"
	 */
	private File distributionsDir;

	/**
	 * @parameter expression="${settings.localRepository}/eclipse/"
	 */
	private File installLocation;

	/**
	 * @parameter expression="${settings.localRepository}/eclipse/install.props"
	 */
	private File propsFile;

	/**
	 * @parameter expression="${overwrite}";
	 */
	private String overwrite = null;

	/**
	 * @parameter expression="${checkModified}"
	 */
	private boolean checkModified = true;

	private Properties props;
	private long propLastModified = -1;

	public DownloadMojo() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {

		if (urls == null) {
			getLog().info("No URL's specified in configuration.  Nothing to download.");
			return;
		}

		if (!distributionsDir.exists())
			distributionsDir.mkdirs();

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

		load();
		int identifier = generateInstallIdentifier(images);


		boolean extract = false;
		if("false".equals(overwrite)) {
			extract = false;
		} else if("true".equals(overwrite)) {
			extract = true;
		} else {
			extract = shouldExtract(identifier);
		}

		if (extract) {
			clean();
			Iterator i = images.iterator();
			while (i.hasNext())
				install((File) i.next());
			setProperties(System.currentTimeMillis(), identifier);
			save();
		}

	}

	private int generateInstallIdentifier(List images) {
		int id = 0;
		Iterator i = images.iterator();
		while (i.hasNext()) {
			File file = (File) i.next();
			id = id + file.hashCode()
					+ (Long.toString(file.length()).hashCode());
		}
		return id;
	}

	private void clean() {
		Delete deleteTask = new Delete();
		deleteTask.setProject(new Project());
		deleteTask.setDir(new File(installLocation.getAbsolutePath()
				+ File.separator + "eclipse"));
		deleteTask.execute();
	}

	private String getPlatformUrlSuffix() {
		String os = System.getProperty("os.name");
		String arch = System.getProperty("os.arch");
		getLog().info("SDKPlatform:  os.name=" + os + ", os.arch=" + arch);
		if (os.startsWith("Windows")) {
         if (arch.equalsIgnoreCase("x86_64") || arch.equalsIgnoreCase("amd64"))
            return "win32-x86_64.zip";
         else
			   return "win32.zip";
		} else if (os.startsWith("Linux")) {
         if (arch.equalsIgnoreCase("x86_64") || arch.equalsIgnoreCase("amd64"))
            return "linux-gtk-x86_64.tar.gz";
         else if (arch.startsWith("ppc"))
            return "linux-gtk-ppc.tar.gz";
         else
			   return "linux-gtk.tar.gz";
		} else if (os.startsWith("Mac")) {
			return "macosx-carbon.tar.gz";
		} else if (os.startsWith("SunOS")) {
         if (arch.startsWith("x86") || arch.startsWith("amd"))
            return "solaris-gtk-x86.zip";
         else
             return "solaris-gtk.zip";
		} else if (os.startsWith("AIX")) {
         return "aix-motif.zip";
      } else  // flag that we don't know which Eclipse SDK to use
		   return "unknown.zip";
	}

	private File getRepositoryDestination(URL url) {
		return new File(distributionsDir.getAbsolutePath() + File.separator
				+ new File(url.getFile()).getName());
	}

	private void download(URL url) {
		Get getTask = new Get();
		getTask.setProject(new Project());
		getTask.setSrc(url);
		getTask.setDest(getRepositoryDestination(url));
		getLog().info("Downloading " + url.toExternalForm() + " ...");
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
			expandTask.setDest(installLocation);
			getLog().info("Extracting " + file.getAbsolutePath());
			expandTask.execute();
		}
	}

	private boolean shouldExtract(int identifier) {
		if (identifier != getInstallIdentifier())
			return true;

		File installImage = new File(installLocation.getAbsolutePath()
				+ File.separator + "eclipse");

		if (!installImage.exists())
			return true;

		return checkModified
				&& isModified(new File(installImage.getAbsolutePath()
						+ File.separator + "plugins"));
	}

	private boolean isModified(File file) {

		boolean modified = file.lastModified() > getModified();
		File[] children = file.listFiles();
		if (!modified && children != null) {
			for (int i = 0; i < children.length; i++) {
				if (children[i].isDirectory()) {
					modified = isModified(children[i]);
					if (modified)
						break;
				}
			}
		}
		return modified;
	}

	private void setProperties(long tds, int identifier) {
		props.put(TDS, Long.toString(System.currentTimeMillis()));
		props.put(IDENTIFIER, Integer.toString(identifier));
	}

	private long getModified() {
		if (propLastModified == -1) {
			String tds = (String) props.get(TDS);
			if (tds != null)
				return Long.parseLong(tds);
			propLastModified = 0;
		}
		return propLastModified;
	}

	private int getInstallIdentifier() {
		String identifer = (String) props.get(IDENTIFIER);
		if (identifer != null)
			return Integer.parseInt(identifer);
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