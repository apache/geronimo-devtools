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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.geronimo.jee.application.Application;
import org.apache.geronimo.jee.connector.Connector;
import org.apache.geronimo.jee.deployment.Artifact;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.openejb.OpenejbJar;
import org.apache.geronimo.jee.web.WebApp;
import org.apache.geronimo.st.core.GeronimoUtils;
import org.apache.geronimo.st.core.internal.Trace;
//port org.apache.geronimo.st.v21.core.jaxb.JAXBModelUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IModule;

/**
 * @version $Rev: 533836 $ $Date: 2007-04-30 15:39:14 -0400 (Mon, 30 Apr 2007) $
 */
public class GeronimoV21Utils extends GeronimoUtils {

    public static JAXBElement getDeploymentPlan(IFile file) {
        Trace.tracePoint("ENTRY", "GeronimoV21Utils.getDeploymentPlan", file);

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

        Trace.tracePoint("EXIT", "GeronimoV21Utils.getDeploymentPlan", null);
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

    public static String getConfigId(IModule module) {
        Trace.tracePoint("ENTRY", "GeronimoV21Utils.getConfigId", module);

        Environment environment = null;
        if (isWebModule(module)) {
            WebApp plan = getWebDeploymentPlan(module).getValue();
            if (plan != null)
                environment = plan.getEnvironment();
        }
        else if (isEjbJarModule(module)) {
            OpenejbJar plan = getOpenEjbDeploymentPlan(module).getValue();
//            if (plan != null)
//                environment = plan.getEnvironment();
        }
        else if (isEarModule(module)) {
            Application plan = getApplicationDeploymentPlan(module).getValue();
            if (plan != null)
                environment = plan.getEnvironment();
        }
        else if (isRARModule(module)) {
            Connector plan = getConnectorDeploymentPlan(module).getValue();
            if (plan != null)
                environment = plan.getEnvironment();
        }

        if (environment != null
            && environment.getModuleId() != null) {
            Trace.tracePoint("EXIT", "GeronimoV21Utils.getConfigId", getQualifiedConfigID(environment.getModuleId()));
            return getQualifiedConfigID(environment.getModuleId());
        }

        Trace.tracePoint("EXIT", "GeronimoV21Utils.getConfigId", getId(module));
        return getId(module);
    }

    public static String getQualifiedConfigID(Artifact artifact) {
        return getQualifiedConfigID(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getType());
    }

    //public static String getQualifiedConfigID(org.apache.geronimo.deployment.xbeans.ArtifactType artifact) {
    //    return getQualifiedConfigID(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getType());
    //}

    public static String getContextRoot(IModule module) {
        String contextRoot = null;

        WebApp deploymentPlan = getWebDeploymentPlan(module).getValue();
        if (deploymentPlan != null)
            contextRoot = deploymentPlan.getContextRoot();

        if (contextRoot == null)
            contextRoot = GeronimoUtils.getContextRoot(module);

        return contextRoot;
    }

    public static JAXBElement<WebApp> getWebDeploymentPlan(IModule module) {
        return getWebDeploymentPlan(getVirtualComponent(module));
    }

    public static JAXBElement<Application> getApplicationDeploymentPlan(IModule module) {
        return getApplicationDeploymentPlan(getVirtualComponent(module));
    }

    public static JAXBElement<OpenejbJar> getOpenEjbDeploymentPlan(IModule module) {
        return getOpenEjbDeploymentPlan(getVirtualComponent(module));
    }

    public static JAXBElement<Connector> getConnectorDeploymentPlan(IModule module) {
        return getConnectorDeploymentPlan(getVirtualComponent(module));
    }

    public static JAXBElement getApplicationDeploymentPlan(IVirtualComponent comp) {
        return getApplicationDeploymentPlan(getApplicationDeploymentPlanFile(comp));
    }

    public static JAXBElement getWebDeploymentPlan(IVirtualComponent comp) {
        return getWebDeploymentPlan(getWebDeploymentPlanFile(comp));
    }

    public static JAXBElement getOpenEjbDeploymentPlan(IVirtualComponent comp) {
        return getOpenEjbDeploymentPlan(getOpenEjbDeploymentPlanFile(comp));
    }

    public static JAXBElement getConnectorDeploymentPlan(IVirtualComponent comp) {
        return getConnectorDeploymentPlan(getConnectorDeploymentPlanFile(comp));
    }

    public static JAXBElement getApplicationDeploymentPlan(IFile file) {
        Trace.tracePoint("ENTRY", "GeronimoV21Utils.getApplicationDeploymentPlan", file);

        if (file.getName().equals(APP_PLAN_NAME) && file.exists()) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.jee.web:" +
                                                                   "org.apache.geronimo.jee.application:" +
                                                                   "org.apache.geronimo.jee.deployment:" +
                                                                   "org.apache.geronimo.jee.naming:" +
                                                                   "org.apache.geronimo.jee.security", Activator.class.getClassLoader() );
                Unmarshaller ums = jaxbContext.createUnmarshaller();
                JAXBElement plan = (JAXBElement)ums.unmarshal( file.getContents() );
                return plan;
            } catch ( JAXBException e ) {
                e.printStackTrace();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            return null;
        }

        Trace.tracePoint("EXIT", "GeronimoV21Utils.getApplicationDeploymentPlan", null);
        return null;
    }

    public static JAXBElement getWebDeploymentPlan(IFile file) {
        Trace.tracePoint("ENTRY", "GeronimoV21Utils.getWebDeploymentPlan", file);

        if (file.getName().equals(WEB_PLAN_NAME) && file.exists()) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.jee.web:" +
                                                                   "org.apache.geronimo.jee.naming:" +
                                                                   "org.apache.geronimo.jee.application:" +
                                                                   "org.apache.geronimo.jee.deployment", Activator.class.getClassLoader() );
                Unmarshaller ums = jaxbContext.createUnmarshaller();
                JAXBElement plan = (JAXBElement)ums.unmarshal( file.getContents() );
                return plan;
            } catch ( JAXBException e ) {
                e.printStackTrace();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            return null;
        }

        Trace.tracePoint("EXIT", "GeronimoV21Utils.getWebDeploymentPlan", null);
        return null;
    }

    public static JAXBElement getOpenEjbDeploymentPlan(IFile file) {
        Trace.tracePoint("ENTRY", "GeronimoV21Utils.getOpenEjbDeploymentPlan", file);

        if (file.getName().equals(OPENEJB_PLAN_NAME) && file.exists()) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.jee.openejb:" +
                                                                   "org.apache.geronimo.jee.web:" +									
                                                                   "org.apache.geronimo.jee.application:" +
                                                                   "org.apache.geronimo.jee.deployment:" +
                                                                   "org.apache.geronimo.jee.naming:" +
                                                                   "org.apache.geronimo.jee.security", Activator.class.getClassLoader() );
                Unmarshaller ums = jaxbContext.createUnmarshaller();
                JAXBElement plan = (JAXBElement)ums.unmarshal( file.getContents() );
                return plan;
            } catch ( JAXBException e ) {
                e.printStackTrace();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            return null;
        }

        Trace.tracePoint("EXIT", "GeronimoV21Utils.getOpenEjbDeploymentPlan", null);
        return null;
    }

    public static JAXBElement getConnectorDeploymentPlan(IFile file) {
        Trace.tracePoint("ENTRY", "GeronimoV21Utils.getConnectorDeploymentPlan", file);

        if (file.getName().equals(CONNECTOR_PLAN_NAME) && file.exists()) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.jee.connector:" +
                                                                   "org.apache.geronimo.jee.web:" +			
                                                                   "org.apache.geronimo.jee.application:" +
                                                                   "org.apache.geronimo.jee.deployment:" +
                                                                   "org.apache.geronimo.jee.naming:" +
                                                                   "org.apache.geronimo.jee.security", Activator.class.getClassLoader() );
                Unmarshaller ums = jaxbContext.createUnmarshaller();
                JAXBElement plan = (JAXBElement)ums.unmarshal( file.getContents() );
                return plan;
            } catch ( JAXBException e ) {
                e.printStackTrace();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            return null;
        }

        Trace.tracePoint("EXIT", "GeronimoV21Utils.getConnectorDeploymentPlan", null);
        return null;
    }
    
}
