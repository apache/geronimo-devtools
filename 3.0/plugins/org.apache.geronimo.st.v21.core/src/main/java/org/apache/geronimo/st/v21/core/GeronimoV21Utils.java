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
package org.apache.geronimo.st.v21.core;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils;
import org.apache.geronimo.jee.application.Application;
import org.apache.geronimo.jee.connector.Connector;
import org.apache.geronimo.jee.deployment.Artifact;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.openejb.OpenejbJar;
import org.apache.geronimo.jee.web.WebApp;
import org.apache.geronimo.st.core.GeronimoUtils;
import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IModule;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoV21Utils extends GeronimoUtils {

    public static JAXBElement getDeploymentPlan(IFile file) throws Exception {
        Trace.tracePoint("ENTRY", Activator.traceCore, "GeronimoV21Utils.getDeploymentPlan", file);

        if (!file.exists()) {
            return null;
        }

        if (file.getName().equals(GeronimoUtils.APP_PLAN_NAME))
            return getApplicationDeploymentPlan(file);
        else if (file.getName().equals(GeronimoUtils.OPENEJB_PLAN_NAME))
            return getOpenEjbDeploymentPlan(file);
        else if (file.getName().equals(GeronimoUtils.WEB_PLAN_NAME))
            return getWebDeploymentPlan(file);
        else if (file.getName().equals(GeronimoUtils.CONNECTOR_PLAN_NAME))
            return getConnectorDeploymentPlan(file);
        else if (file.getName().equals(GeronimoUtils.APP_CLIENT_PLAN_NAME))
            return getApplicationClientDeploymentPlan(file);

        Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoV21Utils.getDeploymentPlan", null);
        return null;
    }

    //public static String getConfigId2(IModule module) {
    //    Trace.tracePoint("ENTRY", "GeronimoV21Utils.getConfigId2", module);

    //    IFile planFile = null;
    //   IVirtualComponent comp = ComponentCore.createComponent(module.getProject());
    //    if (isWebModule(module)) {
    //        planFile = GeronimoUtils.getWebDeploymentPlanFile(comp);
    //   }
    //    else if (isEjbJarModule(module)) {
    //        planFile = GeronimoUtils.getOpenEjbDeploymentPlanFile(comp);
    //    }
    //    else if (isEarModule(module)) {
    //       planFile = GeronimoUtils.getApplicationDeploymentPlanFile(comp);
    //    }
    //    else if (isRARModule(module)) {
    //        planFile = GeronimoUtils.getConnectorDeploymentPlanFile(comp);
    //    }

    //    if (planFile != null) {
    //       try {
    //            XmlObject xmlObject = XmlBeansUtil.parse(planFile.getLocation().toFile());
    //            XmlCursor cursor = xmlObject.newCursor();
    //            cursor.toFirstChild();
    //            xmlObject = cursor.getObject();
    //            XmlObject result[] = xmlObject.selectChildren(QNameSet.singleton(EnvironmentDocument.type.getDocumentElementName()));
    //            if (result != null && result.length > 0) {
    //                org.apache.geronimo.deployment.xbeans.EnvironmentType env = (org.apache.geronimo.deployment.xbeans.EnvironmentType) result[0].changeType(org.apache.geronimo.deployment.xbeans.EnvironmentType.type);
    //                org.apache.geronimo.deployment.xbeans.ArtifactType moduleId = env.getModuleId();
    //                Trace.tracePoint("EXIT", "GeronimoV21Utils.getConfigId2", getQualifiedConfigID(moduleId));
    //                return getQualifiedConfigID(moduleId);
    //            }
    //            else {
                    // 
                    // FIXME -- Once GERONIMODEVTOOLS-263 is resolved
                    // 
    //                String id = getConfigId(module);
    //                Trace.tracePoint("EXIT", "GeronimoV21Utils.getConfigId2", id);
    //                return id;
    //            }
    //        }
    //        catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        catch (XmlException e) {
    //            e.printStackTrace();
    //        }
    //    }

    //    Trace.tracePoint("EXIT", "GeronimoV21Utils.getConfigId2", null);
    //    return null;
    //}

    public static String getConfigId(IModule module) throws Exception {
        Trace.tracePoint("ENTRY", Activator.traceCore, "GeronimoV21Utils.getConfigId", module);

        Environment environment = null;
        if (isWebModule(module)) {
            if (getWebDeploymentPlan(module)!=null) {
            	WebApp plan = getWebDeploymentPlan(module).getValue();
            	if (plan != null)
            		environment = plan.getEnvironment();
            }
        }
        else if (isEjbJarModule(module)) {
        	   if (getOpenEjbDeploymentPlan(module)!=null) {
		            OpenejbJar plan = getOpenEjbDeploymentPlan(module).getValue();
		            if (plan != null)
		            	environment = plan.getEnvironment();
        	   }
        }
        else if (isEarModule(module)) {
        	if (getApplicationDeploymentPlan(module)!=null) {
        		Application plan = getApplicationDeploymentPlan(module).getValue();
        		if (plan != null)
        			environment = plan.getEnvironment();
        	}
        }
        else if (isRARModule(module)) {
        	if (getConnectorDeploymentPlan(module)!=null) {
	            Connector plan = getConnectorDeploymentPlan(module).getValue();
	            if (plan != null)
	                environment = plan.getEnvironment();
        	}
        }

        if (environment != null
            && environment.getModuleId() != null) {
            Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoV21Utils.getConfigId", getQualifiedConfigID(environment.getModuleId()));
            return getQualifiedConfigID(environment.getModuleId());
        }

        Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoV21Utils.getConfigId", getId(module));
        return getId(module);
    }

    public static String getQualifiedConfigID(Artifact artifact) {
        return getQualifiedConfigID(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getType());
    }

    //public static String getQualifiedConfigID(org.apache.geronimo.deployment.xbeans.ArtifactType artifact) {
    //    return getQualifiedConfigID(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getType());
    //}

    public static String getContextRoot(IModule module) throws Exception {
        String contextRoot = null;

        WebApp deploymentPlan = getWebDeploymentPlan(module).getValue();
        if (deploymentPlan != null)
            contextRoot = deploymentPlan.getContextRoot();

        if (contextRoot == null)
            contextRoot = GeronimoUtils.getContextRoot(module);

        return contextRoot;
    }

    public static JAXBElement<WebApp> getWebDeploymentPlan(IModule module) throws Exception {
        return getWebDeploymentPlan(getVirtualComponent(module));
    }

    public static JAXBElement<Application> getApplicationDeploymentPlan(IModule module) throws Exception {
        return getApplicationDeploymentPlan(getVirtualComponent(module));
    }

    public static JAXBElement<OpenejbJar> getOpenEjbDeploymentPlan(IModule module) throws Exception {
        return getOpenEjbDeploymentPlan(getVirtualComponent(module));
    }

    public static JAXBElement<Connector> getConnectorDeploymentPlan(IModule module) throws Exception {
        return getConnectorDeploymentPlan(getVirtualComponent(module));
    }

    public static JAXBElement getApplicationDeploymentPlan(IVirtualComponent comp) throws Exception {
        return getApplicationDeploymentPlan(getApplicationDeploymentPlanFile(comp));
    }

    public static JAXBElement getWebDeploymentPlan(IVirtualComponent comp) throws Exception {
        return getWebDeploymentPlan(getWebDeploymentPlanFile(comp));
    }

    public static JAXBElement getOpenEjbDeploymentPlan(IVirtualComponent comp) throws Exception {
        return getOpenEjbDeploymentPlan(getOpenEjbDeploymentPlanFile(comp));
    }

    public static JAXBElement getConnectorDeploymentPlan(IVirtualComponent comp) throws Exception {
        return getConnectorDeploymentPlan(getConnectorDeploymentPlanFile(comp));
    }

    public static JAXBElement getApplicationDeploymentPlan(IFile file) throws Exception {
        Trace.tracePoint("ENTRY", Activator.traceCore, "GeronimoV21Utils.getApplicationDeploymentPlan", file);

        if (file.getName().equals(APP_PLAN_NAME) && file.exists()) {
        	return JAXBUtils.unmarshalFilterDeploymentPlan(file);
        }

        Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoV21Utils.getApplicationDeploymentPlan", null);
        return null;
    }

    public static JAXBElement getApplicationClientDeploymentPlan(IFile file) throws Exception {
        Trace.tracePoint("ENTRY", Activator.traceCore, "GeronimoV21Utils.getApplicationClientDeploymentPlan", file);

        if (file.getName().equals(APP_CLIENT_PLAN_NAME) && file.exists()) {
        	return JAXBUtils.unmarshalFilterDeploymentPlan(file);
        }

        Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoV21Utils.getApplicationClientDeploymentPlan", null);
        return null;
    }

    public static JAXBElement getWebDeploymentPlan(IFile file) throws Exception {
        Trace.tracePoint("ENTRY", Activator.traceCore, "GeronimoV21Utils.getWebDeploymentPlan", file);

        if (file.getName().equals(WEB_PLAN_NAME) && file.exists()) {
        	return JAXBUtils.unmarshalFilterDeploymentPlan(file);
        }

        Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoV21Utils.getWebDeploymentPlan", null);
        return null;
    }

    public static JAXBElement getOpenEjbDeploymentPlan(IFile file) throws Exception {
        Trace.tracePoint("ENTRY", Activator.traceCore, "GeronimoV21Utils.getOpenEjbDeploymentPlan", file);

        if (file.getName().equals(OPENEJB_PLAN_NAME) && file.exists()) {
        	return JAXBUtils.unmarshalFilterDeploymentPlan(file);
        }

        Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoV21Utils.getOpenEjbDeploymentPlan", null);
        return null;
    }

    public static JAXBElement getConnectorDeploymentPlan(IFile file) throws Exception {
        Trace.tracePoint("ENTRY", Activator.traceCore, "GeronimoV21Utils.getConnectorDeploymentPlan", file);

        if (file.getName().equals(CONNECTOR_PLAN_NAME) && file.exists()) {
        	return JAXBUtils.unmarshalFilterDeploymentPlan(file);
        }

        Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoV21Utils.getConnectorDeploymentPlan", null);
        return null;
    }
    
}
