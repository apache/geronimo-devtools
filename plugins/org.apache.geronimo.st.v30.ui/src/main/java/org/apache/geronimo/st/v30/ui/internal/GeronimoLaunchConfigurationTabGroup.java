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
package org.apache.geronimo.st.v30.ui.internal;

import org.apache.geronimo.st.v30.core.GeronimoServerDelegate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.ui.ServerLaunchConfigurationTab;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.ui.ILaunchConfigurationTabGroup#createTabs(org.eclipse.debug.ui.ILaunchConfigurationDialog,
     *      java.lang.String)
     */
    public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
        ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[6];
        tabs[0] = new ServerLaunchConfigurationTab(new String[] { "org.apache.geronimo" });
        tabs[0].setLaunchConfigurationDialog(dialog);
        tabs[1] = new JavaArgumentsTab();
        tabs[1].setLaunchConfigurationDialog(dialog);
        tabs[2] = new JavaClasspathTab();
        tabs[2].setLaunchConfigurationDialog(dialog);
        tabs[3] = new SourceLookupTab();
        tabs[3].setLaunchConfigurationDialog(dialog);
        tabs[4] = new EnvironmentTab();
        tabs[4].setLaunchConfigurationDialog(dialog);
        tabs[5] = new CommonTab();
        tabs[5].setLaunchConfigurationDialog(dialog);
        setTabs(tabs);
    }
    
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        //need to save all values to Geronimo-specific arguments. Eg, VMArguments
         try {
             super.performApply(configuration);
             
             IServer server = ServerUtil.getServer(configuration);
             GeronimoServerDelegate sd = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
             String oldValue = sd.getVMArgs();
             String newValue = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, oldValue);
             sd.setVMArgs(newValue);     
                       
         } catch (CoreException e) {
        	  MessageDialog.openError(Display.getCurrent().getActiveShell(),"Error", e.getMessage());
         }
         
     }

}
