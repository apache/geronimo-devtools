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

import java.lang.reflect.Method;

import org.apache.geronimo.st.v30.core.GeronimoServerDelegate;
import org.apache.geronimo.st.v30.ui.Activator;
import org.apache.geronimo.st.v30.ui.internal.Trace;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IServerWorkingCopy;

public class SetPropertyCommand extends AbstractOperation {
    
    protected final IServerWorkingCopy server;
    protected GeronimoServerDelegate gs;
    
    protected Object newValue;
    protected Object oldValue;
    
    private final String propertyName;
    private final Class<?> propertyType;
    
    private Method getterMethod;
    private Method setterMethod;

    public SetPropertyCommand(IServerWorkingCopy server, String propertyName, Class<?> propertyType, Object newValue) {
        super(propertyName);
        this.server = server;
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.newValue = newValue;
    }

    private void init() throws Exception {
        try {
            this.getterMethod = GeronimoServerDelegate.class.getMethod("get" + propertyName);
        } catch (NoSuchMethodException e) {
            this.getterMethod = GeronimoServerDelegate.class.getMethod("is" + propertyName);
        }
        
        String setterName = "set" + propertyName;
        this.setterMethod = GeronimoServerDelegate.class.getMethod(setterName, new Class[] {propertyType} );
    }
    
    public IStatus execute(IProgressMonitor monitor, IAdaptable adapt) {
        try {
            init();
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Internal operation error", e, Activator.logUi);
            return new Status(Status.ERROR, Activator.PLUGIN_ID, "Internal operation error", e);
        }
        
        GeronimoServerDelegate gs = getGeronimoServerDelegate();
        
        try {
            oldValue = getterMethod.invoke(gs);
            setterMethod.invoke(gs, new Object[] {newValue});
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Error setting " + propertyName + " property", e, Activator.logUi);
            return new Status(Status.ERROR, Activator.PLUGIN_ID, "Error setting " + propertyName + " property", e);
        } 
        
        return Status.OK_STATUS;
    }

    public IStatus undo(IProgressMonitor monitor, IAdaptable adapt) {
        if (gs != null) {
            try {
                setterMethod.invoke(gs, new Object[] {oldValue});
            } catch (Exception e) {
                Trace.trace(Trace.ERROR, "Error unsetting " + propertyName + " property", e, Activator.logUi);
                return new Status(Status.ERROR, Activator.PLUGIN_ID, "Error unsetting " + propertyName + " property", e);
            } 
        }
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
