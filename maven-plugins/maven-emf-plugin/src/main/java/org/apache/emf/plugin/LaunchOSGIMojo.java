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
import org.apache.maven.model.PluginExecution;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

abstract public class LaunchOSGIMojo extends AbstractMojo {

	public static final String PROP_CLEAN = "osgi.clean";
	public static final String PROP_CONSOLE_LOG = "eclipse.consoleLog";
	public static final String PROP_FRAMEWORK = "osgi.framework";
	public static final String PROP_INSTALL_AREA = "osgi.install.area";
	public static final String PROP_INSTANCE_AREA = "osgi.instance.area";
	public static final String PROP_APPLICATION_ID = "eclipse.application";
	public static final String PROP_USE_SYS_PROPS = "osgi.framework.useSystemProperties";
	public static final String PROP_NOSHUTDOWN = "osgi.noShutdown";
	public static final String STARTER = "org.eclipse.core.runtime.adaptor.EclipseStarter";

	public static final String TOTAL_EXECUTIONS = "plugin.total.executions";
	public static final String CURRENT_EXECUTION = "plugin.current.execution";
	public static final String PLUGIN_ARTIFACT_ID = "maven-emf-plugin";

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

		boolean keepFrameworkAlive = keepFrameworkAlive();

		String[] args = getArguments();
		if (args == null)
			args = new String[] {};

		getLog().debug(Arrays.asList(args).toString());

		System.setProperty(PROP_USE_SYS_PROPS, "true");
		
		Class clazz = null;

		try {
			Map initalPropertyMap = new HashMap();
			if (getLog().isDebugEnabled())
				initalPropertyMap.put(PROP_CONSOLE_LOG, "true");
			initalPropertyMap.put(PROP_CLEAN, "true");
			initalPropertyMap.put(PROP_INSTALL_AREA, eclipseHome.toURL().toExternalForm());
			initalPropertyMap.put(PROP_FRAMEWORK, osgi.toExternalForm());
			initalPropertyMap.put(PROP_INSTANCE_AREA, workspace.toURL().toExternalForm());
			initalPropertyMap.put(PROP_APPLICATION_ID, getApplicationID());
			initalPropertyMap.put(PROP_NOSHUTDOWN, Boolean.toString(keepFrameworkAlive));
			initalPropertyMap.put("eclipse.vmargs","-Xmx512M");

			URL[] osgiURLArray = { new URL((String) initalPropertyMap.get(PROP_FRAMEWORK)) };
			
			if(getPluginContext().containsKey(STARTER)) {
				clazz = (Class) getPluginContext().get(STARTER);
			} else  {
				URLClassLoader frameworkClassLoader = new URLClassLoader(osgiURLArray);
				clazz = frameworkClassLoader.loadClass(STARTER);
				getPluginContext().put(STARTER, clazz);
			}

			Method setInitialProperties = clazz.getMethod("setInitialProperties", new Class[] { Map.class });
			setInitialProperties.invoke(null, new Object[] { initalPropertyMap });

			getLog().debug("Framework Execution " + getCurrentExecution() + "/" + getTotalExecutions());
			
			if (getCurrentExecution() == 1) {
				Method runMethod = clazz.getMethod("run", new Class[] {
						String[].class, Runnable.class });
				runMethod.invoke(null, new Object[] { args, null });			
			} else {				
				Method runMethod = clazz.getMethod("run", new Class[] { Object.class });
				runMethod.invoke(null, new Object[] { args });
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new MojoFailureException(e.getMessage());
		}
		
		if(getCurrentExecution() == getTotalExecutions()) {
			getPluginContext().remove(CURRENT_EXECUTION);
			try {
				Method shutdownMethod = clazz.getMethod("shutdown", new Class[] {});
				shutdownMethod.invoke(null, null);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			clazz = null;
			getPluginContext().remove(STARTER);
			cleanup();
		} else {
			getPluginContext().put(CURRENT_EXECUTION, new Integer(getCurrentExecution() + 1));
		}
	}

	protected abstract String getApplicationID();

	protected abstract String[] getArguments();

	protected abstract String getGoalName();

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

	private boolean keepFrameworkAlive() {
		return getCurrentExecution() < getTotalExecutions();
	}

	private int getCurrentExecution() {
		int currentExecution = 1;
		if (getPluginContext().containsKey(CURRENT_EXECUTION)) {
			currentExecution = ((Integer) getPluginContext().get(CURRENT_EXECUTION)).intValue();
		}
		return currentExecution;
	}

	private int getTotalExecutions() {
		if (!getPluginContext().containsKey(TOTAL_EXECUTIONS)) {
			int totalExecutions = 0;
			List plugins = mavenProject.getBuild().getPlugins();
			Iterator i = plugins.iterator();
			while (i.hasNext()) {
				Plugin plugin = (Plugin) i.next();
				if (PLUGIN_ARTIFACT_ID.equals(plugin.getArtifactId())) {
					Iterator j = plugin.getExecutions().iterator();
					while (j.hasNext()) {
						PluginExecution execution = (PluginExecution) j.next();
						if (execution.getGoals().contains(getGoalName()))
							totalExecutions++;
					}
				}
			}
			getPluginContext().put(TOTAL_EXECUTIONS, new Integer(totalExecutions));
		}
		return ((Integer) getPluginContext().get(TOTAL_EXECUTIONS)).intValue();
	}
	
	protected void cleanup() {
		
	}
}
