package org.apache.geronimo.st.ui.refactoring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WebDeploymentPlanHandler extends DefaultHandler {
	private String file;
	private Locator locator;
	private List<WebTextNode> nodeList = new ArrayList<WebTextNode>();

	public List<WebTextNode> getNodeList() {
		return nodeList;
	}

	public WebTextNode getContextRootTextNode() {
		for (WebTextNode n : nodeList) {
			if (n.getName().equals("context-root"))
				return n;
		}
		return null;
	}

	public WebTextNode getArtifactIdTextNode() {
		for (WebTextNode n : nodeList) {
			if (n.getName().equals("artifactId"))
				return n;
		}
		return null;
	}

	private static final int START = 0, IN_WEB_APP = 1, IN_CONTEXT_ROOT = 2,
			IN_ENVIRONMENT = 3, IN_MODULEID = 4, IN_ARTIFACTID = 5;

	private int state = START;

	public WebDeploymentPlanHandler(String file) {
		this.file = file;
	}

	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String value = null;
		WebTextNode wtn = null;
		switch (state) {
		case IN_CONTEXT_ROOT:
			value = new String(ch, start, length);
			wtn = new WebTextNode();
			try {
				int offset = getOffset(locator.getLineNumber(), locator
						.getColumnNumber());
				wtn.setName("context-root");
				wtn.setValue(value);
				wtn.setOffset(offset - length);
				nodeList.add(wtn);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case IN_ARTIFACTID:
			value = new String(ch, start, length);
			wtn = new WebTextNode();
			try {
				int offset = getOffset(locator.getLineNumber(), locator
						.getColumnNumber());
				wtn.setName("artifactId");
				wtn.setValue(value);
				wtn.setOffset(offset - length);
				nodeList.add(wtn);

			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		switch (state) {
		case START:
			if (localName.equals("web-app"))
				state = IN_WEB_APP;
			break;
		case IN_WEB_APP:
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
		case IN_WEB_APP:
			if (localName.equals("web-app"))
				state = START;
			break;
		case IN_ENVIRONMENT:
			if (localName.equals("environment"))
				state = IN_WEB_APP;
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
				state = IN_WEB_APP;
			break;
		default:
			break;
		}
	}

	// return the offset of the TextNode's end
	private int getOffset(int lineNumber, int columnNumber) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));

		if (lineNumber < 1 || columnNumber < 1)
			return -1;

		int current;
		int offset = 0;

		for (int i = 1; i < lineNumber;) {
			do {
				current = br.read();
				offset++;
			} while (current != '\n');
			i++;
		}
		offset += (columnNumber-1);

		br.close();

		return offset;
	}
}
