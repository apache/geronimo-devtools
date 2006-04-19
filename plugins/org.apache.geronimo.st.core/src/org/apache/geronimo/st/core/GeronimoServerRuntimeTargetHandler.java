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
package org.apache.geronimo.st.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.server.generic.core.internal.GenericServerRuntimeTargetHandler;
import org.eclipse.jst.server.generic.core.internal.ServerTypeDefinitionUtil;
import org.eclipse.jst.server.generic.servertype.definition.ArchiveType;
import org.eclipse.jst.server.generic.servertype.definition.Classpath;
import org.eclipse.jst.server.generic.servertype.definition.ServerRuntime;
import org.eclipse.wst.server.core.IRuntime;

public class GeronimoServerRuntimeTargetHandler extends
		GenericServerRuntimeTargetHandler {

	private static final String EXTENSION_RUNTIME_ACCESS = "discouragedRuntimeAccess";

	String cachedArchiveString = null;
	IClasspathEntry[] cachedClasspath = null;
	private static Map map;
	private IPath runtimeLoc;
	private String runtimeTypeId;

	static {
		loadExtensions();
	}

	private static synchronized void loadExtensions() {
		map = new HashMap();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(Activator.PLUGIN_ID, EXTENSION_RUNTIME_ACCESS);
		for (int i = 0; i < cf.length; i++) {
			IConfigurationElement element = cf[i];
			if ("restriction".equals(element.getName())) {
				String runtimeId = element.getAttribute("id");
				if (runtimeId != null) {
					IConfigurationElement[] children = element.getChildren();
					for (int j = 0; j < children.length; j++) {
						String path = children[j].getAttribute("value");
						if (path != null) {
							Collection c = (Collection) map.get(runtimeId);
							if (c == null) {
								c = new ArrayList();
								map.put(runtimeId, c);
							}
							c.add(new Path(path));
						}
					}
				}
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ClasspathRuntimeTargetHandler#resolveClasspathContainer(IRuntime,
	 *      java.lang.String)
	 */
	public IClasspathEntry[] resolveClasspathContainer(IRuntime runtime,
			String id) {
		this.runtimeTypeId = runtime.getRuntimeType().getId();
		return getServerClassPathEntry(runtime);
	}

	public IClasspathEntry[] getServerClassPathEntry(IRuntime runtime) {
		this.runtimeLoc = runtime.getLocation();
		ServerRuntime serverDefinition = ServerTypeDefinitionUtil.getServerTypeDefinition(runtime);
		String ref = serverDefinition.getProject().getClasspathReference();
		Classpath cp = serverDefinition.getClasspath(ref);
		List archives = cp.getArchive();

		// It's expensive to keep searching directories, so try to cache the
		// result
		IClasspathEntry[] savedClasspath = getCachedClasspathFor(serverDefinition, archives);
		if (savedClasspath != null)
			return savedClasspath;

		Iterator archiveIter = archives.iterator();
		ArrayList entryList = new ArrayList();
		while (archiveIter.hasNext()) {
			ArchiveType archive = (ArchiveType) archiveIter.next();
			String item = serverDefinition.getResolver().resolveProperties(archive.getPath());
			IPath path = new Path(item);
			File file = path.toFile();
			if (file.isDirectory()) {
				boolean discourageAccess = isAccessDiscouraged(path);
				File[] list = file.listFiles();
				for (int i = 0; i < list.length; i++) {
					if (!list[i].isDirectory()) {
						Path p = new Path(list[i].getAbsolutePath());
						if (!discourageAccess)
							discourageAccess = isAccessDiscouraged(p);
						addLibEntry(entryList, p, discourageAccess);
					}
				}
			} else {
				addLibEntry(entryList, path, isAccessDiscouraged(path));
			}
		}

		IClasspathEntry[] classpath = (IClasspathEntry[]) entryList.toArray(new IClasspathEntry[entryList.size()]);
		setCachedClasspath(classpath);

		return classpath;
	}

	private boolean isAccessDiscouraged(IPath path) {
		Collection c = (Collection) map.get(runtimeTypeId);
		if (c == null || c.isEmpty())
			return false;

		Iterator i = c.iterator();
		while (i.hasNext()) {
			IPath xPath = (IPath) i.next();
			if (path.toFile().isDirectory()
					&& runtimeLoc.append(xPath).isPrefixOf(path)) {
				return true;
			} else if (runtimeLoc.append(xPath).equals(path)) {
				return true;
			}
		}
		return false;
	}

	private void addLibEntry(ArrayList entryList, IPath path,
			boolean discourageAccess) {
		IClasspathEntry entry = null;
		if (discourageAccess) {
			IAccessRule rule = JavaCore.newAccessRule(new Path("**"), IAccessRule.K_DISCOURAGED);
			IAccessRule rules[] = new IAccessRule[] { rule };
			entry = JavaCore.newLibraryEntry(path, null, null, rules, new IClasspathAttribute[] {}, false);
		} else {
			entry = JavaCore.newLibraryEntry(path, null, null);
		}
		entryList.add(entry);
	}

	private IClasspathEntry[] getCachedClasspathFor(
			ServerRuntime serverDefinition, List archives) {

		// Need to iterate through the list, and expand the variables (in case
		// they have changed)
		// The simplest approach is to construct/cache a string for this
		// That will still save the overhead of going to the filesystem

		StringBuffer buffer = new StringBuffer();
		Iterator archiveIter = archives.iterator();
		while (archiveIter.hasNext()) {
			ArchiveType archive = (ArchiveType) archiveIter.next();
			String item = serverDefinition.getResolver().resolveProperties(archive.getPath());
			buffer.append(item);
			buffer.append(File.pathSeparatorChar);
		}

		String archiveString = buffer.toString();

		if (cachedArchiveString != null
				&& cachedArchiveString.equals(archiveString))
			return cachedClasspath;

		// This is a cache miss - ensure the data is null (to be safe), but save
		// the key (archiveString) now
		// The data will be set once it's calculated
		cachedClasspath = null;
		cachedArchiveString = archiveString;
		return null;
	}

	private void setCachedClasspath(IClasspathEntry[] classpath) {
		cachedClasspath = classpath;
	}
}
