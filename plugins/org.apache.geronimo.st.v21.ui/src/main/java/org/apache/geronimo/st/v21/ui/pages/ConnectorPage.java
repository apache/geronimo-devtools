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
package org.apache.geronimo.st.v21.ui.pages;

import org.apache.geronimo.jee.application.Application;
import org.apache.geronimo.st.ui.editors.AbstractGeronimoJAXBBasedEditor;
import org.apache.geronimo.st.ui.pages.AbstractGeronimoFormPage;
import org.apache.geronimo.st.v21.core.GeronimoServerInfoManager;
import org.apache.geronimo.st.v21.ui.sections.DBPoolSection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * This page is added to a editor for
 * geronimo-application.xml,geronimo-web.xml,openejb-jar.xml It will contain two
 * sections,Database pool section and JMS resource section
 */
public class ConnectorPage extends AbstractGeronimoFormPage {

	public ConnectorPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	@Override
	protected void fillBody(IManagedForm managedForm) {
		Application application = (Application) ((AbstractGeronimoJAXBBasedEditor) getEditor())
				.getRootElement().getValue();
		managedForm.addPart(new DBPoolSection(getDeploymentPlan(), body,
				toolkit, getStyle(), application.getExtModule()));	
	}

	protected GridLayout getLayout() {
		GridLayout layout = super.getLayout();
		layout.numColumns = 1;
		return layout;
	}

	public String getFormTitle() {
		return "Connector";//TODO
	}

	@Override
	protected void triggerGeronimoServerInfoUpdate() {
		GeronimoServerInfoManager.getProvider(getRuntimeVersion()).updateInfo();
	}

}
