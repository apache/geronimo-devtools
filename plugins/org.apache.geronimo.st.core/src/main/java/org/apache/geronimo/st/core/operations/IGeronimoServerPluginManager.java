package org.apache.geronimo.st.core.operations;

import java.util.List;

public interface IGeronimoServerPluginManager {
	public List<String> getPluginList();

	public void assembleServer(String group, String artifact, String version,
			String format, String relativeServerPath, int[] selected);

}
