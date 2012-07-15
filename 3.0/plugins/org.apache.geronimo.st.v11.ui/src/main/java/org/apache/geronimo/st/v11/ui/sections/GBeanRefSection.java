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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.deployment.PatternType;
import org.apache.geronimo.j2ee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.j2ee.naming.GbeanRefType;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.v11.ui.Activator;
import org.apache.geronimo.st.v11.ui.wizards.GBeanRefWizard;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class GBeanRefSection extends AbstractTreeSection {

	public GBeanRefSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, List gbeanRef) {
		super(plan, parent, toolkit, style);
		//this.objectContainer = gbeanRef;
	      this.objectContainer = new ArrayList(gbeanRef.size());
	        for (int i = 0; i < gbeanRef.size(); i++) {
	            if (GbeanRefType.class.isInstance(gbeanRef.get(i))) {
	                this.objectContainer.add(gbeanRef.get(i));
	            }
	            else if (GbeanRefType.class.isInstance(((JAXBElement)gbeanRef.get(i)).getValue())) {
	                this.objectContainer.add(gbeanRef.get(i));
	            }
	        }
		createClient();
	}

    @Override
	public String getTitle() {
		return CommonMessages.editorGBeanRefTitle;
	}

    @Override
	public String getDescription() {
		return CommonMessages.editorGBeanRefDescription;
	}

    @Override
	public Wizard getWizard() {
		return new GBeanRefWizard(this);
	}

    @Override
	public Class getTableEntryObjectType() {
		return GbeanRefType.class;
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

    public GbeanRefType getSelectedObject () {
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
        if (GbeanRefType.class.isInstance(object)) {
            return (GbeanRefType)object;
        }
        else {
            return (GbeanRefType)((JAXBElement)object).getValue();
        }
    }
    
    @Override
    public void removeItem(Object anItem) {
        if (GbeanRefType.class.isInstance(anItem) || JAXBElement.class.isInstance(anItem)) {
            getObjectContainer().remove(anItem);
            JAXBModelUtils.getGbeanRefs(getPlan()).remove(anItem);
        }
        else if (String.class.isInstance(anItem)) {
            Object object = tree.getSelection()[0].getParentItem().getData();
            if (GbeanRefType.class.isInstance(object)) {
                ((GbeanRefType)object).getRefType().remove(anItem);
            }
            else {
                ((GbeanRefType)((JAXBElement)object).getValue()).getRefType().remove(anItem);
            }
        }
        else if (PatternType.class.isInstance(anItem)) {
            Object object = tree.getSelection()[0].getParentItem().getData();
            if (GbeanRefType.class.isInstance(object)) {
                ((GbeanRefType)object).getPattern().remove(anItem);
            }
            else {
                ((GbeanRefType)((JAXBElement)object).getValue()).getPattern().remove(anItem);
            }
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
                    parentElement = ((JAXBElement)parentElement).getValue();
                }
                if (GbeanRefType.class.isInstance(parentElement)) {
                	GbeanRefType gbeanRef = (GbeanRefType)parentElement;
                    Object[] typeList = gbeanRef.getRefType().toArray();
                    Object[] patternList = gbeanRef.getPattern().toArray();
                    Object[] fullList = new Object[typeList.length + patternList.length];
                    System.arraycopy(typeList, 0, fullList, 0, typeList.length);
                    System.arraycopy(patternList, 0, fullList, typeList.length, patternList.length);
                    return fullList;
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
                    element = ((JAXBElement)element).getValue();
                }
                if (GbeanRefType.class.isInstance(element)) {
                	GbeanRefType gbeanRef = (GbeanRefType)element;
                    return Messages.editorGBeanRefTitle + ": " + Messages.name + " = \"" + gbeanRef.getRefName() + "\"";
                }
                else if (String.class.isInstance(element)) {
                    return Messages.gbeanType + ": " + Messages.name + " = \"" + (String)element + "\"";
                }
                else if (PatternType.class.isInstance(element)) {
                	PatternType pattern = (PatternType)element;
                    return Messages.pattern + ": " + Messages.name + " = \"" + pattern.getCustomFoo() + 
                            "\", " + Messages.group + "  = \"" + pattern.getGroupId() + 
                            "\", " + Messages.artifactId + "  = \"" + pattern.getArtifactId() + 
                            "\", " + Messages.version + "  = \"" + pattern.getVersion() + 
                            "\", " + Messages.module + "  = \"" + pattern.getModule() + "\"";
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
