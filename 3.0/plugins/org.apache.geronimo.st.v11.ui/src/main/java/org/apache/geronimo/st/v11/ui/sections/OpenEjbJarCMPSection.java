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

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.naming.ObjectFactory;
import org.apache.geronimo.j2ee.naming.PatternType;
import org.apache.geronimo.j2ee.naming.ResourceLocatorType;
import org.apache.geronimo.j2ee.openejb_jar.OpenejbJarType;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.sections.AbstractSectionPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
public class OpenEjbJarCMPSection extends AbstractSectionPart {

    protected Text resourceLink;

    protected Text url;

    protected Text artifact;

    protected Text group;

    protected Text module;

    protected Text name;

    protected Text version;

    protected Button specifyAsLink;

    protected Button specifyAsUrl;

    protected Button specifyAsPattern;

    OpenejbJarType plan;
    
    private ObjectFactory namingFactory;

    /**
     * @param parent
     * @param toolkit
     * @param style
     * @param plan
     */
    public OpenEjbJarCMPSection(Composite parent, FormToolkit toolkit, int style, JAXBElement plan) {
        super(parent, toolkit, style, plan);
        this.plan = (OpenejbJarType) plan.getValue();
        namingFactory = new ObjectFactory();
        createClient();
    }

    protected void createClient() {
        Section section = getSection();

        section.setText(Messages.cmpConnectionSection);
        section.setDescription(Messages.cmpConnectionSectionDescription);
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

        specifyAsLink = toolkit.createButton(composite, CommonMessages.useResourceLink, SWT.RADIO);
        GridData data = new GridData();
        data.horizontalSpan = 2;
        specifyAsLink.setLayoutData(data);

        ResourceLocatorType locator = plan.getCmpConnectionFactory();

        toolkit.createLabel(composite, CommonMessages.resourceLink);
        String value = locator != null ? locator.getResourceLink() : null;
        resourceLink = toolkit.createText(composite, value, SWT.BORDER);
        resourceLink.setLayoutData(createTextFieldGridData());
        resourceLink.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getResourceLocator().setResourceLink(resourceLink.getText());
                markDirty();
            }
        });
        
        specifyAsUrl = toolkit.createButton(composite, CommonMessages.useUrl, SWT.RADIO);
        data = new GridData();
        data.horizontalSpan = 2;
        specifyAsUrl.setLayoutData(data);

        toolkit.createLabel(composite, CommonMessages.url);
        value = locator != null ? locator.getUrl() : null;
        url = toolkit.createText(composite, value, SWT.BORDER);
        url.setLayoutData(createTextFieldGridData());
        url.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getResourceLocator().setUrl(url.getText());
                markDirty();
            }
        });

        specifyAsPattern = toolkit.createButton(composite, CommonMessages.useResourcePattern, SWT.RADIO);
        specifyAsPattern.setLayoutData(data);

        toolkit.createLabel(composite, CommonMessages.groupId);
        value = locator != null && locator.getPattern() != null ? locator.getPattern().getGroupId()
                : null;
        group = toolkit.createText(composite, value, SWT.BORDER);
        group.setLayoutData(createTextFieldGridData());
        group.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getPattern().setGroupId(group.getText());
                markDirty();
            }
        });

        toolkit.createLabel(composite, CommonMessages.artifactId);
        value = locator != null && locator.getPattern() != null ? locator.getPattern().getArtifactId()
                : null;
        artifact = toolkit.createText(composite, value, SWT.BORDER);
        artifact.setLayoutData(createTextFieldGridData());
        artifact.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getPattern().setArtifactId(artifact.getText());
                markDirty();
            }
        });

        toolkit.createLabel(composite, CommonMessages.moduleId);
        value = locator != null && locator.getPattern() != null ? locator.getPattern().getModule()
                : null;
        module = toolkit.createText(composite, value, SWT.BORDER);
        module.setLayoutData(createTextFieldGridData());
        module.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getPattern().setModule(module.getText());
                markDirty();
            }
        });

        toolkit.createLabel(composite, CommonMessages.name);
        value = locator != null && locator.getPattern() != null ? locator.getPattern().getName()
                : null;
        name = toolkit.createText(composite, value, SWT.BORDER);
        name.setLayoutData(createTextFieldGridData());
        name.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getPattern().setName(name.getText());
                markDirty();
            }
        });

        toolkit.createLabel(composite, CommonMessages.version);
        value = locator != null && locator.getPattern() != null ? locator.getPattern().getVersion()
                : null;
        version = toolkit.createText(composite, value, SWT.BORDER);
        version.setLayoutData(createTextFieldGridData());
        version.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getPattern().setVersion(version.getText());
                markDirty();
            }
        });

        specifyAsLink.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (specifyAsLink.getSelection()) {
                    getResourceLocator().setUrl(null);
                    getResourceLocator().setPattern(null);
                    if (resourceLink.getText().length() > 0) {
                        getResourceLocator().setResourceLink(resourceLink.getText());
                    }
                    markDirty();
                    toggle();
                }
            }
        });

        specifyAsUrl.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (specifyAsUrl.getSelection()) {
                    getResourceLocator().setResourceLink(null);
                    getResourceLocator().setPattern(null);
                    if (url.getText().length() > 0) {
                        getResourceLocator().setUrl(url.getText());
                    }
                    markDirty();
                    toggle();
                }
            }
        });
 
        specifyAsPattern.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (specifyAsPattern.getSelection()) {
              	    getResourceLocator().setResourceLink (null);
                    getResourceLocator().setUrl (null);

                    if (group.getText().length() > 0) {
                        getPattern().setGroupId(group.getText());
                    }
                    if (artifact.getText().length() > 0) {
                        getPattern().setArtifactId(artifact.getText());
                    }
                    if (module.getText().length() > 0) {
                        getPattern().setModule(module.getText());
                    }
                    if (name.getText().length() > 0) {
                        getPattern().setName(name.getText());
                    }
                    if (version.getText().length() > 0) {
                        getPattern().setVersion(version.getText());
                    }
                    markDirty();
                    toggle();
                }
            }
        });

        if (locator != null) {
            if (locator.getResourceLink() != null) {
                specifyAsLink.setSelection(true);
            } else if (locator.getUrl() != null) {
                specifyAsUrl.setSelection(true);
            } else if (locator.getPattern() != null) {
                specifyAsPattern.setSelection(true);
            }
        }

        toggle();
    }

    public void toggle() {
        resourceLink.setEnabled(specifyAsLink.getSelection());
        url.setEnabled(specifyAsUrl.getSelection());
        artifact.setEnabled(specifyAsPattern.getSelection());
        group.setEnabled(specifyAsPattern.getSelection());
        module.setEnabled(specifyAsPattern.getSelection());
        name.setEnabled(specifyAsPattern.getSelection());
        version.setEnabled(specifyAsPattern.getSelection());
    }

    /**
     * @return
     */
    private ResourceLocatorType getResourceLocator() {
    	ResourceLocatorType locator = plan.getCmpConnectionFactory();
        if (locator == null) {
            locator = namingFactory.createResourceLocatorType();
            plan.setCmpConnectionFactory(locator);
        }
        return locator;
    }

    /**
     * @return
     */
    private PatternType getPattern() {
        ResourceLocatorType locator = getResourceLocator();
        PatternType pattern = locator.getPattern();
        if (pattern == null) {
            pattern = namingFactory.createPatternType();
            locator.setPattern(pattern);
        }
        return pattern;
    }
}
