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

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.naming.GbeanLocator;
import org.apache.geronimo.jee.naming.ObjectFactory;
import org.apache.geronimo.jee.naming.Pattern;
import org.apache.geronimo.jee.web.WebApp;
import org.apache.geronimo.st.ui.CommonMessages;
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
public class WebContainerSection extends AbstractSectionPart {

    protected Text gBeanLink;

    protected Text artifact;

    protected Text group;

    protected Text module;

    protected Text name;

    protected Text version;

    protected Button specifyAsLink;

    protected Button specifyAsPattern;

    WebApp plan;
    
    private ObjectFactory namingFactory;

    /**
     * @param parent
     * @param toolkit
     * @param style
     * @param plan
     */
    public WebContainerSection(Composite parent, FormToolkit toolkit, int style, JAXBElement plan) {
        super(parent, toolkit, style, plan);
        this.plan = (WebApp) plan.getValue();
        namingFactory = new ObjectFactory();
        createClient();
    }

    protected void createClient() {
        Section section = getSection();

        section.setText(CommonMessages.webContainerSection);
        section.setDescription(CommonMessages.webContainerSectionDescription);
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

        specifyAsLink = toolkit.createButton(composite, CommonMessages.useGBeanLink, SWT.RADIO);
        GridData data = new GridData();
        data.horizontalSpan = 2;
        specifyAsLink.setLayoutData(data);

        GbeanLocator wc = plan.getWebContainer();

        toolkit.createLabel(composite, CommonMessages.gBeanLink);
        String value = wc != null ? wc.getGbeanLink() : null;
        gBeanLink = toolkit.createText(composite, value, SWT.BORDER);
        gBeanLink.setLayoutData(createTextFieldGridData());
        gBeanLink.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getGBeanLocator().setGbeanLink(gBeanLink.getText());
                markDirty();
            }
        });

        specifyAsPattern = toolkit.createButton(composite, CommonMessages.useGBeanPattern, SWT.RADIO);
        specifyAsPattern.setLayoutData(data);

        toolkit.createLabel(composite, CommonMessages.groupId);
        value = wc != null && wc.getPattern() != null ? wc.getPattern().getGroupId()
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
        value = wc != null && wc.getPattern() != null ? wc.getPattern().getArtifactId()
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
        value = wc != null && wc.getPattern() != null ? wc.getPattern().getModule()
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
        value = wc != null && wc.getPattern() != null ? wc.getPattern().getName()
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
        value = wc != null && wc.getPattern() != null ? wc.getPattern().getVersion()
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
                    getGBeanLocator().setPattern(null);
                    if (gBeanLink.getText().length() > 0) {
                        plan.getWebContainer().setGbeanLink(gBeanLink.getText());
                    }
                    markDirty();
                    toggle();
                }
            }
        });
 
        specifyAsPattern.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (specifyAsPattern.getSelection()) {
                	if (plan.getWebContainer() != null) {
                	    plan.getWebContainer().setGbeanLink (null);
                	}
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

        if (wc != null) {
            if (wc.getGbeanLink() != null) {
                specifyAsLink.setSelection(true);
            } else if (wc.getPattern() != null) {
                specifyAsPattern.setSelection(true);
            }
        }

        toggle();
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
    private GbeanLocator getGBeanLocator() {
        GbeanLocator wc = plan.getWebContainer();
        if (wc == null) {
            wc = namingFactory.createGbeanLocator();
            plan.setWebContainer(wc);
        }
        return wc;
    }

    /**
     * @return
     */
    private Pattern getPattern() {
        GbeanLocator locator = getGBeanLocator();
        Pattern pattern = locator.getPattern();
        if (pattern == null) {
            pattern = namingFactory.createPattern();
            locator.setPattern(pattern);
        }
        return pattern;
    }
}
