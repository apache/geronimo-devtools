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

import org.apache.geronimo.st.core.GeronimoFacetInstallDelegate;
import org.apache.geronimo.st.v21.core.Activator;
import org.apache.geronimo.st.v21.core.internal.Trace;
import org.eclipse.core.resources.IProject;
import org.eclipse.jst.common.project.facet.JavaProjectFacetCreationDataModelProvider;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoV21FacetInstallDelegate extends GeronimoFacetInstallDelegate {

	public IDataModelOperation createDeploymentPlanCreationOp(IProject project, Object config) {
		Trace.tracePoint("Entry", Activator.traceOperations, "GeronimoV21FacetInstallDelegate.createDeploymentPlanCreationOp", project, config);
		
		IDataModel model = DataModelFactory.createDataModel(new JavaProjectFacetCreationDataModelProvider());
		model.setStringProperty(IFacetDataModelProperties.FACET_PROJECT_NAME, project.getName());
		
		Trace.tracePoint("Exit ", Activator.traceOperations, "GeronimoV21FacetInstallDelegate.createDeploymentPlanCreationOp");
		return new V21DeploymentPlanCreationOperation(model, config);		
	}
}
