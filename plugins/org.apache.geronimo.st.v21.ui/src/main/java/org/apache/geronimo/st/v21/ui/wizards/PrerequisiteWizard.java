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

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.wizards.AbstractWizard;
import org.apache.geronimo.system.plugin.model.ArtifactType;
import org.apache.geronimo.system.plugin.model.PrerequisiteType;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public class PrerequisiteWizard extends AbstractWizard {

    protected PrerequisiteType prereq;
    
    protected Text group;
    protected Text artifact;
    protected Text version;
    protected Text type;
    protected Text description;
    
    public PrerequisiteWizard (PrerequisiteType oldPrereq) {
        super();
        prereq = oldPrereq;
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_Prerequisite;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_Prerequisite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        addPage(new MessageDestWizardPage("Page0"));
    }

    // need to extend the DynamicWizardPage only so that when the Edit dialog is shown
    // the values are brought in properly.
    public class MessageDestWizardPage extends AbstractWizardPage {
        public MessageDestWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            Composite composite = createComposite(parent);
            createLabel (composite, CommonMessages.groupId);
            group = createTextField (composite, "");
            createLabel (composite, CommonMessages.artifactId);
            artifact = createTextField (composite, "");
            createLabel (composite, CommonMessages.version);
            version = createTextField (composite, "");
            createLabel (composite, CommonMessages.type);
            type = createTextField (composite, "");
            createLabel (composite, CommonMessages.description);
            description = createTextField (composite, "");
            
            group.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent arg0) {
					PrerequisiteWizard.this.getContainer().updateButtons();
				}
            });
            
            artifact.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent arg0) {
					PrerequisiteWizard.this.getContainer().updateButtons();
				}
            });
            
            type.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent arg0) {
					PrerequisiteWizard.this.getContainer().updateButtons();
				}
            });

            if (prereq != null) {
                group.setText(prereq.getId().getGroupId());
                artifact.setText(prereq.getId().getArtifactId());
                version.setText(prereq.getId().getVersion());
                type.setText(prereq.getResourceType());
                description.setText(prereq.getDescription());
            }
            setControl(composite);
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_Prerequisite;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_Prerequisite;
        }
        
    }
    
    public boolean performFinish() {
        prereq = new PrerequisiteType();
        ArtifactType artType = new ArtifactType();
        artType.setGroupId(group.getText());
        artType.setArtifactId(artifact.getText());
        artType.setType(type.getText());
        if(!"".equals(version.getText()))
        	artType.setVersion(version.getText());
        prereq.setId(artType);
        prereq.setResourceType(type.getText());
        prereq.setDescription(description.getText());

        return true;
    }
    
    public PrerequisiteType getPrerequisite() {
        return prereq;
    }
    
    public boolean canFinish(){
    	if (group.getText()!=null && group.getText().length()!=0
    			&& artifact.getText()!=null && artifact.getText().length()!=0
    			&& type.getText()!=null && type.getText().length()!=0){
    		return true;
    	}else return false;
    }
   
}
