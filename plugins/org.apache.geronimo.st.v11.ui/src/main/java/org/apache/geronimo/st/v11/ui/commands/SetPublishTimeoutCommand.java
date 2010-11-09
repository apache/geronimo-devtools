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
package org.apache.geronimo.st.v11.ui.commands;

import org.apache.geronimo.st.ui.commands.ServerCommand;
import org.apache.geronimo.st.v11.core.GeronimoServerDelegate;
import org.eclipse.wst.server.core.IServerWorkingCopy;
/**
 * @version $Rev: 732383 $ $Date: 2009-01-08 00:27:32 +0800 (Thu, 08 Jan 2009) $
 *
 * Command to change the server's auto-publish setting.
 */
public class SetPublishTimeoutCommand extends ServerCommand {
    protected long timeOut;
    protected long oldTimeOut;

    /**
     * SetServerAutoPublishDefaultCommand constructor.
     *
     * @param server a server
     * @param time a publish time
     */
    public SetPublishTimeoutCommand(IServerWorkingCopy server, long timeOut) {
        super(server, "SetServerAutoPublishTimeOutCommand");
        this.timeOut = timeOut;
    }

    /**
     * Execute the command.
     */
    public void execute() {
        GeronimoServerDelegate gs = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
        oldTimeOut = gs.getPublishTimeout();
        gs.setPublishTimeout(timeOut);
    }

    /**
     * Undo the command.
     */
    public void undo() {
        GeronimoServerDelegate gs = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
        gs.setPublishTimeout(oldTimeOut);
    }
}
