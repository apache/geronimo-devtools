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

package org.apache.geronimo.jee.common;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * <strong>NamespaceFilter</strong> is used to filter out older versions (e.g., 
 * v1.1) of Geronimo namespaces and replace them with the more current versions 
 * (e.g., v2.1)<p>
 * 
 * This is a test version of the class that is found in org.apache.geronimo.st.core.jaxb
 * 
 * @version $Rev$ $Date$
 */
public class NamespaceFilter extends XMLFilterImpl {

    private static Map<String, String> namespace = new HashMap<String, String>();

    static {
        // 
        // Convert old deployment namespaces
        // 
        namespace.put("http://geronimo.apache.org/xml/ns/deployment", 
                      "http://geronimo.apache.org/xml/ns/deployment-1.2");
        namespace.put("http://geronimo.apache.org/xml/ns/deployment-1.0", 
                      "http://geronimo.apache.org/xml/ns/deployment-1.2");
        namespace.put("http://geronimo.apache.org/xml/ns/deployment-1.1", 
                      "http://geronimo.apache.org/xml/ns/deployment-1.2");

        // 
        // Convert old application namespaces
        // 
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/application", 
                      "http://geronimo.apache.org/xml/ns/j2ee/application-2.0");
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/application-1.1", 
                      "http://geronimo.apache.org/xml/ns/j2ee/application-2.0");
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/application-1.2", 
                      "http://geronimo.apache.org/xml/ns/j2ee/application-2.0");

        // 
        // Convert old application-client namespaces
        // 
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/application-client", 
                      "http://geronimo.apache.org/xml/ns/j2ee/application-client-2.0");
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/application-client-1.2", 
                      "http://geronimo.apache.org/xml/ns/j2ee/application-client-2.0");
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/application-client-1.1", 
                      "http://geronimo.apache.org/xml/ns/j2ee/application-client-2.0");

        // 
        // Convert old connector namespaces
        // 
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/connector", 
                      "http://geronimo.apache.org/xml/ns/j2ee/connector-1.2");
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/connector-1.0", 
                      "http://geronimo.apache.org/xml/ns/j2ee/connector-1.2");
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/connector-1.1", 
                      "http://geronimo.apache.org/xml/ns/j2ee/connector-1.2");

        // 
        // Convert old web namespaces
        // 
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/web", 
                      "http://geronimo.apache.org/xml/ns/j2ee/web-2.0.1");
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/web-1.0", 
                      "http://geronimo.apache.org/xml/ns/j2ee/web-2.0.1");
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/web-1.1", 
                      "http://geronimo.apache.org/xml/ns/j2ee/web-2.0.1");
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/web-1.2", 
                      "http://geronimo.apache.org/xml/ns/j2ee/web-2.0.1");
        namespace.put("http://geronimo.apache.org/xml/ns/j2ee/web-2.0", 
                      "http://geronimo.apache.org/xml/ns/j2ee/web-2.0.1");

        // 
        // Convert old naming namespaces
        // 
        namespace.put("http://geronimo.apache.org/xml/ns/naming", 
                      "http://geronimo.apache.org/xml/ns/naming-1.2");
        namespace.put("http://geronimo.apache.org/xml/ns/naming-1.0", 
                      "http://geronimo.apache.org/xml/ns/naming-1.2");
        namespace.put("http://geronimo.apache.org/xml/ns/naming-1.1", 
                      "http://geronimo.apache.org/xml/ns/naming-1.2");

        // 
        // Convert old security namespaces
        // 
        namespace.put("http://geronimo.apache.org/xml/ns/security", 
                      "http://geronimo.apache.org/xml/ns/security-2.0");
        namespace.put("http://geronimo.apache.org/xml/ns/security-1.1", 
                      "http://geronimo.apache.org/xml/ns/security-2.0");
        namespace.put("http://geronimo.apache.org/xml/ns/security-1.2", 
                      "http://geronimo.apache.org/xml/ns/security-2.0");

        // 
        // Convert old openejb-jar namespaces 
        // 
        namespace.put("http://www.openejb.org/xml/ns/openejb-jar", 
                      "http://openejb.apache.org/xml/ns/openejb-jar-2.2");
        namespace.put("http://www.openejb.org/xml/ns/openejb-jar-2.1", 
                      "http://openejb.apache.org/xml/ns/openejb-jar-2.2");
        namespace.put("http://www.openejb.org/xml/ns/openejb-jar-2.2", 
                      "http://openejb.apache.org/xml/ns/openejb-jar-2.2");
        namespace.put("http://www.openejb.org/xml/ns/openejb-jar-2.3", 
                      "http://openejb.apache.org/xml/ns/openejb-jar-2.2");
        namespace.put("http://www.openejb.org/xml/ns/pkgen", 
                      "http://openejb.apache.org/xml/ns/pkgen-2.1");
        namespace.put("http://www.openejb.org/xml/ns/pkgen-2.0",
                      "http://openejb.apache.org/xml/ns/pkgen-2.1");
    }

    public NamespaceFilter(XMLReader xmlReader) {
        super(xmlReader);
    }

    public void startElement(String uri, String localName, String qname, Attributes atts) throws SAXException {

        if (namespace.containsKey( uri )) {
            uri = namespace.get( uri );
        }

        super.startElement(uri, localName, qname, atts);
    }
}
