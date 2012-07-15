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

import java.io.Serializable;


/**
 * 
 *
 * @version $Rev$ $Date$
 */
public class Artifact implements Serializable{
    private static final long serialVersionUID = -648381812102706448L;
    private String projectName;
    private String configId;
    public Artifact(String projectName, String configId) {
        super();
        this.projectName = projectName;
        this.configId = configId;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getConfigId() {
        return configId;
    }
    public void setConfigId(String configId) {
        this.configId = configId;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((configId == null) ? 0 : configId.hashCode());
        result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
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
        Artifact other = (Artifact) obj;
        if (configId == null) {
            if (other.configId != null)
                return false;
        } else if (!configId.equals(other.configId))
            return false;
        if (projectName == null) {
            if (other.projectName != null)
                return false;
        } else if (!projectName.equals(other.projectName))
            return false;
        return true;
    }
    
    
}
