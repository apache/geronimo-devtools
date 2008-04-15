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
package org.apache.geronimo.st.core.operations;

import org.apache.geronimo.st.core.GeronimoUtils;
import org.apache.geronimo.st.core.IGeronimoRuntime;
import org.apache.geronimo.st.core.internal.Trace;
import org.apache.geronimo.st.core.jaxb.ConversionHelper;
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
 * @version $Rev$ $Date$
 */
public class ImportDeploymentPlanOperation extends AbstractGeronimoJ2EEComponentOperation {

    /**
     * 
     */
    public ImportDeploymentPlanOperation() {
        super();
        Trace.tracePoint("Constructor", "ImportDeploymentPlanOperation");
    }

    /**
     * @param model
     */
    public ImportDeploymentPlanOperation(IDataModel model) {
        super(model);
        Trace.tracePoint("Constructor", "ImportDeploymentPlanOperation", model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.commands.operations.AbstractOperation#execute(org.eclipse.core.runtime.IProgressMonitor,
     *      org.eclipse.core.runtime.IAdaptable)
     */
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        Trace.tracePoint("Entry", "ImportDeploymentPlanOperation.execute", monitor, info);

        if (!isGeronimoRuntimeTarget())
            return Status.OK_STATUS;

        IGeronimoRuntime runtime = (IGeronimoRuntime) getRuntime().loadAdapter(IGeronimoRuntime.class, null);
        IVirtualComponent comp = ComponentCore.createComponent(getProject());
        String type = J2EEProjectUtilities.getJ2EEProjectType(getProject());

        IFile planFile = null;

        try {
            if (type.equals(IModuleConstants.JST_WEB_MODULE)) {
                planFile = GeronimoUtils.getWebDeploymentPlanFile(comp);
                ConversionHelper.convertGeronimoWebFile(planFile);    
            }
            else if (type.equals(IModuleConstants.JST_EJB_MODULE)) {
                planFile = GeronimoUtils.getOpenEjbDeploymentPlanFile(comp);
                ConversionHelper.convertGeronimoOpenEjbFile(planFile);
            }
            else if (type.equals(IModuleConstants.JST_EAR_MODULE)) {
                planFile = GeronimoUtils.getApplicationDeploymentPlanFile(comp);
                ConversionHelper.convertGeronimoApplicationFile(planFile);
            }
            else if (type.equals(IModuleConstants.JST_CONNECTOR_MODULE)) {
                planFile = GeronimoUtils.getConnectorDeploymentPlanFile(comp);
                ConversionHelper.convertGeronimoRaFile(planFile);
            }
            else if (type.equals(IModuleConstants.JST_APPCLIENT_MODULE)) {
                planFile = GeronimoUtils.getApplicationClientDeploymentPlanFile(comp);
                ConversionHelper.convertGeronimoApplicationClientFile(planFile);
            }
        }
        catch (Exception e) {
            throw new ExecutionException("ImportDeploymentPlanOperation.execute(): Error converting plan: " + planFile.getFullPath() );
        }

        Trace.tracePoint("Exit ", "ImportDeploymentPlanOperation.execute", Status.OK_STATUS);
        return Status.OK_STATUS;
    }
}
