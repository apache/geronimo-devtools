package org.apache.geronimo.st.ui.internal;

import org.apache.geronimo.st.ui.Activator;
import org.eclipse.osgi.util.NLS;

/**
 * Translated messages.
 */
public class Messages extends NLS {
	
	public static String editorTabSource;
	
	public static String editorSectionSecurityTitle;
	public static String editorSectionSecurityDescription;
	public static String editorSectionPortsTitle;
	public static String editorSectionPortsDescription;
	public static String editorSectionLogLevelTitle;
	public static String editorSectionLogLevelDescription;
	public static String editorSectionVMArgsTitle;
	public static String editorSectionVMArgsDescription;
	public static String info;
	public static String debug;
	public static String httpPort;
	public static String rmiPort;
	public static String username;
	public static String password;
	public static String console;
	public static String consoleTooltip;
	
	public static String newServerWizardTitle;
	public static String newServerWizardDescription;
	
	public static String installedJRE;
	public static String installedJREs;
	public static String runtimeDefaultJRE;
	public static String runtimeWizardTitle;	
	public static String runtimeWizardDescription;
	public static String browse;
	public static String installDir;
	public static String installDirInfo;
	public static String noSuchDir;
	public static String noImageFound;
	public static String cannotInstallAtLocation;
	public static String downloadOptions;
	public static String chooseWebContainer;
	public static String gWithTomcat;
	public static String gWithJetty;
	public static String install;
	public static String jvmWarning;
	public static String installTitle;
	public static String installMessage;
	public static String tooltipLoc;
	public static String tooltipInstall;
	public static String tooltipJetty;
	public static String tooltipTomcat;
	
	public static String hostName;
	public static String adminId;
	public static String adminPassword;
	public static String specifyPorts;
	public static String portName;
	public static String portValue;

	static {
		NLS.initializeMessages(Activator.PLUGIN_ID + ".internal.Messages", Messages.class);
	}
	
	public static String appGeneralPageTitle;
	public static String connectorOverViewPageTitle;
	public static String deploymentPageTitle;
	public static String namingFormPageTitle;
	public static String securityPageTitle;
	public static String webGeneralPageTitle;
	public static String editorTabGeneral;
	public static String editorTabNaming;
	public static String editorTabSecurity;
	public static String editorTabDeployment;
	public static String errorCouldNotOpenFile;
	public static String editorTitle;
	public static String editorSectionGeneralTitle;
	public static String editorSectionGeneralDescription;
	public static String editorContextRoot;
	//
	public static String editorSectionSecurityRolesTitle;
	public static String editorSectionSecurityRolesDescription;
	public static String name;
	public static String description;
	//
	public static String editorSectionDependenciesTitle;
	public static String editorSectionDependenciesDescription;
	//
	public static String editorSectionImportTitle;
	public static String editorSectionImportDescription;
	//
	public static String editorSectionGBeanTitle;
	public static String editorSectionGBeanDescription;
	public static String className;
	public static String GbeanName;
	//
	public static String editorResourceRefDescription;
	public static String editorResourceRefTitle;
	public static String editorResRefTargetNameTitle;
	public static String editorResRefLinkTitle;
	public static String editorResRefNameTitle;
	//
	public static String editorGBeanRefTitle;
	public static String editorGBeanRefDescription;
	public static String editorGBeanRefName;
	public static String editorGBeanRefType;
	public static String editorGBeanRefTargetName;
	public static String editorGBeanRefProxyType;
	//
	public static String editorServiceRefDescription;
	public static String editorServiceRefTitle;
	public static String editorServiceRefName;
	//
	public static String editorResourceEnvRefDescription;
	public static String editorResourceEnvRefTitle;
	public static String editorResEnvRefMsgDestTitle;
	public static String editorResEnvRefNameTitle;
	//
	public static String editorEjbLocalRefDescription;
	public static String editorEjbLocalRefTitle;
	public static String editorEjbRefTargetName;
	public static String editorEjbRefEjbLink;
	//
	public static String editorEjbRefDescription;
	public static String editorEjbRefTitle;
	// Buttons
	public static String add;
	public static String remove;
	public static String edit;
	// Wizard/Wizard Pages
	public static String wizardNewTitle_ResRef;
	public static String wizardEditTitle_ResRef;
	public static String wizardPageTitle_ResRef;
	public static String wizardPageDescription_ResRef;
	//
	public static String wizardNewTitle_GBeanRef;
	public static String wizardEditTitle_GBeanRef;
	public static String wizardPageTitle_GBeanRef;
	public static String wizardPageDescription_GBeanRef;
	//
	public static String wizardNewTitle_ServiceRef;
	public static String wizardEditTitle_ServiceRef;
	public static String wizardPageTitle_ServiceRef;
	public static String wizardPageDescription_ServiceRef;
	//
	public static String wizardNewTitle_ResEnvRef;
	public static String wizardEditTitle_ResEnvRef;
	public static String wizardPageTitle_ResEnvRef;
	public static String wizardPageDescription_ResEnvRef;
	//
	public static String wizardNewTitle_EjbRef;
	public static String wizardEditTitle_EjbRef;
	public static String wizardPageTitle_EjbRef;
	public static String wizardPageDescription_EjbRef;
	//
	public static String wizardNewTitle_EjbLocalRef;
	public static String wizardEditTitle_EjbLocalRef;
	public static String wizardPageTitle_EjbLocalRef;
	public static String wizardPageDescription_EjbLocalRef;
	//
	public static String wizardNewTitle_Dependency;
	public static String wizardEditTitle_Dependency;
	public static String wizardPageTitle_Dependency;
	public static String wizardPageDescription_Dependency;
	public static String dependencyGroupLabel;

	public static String groupId;
	public static String artifactId;
	public static String version;
	public static String type;
	//
	public static String wizardNewTitle_Import;
	public static String wizardEditTitle_Import;
	public static String wizardPageTitle_Import;
	public static String wizardPageDescription_Import;
	//
	public static String wizardNewTitle_GBean;
	public static String wizardEditTitle_GBean;
	public static String wizardPageTitle_GBean;
	public static String wizardPageDescription_GBean;
	//
	public static String wizardNewTitle_SecurityRole;
	public static String wizardEditTitle_SecurityRole;
	public static String wizardPageTitle_SecurityRole;
	public static String wizardPageDescription_SecurityRole;
	//
	public static String doasCurrentCaller;
	public static String useContextHandler;
	public static String defaultRole;
	
	public static String editorCorrect;
	public static String editorDefault;
	public static String errorOpenDialog;
}