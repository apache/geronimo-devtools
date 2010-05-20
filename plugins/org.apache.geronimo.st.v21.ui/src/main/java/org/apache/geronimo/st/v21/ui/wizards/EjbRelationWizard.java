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
package org.apache.geronimo.st.v21.ui.wizards;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.openejb.EjbRelation;
import org.apache.geronimo.jee.openejb.EjbRelationshipRole;
import org.apache.geronimo.jee.openejb.Relationships;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.ui.wizards.AbstractTreeWizard;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBObjectFactoryImpl;
import org.apache.geronimo.st.v21.ui.sections.EjbRelationSection;

/**
 * @version $Rev$ $Date$
 */
public class EjbRelationWizard extends AbstractTreeWizard {

    private final int EJB_RELATION = 0;
    private final int RELATIONSHIP_ROLE = 1;
    private final int CMR_FIELD_MAPPING = 2;

    public EjbRelationWizard(AbstractTreeSection section) {
        super(section, 3, 3);
        elementTypes[EJB_RELATION] = "EJB Relation";
        elementTypes[RELATIONSHIP_ROLE] = "EJB Relationship Role";
        elementTypes[CMR_FIELD_MAPPING] = "CMR Field Mapping";
    }

    public class EjbRelationWizardPage extends AbstractTreeWizardPage {

        public EjbRelationWizardPage(String pageName) {
            super(pageName);
        }

        protected void initControl () {
            if (eObject == null) {
                element.select(EJB_RELATION);
                if (EjbRelation.class.isInstance(((EjbRelationSection)section).getSelectedObject())) {
                    element.remove(elementTypes[CMR_FIELD_MAPPING]);
                }
                else if (EjbRelationshipRole.class.isInstance(((EjbRelationSection)section).getSelectedObject())) {
                    element.select(CMR_FIELD_MAPPING);
                    element.setEnabled(false);
                }
                else {
                    element.setEnabled(false);
                }
            }
            else {
                if (EjbRelation.class.isInstance(eObject)) {
                    textList.get(0).setText(((EjbRelation)eObject).getEjbRelationName());
                    textList.get(1).setText(((EjbRelation)eObject).getManyToManyTableName());
                    element.select(EJB_RELATION);
                }
                else if (EjbRelationshipRole.class.isInstance(eObject)) {
                    EjbRelationshipRole role = (EjbRelationshipRole)eObject;
                    textList.get(0).setText(role.getEjbRelationshipRoleName());
                    textList.get(1).setText(role.getRelationshipRoleSource().getEjbName());
                    textList.get(2).setText(role.getCmrField().getCmrFieldName());
                    element.select(RELATIONSHIP_ROLE);
                }
                else if (EjbRelationshipRole.RoleMapping.CmrFieldMapping.class.isInstance(eObject)) {
                    EjbRelationshipRole.RoleMapping.CmrFieldMapping fieldMapping =
                            (EjbRelationshipRole.RoleMapping.CmrFieldMapping)eObject;
                    textList.get(0).setText(fieldMapping.getKeyColumn());
                    textList.get(1).setText(fieldMapping.getForeignKeyColumn());
                    element.select(CMR_FIELD_MAPPING);
                }
                element.setEnabled(false);
            }
        }
        
        protected void toggleFields (boolean clearFields) {
            if (element.getText().equals(elementTypes[EJB_RELATION])) {
                for (int i = 0; i < maxTextFields; i++) {
                    labelList.get(i).setVisible(i < 2 ? true : false);
                    textList.get(i).setVisible(i < 2 ? true : false);
                    if (clearFields == true) {
                        textList.get(i).setText("");
                    }
                }
                labelList.get(0).setText(CommonMessages.name);
                labelList.get(1).setText(CommonMessages.mtmTableName);
                // if we are doing an add, then we need to make sure that the longest
                // text can be handled
                labelList.get(2).setText(CommonMessages.foreignKeyColumn);
            }
            else if (element.getText().equals(elementTypes[RELATIONSHIP_ROLE])) {
                for (int i = 0; i < maxTextFields; i++) {
                    labelList.get(i).setVisible(true);
                    textList.get(i).setVisible(true);
                    if (clearFields == true) {
                        textList.get(i).setText("");
                    }
                }
                labelList.get(0).setText(CommonMessages.name);
                labelList.get(1).setText(CommonMessages.ejbSourceName);
                labelList.get(2).setText(CommonMessages.cmrFieldName);
            }
            else if (element.getText().equals(elementTypes[CMR_FIELD_MAPPING])) {
                for (int i = 0; i < maxTextFields; i++) {
                    labelList.get(i).setVisible(i < 2 ? true : false);
                    textList.get(i).setVisible(i < 2 ? true : false);
                    if (clearFields == true) {
                        textList.get(i).setText("");
                    }
                }
                labelList.get(0).setText(CommonMessages.keyColumn);
                labelList.get(1).setText(CommonMessages.foreignKeyColumn);
            }
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_EjbRelation;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_EjbRelation;
        }
    }

    @Override
    public void addPages() {
        addPage(new EjbRelationWizardPage("Page0"));
    }

    @Override
    public boolean performFinish() {
        EjbRelation relation;
        if (element.getText().equals(elementTypes[EJB_RELATION])) {
            relation = (EjbRelation)eObject;
            if (relation == null) {
                relation = (EjbRelation)getEFactory().create(EjbRelation.class);
                JAXBElement plan = section.getPlan();
                
                if (JAXBModelUtils.getEjbRelationships(plan) == null) {
                    Relationships relationships = (Relationships)getEFactory().create(Relationships.class);

                    JAXBModelUtils.setEjbRelationships(plan, relationships);
                    ((EjbRelationSection)section).resetInput(relationships);
                }
                JAXBModelUtils.getEjbRelationships(plan).add(relation);
            }
            relation.setEjbRelationName(textList.get(0).getText());
            relation.setManyToManyTableName(textList.get(1).getText());
        }
        else if (element.getText().equals(elementTypes[RELATIONSHIP_ROLE])) {
            if (isEmpty(textList.get(1).getText())) {
                return false;
            }
            EjbRelationshipRole role = (EjbRelationshipRole)eObject;
            if (role == null) {
                role = (EjbRelationshipRole)getEFactory().create(EjbRelationshipRole.class);
                relation = (EjbRelation)((EjbRelationSection)section).getSelectedObject();
                relation.getEjbRelationshipRole().add(role);
            }
            role.setEjbRelationshipRoleName(textList.get(0).getText());
            EjbRelationshipRole.RelationshipRoleSource source = role.getRelationshipRoleSource();
            if (source == null) {
                source = (EjbRelationshipRole.RelationshipRoleSource)getEFactory().create(EjbRelationshipRole.RelationshipRoleSource.class);
                role.setRelationshipRoleSource(source);
            }
            source.setEjbName(textList.get(1).getText());
            EjbRelationshipRole.CmrField cmrField = role.getCmrField();
            if (cmrField == null) {
                cmrField = (EjbRelationshipRole.CmrField)getEFactory().create(EjbRelationshipRole.CmrField.class);
                role.setCmrField(cmrField);
            }
            cmrField.setCmrFieldName(textList.get(2).getText());
        }
        else if (element.getText().equals(elementTypes[CMR_FIELD_MAPPING])) { 
            if (isEmpty(textList.get(0).getText()) || isEmpty(textList.get(1).getText())) {
                return false;
            }
            EjbRelationshipRole.RoleMapping.CmrFieldMapping fieldMapping =
                    (EjbRelationshipRole.RoleMapping.CmrFieldMapping)eObject;
            if (fieldMapping == null) {
                fieldMapping = (EjbRelationshipRole.RoleMapping.CmrFieldMapping)getEFactory().create(EjbRelationshipRole.RoleMapping.CmrFieldMapping.class);
                EjbRelationshipRole role = (EjbRelationshipRole)((EjbRelationSection)section).getSelectedObject();
                EjbRelationshipRole.RoleMapping roleMapping = role.getRoleMapping();
                if (roleMapping == null) {
                    roleMapping = (EjbRelationshipRole.RoleMapping)getEFactory().create(EjbRelationshipRole.RoleMapping.class);
                    role.setRoleMapping(roleMapping);
                }
                role.getRoleMapping().getCmrFieldMapping().add(fieldMapping);
            }
            fieldMapping.setKeyColumn(textList.get(0).getText());
            fieldMapping.setForeignKeyColumn(textList.get(1).getText());
        }
        return true;
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_EjbRelation;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_EjbRelation;
    }
}
