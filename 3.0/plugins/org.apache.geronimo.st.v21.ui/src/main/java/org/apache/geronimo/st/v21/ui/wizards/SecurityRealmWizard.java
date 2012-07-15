/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.st.v21.ui.wizards;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.application.Application;
import org.apache.geronimo.jee.deployment.Attribute;
import org.apache.geronimo.jee.deployment.Dependencies;
import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.deployment.ObjectFactory;
import org.apache.geronimo.jee.deployment.Reference;
import org.apache.geronimo.jee.deployment.XmlAttributeType;
import org.apache.geronimo.jee.loginconfig.ControlFlag;
import org.apache.geronimo.jee.loginconfig.LoginConfig;
import org.apache.geronimo.jee.loginconfig.LoginModule;
import org.apache.geronimo.jee.loginconfig.Option;
import org.apache.geronimo.jee.naming.Pattern;
import org.apache.geronimo.jee.openejb.OpenejbJar;
import org.apache.geronimo.jee.web.WebApp;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.Activator;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.st.v21.core.GeronimoServerInfoManager;
import org.apache.geronimo.st.v21.core.IGeronimoServerInfo;
import org.apache.geronimo.st.v21.ui.sections.SecurityAdvancedSection;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBObjectFactoryImpl;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SecurityRealmWizard extends AbstractTableWizard {
    protected ImageDescriptor descriptor = Activator.imageDescriptorFromPlugin(
	    "org.apache.geronimo.st.ui", "icons/bigG.gif");

    public SecurityRealmWizard(AbstractTableSection section) {
	super(section);
    }

    public String getWizardWindowTitle() {
	return CommonMessages.wizardTitle_SecurityRealm;
    }

    @Override
    public void addPages() {
	SecurityRealmBasicPage page0 = new SecurityRealmBasicPage(
		"SecurityRealmBasicPage");
	page0.setImageDescriptor(descriptor);
	addPage(page0);

	PropertiesFileRealmPage page1 = new PropertiesFileRealmPage(
		"PropertiesFileRealmPage");
	page1.setImageDescriptor(descriptor);
	addPage(page1);

	SelectSQLPage page2 = new SelectSQLPage("SelectSQLPage");
	page2.setImageDescriptor(descriptor);
	addPage(page2);

	DBConnectionPage page3 = new DBConnectionPage("DBConnectionPage");
	page3.setImageDescriptor(descriptor);
	addPage(page3);

	LDAPConnectionPage page4 = new LDAPConnectionPage("LDAPConnectionPage");
	page4.setImageDescriptor(descriptor);
	addPage(page4);

	LDAPSearchPage page5 = new LDAPSearchPage("LDAPSearchPage");
	page5.setImageDescriptor(descriptor);
	addPage(page5);

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
    	return CommonMessages.wizardSecurityRealmNew;
    }

    @Override
    protected String getEditWizardWindowTitle() {
	    return CommonMessages.wizardSecurityRealmEdit;
    }

    public boolean performFinish() {
	boolean isedit = false;
	JAXBElement<?> plan = this.section.getPlan();
	ObjectFactory objectFactory = new ObjectFactory();
	// all pages
	SecurityRealmBasicPage page0 = (SecurityRealmBasicPage) this
		.getPage("SecurityRealmBasicPage");
	PropertiesFileRealmPage page1 = (PropertiesFileRealmPage) this
		.getPage("PropertiesFileRealmPage");
	SelectSQLPage page2 = (SelectSQLPage) this.getPage("SelectSQLPage");
	DBConnectionPage page3 = (DBConnectionPage) this
		.getPage("DBConnectionPage");
	LDAPConnectionPage page4 = (LDAPConnectionPage) this
		.getPage("LDAPConnectionPage");
	LDAPSearchPage page5 = (LDAPSearchPage) this.getPage("LDAPSearchPage");

    // password not match , popup a dialog
    if (!page3.textEntries[3].getText().equals(
        page3.textEntries[4].getText())) {
        Status status = new Status(IStatus.WARNING, Messages.error, 0,
            Messages.dbLoginError0, null);
        ErrorDialog.openError(this.getShell(), Messages.error,
        		Messages.dbLoginError1, status);
        return false;
    }

	// set a gbean according to the input from wizard
	Gbean gbean = null;
	if (eObject != null) {
	    gbean = (Gbean) ((JAXBElement) eObject).getValue();
	    isedit = true;
	} else {
	    gbean = new Gbean();// create a new gbean
	    eObject = objectFactory.createGbean(gbean);
	}

	// add the data into gbean
	String realmName = page0.textEntries[0].getText() == null ? ""
		: page0.textEntries[0].getText();
	gbean.setName(realmName);// set the gbean name the same as realm name
	gbean
		.setClazz("org.apache.geronimo.security.realm.GenericSecurityRealm");// TODO
	// message

	List<JAXBElement<?>> elelist = gbean
		.getAttributeOrXmlAttributeOrReference();

	// set the attribute
	JAXBElement<Attribute> attributeElement = getAttributeElement(elelist);
	if (attributeElement == null) {
	    attributeElement = objectFactory.createGbeanAttribute(null);
	    elelist.add(attributeElement);
	}
	Attribute attribute = new Attribute();
	attribute.setName("realmName");
	attribute.setValue(realmName);
	attributeElement.setValue(attribute);

	// set the reference
	JAXBElement<Reference> referenceElement = getReferenceElement(elelist);
	if (referenceElement == null) {
	    referenceElement = objectFactory.createGbeanReference(null);
	    elelist.add(referenceElement);
	}
	Reference ref = new Reference();
	ref.setName("ServerInfo");
	ref.setCustomFoo("ServerInfo");
	referenceElement.setValue(ref);

	// set the xml-reference
	JAXBElement<XmlAttributeType> xmlrefElement = getXmlReferenceElement(elelist);
	if (xmlrefElement == null) {
	    xmlrefElement = objectFactory.createGbeanXmlReference(null);
	    elelist.add(xmlrefElement);
	}
	XmlAttributeType xmlAtttribute = new XmlAttributeType();
	xmlAtttribute.setName("LoginModuleConfiguration");
	xmlrefElement.setValue(xmlAtttribute);

	LoginConfig config = new LoginConfig();
	xmlAtttribute.setAny(config);
	LoginModule loginModule = new LoginModule();
	config.getLoginModuleRefOrLoginModule().add(loginModule);
	loginModule.setControlFlag(ControlFlag.fromValue("REQUIRED"));
	loginModule.setWrapPrincipals(false);
	loginModule.setLoginDomainName(realmName);

    String realmType = page0.combo.getText().trim();
    if (realmType.equals(Messages.propertiesFileRealm)) {
        loginModule
            .setLoginModuleClass("org.apache.geronimo.security.realm.providers.PropertiesFileLoginModule");

	    String usersfile = page1.textEntries[0].getText().trim();
	    Option usersfileopt = createOption("usersURI", usersfile);

	    String groupsfile = page1.textEntries[1].getText().trim();
	    Option groupsfileopt = createOption("groupsURI", groupsfile);

	    String algorithm = page1.textEntries[2].getText();
	    Option algorithmopt = createOption("digest", algorithm);

	    String encoding = page1.textEntries[3].getText();
	    Option encodingopt = createOption("encoding", encoding);

	    loginModule.getOption().add(usersfileopt);
	    loginModule.getOption().add(groupsfileopt);
	    if (algorithm != null)
		loginModule.getOption().add(algorithmopt);
	    if (encoding != null)
		loginModule.getOption().add(encodingopt);

    } else if (realmType.equals(Messages.sqlRealm)) {
        loginModule
            .setLoginModuleClass("org.apache.geronimo.security.realm.providers.SQLLoginModule");

	    String selectUsers = page2.textEntries[0].getText().trim();
	    Option selectUsersopt = createOption("userSelect", selectUsers);

	    String selectGroups = page2.textEntries[1].getText().trim();
	    Option selectGroupsopt = createOption("groupSelect", selectGroups);

	    String algorithm = page2.textEntries[2].getText().trim();
	    Option algorithmopt = createOption("digest", algorithm);

	    String encoding = page2.textEntries[3].getText().trim();
	    Option encodingopt = createOption("encoding", encoding);

	    if (page3.buttons[0].getSelection()) {
		String dsname = page3.dataBasePoolCombo.getText();
		Option dsnameopt = createOption("dataSourceName", dsname);
		loginModule.getOption().add(dsnameopt);
	    } else if (page3.buttons[1].getSelection()) {

		String jdbcDriverClass = page3.textEntries[0].getText().trim();
		Option jdbcDriverClassopt = createOption("jdbcDriver",
			jdbcDriverClass);

		String jdbcURL = page3.textEntries[1].getText().trim();
		Option jdbcURLopt = createOption("jdbcURL", jdbcURL);

		String userName = page3.textEntries[2].getText().trim();
		Option userNameopt = createOption("jdbcUser", userName);

		String password = page3.textEntries[3].getText().trim();
		Option passwordopt = createOption("jdbcPassword", password);

		loginModule.getOption().add(jdbcDriverClassopt);
		loginModule.getOption().add(jdbcURLopt);
		loginModule.getOption().add(userNameopt);
		loginModule.getOption().add(passwordopt);

	    }

	    loginModule.getOption().add(selectUsersopt);
	    loginModule.getOption().add(selectGroupsopt);
	    loginModule.getOption().add(algorithmopt);
	    loginModule.getOption().add(encodingopt);

    } else if (realmType.equals(Messages.ldapRealm)) {
        loginModule
            .setLoginModuleClass("org.apache.geronimo.security.realm.providers.LDAPLoginModule");
        String initialContextFactory = page4.combo[0].getText().trim();
        loginModule.getOption()
            .add(
                createOption("initialContextFactory",
                    initialContextFactory));
        String connectionURL = page4.combo[1].getText();
        loginModule.getOption().add(
            createOption("connectionURL", connectionURL));
        String connectionUsername = page4.text[0].getText();
        loginModule.getOption().add(
            createOption("connectionUsername", connectionUsername));
        String connectionPassword = page4.text[1].getText();
        loginModule.getOption().add(
            createOption("connectionPassword", connectionPassword));
        String connectionProtocol = page4.text[3].getText();
        loginModule.getOption().add(
            createOption("connectionProtocol", connectionProtocol));
        String authentication = page4.combo[2].getText();
        loginModule.getOption().add(
            createOption("authentication", authentication));

	    String userBase = page5.text[0].getText();
	    loginModule.getOption().add(createOption("userBase", userBase));
	    String userSearchMatching = page5.text[1].getText();
	    loginModule.getOption().add(
		    createOption("userSearchMatching", userSearchMatching));
	    String userSearchSubtree = Boolean.toString(page5.userSearchSubtree
		    .getSelection());
	    loginModule.getOption().add(
		    createOption("userSearchSubtree", userSearchSubtree));
	    String roleBase = page5.text[2].getText();
	    loginModule.getOption().add(createOption("roleBase", roleBase));
	    String roleName = page5.text[3].getText();
	    loginModule.getOption().add(createOption("roleName", roleName));
	    String roleSearchMatching = page5.text[4].getText();
	    loginModule.getOption().add(
		    createOption("roleSearchMatching", roleSearchMatching));
	    String userRoleName = page5.text[5].getText();
	    loginModule.getOption().add(
		    createOption("userRoleName", userRoleName));
	    String roleSearchSubtree = Boolean.toString(page5.roleSearchSubtree
		    .getSelection());
	    loginModule.getOption().add(
		    createOption("roleSearchSubtree", roleSearchSubtree));
	}

	if (isedit)
	    return true;

	// is add, need to add the gbean into plan
	Object planValue = plan.getValue();
	if (Application.class.isInstance(planValue)) {
	    Application application = (Application) planValue;

	    // set dependency
	    Environment env = application.getEnvironment();
	    if (env == null) {
		env = new Environment();
		application.setEnvironment(env);
	    }
	    setDependency(env);

	    // add the gbean into plan
	    JAXBElement<Gbean> gbeanElement = objectFactory.createGbean(gbean);
	    application.getService().add(gbeanElement);
	} else if (WebApp.class.isInstance(planValue)) {
	    WebApp webapp = (WebApp) planValue;

	    // set dependency
	    Environment env = webapp.getEnvironment();
	    if (env == null) {
		env = new Environment();
		webapp.setEnvironment(env);
	    }
	    setDependency(env);

	    // add the gbean into plan
	    JAXBElement<Gbean> gbeanElement = objectFactory.createGbean(gbean);
	    webapp.getServiceOrPersistence().add(gbeanElement);
	} else if (OpenejbJar.class.isInstance(planValue)) {
	    OpenejbJar openejbJar = (OpenejbJar) planValue;

	    // set dependency
	    Environment env = openejbJar.getEnvironment();
	    if (env == null) {
		env = new Environment();
		openejbJar.setEnvironment(env);
	    }
	    setDependency(env);

	    // add the gbean into plan
	    JAXBElement<Gbean> gbeanElement = objectFactory.createGbean(gbean);
	    openejbJar.getService().add(gbeanElement);
	}

	return true;
    }

    private void setDependency(Environment env) {
	Dependencies dependencies = env.getDependencies();
	if (dependencies == null) {
	    dependencies = new Dependencies();
	    env.setDependencies(dependencies);
	}
	Dependency securityDependency = new Dependency();
	securityDependency.setGroupId("org.apache.geronimo.framework");
	securityDependency.setArtifactId("j2ee-security");
	securityDependency.setType("car");
	if (!dependencies.getDependency().contains(securityDependency)) {
	    dependencies.getDependency().add(securityDependency);
	}
    }

    private Option createOption(String name, String value) {
	Option option = new Option();
	option.setName(name);
	option.setValue(value);
	return option;
    }

    private JAXBElement<Attribute> getAttributeElement(List<JAXBElement<?>> list) {
	for (JAXBElement<?> ele : list) {
	    if (Attribute.class.isInstance(ele.getValue())
		    && ((Attribute) ele.getValue()).getName().equals(
			    "realmName")) {
		return (JAXBElement<Attribute>) ele;
	    }
	}
	return null;
    }

    private JAXBElement<Reference> getReferenceElement(List<JAXBElement<?>> list) {
	for (JAXBElement<?> ele : list) {
	    if (Reference.class.isInstance(ele.getValue())
		    && ((Reference) ele.getValue()).getName().equals(
			    "ServerInfo")) {
		return (JAXBElement<Reference>) ele;
	    }
	}
	return null;
    }

    private JAXBElement<XmlAttributeType> getXmlReferenceElement(
	    List<JAXBElement<?>> list) {
	for (JAXBElement<?> ele : list) {
	    if (XmlAttributeType.class.isInstance(ele.getValue())
		    && ((XmlAttributeType) ele.getValue()).getName().equals(
			    "LoginModuleConfiguration")) {
		return (JAXBElement<XmlAttributeType>) ele;
	    }
	}
	return null;
    }

    public class SecurityRealmBasicPage extends WizardPage {
	private int recordedRealmType = -2;

	Text textEntries[] = new Text[1];
	Combo combo = null;

	public SecurityRealmBasicPage(String pageName) {
	    super(pageName);
	    setTitle(CommonMessages.wizardFirstPageTitle_SecurityRealm);
	    setDescription(CommonMessages.wizardFirstPageDescription_SecurityRealm);
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

        Group basicGroup = new Group(composite, SWT.NONE);
        basicGroup.setText(Messages.basicGroup);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        GridData data1 = new GridData();
        data1.horizontalAlignment = GridData.FILL;
        data1.grabExcessHorizontalSpace = true;
        data1.grabExcessVerticalSpace = true;
        data1.horizontalSpan = 2;
        basicGroup.setLayout(gridLayout);
        data1.grabExcessHorizontalSpace = true;
        basicGroup.setLayoutData(data1);
        createLabel(basicGroup, CommonMessages.realmName, 1);
        textEntries[0] = createText(basicGroup, 3);
        createLabel(basicGroup, CommonMessages.realmType, 1);
        combo = new Combo(basicGroup, SWT.NONE | SWT.READ_ONLY);
        combo.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
            true, false, 3, 1));
        combo.add(Messages.propertiesFileRealm);
        combo.add(Messages.sqlRealm);
        combo.add(Messages.ldapRealm);
        combo.select(0);

	    if (eObject != null) {
		Gbean gbean = (Gbean) ((JAXBElement) eObject).getValue();
		String realmName = getAttributeValue(gbean, "realmName");
		this.textEntries[0].setText(realmName == null ? "" : realmName);
		String loginModuleClass = getLoginModuleClass(gbean);
		if ("org.apache.geronimo.security.realm.providers.PropertiesFileLoginModule"
			.equals(loginModuleClass)) {
		    this.combo.select(0);
		} else if ("org.apache.geronimo.security.realm.providers.SQLLoginModule"
			.equals(loginModuleClass)) {
		    this.combo.select(1);
		} else if ("org.apache.geronimo.security.realm.providers.LDAPLoginModule"
			.equals(loginModuleClass)) {
		    this.combo.select(2);
		}
	    }
	    setControl(composite);
	}

	public int getRecordedRealmType() {
	    return recordedRealmType;
	}

	public void setRecordedRealmType(int recordedRealmType) {
	    this.recordedRealmType = recordedRealmType;
	}

	public int getRealmTypeSelectionIndex() {
	    if (combo != null) {
		return combo.getSelectionIndex();
	    } else {
		return -1;
	    }
	}

    @Override
    public IWizardPage getNextPage() {
        if (combo.getText().trim().equals(Messages.propertiesFileRealm)) {// properties
        // file
        // type
        return this.getWizard().getPage("PropertiesFileRealmPage");
        } else if (combo.getText().trim().equals(Messages.sqlRealm)) {// sql realm
        // type
        return this.getWizard().getPage("SelectSQLPage");
        } else if (combo.getText().trim().equals(Messages.ldapRealm)) {// ldap
        // realm
        // type
        return this.getWizard().getPage("LDAPConnectionPage");
        }
        return null;
    }
    }

    public class PropertiesFileRealmPage extends WizardPage {

	Text textEntries[] = new Text[4];

    public PropertiesFileRealmPage(String pageName) {
        super(pageName);
        setTitle(Messages.propertiesFileRealm);
        setDescription(Messages.propertiesFileRealmDesc);
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

        Group uriGroup = new Group(composite, SWT.NONE);
        uriGroup.setText(Messages.fileUri);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        uriGroup.setLayout(gridLayout);
        GridData data1 = new GridData();
        data1.horizontalAlignment = GridData.FILL;
        data1.grabExcessHorizontalSpace = true;
        data1.grabExcessVerticalSpace = true;
        data1.horizontalSpan = 2;
        data1.grabExcessHorizontalSpace = true;
        uriGroup.setLayoutData(data1);
        createLabel(uriGroup, Messages.userFileUri, 1);
        textEntries[0] = createText(uriGroup, 3);
        createLabel(uriGroup, Messages.groupFileUri, 1);
        textEntries[1] = createText(uriGroup, 3);

        Group digestGroup = new Group(composite, SWT.NONE);
        digestGroup.setText(Messages.digestConfig);
        digestGroup.setText("Digest Configuration");
        gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        digestGroup.setLayout(gridLayout);
        data1 = new GridData();
        data1.horizontalAlignment = GridData.FILL;
        data1.grabExcessHorizontalSpace = true;
        data1.grabExcessVerticalSpace = true;
        data1.horizontalSpan = 2;
        data1.grabExcessHorizontalSpace = true;
        digestGroup.setLayoutData(data1);
        createLabel(digestGroup, Messages.digestAlgorithm, 1);
        textEntries[2] = createText(digestGroup, 3);
        createLabel(digestGroup, Messages.digestEncoding, 1);
        textEntries[3] = createText(digestGroup, 3);

	    if (eObject != null) {
		Gbean gbean = (Gbean) ((JAXBElement) eObject).getValue();
		if (getOptionValue(gbean, "usersURI") != null) {
		    textEntries[0].setText(getOptionValue(gbean, "usersURI"));
		}
		if (getOptionValue(gbean, "groupsURI") != null) {
		    textEntries[1].setText(getOptionValue(gbean, "groupsURI"));
		}
		if (getOptionValue(gbean, "digest") != null) {
		    textEntries[2].setText(getOptionValue(gbean, "digest"));
		}
		if (getOptionValue(gbean, "encoding") != null) {
		    textEntries[3].setText(getOptionValue(gbean, "encoding"));
		}
	    }

	    setControl(composite);
	}

	public boolean canFlipToNextPage() {
	    // no next page for this path through the wizard
	    return false;
	}

	public IWizardPage getNextPage() {
	    return null;
	}
    }

    public class SelectSQLPage extends WizardPage {

	Text textEntries[] = new Text[4];

    public SelectSQLPage(String pageName) {
        super(pageName);
        setTitle(Messages.sqlRealm);
        setDescription(Messages.sqlRealmDesc);
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

        Group uriGroup = new Group(composite, SWT.NONE);
        uriGroup.setText(Messages.sqlQueries);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        uriGroup.setLayout(gridLayout);
        GridData data1 = new GridData();
        data1.horizontalAlignment = GridData.FILL;
        data1.grabExcessHorizontalSpace = true;
        data1.grabExcessVerticalSpace = true;
        data1.horizontalSpan = 2;
        data1.grabExcessHorizontalSpace = true;
        uriGroup.setLayoutData(data1);
        createLabel(uriGroup, Messages.userSelectSQL, 1);
        textEntries[0] = createText(uriGroup, 3);
        createLabel(uriGroup, Messages.groupSelectSQL, 1);
        textEntries[1] = createText(uriGroup, 3);

        Group digestGroup = new Group(composite, SWT.NONE);
        digestGroup.setText(Messages.digestConfig);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        digestGroup.setLayout(gridLayout);
        data1 = new GridData();
        data1.horizontalAlignment = GridData.FILL;
        data1.grabExcessHorizontalSpace = true;
        data1.grabExcessVerticalSpace = true;
        data1.horizontalSpan = 2;
        data1.grabExcessHorizontalSpace = true;
        digestGroup.setLayoutData(data1);
        createLabel(digestGroup, Messages.digestAlgorithm, 1);
        textEntries[2] = createText(digestGroup, 3);
        createLabel(digestGroup, Messages.digestEncoding, 1);
        textEntries[3] = createText(digestGroup, 3);

	    if (eObject != null) {
		Gbean gbean = (Gbean) ((JAXBElement) eObject).getValue();
		if (getOptionValue(gbean, "userSelect") != null) {
		    textEntries[0].setText(getOptionValue(gbean, "userSelect"));
		}
		if (getOptionValue(gbean, "groupSelect") != null) {
		    textEntries[1]
			    .setText(getOptionValue(gbean, "groupSelect"));
		}
		if (getOptionValue(gbean, "digest") != null) {
		    textEntries[2].setText(getOptionValue(gbean, "digest"));
		}
		if (getOptionValue(gbean, "encoding") != null) {
		    textEntries[3].setText(getOptionValue(gbean, "encoding"));
		}
	    }

	    setControl(composite);
	}

	@Override
	public IWizardPage getNextPage() {
	    return this.getWizard().getPage("DBConnectionPage");
	}

    }

    public class DBConnectionPage extends WizardPage {

	Text textEntries[] = new Text[5];
	Button buttons[] = new Button[2];
	Combo dataBasePoolCombo;

	protected DBConnectionPage(String pageName) {
	    super(pageName);
	    setTitle(CommonMessages.wizardSecondPageTitle_SecurityRealm);
	    setDescription(CommonMessages.wizardSecondPageDescription_SecurityRealm);
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

	    Group databasePoolGroup = new Group(composite, SWT.NONE);
	    databasePoolGroup.setText(CommonMessages.dataBasePool);
	    GridLayout gridLayout = new GridLayout();
	    gridLayout.numColumns = 4;
	    GridData data1 = new GridData();
	    data1.horizontalAlignment = GridData.FILL;
	    data1.grabExcessHorizontalSpace = true;
	    data1.horizontalSpan = 2;
	    databasePoolGroup.setLayout(gridLayout);
	    databasePoolGroup.setLayoutData(data1);
	    buttons[0] = new Button(databasePoolGroup, SWT.RADIO);
	    buttons[0].setSelection(true);
	    buttons[0].setText(CommonMessages.useDataBasePool);
	    buttons[0].setLayoutData(new GridData(GridData.FILL,
		    GridData.CENTER, true, false, 4, 1));
	    buttons[0].addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
		    buttons[1].setSelection(false);
		    dataBasePoolCombo.setEnabled(true);
		    for (int i = 0; i < textEntries.length; i++) {
			textEntries[i].setEnabled(false);
		    }
		}
	    });
	    createLabel(databasePoolGroup, CommonMessages.dataBasePoolName, 1);
	    dataBasePoolCombo = new Combo(databasePoolGroup, SWT.NONE);
	    dataBasePoolCombo.setLayoutData(new GridData(GridData.FILL,
		    GridData.FILL, true, false, 3, 1));
	    populateList(dataBasePoolCombo);
	    dataBasePoolCombo.select(0);

	    GridData data2 = new GridData();
	    data2.horizontalAlignment = GridData.FILL;
	    data2.grabExcessHorizontalSpace = true;
	    data2.horizontalSpan = 2;
	    Group databaseGroup = new Group(composite, SWT.NONE);
	    databaseGroup.setLayout(gridLayout);
	    databaseGroup.setLayoutData(data2);
	    buttons[1] = new Button(databaseGroup, SWT.RADIO);
	    buttons[1].setText(CommonMessages.useDataBase);
	    buttons[1].setLayoutData(new GridData(GridData.FILL, GridData.FILL,
		    true, false, 4, 1));
	    buttons[1].addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
		    buttons[0].setSelection(false);
		    dataBasePoolCombo.setEnabled(false);
		    for (int i = 0; i < textEntries.length; i++) {
			textEntries[i].setEnabled(true);
		    }
		}
	    });
	    createLabel(databaseGroup, CommonMessages.jdbcDriverClass, 1);
	    textEntries[0] = createText(databaseGroup, 3);

	    createLabel(databaseGroup, CommonMessages.jdbcURL, 1);
	    textEntries[1] = createText(databaseGroup, 3);
	    createLabel(databaseGroup, CommonMessages.jdbcUserName, 1);
	    textEntries[2] = createText(databaseGroup, 3);
	    createLabel(databaseGroup, CommonMessages.jdbcPassword, 1);
	    textEntries[3] = createText(databaseGroup, 1);
	    textEntries[3].setEchoChar('*');
	    createLabel(databaseGroup, CommonMessages.confirmPassword, 1);
	    textEntries[4] = createText(databaseGroup, 1);
	    textEntries[4].setEchoChar('*');

	    dataBasePoolCombo.setEnabled(true);
	    for (int i = 0; i < textEntries.length; i++) {
		textEntries[i].setEnabled(false);
	    }

	    if (eObject != null) {
		Gbean gbean = (Gbean) ((JAXBElement) eObject).getValue();
		if (getOptionValue(gbean, "dataSourceName") != null) {
		    this.buttons[0].setSelection(true);
		    this.dataBasePoolCombo.setEnabled(true);
		    this.dataBasePoolCombo.setText(getOptionValue(gbean,
			    "dataSourceName"));

		    this.buttons[1].setSelection(false);
		    for (int i = 0; i < textEntries.length; i++) {
			this.textEntries[i].setEnabled(false);
		    }

		} else {
		    this.buttons[1].setSelection(true);
		    this.buttons[0].setSelection(false);
		    this.dataBasePoolCombo.setEnabled(false);
		    for (int i = 0; i < textEntries.length; i++) {
			textEntries[i].setEnabled(true);
		    }

		    if (getOptionValue(gbean, "jdbcDriver") != null) {
			this.textEntries[0].setText(getOptionValue(gbean,
				"jdbcDriver"));
		    }
		    if (getOptionValue(gbean, "jdbcURL") != null) {
			this.textEntries[1].setText(getOptionValue(gbean,
				"jdbcURL"));
		    }
		    if (getOptionValue(gbean, "jdbcUser") != null) {
			this.textEntries[2].setText(getOptionValue(gbean,
				"jdbcUser"));
		    }
		    if (getOptionValue(gbean, "jdbcPassword") != null) {
			this.textEntries[3].setText(getOptionValue(gbean,
				"jdbcPassword"));
			this.textEntries[4].setText(getOptionValue(gbean,
				"jdbcPassword"));
		    }
		}

	    }

	    setControl(composite);

	}

	private void populateList(Combo combo) {
		IGeronimoServerInfo serverInfo = GeronimoServerInfoManager.getProvider(SecurityAdvancedSection.getRuntimeVersionNumber());
	    ArrayList<Pattern> dbPool = serverInfo.getJdbcConnectionPools();
	    for (int i = 0; i < dbPool.size(); i++) {
		String str = dbPool.get(i).getName();
		combo.add(str);
	    }
	}

	public boolean canFlipToNextPage() {
	    // no next page for this path through the wizard
	    return false;
	}

	public IWizardPage getNextPage() {
	    return null;
	}
    }

    public class LDAPConnectionPage extends WizardPage {
	Text[] text = new Text[4];
	Combo[] combo = new Combo[3];

    protected LDAPConnectionPage(String pageName) {
        super(pageName);
        setTitle(Messages.ldapRealm);
        setDescription(Messages.ldapRealmDesc);
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

        Group connectionGroup = new Group(composite, SWT.NULL);
        connectionGroup.setText(Messages.ldapServerConnection);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        GridData data1 = new GridData();
        data1.horizontalAlignment = GridData.FILL;
        data1.grabExcessHorizontalSpace = true;
        data1.horizontalSpan = 2;
        connectionGroup.setLayout(gridLayout);
        connectionGroup.setLayoutData(data1);

        createLabel(connectionGroup, Messages.initContextFactory, 1);
        combo[0] = new Combo(connectionGroup, SWT.NONE);
        combo[0].setLayoutData(new GridData(GridData.FILL, GridData.FILL,
            true, false, 3, 1));
        combo[0].add("com.sun.jndi.ldap.LdapCtxFactory");
        
        createLabel(connectionGroup, Messages.connectionUrl, 1);
        combo[1] = new Combo(connectionGroup, SWT.NONE);
        combo[1].setLayoutData(new GridData(GridData.FILL, GridData.FILL,
            true, false, 3, 1));
        combo[1].add("ldap://localhost:1389");
        combo[1].add("ldap://localhost:389");
        createLabel(connectionGroup, Messages.connectUsername, 1);
        text[0] = createText(connectionGroup, 3);
        text[0].setText("system");
        createLabel(connectionGroup, Messages.conncetPwd, 1);
        text[1] = createText(connectionGroup, 3);
        text[1].setEchoChar('*');
        text[1].setText("manager");
        createLabel(connectionGroup, Messages.confirmPassword, 1);
        text[2] = createText(connectionGroup, 3);
        text[2].setEchoChar('*');
        text[2].setText("manager");
        createLabel(connectionGroup, Messages.connectProtocol, 1);
        text[3] = createText(connectionGroup, 1);
        createLabel(connectionGroup, Messages.authentication, 1);
        combo[2] = new Combo(connectionGroup, SWT.NONE);
        combo[2].setLayoutData(new GridData(GridData.FILL, GridData.FILL,
            true, false, 1, 1));
        combo[2].add(Messages.none);
        combo[2].add(Messages.simple);
        combo[2].add(Messages.strong);
        combo[2].select(1);

	    if (eObject != null) {
		Gbean gbean = (Gbean) ((JAXBElement) eObject).getValue();
		if (getOptionValue(gbean, "initialContextFactory") != null) {
		    this.combo[0].setText(getOptionValue(gbean,
			    "initialContextFactory"));
		}
		if (getOptionValue(gbean, "connectionURL") != null) {
		    this.combo[1]
			    .setText(getOptionValue(gbean, "connectionURL"));
		}
		if (getOptionValue(gbean, "connectionUsername") != null) {
		    this.text[0].setText(getOptionValue(gbean,
			    "connectionUsername"));
		}
		if (getOptionValue(gbean, "connectionPassword") != null) {
		    this.text[1].setText(getOptionValue(gbean,
			    "connectionPassword"));
		    this.text[2].setText(getOptionValue(gbean,
			    "connectionPassword"));
		}
		if (getOptionValue(gbean, "connectionProtocol") != null) {
		    this.text[3].setText(getOptionValue(gbean,
			    "connectionProtocol"));
		}
		if (getOptionValue(gbean, "authentication") != null) {
		    this.combo[2].setText(getOptionValue(gbean,
			    "authentication"));
		}

	    }

	    setControl(composite);
	}

	public IWizardPage getNextPage() {
	    return this.getWizard().getPage("LDAPSearchPage");
	}

    }

    public class LDAPSearchPage extends WizardPage {
	Text[] text = new Text[6];
	Button userSearchSubtree, roleSearchSubtree;

    protected LDAPSearchPage(String pageName) {
        super(pageName);
        setTitle(Messages.ldapRealmSearchConfig);
        setDescription(Messages.editLdapRealmSearch);
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

        Group userGroup = new Group(composite, SWT.NULL);
        userGroup.setText(Messages.userSearchConfig);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        GridData data1 = new GridData();
        data1.horizontalAlignment = GridData.FILL;
        data1.grabExcessHorizontalSpace = true;
        data1.horizontalSpan = 2;
        userGroup.setLayout(gridLayout);
        userGroup.setLayoutData(data1);

        createLabel(userGroup, Messages.userBase, 1);
        text[0] = createText(userGroup, 3);
        createLabel(userGroup, Messages.userSearchMachting, 1);
        text[1] = createText(userGroup, 3);
        userSearchSubtree = new Button(userGroup, SWT.CHECK);
        userSearchSubtree.setText(Messages.userSearchSubtree);
        userSearchSubtree.setLayoutData(new GridData(GridData.FILL,
            GridData.FILL, true, false, 4, 1));
        userSearchSubtree.setSelection(true);

        Group roleGroup = new Group(composite, SWT.NULL);
        roleGroup.setText(Messages.roleSearchConfig);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        data1 = new GridData();
        data1.horizontalAlignment = GridData.FILL;
        data1.grabExcessHorizontalSpace = true;
        data1.horizontalSpan = 2;
        roleGroup.setLayout(gridLayout);
        roleGroup.setLayoutData(data1);
        createLabel(roleGroup, Messages.roleBase, 1);
        text[2] = createText(roleGroup, 3);
        createLabel(roleGroup, Messages.roleName, 1);
        text[3] = createText(roleGroup, 3);
        createLabel(roleGroup, Messages.roleUserSearchStr, 1);
        text[4] = createText(roleGroup, 3);
        createLabel(roleGroup, Messages.userRoleSearchStr, 1);
        text[5] = createText(roleGroup, 3);
        roleSearchSubtree = new Button(roleGroup, SWT.CHECK);
        roleSearchSubtree.setText(Messages.roleSearchSubtree);
        roleSearchSubtree.setLayoutData(new GridData(GridData.FILL,
            GridData.FILL, true, false, 4, 1));
        roleSearchSubtree.setSelection(true);

	    if (eObject != null) {
		Gbean gbean = (Gbean) ((JAXBElement<?>) eObject).getValue();
		if (getOptionValue(gbean, "userBase") != null) {
		    text[0].setText(getOptionValue(gbean, "userBase"));
		}
		if (getOptionValue(gbean, "userSearchMatching") != null) {
		    text[1]
			    .setText(getOptionValue(gbean, "userSearchMatching"));
		}
		if (getOptionValue(gbean, "userSearchSubtree") != null) {
		    this.userSearchSubtree.setSelection(Boolean
			    .parseBoolean(getOptionValue(gbean,
				    "userSearchSubtree")));
		}
		if (getOptionValue(gbean, "roleBase") != null) {
		    text[2].setText(getOptionValue(gbean, "roleBase"));
		}
		if (getOptionValue(gbean, "roleName") != null) {
		    text[3].setText(getOptionValue(gbean, "roleName"));
		}
		if (getOptionValue(gbean, "roleSearchMatching") != null) {
		    text[4]
			    .setText(getOptionValue(gbean, "roleSearchMatching"));
		}
		if (getOptionValue(gbean, "userRoleName") != null) {
		    text[5].setText(getOptionValue(gbean, "userRoleName"));
		}
		if (getOptionValue(gbean, "roleSearchSubtree") != null) {
		    this.userSearchSubtree.setSelection(Boolean
			    .parseBoolean(getOptionValue(gbean,
				    "roleSearchSubtree")));
		}
	    }

	    setControl(composite);
	}

	public boolean canFlipToNextPage() {
	    // no next page for this path through the wizard
	    return false;
	}

	public IWizardPage getNextPage() {
	    return null;
	}

    }

    private String getAttributeValue(Gbean gbean, String attributeName) {
	try {
	    List<JAXBElement<?>> elelist = gbean
		    .getAttributeOrXmlAttributeOrReference();
	    for (JAXBElement<?> ele : elelist) {
		if (Attribute.class.isInstance(ele.getValue())
			&& ((Attribute) ele.getValue()).getName().equals(
				attributeName)) {
		    return ((Attribute) ele.getValue()).getValue();
		}
	    }
	} catch (NullPointerException e) {
	    // ignore
	}
	return null;
    }

    private String getLoginModuleClass(Gbean gbean) {

	try {
	    List<JAXBElement<?>> elelist = gbean
		    .getAttributeOrXmlAttributeOrReference();
	    for (JAXBElement<?> ele : elelist) {
		if (XmlAttributeType.class.isInstance(ele.getValue())
			&& ((XmlAttributeType) ele.getValue()).getName()
				.equals("LoginModuleConfiguration")) {
		    LoginModule loginModule = (LoginModule) ((LoginConfig) ((XmlAttributeType) ele
			    .getValue()).getAny())
			    .getLoginModuleRefOrLoginModule().get(0);
		    return loginModule.getLoginModuleClass();
		}
	    }
	    return null;
	} catch (NullPointerException e) {
	    // ignore
	}

	return null;
    }

    private String getOptionValue(Gbean gbean, String optionName) {
	try {
	    List<JAXBElement<?>> elelist = gbean
		    .getAttributeOrXmlAttributeOrReference();
	    for (JAXBElement<?> ele : elelist) {
		if (XmlAttributeType.class.isInstance(ele.getValue())
			&& ((XmlAttributeType) ele.getValue()).getName()
				.equals("LoginModuleConfiguration")) {
		    LoginModule loginModule = (LoginModule) ((LoginConfig) ((XmlAttributeType) ele
			    .getValue()).getAny())
			    .getLoginModuleRefOrLoginModule().get(0);
		    List<Option> options = loginModule.getOption();
		    for (Option opt : options) {
			if (opt.getName().equals(optionName))
			    return opt.getValue();
		    }
		}
	    }
	    return null;
	} catch (NullPointerException e) {
	    // ignore
	}

	return null;
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
