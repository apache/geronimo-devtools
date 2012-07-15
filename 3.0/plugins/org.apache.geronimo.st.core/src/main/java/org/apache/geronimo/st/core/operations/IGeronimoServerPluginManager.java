/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.geronimo.st.core.operations;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public interface IGeronimoServerPluginManager {
    public List<String> getPluginList();

    public void assembleServer(String group, String artifact, String version, String format, String relativeServerPath,
            int[] selected);

    public void addGeronimoDependencies(Object data, List deps, boolean includeVersion);

    public void exportCAR(String localRepoDir, String configId) throws Exception;

    public List<String> getConfigurationList();

    public Object getPluginMetadata(String configId);

    public ArrayList<String> installPlugins(String localRepoDir, List pluginList);

    public Object loadPluginList(InputStream in);

    public Object readPluginList(String localRepoDir);

    public void savePluginXML(String configId, Object metadata);

    public Object toArtifact(Object id);

    public Object toArtifactType(Object id);

    public Object toArtifactType(String configId);

    public Object toDependencyType(Object dep, boolean includeVersion);

    public Object toDependencyType(String configId);

    public void updatePluginList(String localRepoDir, Object metadata) throws Exception;

    public boolean validatePlugin(Object plugin);

}
