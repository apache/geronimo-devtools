/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.geronimo.st.ui.refactoring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A SAX Handler for geronimo deployment plan
 */
public class DeploymentPlanHandler extends DefaultHandler {
	protected String file;
	protected List<DeploymentPlanTextNode> nodeList = new ArrayList<DeploymentPlanTextNode>();

	protected static final int START = 0, IN_ROOT_ELEMENT = 1,
			IN_CONTEXT_ROOT = 2, IN_ENVIRONMENT = 3, IN_MODULEID = 4,
			IN_ARTIFACTID = 5;

	protected int state = START;

	public DeploymentPlanHandler(String file) {
		this.file = file;
	}

	public List<DeploymentPlanTextNode> getNodeList() {
		return nodeList;
	}

	public int getNodeOffset(String nodeName) {
		for (DeploymentPlanTextNode n : nodeList) {
			if (n.getName().equals(nodeName))
				return n.getOffset();
		}
		return -1;
	}

	public String getNodeValue(String nodeName) {
		for (DeploymentPlanTextNode n : nodeList) {
			if (n.getName().equals(nodeName))
				return n.getValue();
		}
		return null;
	}
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		DeploymentPlanTextNode wtn = null;
		String name = null;
		
		switch (state) {
			case IN_CONTEXT_ROOT:
				name = DeploymentPlanTextNode.CONTEXT_ROOT;
				break;
			case IN_ARTIFACTID:
				name = DeploymentPlanTextNode.ARTIFACT_ID;
				break;
			default:
				return;
		}
		
		String value = new String(ch, start, length);
		wtn = new DeploymentPlanTextNode();
		
		wtn.setName(name);
		wtn.setValue(value);
		try {
			//ch doesn't contains XML declare statement at the beginning of deployment plan
			//get the character number of first line
			int xmlDeclareLength = getXMLDeclareLength();
			wtn.setOffset(xmlDeclareLength + start - 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		nodeList.add(wtn);	
		
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		switch (state) {
		case START:
			if (localName.equals("web-app") || localName.equals("openejb-jar")
					|| localName.equals("connector")
					|| localName.equals("application"))
				state = IN_ROOT_ELEMENT;
			break;
		case IN_ROOT_ELEMENT:
			if (localName.equals("environment"))
				state = IN_ENVIRONMENT;
			else if (localName.equals("context-root"))
				state = IN_CONTEXT_ROOT;
			break;
		case IN_ENVIRONMENT:
			if (localName.equals("moduleId"))
				state = IN_MODULEID;
			break;
		case IN_MODULEID:
			if (localName.equals("artifactId"))
				state = IN_ARTIFACTID;
			break;
		default:
			break;
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		switch (state) {
		case IN_ROOT_ELEMENT:
			if (localName.equals("web-app") || localName.equals("openejb-jar")
					|| localName.equals("connector")
					|| localName.equals("application"))
				state = START;
			break;
		case IN_ENVIRONMENT:
			if (localName.equals("environment"))
				state = IN_ROOT_ELEMENT;
			break;
		case IN_MODULEID:
			if (localName.equals("moduleId"))
				state = IN_ENVIRONMENT;
			break;
		case IN_ARTIFACTID:
			if (localName.equals("artifactId"))
				state = IN_MODULEID;
			break;
		case IN_CONTEXT_ROOT:
			if (localName.equals("context-root"))
				state = IN_ROOT_ELEMENT;
			break;
		default:
			break;
		}
	}

	// return the character number of first line in deployment plan
	protected int getXMLDeclareLength()
			throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));

		int current;
		int offset = 0;

		do {
			current = br.read();
			offset++;
		} while (current != '\n');

		br.close();

		return offset;
	}
}
