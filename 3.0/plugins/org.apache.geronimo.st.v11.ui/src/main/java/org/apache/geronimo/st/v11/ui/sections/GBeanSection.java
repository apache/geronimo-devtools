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
package org.apache.geronimo.st.v11.ui.sections;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.deployment.AttributeType;
import org.apache.geronimo.j2ee.deployment.GbeanType;
import org.apache.geronimo.j2ee.deployment.PatternType;
import org.apache.geronimo.j2ee.deployment.ReferenceType;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.v11.ui.Activator;
import org.apache.geronimo.st.v11.ui.wizards.GBeanWizard;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class GBeanSection extends AbstractTreeSection {

    public GBeanSection(JAXBElement plan, List gbeans, Composite parent, FormToolkit toolkit, int style) {
        super(plan, parent, toolkit, style);
        this.objectContainer = gbeans;
        createClient();
    }

    @Override
    public String getTitle() {
        return CommonMessages.editorSectionGBeanTitle;
    }

    @Override
    public String getDescription() {
        return CommonMessages.editorSectionGBeanDescription;
    }

    @Override
    public Wizard getWizard() {
        return new GBeanWizard(this);
    }

    @Override
    public Class getTableEntryObjectType() {
        return GbeanType.class;
    }

    @Override
    protected void activateAddButton() {
        if (tree.getSelectionCount() == 0 || tree.getSelection()[0].getParentItem() == null) {
            addButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
        }
    }

    public GbeanType getSelectedObject () {
        if (tree.getSelection().length == 0) {
            return null;
        }
        return (GbeanType)((JAXBElement)tree.getSelection()[0].getData()).getValue();
    }
    
    @Override
    public void removeItem(Object anItem) {
        JAXBElement element =(JAXBElement)tree.getSelection()[0].getData(); 
        Object object = element.getValue();
        if (GbeanType.class.isInstance(object)) {
            objectContainer.remove(anItem);
        }
        else {
        	GbeanType gbean = (GbeanType)((JAXBElement)tree.getSelection()[0].getParentItem().getData()).getValue();
            gbean.getAttributeOrXmlAttributeOrReference().remove(element);
        }
    }
    
    @Override
    public Object getInput() {
        if (objectContainer != null) {
            return objectContainer;
        }
        return super.getInput();
    }

    @Override
    public ITreeContentProvider getContentProvider() {
        return new ContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                return getChildren(inputElement);
            }

            @Override
            public Object[] getChildren(Object parentElement) {
                if (List.class.isInstance(parentElement)) {
                    return ((List)parentElement).toArray();
                }
                if (JAXBElement.class.isInstance(parentElement)) {
                    Object object = ((JAXBElement)parentElement).getValue();
                    if (GbeanType.class.isInstance(object)) {
                    	GbeanType gbean = (GbeanType)object;
                        return gbean.getAttributeOrXmlAttributeOrReference().toArray();
                    }
                }
                return new String[] {};
            }
        };
    }

    @Override
    public ILabelProvider getLabelProvider() {
        return new LabelProvider() {
            @Override
            public String getText(Object element) {
                if (JAXBElement.class.isInstance(element)) {
                    Object object = ((JAXBElement)element).getValue();
                    if (GbeanType.class.isInstance(object)) {
                    	GbeanType gbean = (GbeanType)object;
            return Messages.gbean + ": " + Messages.name + " =\"" + gbean.getName() +
            	"\", " + Messages.clazz + "\"" + gbean.getClazz() + "\"";
                    }
                    else if (AttributeType.class.isInstance(object)) {
                    	AttributeType attribute = (AttributeType)object;
            return Messages.attribute + Messages.name + " = \"" + attribute.getName() + 
            	"\", " + Messages.type + " = \"" + attribute.getType() + 
            	"\", " + Messages.value + " = \"" + attribute.getValue() + "\"";
                    }
                    else if (PatternType.class.isInstance(object)) {
                    	PatternType dependency = (PatternType)object;
            return Messages.dependency + ": " + Messages.group + " = \"" + dependency.getGroupId() + 
            	"\", " + Messages.artifact + " = \"" + dependency.getArtifactId() + 
            	"\", " + Messages.version +  "= \"" + dependency.getVersion()+ 
            	"\", " + Messages.module +  " = \"" + dependency.getModule()+ 
            	"\", " + Messages.type + " = \"" + dependency.getType()+ 
            	"\", " + Messages.customName + " = \"" + dependency.getCustomFoo() + "\"";
                    }
                    else if (ReferenceType.class.isInstance(object)) {
                    	ReferenceType reference = (ReferenceType)object;
            return Messages.reference + ":" + Messages.name + " = \"" + reference.getName()
                + "\", " + Messages.group + " = \"" + reference.getGroupId()
                + "\", " + Messages.artifact + " = \"" + reference.getArtifactId()
                + "\", " + Messages.version + " = \"" + reference.getVersion()
                + "\", " + Messages.module + " = \"" + reference.getModule()
                + "\", " + Messages.type + " = \"" + reference.getType()
                + "\", " + Messages.customName + " = \"" + reference.getCustomFoo() + "\"";
            }
        }

                return null;
            }

            @Override
            public Image getImage(Object arg0) {
                return Activator.imageDescriptorFromPlugin("org.eclipse.jst.j2ee",
                        "icons/full/obj16/accessbean_obj.gif").createImage();
            }
        };
    }
}
