/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.geronimo.st.v21.ui.sections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.deployment.Attribute;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.deployment.XmlAttributeType;
import org.apache.geronimo.jee.loginconfig.LoginConfig;
import org.apache.geronimo.jee.loginconfig.LoginModule;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.ui.pages.SecurityPage;
import org.apache.geronimo.st.v21.ui.wizards.SecurityRealmWizard;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class SecurityRealmSection extends AbstractTableSection {
    public SecurityRealmSection(JAXBElement plan, List gbeans,
	    Composite parent, FormToolkit toolkit, int style) {
	super(plan, parent, toolkit, style);
	this.objectContainer = gbeans;
	COLUMN_NAMES = new String[] { "Security Realm Name",
		"Login Module Class" };// TODO put into message
	createClient();
    }

    @Override
    public String getDescription() {
	return "The following security realms are defined:";// TODO put into
	// message
    }

    @Override
    public Class getTableEntryObjectType() {
	return JAXBElement.class;
    }

    @Override
    public String getTitle() {
	return "Security Realm";// TODO put into message
    }

    @Override
    protected Wizard getWizard() {
	return new SecurityRealmWizard(this);
    }

    protected void notifyOthers() {
	notifyGBeanSectionToRefresh();
    }

    /*
     * After add, remove, edit realm gbean, notify the gbean section to refresh.
     * If the deploymentpage has not been initialized, catch a
     * NullPointerException and just ignore it.
     */
    private void notifyGBeanSectionToRefresh() {
	try {
	    SecurityPage securityPage = (SecurityPage) this.getManagedForm()
		    .getContainer();
	    FormEditor editor = securityPage.getEditor();
	    IFormPart[] parts = editor.findPage("deploymentpage")
		    .getManagedForm().getParts();
	    GBeanSection gbeanSection = null;
	    for (IFormPart part : parts) {
		if (GBeanSection.class.isInstance(part)) {
		    gbeanSection = (GBeanSection) part;
		}
	    }
	    gbeanSection.getViewer().refresh();
	} catch (NullPointerException e) {
	    // Ignore, this exception happens when the deployment page hasn't
	    // been initialized
	}
    }

    @Override
    public ITreeContentProvider getContentProvider() {
	return new ContentProvider() {
	    @Override
	    public Object[] getElements(Object inputElement) {
		List<JAXBElement<?>> result = new ArrayList<JAXBElement<?>>();
		List<?> list = getObjectContainer();
		for (Iterator<?> it = list.iterator(); it.hasNext();) {
		    JAXBElement<?> current = (JAXBElement<?>) it.next();
		    if (isSecurityRealmGbean((Gbean) current.getValue())) {
			result.add(current);
		    }
		}
		return result.toArray();
	    }
	};
    }

    private boolean isSecurityRealmGbean(Gbean gbean) {
	return "org.apache.geronimo.security.realm.GenericSecurityRealm"
		.equals(gbean.getClazz());
    }

    @Override
    public ITableLabelProvider getLabelProvider() {
	return new LabelProvider() {
	    @Override
	    public String getColumnText(Object element, int columnIndex) {
		// System.out.println("here");
		if (JAXBElement.class.isInstance(element)) {
		    Object value = ((JAXBElement<?>) element).getValue();
		    if (Gbean.class.isInstance(value)) {
			Gbean gbean = (Gbean) value;
			switch (columnIndex) {
			case 0:// security realm name
			    if (getGbeanAttributeValue(gbean, "realmName") != null) {
				return getGbeanAttributeValue(gbean,
					"realmName");
			    }
			    return "";
			case 1:// login module class
			    if (getSecurityRealmGbeanLoginModuleClass(gbean) != null) {
				return getSecurityRealmGbeanLoginModuleClass(gbean);
			    }
			    return "";
			}
		    }
		}
		return null;
	    }
	};
    }

    private String getGbeanAttributeValue(Gbean gbean, String attributeName) {
	try {
	    List<JAXBElement<?>> elelist = gbean
		    .getAttributeOrXmlAttributeOrReference();
	    for (JAXBElement<?> ele : elelist) {
		if (Attribute.class.isInstance(ele.getValue())
			&& ((Attribute) ele.getValue()).getName().equals(
				attributeName)) {
		    return ((Attribute) ele.getValue()).getValue();
		}
	    }
	} catch (NullPointerException e) {
	    // ignore
	}
	return null;
    }

    private String getSecurityRealmGbeanLoginModuleClass(Gbean gbean) {
	if (isSecurityRealmGbean(gbean)) {
	    try {
		List<JAXBElement<?>> elelist = gbean
			.getAttributeOrXmlAttributeOrReference();
		for (JAXBElement<?> ele : elelist) {
		    if (XmlAttributeType.class.isInstance(ele.getValue())
			    && ((XmlAttributeType) ele.getValue()).getName()
				    .equals("LoginModuleConfiguration")) {
			LoginModule loginModule = (LoginModule) ((LoginConfig) ((XmlAttributeType) ele
				.getValue()).getAny())
				.getLoginModuleRefOrLoginModule().get(0);
			return loginModule.getLoginModuleClass();
		    }
		}
		return null;
	    } catch (NullPointerException e) {
		// ignore
	    }
	}
	return null;
    }
}
