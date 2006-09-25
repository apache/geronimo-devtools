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

import org.apache.geronimo.st.core.GeronimoUtils;
import org.apache.geronimo.st.core.IGeronimoRuntime;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class ImportDeploymentPlanOperation extends AbstractGeronimoJ2EEComponentOperation {

	/**
	 * 
	 */
	public ImportDeploymentPlanOperation() {
		super();
	}

	/**
	 * @param model
	 */
	public ImportDeploymentPlanOperation(IDataModel model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.operations.AbstractOperation#execute(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.runtime.IAdaptable)
	 */
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		if (!isGeronimoRuntimeTarget())
			return Status.OK_STATUS;

		IGeronimoRuntime runtime = (IGeronimoRuntime) getRuntime().loadAdapter(IGeronimoRuntime.class, null);
		IVirtualComponent comp = ComponentCore.createComponent(getProject());
		String type = J2EEProjectUtilities.getJ2EEProjectType(getProject());

		IFile planFile = null;
		XmlObject plan = null;
		
		try {
			if (type.equals(IModuleConstants.JST_WEB_MODULE)) {
				planFile = GeronimoUtils.getWebDeploymentPlanFile(comp);
				plan = runtime.fixGeronimoWebSchema(planFile);
			} else if (type.equals(IModuleConstants.JST_EJB_MODULE)) {
				planFile = GeronimoUtils.getOpenEjbDeploymentPlanFile(comp);
				runtime.fixGeronimoEjbSchema(planFile);
			} else if (type.equals(IModuleConstants.JST_EAR_MODULE)) {
				planFile = GeronimoUtils.getApplicationDeploymentPlanFile(comp);
				plan = runtime.fixGeronimoEarSchema(planFile);
			} else if (type.equals(IModuleConstants.JST_CONNECTOR_MODULE)) {
				planFile = GeronimoUtils.getConnectorDeploymentPlanFile(comp);
				plan = runtime.fixGeronimoConnectorSchema(planFile);
			}
			
			if (planFile != null && plan != null) {
				save(plan, planFile);
			}
		} catch (XmlException e) {
			throw new ExecutionException("Error fixing plan., e");
		}

		return Status.OK_STATUS;
	}

	private void save(XmlObject object, IFile file) {
		try {
			object.save(file.getLocation().toFile());
			file.refreshLocal(IFile.DEPTH_ONE, null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
