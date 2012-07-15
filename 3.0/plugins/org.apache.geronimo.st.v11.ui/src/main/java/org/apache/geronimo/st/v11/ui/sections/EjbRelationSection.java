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

import org.apache.geronimo.j2ee.openejb_jar.EjbRelationType;
import org.apache.geronimo.j2ee.openejb_jar.EjbRelationshipRoleType;
import org.apache.geronimo.j2ee.openejb_jar.RelationshipsType;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.v11.ui.Activator;
import org.apache.geronimo.st.v11.ui.wizards.EjbRelationWizard;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class EjbRelationSection extends AbstractTreeSection {
    public EjbRelationSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, RelationshipsType relationships) {
        super(plan, parent, toolkit, style);
        this.objectContainer = relationships == null ? null : relationships.getEjbRelation();
        createClient();
    }

    @Override
    public String getTitle() {
        return CommonMessages.editorEjbRelationTitle;
    }

    @Override
    public String getDescription() {
        return CommonMessages.editorEjbRelationDescription;
    }

    @Override
    public Wizard getWizard() {
        return new EjbRelationWizard(this);
    }

    @Override
    public Class getTableEntryObjectType() {
        return EjbRelationType.class;
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
        if (EjbRelationType.class.isInstance(anItem)) {
            getObjectContainer().remove(anItem);
        }
        else if (EjbRelationshipRoleType.class.isInstance(anItem)) {
        	EjbRelationType relation = (EjbRelationType)tree.getSelection()[0].getParentItem().getData();
            relation.getEjbRelationshipRole().remove(anItem);
        }
        else if (EjbRelationshipRoleType.RoleMapping.CmrFieldMapping.class.isInstance(anItem)) {
        	EjbRelationshipRoleType role = (EjbRelationshipRoleType)tree.getSelection()[0].getParentItem().getData();
            role.getRoleMapping().getCmrFieldMapping().remove(anItem);
        }
    }
    
    @Override
    public Object getInput() {
        if (objectContainer != null) {
            return objectContainer;
        }
        return super.getInput();
    }

    public void resetInput (RelationshipsType relationships) {
        objectContainer = relationships.getEjbRelation();
        getViewer().setInput(objectContainer);
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
                if (EjbRelationType.class.isInstance(parentElement)) {
                	EjbRelationType relation = (EjbRelationType)parentElement;
                    return relation.getEjbRelationshipRole().toArray();
                }
                if (EjbRelationshipRoleType.class.isInstance(parentElement)) {
                	EjbRelationshipRoleType role = (EjbRelationshipRoleType)parentElement;
                    if (role.getRoleMapping() != null) {
                        return role.getRoleMapping().getCmrFieldMapping().toArray();
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
                if (EjbRelationType.class.isInstance(element)) {
                	EjbRelationType relation = (EjbRelationType)element;
                    return Messages.ejbRelalitionName + " \"" + relation.getEjbRelationName() +
                    "\", " + Messages.ejbMTMTableName +  " \"" + relation.getManyToManyTableName() + "\"";
                }
                if (EjbRelationshipRoleType.class.isInstance(element)) {
                	EjbRelationshipRoleType role = (EjbRelationshipRoleType)element;
                    return Messages.ejbRelalitionRoleName + " \"" + role.getEjbRelationshipRoleName() +
                    "\", " + Messages.ejbSourceName +  " = \"" + role.getRelationshipRoleSource().getEjbName() + 
                    "\", " + Messages.cmrFieldName + " = \"" + role.getCmrField().getCmrFieldName() + "\"";

                }
                if (EjbRelationshipRoleType.RoleMapping.CmrFieldMapping.class.isInstance(element)) {
                	EjbRelationshipRoleType.RoleMapping.CmrFieldMapping fieldMapping =
                            (EjbRelationshipRoleType.RoleMapping.CmrFieldMapping)element;
                    return Messages.ejbCmrFiledMapping + ": " +  Messages.keyColumn + " = \"" + fieldMapping.getKeyColumn() +
                    "\", " + Messages.foreignKeyColumn + " = \"" + fieldMapping.getForeignKeyColumn() + "\"";
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
