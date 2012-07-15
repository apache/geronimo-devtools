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
package org.apache.geronimo.st.v30.core.facets;

import org.apache.geronimo.st.v30.core.Activator;
import org.apache.geronimo.st.v30.core.internal.Trace;
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
 * <b>GeronimoJEEFacetInstallDelegate</b> implements any Geronimo-specific functions for JEE projects when the 
 *    project is created, which is mainly to create a Geronimo deployment plan
 * 
 * @version $Rev$ $Date$
 */
public class GeronimoJEEFacetInstallDelegate implements IDelegate {

    public static final String FACET_ID = "org.apache.geronimo.facet";

    /**
     * The method that's called to execute the delegate
     * 
     * @param project   The workspace project
     * @param fv        The project facet version that this delegate is handling; this
     *                  is useful when sharing the delegate among several versions of the same
     *                  project facet or even different project facets
     * @param config    The configuration object, or null if defaults should be used
     * @param monitor   The progress monitor
     * 
     * @throws CoreException if the delegate fails for any reason
     */
    public void execute(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor) throws CoreException {
        Trace.tracePoint("Entry", Activator.traceFacets, "GeronimoJEEFacetInstallDelegate.execute", project, fv, config, monitor);

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

        Trace.tracePoint("Exit ", Activator.traceFacets, "GeronimoJEEFacetInstallDelegate.execute");
    }

    public IDataModelOperation createDeploymentPlanCreationOp(IProject project, Object config) {
        Trace.tracePoint("Entry", Activator.traceFacets, "GeronimoJEEFacetInstallDelegate.createDeploymentPlanCreationOp", project, config);
        
        IDataModel model = DataModelFactory.createDataModel(new JavaProjectFacetCreationDataModelProvider());
        model.setStringProperty(IFacetDataModelProperties.FACET_PROJECT_NAME, project.getName());
        
        Trace.tracePoint("Exit ", Activator.traceFacets, "GeronimoJEEFacetInstallDelegate.createDeploymentPlanCreationOp");
        return new DeploymentPlanCreationOperation(model, config);       
    }
}
