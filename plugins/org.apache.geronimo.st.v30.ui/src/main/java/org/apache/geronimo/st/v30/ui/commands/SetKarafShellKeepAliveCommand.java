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
package org.apache.geronimo.st.v30.ui.commands;

import org.apache.geronimo.st.v30.core.GeronimoServerDelegate;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.server.core.IServerWorkingCopy;

/**
 * 
 *
 * @version $Rev$ $Date$
 */
public class SetKarafShellKeepAliveCommand extends ServerCommand {
    int value;
    int oldValue;
    Text text;
    GeronimoServerDelegate serverDelegate;
    /**
     * @param server
     * @param name
     */
    public SetKarafShellKeepAliveCommand(IServerWorkingCopy server, Text text, int value) {
        super(server, "SetKarafShellKeepAliveCommand");
        this.text = text;
        this.value = value;
        this.serverDelegate = (GeronimoServerDelegate) server.getOriginal().getAdapter(GeronimoServerDelegate.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.st.v30.ui.commands.ServerCommand#execute()
     */
    public void execute() {
        oldValue = serverDelegate.getKarafShellKeepAlive();
        server.setAttribute(GeronimoServerDelegate.PROPERTY_KARAF_SHELL_KEEPALIVE, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.st.v30.ui.commands.ServerCommand#undo()
     */
    public void undo() {
        text.setText(Integer.toString(oldValue));
        server.setAttribute(GeronimoServerDelegate.PROPERTY_KARAF_SHELL_KEEPALIVE, oldValue);
    }
}
