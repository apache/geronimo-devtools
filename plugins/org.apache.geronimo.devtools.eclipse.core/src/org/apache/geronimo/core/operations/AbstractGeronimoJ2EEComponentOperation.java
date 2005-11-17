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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.j2ee.application.internal.operations.J2EEComponentCreationDataModelProvider;
import org.eclipse.wst.common.componentcore.datamodel.properties.IComponentCreationDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public abstract class AbstractGeronimoJ2EEComponentOperation extends
		AbstractDataModelOperation {

	/**
	 * 
	 */
	public AbstractGeronimoJ2EEComponentOperation() {
		super();
	}

	/**
	 * @param model
	 */
	public AbstractGeronimoJ2EEComponentOperation(IDataModel model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.operations.AbstractOperation#redo(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.runtime.IAdaptable)
	 */
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.operations.AbstractOperation#undo(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.runtime.IAdaptable)
	 */
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return null;
	}

	public boolean isGeronimoRuntimeTarget() {
		String runtimeID = model
				.getStringProperty(J2EEComponentCreationDataModelProvider.RUNTIME_TARGET_ID);
		return runtimeID != null && runtimeID.startsWith("Apache Geronimo");
		/*
		 * try { IFacetedProject p = ProjectFacetsManager.create(getProject());
		 * return p.getRuntime().getName().startsWith("Apache Geronimo"); }
		 * catch (CoreException e) { e.printStackTrace(); } return false;
		 */
	}

	public String getComponentName() {
		return model.getProperty(
				IComponentCreationDataModelProperties.COMPONENT_NAME)
				.toString();
	}

	public IProject getProject() {
		String projectName = model.getProperty(
				IComponentCreationDataModelProperties.PROJECT_NAME)
				.toString();
		if (projectName != null) {
			return ResourcesPlugin.getWorkspace().getRoot().getProject(
					projectName);
		}
		return null;
	}

}
