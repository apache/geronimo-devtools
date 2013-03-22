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

import org.apache.geronimo.st.v30.core.GeronimoServerDelegate;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IServerWorkingCopy;

/**
 * @version $Rev$ $Date$
 */
public abstract class ServerCommand extends AbstractOperation {
    
    protected IServerWorkingCopy server;
    protected GeronimoServerDelegate gs;

    public ServerCommand(IServerWorkingCopy server, String name) {
        super(name);
        this.server = server;
    }

    public abstract void execute();

    public IStatus execute(IProgressMonitor monitor, IAdaptable adapt) {
        execute();
        return Status.OK_STATUS;
    }

    public abstract void undo();

    public IStatus undo(IProgressMonitor monitor, IAdaptable adapt) {
        undo();
        return Status.OK_STATUS;
    }

    public IStatus redo(IProgressMonitor monitor, IAdaptable adapt) {
        return execute(monitor, adapt);
    }
    
    protected GeronimoServerDelegate getGeronimoServerDelegate() {
        gs = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
        if (gs == null) {
            gs = (GeronimoServerDelegate) server.loadAdapter(GeronimoServerDelegate.class, new NullProgressMonitor());
        }
        return gs;
    }
}
