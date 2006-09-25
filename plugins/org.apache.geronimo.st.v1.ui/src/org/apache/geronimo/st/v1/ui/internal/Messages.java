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

	static {
		NLS.initializeMessages(Activator.PLUGIN_ID + ".internal.Messages", Messages.class);
	}
}