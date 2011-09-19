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
package org.apache.geronimo.st.v30.core.base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.osgi.framework.Version;

/**
 * 
 *
 * @version $Rev$ $Date$
 */
public class Bundle implements Serializable {
    private static final long serialVersionUID = 206202053802426677L;
    private String projectName;
    private String symbolicName;
    private transient Version version;
    private transient long id;
    private int startLevel;
    
    
    public Bundle(String projectName, String symbolicName, Version version, long id, int startLevel) {
        super();
        this.projectName = projectName;
        this.symbolicName = symbolicName;
        this.version = version;
        this.id = id;
        this.startLevel = startLevel;
    }
    
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(version.toString());
    }
    
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.version = new Version((String) stream.readObject());
    }
    
    public String getProjectName() {
        return projectName;
    }


    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public String getSymbolicName() {
        return symbolicName;
    }


    public void setSymbolicName(String symbolicName) {
        this.symbolicName = symbolicName;
    }


    public Version getVersion() {
        return version;
    }


    public void setVersion(Version version) {
        this.version = version;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public int getStartLevel() {
        return startLevel;
    }


    public void setStartLevel(int startLevel) {
        this.startLevel = startLevel;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((symbolicName == null) ? 0 : symbolicName.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bundle other = (Bundle) obj;
        if (symbolicName == null) {
            if (other.symbolicName != null)
                return false;
        } else if (!symbolicName.equals(other.symbolicName))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }


}
