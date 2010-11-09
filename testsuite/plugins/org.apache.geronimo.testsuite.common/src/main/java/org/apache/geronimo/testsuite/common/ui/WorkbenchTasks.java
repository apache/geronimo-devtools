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

package org.apache.geronimo.testsuite.common.ui;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

import abbot.swt.finder.generic.MultipleFoundException;
import abbot.swt.finder.generic.NotFoundException;

public class WorkbenchTasks {
    Shell workbenchShell;
    AbbotHelper aHelper;

    public WorkbenchTasks (Shell shell, AbbotHelper helper) {
        workbenchShell = shell;
        aHelper = helper;
    }
    
    public void showJEEPerspective() throws MultipleFoundException, NotFoundException {

    	aHelper.clickMenuItem (workbenchShell,
                new String[] {"&Window", "&Close Perspective"});
    	
    	Shell perspectiveShell = aHelper.clickMenuItem (workbenchShell,
              new String[] {"&Window", "&Open Perspective", "&Other..." },
              "Open Perspective");
        aHelper.clickItem (perspectiveShell, "Java EE (default)");
        aHelper.clickButton (perspectiveShell, IDialogConstants.OK_LABEL);        
    }
}
