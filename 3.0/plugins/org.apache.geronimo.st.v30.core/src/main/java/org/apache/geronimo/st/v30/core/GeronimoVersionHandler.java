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
package org.apache.geronimo.st.v30.core;

import javax.enterprise.deploy.spi.TargetModuleID;

import org.apache.geronimo.deployment.plugin.TargetModuleIDImpl;
import org.apache.geronimo.st.v30.core.IGeronimoVersionHandler;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.wst.server.core.IModule;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoVersionHandler implements IGeronimoVersionHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.st.core.IGeronimoVersionHandler#getConfigID(org.eclipse.wst.server.core.IModule)
     */
    public String getConfigID(IModule module) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoVersionHandler.getConfigID", module);
        String configId = GeronimoUtils.getConfigId(module);
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoVersionHandler.getConfigID", configId);
        return configId;
    }
    
    /* (non-Javadoc)
     * @see org.apache.geronimo.st.core.IGeronimoVersionHandler#createTargetModuleId(java.lang.String)
     */
    public TargetModuleID createTargetModuleId(String configId) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoVersionHandler.createTargetModuleId", configId);
        TargetModuleID moduleId = new TargetModuleIDImpl(null, configId);
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoVersionHandler.createTargetModuleId", moduleId);
        return moduleId;
    }
}
