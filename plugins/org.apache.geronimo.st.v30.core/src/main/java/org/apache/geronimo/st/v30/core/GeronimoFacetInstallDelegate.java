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
package org.apache.geronimo.st.v30.core;

import org.apache.geronimo.st.v30.core.internal.Trace;
import org.apache.geronimo.st.v30.core.operations.DeploymentPlanCreationOperation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.common.project.facet.JavaProjectFacetCreationDataModelProvider;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoFacetInstallDelegate implements IDelegate {

    public static final String FACET_ID = "org.apache.geronimo.facet";

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.common.project.facet.core.IDelegate#execute(org.eclipse.core.resources.IProject,
     *      org.eclipse.wst.common.project.facet.core.IProjectFacetVersion,
     *      java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
     */
    public void execute(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor) throws CoreException {

        try {
            createDeploymentPlanCreationOp(project, config).execute(monitor, null);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        IProject[] ears = J2EEProjectUtilities.getReferencingEARProjects(project);
        IProjectFacet facet = ProjectFacetsManager.getProjectFacet(FACET_ID);
        for (int i = 0; i < ears.length; i++) {
            IFacetedProject fp = ProjectFacetsManager.create(ears[i]);
            if (!fp.hasProjectFacet(facet)) {
                try {
                    createDeploymentPlanCreationOp(ears[i], config).execute(monitor, null);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public IDataModelOperation createDeploymentPlanCreationOp(IProject project, Object config) {
        Trace.tracePoint("Entry", "GeronimoFacetInstallDelegate.createDeploymentPlanCreationOp", project, config);
        
        IDataModel model = DataModelFactory.createDataModel(new JavaProjectFacetCreationDataModelProvider());
        model.setStringProperty(IFacetDataModelProperties.FACET_PROJECT_NAME, project.getName());
        
        Trace.tracePoint("Exit ", "GeronimoFacetInstallDelegate.createDeploymentPlanCreationOp");
        return new DeploymentPlanCreationOperation(model, config);       
    }
}
