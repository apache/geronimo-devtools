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
package org.apache.geronimo.st.core.operations;

import java.io.IOException;
import java.util.Collections;

import org.apache.geronimo.st.core.GeronimoUtils;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public abstract class DeploymentPlanCreationOperation extends
		AbstractGeronimoJ2EEComponentOperation implements
		IDeploymentPlanCreationOp {

	public DeploymentPlanCreationOperation(IDataModel model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.operations.AbstractOperation#execute(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.runtime.IAdaptable)
	 */
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		execute();
		return Status.OK_STATUS;
	}

	public void execute() {
		IVirtualComponent comp = ComponentCore.createComponent(getProject());

		String type = J2EEProjectUtilities.getJ2EEProjectType(getProject());

		if (IModuleConstants.JST_WEB_MODULE.equals(type)) {
			createGeronimoWebDeploymentPlan(GeronimoUtils.getWebDeploymentPlanFile(comp));
		} else if (IModuleConstants.JST_EJB_MODULE.equals(type)) {
			createOpenEjbDeploymentPlan(GeronimoUtils.getOpenEjbDeploymentPlanFile(comp));
		} else if (IModuleConstants.JST_EAR_MODULE.equals(type)) {
			createGeronimoApplicationDeploymentPlan(GeronimoUtils.getApplicationDeploymentPlanFile(comp));
		} else if (IModuleConstants.JST_CONNECTOR_MODULE.equals(type)) {
			createConnectorDeploymentPlan(GeronimoUtils.getConnectorDeploymentPlanFile(comp));
		}
	}

	public void save(Resource resource) {

		if (resource instanceof XMLResource) {
			((XMLResource) resource).setEncoding("UTF-8");
		}

		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
