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
package org.apache.geronimo.st.v11.ui.sections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openejb.xml.ns.openejb.jar.JarPackage;
import org.openejb.xml.ns.openejb.jar.OpenejbJarType;

public class OpenEjbJarGeneralSection extends CommonGeneralSection {

	OpenejbJarType plan;

	public OpenEjbJarGeneralSection(Composite parent, FormToolkit toolkit, int style, EObject plan) {
		super(parent, toolkit, style, plan);
		this.plan = (OpenejbJarType) plan;
		createClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.v11.ui.sections.CommonGeneralSection#getEnvironmentEReference()
	 */
	protected EReference getEnvironmentEReference() {
		return JarPackage.eINSTANCE.getOpenejbJarType_Environment();
	}
}
