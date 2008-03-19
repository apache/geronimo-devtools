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

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.ui.Activator;
import org.apache.geronimo.st.v21.ui.wizards.EjbLocalRefWizard;
import org.apache.geronimo.xml.ns.naming_1.EjbLocalRefType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class EjbLocalRefSection extends AbstractTableSection {

	List ejbLocalRefERef;

	private static final String[] COLUMN_NAMES = new String[] {
			CommonMessages.name,
			CommonMessages.editorEjbRefEjbLink };

	public EjbLocalRefSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, List ejbLocalRefERef) {
		super(plan, parent, toolkit, style);
		this.ejbLocalRefERef = ejbLocalRefERef;
		createClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTitle()
	 */
	public String getTitle() {
		return CommonMessages.editorEjbLocalRefTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getDescription()
	 */
	public String getDescription() {
		return CommonMessages.editorEjbLocalRefDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTableColumnNames()
	 */
	public String[] getTableColumnNames() {
		return COLUMN_NAMES;
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getEReference()
//	 */
//	public JAXBElement getEReference() {
//		return ejbLocalRefERef;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getWizard()
	 */
	public Wizard getWizard() {
		return new EjbLocalRefWizard(this);
	}

	public ImageDescriptor getImageDescriptor() {
		return Activator.imageDescriptorFromPlugin("org.eclipse.jst.j2ee", "icons/full/obj16/ejb_local_ref_obj.gif");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTableEntryObjectType()
	 */
	public Class getTableEntryObjectType() {
		return EjbLocalRefType.class;
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.apache.geronimo.st.ui.sections.AbstractTableSection#getAdapterFactory()
//	 */
//	public AdapterFactory getAdapterFactory() {
//		return EMFEditorContext.getFactory();
//	}
}
