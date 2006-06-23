package org.apache.geronimo.st.v1.ui.internal;

import org.apache.geronimo.st.v1.ui.Activator;
import org.eclipse.osgi.util.NLS;

/**
 * Translated messages.
 */
public class Messages extends NLS {

	public static String editorConfigId;
	public static String editorParentId;
	public static String editorContextRoot;
	public static String editorClassloader;
	public static String editorClassloaderServer;
	public static String editorClassloaderWebApp;
	public static String securityRealmName;

	public static String dependencyGroupLabel;
	public static String serverRepos;
	public static String uri;
	public static String mavenArtifact;

	// GeronimoServerRuntimeWizardFragment
	public static String serverWizardDescription;
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

	static {
		NLS.initializeMessages(Activator.PLUGIN_ID + ".internal.Messages", Messages.class);
	}
}