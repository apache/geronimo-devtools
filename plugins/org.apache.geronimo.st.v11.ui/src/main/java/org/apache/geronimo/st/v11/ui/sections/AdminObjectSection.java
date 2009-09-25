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

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.v11.ui.Activator;
import org.apache.geronimo.st.v11.ui.wizards.AdminObjectWizard;
import org.apache.geronimo.xml.ns.j2ee.connector_1.AdminobjectInstanceType;
import org.apache.geronimo.xml.ns.j2ee.connector_1.AdminobjectType;
import org.apache.geronimo.xml.ns.j2ee.connector_1.ConfigPropertySettingType;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev: 705268 $ $Date: 2008-10-16 23:54:29 +0800 (Thu, 16 Oct 2008) $
 */
public class AdminObjectSection extends AbstractTreeSection {
    public AdminObjectSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, List adminObjects) {
        super(plan, parent, toolkit, style);
        this.objectContainer = adminObjects;
        createClient();
    }

    @Override
    public String getTitle() {
        return CommonMessages.editorSectionAdminObjectTitle;
    }

    @Override
    public String getDescription() {
        return CommonMessages.editorSectionAdminObjectDescription;
    }

    @Override
    public Wizard getWizard() {
        return new AdminObjectWizard(this);
    }

    @Override
    public Class getTableEntryObjectType() {
        return AdminobjectType.class;
    }

    @Override
    protected void activateAddButton() {
        if (tree.getSelectionCount() == 0 || tree.getSelection()[0].getParentItem() == null ||
            tree.getSelection()[0].getParentItem().getParentItem() == null) {
            addButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
        }
    }

    public Object getSelectedObject () {
        if (tree.getSelection().length == 0) {
            return null;
        }
        return tree.getSelection()[0].getData();
    }

    @Override
    public void removeItem(Object anItem) {
        if (AdminobjectType.class.isInstance(anItem)) {
            getObjectContainer().remove(anItem);
        }
        else if (AdminobjectInstanceType.class.isInstance(anItem)) {
        	AdminobjectType admin = (AdminobjectType)tree.getSelection()[0].getParentItem().getData();
            admin.getAdminobjectInstance().remove(anItem);
        }
        else if (ConfigPropertySettingType.class.isInstance(anItem)) {
        	AdminobjectInstanceType aoInstance = (AdminobjectInstanceType)tree.getSelection()[0].getParentItem().getData();
            aoInstance.getConfigPropertySetting().remove(anItem);
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
                if (AdminobjectType.class.isInstance(parentElement)) {
                    AdminobjectType admin = (AdminobjectType)parentElement;
                    return admin.getAdminobjectInstance().toArray();
                }
                if (AdminobjectInstanceType.class.isInstance(parentElement)) {
                    AdminobjectInstanceType aoInstance = (AdminobjectInstanceType)parentElement;
                    return aoInstance.getConfigPropertySetting().toArray();
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
                if (AdminobjectType.class.isInstance(element)) {
                	AdminobjectType admin = (AdminobjectType)element;
                    return "Admin Object: interface = \"" + admin.getAdminobjectInterface() +
                            "\", class = \"" + admin.getAdminobjectClass() + "\"";
                }
                if (AdminobjectInstanceType.class.isInstance(element)) {
                	AdminobjectInstanceType aoInstance = (AdminobjectInstanceType)element;
                    return "Admin Object Instance: message destination name = \"" + aoInstance.getMessageDestinationName() + "\"";
                }
                if (ConfigPropertySettingType.class.isInstance(element)) {
                	ConfigPropertySettingType property = (ConfigPropertySettingType)element;
                    return "Config Property Setting: name = \"" + property.getName() +
                            "\", value = \"" + property.getValue() + "\"";
                }

                return null;
            }

            @Override
            public Image getImage(Object arg0) {
                return Activator.imageDescriptorFromPlugin("org.eclipse.jst.j2ee",
                        "icons/full/obj16/module_web_obj.gif").createImage();
            }
        };
    }
}
