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
package org.apache.geronimo.st.v30.core.persist.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.apache.geronimo.st.v30.core.Activator;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.apache.geronimo.st.v30.core.persist.PersistenceManager;

/**
 * 
 *
 * @version $Rev$ $Date$
 */
public class FilePersistenceManager implements PersistenceManager<File, File> {
    private File source;
    private File destination;
    
    public FilePersistenceManager(File src, File dst) {
        this.source = src;
        this.destination = dst;
    }
    /* (non-Javadoc)
     * @see org.apache.geronimo.st.v30.core.persist.PersistenceManager#load(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <K, V> V load(K key, V value) throws Exception {
        if(this.source.exists()) {
            ObjectInput oi = null;
            try {
                oi = new ObjectInputStream(new BufferedInputStream(new FileInputStream(this.source)));
                return (V) oi.readObject();
            } catch(IOException e) {
                Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logCore);
            } finally {
                if(oi != null) {
                    oi.close();
                    oi = null;
                }
            }
        }
        return value;
        
    }
    
    /* (non-Javadoc)
     * @see org.apache.geronimo.st.v30.core.persist.PersistenceManager#load(java.lang.Object, java.lang.Class)
     */
    @Override
    public <K, V> V load(K key, Class<V> clazz) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.apache.geronimo.st.v30.core.persist.PersistenceManager#save(java.lang.Object)
     */
    @Override
    public <V> void save(V value) throws Exception {
        ObjectOutput oo = null;
        try {
            oo = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(this.destination)));
            oo.writeObject(value);
        } catch(IOException e) {
            Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logCore);
        } finally {
            if(oo != null) {
                oo.flush();
                oo.close();
                oo = null;
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.geronimo.st.v30.core.persist.PersistenceManager#getSource()
     */
    @Override
    public File getSource() {
        return this.source;
    }

    /* (non-Javadoc)
     * @see org.apache.geronimo.st.v30.core.persist.PersistenceManager#setSource(java.lang.Object)
     */
    @Override
    public void setSource(File src) {
        this.source = src;
    }

    /* (non-Javadoc)
     * @see org.apache.geronimo.st.v30.core.persist.PersistenceManager#getDestination()
     */
    @Override
    public File getDestination() {
        return this.destination;
    }

    /* (non-Javadoc)
     * @see org.apache.geronimo.st.v30.core.persist.PersistenceManager#setDestination(java.lang.Object)
     */
    @Override
    public void setDestination(File dst) {
        this.destination = dst;
        
    }
    @Override
    public void setSource(String srcPath) {
        this.source = Activator.getDefault().getStateLocation().append(srcPath).toFile();
    }
    @Override
    public void setDestination(String dstPath) {
        this.destination = Activator.getDefault().getStateLocation().append(dstPath).toFile();
    }

}
