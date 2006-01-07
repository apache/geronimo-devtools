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
import java.util.List;

import org.apache.geronimo.ui.internal.GeronimoUIPlugin;
import org.apache.geronimo.ui.internal.Messages;
import org.apache.geronimo.ui.wizards.EjbRefWizard;
import org.apache.geronimo.xml.ns.j2ee.web.provider.WebItemProviderAdapterFactory;
import org.apache.geronimo.xml.ns.naming.NamingPackage;
import org.apache.geronimo.xml.ns.naming.provider.NamingItemProviderAdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class EjbRefSection extends DynamicTableSection {
    
    EReference ejbRefERef;

    /**
     * @param plan
     * @param parent
     * @param toolkit
     * @param style
     */
    public EjbRefSection(EObject plan, Composite parent, FormToolkit toolkit,
            int style, EReference ejbRefERef) {
        super(plan, parent, toolkit, style);
        this.ejbRefERef = ejbRefERef;
        create();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getTitle()
     */
    public String getTitle() {
        return Messages.editorEjbRefTitle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getDescription()
     */
    public String getDescription() {
        return Messages.editorEjbRefDescription;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getEReference()
     */
    public EReference getEReference() {
        return ejbRefERef;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getTableColumnNames()
     */
    public String[] getTableColumnNames() {
        return new String[] { Messages.editorEjbRefTargetName,
                Messages.editorEjbRefEjbLink };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getWizard()
     */
    public Wizard getWizard() {
        return new EjbRefWizard(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.DynamicTableSection#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
        return GeronimoUIPlugin.imageDescriptorFromPlugin(
                "org.eclipse.jst.j2ee", "icons/full/obj16/ejbRef_obj.gif");
    }
    
    public List getFactories() {
    	List factories = new ArrayList();
		factories.add(new WebItemProviderAdapterFactory());
		factories.add(new NamingItemProviderAdapterFactory());
		return factories;
    }

	public EClass getTableEntryObjectType() {
		return NamingPackage.eINSTANCE.getEjbRefType();
	}

}
