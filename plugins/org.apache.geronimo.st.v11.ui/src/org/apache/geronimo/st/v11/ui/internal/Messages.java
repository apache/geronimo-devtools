package org.apache.geronimo.st.v11.ui.internal;

import org.apache.geronimo.st.v11.ui.Activator;
import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	
	static {
		NLS.initializeMessages(Activator.PLUGIN_ID + ".internal.Messages", Messages.class);
	}
	
	public static String editorSectionTestEnvTitle;
	public static String editorSectionTestEnvDescription;
	public static String editorSectionRunFromProject;
	public static String editorSectionEnableInPlace;
	public static String editorSectionSetPersistant;

}
