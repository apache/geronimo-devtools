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

import org.apache.geronimo.ui.internal.GeronimoUIPlugin;
import org.apache.geronimo.ui.internal.Messages;
import org.apache.geronimo.ui.wizards.DependencyWizard;
import org.apache.geronimo.xml.ns.deployment.DependencyType;
import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;
import org.apache.geronimo.xml.ns.deployment.DeploymentPackage;
import org.apache.geronimo.xml.ns.deployment.provider.DeploymentItemProviderAdapterFactory;
import org.apache.geronimo.xml.ns.j2ee.web.provider.WebItemProviderAdapterFactory;
import org.apache.geronimo.xml.ns.naming.provider.NamingItemProviderAdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class DependencySection extends DynamicTableSection {

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
    public DependencySection(EObject plan, EReference dependenciesERef,
            Composite parent, FormToolkit toolkit, int style) {
        super(plan, parent, toolkit, style);
        this.dependenciesERef = dependenciesERef;
        createNew();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getTitle()
     */
    public String getTitle() {
        return Messages.editorSectionDependenciesTitle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getDescription()
     */
    public String getDescription() {
        return Messages.editorSectionDependenciesDescription;
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
        return dependenciesERef;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getTableColumnNames()
     */
    public String[] getTableColumnNames() {
        return new String[] {};
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getTableColumnEAttributes()
     */
    public EAttribute[] getTableColumnEAttributes() {
        return new EAttribute[] {};
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getWizard()
     */
    public Wizard getWizard() {
        return new DependencyWizard(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#configureSection(org.eclipse.ui.forms.widgets.Section)
     */
    protected void configureSection(Section section) {
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#showTableColumNames()
     */
    public boolean isHeaderVisible() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#fillTableItems()
     */
    /*protected void fillTableItems() {
        EList list = (EList) getPlan().eGet(getEReference());

        for (int j = 0; j < list.size(); j++) {
            TableItem item = new TableItem(table, SWT.NONE);
            String[] tableTextData = getTableText((EObject) list.get(j));
            item.setImage(getImage());
            item.setText(tableTextData);
            item.setData((EObject) list.get(j));
        }

    }*/


    /* (non-Javadoc)
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getTableText(org.eclipse.emf.ecore.EObject)
     */
    /*public String[] getTableText(EObject eObject) {
        DependencyType dt = (DependencyType) eObject;
        if (dt.eIsSet(DeploymentPackage.eINSTANCE.getDependencyType_Uri())) {
            return new String[] { dt.getUri() };
        } else {
            return new String[] { dt.getGroupId() + "/" + dt.getArtifactId()
                    + "-" + dt.getVersion() + ".jar" };
        }
    }*/
    
    /* (non-Javadoc)
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getImageDescriptor()
     */
    /*public ImageDescriptor getImageDescriptor() {
        return GeronimoUIPlugin.imageDescriptorFromPlugin(
                "org.eclipse.jdt.ui", "icons/full/obj16/jar_obj.gif");
    }*/
    
    public List getFactories() {
    	List factories = new ArrayList();
		factories.add(new WebItemProviderAdapterFactory());
		factories.add(new DeploymentItemProviderAdapterFactory());
		return factories;
    }

	public EClass getTableEntryObjectType() {
		return DeploymentPackage.eINSTANCE.getDependencyType();
	}

}
