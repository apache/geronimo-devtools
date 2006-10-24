/**
 * Copyright 2004, 2005 The Apache Software Foundation or its licensors, as applicable
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
package org.apache.geronimo.st.core;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.server.core.IJavaRuntime;

public interface IGeronimoRuntime extends IJavaRuntime {
	
	public XmlObject fixGeronimoEarSchema(IFile plan) throws XmlException;
	
	public XmlObject fixGeronimoWebSchema(IFile plan) throws XmlException;
	
	public XmlObject fixGeronimoEjbSchema(IFile plan) throws XmlException;
	
	public XmlObject fixGeronimoConnectorSchema(IFile plan) throws XmlException;
	
	public IPath getRuntimeSourceLocation();

}
