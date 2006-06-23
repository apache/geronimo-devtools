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
package org.apache.geronimo.st.v1.ui.sections;

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v1.ui.internal.EMFEditorContext;
import org.apache.geronimo.st.v1.ui.wizards.GBeanRefWizard;
import org.apache.geronimo.xml.ns.naming.NamingPackage;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class GBeanRefSection extends AbstractTableSection {

	EReference gbeanERef;

	private static final String[] COLUMN_NAMES = new String[] {
		CommonMessages.editorGBeanRefName, CommonMessages.editorGBeanRefType,
		CommonMessages.editorGBeanRefProxyType };

	public GBeanRefSection(EObject plan, Composite parent, FormToolkit toolkit, int style, EReference gbeanERef) {
		super(plan, parent, toolkit, style);
		this.gbeanERef = gbeanERef;
		createClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTitle()
	 */
	public String getTitle() {
		return CommonMessages.editorGBeanRefTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getDescription()
	 */
	public String getDescription() {
		return CommonMessages.editorGBeanRefDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTableColumnNames()
	 */
	public String[] getTableColumnNames() {
		return COLUMN_NAMES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getEReference()
	 */
	public EReference getEReference() {
		return gbeanERef;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getWizard()
	 */
	public Wizard getWizard() {
		return new GBeanRefWizard(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTableEntryObjectType()
	 */
	public EClass getTableEntryObjectType() {
		return NamingPackage.eINSTANCE.getGbeanRefType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.ui.sections.AbstractTableSection#getAdapterFactory()
	 */
	public AdapterFactory getAdapterFactory() {
		return EMFEditorContext.getFactory();
	}
}
