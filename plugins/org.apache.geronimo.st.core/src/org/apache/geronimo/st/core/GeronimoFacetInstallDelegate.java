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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

abstract public class GeronimoFacetInstallDelegate implements IDelegate {

	public static final String FACET_ID = "org.apache.geronimo.facet";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.project.facet.core.IDelegate#execute(org.eclipse.core.resources.IProject,
	 *      org.eclipse.wst.common.project.facet.core.IProjectFacetVersion,
	 *      java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void execute(IProject project, IProjectFacetVersion fv,
			Object config, IProgressMonitor monitor) throws CoreException {

		try {
			createDeploymentPlanCreationOp(project).execute(monitor, null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		IProject[] ears = J2EEProjectUtilities.getReferencingEARProjects(project);
		IProjectFacet facet = ProjectFacetsManager.getProjectFacet(FACET_ID);
		for (int i = 0; i < ears.length; i++) {
			IFacetedProject fp = ProjectFacetsManager.create(ears[i]);
			if (!fp.hasProjectFacet(facet)) {
				try {
					createDeploymentPlanCreationOp(ears[i]).execute(monitor, null);
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public abstract IDataModelOperation createDeploymentPlanCreationOp(
			IProject project);

}
