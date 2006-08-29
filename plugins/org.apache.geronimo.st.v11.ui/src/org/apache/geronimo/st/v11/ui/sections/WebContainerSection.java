/**
 *  Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.st.v11.ui.sections;

import org.apache.geronimo.st.ui.sections.AbstractSectionPart;
import org.apache.geronimo.st.v11.ui.internal.Messages;
import org.apache.geronimo.xml.ns.j2ee.web.WebAppType;
import org.apache.geronimo.xml.ns.naming.GbeanLocatorType;
import org.apache.geronimo.xml.ns.naming.NamingFactory;
import org.apache.geronimo.xml.ns.naming.NamingPackage;
import org.apache.geronimo.xml.ns.naming.PatternType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class WebContainerSection extends AbstractSectionPart {

	protected Text gBeanLink;

	protected Text artifact;

	protected Text group;

	protected Text module;

	protected Text name;

	protected Text version;

	protected Button specifyAsLink;

	protected Button specifyAsPattern;

	WebAppType plan;

	/**
	 * @param parent
	 * @param toolkit
	 * @param style
	 * @param plan
	 */
	public WebContainerSection(Composite parent, FormToolkit toolkit, int style, EObject plan) {
		super(parent, toolkit, style, plan);
		this.plan = (WebAppType) plan;
		createClient();
	}

	protected void createClient() {
		Section section = getSection();

		section.setText(Messages.webContainerSection);
		section.setDescription(Messages.webContainerSectionDescription);
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

		specifyAsLink = toolkit.createButton(composite, Messages.useGBeanLink, SWT.RADIO);
		GridData data = new GridData();
		data.horizontalSpan = 2;
		specifyAsLink.setLayoutData(data);

		GbeanLocatorType wc = plan.getWebContainer();

		createLabel(composite, Messages.gBeanLink);
		String value = wc != null ? wc.getGbeanLink() : null;
		gBeanLink = toolkit.createText(composite, value, SWT.BORDER);
		gBeanLink.setLayoutData(createTextFieldGridData());
		gBeanLink.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getGBeanLocator().setGbeanLink(gBeanLink.getText());
				markDirty();
			}
		});

		specifyAsPattern = toolkit.createButton(composite, Messages.useGBeanPattern, SWT.RADIO);
		specifyAsPattern.setLayoutData(data);

		createLabel(composite, Messages.groupId);
		value = wc != null && wc.getPattern() != null ? wc.getPattern().getGroupId()
				: null;
		group = toolkit.createText(composite, value, SWT.BORDER);
		group.setLayoutData(createTextFieldGridData());
		group.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setPatternTypeAttribute(NamingPackage.eINSTANCE.getPatternType_GroupId(), group.getText());
				markDirty();
			}
		});

		createLabel(composite, Messages.artifactId);
		value = wc != null && wc.getPattern() != null ? wc.getPattern().getArtifactId()
				: null;
		artifact = toolkit.createText(composite, value, SWT.BORDER);
		artifact.setLayoutData(createTextFieldGridData());
		artifact.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setPatternTypeAttribute(NamingPackage.eINSTANCE.getPatternType_ArtifactId(), artifact.getText());
				markDirty();
			}
		});

		createLabel(composite, Messages.moduleId);
		value = wc != null && wc.getPattern() != null ? wc.getPattern().getModule()
				: null;
		module = toolkit.createText(composite, value, SWT.BORDER);
		module.setLayoutData(createTextFieldGridData());
		module.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setPatternTypeAttribute(NamingPackage.eINSTANCE.getPatternType_Module(), module.getText());
				markDirty();
			}
		});

		createLabel(composite, Messages.name);
		value = wc != null && wc.getPattern() != null ? wc.getPattern().getName()
				: null;
		name = toolkit.createText(composite, value, SWT.BORDER);
		name.setLayoutData(createTextFieldGridData());
		name.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setPatternTypeAttribute(NamingPackage.eINSTANCE.getPatternType_Name(), name.getText());
				markDirty();
			}
		});

		createLabel(composite, Messages.version);
		value = wc != null && wc.getPattern() != null ? wc.getPattern().getVersion()
				: null;
		version = toolkit.createText(composite, value, SWT.BORDER);
		version.setLayoutData(createTextFieldGridData());
		version.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setPatternTypeAttribute(NamingPackage.eINSTANCE.getPatternType_Version(), version.getText());
				markDirty();
			}
		});

		specifyAsLink.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (specifyAsLink.getSelection()) {
					if (plan.getWebContainer() != null
							&& plan.getWebContainer().eIsSet(NamingPackage.eINSTANCE.getGbeanLocatorType_Pattern())) {
						plan.getWebContainer().eUnset(NamingPackage.eINSTANCE.getGbeanLocatorType_Pattern());
						markDirty();
					}
					if (gBeanLink.getText().length() > 0) {
						plan.getWebContainer().setGbeanLink(gBeanLink.getText());
						markDirty();
					}
					toggle();
				}
			}
		});
 
		specifyAsPattern.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (specifyAsPattern.getSelection()) {
					if (plan.getWebContainer() != null
							&& plan.getWebContainer().eIsSet(NamingPackage.eINSTANCE.getGbeanLocatorType_GbeanLink())) {
						plan.getWebContainer().eUnset(NamingPackage.eINSTANCE.getGbeanLocatorType_GbeanLink());
						markDirty();
					}
					if (group.getText().length() > 0) {
						getPatternType().setGroupId(group.getText());
						markDirty();
					}
					if (artifact.getText().length() > 0) {
						getPatternType().setArtifactId(artifact.getText());
						markDirty();
					}
					if (module.getText().length() > 0) {
						getPatternType().setModule(module.getText());
						markDirty();
					}
					if (name.getText().length() > 0) {
						getPatternType().setName(name.getText());
						markDirty();
					}
					if (version.getText().length() > 0) {
						getPatternType().setVersion(version.getText());
						markDirty();
					}
					toggle();
				}
			}
		});

		if (wc != null) {
			if (wc.getGbeanLink() != null) {
				specifyAsLink.setSelection(true);
			} else if (wc.getPattern() != null) {
				specifyAsPattern.setSelection(true);
			}
		}
	}

	public void toggle() {
		gBeanLink.setEnabled(specifyAsLink.getSelection());
		artifact.setEnabled(specifyAsPattern.getSelection());
		group.setEnabled(specifyAsPattern.getSelection());
		module.setEnabled(specifyAsPattern.getSelection());
		name.setEnabled(specifyAsPattern.getSelection());
		version.setEnabled(specifyAsPattern.getSelection());
	}

	/**
	 * @return
	 */
	private GbeanLocatorType getGBeanLocator() {
		GbeanLocatorType wc = plan.getWebContainer();
		if (wc == null) {
			wc = NamingFactory.eINSTANCE.createGbeanLocatorType();
			plan.setWebContainer(wc);
		}
		return wc;
	}

	/**
	 * @return
	 */
	private PatternType getPatternType() {
		GbeanLocatorType locator = getGBeanLocator();
		PatternType pattern = locator.getPattern();
		if (pattern == null) {
			pattern = NamingFactory.eINSTANCE.createPatternType();
			locator.setPattern(pattern);
		}
		return pattern;
	}

	/**
	 * @param feature
	 * @param value
	 */
	private void setPatternTypeAttribute(EStructuralFeature feature, String value) {
		getPatternType().eSet(feature, value);
	}

	protected Label createLabel(Composite parent, String text) {
		Label label = toolkit.createLabel(parent, text);
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		return label;
	}

	protected GridData createTextFieldGridData() {
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
		data.widthHint = 150;
		return data;
	}

}
