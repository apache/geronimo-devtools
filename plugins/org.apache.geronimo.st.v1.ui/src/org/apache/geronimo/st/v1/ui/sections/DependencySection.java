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
package org.apache.geronimo.st.v1.ui.sections;

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v1.ui.Activator;
import org.apache.geronimo.st.v1.ui.internal.EMFEditorContext;
import org.apache.geronimo.st.v1.ui.wizards.DependencyWizard;
import org.apache.geronimo.xml.ns.deployment.DeploymentPackage;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class DependencySection extends AbstractTableSection {

	/**
	 * Must be a set to a ERef type of XPackage.eINSTANCE.getXType_Dependency();
	 */
	private EReference dependenciesERef;

	/**
	 * @param plan
	 * @param parent
	 * @param toolkit
	 * @param style
	 */
	public DependencySection(EObject plan, EReference dependenciesERef, Composite parent, FormToolkit toolkit, int style) {
		super(plan, parent, toolkit, style);
		this.dependenciesERef = dependenciesERef;
		createClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTitle()
	 */
	public String getTitle() {
		return CommonMessages.editorSectionDependenciesTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getDescription()
	 */
	public String getDescription() {
		return CommonMessages.editorSectionDependenciesDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getEReference()
	 */
	public EReference getEReference() {
		return dependenciesERef;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getWizard()
	 */
	public Wizard getWizard() {
		return new DependencyWizard(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#isHeaderVisible()
	 */
	public boolean isHeaderVisible() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return Activator.imageDescriptorFromPlugin("org.eclipse.jdt.ui", "icons/full/obj16/jar_obj.gif");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTableEntryObjectType()
	 */
	public EClass getTableEntryObjectType() {
		return DeploymentPackage.eINSTANCE.getDependencyType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#filter(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	protected boolean filter(Viewer viewer, Object parentElement, Object element) {
		if (super.filter(viewer, parentElement, element)) {
			return ((EList) getPlan().eGet(getEReference())).contains(element);
		}
		return false;
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
