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

import org.apache.geronimo.jaxbmodel.common.operations.ConversionHelper;
import org.apache.geronimo.st.core.Activator;
import org.apache.geronimo.st.core.GeronimoUtils;
import org.apache.geronimo.st.core.internal.Trace;
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

    private final IFile plan;
    
    /**
     * 
     */
    public ImportDeploymentPlanOperation() {
        super();
        this.plan = null;
        Trace.tracePoint("Constructor", "ImportDeploymentPlanOperation", Activator.traceOperations);
    }

    /**
     * @param model
     */
    public ImportDeploymentPlanOperation(IDataModel model) {
        super(model);
        this.plan = null;
        Trace.tracePoint("Constructor", Activator.traceOperations, "ImportDeploymentPlanOperation", model);
    }
    
    /**
     * @param model
     */
    public ImportDeploymentPlanOperation(IDataModel model, IFile plan) {
        super(model);
        this.plan = plan;
        Trace.tracePoint("Constructor", Activator.traceOperations, "ImportDeploymentPlanOperation", model, plan);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.commands.operations.AbstractOperation#execute(org.eclipse.core.runtime.IProgressMonitor,
     *      org.eclipse.core.runtime.IAdaptable)
     */
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        Trace.tracePoint("Entry", Activator.traceOperations, "ImportDeploymentPlanOperation.execute", monitor, info);

        if (!isGeronimoRuntimeTarget())
            return Status.OK_STATUS;

        IVirtualComponent comp = ComponentCore.createComponent(getProject());
        String type = J2EEProjectUtilities.getJ2EEProjectType(getProject());

        IFile planFile = null;

        try {
            if (type.equals(IModuleConstants.JST_WEB_MODULE)) {
                planFile = (plan == null) ? GeronimoUtils.getWebDeploymentPlanFile(comp) : plan;
                ConversionHelper.convertGeronimoWebFile(planFile);    
            }
            else if (type.equals(IModuleConstants.JST_EJB_MODULE)) {
                planFile = (plan == null) ? GeronimoUtils.getOpenEjbDeploymentPlanFile(comp) : plan;
                ConversionHelper.convertOpenEjbJarFile(planFile);
            }
            else if (type.equals(IModuleConstants.JST_EAR_MODULE)) {
                planFile = (plan == null) ? GeronimoUtils.getApplicationDeploymentPlanFile(comp) : plan;
                ConversionHelper.convertGeronimoApplicationFile(planFile);
            }
            else if (type.equals(IModuleConstants.JST_CONNECTOR_MODULE)) {
                planFile = (plan == null) ? GeronimoUtils.getConnectorDeploymentPlanFile(comp) : plan;
                ConversionHelper.convertGeronimoRaFile(planFile);
            }
            else if (type.equals(IModuleConstants.JST_APPCLIENT_MODULE)) {
                planFile = (plan == null) ? GeronimoUtils.getApplicationClientDeploymentPlanFile(comp) : plan;
                ConversionHelper.convertGeronimoApplicationClientFile(planFile);
            }
        }
        catch (Exception e) {
            throw new ExecutionException("ImportDeploymentPlanOperation.execute(): Error converting plan: " + planFile.getFullPath(), e);
        }

        Trace.tracePoint("Exit ", Activator.traceOperations, "ImportDeploymentPlanOperation.execute", Status.OK_STATUS);
        return Status.OK_STATUS;
    }
}
