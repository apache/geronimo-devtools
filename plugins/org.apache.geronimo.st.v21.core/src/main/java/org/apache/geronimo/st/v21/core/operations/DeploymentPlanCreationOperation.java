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
package org.apache.geronimo.st.v21.core.operations;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.core.operations.AbstractGeronimoJ2EEComponentOperation;
import org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp;
import org.apache.geronimo.st.v21.core.Activator;
import org.apache.geronimo.st.v21.core.GeronimoUtils;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;


/**
 * @version $Rev: 817996 $ $Date: 2009-09-23 16:04:12 +0800 (Wed, 23 Sep 2009) $
 */
public abstract class DeploymentPlanCreationOperation extends
		AbstractGeronimoJ2EEComponentOperation implements
		IDeploymentPlanCreationOp {
	
	protected Object config;

	public DeploymentPlanCreationOperation(IDataModel model, Object config) {
		super(model);
		this.config = config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.operations.AbstractOperation#execute(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.runtime.IAdaptable)
	 */
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		try {
			execute();
		}catch (Exception e){
			return new Status(IStatus.ERROR,Activator.PLUGIN_ID,"Error in creating deployment plan",e);
		}
		return Status.OK_STATUS;
	}

	public void execute() throws Exception {
		IVirtualComponent comp = ComponentCore.createComponent(getProject());

		String type = J2EEProjectUtilities.getJ2EEProjectType(getProject());

		if (IModuleConstants.JST_WEB_MODULE.equals(type)) {
			createGeronimoWebDeploymentPlan(GeronimoUtils.getWebDeploymentPlanFile(comp));			
		} else if (IModuleConstants.JST_EJB_MODULE.equals(type)) {
			createOpenEjbDeploymentPlan(GeronimoUtils.getOpenEjbDeploymentPlanFile(comp));
		} else if (IModuleConstants.JST_EAR_MODULE.equals(type)) {
			createGeronimoApplicationDeploymentPlan(GeronimoUtils.getApplicationDeploymentPlanFile(comp));
		} else if (IModuleConstants.JST_APPCLIENT_MODULE.equals(type)) {
			createGeronimoApplicationClientDeploymentPlan(GeronimoUtils.getApplicationClientDeploymentPlanFile(comp));
		} else if (IModuleConstants.JST_CONNECTOR_MODULE.equals(type)) {
			createConnectorDeploymentPlan(GeronimoUtils.getConnectorDeploymentPlanFile(comp));
		} else if (IModuleConstants.JST_UTILITY_MODULE.equals(type)) {
			createServiceDeploymentPlan(GeronimoUtils.getServiceDeploymentPlanFile(comp));
		}
	}

	
	public JAXBElement createOpenEjbDeploymentPlan(IFile file) throws Exception {
		return null;
	}

	public JAXBElement createGeronimoWebDeploymentPlan(IFile file) throws Exception {
		return null;
	}

	public JAXBElement createGeronimoApplicationDeploymentPlan(IFile file) throws Exception {
		return null;
	}

	public JAXBElement createGeronimoApplicationClientDeploymentPlan(IFile file) throws Exception{
		return null;
	}

	public JAXBElement createConnectorDeploymentPlan(IFile file) throws Exception{
		return null;
	}
	

	public JAXBElement createServiceDeploymentPlan(IFile file) throws Exception{
		return null;
	}
}
