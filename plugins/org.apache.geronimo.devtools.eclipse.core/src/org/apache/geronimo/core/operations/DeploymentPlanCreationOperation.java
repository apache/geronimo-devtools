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
package org.apache.geronimo.core.operations;

import java.io.IOException;
import java.util.Collections;

import org.apache.geronimo.core.internal.GeronimoSchemaNS;
import org.apache.geronimo.core.internal.GeronimoUtils;
import org.apache.geronimo.xml.ns.j2ee.application.ApplicationFactory;
import org.apache.geronimo.xml.ns.j2ee.application.ApplicationType;
import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorFactory;
import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorType;
import org.apache.geronimo.xml.ns.web.DocumentRoot;
import org.apache.geronimo.xml.ns.web.WebAppType;
import org.apache.geronimo.xml.ns.web.WebFactory;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.openejb.xml.ns.openejb.jar.JarFactory;
import org.openejb.xml.ns.openejb.jar.OpenejbJarType;

public class DeploymentPlanCreationOperation extends
        AbstractGeronimoJ2EEComponentOperation {

    public DeploymentPlanCreationOperation() {
    }

    public DeploymentPlanCreationOperation(IDataModel model) {
        super(model);
    }

    public IStatus execute(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {

        if (isGeronimoRuntimeTarget()) {

            IVirtualComponent comp = ComponentCore.createComponent(
                    getProject(), getComponentName());

            if (comp.getComponentTypeId().equals(
                    IModuleConstants.JST_WEB_MODULE)) {
                createGeronimoWebDeploymentPlan(GeronimoUtils
                        .getWebDeploymentPlanFile(comp));
            } else if (comp.getComponentTypeId().equals(
                    IModuleConstants.JST_EJB_MODULE)) {
                createOpenEjbDeploymentPlan(GeronimoUtils
                        .getOpenEjbDeploymentPlanFile(comp));
            } else if (comp.getComponentTypeId().equals(
                    IModuleConstants.JST_EAR_MODULE)) {
                createGeronimoApplicationDeploymentPlan(GeronimoUtils
                        .getApplicationDeploymentPlanFile(comp));
            } else if (comp.getComponentTypeId().equals(
                    IModuleConstants.JST_CONNECTOR_MODULE)) {
                createConnectorDeploymentPlan(GeronimoUtils
                        .getConnectorDeploymentPlanFile(comp));
            }
        }

        return Status.OK_STATUS;
    }

    public ApplicationType createGeronimoApplicationDeploymentPlan(IFile dpFile) {
        URI uri = URI
                .createPlatformResourceURI(dpFile.getFullPath().toString());

        ResourceSet resourceSet = new ResourceSetImpl();
        GeronimoUtils.registerAppFactoryAndPackage(resourceSet);

        Resource resource = resourceSet.createResource(uri);
        org.apache.geronimo.xml.ns.j2ee.application.DocumentRoot documentRoot = ApplicationFactory.eINSTANCE
                .createDocumentRoot();
        ApplicationType root = ApplicationFactory.eINSTANCE
                .createApplicationType();

        EMap map = documentRoot.getXMLNSPrefixMap();
        map.put("", GeronimoSchemaNS.GERONIMO_APP_NS);
        map.put("sec", GeronimoSchemaNS.GERONIMO_SECURITY_NS);
        map.put("sys", GeronimoSchemaNS.GERONIMO_DEPLOYMENT_NS);

        root.setApplicationName(getComponentName());
        root.setConfigId(getProject().getName() + "/" + getComponentName());

        documentRoot.setApplication(root);
        resource.getContents().add(documentRoot);

        doSave(resource);

        return root;
    }

    public WebAppType createGeronimoWebDeploymentPlan(IFile dpFile) {

        URI uri = URI
                .createPlatformResourceURI(dpFile.getFullPath().toString());

        ResourceSet resourceSet = new ResourceSetImpl();
        GeronimoUtils.registerWebFactoryAndPackage(resourceSet);

        Resource resource = resourceSet.createResource(uri);
        DocumentRoot documentRoot = WebFactory.eINSTANCE.createDocumentRoot();

        EMap map = documentRoot.getXMLNSPrefixMap();
        map.put("", GeronimoSchemaNS.GERONIMO_WEB_NS);
        map.put("sec", GeronimoSchemaNS.GERONIMO_SECURITY_NS);
        map.put("nam", GeronimoSchemaNS.GERONIMO_NAMING_NS);
        map.put("sys", GeronimoSchemaNS.GERONIMO_DEPLOYMENT_NS);

        WebAppType root = WebFactory.eINSTANCE.createWebAppType();

        root.setConfigId(getProject().getName() + "/" + getComponentName());
        root.setContextRoot("/" + getComponentName());
        root.setContextPriorityClassloader(false);

        documentRoot.setWebApp(root);
        resource.getContents().add(documentRoot);

        doSave(resource);

        return root;
    }

    public OpenejbJarType createOpenEjbDeploymentPlan(IFile dpFile) {
        URI uri = URI
                .createPlatformResourceURI(dpFile.getFullPath().toString());

        ResourceSet resourceSet = new ResourceSetImpl();
        GeronimoUtils.registerEjbFactoryAndPackage(resourceSet);

        Resource resource = resourceSet.createResource(uri);
        org.openejb.xml.ns.openejb.jar.DocumentRoot documentRoot = JarFactory.eINSTANCE
                .createDocumentRoot();
        OpenejbJarType root = JarFactory.eINSTANCE.createOpenejbJarType();

        EMap map = documentRoot.getXMLNSPrefixMap();
        map.put("", GeronimoSchemaNS.GERONIMO_OPENEJB_NS);
        map.put("sec", GeronimoSchemaNS.GERONIMO_SECURITY_NS);
        map.put("nam", GeronimoSchemaNS.GERONIMO_NAMING_NS);
        map.put("sys", GeronimoSchemaNS.GERONIMO_DEPLOYMENT_NS);
        map.put("pkgen", GeronimoSchemaNS.GERONIMO_PKGEN_NS);

        root.setConfigId(getProject().getName() + "/" + getComponentName());

        documentRoot.setOpenejbJar(root);
        resource.getContents().add(documentRoot);

        doSave(resource);

        return root;
    }

    public ConnectorType createConnectorDeploymentPlan(IFile dpFile) {
        URI uri = URI
                .createPlatformResourceURI(dpFile.getFullPath().toString());

        ResourceSet resourceSet = new ResourceSetImpl();
        GeronimoUtils.registerEjbFactoryAndPackage(resourceSet);

        Resource resource = resourceSet.createResource(uri);
        org.apache.geronimo.xml.ns.j2ee.connector.DocumentRoot documentRoot = ConnectorFactory.eINSTANCE
                .createDocumentRoot();
        ConnectorType root = ConnectorFactory.eINSTANCE.createConnectorType();

        EMap map = documentRoot.getXMLNSPrefixMap();
        map.put("", GeronimoSchemaNS.GERONIMO_CONNECTOR_NS);
        map.put("nam", GeronimoSchemaNS.GERONIMO_NAMING_NS);
        map.put("sys", GeronimoSchemaNS.GERONIMO_DEPLOYMENT_NS);

        root.setConfigId(getProject().getName() + "/" + getComponentName());

        documentRoot.setConnector(root);
        resource.getContents().add(documentRoot);

        doSave(resource);

        return root;
    }

    private void doSave(Resource resource) {
        if (resource instanceof XMLResource) {
            ((XMLResource) resource).setEncoding("UTF-8");
        }

        try {
            resource.save(Collections.EMPTY_MAP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
