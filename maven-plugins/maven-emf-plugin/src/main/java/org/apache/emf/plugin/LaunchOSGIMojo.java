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
package org.apache.emf.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.eclipse.core.runtime.adaptor.LocationManager;

abstract public class LaunchOSGIMojo extends AbstractMojo {

	/**
	 * @parameter expression="${settings.localRepository}/eclipse/eclipse"
	 */
	protected File eclipseHome;
	
	/**
	 * @parameter expression="${project.basedir}"
	 */
	protected File workspace;

	/**
	 * @parameter expression="${project}"
	 */
	protected MavenProject mavenProject;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {

		if (!eclipseHome.exists())
			throw new MojoFailureException("Eclipse installation not available");

		URL osgi = findOSGI();
		if (osgi == null) {
			throw new MojoFailureException("OSGI bundle not found");
		}

		if (getLog().isDebugEnabled())
			System.setProperty(EclipseStarter.PROP_CONSOLE_LOG, "true");
		
		System.setProperty(EclipseStarter.PROP_CLEAN, "true");
		System.setProperty(EclipseStarter.PROP_INSTALL_AREA, eclipseHome.getAbsolutePath());
		System.setProperty(EclipseStarter.PROP_FRAMEWORK, osgi.toExternalForm());
		System.setProperty(LocationManager.PROP_INSTANCE_AREA, workspace.getAbsolutePath());
		System.setProperty("eclipse.application", getApplicationID());

		String[] args = getArguments();
		if (args == null)
			args = new String[] {};

		getLog().debug(Arrays.asList(args).toString());

		try {
			EclipseStarter.run(args, null);
		} catch (Exception e) {
			throw new MojoFailureException(e.getMessage());
		}
	}

	protected abstract String getApplicationID();

	protected abstract String[] getArguments();

	protected URL findOSGI() {
		File bundleDir = new File(eclipseHome.getAbsoluteFile()
				+ File.separator + "plugins");
		if (bundleDir.isDirectory()) {
			File[] bundles = bundleDir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith("org.eclipse.osgi_");
				}
			});
			if (bundles.length > 0)
				try {
					return bundles[0].toURL();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
}
