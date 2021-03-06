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
package org.apache.geronimo.st.v30.ui.commands;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.wst.server.core.IServerWorkingCopy;

/**
 * @version $Rev$ $Date$
 *
 * Command to change the server's auto-publish setting.
 */
public class SetPublishTimeoutCommand extends SetPropertyCommand {
    
    private Spinner spinner;
    
    /**
     * SetServerAutoPublishDefaultCommand constructor.
     *
     * @param server a server
     * @param time a publish time
     */
    public SetPublishTimeoutCommand(IServerWorkingCopy server, Spinner spinner) {
        super(server, "PublishTimeout", long.class, spinner.getSelection() * 1000);
        this.spinner = spinner;
    }

    public IStatus undo(IProgressMonitor monitor, IAdaptable adapt) {
        IStatus status = super.undo(monitor, adapt);
        if (status.isOK()) {
            int value = (int) ((Long)oldValue).longValue() / 1000;
            spinner.setSelection(value);
        }
        return status;
    }
    
}
