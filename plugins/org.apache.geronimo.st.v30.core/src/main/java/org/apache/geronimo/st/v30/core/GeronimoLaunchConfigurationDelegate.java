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
package org.apache.geronimo.st.v30.core;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.geronimo.st.v30.core.internal.Messages;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.jst.server.core.ServerProfilerDelegate;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoLaunchConfigurationDelegate extends AbstractJavaLaunchConfigurationDelegate {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration,
     *      java.lang.String, org.eclipse.debug.core.ILaunch,
     *      org.eclipse.core.runtime.IProgressMonitor)
     */
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {

        if (configuration.hasAttribute(GeronimoServerBehaviourDelegate.ERROR_SETUP_LAUNCH_CONFIGURATION)){
            //get error flag from configuration if it's set in setLaunchConfiguration
            String errorMessage = configuration.getAttribute(GeronimoServerBehaviourDelegate.ERROR_SETUP_LAUNCH_CONFIGURATION,"");
            throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, IJavaLaunchConfigurationConstants.ERR_INTERNAL_ERROR, errorMessage, null));
        }
        
        IServer server = ServerUtil.getServer(configuration);
        if (server == null) {
            throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, IJavaLaunchConfigurationConstants.ERR_INTERNAL_ERROR, Messages.missingServer, null));
        }
        GeronimoServerBehaviourDelegate geronimoServer = (GeronimoServerBehaviourDelegate) server.loadAdapter(GeronimoServerBehaviourDelegate.class, null);
        geronimoServer.setupLaunch(launch, mode, monitor);

        if (geronimoServer.isRemote()) {
            // no support for launching remote servers
            return;
        }

        String mainTypeName = geronimoServer.getRuntimeClass();
        IVMInstall vm = verifyVMInstall(configuration);
        IVMRunner runner = vm.getVMRunner(mode);
        
        if(runner == null && ILaunchManager.PROFILE_MODE.equals(mode)){
            runner = vm.getVMRunner(ILaunchManager.RUN_MODE);
        }

        File workingDir = verifyWorkingDirectory(configuration);
        String workingDirName = null;
        if (workingDir != null)
            workingDirName = workingDir.getAbsolutePath();

        String pgmArgs = getProgramArguments(configuration);
        String vmArgs = getVMArguments(configuration);
        String[] envp = getEnvironment(configuration);
        
        File jreHome = new File(vm.getInstallLocation(), "jre");
        if (!jreHome.exists()) {
            jreHome = vm.getInstallLocation();
        }        
        
        vmArgs = vmArgs.replace("$(JRE_HOME)", jreHome.getAbsolutePath());
        vmArgs = vmArgs.replace("$(GERONIMO_HOME)", server.getRuntime().getLocation().toOSString());
        
        ExecutionArguments execArgs = new ExecutionArguments(vmArgs, pgmArgs);
        Map vmAttributesMap = getVMSpecificAttributesMap(configuration);
        String[] classpath = getClasspath(configuration);
        
        // Create VM config
        VMRunnerConfiguration runConfig = new VMRunnerConfiguration(mainTypeName, classpath);
        runConfig.setProgramArguments(execArgs.getProgramArgumentsArray());
        runConfig.setVMArguments(updateJVMArguments(execArgs.getVMArgumentsArray(), geronimoServer));
        runConfig.setWorkingDirectory(workingDirName);
        runConfig.setEnvironment(envp);
        runConfig.setVMSpecificAttributesMap(vmAttributesMap);
        
        traceArguments("vmArgs", runConfig.getVMArguments());
        traceArguments("pgmArgs", runConfig.getProgramArguments());
        
        // Bootpath
        String[] bootpath = getBootpath(configuration);
        if (bootpath != null && bootpath.length > 0)
            runConfig.setBootClassPath(bootpath);

        setDefaultSourceLocator(launch, configuration);
        
        if (ILaunchManager.PROFILE_MODE.equals(mode)) {
            try {
                ServerProfilerDelegate.configureProfiling(launch, vm, runConfig, monitor);
            } catch (CoreException ce) {
                geronimoServer.stopImpl();
                throw ce;
            }
        }

        geronimoServer.startPingThread();
        runner.run(runConfig, launch, monitor);
        geronimoServer.setProcess(launch.getProcesses()[0]);
    }

    private String[] updateJVMArguments(String[] jvmArguments, GeronimoServerBehaviourDelegate server) {
        boolean managedApplicationStart = server.getServerDelegate().isManageApplicationStart();        
        Trace.trace(Trace.INFO, "GeronimoLaunchConfigurationDelegate: manageApplicationStart:=" + managedApplicationStart, Activator.traceCore);
        if (managedApplicationStart) {
            Set<String> modifiedConfigs = server.getModifiedConfigIds();
            if (!modifiedConfigs.isEmpty()) {
                String[] newJvmArguments = new String[jvmArguments.length + 1];
                System.arraycopy(jvmArguments, 0, newJvmArguments, 0, jvmArguments.length);
                newJvmArguments[jvmArguments.length] = toString("-Dgeronimo.loadOnlyConfigList=", modifiedConfigs);
                return newJvmArguments;
            }
        }
        return jvmArguments;
    }
    
    private static String toString(String start, Collection<String> list) {
        Iterator<String> iter = list.iterator();
        StringBuilder builder = new StringBuilder(start);
        while(iter.hasNext()) {
            builder.append(iter.next());
            if (iter.hasNext()) {
                builder.append(",");
            }
        }
        return builder.toString();
    }
    
    private static void traceArguments(String name, String[] array) {
        for (int i = 0; i < array.length; i++) {
            Trace.trace(Trace.INFO, name + "[" + i + "] = " + array[i], Activator.traceCore);
        }
    }
}
