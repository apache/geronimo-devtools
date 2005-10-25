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
package org.apache.geronimo.core.operations;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.geronimo.core.internal.GeronimoUtils;
import org.apache.geronimo.deployment.xmlbeans.XmlBeansUtil;
import org.apache.geronimo.schema.SchemaConversionUtils;
import org.apache.geronimo.xbeans.geronimo.GerConnectorType;
import org.apache.geronimo.xbeans.geronimo.j2ee.GerApplicationType;
import org.apache.geronimo.xbeans.geronimo.web.GerWebAppType;
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
import org.openejb.xbeans.ejbjar.OpenejbOpenejbJarType;

public class ImportDeploymentPlanOperation extends
        AbstractGeronimoJ2EEComponentOperation {

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
    public IStatus execute(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
        if (!isGeronimoRuntimeTarget())
            return Status.OK_STATUS;

        IVirtualComponent comp = ComponentCore.createComponent(getProject());
        String type = J2EEProjectUtilities.getJ2EEProjectType(getProject());

        try {
            if (type.equals(
                    IModuleConstants.JST_WEB_MODULE)) {
                importWebDeploymentPlan(GeronimoUtils
                        .getWebDeploymentPlanFile(comp));
            } else if (type.equals(
                    IModuleConstants.JST_EJB_MODULE)) {
                importEjbDeploymentPlan(GeronimoUtils
                        .getOpenEjbDeploymentPlanFile(comp));
            } else if (type.equals(
                    IModuleConstants.JST_EAR_MODULE)) {
                importEarDeploymentPlan(GeronimoUtils
                        .getApplicationDeploymentPlanFile(comp));
            } else if (type.equals(
                    IModuleConstants.JST_CONNECTOR_MODULE)) {
                importConnectorDeploymentPlan(GeronimoUtils
                        .getConnectorDeploymentPlanFile(comp));
            }
        } catch (XmlException e) {
            e.printStackTrace();
        }

        return Status.OK_STATUS;
    }

    public void importWebDeploymentPlan(IFile dpFile) throws XmlException {
        XmlObject plan = getXmlObject(dpFile);
        if (plan != null) {
            SchemaConversionUtils.fixGeronimoSchema(plan, "web-app",
                    GerWebAppType.type);
            save(plan, dpFile);
        }
    }

    public void importEarDeploymentPlan(IFile dpFile) throws XmlException {
        XmlObject plan = getXmlObject(dpFile);
        if (plan != null) {
            SchemaConversionUtils.fixGeronimoSchema(plan, "application",
                    GerApplicationType.type);
            save(plan, dpFile);
        }
    }

    public void importEjbDeploymentPlan(IFile dpFile) throws XmlException {
        XmlObject plan = getXmlObject(dpFile);
        if (plan != null) {
            SchemaConversionUtils.fixGeronimoSchema(plan, "openejb-jar",
                    OpenejbOpenejbJarType.type);
            save(plan, dpFile);
        }
    }

    public void importConnectorDeploymentPlan(IFile dpFile) throws XmlException {
        XmlObject plan = getXmlObject(dpFile);
        if (plan != null) {
            SchemaConversionUtils.fixGeronimoSchema(plan, "connector",
                    GerConnectorType.type);
            save(plan, dpFile);
        }
    }

    private XmlObject getXmlObject(IFile dpFile) {
        if (dpFile.exists()) {
            try {
                return XmlBeansUtil
                        .parse(dpFile.getLocation().toFile().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlException e) {
                e.printStackTrace();
            }
        }
        return null;
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
