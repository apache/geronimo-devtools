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
package org.apache.geronimo.st.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
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
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.util.SocketUtil;

/**
 * @version $Rev$ $Date$
 */
public class ModuleArtifactMapper {

	private static ModuleArtifactMapper instance = new ModuleArtifactMapper();

	private static final String FILE_NAME = "servermodule.info";

	ServerEntries serverEntries = null;

	private ModuleArtifactMapper() {
		if (serverEntries == null)
			serverEntries = new ServerEntries();
		load();
	}

	public static ModuleArtifactMapper getInstance() {
		return instance;
	}

	public void addEntry(IServer server, IProject project, String configId) {

		if (!SocketUtil.isLocalhost(server.getHost()))
			return;

		File runtimeLoc = server.getRuntime().getLocation().toFile();
		Map artifactEntries = (Map) serverEntries.get(runtimeLoc);
		if (artifactEntries == null) {
			artifactEntries = new HashMap();
			serverEntries.put(runtimeLoc, artifactEntries);
		}

		artifactEntries.put(project.getName(), configId);
	}

	public void removeEntry(IServer server, IModule module) {

		if (!SocketUtil.isLocalhost(server.getHost()))
			return;

		File runtimeLoc = server.getRuntime().getLocation().toFile();
		Map artifactEntries = (Map) serverEntries.get(runtimeLoc);
		if (artifactEntries != null) {
			artifactEntries.remove(module.getName());
		}
	}

	public String resolve(IServer server, IModule module) {
		Map artifactEntries = (Map) serverEntries.get(server.getRuntime().getLocation().toFile());
		if (artifactEntries != null && module != null) {
		    if(module.getProject() != null) {
		        return (String) artifactEntries.get(module.getProject().getName());
		    } else {
		        return (String) artifactEntries.get(module.getName());
		    }
			
		}
		return null;
	}

	public void save() {
		ObjectOutput output = null;
		try {
			IPath dest = Activator.getDefault().getStateLocation().append(FILE_NAME);
			OutputStream file = new FileOutputStream(dest.toFile());
			OutputStream buffer = new BufferedOutputStream(file);
			output = new ObjectOutputStream(buffer);
			String xml = serverEntries.toXML();
			output.writeObject(xml);
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
				String xml = (String) input.readObject();
				serverEntries.loadXML(xml);
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

	// This Inner class is the result of removing XStream.  ModuleArtifactMapper
	// was the only class using it so it seemed silly to have two extra jar
	// files (xpp3.jar and xstream.jar) just for one class.
	// this class is a HashMap
	// keys are the files
	// entries are the Maps of artifact entries
	// this is all saved/loaded to .plugins/org.apache.geronimo.st.core/servermodule.info
    protected class ServerEntries extends HashMap {
        static final long serialVersionUID = 0;

        protected void loadXML (String xml) {
            if (xml == null || xml.length() == 0)
                return;

            String projectName, configId;
            int fileEndPos, nomapStartPos, mapStartPos, mapEndPos, stringStartPos, stringEndPos;
            int fileStartPos = xml.indexOf("<file>", 0);
            Map artifactEntries;
            while (fileStartPos > -1) {
                fileEndPos = xml.indexOf("</file>", fileStartPos);
                File runtimeLoc = new File(xml.substring(fileStartPos + 6, fileEndPos));

                nomapStartPos = xml.indexOf("<map/>", fileEndPos);
                mapStartPos = xml.indexOf("<map>", fileEndPos);
                artifactEntries = new HashMap();
                // have projects on the server
                if ((nomapStartPos == -1) || (nomapStartPos > mapStartPos)) {
                    mapEndPos = xml.indexOf("</map>", mapStartPos);
                    stringStartPos = xml.indexOf("<string>", mapStartPos);
                    while ((stringStartPos > -1) && (stringStartPos < mapEndPos)) {
                        stringEndPos = xml.indexOf("</string>", stringStartPos);
                        projectName = xml.substring(stringStartPos + 8, stringEndPos);
                        stringStartPos = xml.indexOf("<string>", stringEndPos);
                        stringEndPos = xml.indexOf("</string>", stringStartPos);
                        configId = xml.substring(stringStartPos + 8, stringEndPos);
                        artifactEntries.put(projectName, configId);
                        stringStartPos = xml.indexOf("<string>", stringEndPos);
                    }
                }
                // if no projects on the server, it is ok to put an empty HashMap
                this.put (runtimeLoc, artifactEntries);

                fileStartPos = xml.indexOf("<file>", fileEndPos);
            }
        }

        protected String toXML () {
            String xmlString = "";
            if (!isEmpty()) {
                xmlString = "<map>\n  <entry>\n";

                Object[] serverKeySet = keySet().toArray();
                for (int i = 0; i < serverKeySet.length; i++) {
                    xmlString += "    <file>" + serverKeySet[i] + "</file>\n";
                    Map projectMap = (Map)get(serverKeySet[i]);
                    if (projectMap == null || projectMap.size() == 0) {
                        xmlString += "    <map/>\n";
                    }
                    else {
                        xmlString += "    <map>\n";
                        Object[] projectKeySet = projectMap.keySet().toArray();
                        for (int j = 0; j < projectKeySet.length; j++)
                        {
                             xmlString += "      <entry>\n";
                             xmlString += "        <string>" + (String)projectKeySet[j] + "</string>\n";
                             xmlString += "        <string>" + (String)projectMap.get(projectKeySet[j]) + "</string>\n";
                             xmlString += "      </entry>\n";
                        }
                        xmlString += "    </map>\n";
                    }
                }
                xmlString += "  </entry>\n</map>";
            }
            return xmlString;
        }
    }
}
