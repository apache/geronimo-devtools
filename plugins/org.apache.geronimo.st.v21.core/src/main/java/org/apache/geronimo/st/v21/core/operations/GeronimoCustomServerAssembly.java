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
package org.apache.geronimo.st.v21.core.operations;

import java.util.ArrayList;
import java.util.List;

import org.apache.geronimo.deployment.plugin.jmx.RemoteDeploymentManager;
import org.apache.geronimo.kernel.repository.Artifact;
import org.apache.geronimo.st.core.GeronimoConnectionFactory;
import org.apache.geronimo.system.plugin.model.PluginListType;
import org.apache.geronimo.system.plugin.model.PluginType;
import org.eclipse.wst.server.core.IServer;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoCustomServerAssembly {

    private IServer server;
    private RemoteDeploymentManager remoteDM;
    private PluginListType data;
    private List<String> pluginList;
    
    public GeronimoCustomServerAssembly () {
    }
    
    public boolean serverChanged (Object aServer, String serverPrefix) {
        server = (IServer)aServer;

        boolean enabled = server != null && 
                server.getServerType().getId().startsWith(serverPrefix) &&
                server.getServerState() == IServer.STATE_STARTED;

        return enabled;
    }
    
    public List<String> getPluginList () {
        String name;
        boolean added;
        try {
            GeronimoConnectionFactory gcFactory = GeronimoConnectionFactory.getInstance();
            remoteDM = (RemoteDeploymentManager)gcFactory.getDeploymentManager(server);
            data = remoteDM.createPluginListForRepositories(null);
            List<PluginType> aList = data.getPlugin();
            pluginList = new ArrayList<String>(aList.size());
            for (int i = 0; i < aList.size(); i++) {
                name = aList.get(i).getName();
                added = false;
                for (int j = 0; j < pluginList.size() && added == false; j++) {
                    if (name.compareTo(pluginList.get(j)) < 0) {
                        pluginList.add(j, name);
                        added = true;
                    }
                }
                if (added == false) {
                    pluginList.add(name);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return pluginList;
    }

    public void assembleServer (String group, String artifact, String version,
                        String format, String relativeServerPath, int[] selected) {
        PluginListType selectedPlugins = new PluginListType();
        String name;
        boolean found;
        
        for (int i = 0; i < selected.length; i++) {
            name = pluginList.get(selected[i]);
            found = false;
            for (int j = 0 ; j < data.getPlugin().size() && found == false; j++) {
                if (name.equals(data.getPlugin().get(j).getName())) {
                    selectedPlugins.getPlugin().add(data.getPlugin().get(j));
                    found = true;
                }
            }
        }
        
        try {
            remoteDM.installPluginList("repository", relativeServerPath, selectedPlugins);
            remoteDM.archive(relativeServerPath, "var/temp", new Artifact(group, artifact, (String)version, format));
        }
        catch (Exception e) {
        }
    }
}
