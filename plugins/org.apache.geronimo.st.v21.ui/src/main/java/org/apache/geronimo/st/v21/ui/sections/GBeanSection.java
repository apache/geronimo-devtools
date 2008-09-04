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

import org.apache.geronimo.jee.deployment.Attribute;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.deployment.Pattern;
import org.apache.geronimo.jee.deployment.Reference;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.v21.ui.Activator;
import org.apache.geronimo.st.v21.ui.wizards.GBeanWizard;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev: 688452 $ $Date: 2008-08-24 01:56:20 -0400 (Sun, 24 Aug 2008) $
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
        return Gbean.class;
    }

    @Override
    protected void activateAddButton() {
        if (tree.getSelectionCount() == 0 || tree.getSelection()[0].getParentItem() == null) {
            addButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
        }
    }

    public Gbean getSelectedGbean () {
        if (tree.getSelection().length == 0) {
            return null;
        }
        return (Gbean)((JAXBElement)tree.getSelection()[0].getData()).getValue();
    }
    
    @Override
    public void removeItem(Object anItem) {
        JAXBElement element =(JAXBElement)tree.getSelection()[0].getData(); 
        Object object = element.getValue();
        if (Gbean.class.isInstance(object)) {
            objectContainer.remove(anItem);
        }
        else {
            Gbean gbean = (Gbean)((JAXBElement)tree.getSelection()[0].getParentItem().getData()).getValue();
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
                    if (Gbean.class.isInstance(object)) {
                        Gbean gbean = (Gbean)object;
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
                    if (Gbean.class.isInstance(object)) {
                        Gbean gbean = (Gbean)object;
                        return "GBean: name = \"" + gbean.getName() +
                                "\", class = \"" + gbean.getClazz() + "\"";
                    }
                    else if (Attribute.class.isInstance(object)) {
                        Attribute attribute = (Attribute)object;
                        return "Attribute: name = \"" + attribute.getName() + 
                                "\", type = \"" + attribute.getType() + 
                                "\", value = \"" + attribute.getValue() + "\"";
                    }
                    else if (Pattern.class.isInstance(object)) {
                        Pattern dependency = (Pattern)object;
                        return "Dependency: group = \"" + dependency.getGroupId() +
                                "\", artifact = \"" + dependency.getArtifactId() +
                                "\", version = \"" + dependency.getVersion() +
                                "\", module = \"" + dependency.getModule() +
                                "\", type = \"" + dependency.getType() +
                                "\", custom name = \"" + dependency.getCustomFoo() + "\"";
                    }
                    else if (Reference.class.isInstance(object)) {
                        Reference reference = (Reference)object;
                        return "Reference: name = \"" + reference.getName() +
                                "\", group = \"" + reference.getGroupId() +
                                "\", artifact = \"" + reference.getArtifactId() +
                                "\", version = \"" + reference.getVersion() +
                                "\", module = \"" + reference.getModule() +
                                "\", type = \"" + reference.getType() +
                                "\", custom name = \"" + reference.getCustomFoo() + "\"";
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
