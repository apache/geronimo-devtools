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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.geronimo.st.v30.core.Activator;
import org.apache.geronimo.st.v30.core.DeploymentUtils;
import org.apache.geronimo.st.v30.core.GeronimoServerDelegate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.wst.server.core.IModule;

/**
 * Using the class to help the Server delegate
 *
 * @version $Rev$ $Date$
 */
public class ServerDelegateHelper {
    public final GeronimoServerDelegate serverDelegate;
    
    public ServerDelegateHelper(GeronimoServerDelegate serverDelegate) {
        this.serverDelegate = serverDelegate;
    }
    
    public void markRemoveModules(IModule[] modules, IProgressMonitor monitor) throws CoreException {
        String jvmArgs = serverDelegate.getVMArgs();
        String trimedJvmArgs = deleteRemovedArtifactListFromJVMArgs(jvmArgs);
        String removedModuleString = getRemovedModuleStringFromJVMArgs(jvmArgs);
        
        // mark the module as removed
        for(IModule rModule : modules) {
            String configId = DeploymentUtils.getConfigId(serverDelegate.getServer(), rModule);
            
            boolean needChange = true;
            if(! removedModuleString.equals("")) {
                String[] rArtifactIds = removedModuleString.split(",");
                
                for(String rArtifactId : rArtifactIds) {
                    if(rArtifactId.equals(configId)) {
                        needChange = false;
                        break;
                    }
                }
            } else {// it means no modules need to be removed before, so just adds it
                needChange = true;// 
            }
            if(needChange) {
                removedModuleString = removedModuleString == "" ? configId : removedModuleString + "," + configId;
                //persist the removed module to jvm args
                trimedJvmArgs = trimedJvmArgs.endsWith(" ") ? trimedJvmArgs : trimedJvmArgs + " ";
                persistJVMArgs(trimedJvmArgs + GeronimoServerDelegate.REMOVE_ARTIFACT_LIST + "=" + removedModuleString, monitor);
                Trace.trace(Trace.INFO, rModule.getName() + ":" + rModule.getModuleType() + " is marked as REMOVED", Activator.traceCore);
            }
        }
    }

    public void unMarkRemoveModules(IModule[] modules, IProgressMonitor monitor) throws CoreException {
        String jvmArgs = serverDelegate.getVMArgs();
        String trimedJvmArgs = deleteRemovedArtifactListFromJVMArgs(jvmArgs);
        String removedModuleString = getRemovedModuleStringFromJVMArgs(jvmArgs);
        
        for(IModule rModule : modules) {
            String configId = DeploymentUtils.getConfigId(serverDelegate.getServer(), rModule);
            List<String> rArtifactList = new ArrayList<String>();
            boolean needChange = false;
            
            if(! removedModuleString.equals("")) {
                String[] rArtifactIds = removedModuleString.split(",");
                
                rArtifactList.addAll(Arrays.asList(rArtifactIds));
                Iterator<String> iter = rArtifactList.iterator();
                while(iter.hasNext()) {
                    if(iter.next().equals(configId)) {
                        iter.remove();
                        needChange = true;
                        break;
                    }
                }
            }
            if(needChange) {
                Iterator<String> iter = rArtifactList.iterator();
                StringBuilder sBuilder = new StringBuilder("");
                while(iter.hasNext()) {
                    sBuilder.append(iter.next()).append(",");
                }
                if(sBuilder.length() > 0) {
                    removedModuleString = sBuilder.substring(0, sBuilder.length() - 1);
                    trimedJvmArgs = trimedJvmArgs.endsWith(" ") ? trimedJvmArgs : trimedJvmArgs + " ";
                    trimedJvmArgs = trimedJvmArgs + GeronimoServerDelegate.REMOVE_ARTIFACT_LIST + "=" + removedModuleString;
                }
                //persist the removed module to jvm args
                persistJVMArgs(trimedJvmArgs, monitor);
                Trace.trace(Trace.INFO, rModule.getName() + ":" + rModule.getModuleType() + " is unmarked", Activator.traceCore);
            }
        }
    }
    
    public String deleteRemovedArtifactListFromJVMArgs(String jvmArgs) {
        String newJvmArgs = "";
        
        int index = jvmArgs.indexOf(GeronimoServerDelegate.REMOVE_ARTIFACT_LIST);
        if(index >= 0) {
            newJvmArgs = jvmArgs.substring(0, index);
            int splitIndex = jvmArgs.indexOf(0x20, index);
            
            if(splitIndex > 0) {
                newJvmArgs += jvmArgs.substring(splitIndex + 1);// the new jvm args eliminate the -Dgeronimo.removedArtifactList=...
            }
            return newJvmArgs;
        }
        return jvmArgs;
    }
    
    public String getRemovedModuleStringFromJVMArgs(String jvmArgs) {
        String removedModuleString = "";
        
        int index = jvmArgs.indexOf(GeronimoServerDelegate.REMOVE_ARTIFACT_LIST);
        if(index >= 0) {
            removedModuleString = jvmArgs.substring(index);
            int splitIndex = removedModuleString.indexOf(0x20);
            
            if(splitIndex == -1) {// it means the -Dgeronimo.removedArtifactList=... is the last jvm arg
                removedModuleString = removedModuleString.substring(GeronimoServerDelegate.REMOVE_ARTIFACT_LIST.length() + 1);
            } else if(splitIndex > 0) {
                removedModuleString = removedModuleString.substring(GeronimoServerDelegate.REMOVE_ARTIFACT_LIST.length() + 1, splitIndex);
            }
        }
        return removedModuleString;
    }
    
    public void persistJVMArgs(String jvmArgs, IProgressMonitor monitor) throws CoreException {
        jvmArgs = jvmArgs.trim();
        ILaunchConfigurationWorkingCopy launchConfigWc = serverDelegate.getServer().getLaunchConfiguration(true, monitor).getWorkingCopy();
        launchConfigWc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, jvmArgs);
        launchConfigWc.doSave();

        serverDelegate.setVMArgs(jvmArgs);
    }
}
