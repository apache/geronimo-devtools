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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.application.ExtModule;
import org.apache.geronimo.jee.connector.Connector;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.deployment.Pattern;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.sections.AbstractListSection.ContentProvider;
import org.apache.geronimo.st.ui.sections.AbstractListSection.LabelProvider;
import org.apache.geronimo.st.v21.ui.pages.ConnectorPage;
import org.apache.geronimo.st.v21.ui.wizards.DBPoolWizard;
import org.apache.geronimo.st.v21.ui.wizards.ExtModuleWizard;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class DBPoolSection extends AbstractTableSection {

    public DBPoolSection(JAXBElement plan, Composite parent,
        FormToolkit toolkit, int style, List extModules) {
    super(plan, parent, toolkit, style);
    this.objectContainer = extModules;
    COLUMN_NAMES = new String[] { Messages.wizardDatabasePoolConnector, Messages.wizardDatabasePoolExternalPath };// TODO
                                      // put
                                      // into
                                      // message
    createClient();
    }

    @Override
    public String getDescription() {
    return Messages.wizardDatabasePoolDescription;// TODO put into
                               // message
    }

    @Override
    public Class getTableEntryObjectType() {
    return ExtModule.class;// TODO put into message
    }

    @Override
    public String getTitle() {
    return Messages.wizardDatabasePoolTitle;// TODO put into message
    }

    @Override
    protected Wizard getWizard() {
    return new DBPoolWizard(this);
    }

    protected void notifyOthers() {
    notifyExtModuleSectionToRefresh();
    }

    /*
     * After add, remove, edit dbpool ext-module, notify the extModuleSection to
     * refresh. If the deploymentpage has not been initialized, catch a
     * NullPointerException and just ignore it.
     */
    private void notifyExtModuleSectionToRefresh() {
    try {
        ConnectorPage connectorPage = (ConnectorPage) this.getManagedForm()
            .getContainer();
        FormEditor editor = connectorPage.getEditor();
        IFormPart[] parts = editor.findPage("deploymentpage")
            .getManagedForm().getParts();
        ExtModuleSection extModuleSection = null;
        for (IFormPart part : parts) {
        if (ExtModuleSection.class.isInstance(part)) {
            extModuleSection = (ExtModuleSection) part;
        }
        }
        extModuleSection.getViewer().refresh();
    } catch (NullPointerException e) {
        // Ignore, this exception happens when the deployment page hasn't
        // been initialized
    }
    }

    @Override
    public ITreeContentProvider getContentProvider() {
    return new ContentProvider() {
        @Override
        public Object[] getElements(Object inputElement) {
        List<ExtModule> result = new ArrayList<ExtModule>();
        List extModules = getObjectContainer();
        Iterator it = extModules.iterator();
        while (it.hasNext()) {
            ExtModule current = (ExtModule) it.next();
            if (isDBPoolConnectorExtModule(current)) {
            result.add(current);
            }
        }
        return result.toArray();
        }
    };
    }

    private boolean isDBPoolConnectorExtModule(ExtModule extModule) {
    boolean result = false;
    JAXBElement any = (JAXBElement) extModule.getAny();
    if (any == null)
        return false;
    Object anyValue = any.getValue();
    if (Connector.class.isInstance(anyValue)) {
        Connector connector = (Connector) anyValue;
        try {
        // if the <ext-module/> contains <connectionfactory-interface/>
        // which value is "javax.sql.DataSource",then it is a dbpool
        // ext-module.
        String connectionfactoryInterface = connector
            .getResourceadapter().get(0)
            .getOutboundResourceadapter().getConnectionDefinition()
            .get(0).getConnectionfactoryInterface();
        if (connectionfactoryInterface.trim().equals(
            "javax.sql.DataSource")) {
            result = true;
        }
        } catch (NullPointerException e) {
        // e.printStackTrace();
        }
    }
    return result;
    }

    @Override
    public ITableLabelProvider getLabelProvider() {
    return new LabelProvider() {
        @Override
        public String getColumnText(Object element, int columnIndex) {
        if (ExtModule.class.isInstance(element)) {
            ExtModule extModule = (ExtModule) element;
            switch (columnIndex) {
            case 0:// connector
            if (extModule.getConnector() != null) {
                return extModule.getConnector().getValue();
            }
            return "";
            case 1:// external-path
            Pattern externalPath = extModule.getExternalPath();
            if (externalPath != null) {
                String groupId = externalPath.getGroupId() != null ? externalPath
                    .getGroupId()
                    : "";
                String artifactId = externalPath.getArtifactId() != null ? externalPath
                    .getArtifactId()
                    : "";
                String version = externalPath.getVersion() != null ? externalPath
                    .getVersion()
                    : "";
                String type = externalPath.getType() != null ? externalPath
                    .getType()
                    : "";
                return groupId + "/" + artifactId + "/" + version
                    + "/" + type;
            }
            return "";
            }
        }
        return null;
        }
    };
    }
}
