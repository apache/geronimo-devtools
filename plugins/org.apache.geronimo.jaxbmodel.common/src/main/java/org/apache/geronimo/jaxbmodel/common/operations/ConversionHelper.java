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

package org.apache.geronimo.jaxbmodel.common.operations;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.geronimo.jaxbmodel.common.Activator;
import org.apache.geronimo.jaxbmodel.common.internal.Trace;
import org.eclipse.core.resources.IFile;

/**
 * <strong>ConversionHelper</strong> is a helper class with numerous static
 * methods to aid in the conversion of Geronimo-specific deployment plans from 
 * one JAXB version to another (e.g., v1.1 to v2.1)<p> 
 * 
 * @version $Rev$ $Date$ 
 */
public class ConversionHelper {

    /**
     * Convert a geronimo-web.xml deployment plan file (if necessary)
     * and return the JAXB representation
     * 
     * @param plan Geronimo deployment plan
     * @throws Exception 
     */
    public static void convertGeronimoWebFile( IFile plan ) throws Exception {
        Trace.tracePoint("Entry", Activator.traceOperations, "ConversionHelper.convertGeronimoWebFile", plan);

        convertNamespace( plan );

        Trace.tracePoint("Exit ", Activator.traceOperations, "ConversionHelper.convertGeronimoWebFile");
    }


    /**
     * Convert an openejb-jar.xml deployment plan file (if necessary) and return the
     * JAXB representation 
     * 
     * @param plan OpenEJB deployment plan
     * @throws Exception 
     */
    public static void convertOpenEjbJarFile( IFile plan ) throws Exception {
        Trace.tracePoint("Entry", Activator.traceOperations, "ConversionHelper.convertGeronimoOpenEjbFile", plan);

        convertNamespace( plan );

        Trace.tracePoint("Exit ", Activator.traceOperations, "ConversionHelper.convertGeronimoOpenEjbFile");
    }


    /**
     * Convert a geronimo-application.xml deployment plan file (if necessary)
     * and return the JAXB representation
     * 
     * @param plan Geronimo deployment plan
     * 
     * @exception JAXBException if JAXB error
     */
    public static void convertGeronimoApplicationFile( IFile plan ) throws Exception {
        Trace.tracePoint("Entry", Activator.traceOperations, "ConversionHelper.convertGeronimoApplicationFile", plan);

        convertNamespace( plan );

        Trace.tracePoint("Exit ", Activator.traceOperations, "ConversionHelper.convertGeronimoApplicationFile");
    }


    /**
     * Convert a geronimo-ra.xml deployment plan file (if necessary)
     * and return the JAXB representation
     * 
     * @param plan Geronimo deployment plan
     * 
     * @exception JAXBException if JAXB error
     */
    public static void convertGeronimoRaFile( IFile plan ) throws Exception {
        Trace.tracePoint("Entry", Activator.traceOperations, "ConversionHelper.convertGeronimoRaFile", plan);

        convertNamespace( plan );

        Trace.tracePoint("Exit ", Activator.traceOperations, "ConversionHelper.convertGeronimoRaFile");
    }


    /**
     * Convert a geronimo-application-client.xml deployment plan file
     * and return the JAXB representation
     * 
     * @param plan Geronimo deployment plan
     * 
     * @exception JAXBException if JAXB error
     */
    public static void convertGeronimoApplicationClientFile( IFile plan ) throws Exception {
        Trace.tracePoint("Entry", Activator.traceOperations, "ConversionHelper.convertGeronimoApplicationClientFile", plan);

        convertNamespace( plan );

        Trace.tracePoint("Exit ", Activator.traceOperations, "ConversionHelper.convertGeronimoApplicationClientFile");
    }


    /*------------------------------------------------------------------------*\
    |                                                                          |
    |  Private method(s)                                                       | 
    |                                                                          |
    \*------------------------------------------------------------------------*/


    /**
     * Convert the namespace of the Geronimo deployment plan and then save the 
     * deployment plan 
     * 
     * @param plan Geronimo deployment plan
     */
    private static void convertNamespace( IFile plan ) throws Exception{

        // 
        // Unmarshall and filter the deployment plan 
        // 
        JAXBElement jaxbElement = JAXBUtils.unmarshalFilterDeploymentPlan(plan);

        // 
        // Marshall and save the deployment plan from the jaxbElement
        // 
        JAXBUtils.marshalDeploymentPlan( jaxbElement, plan );
    }
}