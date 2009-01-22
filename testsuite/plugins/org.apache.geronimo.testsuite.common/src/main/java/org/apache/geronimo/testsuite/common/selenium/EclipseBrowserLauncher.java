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

package org.apache.geronimo.testsuite.common.selenium;

import org.apache.geronimo.testsuite.common.ui.AbbotHelper;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.openqa.selenium.server.browserlaunchers.AbstractBrowserLauncher;

/**
 * EclipseBrowserLauncher
 *
 * @version $Rev$ $Date$
 */
public class EclipseBrowserLauncher extends AbstractBrowserLauncher {

    public EclipseBrowserLauncher(int a, String sessionId) {
        super(sessionId);
    }

    @Override
    protected void launch(String url) {
        try {
            AbbotHelper abbotHelper = EclipseSeleniumServer.INSTANCE.getAHelper();
            Shell workbenchShell = EclipseSeleniumServer.INSTANCE.getShell();

            abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&Window", "Web Browser", "&0 Internal Web Browser"});
            Shell openShell = abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&Window", "Show &View", "&Other..."}, "Show View");
            abbotHelper.clickTreeItem (openShell, 
                    new String[] {"General", "Internal Web Browser"});
            abbotHelper.clickButton (openShell, IDialogConstants.OK_LABEL);
            
            abbotHelper.setCombo (workbenchShell, url);
            abbotHelper.clickToolItem (workbenchShell, "Go to the selected URL");
            abbotHelper.waitTime (AbbotHelper.WAIT_LONG);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void close() {
    }

    public Process getProcess() {
        return null;
    }

}
