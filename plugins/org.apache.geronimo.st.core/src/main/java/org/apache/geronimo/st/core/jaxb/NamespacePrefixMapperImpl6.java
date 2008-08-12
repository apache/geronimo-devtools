/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.geronimo.st.core.jaxb;

import java.util.HashMap;
import java.util.Map;

import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
import org.apache.geronimo.st.core.internal.Trace;

/**
 * <strong>NamespacePrefixMapperImpl6</strong> is used to map XML namespaces 
 * to a set of predetermined values for a Jave 1.6 runtime, which uses the internal JAXB 
 * implementation. 
 * 
 * If this class changes, then the test version in org.apache.geronimo.jee.common
 * needs to be updated to be kept in sync, as well as the Java 1.5 runtime version
 * 
 * @version $Rev$ $Date$ 
 */
public class NamespacePrefixMapperImpl6 extends NamespacePrefixMapper {

    private static Map<String, String> prefixMap = new HashMap<String, String>();

    static {
        prefixMap.put("http://geronimo.apache.org/xml/ns/deployment-1.2", "dep");
        prefixMap.put("http://geronimo.apache.org/xml/ns/j2ee/application-2.0", "app");
        prefixMap.put("http://geronimo.apache.org/xml/ns/j2ee/application-client-2.0", "client");
        prefixMap.put("http://geronimo.apache.org/xml/ns/j2ee/connector-1.2", "conn");
        prefixMap.put("http://openejb.apache.org/xml/ns/openejb-jar-2.2", "ejb");
        prefixMap.put("http://java.sun.com/xml/ns/persistence", "pers");
        prefixMap.put("http://openejb.apache.org/xml/ns/pkgen-2.1", "pkgen");
        prefixMap.put("http://geronimo.apache.org/xml/ns/naming-1.2", "name");
        prefixMap.put("http://geronimo.apache.org/xml/ns/security-2.0", "sec");
        prefixMap.put("http://geronimo.apache.org/xml/ns/j2ee/web-2.0.1", "web");
    }

    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        Trace.tracePoint("Entry", "NamespacePrefixMapperImpl6.getPreferredPrefix", namespaceUri, suggestion, requirePrefix);

        if (prefixMap.containsKey(namespaceUri))
            return prefixMap.get(namespaceUri);

        Trace.tracePoint("Exit", "NamespacePrefixMapperImpl6.getPreferredPrefix", namespaceUri, suggestion, requirePrefix);
        return suggestion;
    }
    
}
