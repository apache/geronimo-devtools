/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.geronimo.st.v11.ui.editors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.geronimo.st.ui.Activator;
import org.apache.geronimo.st.ui.editors.IGeronimoFormContentLoader;
import org.apache.geronimo.st.ui.internal.Trace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IRuntime;

/**
 * @version $Rev: 817996 $ $Date: 2009-09-23 16:04:12 +0800 (Wed, 23 Sep 2009) $
 */
public class SharedDeploymentPlanEditor extends AbstractGeronimoDeploymentPlanEditor {

	private static Map loaders = new HashMap();

	private IGeronimoFormContentLoader currentLoader = null;

	static {
		loadExtensionPoints();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.ui.editors.AbstractGeronimoDeploymentPlanEditor#doAddPages()
	 */
	public void doAddPages() throws PartInitException {
        Trace.tracePoint("ENTRY", "SharedDeploymentPlanEditor.doAddPages");

		if (getDeploymentPlan() != null && getLoader() != null) {
			currentLoader.doAddPages(this);
		}
		addSourcePage();

        Trace.tracePoint("EXIT", "SharedDeploymentPlanEditor.doAddPages");
	}

	private static synchronized void loadExtensionPoints() {
        Trace.tracePoint("ENTRY", "SharedDeploymentPlanEditor.loadExtensionPoints");

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(Activator.PLUGIN_ID, "loader");
		for (int i = 0; i < cf.length; i++) {
			IConfigurationElement element = cf[i];
			if ("loader".equals(element.getName())) {
				try {
					IGeronimoFormContentLoader loader = (IGeronimoFormContentLoader) element.createExecutableExtension("class");
					String version = element.getAttribute("version");
					loaders.put(version, loader);
				} catch (CoreException e) {
                    Trace.tracePoint("CoreException", "SharedDeploymentPlanEditor.loadExtensionPoints");
					e.printStackTrace();
				}
			}
		}

        Trace.tracePoint("EXIT", "SharedDeploymentPlanEditor.loadExtensionPoints");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.ui.editors.AbstractGeronimoDeploymentPlanEditor#loadDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement loadDeploymentPlan(IFile file) throws Exception {
        Trace.tracePoint("ENTRY", "SharedDeploymentPlanEditor.loadDeploymentPlan", file);
        JAXBElement jaxbElement = getLoader() != null ? currentLoader.loadDeploymentPlan(file) : null;
        Trace.tracePoint("EXIT", "SharedDeploymentPlanEditor.loadDeploymentPlan", jaxbElement);
		return jaxbElement;
	}
	
	public void saveDeploymentPlan(IFile file) throws Exception {
		if (getLoader() != null) {
			getLoader().saveDeploymentPlan(deploymentPlan, file);
		}
	}

	private IGeronimoFormContentLoader getLoader() {
        Trace.tracePoint("ENTRY", "SharedDeploymentPlanEditor.getLoader");

		if (currentLoader == null) {
			IEditorInput input = getEditorInput();
			if (input instanceof IFileEditorInput) {
				IProject project = ((IFileEditorInput) input).getFile().getProject();
				try {
					IFacetedProject fp = ProjectFacetsManager.create(project);
					if (fp == null) return null;
					IRuntime runtime = FacetUtil.getRuntime(fp.getPrimaryRuntime());
					if (runtime == null) return null;
					String version = runtime.getRuntimeType().getVersion();
					currentLoader = (IGeronimoFormContentLoader) loaders.get(version);
				} catch (CoreException e) {
                    Trace.tracePoint("CoreException", "SharedDeploymentPlanEditor.getLoader");
					e.printStackTrace();
				} catch (IllegalArgumentException ie) {
                    Trace.tracePoint("IllegalArgumentException", "SharedDeploymentPlanEditor.getLoader");
				    throw new IllegalArgumentException("The project [" + project.getName() + "] does not have a Targeted Runtime specified.");
                }
			}
		}

        Trace.tracePoint("EXIT", "SharedDeploymentPlanEditor.getLoader", currentLoader);
		return currentLoader;
	}

}
