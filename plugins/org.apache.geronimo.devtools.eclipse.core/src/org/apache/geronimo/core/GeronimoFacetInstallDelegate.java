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
package org.apache.geronimo.core;

import org.apache.geronimo.core.internal.GeronimoUtils;
import org.apache.geronimo.core.operations.DeploymentPlanCreationOperation;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

public class GeronimoFacetInstallDelegate implements IDelegate {

	/**
	 * 
	 */
	public GeronimoFacetInstallDelegate() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.project.facet.core.IDelegate#execute(org.eclipse.core.resources.IProject,
	 *      org.eclipse.wst.common.project.facet.core.IProjectFacetVersion,
	 *      java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void execute(IProject project, IProjectFacetVersion fv,
			Object config, IProgressMonitor monitor) throws CoreException {
		
		DeploymentPlanCreationOperation op = new DeploymentPlanCreationOperation();
		
		IVirtualComponent comp = ComponentCore.createComponent(project);

		String type = J2EEProjectUtilities.getJ2EEProjectType(project);

		if (IModuleConstants.JST_WEB_MODULE.equals(type)) {
			op.createGeronimoWebDeploymentPlan(GeronimoUtils
					.getWebDeploymentPlanFile(comp));
		} else if (IModuleConstants.JST_EJB_MODULE.equals(type)) {
			op.createOpenEjbDeploymentPlan(GeronimoUtils
					.getOpenEjbDeploymentPlanFile(comp));
		} else if (IModuleConstants.JST_EAR_MODULE.equals(type)) {
			op.createGeronimoApplicationDeploymentPlan(GeronimoUtils
					.getApplicationDeploymentPlanFile(comp));
		} else if (IModuleConstants.JST_CONNECTOR_MODULE.equals(type)) {
			op.createConnectorDeploymentPlan(GeronimoUtils
					.getConnectorDeploymentPlanFile(comp));
		}
	}

}
