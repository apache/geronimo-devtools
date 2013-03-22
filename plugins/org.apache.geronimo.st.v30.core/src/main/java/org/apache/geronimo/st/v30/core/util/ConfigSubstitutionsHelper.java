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
package org.apache.geronimo.st.v30.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.geronimo.kernel.util.IOUtils;
import org.apache.geronimo.st.v30.core.Activator;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.runtime.IPath;


public class ConfigSubstitutionsHelper {
    
    private static final String INSTRUCTION = ("# Put variables and their substitution values in this file. \n"
            + "# They will be used when processing the corresponding config.xml. \n"
            + "# Values in this file can be overridden by environment variables and system properties \n"
            + "# by prefixing the property name with 'org.apache.geronimo.config.substitution.' \n"
            + "# For example, an entry such as hostName=localhost \n"
            + "# can be overridden by an environment variable or system property org.apache.geronimo.config.substitution.hostName=foo \n"
            + "# When running multiple instances of Geronimo choose a PortOffset value such that none of the ports conflict. \n"
            + "# For example, try PortOffset=10 \n");
    
    public static final String PORT_OFFSET = "PortOffset";
    public static final String NAMING_PORT = "NamingPort";
    public static final String HTTP_PORT = "HTTPPort";
    
    private final File configSubstitutionsFile;
    private Properties properties;
    private boolean dirty;
    
    public ConfigSubstitutionsHelper(IPath installDirectory) {
        configSubstitutionsFile = installDirectory.append("var/config/config-substitutions.properties").toFile();
    }
    
    public void load() {
        properties = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(configSubstitutionsFile);
            properties.load(in);
        } catch (Exception e) {
            // ignore - file does not have to be there
        } finally {
            IOUtils.close(in);
        }
    }
 
    public void store() {
        if (dirty) {
            save();
            dirty = false;
        }
    }
    
    private void save() {
        OutputStream out = null;
        try {
            out = new FileOutputStream(configSubstitutionsFile);
            properties.store(out, INSTRUCTION);
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Error saving config-substitutions.properties file", e, Activator.traceCore);
        } finally {
            IOUtils.close(out);
        }
    }
    
    public String getProperty(String name) {
        return properties.getProperty(name);
    }
    
    public String getProperty(String name, String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }
    
    public void setProperty(String name, String value) {
        Object oldValue = properties.setProperty(name, value);
        if (oldValue == null || !oldValue.equals(value)) {
            dirty = true;
        }
    }
}
