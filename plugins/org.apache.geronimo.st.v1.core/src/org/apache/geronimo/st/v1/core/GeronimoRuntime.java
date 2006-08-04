/**
 *  Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.st.v1.core;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.geronimo.deployment.xmlbeans.XmlBeansUtil;
import org.apache.geronimo.schema.SchemaConversionUtils;
import org.apache.geronimo.st.core.GenericGeronimoServerRuntime;
import org.apache.geronimo.xbeans.geronimo.GerConnectorDocument;
import org.apache.geronimo.xbeans.geronimo.GerConnectorType;
import org.apache.geronimo.xbeans.geronimo.j2ee.GerApplicationDocument;
import org.apache.geronimo.xbeans.geronimo.j2ee.GerApplicationType;
import org.apache.geronimo.xbeans.geronimo.web.GerWebAppDocument;
import org.apache.geronimo.xbeans.geronimo.web.GerWebAppType;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.eclipse.core.resources.IFile;
import org.openejb.xbeans.ejbjar.OpenejbOpenejbJarDocument;
import org.openejb.xbeans.ejbjar.OpenejbOpenejbJarType;

public class GeronimoRuntime extends GenericGeronimoServerRuntime {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoRuntime#fixGeronimoConnectorSchema(org.eclipse.core.resources.IFile)
	 */
	public XmlObject fixGeronimoConnectorSchema(IFile plan) throws XmlException {
		return SchemaConversionUtils.fixGeronimoSchema(getXmlObject(plan), GerConnectorDocument.type.getDocumentElementName(), GerConnectorType.type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoRuntime#fixGeronimoEarSchema(org.eclipse.core.resources.IFile)
	 */
	public XmlObject fixGeronimoEarSchema(IFile plan) throws XmlException {
		return SchemaConversionUtils.fixGeronimoSchema(getXmlObject(plan), GerApplicationDocument.type.getDocumentElementName(), GerApplicationType.type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoRuntime#fixGeronimoEjbSchema(org.eclipse.core.resources.IFile)
	 */
	public XmlObject fixGeronimoEjbSchema(IFile plan) throws XmlException {
		return SchemaConversionUtils.fixGeronimoSchema(getXmlObject(plan), OpenejbOpenejbJarDocument.type.getDocumentElementName(), OpenejbOpenejbJarType.type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoRuntime#fixGeronimoWebSchema(org.eclipse.core.resources.IFile)
	 */
	public XmlObject fixGeronimoWebSchema(IFile plan) throws XmlException {
		return SchemaConversionUtils.fixGeronimoSchema(getXmlObject(plan), GerWebAppDocument.type.getDocumentElementName(), GerWebAppType.type);
	}

	private XmlObject getXmlObject(IFile plan) {
		if (plan.exists()) {
			try {
				return XmlBeansUtil.parse(plan.getLocation().toFile().toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
