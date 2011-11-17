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

import java.util.ArrayList;
import javax.xml.bind.JAXBElement;
import org.apache.geronimo.jee.application.Application;
import org.apache.geronimo.jee.application.ExtModule;
import org.apache.geronimo.jee.application.Path;
import org.apache.geronimo.jee.connector.ConfigPropertySetting;
import org.apache.geronimo.jee.connector.ConnectionDefinition;
import org.apache.geronimo.jee.connector.ConnectiondefinitionInstance;
import org.apache.geronimo.jee.connector.Connectionmanager;
import org.apache.geronimo.jee.connector.Connector;
import org.apache.geronimo.jee.connector.OutboundResourceadapter;
import org.apache.geronimo.jee.connector.Resourceadapter;
import org.apache.geronimo.jee.connector.Singlepool;
import org.apache.geronimo.jee.connector.Xatransaction;
import org.apache.geronimo.jee.deployment.Dependencies;
import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.deployment.Pattern;
import org.apache.geronimo.jee.openejb.OpenejbJar;
import org.apache.geronimo.jee.web.WebApp;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.Activator;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBObjectFactoryImpl;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public class DBPoolWizard extends AbstractTableWizard {

    protected ImageDescriptor descriptor = Activator.imageDescriptorFromPlugin(
        "org.apache.geronimo.st.ui", "icons/bigG.gif");

    public DBPoolWizard(AbstractTableSection section) {
    super(section);
    }

    @Override
    public JAXBObjectFactory getEFactory() {
    return JAXBObjectFactoryImpl.getInstance();
    }

    @Override
    public String[] getTableColumnEAttributes() {
    return new String[] {};
    }

    @Override
    protected String getAddWizardWindowTitle() {
    return Messages.wizardDatabasePoolNew;// TODO put into message
    }

    @Override
    protected String getEditWizardWindowTitle() {
    return Messages.wizardDatabasePoolEdit;// TODO put into message
    }

    public void addPages() {
    ExternalPathPage page0 = new ExternalPathPage("Page0");
    page0.setImageDescriptor(descriptor);
    addPage(page0);

    DBPoolBasicPage page1 = new DBPoolBasicPage("Page1");
    page1.setImageDescriptor(descriptor);
    addPage(page1);

    DBPoolAdvancedPage page2 = new DBPoolAdvancedPage("Page2");
    page2.setImageDescriptor(descriptor);
    addPage(page2);

    ConnectionPoolPage page3 = new ConnectionPoolPage("Page3");
    page3.setImageDescriptor(descriptor);
    addPage(page3);
    }

    public boolean performFinish() {
    JAXBElement plan = this.section.getPlan();

    // all pages
    ExternalPathPage page0 = (ExternalPathPage) getPages()[0];
    DBPoolBasicPage page1 = (DBPoolBasicPage) getPages()[1];
    DBPoolAdvancedPage page2 = (DBPoolAdvancedPage) getPages()[2];
    ConnectionPoolPage page3 = (ConnectionPoolPage) getPages()[3];

    // check necessary info has been filled
    if (page1.getPoolName() == null
        || page1.getPoolName().trim().length() == 0
        || page1.getPoolName().contains(" ")) {
        MessageDialog.openWarning(this.getShell(), Messages.poolNameInvalidTitle,
            Messages.poolNameInvalidDesc);
        return false;
    }
    if (page1.getDBPooolType() == null
        || page1.getDBPooolType().trim().length() == 0) {
        MessageDialog.openWarning(this.getShell(), Messages.poolTypeInvalidTitle,
            Messages.poolTypeInvalidDesc);
        return false;
    }
    if (page1.getDBName() == null || page1.getDBName().trim().length() == 0
        || page1.getDBName().contains(" ")) {
        MessageDialog.openWarning(this.getShell(), Messages.dbNameInvalidTitle,
        		Messages.dbNameInvalidDesc);
        return false;
    }
    if (page1.getDrivers() == null || page1.getDrivers().length == 0) {
        MessageDialog.openWarning(this.getShell(), Messages.dbDriverInvalidTitle,
            Messages.dbDriverInvalidDesc);
        return false;
    }

    // add the dependencies and ext-module to plan
    if (Application.class.isInstance(plan.getValue())) {
        Application application = (Application) plan.getValue();
        Environment env = application.getEnvironment();
        if (env == null) {
        env = new Environment();
        application.setEnvironment(env);
        }
        Dependencies dependencies = env.getDependencies();
        if (dependencies == null) {
        dependencies = new Dependencies();
        env.setDependencies(dependencies);
        }

        // if there are no same dependencies in plan,then add them
        for (int i = 0; i < page1.getDrivers().length; i++) {
        Dependency selectedDependency = (Dependency) page1.getDrivers()[i];
        if (!dependencies.getDependency().contains(selectedDependency)) {
            dependencies.getDependency().add(selectedDependency);
        }
        }

        ExtModule extModule;
        if (eObject == null) {
        eObject = getEFactory().create(ExtModule.class);
        extModule = (ExtModule) eObject;
        java.util.List<ExtModule> extModuleList = application
            .getExtModule();
        extModuleList.add(extModule);
        } else {
        extModule = (ExtModule) eObject;
        }

        // NOTE!! this replaces the call to processEAttributes (page);
        Path path = (Path) getEFactory().create(Path.class);
        path.setValue(page0.text[0].getText());
        extModule.setConnector(path);

        Pattern pattern = (Pattern) getEFactory().create(Pattern.class);
        extModule.setExternalPath(pattern);
        pattern.setGroupId(page0.text[1].getText());
        pattern.setArtifactId(page0.text[2].getText());
        //empty version element will cause deploy failure
        String version = page0.text[3].getText();
        if (version!=null && version.length()!=0)
            pattern.setVersion(page0.text[3].getText());
        pattern.setType(page0.text[4].getText().trim());

        Connector conn = new Connector();
        // attention here <connector/> is unmashalled as a JAXBElement
        JAXBElement<Connector> connElement = (new org.apache.geronimo.jee.connector.ObjectFactory())
            .createConnector(conn);
        extModule.setAny(connElement);
        Resourceadapter adapter = new Resourceadapter();
        conn.getResourceadapter().add(adapter);

        OutboundResourceadapter outboundAdpater = new OutboundResourceadapter();
        ConnectionDefinition definition = new ConnectionDefinition();
        definition.setConnectionfactoryInterface("javax.sql.DataSource");
        ConnectiondefinitionInstance instance = new ConnectiondefinitionInstance();
        instance.setName(page1.getPoolName());

        String[] loginData = page2.getLoginData();
        if (!loginData[1].equals(loginData[2])) {
        Status status = new Status(IStatus.WARNING, "Login Error", 0,
        		 Messages.dbLoginError0, null);
        ErrorDialog.openError(this.getShell(), "Login Error",
        		 Messages.dbLoginError1, status);
        return false;
        }
        ConfigPropertySetting setting1 = new ConfigPropertySetting();
        setting1.setName(Messages.dbWizardPwd);
        setting1.setValue(loginData[1]);
        instance.getConfigPropertySetting().add(setting1);
        ConfigPropertySetting setting2 = new ConfigPropertySetting();
        setting2.setName(Messages.dbWizardLoginTimeout);
        setting2.setValue(page2.getLoginTimeout());
        instance.getConfigPropertySetting().add(setting2);
        ConfigPropertySetting setting3 = new ConfigPropertySetting();
        setting3.setName(Messages.dbWizardDbName);
        setting3.setValue(page1.getDBName());
        instance.getConfigPropertySetting().add(setting3);
        ConfigPropertySetting setting4 = new ConfigPropertySetting();
        setting4.setName(Messages.dbWizardCreateDb);
        setting4.setValue(page2.getCreateDatabase());
        instance.getConfigPropertySetting().add(setting4);
        ConfigPropertySetting setting5 = new ConfigPropertySetting();
        setting5.setName(Messages.dbWizardUserName);
        setting5.setValue(loginData[0]);
        instance.getConfigPropertySetting().add(setting5);

        instance.setConnectionmanager(page3.getConnectionManager());
        definition.getConnectiondefinitionInstance().add(instance);
        outboundAdpater.getConnectionDefinition().add(definition);
        adapter.setOutboundResourceadapter(outboundAdpater);

        if (section.getViewer().getInput() == section.getPlan()) {
        section.getViewer().setInput(section.getInput());
        }
        return true;
    }

    return true;
    }

    public class ExternalPathPage extends AbstractTableWizardPage {
    Text[] text = new Text[5];

    protected ExternalPathPage(String pageName) {
        super(pageName);
    }

    @Override
    public void createControl(Composite parent) {
        Composite composite = createComposite(parent);

        GridData groupGridData1;
        GridData groupGridData2;
        GridData textGridData;
        GridLayout gridLayout1;
        GridLayout gridLayout2;
        Label label;

        Group group = new Group(composite, SWT.NONE);
        gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 2;
        group.setLayout(gridLayout1);
        groupGridData1 = new GridData();
        groupGridData1.horizontalAlignment = GridData.FILL;
        groupGridData1.grabExcessHorizontalSpace = true;
        groupGridData1.horizontalSpan = 2;
        group.setLayoutData(groupGridData1);

        label = new Label(group, SWT.LEFT);
        label.setText(Messages.dbWizardConnector);

        text[0] = new Text(group, SWT.SINGLE | SWT.BORDER);
        textGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
            | GridData.VERTICAL_ALIGN_FILL);
        textGridData.grabExcessHorizontalSpace = true;
        textGridData.widthHint = 100;
        text[0].setLayoutData(textGridData);

        Group group2 = new Group(composite, SWT.NONE);
        gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 2;
        group2.setLayout(gridLayout2);
        groupGridData2 = new GridData();
        groupGridData2.horizontalAlignment = GridData.FILL;
        groupGridData2.grabExcessHorizontalSpace = true;
        groupGridData2.horizontalSpan = 2;
        groupGridData2.verticalAlignment = GridData.FILL;
        group2.setLayoutData(groupGridData2);
        group2.setText(Messages.dbWizardExternalPath);

        label = new Label(group2, SWT.LEFT);
        label.setText(Messages.dbWizardGroupId);
        text[1] = new Text(group2, SWT.SINGLE | SWT.BORDER);
        text[1].setLayoutData(textGridData);

        label = new Label(group2, SWT.LEFT);
        label.setText(Messages.dbWizardArtifactId);
        text[2] = new Text(group2, SWT.SINGLE | SWT.BORDER);
        text[2].setLayoutData(textGridData);

        label = new Label(group2, SWT.LEFT);
        label.setText(Messages.dbWizardArtifactVersion);
        text[3] = new Text(group2, SWT.SINGLE | SWT.BORDER);
        text[3].setLayoutData(textGridData);

        label = new Label(group2, SWT.LEFT);
        label.setText(Messages.dbWizardArtifactType);
        text[4] = new Text(group2, SWT.SINGLE | SWT.BORDER);
        text[4].setLayoutData(textGridData);

        // if edit a pool,the info must be filled into wizard
        if (eObject != null && ExtModule.class.isInstance(eObject)) {
        ExtModule extModule = (ExtModule) eObject;
        if (extModule.getConnector() != null) {
            text[0].setText(extModule.getConnector().getValue());
        }
        if (extModule.getExternalPath() != null) {
            text[1]
                .setText(extModule.getExternalPath().getGroupId() == null ? ""
                    : extModule.getExternalPath().getGroupId());
            text[2]
                .setText(extModule.getExternalPath()
                    .getArtifactId() == null ? "" : extModule
                    .getExternalPath().getArtifactId());
            text[3]
                .setText(extModule.getExternalPath().getVersion() == null ? ""
                    : extModule.getExternalPath().getVersion());
            text[4]
                .setText(extModule.getExternalPath().getType() == null ? ""
                    : extModule.getExternalPath().getType());
        }
        }

        setControl(composite);
    }

    @Override
    protected String getWizardPageDescription() {
    	return Messages.dbWizardSpecifyConnectInRepository;
    }

    @Override
    protected String getWizardPageTitle() {
    	return Messages.dbWizardExternalPath;
    }

    }

    public class DBPoolBasicPage extends AbstractTableWizardPage {

    Text text[] = new Text[2];
    Combo combo;
    ListViewer listViewer;
    ArrayList<Dependency> dependencies = new ArrayList<Dependency>();

    protected DBPoolBasicPage(String pageName) {
        super(pageName);
        setTitle(CommonMessages.wizardBasicPageTitle_DBPool);
        setDescription(CommonMessages.wizardBasicPageDescription_DBPool);
    }

    protected Object[] getDrivers() {
        StructuredSelection sel = (StructuredSelection) listViewer
            .getSelection();
        return sel.toArray();
    }

    protected String getPoolName() {
        return text[0].getText();
    }

    protected String getDBName() {
        return text[1].getText();
    }

    protected String getDBPooolType() {
        if (combo.getSelectionIndex() == -1) {
        return null;
        }
        return combo.getItem(combo.getSelectionIndex());
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        GridData data = new GridData();
        // data.verticalAlignment = GridData.FILL;
        data.horizontalAlignment = GridData.FILL;
        data.widthHint = 300;
        composite.setLayoutData(data);

        Group basicGroup = new Group(composite, SWT.NONE);
        basicGroup.setText(CommonMessages.basicGroup);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        // data.grabExcessVerticalSpace = true;
        data.horizontalSpan = 2;
        basicGroup.setLayout(layout);
        data.grabExcessHorizontalSpace = true;
        basicGroup.setLayoutData(data);
        Label label = new Label(basicGroup, SWT.LEFT);
        label.setText(CommonMessages.poolName);
        GridData labelData = new GridData();
        labelData.horizontalAlignment = GridData.FILL;
        label.setLayoutData(labelData);
        text[0] = new Text(basicGroup, SWT.BORDER);
        GridData textData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
            | GridData.VERTICAL_ALIGN_FILL);
        textData.grabExcessHorizontalSpace = true;
        textData.widthHint = 100;
        text[0].setLayoutData(textData);
        label = new Label(basicGroup, SWT.LEFT);
        label.setText(CommonMessages.dbType);
        label.setLayoutData(labelData);
        combo = new Combo(basicGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setLayoutData(textData);
        combo.add("Derby Embedded");
        combo.add("Derby Embedded XA");
        combo.select(1);
        label = new Label(basicGroup, SWT.LEFT);
        label.setText(CommonMessages.dbName);
        label.setLayoutData(labelData);
        text[1] = new Text(basicGroup, SWT.BORDER);
        text[1].setLayoutData(textData);

        Group driverGroup = new Group(composite, SWT.NONE);
        driverGroup.setText(CommonMessages.driverGroup);
        driverGroup.setLayout(layout);
        driverGroup.setLayoutData(data);
        List list = new List(driverGroup, SWT.BORDER | SWT.MULTI
            | SWT.V_SCROLL);
        GridData listData = new GridData(SWT.FILL, SWT.FILL, true, true);
        list.setLayoutData(listData);
        listViewer = new ListViewer(list);
        listViewer.setContentProvider(new IStructuredContentProvider() {
        public Object[] getElements(Object element) {
            return ((java.util.List<?>) element).toArray();
        }

        public void dispose() {
        }

        public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        }
        });
        listViewer.setInput(getInput());
        MenuManager popupMenu = new MenuManager();
        Action helpAction = new Action(Messages.dbAddNewDriver) {
        public void run() {
            DriverDialog dialog = new DriverDialog(Display.getCurrent()
                .getActiveShell());
            dialog.open();
            if (dialog.getReturnCode() == 0) {
            if (dialog.getDependency() != null) {
                dependencies.add(dialog.getDependency());
                listViewer.refresh();
            }
            }
        }
        };
        popupMenu.add(helpAction);
        Menu menu = popupMenu.createContextMenu(list);
        list.setMenu(menu);

        // if edit a pool,the info must be filled into wizard
        if (eObject != null && ExtModule.class.isInstance(eObject)) {
        ExtModule extModule = (ExtModule) eObject;

        try {
            ConnectiondefinitionInstance conndefInstance = ((JAXBElement<Connector>) extModule
                .getAny()).getValue().getResourceadapter().get(0)
                .getOutboundResourceadapter()
                .getConnectionDefinition().get(0)
                .getConnectiondefinitionInstance().get(0);
            text[0].setText(conndefInstance.getName());
            java.util.List<ConfigPropertySetting> configPropertySettingList = conndefInstance
                .getConfigPropertySetting();
            for (ConfigPropertySetting cps : configPropertySettingList) {
            if (cps.getName().equals("DatabaseName")) {
                text[1].setText(cps.getValue());
            }
            }

            // there need some improvement
            list.setSelection(new int[] { 0 });
            listViewer.refresh();

        } catch (Exception e) {
            e.printStackTrace();
        }
        }

        setControl(composite);

    }

    public Object getInput() {
        // to be obtained dynamically
        Dependency dependency = new Dependency();
        dependency.setArtifactId("system-database");
        dependency.setGroupId("org.apache.geronimo.configs");
        dependency.setVersion("");
        dependency.setType("car");
        dependencies.add(dependency);
        return dependencies;
    }

    @Override
    protected String getWizardPageDescription() {
        return "";
    }

    @Override
    protected String getWizardPageTitle() {
        return "";
    }

    }

    public class DBPoolAdvancedPage extends AbstractTableWizardPage {

    Button createDatabase;
    Text text[] = new Text[4];

    protected String[] getLoginData() {
        return new String[] { text[1].getText(), text[2].getText(),
            text[3].getText() };
    }

    protected String getCreateDatabase() {
        return createDatabase.getSelection() ? "true" : "false";
    }

    protected String getLoginTimeout() {
        return text[0].getText();
    }

    protected DBPoolAdvancedPage(String pageName) {
        super(pageName);
        setTitle(CommonMessages.wizardAdvancedPageTitle__DBPool);
        setDescription(CommonMessages.wizardAdvancedPageDescription__DBPool);
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        GridData data = new GridData();
        data.verticalAlignment = GridData.FILL;
        data.horizontalAlignment = GridData.FILL;
        data.widthHint = 300;
        composite.setLayoutData(data);

        Group advancedGroup = createGroup(composite,
            CommonMessages.advancedGroup, 2);
        createDatabase = new Button(advancedGroup, SWT.CHECK);
        createDatabase.setText(CommonMessages.createDatabase);
        createDatabase.setLayoutData(new GridData(GridData.FILL,
            GridData.FILL, true, false, 4, 1));
        createDatabase.setSelection(true);
        createLabel(advancedGroup, CommonMessages.loginTimeout, 2);
        text[0] = createText(advancedGroup, 2);

        Group userGroup = createGroup(composite, CommonMessages.loginGroup,
            2);
        createLabel(userGroup, CommonMessages.username, 2);
        text[1] = createText(userGroup, 2);
        text[1].setText("system");
        createLabel(userGroup, CommonMessages.password, 2);
        text[2] = createText(userGroup, 2);
        text[2].setEchoChar('*');
        text[2].setText("manager");
        createLabel(userGroup, CommonMessages.confirmPassword, 2);
        text[3] = createText(userGroup, 2);
        text[3].setEchoChar('*');
        text[3].setText("manager");

        // if edit a pool,the info must be filled into wizard
        if (eObject != null && ExtModule.class.isInstance(eObject)) {
        ExtModule extModule = (ExtModule) eObject;
        try {
            ConnectiondefinitionInstance conndefInstance = ((JAXBElement<Connector>) extModule
                .getAny()).getValue().getResourceadapter().get(0)
                .getOutboundResourceadapter()
                .getConnectionDefinition().get(0)
                .getConnectiondefinitionInstance().get(0);
            java.util.List<ConfigPropertySetting> configPropertySettingList = conndefInstance
                .getConfigPropertySetting();
            for (ConfigPropertySetting cps : configPropertySettingList) {
            if (cps.getName().equals(Messages.dbWizardCreateDb)) {
                if (cps.getValue().equals("true"))
                createDatabase.setSelection(true);
                else
                createDatabase.setSelection(false);
            } else if (cps.getName().equals(Messages.dbWizardLoginTimeout)) {
                text[0].setText(cps.getValue());
            } else if (cps.getName().equals(Messages.dbWizardUserName)) {
                text[1].setText(cps.getValue());
            } else if (cps.getName().equals(Messages.dbWizardPwd)) {
                text[2].setEchoChar('*');
                text[2].setText(cps.getValue());
                text[3].setEchoChar('*');
                text[3].setText(cps.getValue());
            }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        }

        setControl(composite);

    }

    protected Group createGroup(Composite composite, String value, int span) {
        Group group = new Group(composite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 4;
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.horizontalSpan = span;
        group.setLayout(layout);
        group.setLayoutData(data);
        group.setText(value);
        return group;
    }

    protected Label createLabel(Composite composite, String value, int span) {
        Label label = new Label(composite, SWT.NONE);
        label.setText(value);
        GridData labelData = new GridData();
        labelData.horizontalAlignment = GridData.FILL;
        labelData.horizontalSpan = span;
        label.setLayoutData(labelData);
        return label;
    }

    protected Text createText(Composite composite, int span) {
        Text text = new Text(composite, SWT.BORDER);
        GridData textData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
            | GridData.VERTICAL_ALIGN_FILL);
        textData.grabExcessHorizontalSpace = true;
        textData.widthHint = 100;
        textData.horizontalSpan = span;
        text.setLayoutData(textData);
        return text;
    }

    @Override
    protected String getWizardPageDescription() {
        return "";
    }

    @Override
    protected String getWizardPageTitle() {
        return "";
    }

    }

    public class ConnectionPoolPage extends AbstractTableWizardPage {

    Combo combo;
    Text text[] = new Text[4];

    protected ConnectionPoolPage(String pageName) {
        super(pageName);
        setTitle(CommonMessages.wizardConnectionPageTitle__DBPool);
        setDescription(CommonMessages.wizardConnectionPageDescription__DBPool);
    }

    protected Connectionmanager getConnectionManager() {
        Connectionmanager manager = new Connectionmanager();

        // add the pool's transaction type into plan
        int poolTypeIndex = combo.getSelectionIndex();
        // pool type is XA
        if (poolTypeIndex == 0) {
        Xatransaction xaTransaction = new Xatransaction();
        xaTransaction
            .setTransactionCaching(new org.apache.geronimo.jee.connector.Empty());
        manager.setXaTransaction(xaTransaction);
        }
        // pool type is LOCAL
        else if (poolTypeIndex == 1) {
        manager
            .setLocalTransaction(new org.apache.geronimo.jee.connector.Empty());
        }
        // pool type is NONE
        else if (poolTypeIndex == 2) {
        manager
            .setNoTransaction(new org.apache.geronimo.jee.connector.Empty());
        }

        Singlepool pool = new Singlepool();
        pool.setMinSize(text[0].getText().trim().length() == 0 ? 0
            : new Integer(text[0].getText()));
        pool.setMaxSize(text[1].getText().trim().length() == 0 ? 10
            : new Integer(text[1].getText()));
        pool.setBlockingTimeoutMilliseconds(text[2].getText().trim()
            .length() == 0 ? null : new Integer(text[2].getText()));
        pool
            .setIdleTimeoutMinutes(text[3].getText().trim().length() == 0 ? null
                : new Integer(text[3].getText()));
        pool.setMatchOne(new org.apache.geronimo.jee.connector.Empty());
        manager.setSinglePool(pool);
        return manager;
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        GridData data = new GridData();
        data.verticalAlignment = GridData.FILL;
        data.horizontalAlignment = GridData.FILL;
        data.widthHint = 300;
        composite.setLayoutData(data);

        Group connectionGroup = createGroup(composite,
            CommonMessages.wizardConnectionPageTitle__DBPool, 2);
        createLabel(connectionGroup, CommonMessages.transactionType, 2);
        combo = new Combo(connectionGroup, SWT.NONE | SWT.READ_ONLY);
        combo.add("XA");
        combo.add("LOCAL");
        combo.add("NONE");
        combo.select(0);
        GridData textData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
            | GridData.VERTICAL_ALIGN_FILL);
        textData.grabExcessHorizontalSpace = true;
        textData.horizontalSpan = 2;
        textData.widthHint = 100;
        combo.setLayoutData(textData);
        createLabel(connectionGroup, CommonMessages.minPoolSize, 2);
        text[0] = createText(connectionGroup, 2);
        text[0].setText("0");
        createLabel(connectionGroup, CommonMessages.maxPoolSize, 2);
        text[1] = createText(connectionGroup, 2);
        text[1].setText("10");
        createLabel(connectionGroup, CommonMessages.blockingTimeout, 2);
        text[2] = createText(connectionGroup, 2);
        createLabel(connectionGroup, CommonMessages.idleTimeout, 2);
        text[3] = createText(connectionGroup, 2);

        // if edit a pool,the info must be filled into wizard
        if (eObject != null && ExtModule.class.isInstance(eObject)) {
        ExtModule extModule = (ExtModule) eObject;
        try {
            ConnectiondefinitionInstance conndefInstance = ((JAXBElement<Connector>) extModule
                .getAny()).getValue().getResourceadapter().get(0)
                .getOutboundResourceadapter()
                .getConnectionDefinition().get(0)
                .getConnectiondefinitionInstance().get(0);

            Connectionmanager connManager = conndefInstance
                .getConnectionmanager();
            if (connManager.getXaTransaction() != null) {
            combo.select(0);
            } else if (connManager.getLocalTransaction() != null) {
            combo.select(1);
            } else if (connManager.getNoTransaction() != null) {
            combo.select(2);
            }
            Singlepool singlepool = connManager.getSinglePool();
            if (singlepool != null) {
            text[0].setText(singlepool.getMaxSize() == null ? ""
                : singlepool.getMaxSize().toString());
            text[1].setText(singlepool.getMinSize() == null ? ""
                : singlepool.getMinSize().toString());
            text[2].setText(singlepool
                .getBlockingTimeoutMilliseconds() == null ? ""
                : singlepool.getBlockingTimeoutMilliseconds()
                    .toString());
            text[3]
                .setText(singlepool.getIdleTimeoutMinutes() == null ? ""
                    : singlepool.getIdleTimeoutMinutes()
                        .toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        }

        setControl(composite);

    }

    protected Group createGroup(Composite composite, String value, int span) {
        Group group = new Group(composite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 4;
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.horizontalSpan = span;
        group.setLayout(layout);
        group.setLayoutData(data);
        group.setText(value);
        return group;
    }

    protected Label createLabel(Composite composite, String value, int span) {
        Label label = new Label(composite, SWT.NONE);
        label.setText(value);
        GridData labelData = new GridData();
        labelData.horizontalAlignment = GridData.FILL;
        labelData.horizontalSpan = span;
        label.setLayoutData(labelData);
        return label;
    }

    protected Text createText(Composite composite, int span) {
        Text text = new Text(composite, SWT.BORDER);
        GridData textData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
            | GridData.VERTICAL_ALIGN_FILL);
        textData.grabExcessHorizontalSpace = true;
        textData.widthHint = 100;
        textData.horizontalSpan = span;
        text.setLayoutData(textData);
        return text;
    }

    @Override
    protected String getWizardPageDescription() {
        return "";
    }

    @Override
    protected String getWizardPageTitle() {
        return "";
    }

    }

}

class DriverDialog extends Dialog {

    String data[] = new String[4];
    Text text[] = new Text[4];

    public DriverDialog(Shell parent) {
    super(parent);
    }

    public Dependency getDependency() {
    if (data[0].trim().length() == 0 || data[1].trim().length() == 0
        || data[2].trim().length() == 0 || data[3].trim().length() == 0) {
        return null;
    }
    Dependency dependency = new Dependency();
    dependency.setGroupId(data[0]);
    dependency.setArtifactId(data[1]);
    dependency.setVersion(data[2]);
    dependency.setType(data[3]);
    return dependency;
    }

    protected Control createContents(Composite parent) {
    Composite composite = new Composite(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    composite.setLayout(layout);
    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    data.widthHint = 300;
    composite.setLayoutData(data);

    createLabel(composite, CommonMessages.groupId, 2);
    text[0] = createText(composite, 2);
    createLabel(composite, CommonMessages.artifactId, 2);
    text[1] = createText(composite, 2);
    createLabel(composite, CommonMessages.version, 2);
    text[2] = createText(composite, 2);
    createLabel(composite, CommonMessages.artifactType, 2);
    text[3] = createText(composite, 2);

    createButtonBar(parent);

    return composite;
    }

    protected void okPressed() {
    for (int i = 0; i < text.length; i++) {
        data[i] = text[i].getText();
    }
    super.okPressed();
    }

    protected Label createLabel(Composite composite, String value, int span) {
    Label label = new Label(composite, SWT.NONE);
    label.setText(value);
    GridData labelData = new GridData();
    labelData.horizontalAlignment = GridData.FILL;
    labelData.horizontalSpan = span;
    label.setLayoutData(labelData);
    return label;
    }

    protected Text createText(Composite composite, int span) {
    Text text = new Text(composite, SWT.BORDER);
    GridData textData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
        | GridData.VERTICAL_ALIGN_FILL);
    textData.grabExcessHorizontalSpace = true;
    textData.widthHint = 100;
    textData.horizontalSpan = span;
    text.setLayoutData(textData);
    return text;
    }

}


