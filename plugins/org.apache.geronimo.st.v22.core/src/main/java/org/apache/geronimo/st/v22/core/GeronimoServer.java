package org.apache.geronimo.st.v22.core;

import org.apache.geronimo.st.core.operations.IGeronimoServerPluginManager;
import org.apache.geronimo.st.v22.core.operations.GeronimoServerV22PluginManager;

public class GeronimoServer extends org.apache.geronimo.st.v21.core.GeronimoServer{

	@Override
	public IGeronimoServerPluginManager getServerPluginManager() {
		return new GeronimoServerV22PluginManager(super.getServer());
	}
}
