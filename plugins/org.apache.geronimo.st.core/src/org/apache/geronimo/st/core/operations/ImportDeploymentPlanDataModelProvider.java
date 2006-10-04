package org.apache.geronimo.st.core.operations;

import java.util.Set;

import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;

public class ImportDeploymentPlanDataModelProvider extends AbstractDataModelProvider {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider#getPropertyNames()
	 */
	public Set getPropertyNames() {
		Set names = super.getPropertyNames();
		names.add(IFacetDataModelProperties.FACET_PROJECT_NAME);
		names.add(IFacetProjectCreationDataModelProperties.FACET_RUNTIME);
		return names;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider#getDefaultOperation()
	 */
	public IDataModelOperation getDefaultOperation() {
		return new ImportDeploymentPlanOperation(model);
	}

}
