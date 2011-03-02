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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jaxbmodel.common.Activator;
import org.apache.geronimo.jaxbmodel.common.internal.Trace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IRuntime;

/**
 * @version $Rev$ $Date$
 */
public class JAXBUtils {

    private static Map<String,IJAXBUtilsProvider> providers = new HashMap<String,IJAXBUtilsProvider>();
    
	static {
		loadExtensionPoints();
	}
	
	private static synchronized void loadExtensionPoints() {
        Trace.tracePoint("ENTRY", "JAXBUtils.loadExtensionPoints");

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(Activator.PLUGIN_ID, "JAXBUtilsProvider");
		for (int i = 0; i < cf.length; i++) {
			IConfigurationElement element = cf[i];
			if ("provider".equals(element.getName())) {
				try {
					IJAXBUtilsProvider provider = (IJAXBUtilsProvider) element.createExecutableExtension("class");
					String versions = element.getAttribute("version");
					String[] versionArray = versions.split(",");
					for (int j=0;j<versionArray.length;j++) {
						providers.put(versionArray[j], provider);
					}
				} catch (CoreException e) {
                    Trace.tracePoint("CoreException", "JAXBUtils.loadExtensionPoints");
					e.printStackTrace();
				}
			}
		}

        Trace.tracePoint("EXIT", "JAXBUtils.loadExtensionPoints");
	}
    
    public static List<JAXBContext> getJAXBContext() {
    	List<JAXBContext> contextList = new ArrayList<JAXBContext>();
    	
    	Collection<IJAXBUtilsProvider> jaxbutils =  providers.values();
    	Iterator<IJAXBUtilsProvider> iterator = jaxbutils.iterator();
    	while (iterator.hasNext()){
    		IJAXBUtilsProvider provider = iterator.next();
    		contextList.add(provider.getJAXBContext());
    	}
    	return contextList;
    }
    
    public static IJAXBUtilsProvider getProvider(IFile plan) {
        Trace.tracePoint("ENTRY", "JAXBUtils.getProvider");

        IJAXBUtilsProvider provider = null;
        if (plan != null) {
				IProject project = plan.getProject();				
				try {
					IFacetedProject fp = ProjectFacetsManager.create(project);
					if (fp == null) return null;
					IRuntime runtime = FacetUtil.getRuntime(fp.getPrimaryRuntime());
					if (runtime == null) return null;
					String version = runtime.getRuntimeType().getVersion();
					provider = (IJAXBUtilsProvider) providers.get(version);
				} catch (CoreException e) {
                    Trace.tracePoint("CoreException", "JAXBUtils.getProvider");
					e.printStackTrace();
				} catch (IllegalArgumentException ie) {
                    Trace.tracePoint("IllegalArgumentException", "JAXBUtils.getProvider");
				    throw new IllegalArgumentException("The project [" + project.getName() + "] does not have a Targeted Runtime specified.");
                }
        }
        
        Trace.tracePoint("EXIT", "JAXBUtils.getProvider", provider);
		return provider;
	}

    public static IJAXBUtilsProvider getProvider(String version) {
        return providers.get(version);
    }
    
    public static void marshalDeploymentPlan(JAXBElement jaxbElement, IFile file) throws Exception {
        IJAXBUtilsProvider provider = getProvider(file);
        provider.marshalDeploymentPlan(jaxbElement, file);
    }

    public static JAXBElement unmarshalFilterDeploymentPlan(IFile file) throws Exception {
        IJAXBUtilsProvider provider = getProvider(file);
    	return provider.unmarshalFilterDeploymentPlan(file);
    }

//    public static void marshalPlugin(JAXBElement jaxbElement, OutputStream outputStream) throws Exception {
//        //currently only JAXB21Utils provide this method,so invoke it directly
//    	providers.get("2.1").marshalPlugin(jaxbElement, outputStream);
//    }
//
//    public static JAXBElement unmarshalPlugin(InputStream inputStream) {
//    	//currently only JAXB21Utils provide this method,so invoke it directly
//    	return providers.get("2.1").unmarshalPlugin(inputStream);
//    }

    public static Object getValue( Object element, String name ) throws Exception {
        try {
            if (String.class.isInstance(element))
                return (String)element;
            Method method = element.getClass().getMethod( "get" + name, null);
            return method.invoke(element, null);
        } catch ( Exception e ) {
            throw e;
        }
    }
    
    public static void setValue( Object element, String name, Object value ) throws Exception {
        try {
            Method[] methods = element.getClass().getMethods();
            for ( Method method: methods) {
                if ( method.getName().equals( "set" + name ) ) {
                    method.invoke( element, value );
                    return;
                }
            }
        } catch (Exception e) {
            throw e;
        }
        System.out.println( "============== No such method set" + name + " in class " + element.getClass().getName() );
    }
}
