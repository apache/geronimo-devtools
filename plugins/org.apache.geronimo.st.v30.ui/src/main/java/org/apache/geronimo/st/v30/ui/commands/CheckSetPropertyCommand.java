/**
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
package org.apache.geronimo.st.v30.ui.commands;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.wst.server.core.IServerWorkingCopy;

public class CheckSetPropertyCommand extends SetPropertyCommand {
    
    private Button checkButton;

    public CheckSetPropertyCommand(IServerWorkingCopy server, String propertyName, Button checkButton) {
        super(server, propertyName, boolean.class, checkButton.getSelection());
        this.checkButton = checkButton;
    }

    public IStatus undo(IProgressMonitor monitor, IAdaptable adapt) {
        IStatus status = super.undo(monitor, adapt);
        if (status.isOK()) {
            checkButton.setData("undo");
            boolean value = !((Boolean)newValue).booleanValue();
            checkButton.setSelection(value);
            // setSelection does not fire Selection event so have to do it manually
            checkButton.notifyListeners(SWT.Selection, new Event());
        }
        return status;
    }

}
