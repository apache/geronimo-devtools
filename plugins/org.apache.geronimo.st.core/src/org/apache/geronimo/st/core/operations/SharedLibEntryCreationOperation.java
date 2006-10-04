/**
 *  Copyright 2006 The Apache Software Foundation
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
package org.apache.geronimo.st.core.operations;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.spi.status.ProgressObject;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.geronimo.st.core.GeronimoServerBehaviourDelegate;
import org.apache.geronimo.st.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.j2ee.internal.deployables.J2EEFlexProjDeployable;
import org.eclipse.jst.server.core.IEnterpriseApplication;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.model.ModuleDelegate;

public class SharedLibEntryCreationOperation extends AbstractDataModelOperation implements ISharedLibEntryCreationDataModelProperties {
	
	private TargetModuleID sharedLibTarget;
	private IServer server;

	public SharedLibEntryCreationOperation() {
	}

	/**
	 * @param model
	 */
	public SharedLibEntryCreationOperation(IDataModel model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.operations.AbstractOperation#execute(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.runtime.IAdaptable)
	 */
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		IModule module = (IModule) model.getProperty(MODULE);
		this.server = (IServer) model.getProperty(SERVER);
		
		boolean recycle = false;
		
		J2EEFlexProjDeployable j2eeModule = (J2EEFlexProjDeployable) module.loadAdapter(J2EEFlexProjDeployable.class, null);
		if(j2eeModule instanceof IEnterpriseApplication) {
			IModule[] modules = j2eeModule.getChildModules();
			for(int i = 0; i < modules.length; i++) {
				IStatus status = process(modules[i], monitor);
				if(status.isOK()) {
					recycle = true;
				}
			}
		} else {
			return process(module, monitor);
		}
		
		return recycle ? Status.OK_STATUS : Status.CANCEL_STATUS;
	}
	
	private IStatus process(IModule module, IProgressMonitor monitor) throws ExecutionException {
		Trace.trace(Trace.INFO, "SharedLibEntryCreationOperation.process() " + module.getName());
		IProject project = module.getProject();
		try {
			
			//locate the path of the first sharedlib library folder
			String sharedLibPath = null;
			GeronimoServerBehaviourDelegate gsDelegate = (GeronimoServerBehaviourDelegate) server.getAdapter(GeronimoServerBehaviourDelegate.class);
			MBeanServerConnection connection = gsDelegate.getServerConnection();
			Set result = connection.queryMBeans(new ObjectName("*:j2eeType=GBean,name=SharedLib,*"), null);
			if (!result.isEmpty()) {
				ObjectInstance instance = (ObjectInstance) result.toArray()[0];
				String[] libDirs = (String[]) connection.getAttribute(instance.getObjectName(),"libDirs");
				if (libDirs != null && libDirs.length > 0) {
					sharedLibPath = libDirs[0];
				}
			}
			
			if(sharedLibPath == null) 
				return Status.CANCEL_STATUS;
			
			// determine the absolute path of the dummy jar
			String dummyJarName = project.getName() + ".eclipse.jar";
			File dummyJarFile = server.getRuntime().getLocation().append(sharedLibPath).append(dummyJarName).toFile();

			// delete the dummy jar and return if module no longer associated with server 
			if (!ServerUtil.containsModule(server, module, monitor)) {
				if (dummyJarFile.exists()) {
					stopSharedLib();
					delete(dummyJarFile);
					startSharedLib();
					return Status.OK_STATUS;
				} else {
					// don't need to recycle shared lib
					return Status.CANCEL_STATUS;
				}
			}

			// filter the cp entries needed to be added to the dummy shared lib jar
			HashSet entries = new HashSet();
			processJavaProject(project, entries, false);
			
			//add output locations of referenced projects excluding non-child projects
			ModuleDelegate delegate = (ModuleDelegate) module.loadAdapter(ModuleDelegate.class, null);
			if(delegate != null) {
				IProject[] refs = project.getReferencedProjects();
				for(int i = 0; i < refs.length; i++) {
					boolean found = false;
					IModule[] children = delegate.getChildModules();
					for(int j = 0; j < children.length; j++) {
						if(children[j].getProject().equals(refs[i])) {
							found = true;
							break;
						}
					}
					if(!found) {
						processJavaProject(refs[i], entries, true);
					}
				}
			}

			// regen the jar only if required
			if (regenerate(dummyJarFile, entries)) {
				stopSharedLib();
				Trace.trace(Trace.INFO, "Updating external sharedlib entries for " + module.getName());
				if(dummyJarFile.exists())
					delete(dummyJarFile);
				Manifest manifest = new Manifest();
				Attributes attributes = manifest.getMainAttributes();
				attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
				attributes.put(Attributes.Name.CLASS_PATH, getCPEntriesAsString(entries));
				JarOutputStream os = new JarOutputStream(new FileOutputStream(dummyJarFile), manifest);
				os.flush();
				os.close();
				startSharedLib();
			} else {
				return Status.CANCEL_STATUS;
			}
		} catch (Exception e) {
			throw new ExecutionException("Failed to update shared lib.", e);
		}
		
		return Status.OK_STATUS;
	}

	private void delete(File dummyJarFile) {
		if(dummyJarFile.delete()) {
			Trace.trace(Trace.INFO, dummyJarFile.getAbsolutePath() + " deleted sucessfully.");
		} else {
			Trace.trace(Trace.SEVERE, "Failed to delete " + dummyJarFile.getAbsolutePath(), null);
		}
	}

	private void processJavaProject(IProject project, HashSet entries, boolean includeOutputLocations) throws JavaModelException {
		IJavaProject jp = JavaCore.create(project);
		IClasspathEntry[] cp = jp.getRawClasspath();
		for (int i = 0; i < cp.length; i++) {
			IClasspathEntry entry = cp[i];
			int kind = entry.getEntryKind();
			String path = null;
			if(kind == IClasspathEntry.CPE_CONTAINER) {
				if("org.maven.ide.eclipse.MAVEN2_CLASSPATH_CONTAINER".equals(entry.getPath().toString())) {
					IClasspathContainer container = JavaCore.getClasspathContainer(entry.getPath(), jp);
					IClasspathEntry[] containerEntries = container.getClasspathEntries();
					for(int j = 0; j  < containerEntries.length; j++) {
						addEntry(entries, resolveVarOrLibEntry(containerEntries[j]));
					}
				}
			} else if (kind == IClasspathEntry.CPE_PROJECT) {
				IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(entry.getPath().segment(0));
				IJavaProject ref = JavaCore.create(p);
				path = p.getLocation().removeLastSegments(1).append(ref.getOutputLocation()).addTrailingSeparator().toOSString();
			} else if (kind == IClasspathEntry.CPE_SOURCE) {
				//this if not combined with parent statement to filter out CPE_SOURCE entries from following else statement
				//if no outputlocation, output path will get picked up by default output path
				if(includeOutputLocations && entry.getOutputLocation() != null) {
					path = project.getLocation().append(entry.getOutputLocation()).addTrailingSeparator().toOSString();
				}
			} else {
				path = resolveVarOrLibEntry(entry);
			}
			addEntry(entries, path);
		}
		
		if(includeOutputLocations) {
			String path = project.getLocation().removeLastSegments(1).append(jp.getOutputLocation()).addTrailingSeparator().toOSString();
			addEntry(entries, path);
		}
	}

	private String resolveVarOrLibEntry(IClasspathEntry entry) {
		IClasspathEntry resolved = JavaCore.getResolvedClasspathEntry(entry);
		IPath resolvedPath = resolved.getPath().makeAbsolute();
		IProject candiate = ResourcesPlugin.getWorkspace().getRoot().getProject(resolvedPath.segment(0));
		//check if resolvedPath is a project resource
		if(candiate.exists(resolvedPath.removeFirstSegments(1))) {
			return candiate.getLocation().append(resolvedPath.removeFirstSegments(1)).toOSString();
		} else {
			return resolvedPath.toOSString();
		}
	}

	private void addEntry(HashSet entries, String path) {
		if(path != null) {
			File f = new File(path);
			try {
				String url = f.toURL().toExternalForm();
				if (!entries.contains(url)) {
					Trace.trace(Trace.INFO, "Adding " + url);
					entries.add(url);
				}
			} catch (MalformedURLException e1) {
				Trace.trace(Trace.INFO, "Failed to add " + path);
				e1.printStackTrace();
			}
		}
	}

	/**
	 * @param entries
	 * @return
	 */
	private String getCPEntriesAsString(Set entries) {
		StringBuffer buffer = new StringBuffer();
		Iterator i = entries.iterator();
		while (i.hasNext()) {
			String cpEntry = (String) i.next();
			buffer.append(cpEntry);
			if (i.hasNext()) {
				buffer.append(" ");
			}
		}
		return buffer.toString();
	}

	/**
	 * @param jarPath
	 * @param entries
	 * @return
	 * @throws Exception
	 */
	private boolean regenerate(File jarFile, Set entries) throws Exception {
		if (jarFile.exists()) {
			if (entries.isEmpty()) {
				// go ahead and return if zero entires, dummy jar will be
				// deleted
				return true;
			} else {
				JarFile jar = new JarFile(jarFile);
				Manifest manifest = jar.getManifest();
				Attributes attributes = manifest.getMainAttributes();
				String value = attributes.getValue(Attributes.Name.CLASS_PATH);
				jar.close();

				Set currentEntries = new HashSet();
				if (value != null) {
					StringTokenizer tokenizer = new StringTokenizer(value);
					while (tokenizer.hasMoreTokens()) {
						currentEntries.add(tokenizer.nextToken());
					}
				}
				// regen dummy jar if old and new entries don't match
				return !entries.equals(currentEntries);
			}
		}

		return !entries.isEmpty();
	}
	
	private void stopSharedLib() throws Exception {
		DeploymentManager dm = DeploymentCommandFactory.getDeploymentManager(server);
		TargetModuleID id = getSharedLibTargetModuleID();
		TargetModuleID[] ids = new TargetModuleID[]{id};
		ProgressObject po = dm.stop(ids);
		waitForProgress(po);
		if(po.getDeploymentStatus().isFailed()) {
			throw new Exception(po.getDeploymentStatus().getMessage());
		} 
	}
	
	private void startSharedLib() throws Exception {
		DeploymentManager dm = DeploymentCommandFactory.getDeploymentManager(server);
		TargetModuleID id = getSharedLibTargetModuleID();
		ProgressObject po = dm.start(new TargetModuleID[]{id});
		waitForProgress(po);
		if(po.getDeploymentStatus().isFailed()) {
			throw new Exception(po.getDeploymentStatus().getMessage());
		} 
	}
	
	private TargetModuleID getSharedLibTargetModuleID() throws Exception {
		if(sharedLibTarget == null) {
			DeploymentManager dm = DeploymentCommandFactory.getDeploymentManager(server);
			TargetModuleID[] ids = dm.getAvailableModules(null, dm.getTargets());
			for(int i = 0; i < ids.length; i++) {
				if(ids[i].getModuleID().indexOf("sharedlib") > 0) {
					sharedLibTarget = ids[i];
					break;
				}
			}	
		}
		
		if(sharedLibTarget == null) {
			throw new Exception("Could not determine SharedLib TargetModuleID.");
		}
		
		return sharedLibTarget;
	}

	private void waitForProgress(ProgressObject po) {
		while (po.getDeploymentStatus().isRunning()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		DeploymentStatus status = po.getDeploymentStatus();
		String command = status.getCommand().toString();
		String state = status.getState().toString();
		Trace.trace(Trace.INFO, "SharedLib " + " " + command + " " + state);
	}
}
