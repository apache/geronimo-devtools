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
package org.apache.geronimo.st.v30.core.persist.factory.impl;

import java.io.File;

import org.apache.geronimo.st.v30.core.Activator;
import org.apache.geronimo.st.v30.core.persist.PersistenceManager;
import org.apache.geronimo.st.v30.core.persist.factory.PersistenceManagerFactory;
import org.apache.geronimo.st.v30.core.persist.impl.FilePersistenceManager;

/**
 * 
 *
 * @version $Rev$ $Date$
 */
public class FilePersistenceManagerFactory implements PersistenceManagerFactory<File, File> {
    private static PersistenceManagerFactory<File, File> instance = new FilePersistenceManagerFactory();
    private PersistenceManager<File, File> pMgr = null;
    
    private FilePersistenceManagerFactory(){};
    
    public synchronized static PersistenceManagerFactory<File, File> getInstance() {
        if(instance == null) instance = new FilePersistenceManagerFactory();
        return instance;
    }
    /* (non-Javadoc)
     * @see org.apache.geronimo.st.v30.core.persist.factory.PersistenceManagerFactory#create(java.lang.Object[])
     */
    @Override
    public synchronized PersistenceManager<File, File> create(Object... objs) throws Exception {
        if(objs == null || objs.length != 2 || !(objs[0] instanceof String) || !(objs[1] instanceof String)) throw new Exception("The parameters must be two Strings");
        
        File src = Activator.getDefault().getStateLocation().append((String) objs[0]).toFile();
        File dst = Activator.getDefault().getStateLocation().append((String) objs[1]).toFile();
        if(pMgr == null) pMgr = new FilePersistenceManager(src, dst);
        return pMgr;
    }

}
