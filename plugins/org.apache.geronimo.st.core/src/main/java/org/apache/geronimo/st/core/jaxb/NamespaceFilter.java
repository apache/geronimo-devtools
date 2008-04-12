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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * <strong>NamespaceFilter</strong> is used to filter out older versions (e.g., 
 * v1.1) of Geronimo namespaces and replace them with the more current versions 
 * (e.g., v2.1)<p>
 * 
 * 
 * @version $Rev$ $Date$
 */
public class NamespaceFilter extends XMLFilterImpl {

    public NamespaceFilter(XMLReader xmlReader) {
        super(xmlReader);
    }


    public void startElement(String uri, String localName, String qname, Attributes atts) throws SAXException {

        if (uri.equals("http://geronimo.apache.org/xml/ns/j2ee/application-1.2")) {
            uri = "http://geronimo.apache.org/xml/ns/j2ee/application-2.0";
        }
        else if (uri.equals("http://geronimo.apache.org/xml/ns/j2ee/application-client-1.1")) {
            uri = "http://geronimo.apache.org/xml/ns/j2ee/application-client-2.0";
        }
        else if (uri.equals("http://geronimo.apache.org/xml/ns/deployment-1.1")) {
            uri = "http://geronimo.apache.org/xml/ns/deployment-1.2";
        }

        super.startElement(uri, localName, qname, atts);
    }
}
