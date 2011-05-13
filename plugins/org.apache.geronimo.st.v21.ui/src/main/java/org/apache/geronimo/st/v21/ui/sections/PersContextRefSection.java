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
import org.apache.geronimo.jee.naming.PersistenceContextRef;
import org.apache.geronimo.jee.naming.Property;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.v21.ui.Activator;
import org.apache.geronimo.st.v21.ui.wizards.PersContextRefWizard;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class PersContextRefSection extends AbstractTreeSection {
    
	public PersContextRefSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, List persContextRefs) {
		super(plan, parent, toolkit, style);
		this.objectContainer = new ArrayList(persContextRefs.size());
		for (int i = 0; i < persContextRefs.size(); i++) {
		    if (PersistenceContextRef.class.isInstance(((JAXBElement)persContextRefs.get(i)).getValue())) {
		        this.objectContainer.add(persContextRefs.get(i));
		    }
		}
		createClient();
	}

    @Override
	public String getTitle() {
		return CommonMessages.editorPersContextRefTitle;
	}

    @Override
	public String getDescription() {
		return CommonMessages.editorPersContextRefDescription;
	}

    @Override
	public Wizard getWizard() {
		return new PersContextRefWizard(this);
	}

    @Override
	public Class getTableEntryObjectType() {
		return PersistenceContextRef.class;
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

    public PersistenceContextRef getSelectedObject () {
        if (tree.getSelection().length == 0) {
            return null;
        }
        Object object;
        if (tree.getSelection()[0].getParentItem() == null) {
            object = tree.getSelection()[0].getData();
        }
        else {
            object = tree.getSelection()[0].getParentItem().getData();
        }
        return (PersistenceContextRef)((JAXBElement)object).getValue();
    }
    
    @Override
    public void removeItem(Object anItem) {
        if (JAXBElement.class.isInstance(anItem)) {
            getObjectContainer().remove(anItem);
            JAXBModelUtils.getGbeanRefs(getPlan()).remove(anItem);
        }
        else if (Property.class.isInstance(anItem)) {
            Object object = tree.getSelection()[0].getParentItem().getData();
            ((PersistenceContextRef)((JAXBElement)object).getValue()).getProperty().remove(anItem);
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
                if (JAXBElement.class.isInstance(parentElement)) {
                    return ((PersistenceContextRef)((JAXBElement)parentElement).getValue()).getProperty().toArray();
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
                    PersistenceContextRef contextRef = (PersistenceContextRef)((JAXBElement)element).getValue();
                    String temp = Messages.editorPersContextRefTitle + ":" + Messages.name + " = \"" + contextRef.getPersistenceContextRefName() +
                    "\", " + Messages.type + " = \"" + contextRef.getPersistenceContextType().value();
                    
                    if (contextRef.getPersistenceUnitName() != null)
                        temp += "\", " + Messages.pattern + " " + Messages.unit + " = \"" + contextRef.getPersistenceUnitName();
                    if (contextRef.getPattern() != null && contextRef.getPattern().getName() != null)
                        temp += "\", " + Messages.pattern + " " + Messages.name + " = \"" + contextRef.getPattern().getName();
                    if (contextRef.getPattern() != null && contextRef.getPattern().getGroupId() != null)
                        temp += "\",  " + Messages.pattern + " " + Messages.group + " = \"" + contextRef.getPattern().getGroupId();
                    if (contextRef.getPattern() != null && contextRef.getPattern().getArtifactId() != null)
                        temp += "\",  " + Messages.artifact + " " + Messages.unit + " = \"" + contextRef.getPattern().getArtifactId();
                    if (contextRef.getPattern() != null && contextRef.getPattern().getVersion() != null)
                        temp += "\",  " + Messages.pattern + " " + Messages.version + " = \"" + contextRef.getPattern().getVersion();
                    if (contextRef.getPattern() != null && contextRef.getPattern().getModule() != null)
                        temp += "\",  " + Messages.pattern + " " + Messages.module + " = \"" + contextRef.getPattern().getModule();
                    temp += "\"";
                    return temp;
                }
                else if (Property.class.isInstance(element)) {
                    Property property = (Property)element;
                    return Messages.property + ": " + Messages.key + " = \"" + property.getKey() + 
                    "\", " + Messages.value + " = \"" + property.getValue() + "\"";
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
