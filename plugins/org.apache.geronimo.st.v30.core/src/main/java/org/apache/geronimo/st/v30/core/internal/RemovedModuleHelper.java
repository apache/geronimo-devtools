/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.st.v30.core.internal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.geronimo.kernel.util.IOUtils;
import org.apache.geronimo.st.v30.core.Activator;
import org.apache.geronimo.st.v30.core.DeploymentUtils;
import org.apache.geronimo.st.v30.core.GeronimoServerBehaviourDelegate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.server.core.IModule;

/**
 * Using the class to help the Server delegate
 * 
 * @version $Rev$ $Date: 2012-07-05 09:57:06 -0400 (Thu, 05 Jul 2012)
 *          $
 */
public class RemovedModuleHelper {

    private final GeronimoServerBehaviourDelegate serverDelegate;
    private final IPath dataFile;
    private final Set<String> deletedConfigs;

    public RemovedModuleHelper(GeronimoServerBehaviourDelegate serverDelegate) {
        this.serverDelegate = serverDelegate;
        this.dataFile = DeploymentUtils.getServerDirectory(serverDelegate.getServer()).append("deleted-modules.dat");
        this.deletedConfigs = loadDataFile();
    }

    public synchronized void markRemoveModules(IModule[] modules, IProgressMonitor monitor) throws CoreException {
        // mark the module as removed
        boolean needChange = false;
        for (IModule rModule : modules) {
            String configId = DeploymentUtils.getConfigId(serverDelegate.getServer(), rModule);
            if (!deletedConfigs.contains(configId)) {
                deletedConfigs.add(configId);
                needChange = true;
            }
        }

        if (needChange) {
            saveDataFile();
        }
    }

    public synchronized void unMarkRemoveModules(IModule[] modules, IProgressMonitor monitor) throws CoreException {
        boolean needChange = false;
        for (IModule rModule : modules) {
            String configId = DeploymentUtils.getConfigId(serverDelegate.getServer(), rModule);
            if (deletedConfigs.remove(configId)) {
                needChange = true;
            }
        }

        if (needChange) {
            saveDataFile();
        }
    }

    public synchronized void clearRemoveModules() {
        deletedConfigs.clear();
        deleteDataFile();
    }
    
    public synchronized Set<String> getRemovedConfigIds() {
        return new HashSet<String>(deletedConfigs);
    }

    private void deleteDataFile() {
        Trace.tracePoint("Entry", Activator.traceCore, "ServerDelegateHelper.deleteDataFile", dataFile);
        File file = dataFile.toFile();
        boolean rs = file.delete();
        Trace.tracePoint("Exit", Activator.traceCore, "ServerDelegateHelper.deleteDataFile", rs);
    }
    
    private Set<String> loadDataFile() {
        Trace.tracePoint("Entry", Activator.traceCore, "ServerDelegateHelper.loadDataFile", dataFile);
        File file = dataFile.toFile();  
        Set<String> set = null;
        if (file.exists()) {
            ObjectInputStream oi = null;
            try {
                oi = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
                set = (Set<String>) oi.readObject();
            } catch (Exception e) {
                Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logCore);
            } finally {
                IOUtils.close(oi);
            }
        }
        if (set == null) {
            set = new HashSet<String>();
        }
        Trace.tracePoint("Exit", Activator.traceCore, "ServerDelegateHelper.loadDataFile", set);
        return set;
    }

    private void saveDataFile() {
        Trace.tracePoint("Entry", Activator.traceCore, "ServerDelegateHelper.saveDataFile", dataFile, deletedConfigs);
        File file = dataFile.toFile();
        ObjectOutputStream oo = null;
        try {
            oo = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            oo.writeObject(deletedConfigs);
        } catch (IOException e) {
            Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logCore);
        } finally {
            IOUtils.close(oo);
        }
        Trace.tracePoint("Exit", Activator.traceCore, "ServerDelegateHelper.saveDataFile");
    }

}
