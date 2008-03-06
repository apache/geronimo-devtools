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

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractSectionPart;
import org.apache.geronimo.st.v11.core.operations.V11DeploymentPlanCreationOperation;
import org.apache.geronimo.st.v11.ui.internal.Messages;
import org.apache.geronimo.xml.ns.deployment_1.ArtifactType;
import org.apache.geronimo.xml.ns.deployment_1.DependenciesType;
import org.apache.geronimo.xml.ns.deployment_1.EnvironmentType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public abstract class CommonGeneralSection extends AbstractSectionPart {

	protected Text artifactId;

	protected Text groupId;

	protected Text version;

	protected Text type;

	protected Button inverseClassLoading;

	protected Button suppressDefaultEnv;
	
	protected Button sharedLibDepends;

	public CommonGeneralSection(Composite parent, FormToolkit toolkit, int style, JAXBElement plan) {
		super(parent, toolkit, style, plan);
	}

	protected void createClient() {

		Section section = getSection();

		section.setText(CommonMessages.editorSectionGeneralTitle);
		section.setDescription(CommonMessages.editorSectionGeneralDescription);
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

		createLabel(composite, Messages.groupId);

		groupId = toolkit.createText(composite, getGroupId(), SWT.BORDER);
		groupId.setLayoutData(createTextFieldGridData());
		groupId.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getModuleId(true).setGroupId(groupId.getText());
				markDirty();
			}
		});

		createLabel(composite, Messages.artifactId);

		artifactId = toolkit.createText(composite, getArtifactId(), SWT.BORDER);
		artifactId.setLayoutData(createTextFieldGridData());
		artifactId.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getModuleId(true).setArtifactId(artifactId.getText());
				markDirty();
			}
		});

		createLabel(composite, Messages.version);

		version = toolkit.createText(composite, getVersion(), SWT.BORDER);
		version.setLayoutData(createTextFieldGridData());
		version.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getModuleId(true).setVersion(version.getText());
				markDirty();
			}
		});

		createLabel(composite, Messages.artifactType);

		type = toolkit.createText(composite, getArtifactType(), SWT.BORDER);
		type.setLayoutData(createTextFieldGridData());
		type.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getModuleId(true).setType(type.getText());
				markDirty();
			}
		});

		inverseClassLoading = toolkit.createButton(composite, Messages.inverseClassloading, SWT.CHECK);
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

		suppressDefaultEnv = toolkit.createButton(composite, Messages.supressDefaultEnv, SWT.CHECK);
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
		
		sharedLibDepends = toolkit.createButton(composite, Messages.sharedLibDepends, SWT.CHECK);
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

	protected Label createLabel(Composite parent, String text) {
		Label label = toolkit.createLabel(parent, text);
		label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		return label;
	}

	protected GridData createTextFieldGridData() {
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
		data.widthHint = 150;
		return data;
	}

	protected String getGroupId() {
		ArtifactType moduleId = getModuleId(false);
//		if (moduleId != null
//				&& moduleId.eIsSet(DeploymentPackage.eINSTANCE.getArtifactType_GroupId()))
//			return moduleId.getGroupId();
		return "";
	}

	protected String getArtifactId() {
		ArtifactType moduleId = getModuleId(false);
//		if (moduleId != null
//				&& moduleId.eIsSet(DeploymentPackage.eINSTANCE.getArtifactType_ArtifactId()))
//			return moduleId.getArtifactId();
		return "";
	}

	protected String getVersion() {
		ArtifactType moduleId = getModuleId(false);
//		if (moduleId != null
//				&& moduleId.eIsSet(DeploymentPackage.eINSTANCE.getArtifactType_Version()))
//			return moduleId.getVersion();
		return "";
	}

	protected String getArtifactType() {
		ArtifactType moduleId = getModuleId(false);
//		if (moduleId != null
//				&& moduleId.eIsSet(DeploymentPackage.eINSTANCE.getArtifactType_Type()))
//			return moduleId.getType();
		return "";
	}

	protected boolean isInverseClassloading() {
		EnvironmentType type = getEnvironmentType(false);
		return type != null && type.getInverseClassloading() != null;
	}

	protected boolean isSuppressDefaultEnvironment() {
		EnvironmentType type = getEnvironmentType(false);
		return type != null && type.getSuppressDefaultEnvironment() != null;
	}
	
	protected boolean isSharedLibDependency() {
		DependenciesType depType = getDependenciesType(false);
		if(depType != null) {
			return getSharedLibDependency(depType) != null;
		}
		return false;
	}

	protected void setInverseClassloading(boolean enable) {
//		if (enable) {
//			EnvironmentType type = getEnvironmentType(true);
//			type.setInverseClassloading(DeploymentFactory.eINSTANCE.createEmptyType());
//		} else {
//			EnvironmentType type = getEnvironmentType(false);
//			if (type != null) {
//				type.eUnset(DeploymentPackage.eINSTANCE.getEnvironmentType_InverseClassloading());
//			}
//		}
	}

	protected void setSuppressDefaultEnvironment(boolean enable) {
//		if (enable) {
//			EnvironmentType type = getEnvironmentType(true);
//			type.setSuppressDefaultEnvironment(DeploymentFactory.eINSTANCE.createEmptyType());
//		} else {
//			EnvironmentType type = getEnvironmentType(false);
//			if (type != null) {
//				type.eUnset(DeploymentPackage.eINSTANCE.getEnvironmentType_SuppressDefaultEnvironment());
//			}
//		}
	}
	
	protected void setSharedLibDependency(boolean enable) {
		if (enable) {
			DependenciesType deptype = getDependenciesType(true);
			ArtifactType sharedLib = V11DeploymentPlanCreationOperation.createDependencyType("geronimo", "sharedlib", null, "car");
//			deptype.getDependency().add(sharedLib);
		} else {
			DependenciesType deptype = getDependenciesType(false);
			if (deptype != null) {
				ArtifactType artifact = getSharedLibDependency(deptype);
				if(artifact != null) {
					deptype.getDependency().remove(artifact);
				}
			}
		}
	}
	
	private ArtifactType getSharedLibDependency(DependenciesType dependenciesType) {
		DependenciesType depType = getDependenciesType(false);
		List dependencies = depType.getDependency();
		Iterator i = dependencies.iterator();
		while(i.hasNext()) {
			ArtifactType artifact = (ArtifactType) i.next();
			if("geronimo".equals(artifact.getGroupId()) && "sharedlib".equals(artifact.getArtifactId()) && "car".equals(artifact.getType())) {
				return artifact;
			}
		}
		return null;
	}

	private EnvironmentType getEnvironmentType(boolean create) {
//		EnvironmentType type = (EnvironmentType) getPlan().eGet(getEnvironmentEReference());
//		if (type == null && create) {
//			type = DeploymentFactory.eINSTANCE.createEnvironmentType();
//			getPlan().eSet(getEnvironmentEReference(), type);
//		}
		return null;//type;
	}
	
	private DependenciesType getDependenciesType(boolean create) {
		EnvironmentType env = getEnvironmentType(create);
		if(env != null) {
			DependenciesType dep = env.getDependencies();
			if (dep == null && create) {
//				dep = DeploymentFactory.eINSTANCE.createDependenciesType();
				env.setDependencies(dep);
			}
			return dep;
		}
		return null;
	}

	private ArtifactType getModuleId(boolean create) {
		EnvironmentType type = getEnvironmentType(create);
		if (type != null) {
			ArtifactType moduleId = type.getModuleId();
			if (moduleId == null && create) {
//				moduleId = DeploymentFactory.eINSTANCE.createArtifactType();
				type.setModuleId(moduleId);
			}
			return moduleId;
		}
		return null;
	}

	protected abstract JAXBElement getEnvironmentEReference();

}
