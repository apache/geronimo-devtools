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

package org.apache.geronimo.jee.jaxbmodel.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * @version $Rev$ $Date$ 
 */
public class NamespacePrefix extends NamespacePrefixMapper{

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
        prefixMap.put("http://geronimo.apache.org/xml/ns/loginconfig-2.0", "log");
    }
	
	public static void processPrefix( Node parent ) {
		NodeList nl = parent.getChildNodes();
		
		if ( parent instanceof Element ) {
			updatePrefix( (Element)parent );
		}
		
		for ( int i = 0; i <= nl.getLength(); i ++ ) {
			Node node = nl.item(i);
			if ( node instanceof Element ) {
				processPrefix( node );
			}
		}

	}
	
	private static void updatePrefix( Element element ) {
		NamedNodeMap mnm = element.getAttributes();
		
		ArrayList<Attr> attributes = new ArrayList<Attr>();
		for ( int j = 0; j <= mnm.getLength(); j ++ ) { 
			Attr attr = (Attr)mnm.item(j);
			if ( attr != null && attr.getOwnerElement() != null && getPrefix( attr.getNodeValue() ) != null ) {
				attributes.add((Attr)attr.cloneNode(false));
			}
		}
		for ( int j = 0; j < attributes.size(); j ++ ) {
			Attr tempAttr = attributes.get(j);
			Attr attr = element.getAttributeNode(tempAttr.getName());
			Element owner = (Element)attr.getOwnerElement();
			owner.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + getPrefix( attr.getNodeValue() ), attr.getNodeValue());
			owner.removeAttributeNode(attr);
		}
		String prefix = getPrefix( element.getNamespaceURI() );
		if ( prefix != null ) {
		    element.setPrefix( prefix );
		}
	}

	private static String getPrefix(String namespaceURI) {
        if (prefixMap.containsKey(namespaceURI))
            return prefixMap.get(namespaceURI);
		return null;
	}

	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {

        if (prefixMap.containsKey(namespaceUri))
            return prefixMap.get(namespaceUri);

        return suggestion;
    }

}
