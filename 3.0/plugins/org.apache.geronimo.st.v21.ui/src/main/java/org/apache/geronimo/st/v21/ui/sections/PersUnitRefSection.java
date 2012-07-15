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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.jee.naming.PersistenceUnitRef;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.v21.ui.Activator;
import org.apache.geronimo.st.v21.ui.wizards.PersUnitRefWizard;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class PersUnitRefSection extends AbstractTreeSection {
    
	public PersUnitRefSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, List persUnitRefs) {
		super(plan, parent, toolkit, style);
		this.objectContainer = new ArrayList(persUnitRefs.size());
		for (int i = 0; i < persUnitRefs.size(); i++) {
		    if (PersistenceUnitRef.class.isInstance(((JAXBElement)persUnitRefs.get(i)).getValue())) {
		        this.objectContainer.add(persUnitRefs.get(i));
		    }
		}
		createClient();
	}

    @Override
	public String getTitle() {
		return CommonMessages.editorPersUnitRefTitle;
	}

    @Override
	public String getDescription() {
		return CommonMessages.editorPersUnitRefDescription;
	}

    @Override
	public Wizard getWizard() {
		return new PersUnitRefWizard(this);
	}

    @Override
	public Class getTableEntryObjectType() {
		return PersistenceUnitRef.class;
	}

    @Override
    public Object getInput() {
        if (objectContainer != null) {
            return objectContainer;
        }
        return super.getInput();
    }

    @Override
    protected void activateAddButton() {
        if (tree.getSelectionCount() == 0 || tree.getSelection()[0].getParentItem() == null) {
            addButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
        }
    }

    public PersistenceUnitRef getSelectedObject () {
        if (tree.getSelection().length == 0) {
            return null;
        }
        return (PersistenceUnitRef)((JAXBElement)tree.getSelection()[0].getData()).getValue();
    }
    
    @Override
    public void removeItem(Object anItem) {
        if (JAXBElement.class.isInstance(anItem)) {
            getObjectContainer().remove(anItem);
            JAXBModelUtils.getGbeanRefs(getPlan()).remove(anItem);
        }
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
                    PersistenceUnitRef unitRef = (PersistenceUnitRef)((JAXBElement)element).getValue();
                    String temp = "Persistence Unit Ref: name = \"" + unitRef.getPersistenceUnitRefName();
                    if (unitRef.getPersistenceUnitName() != null)
                        temp += "\", unit name = \"" + unitRef.getPersistenceUnitName();
                    if (unitRef.getPattern() != null && unitRef.getPattern().getName() != null)
                        temp += "\", pattern name = \"" + unitRef.getPattern().getName();
                    if (unitRef.getPattern() != null && unitRef.getPattern().getGroupId() != null)
                        temp += "\", pattern group = \"" + unitRef.getPattern().getGroupId();
                    if (unitRef.getPattern() != null && unitRef.getPattern().getArtifactId() != null)
                        temp += "\", pattern artifact = \"" + unitRef.getPattern().getArtifactId();
                    if (unitRef.getPattern() != null && unitRef.getPattern().getVersion() != null)
                        temp += "\", pattern version = \"" + unitRef.getPattern().getVersion();
                    if (unitRef.getPattern() != null && unitRef.getPattern().getModule() != null)
                        temp += "\", pattern module = \"" + unitRef.getPattern().getModule();
                    temp += "\"";
                    return temp;
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
