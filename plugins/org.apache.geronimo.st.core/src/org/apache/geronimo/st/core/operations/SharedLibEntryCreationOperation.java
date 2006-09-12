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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.model.ModuleDelegate;

public class SharedLibEntryCreationOperation extends AbstractDataModelOperation implements ISharedLibEntryCreationDataModelProperties {

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
		
		//TODO process child modules if ear project
		
		IProject project = module.getProject();
		try {

			// determine the dummy jar path from project name and runtime
			// location
			IFacetedProject fp = ProjectFacetsManager.create(project);
			IRuntime runtime = FacetUtil.getRuntime(fp.getPrimaryRuntime());
			String dummyJarName = project.getName() + ".eclipse.jar";
			File dummyJarFile = runtime.getLocation().append("var/shared/lib").append(dummyJarName).toFile();

			// delete the dummy jar and return if module no longer associated
			// with server with the same runtime
			boolean delete = true;
			IServer[] servers = ServerUtil.getServersByModule(module, monitor);
			for (int i = 0; i < servers.length; i++) {
				if (runtime.equals(servers[i].getRuntime())) {
					delete = false;
					break;
				}
			}
			if (delete) {
				if (dummyJarFile.delete()) {
					return Status.OK_STATUS;
				} else {
					// don't need to recycle shared lib
					return Status.CANCEL_STATUS;
				}
			}

			// filter the cp entries needed to be added to the dummy shared lib jar
			HashSet entries = new HashSet();
			processJavaProject(project, entries);
			
			//add output locations of non-child java projects
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
						processJavaProject(refs[i], entries);
					}
				}
			}

			// regen the jar only if required
			if (regenerate(dummyJarFile, entries)) {
				dummyJarFile.delete();
				Manifest manifest = new Manifest();
				Attributes attributes = manifest.getMainAttributes();
				attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
				attributes.put(Attributes.Name.CLASS_PATH, getCPEntriesAsString(entries));
				JarOutputStream os = new JarOutputStream(new FileOutputStream(dummyJarFile), manifest);
				os.flush();
				os.close();
			} else {
				return Status.CANCEL_STATUS;
			}
		} catch (Exception e) {
			throw new ExecutionException("Failed to update shared lib.", e);
		}

		return Status.OK_STATUS;
	}

	private void processJavaProject(IProject project, HashSet entries) throws JavaModelException {
		IJavaProject jp = JavaCore.create(project);
		IClasspathEntry[] cp = jp.getRawClasspath();
		for (int i = 0; i < cp.length; i++) {
			IClasspathEntry entry = cp[i];
			int kind = entry.getEntryKind();
			if (kind == IClasspathEntry.CPE_LIBRARY || kind == IClasspathEntry.CPE_VARIABLE || kind == IClasspathEntry.CPE_PROJECT) {
				String path = null;
				if(kind == IClasspathEntry.CPE_PROJECT) {
					IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(entry.getPath().segment(0));
					IJavaProject ref = JavaCore.create(p);
					path = p.getLocation().removeLastSegments(1).append(ref.getOutputLocation()).addTrailingSeparator().toOSString();
				} else {
					IClasspathEntry resolved = JavaCore.getResolvedClasspathEntry(entry);
					path = resolved.getPath().makeAbsolute().toOSString();
				}
				
				Trace.trace(Trace.INFO, "Adding " + path);
				if (!entries.contains(path))
					entries.add(path);
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
}
