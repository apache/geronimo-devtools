package org.apache.geronimo.st.v11.ui.internal;

import org.apache.geronimo.st.v11.ui.Activator;
import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	
	static {
		NLS.initializeMessages(Activator.PLUGIN_ID + ".internal.Messages", Messages.class);
	}
	
	public static String artifactId;
	public static String editorSectionTestEnvTitle;
	public static String editorSectionTestEnvDescription;
	public static String editorSectionRunFromWorkspace;
	public static String editorSectionEnableInPlace;
	public static String editorSectionSetPersistent;
	public static String gBeanLink;
	public static String groupId;
	public static String moduleId;
	public static String name;
	public static String seeRestrictions;
	
	public static String useGBeanLink;
	public static String useGBeanPattern;
	public static String artifactType;
	public static String inverseClassloading;
	public static String supressDefaultEnv;
	
	public static String addSharedLib;
	public static String version;
	public static String webContainerSection;
	public static String webContainerSectionDescription;

}
