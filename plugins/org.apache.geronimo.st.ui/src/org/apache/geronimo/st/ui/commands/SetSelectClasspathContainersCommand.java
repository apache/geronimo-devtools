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
package org.apache.geronimo.st.ui.commands;

import org.apache.geronimo.st.core.GeronimoServerDelegate;
import org.eclipse.wst.server.core.IServerWorkingCopy;

public class SetSelectClasspathContainersCommand extends ServerCommand {

    boolean value;
    boolean oldValue;


    /**
     * @param server
     * @param name
     */
    public SetSelectClasspathContainersCommand(IServerWorkingCopy server, boolean value) {
        super(server, "SetSelectClasspathContainersCommand");
        this.value = value;
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.st.ui.commands.ServerCommand#execute()
     */
    public void execute() {
        GeronimoServerDelegate gs = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
        oldValue = gs.isSelectClasspathContainers();
        gs.setSelectClasspathContainers(value);
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.st.ui.commands.ServerCommand#undo()
     */
    public void undo() {
        GeronimoServerDelegate gs = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
        gs.setSelectClasspathContainers(oldValue);
    }

}
