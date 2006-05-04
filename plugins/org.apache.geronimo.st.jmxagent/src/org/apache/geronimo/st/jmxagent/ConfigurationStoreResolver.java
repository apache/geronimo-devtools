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
package org.apache.geronimo.st.jmxagent;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.geronimo.st.core.ModuleArtifactMapper;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.j2ee.componentcore.util.EARArtifactEdit;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.wst.common.componentcore.internal.StructureEdit;
import org.eclipse.wst.common.componentcore.internal.WorkbenchComponent;
import org.eclipse.wst.common.componentcore.internal.impl.WorkbenchComponentImpl;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;

public class ConfigurationStoreResolver implements ConfigurationStoreResolverMBean {

	public Set resolve(File repoRoot, String configId, String module, String path) throws Exception {
		
		Trace.trace(Trace.INFO, "ConfigStore Resolve Query: " + configId + ":" + module + ":" + path);
		Trace.trace(Trace.INFO, "BaseDir:" + repoRoot);

		IProject project = ModuleArtifactMapper.getInstance().resolve(repoRoot, configId);

		if (project == null) {
			throw new Exception("Could not find project in workspace for configId " + configId);
		}
		
		Trace.trace(Trace.INFO, "Found project " + project.getName());

		if (module != null && J2EEProjectUtilities.isEARProject(project)) {
			EARArtifactEdit edit = EARArtifactEdit.getEARArtifactEditForRead(project);
			try {
				IVirtualReference[] refs = edit.getComponentReferences();
				for (int i = 0; i < refs.length; i++) {
					IVirtualReference ref = refs[i];
					String moduleURI = edit.getModuleURI(ref.getReferencedComponent());
					if (moduleURI.equals(module)) {
						// found module
						project = ref.getReferencedComponent().getProject();
					}
				}
			} finally {
				if (edit != null)
					edit.dispose();
			}
		}

		Set urls = new HashSet();

		if (path == null || path.length() == 0) {
			StructureEdit moduleCore = StructureEdit.getStructureEditForRead(project);
			try {
				WorkbenchComponent component = moduleCore.getComponent();
				IPath loc = ((WorkbenchComponentImpl) component).getDefaultSourceRoot();
				try {
					URL url = project.findMember(loc).getLocation().toFile().toURL();
					urls.add(url);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

			} finally {
				if (moduleCore != null)
					moduleCore.dispose();
			}
		} else {
			// return output containers
			IContainer[] containers = J2EEProjectUtilities.getOutputContainers(project);
			for (int i = 0; i < containers.length; i++) {
				try {
					urls.add(containers[i].getLocation().toFile().toURL());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		Trace.trace(Trace.INFO, "ConfigStore Resolve Result: " + urls);

		return urls;

	}
}
