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

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.application.ApplicationType;
import org.apache.geronimo.j2ee.connector.ConnectorType;
import org.apache.geronimo.j2ee.deployment.ArtifactType;
import org.apache.geronimo.j2ee.deployment.DependenciesType;
import org.apache.geronimo.j2ee.deployment.DependencyType;
import org.apache.geronimo.j2ee.deployment.EnvironmentType;
import org.apache.geronimo.j2ee.deployment.ObjectFactory;
import org.apache.geronimo.j2ee.openejb_jar.OpenejbJarType;
import org.apache.geronimo.j2ee.web.WebAppType;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractSectionPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @version $Rev$ $Date$
 */
public abstract class CommonGeneralSection extends AbstractSectionPart {

    protected Text artifactId;

    protected Text groupId;

    protected Text version;

    protected Text type;

    protected Button inverseClassLoading;

    protected Button suppressDefaultEnv;
    
    protected Button sharedLibDepends;
    
    protected ObjectFactory deploymentObjectFactory = null;

    public CommonGeneralSection(Composite parent, FormToolkit toolkit, int style, JAXBElement plan) {
        super(parent, toolkit, style, plan);
    }

    protected void createClient() {

        Section section = getSection();

        section.setText(getSectionGeneralTitle());
        section.setDescription(getSectionGeneralDescription());
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Composite composite = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 15;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        section.setClient(composite);

        createLabel(composite, CommonMessages.groupId);

        groupId = toolkit.createText(composite, getGroupId(), SWT.BORDER);
        groupId.setLayoutData(createTextFieldGridData());
        groupId.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getModuleId(true).setGroupId(groupId.getText());
                markDirty();
            }
        });

        createLabel(composite, CommonMessages.artifactId);

        artifactId = toolkit.createText(composite, getArtifactId(), SWT.BORDER);
        artifactId.setLayoutData(createTextFieldGridData());
        artifactId.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getModuleId(true).setArtifactId(artifactId.getText());
                markDirty();
            }
        });

        createLabel(composite, CommonMessages.version);

        version = toolkit.createText(composite, getVersion(), SWT.BORDER);
        version.setLayoutData(createTextFieldGridData());
        version.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getModuleId(true).setVersion(version.getText());
                markDirty();
            }
        });

        createLabel(composite, CommonMessages.artifactType);

        type = toolkit.createText(composite, getArtifact(), SWT.BORDER);
        type.setLayoutData(createTextFieldGridData());
        type.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getModuleId(true).setType(type.getText());
                markDirty();
            }
        });

        inverseClassLoading = toolkit.createButton(composite, CommonMessages.inverseClassloading, SWT.CHECK);
        inverseClassLoading.setSelection(isInverseClassloading());
        GridData data = new GridData();
        data.horizontalSpan = 2;
        inverseClassLoading.setLayoutData(data);

        inverseClassLoading.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                setInverseClassloading(inverseClassLoading.getSelection());
                markDirty();
            }
        });

        suppressDefaultEnv = toolkit.createButton(composite, CommonMessages.supressDefaultEnv, SWT.CHECK);
        suppressDefaultEnv.setSelection(isSuppressDefaultEnvironment());
        data = new GridData();
        data.horizontalSpan = 2;
        suppressDefaultEnv.setLayoutData(data);

        suppressDefaultEnv.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                setSuppressDefaultEnvironment(suppressDefaultEnv.getSelection());
                markDirty();
            }
        });
        
        sharedLibDepends = toolkit.createButton(composite, CommonMessages.sharedLibDepends, SWT.CHECK);
        sharedLibDepends.setSelection(isSharedLibDependency());
        data = new GridData();
        data.horizontalSpan = 2;
        sharedLibDepends.setLayoutData(data);

        sharedLibDepends.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                setSharedLibDependency(sharedLibDepends.getSelection());
                markDirty();
            }
        });
    }

    protected String getGroupId() {
        ArtifactType moduleId = getModuleId(false);
        if (moduleId != null
                && moduleId.getGroupId() != null)
            return moduleId.getGroupId();
        return "";
    }

    protected String getArtifactId() {
        ArtifactType moduleId = getModuleId(false);
        if (moduleId != null
                && moduleId.getArtifactId() != null)
            return moduleId.getArtifactId();
        return "";
    }

    protected String getVersion() {
        ArtifactType moduleId = getModuleId(false);
        if (moduleId != null
                && moduleId.getVersion() != null)
            return moduleId.getVersion();
        return "";
    }

    protected String getArtifact() {
        ArtifactType moduleId = getModuleId(false);
        if (moduleId != null
                && moduleId.getType() != null)
            return moduleId.getType();
        return "";
    }

    protected boolean isInverseClassloading() {
        EnvironmentType type = getEnvironment(false);
        return type != null && type.getInverseClassloading() != null;
    }

    protected boolean isSuppressDefaultEnvironment() {
        EnvironmentType type = getEnvironment(false);
        return type != null && type.getSuppressDefaultEnvironment() != null;
    }
    
    protected boolean isSharedLibDependency() {
        DependenciesType depType = getDependencies(false);
        if(depType != null) {
            return getSharedLibDependency(depType) != null;
        }
        return false;
    }

    protected void setInverseClassloading(boolean enable) {
        if (enable) {
            EnvironmentType type = getEnvironment(true);
            type.setInverseClassloading(getDeploymentObjectFactory().createEmptyType());
        } else {
            EnvironmentType type = getEnvironment(false);
            if (type != null) {
                type.setInverseClassloading(null);
            }
        }
    }

    protected void setSuppressDefaultEnvironment(boolean enable) {
        if (enable) {
            EnvironmentType type = getEnvironment(true);
            type.setSuppressDefaultEnvironment(getDeploymentObjectFactory().createEmptyType());
        } else {
            EnvironmentType type = getEnvironment(false);
            if (type != null) {
                type.setSuppressDefaultEnvironment(null);
            }
        }
    }
    
    protected void setSharedLibDependency(boolean enable) {
        if (enable) {
            DependenciesType deptype = getDependencies(true);
            DependencyType sharedLib = getDeploymentObjectFactory().createDependencyType();
            sharedLib.setGroupId("org.apache.geronimo.configs");
            sharedLib.setArtifactId("sharedlib");
            sharedLib.setType("car");
            deptype.getDependency().add(sharedLib);
        } else {
        	DependenciesType deptype = getDependencies(false);
            if (deptype != null) {
                ArtifactType ArtifactType = getSharedLibDependency(deptype);
                if(ArtifactType != null) {
                    deptype.getDependency().remove(ArtifactType);
                }
            }
        }
    }
    
    private ArtifactType getSharedLibDependency(DependenciesType dependencies) {
    	DependenciesType depType = getDependencies(false);
        List dependenciesList = depType.getDependency();
        Iterator i = dependenciesList.iterator();
        while(i.hasNext()) {
            ArtifactType ArtifactType = (ArtifactType) i.next();
            if("org.apache.geronimo.configs".equals(ArtifactType.getGroupId()) && "sharedlib".equals(ArtifactType.getArtifactId()) && "car".equals(ArtifactType.getType())) {
                return ArtifactType;
            }
        }
        return null;
    }

    protected EnvironmentType getEnvironment(boolean create) {
        EnvironmentType type = null;
        Object plan = getPlan().getValue();
        if (WebAppType.class.isInstance(plan)) {
            type = ((WebAppType) plan).getEnvironment();
            if (type == null && create) {
                type = getDeploymentObjectFactory().createEnvironmentType();
                ((WebAppType) plan).setEnvironment(type);
            }
        } else if (ConnectorType.class.isInstance(plan)) {
            type = ((ConnectorType) plan).getEnvironment();
            if (type == null && create) {
                type = getDeploymentObjectFactory().createEnvironmentType();
                ((ConnectorType) plan).setEnvironment(type);
            }
        } else if (ApplicationType.class.isInstance(plan)) {
            type = ((ApplicationType) plan).getEnvironment();
            if (type == null && create) {
                type = getDeploymentObjectFactory().createEnvironmentType();
                ((ApplicationType) plan).setEnvironment(type);
            }
        } else if (OpenejbJarType.class.isInstance(plan)) {
            type = ((OpenejbJarType) plan).getEnvironment();
            if (type == null && create) {
                type = getDeploymentObjectFactory().createEnvironmentType();
                ((OpenejbJarType) plan).setEnvironment(type);
            }
        }

        return type;
    }
    
    private DependenciesType getDependencies(boolean create) {
        EnvironmentType env = getEnvironment(create);
        if(env != null) {
            DependenciesType dep = env.getDependencies();
            if (dep == null && create) {
                dep = getDeploymentObjectFactory().createDependenciesType();
                env.setDependencies(dep);
            }
            return dep;
        }
        return null;
    }

    private ArtifactType getModuleId(boolean create) {
        EnvironmentType type = getEnvironment(create);
        if (type != null) {
            ArtifactType moduleId = type.getModuleId();
            if (moduleId == null && create) {
                moduleId = getDeploymentObjectFactory().createArtifactType();
                type.setModuleId(moduleId);
            }
            return moduleId;
        }
        return null;
    }
    
    protected org.apache.geronimo.j2ee.deployment.ObjectFactory getDeploymentObjectFactory() {
        if ( deploymentObjectFactory == null ) {
            deploymentObjectFactory = new org.apache.geronimo.j2ee.deployment.ObjectFactory();
        }
        return deploymentObjectFactory;
    }

    protected String getSectionGeneralTitle() {
        return CommonMessages.editorSectionGeneralTitle;
    }

    protected String getSectionGeneralDescription() {
        return CommonMessages.editorSectionGeneralDescription;
    }
}
