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
 * See the License for the specific  anguage governing permissions and
 * limitations under the License.
 */
package org.apache.geronimo.st.v30.core.jaxb;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;

import org.eclipse.core.resources.IFile;

/**
 * @version $Rev$ $Date$
 */
public class JAXBUtils {

    public static List<JAXBContext> getJAXBContext() {
        return org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils.getJAXBContext();
    }
       
    public static void marshalDeploymentPlan(JAXBElement jaxbElement, IFile file) throws Exception {
        org.apache.geronimo.jaxbmodel.common.operations.IJAXBUtilsProvider provider = org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils.getProvider(file);
        provider.marshalDeploymentPlan(jaxbElement, file);
    }

    public static JAXBElement unmarshalFilterDeploymentPlan(IFile file) throws Exception {
        org.apache.geronimo.jaxbmodel.common.operations.IJAXBUtilsProvider provider = org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils.getProvider(file);
        return provider.unmarshalFilterDeploymentPlan(file);
    }

    public static void marshalPlugin(JAXBElement jaxbElement, OutputStream outputStream) throws Exception {
        //for 3.0 jaxb provider,invoke it directly
        IJAXBUtilsProvider provider = (IJAXBUtilsProvider) org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils.getProvider("3.0");
        provider.marshalPlugin(jaxbElement, outputStream);
    }

    public static JAXBElement unmarshalPlugin(InputStream inputStream) {
        //for 3.0 jaxb provider,invoke it directly
        IJAXBUtilsProvider provider = (IJAXBUtilsProvider) org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils.getProvider("3.0");
    	return provider.unmarshalPlugin(inputStream);
    }

    public static Object getValue( Object element, String name ) throws Exception {
        return org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils.getValue(element, name);
    }
    
    public static void setValue( Object element, String name, Object value ) throws Exception {
        org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils.setValue(element, name, value);
    }
}
