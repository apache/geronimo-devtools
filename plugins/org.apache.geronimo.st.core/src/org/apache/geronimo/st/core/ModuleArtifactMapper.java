/**
 * Copyright 2004, 2005 The Apache Software Foundation or its licensors, as applicable
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.st.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

public class ModuleArtifactMapper {

	private static ModuleArtifactMapper instance = new ModuleArtifactMapper();
	
	private static final String FILE_NAME = "servermodule.info";

	HashMap serverEntries;

	private ModuleArtifactMapper() {
		load();
		if (serverEntries == null)
			serverEntries = new HashMap();
	}

	public static ModuleArtifactMapper getInstance() {
		return instance;
	}

	public IProject resolve(IServer server, String configId) {
		Map artifactEntries = (Map) serverEntries.get(server.getId());
		if (artifactEntries != null) {
			String projectName = (String) artifactEntries.get(configId);
			if (projectName != null)
				return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		}
		return null;
	}

	public void addEntry(IServer server, IProject project, String configId) {
		Map artifactEntries = (Map) serverEntries.get(server.getId());
		if (artifactEntries == null) {
			artifactEntries = new HashMap();
			serverEntries.put(server.getId(), artifactEntries);
		}
		artifactEntries.put(configId, project.getName());
	}
	
	public IProject resolve(String serverId, String configId) {
		IServer server = ServerCore.findServer(serverId);
		return resolve(server, configId);
	}

	public void save() {
		ObjectOutput output = null;
		try {
			IPath dest = Activator.getDefault().getStateLocation().append(FILE_NAME);
			OutputStream file = new FileOutputStream(dest.toFile());
			OutputStream buffer = new BufferedOutputStream(file);
			output = new ObjectOutputStream(buffer);
			output.writeObject(serverEntries);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null)
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void load() {
		ObjectInput input = null;
		try {
			IPath dest = Activator.getDefault().getStateLocation().append(FILE_NAME);
			if (dest.toFile().exists()) {
				InputStream file = new FileInputStream(dest.toFile());
				InputStream buffer = new BufferedInputStream(file);
				input = new ObjectInputStream(buffer);
				serverEntries = (HashMap) input.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
