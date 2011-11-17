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

import java.util.ArrayList;
import java.util.List;
import org.apache.geronimo.st.core.GeronimoServerDelegate;
import org.apache.geronimo.st.ui.Activator;
import org.apache.geronimo.st.ui.internal.Trace;
import org.eclipse.wst.server.core.IServerWorkingCopy;

/**
 * @version $Rev$ $Date$
 */
public class SetClasspathContainersCommand extends ServerCommand {

    private List<String> newList;
    private List<String> oldList;


    /*
     * @param server
     * @param checkList string array
     */
    public SetClasspathContainersCommand(IServerWorkingCopy server, Object[] checkList) {

        super(server, "SetClasspathContainersCommand");
        this.newList = createList( checkList );

        Trace.tracePoint("ENTRY", Activator.traceCommands, "SetClasspathContainersCommand", checkList, checkList.length );
        Trace.tracePoint("EXIT", Activator.traceCommands, "SetClasspathContainersCommand");
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.st.ui.commands.ServerCommand#execute()
     */
    public void execute() {
        Trace.tracePoint("ENTRY", Activator.traceCommands, "SetClasspathContainersCommand.execute");

        GeronimoServerDelegate gs = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
        oldList = gs.getClasspathContainers();
        gs.setClasspathContainers(newList);

        Trace.tracePoint("EXIT", Activator.traceCommands, "SetClasspathContainersCommand.execute");
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.st.ui.commands.ServerCommand#undo()
     */
    public void undo() {
        Trace.tracePoint("ENTRY", Activator.traceCommands, "SetClasspathContainersCommand.undo");

        GeronimoServerDelegate gs = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
        gs.setClasspathContainers(oldList);

        Trace.tracePoint("EXIT", Activator.traceCommands, "SetClasspathContainersCommand.undo");
    }


    //
    // Convert object array to List<String>
    //
	public List<String> createList( Object[] checkList ) {
        Trace.tracePoint("ENTRY", Activator.traceCommands, "SetClasspathContainersCommand.createList");
    
        List<String> containers = new ArrayList<String>();
        for (Object container : checkList) {
            containers.add( (String)container );
        }

        Trace.tracePoint("EXIT", Activator.traceCommands, "SetClasspathContainersCommand.createList", containers );
        return containers;
   }

}
