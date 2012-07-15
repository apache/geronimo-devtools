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

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.geronimo.system.plugin.model.ArtifactType;
import org.apache.geronimo.system.plugin.model.DependencyType;
import org.apache.geronimo.system.plugin.model.LicenseType;
import org.apache.geronimo.system.plugin.model.PluginArtifactType;
import org.apache.geronimo.system.plugin.model.PluginListType;
import org.apache.geronimo.system.plugin.model.PluginType;
import org.apache.geronimo.system.plugin.model.PrerequisiteType;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.wizards.AbstractWizard;
import org.apache.geronimo.st.v21.core.operations.GeronimoServerV21PluginManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public class ServerPluginManagerWizard extends AbstractWizard {

    protected Text localRepoPath;
    protected Button createPlugin;
    protected Button installPlugin;
    protected Combo createPluginCombo;
    protected Label pluginCreatedStatus;
    protected Table licenseTable;
    protected Table prereqTable;
    protected Table pluginLoadTable;
    protected ServerPluginManagerWizardPage1 page1;
    protected ServerPluginManagerWizardPage2 page2;
    protected ServerPluginManagerWizardPage3 page3;
    protected ServerPluginManagerWizardPage4 page4;
    protected ServerPluginManagerWizardPage5 page5;

    // pageVisible is used to keep track of exactly which page we are viewing
    protected int pageVisible;
    protected GeronimoServerV21PluginManager pluginManager;
    protected PluginType metadata;

    public ServerPluginManagerWizard(GeronimoServerV21PluginManager customAssembly) {
        super();
        this.pluginManager = customAssembly;
        pageVisible = 0;
    }

    public void addPages() {
        addPage(new ServerPluginManagerWizardPage0("page0"));
        page1 = new ServerPluginManagerWizardPage1("page1");
        addPage(page1);
        page2 = new ServerPluginManagerWizardPage2("page2");
        addPage(page2);
        page3 = new ServerPluginManagerWizardPage3("page3");
        addPage(page3);
        page4 = new ServerPluginManagerWizardPage4("page4");
        addPage(page4);
        page5 = new ServerPluginManagerWizardPage5("page5");
        addPage(page5);
    }

    public void backPressed () {
        if (pageVisible == 4) {
            pageVisible = 0;
        } else {
            pageVisible--;
        }
    }

    // perform any intermediate steps when next button is pressed
    public void nextPressed () {
        switch (pageVisible) {
            case 0:
                if (installPlugin.getSelection() == true) {
                    page4.populateTable(pluginManager.readPluginList(localRepoPath.getText()));
                    page1.setPageComplete(true);
                    page2.setPageComplete(true);
                    page3.setPageComplete(true);
                    page5.setPageComplete(false);
                    pageVisible = 4;
                } else {
                    // refresh the list of available plugins
                    page1.refreshPluginList();
                    page1.setPageComplete(false);
                    page5.setPageComplete(true);
                    pageVisible = 1;
                }
                break;
            case 1:
                try {
                    metadata = pluginManager.getPluginMetadata(createPluginCombo.getItem(createPluginCombo.getSelectionIndex()));
                    page2.loadMetadata (metadata);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                pageVisible++;
                break;
            case 2:
                try {
                    page3.loadMetadata (metadata);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                pageVisible++;
                break;
            case 3:
                boolean success = true;
                try {
                    metadata = page2.getMetadata();
                    metadata = page3.getMetadata(metadata);
                    pluginManager.savePluginXML (createPluginCombo.getItem(createPluginCombo.getSelectionIndex()), metadata);
                    // create the plugin in the proper directory
                    pluginManager.exportCAR (localRepoPath.getText(), createPluginCombo.getItem(createPluginCombo.getSelectionIndex()));
                    // update the plugin list that's in the base path
                    pluginManager.updatePluginList (localRepoPath.getText(), metadata);
                }
                catch (Exception e) {
                    pluginCreatedStatus.setText(CommonMessages.bind(CommonMessages.failedToSave, createPluginCombo.getItem(createPluginCombo.getSelectionIndex())));
                    Device device = pluginCreatedStatus.getForeground().getDevice();
                    Color color = new Color (device, 255, 0, 0);
                    pluginCreatedStatus.setForeground(color);
                    success = false;
                }
                if (success == true) {
                    pluginCreatedStatus.setText(CommonMessages.bind (CommonMessages.savedSuccess, createPluginCombo.getItem(createPluginCombo.getSelectionIndex())));
                    Device device = pluginCreatedStatus.getForeground().getDevice();
                    Color color = new Color (device, 0, 0, 0);
                    pluginCreatedStatus.setForeground(color);
                }
                pageVisible = 0;
                break;
            case 4:
                ArrayList<String> eventList = page4.installPlugins();
                page5.setEventList (eventList);
                page5.setPageComplete(true);
                pageVisible++;
                break;
        }
    }

    public class ServerPluginManagerWizardPage0 extends AbstractWizardPage {

        public ServerPluginManagerWizardPage0(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            parent.setLayoutData(createGridData(400, 300));
            Composite composite = createComposite(parent);

            createLabel(composite, CommonMessages.localPluginRepo);
            createLabel(composite, "");
            localRepoPath = createTextField(composite, "");
            Button browseButton = createPushButton(composite, CommonMessages.browse);
            browseButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    DirectoryDialog dirDlg = new DirectoryDialog(getShell());
                    dirDlg.setMessage(CommonMessages.localPluginRepo);
                    dirDlg.setFilterPath(localRepoPath.getText());
                    localRepoPath.setText(dirDlg.open());
                }
            });

            localRepoPath.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent arg0) {
                    File temp = new File (localRepoPath.getText());
                    setPageComplete(temp.isDirectory());
                }
            });

            createPlugin = createButton(composite, CommonMessages.createPlugin);
            installPlugin = createButton(composite, CommonMessages.installPlugins);
            setPageComplete(false);
            pluginCreatedStatus = createLabel (composite, "");

            setControl(composite);
        }

        @Override
        public IWizardPage getNextPage() {
            IWizardPage[] pages = getPages();
            if (createPlugin.getSelection() == true) {
                return super.getNextPage();
            } else {
                return pages[4];
            }
        }

        @Override
        public void setPreviousPage(IWizardPage page) {
            super.setPreviousPage(null);
        }

        @Override
        protected String getWizardPageTitle() {
            return CommonMessages.wizardPage0Title_PluginManager;
        }

        @Override
        protected String getWizardPageDescription() {
            return CommonMessages.wizardPage0Description_PluginManager;
        }
    }

    public class ServerPluginManagerWizardPage1 extends AbstractWizardPage {

        public ServerPluginManagerWizardPage1(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            parent.setLayoutData(createGridData(400, 300));
            Composite composite = createComposite(parent);

            List<String> strList = pluginManager.getConfigurationList();
            String[] strArray = new String[strList.size()];
            strArray = strList.toArray(strArray);
            createPluginCombo = createCombo(composite, strArray, false);
            createPluginCombo.setVisibleItemCount(20);
            createPluginCombo.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    setPageComplete(createPluginCombo.getSelectionIndex() > -1);
                }
            });

            setPageComplete(false);
            setControl(composite);
        }

        public void refreshPluginList () {
            List<String> strList = pluginManager.getConfigurationList();
            String[] strArray = new String[strList.size()];
            strArray = strList.toArray(strArray);
            createPluginCombo.removeAll();
            createPluginCombo.setItems(strArray);
        }

        @Override
        protected String getWizardPageTitle() {
            return CommonMessages.wizardPage1Title_PluginManager;
        }

        @Override
        protected String getWizardPageDescription() {
            return CommonMessages.wizardPage1Description_PluginManager;
        }
    }

    public class ServerPluginManagerWizardPage2 extends AbstractWizardPage {
        Text name, downloadRepos, category, description, pluginURL, author;
        Text geronimoVersions, jvmVersions, dependencies, obsoletes;
        Label id;

        public ServerPluginManagerWizardPage2(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            parent.setLayoutData(createGridData(400, 300));
            Composite composite = createComposite(parent);

            createLabel (composite, CommonMessages.name);
            name = createTextField (composite, "");
            createLabel (composite, CommonMessages.id);
            id = createLabel (composite, "");
            createLabel (composite, CommonMessages.downloadRepos);
            downloadRepos = createMultiTextField (composite, null);
            createLabel (composite, CommonMessages.category);
            category = createTextField (composite, "");
            createLabel (composite, CommonMessages.description);
            description = createMultiTextField (composite, null);
            createLabel (composite, CommonMessages.pluginURL);
            pluginURL = createTextField (composite, "");
            createLabel (composite, CommonMessages.author);
            author = createTextField (composite, "");
            createLabel (composite, CommonMessages.geronimoVersions);
            geronimoVersions = createMultiTextField (composite, null);
            createLabel (composite, CommonMessages.jvmVersions);
            jvmVersions = createMultiTextField (composite, null);
            createLabel (composite, CommonMessages.dependencies);
            dependencies = createMultiTextField (composite, null);
            createLabel (composite, CommonMessages.obsoletes);
            obsoletes = createMultiTextField (composite, null);

            setControl(composite);
        }

        public void loadMetadata (PluginType metadata) {
            PluginArtifactType instance = metadata.getPluginArtifact().get(0);
            setText (name, metadata.getName());
            id.setText (artifactToString(instance.getModuleId()));
            setText (downloadRepos, combine (instance.getSourceRepository()));
            setText (category, metadata.getCategory());
            setText (description, metadata.getDescription());
            setText (pluginURL, metadata.getUrl());
            setText (author, metadata.getAuthor());
            setText (geronimoVersions, combine(instance.getGeronimoVersion()));
            setText (jvmVersions, combine(instance.getJvmVersion()));
            setText (dependencies, artifactsToString(instance.getDependency()));
            setText (obsoletes, artifactsToString(instance.getObsoletes()));
        }

        public PluginType getMetadata () {
            metadata = pluginManager.getPluginMetadata(id.getText());
            PluginArtifactType instance = metadata.getPluginArtifact().get(0);
            metadata.setName(name.getText());
            split (downloadRepos.getText(), instance.getSourceRepository());
            metadata.setCategory(category.getText());
            metadata.setDescription(description.getText());
            metadata.setUrl(pluginURL.getText());
            metadata.setAuthor(author.getText());
            split (geronimoVersions.getText(), instance.getGeronimoVersion());
            split (jvmVersions.getText(), instance.getJvmVersion());
            stringToDependencies (split(dependencies.getText()), instance.getDependency());
            stringToArtifacts (split(obsoletes.getText()), instance.getObsoletes());
            return metadata;
        }

        private void stringToArtifacts(List<String> artifacts, List<ArtifactType> result) {
            result.clear();
            for (String artifact : artifacts) {
                result.add(pluginManager.toArtifactType(artifact));
            }
        }

        private void stringToDependencies(List<String> artifacts, List<DependencyType> result) {
            result.clear();
            for (String artifact : artifacts) {
                result.add(pluginManager.toDependencyType(artifact));
            }
        }

        private List<String> split(String deps) {
            List<String> split = new ArrayList<String>();
            if (deps != null && !deps.equals("")) {
                split(deps, split);
            }
            return split;
        }

        private void split(String deps, List<String> split) {
            split.clear();
            BufferedReader in = new BufferedReader(new StringReader(deps));
            String line;
            try {
                while ((line = in.readLine()) != null) {
                    line = line.trim();
                    if (!line.equals("")) {
                        split.add(line);
                    }
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String artifactsToString(List<? extends ArtifactType> artifacts) {
            if (artifacts == null || artifacts.size() == 0) {
                return null;
            }
            StringBuffer buf = new StringBuffer();
            boolean first = true;
            for (ArtifactType artifactType : artifacts) {
                if (!first) {
                    buf.append("\n");
                }
                first = false;
                buf.append(artifactToString(artifactType));
            }
            return buf.toString();
        }

        private String artifactToString(ArtifactType artifact) {
            StringBuffer buffer = new StringBuffer();

            if (artifact.getGroupId() != null) {
                buffer.append(artifact.getGroupId());
            }
            buffer.append("/");

            if (artifact.getArtifactId() != null) {
                buffer.append(artifact.getArtifactId());
            }
            buffer.append("/");

            if (artifact.getVersion() != null) {
                buffer.append(artifact.getVersion());
            }
            buffer.append("/");

            if (artifact.getType() != null) {
                buffer.append(artifact.getType());
            }
            return buffer.toString();
        }

        private void setText (Text text, String value) {
            if (value != null) {
                text.setText(value);
            } else {
                text.setText("");
            }
        }

        private String combine(List<String> strings) {
            if (strings == null || strings.size() == 0) {
                return null;
            }
            StringBuffer buf = new StringBuffer();
            boolean first = true;
            for (String string : strings) {
                if (!first) {
                    buf.append("\n");
                }
                first = false;
                buf.append(string);
            }
            return buf.toString();
        }

        @Override
        protected String getWizardPageTitle() {
            return CommonMessages.wizardPage2Title_PluginManager;
        }

        @Override
        protected String getWizardPageDescription() {
            return CommonMessages.wizardPage2Description_PluginManager;
        }
    }

    public class ServerPluginManagerWizardPage3 extends AbstractWizardPage {
        protected Button addLicenseButton, editLicenseButton, removeLicenseButton;
        protected Button addPrereqButton, editPrereqButton, removePrereqButton;
        protected PluginType pluginType;

        public ServerPluginManagerWizardPage3(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            parent.setLayoutData(createGridData(400, 300));
            Composite composite = createComposite(parent);

            createLabel(composite, CommonMessages.license);
            createLabel(composite, "");
            String[] lColumnNames = {CommonMessages.license, CommonMessages.osiApproved};
            int[] lColumnWidths = {275, 100};
            licenseTable = createEditableTable(composite, lColumnNames, lColumnWidths);
            licenseTable.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    activateButtons();
                }
            });
            Composite licenseButtonComposite = createButtonComposite(composite);
            addLicenseButton = createPushButton(licenseButtonComposite, CommonMessages.add);
            addLicenseButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    LicenseWizard wizard = new LicenseWizard(null);
                    if (wizard != null) {
                        WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
                        dialog.open();
                        if (dialog.getReturnCode() == Dialog.OK) {
                            LicenseType newLicense = wizard.getLicense();
                            pluginType.getLicense().add(newLicense);
                            loadMetadata(pluginType);
                            activateButtons();
                        }
                    }
                }
            });
            editLicenseButton = createPushButton(licenseButtonComposite, CommonMessages.edit);
            editLicenseButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    LicenseType oldLicense = (LicenseType)licenseTable.getItem(licenseTable.getSelectionIndex()).getData();
                    LicenseWizard wizard = new LicenseWizard(oldLicense);
                    if (wizard != null) {
                        WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
                        dialog.open();
                        if (dialog.getReturnCode() == Dialog.OK) {
                            LicenseType newLicense = wizard.getLicense();
                            int index = pluginType.getLicense().indexOf(oldLicense);
                            pluginType.getLicense().remove(index);
                            pluginType.getLicense().add(index, newLicense);
                            loadMetadata(pluginType);
                            activateButtons();
                        }
                    }
                }
            });
            removeLicenseButton = createPushButton(licenseButtonComposite, CommonMessages.remove);
            removeLicenseButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    LicenseType license;
                    license = (LicenseType)licenseTable.getItem(licenseTable.getSelectionIndex()).getData();
                    pluginType.getLicense().remove(license);
                    loadMetadata(pluginType);
                    activateButtons();
                }
            });

            createLabel(composite, CommonMessages.prerequisites);
            createLabel(composite, "");
            String[] pColumnNames = {CommonMessages.groupId, CommonMessages.artifactId, CommonMessages.version, CommonMessages.type, CommonMessages.description};
            int[] pColumnWidths = {50, 50, 50, 50, 200};
            prereqTable = createEditableTable(composite, pColumnNames, pColumnWidths);
            prereqTable.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    activateButtons();
                }
            });
            Composite prereqButtonComposite = createButtonComposite(composite);
            addPrereqButton = createPushButton(prereqButtonComposite, CommonMessages.add);
            addPrereqButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    PrerequisiteWizard wizard = new PrerequisiteWizard(null);
                    if (wizard != null) {
                        WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
                        dialog.open();
                        if (dialog.getReturnCode() == Dialog.OK) {
                            PrerequisiteType newPrereq = wizard.getPrerequisite();
                            pluginType.getPluginArtifact().get(0).getPrerequisite().add(newPrereq);
                            loadMetadata(pluginType);
                            activateButtons();
                        }
                    }
                }
            });
            editPrereqButton = createPushButton(prereqButtonComposite, CommonMessages.edit);
            editPrereqButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    PrerequisiteType oldPrereq = (PrerequisiteType)prereqTable.getItem(prereqTable.getSelectionIndex()).getData();
                    PrerequisiteWizard wizard = new PrerequisiteWizard(oldPrereq);
                    if (wizard != null) {
                        WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
                        dialog.open();
                        if (dialog.getReturnCode() == Dialog.OK) {
                            PrerequisiteType newPrereq = wizard.getPrerequisite();
                            int index = pluginType.getPluginArtifact().get(0).getPrerequisite().indexOf(oldPrereq);
                            pluginType.getPluginArtifact().get(0).getPrerequisite().remove(index);
                            pluginType.getPluginArtifact().get(0).getPrerequisite().add(index, newPrereq);
                            loadMetadata(pluginType);
                            activateButtons();
                        }
                    }
                }
            });
            removePrereqButton = createPushButton(prereqButtonComposite, CommonMessages.remove);
            removePrereqButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    PrerequisiteType prereq;
                    prereq = (PrerequisiteType)prereqTable.getItem(prereqTable.getSelectionIndex()).getData();
                    pluginType.getPluginArtifact().get(0).getPrerequisite().remove(prereq);
                    loadMetadata(pluginType);
                    activateButtons();
                }
            });
            activateButtons();
            setControl(composite);
        }

        protected void activateButtons() {
            editLicenseButton.setEnabled(licenseTable.getSelectionCount() > 0);
            removeLicenseButton.setEnabled(licenseTable.getSelectionCount() > 0);
            editPrereqButton.setEnabled(prereqTable.getSelectionCount() > 0);
            removePrereqButton.setEnabled(prereqTable.getSelectionCount() > 0);
        }

        protected Composite createButtonComposite(Composite parent) {
            Composite buttonComp = new Composite(parent, SWT.NONE);
            GridLayout layout = new GridLayout();
            layout.horizontalSpacing = 2;
            layout.verticalSpacing = 2;
            layout.marginWidth = 0;
            layout.marginHeight = 0;
            layout.numColumns = 1;
            buttonComp.setLayout(layout);
            buttonComp.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
            return buttonComp;
        }

        @Override
        public IWizardPage getNextPage() {
            return getPages()[0];
        }

        public void loadMetadata (PluginType metadata) {
            pluginType = metadata;
            PluginArtifactType instance = pluginType.getPluginArtifact().get(0);
            licenseTable.removeAll();
            List<LicenseType> licenses = pluginType.getLicense();
            for (int i = 0; i < licenses.size(); i++) {
                TableItem tabItem = new TableItem (licenseTable, SWT.NONE);
                LicenseType license = licenses.get(i);
                tabItem.setData(license);
                tabItem.setText(licenseToStringArray(license));
            }
            prereqTable.removeAll();
            List<PrerequisiteType> prereqs = instance.getPrerequisite();
            for (int i = 0; i < prereqs.size(); i++) {
                TableItem tabItem = new TableItem (prereqTable, SWT.NONE);
                PrerequisiteType prereq = prereqs.get(i);
                tabItem.setData(prereq);
                tabItem.setText(prereqToStringArray(prereq));
            }
        }

        // may not need this once the add/edit/delete buttons are available
        public PluginType getMetadata (PluginType metadata) {
            PluginArtifactType instance = metadata.getPluginArtifact().get(0);

            metadata.getLicense().clear();
            for (int i = 0; i < licenseTable.getItemCount(); i++) {
                LicenseType license = (LicenseType)licenseTable.getItem (i).getData();
                metadata.getLicense().add(license);
            }
            instance.getPrerequisite().clear();
            for (int i = 0; i < prereqTable.getItemCount(); i++) {
                PrerequisiteType prereq = (PrerequisiteType)prereqTable.getItem (i).getData();
                instance.getPrerequisite().add(prereq);
            }
            return metadata;
        }

        private String[] licenseToStringArray (LicenseType license) {
            String[] stringArray = new String[licenseTable.getColumnCount()];
            stringArray[0] = license.getValue();
            stringArray[1] = String.valueOf(license.isOsiApproved());
            return stringArray;
        }

        private String[] prereqToStringArray (PrerequisiteType prereq) {
            String[] stringArray = new String[prereqTable.getColumnCount()];
            if (prereq.getId() != null) {
                stringArray[0] = prereq.getId().getGroupId();
                stringArray[1] = prereq.getId().getArtifactId();
                stringArray[2] = prereq.getId().getVersion();
            } else {
                stringArray[0] = "";
                stringArray[1] = "";
                stringArray[2] = "";
            }
            stringArray[3] = prereq.getResourceType();
            stringArray[4] = prereq.getDescription();
            return stringArray;
        }

        @Override
        protected String getWizardPageTitle() {
            return CommonMessages.wizardPage3Title_PluginManager;
        }

        @Override
        protected String getWizardPageDescription() {
            return CommonMessages.wizardPage3Description_PluginManager;
        }
    }

    public class ServerPluginManagerWizardPage4 extends AbstractWizardPage {

        public ServerPluginManagerWizardPage4(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            parent.setLayoutData(createGridData(400, 300));
            Composite composite = createComposite(parent);

            String[] columnNames = {CommonMessages.plugin, CommonMessages.version, CommonMessages.category, CommonMessages.installable};
            int[] columnWidths = {175, 75, 100, 75};
            pluginLoadTable = this.createTable(composite, columnNames, columnWidths);
            pluginLoadTable.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    TableItem tabItem = pluginLoadTable.getItem(pluginLoadTable.getSelectionIndex());
                    if (tabItem.getText(3).equals("false")) {
                        pluginLoadTable.deselect(pluginLoadTable.getSelectionIndex());
                    }
                    if (pluginLoadTable.getSelectionCount() > 0) {
                        setPageComplete(true);
                    } else {
                        setPageComplete(false);
                    }
                }
            });

            setPageComplete(false);
            setControl(composite);
        }

        @Override
        public IWizardPage getPreviousPage() {
            return getPages()[0];
        }

        public void populateTable (PluginListType pluginList) {
            String[] pluginValues;
            pluginLoadTable.removeAll();
            List<PluginType> plugins = pluginList.getPlugin();
            for (int i = 0; i < plugins.size(); i++) {
                TableItem tabItem = new TableItem (pluginLoadTable, SWT.NONE);
                PluginType plugin = plugins.get(i);
                tabItem.setData(plugin);
                pluginValues = pluginToStringArray(plugin);
                tabItem.setText(pluginValues);
            }
        }

        // install selected plugins to the server
        public ArrayList<String> installPlugins() {
            // take each selected object in the pluginLoadTable and install and start
            List<PluginType> pluginList = new ArrayList<PluginType>();
            for (int i = 0; i < pluginLoadTable.getSelectionCount(); i++) {
                pluginList.add ((PluginType)pluginLoadTable.getItem(pluginLoadTable.getSelectionIndices()[i]).getData());
            }
            return pluginManager.installPlugins(localRepoPath.getText(), pluginList);
        }

        private String[] pluginToStringArray (PluginType plugin) {
            String[] stringArray = new String[pluginLoadTable.getColumnCount()];
            stringArray[0] = plugin.getName();
            stringArray[1] = plugin.getPluginArtifact().get(0).getModuleId().getVersion();
            stringArray[2] = plugin.getCategory();
            stringArray[3] = String.valueOf(pluginManager.validatePlugin(plugin));
            return stringArray;
        }

        @Override
        protected String getWizardPageTitle() {
            return CommonMessages.wizardPage4Title_PluginManager;
        }

        @Override
        protected String getWizardPageDescription() {
            return CommonMessages.wizardPage4Description_PluginManager;
        }
    }

    public class ServerPluginManagerWizardPage5 extends AbstractWizardPage {
        Table eventTable;

        public ServerPluginManagerWizardPage5(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            parent.setLayoutData(createGridData(400, 300));
            Composite composite = createComposite(parent);

            String[] columnNames = {CommonMessages.event};
            int[] columnWidths = {400};
            eventTable = this.createTable(composite, columnNames, columnWidths);

            setPageComplete(true);
            setControl(composite);
        }

        protected void setEventList (ArrayList<String> eventList) {
            eventTable.clearAll();
            for (int i = 0; i < eventList.size(); i++) {
                TableItem tabItem = new TableItem (eventTable, SWT.NONE);
                String event = eventList.get(i);
                tabItem.setData(event);
                tabItem.setText(event);
            }
        }

        @Override
        protected String getWizardPageTitle() {
            return CommonMessages.wizardPage5Title_PluginManager;
        }

        @Override
        protected String getWizardPageDescription() {
            return CommonMessages.wizardPage5Description_PluginManager;
        }
    }

    // everything already done, simply close everything down
    public boolean performFinish() {
        return true;
    }

    @Override
    protected String getAddWizardWindowTitle() {
        return CommonMessages.wizardTitle_PluginManager;
    }

    @Override
    protected String getEditWizardWindowTitle() {
        return CommonMessages.wizardTitle_PluginManager;
    }
}
