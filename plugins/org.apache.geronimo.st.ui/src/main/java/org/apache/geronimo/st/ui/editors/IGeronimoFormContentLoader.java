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
package org.apache.geronimo.st.ui.editors;

import javax.xml.bind.JAXBElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelProvider;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

/**
 * @version $Rev$ $Date$
 */
public interface IGeronimoFormContentLoader {

	public JAXBElement loadDeploymentPlan(IFile file) throws Exception;

	public void saveDeploymentPlan(JAXBElement deploymentPlan, IFile file)
	        throws Exception;

	public void doAddPages(FormEditor editor) throws PartInitException;

	public StructuredTextEditor getDeploymentPlanSourcePage(AbstractGeronimoDeploymentPlanEditor editor);

	public IDataModelOperation getImportDeploymentPlanOperation(
	        IDataModel model);

	public IDataModelProvider getImportDeploymentPlanDataModelProvider();
	
	public void refreshPage(IFormPage page);

	public boolean isValidPage(IFormPage page);
	
}
