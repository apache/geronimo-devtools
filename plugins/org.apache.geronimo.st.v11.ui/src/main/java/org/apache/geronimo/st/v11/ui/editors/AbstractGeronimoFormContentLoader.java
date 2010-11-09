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
package org.apache.geronimo.st.v11.ui.editors;

import java.io.IOException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.geronimo.st.ui.editors.IGeronimoFormContentLoader;
import org.apache.geronimo.st.v11.core.GeronimoUtils;
import org.apache.geronimo.st.core.jaxb.JAXBUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * @version $Rev: 817996 $ $Date: 2009-09-23 16:04:12 +0800 (Wed, 23 Sep 2009) $
 */
public abstract class AbstractGeronimoFormContentLoader implements IGeronimoFormContentLoader {

	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.ui.editors.IGeronimoFormContentLoader#doAddPages(org.eclipse.ui.forms.editor.FormEditor)
	 */
	public void doAddPages(FormEditor editor) throws PartInitException{
        triggerGeronimoServerInfoUpdate();
		IEditorInput input = editor.getEditorInput();
		if(input instanceof IFileEditorInput) {
			String planFileName = ((IFileEditorInput) input).getFile().getName();
			if(GeronimoUtils.WEB_PLAN_NAME.equals(planFileName)) {
				addWebPlanPages(editor);
			} else if(GeronimoUtils.OPENEJB_PLAN_NAME.equals(planFileName)) {
				addOpenEjbPlanPages(editor);
			} else if(GeronimoUtils.CONNECTOR_PLAN_NAME.equals(planFileName)) {
				addConnectorPlanPages(editor);
			} else if(GeronimoUtils.APP_PLAN_NAME.equals(planFileName)) {
				addApplicationPlanPages(editor);
			} else if(GeronimoUtils.APP_CLIENT_PLAN_NAME.equals(planFileName)) {
				addApplicationClientPlanPages(editor);
			}
		}
	}
	
	abstract public void triggerGeronimoServerInfoUpdate() throws PartInitException;
	
	abstract public void addWebPlanPages(FormEditor editor) throws PartInitException;
	
	abstract public void addOpenEjbPlanPages(FormEditor editor) throws PartInitException;
	
	abstract public void addConnectorPlanPages(FormEditor editor) throws PartInitException;
	
	abstract public void addApplicationPlanPages(FormEditor editor) throws PartInitException;

	abstract public void addApplicationClientPlanPages(FormEditor editor) throws PartInitException;

	public void saveDeploymentPlan(JAXBElement deploymentPlan, IFile file) throws Exception {
		JAXBUtils.marshalDeploymentPlan(deploymentPlan, file);
	}

}
