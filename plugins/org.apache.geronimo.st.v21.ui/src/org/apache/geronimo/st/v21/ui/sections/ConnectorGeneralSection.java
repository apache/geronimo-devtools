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
package org.apache.geronimo.st.v21.ui.sections;

import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorPackage;
import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ConnectorGeneralSection extends CommonGeneralSection {

	ConnectorType plan;

	public ConnectorGeneralSection(Composite parent, FormToolkit toolkit, int style, EObject plan) {
		super(parent, toolkit, style, plan);
		this.plan = (ConnectorType) plan;
		createClient();
	}

	protected EReference getEnvironmentEReference() {
		return ConnectorPackage.eINSTANCE.getConnectorType_Environment();
	}
}
