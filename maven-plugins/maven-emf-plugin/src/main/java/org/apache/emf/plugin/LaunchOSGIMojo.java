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
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Delete;

abstract public class LaunchOSGIMojo extends AbstractMojo {

	public static final String PROP_CLEAN = "osgi.clean";
	public static final String PROP_CONSOLE_LOG = "eclipse.consoleLog";
	public static final String PROP_FRAMEWORK = "osgi.framework";
	public static final String PROP_INSTALL_AREA = "osgi.install.area";
	public static final String PROP_INSTANCE_AREA = "osgi.instance.area";
	public static final String PROP_APPLICATION_ID = "eclipse.application";
	public static final String PROP_USE_SYS_PROPS = "osgi.framework.useSystemProperties";
	public static final String STARTER = "org.eclipse.core.runtime.adaptor.EclipseStarter";

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

	protected static final String SPACE = " ";

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

		validate();

		String[] args = getArguments();
		if (args == null)
			args = new String[] {};

		getLog().debug(Arrays.asList(args).toString());

		System.setProperty(PROP_USE_SYS_PROPS, "true");

		try {
			Map initalPropertyMap = new HashMap();
			if (getLog().isDebugEnabled())
				initalPropertyMap.put(PROP_CONSOLE_LOG, "true");
			initalPropertyMap.put(PROP_CLEAN, "true");
			initalPropertyMap.put(PROP_INSTALL_AREA, eclipseHome.toURL().toExternalForm());
			initalPropertyMap.put(PROP_FRAMEWORK, osgi.toExternalForm());
			initalPropertyMap.put(PROP_INSTANCE_AREA, workspace.toURL().toExternalForm());
			initalPropertyMap.put(PROP_APPLICATION_ID, getApplicationID());

			URL[] osgiURLArray = { new URL((String) initalPropertyMap.get(PROP_FRAMEWORK)) };
			URLClassLoader frameworkClassLoader = new URLClassLoader(osgiURLArray);
			Class clazz = frameworkClassLoader.loadClass(STARTER);

			Method setInitialProperties = clazz.getMethod("setInitialProperties", new Class[] { Map.class });
			setInitialProperties.invoke(null, new Object[] { initalPropertyMap });

			Method runMethod = clazz.getMethod("run", new Class[] {
					String[].class, Runnable.class });
			runMethod.invoke(null, new Object[] { args, null });
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

	public static String[] getArguments(StringBuffer buffer) {
		StringTokenizer st = new StringTokenizer(buffer.toString());
		String[] args = new String[st.countTokens()];
		for (int i = 0; st.hasMoreTokens(); i++) {
			args[i] = st.nextToken();
		}
		return args;
	}

	protected void validate() throws MojoFailureException {

	}
	
	private int getTotalExecutions() {
		int totalExecutions = 0;
		List plugins = mavenProject.getBuild().getPlugins();
		Iterator i = plugins.iterator();
		while(i.hasNext()) {
			Plugin plugin = (Plugin) i.next();
			if(plugin.getArtifactId().equals("maven-emf-plugin")) {
				totalExecutions = plugin.getExecutions().size();
				break;
			}
		}
		return totalExecutions;
	}
}
