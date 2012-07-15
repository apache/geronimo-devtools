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
package org.apache.geronimo.st.v21.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.jee.naming.Pattern;

/**
 * @version $Rev$ $Date$
 */
public interface IGeronimoServerInfo {
    
    public ArrayList<String> getSecurityRealms();

    public ArrayList<Pattern> getJmsConnectionFactories() ;

    public ArrayList<Pattern> getJmsDestinations();

    public ArrayList<Pattern> getJdbcConnectionPools();
    
    public ArrayList<Pattern> getDeployedEjbs();

    public ArrayList<Pattern> getJavaMailSessions();
    
    public ArrayList<Dependency> getCommonLibs();

    public ArrayList<org.apache.geronimo.jee.deployment.Pattern> getDeployedCredentialStores() ;
    
    public HashMap<org.apache.geronimo.jee.deployment.Pattern,HashMap<String,ArrayList<String>>> getDeployedCredentialStoreAttributes();

    public void updateInfo();

    public void printServerInfo() ;
    
}
