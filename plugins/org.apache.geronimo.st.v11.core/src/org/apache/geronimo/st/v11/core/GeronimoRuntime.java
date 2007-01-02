/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.geronimo.st.v11.core;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.geronimo.deployment.xmlbeans.XmlBeansUtil;
import org.apache.geronimo.schema.SchemaConversionUtils;
import org.apache.geronimo.st.core.GeronimoRuntimeDelegate;
import org.apache.geronimo.xbeans.geronimo.GerConnectorDocument;
import org.apache.geronimo.xbeans.geronimo.GerConnectorType;
import org.apache.geronimo.xbeans.geronimo.j2ee.GerApplicationDocument;
import org.apache.geronimo.xbeans.geronimo.j2ee.GerApplicationType;
import org.apache.geronimo.xbeans.geronimo.web.GerWebAppDocument;
import org.apache.geronimo.xbeans.geronimo.web.GerWebAppType;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.openejb.xbeans.ejbjar.OpenejbOpenejbJarDocument;
import org.openejb.xbeans.ejbjar.OpenejbOpenejbJarType;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoRuntime extends GeronimoRuntimeDelegate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoRuntime#fixGeronimoConnectorSchema(org.eclipse.core.resources.IFile)
	 */
	public XmlObject fixGeronimoConnectorSchema(IFile plan) throws XmlException {
            XmlObject xmlplan = getXmlObject(plan);
            if (plan != null) {
                SchemaConversionUtils.fixGeronimoSchema(xmlplan, GerConnectorDocument.type.getDocumentElementName(),
                    GerConnectorType.type);
                save(xmlplan, plan);
            }	
            return xmlplan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoRuntime#fixGeronimoEarSchema(org.eclipse.core.resources.IFile)
	 */
	public XmlObject fixGeronimoEarSchema(IFile plan) throws XmlException {
            XmlObject xmlplan = getXmlObject(plan);
            if (plan != null) {
                SchemaConversionUtils.fixGeronimoSchema(xmlplan, GerApplicationDocument.type.getDocumentElementName(),
                    GerApplicationType.type);
                save(xmlplan, plan);
            }
            return xmlplan;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoRuntime#fixGeronimoEjbSchema(org.eclipse.core.resources.IFile)
	 */
	public XmlObject fixGeronimoEjbSchema(IFile plan) throws XmlException {
            XmlObject xmlplan = getXmlObject(plan);
            if (plan != null) {
                SchemaConversionUtils.fixGeronimoSchema(xmlplan, OpenejbOpenejbJarDocument.type.getDocumentElementName(),
                    OpenejbOpenejbJarType.type);
                save(xmlplan, plan);
            }
            return xmlplan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoRuntime#fixGeronimoWebSchema(org.eclipse.core.resources.IFile)
	 */
	public XmlObject fixGeronimoWebSchema(IFile plan) throws XmlException {
            XmlObject xmlplan = getXmlObject(plan);
            if (plan != null) {
                SchemaConversionUtils.fixGeronimoSchema(xmlplan, GerWebAppDocument.type.getDocumentElementName(),
                    GerWebAppType.type);
                save(xmlplan, plan);
            }
            return xmlplan; 
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

 	private void save(XmlObject object, IFile file) {
        try {
 	        object.save(file.getLocation().toFile());
 	        file.refreshLocal(IFile.DEPTH_ONE, null);
 	    } catch (IOException e) {
 	          e.printStackTrace();
 	    } catch (CoreException e) {
 		     e.printStackTrace();
 	    }
 	}
}
