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
package org.apache.geronimo.ui.sections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.geronimo.ui.internal.Messages;
import org.apache.geronimo.ui.wizards.GBeanWizard;
import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;
import org.apache.geronimo.xml.ns.deployment.DeploymentPackage;
import org.apache.geronimo.xml.ns.deployment.provider.DeploymentItemProviderAdapterFactory;
import org.apache.geronimo.xml.ns.j2ee.web.provider.WebItemProviderAdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class GBeanSection extends DynamicTableSection {

    private EReference gBeanERef;

    /**
     * @param plan
     * @param parent
     * @param toolkit
     * @param style
     */
    public GBeanSection(EObject plan, EReference gBeanERef, Composite parent,
            FormToolkit toolkit, int style) {
        super(plan, parent, toolkit, style);
        this.gBeanERef = gBeanERef;
        createNew();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getTitle()
     */
    public String getTitle() {
        return Messages.editorSectionGBeanTitle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getDescription()
     */
    public String getDescription() {
        return Messages.editorSectionGBeanDescription;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getEFactory()
     */
    public EFactory getEFactory() {
        return DeploymentFactory.eINSTANCE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getEReference()
     */
    public EReference getEReference() {
        return gBeanERef;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getTableColumnNames()
     */
    public String[] getTableColumnNames() {
        return new String[] { Messages.name, Messages.GbeanName,
                Messages.className };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getTableColumnEAttributes()
     */
    public EAttribute[] getTableColumnEAttributes() {
        return new EAttribute[] {
                DeploymentPackage.eINSTANCE.getGbeanType_Name(),
                DeploymentPackage.eINSTANCE.getGbeanType_GbeanName(),
                DeploymentPackage.eINSTANCE.getGbeanType_Class() };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getWizard()
     */
    public Wizard getWizard() {
        return new GBeanWizard(this);
    }
    
    public List getFactories() {
    	List factories = new ArrayList();
		factories.add(new WebItemProviderAdapterFactory());
		factories.add(new DeploymentItemProviderAdapterFactory());
		return factories;
    }

	public EClass getTableEntryObjectType() {
		return DeploymentPackage.eINSTANCE.getGbeanType();
	}

}
