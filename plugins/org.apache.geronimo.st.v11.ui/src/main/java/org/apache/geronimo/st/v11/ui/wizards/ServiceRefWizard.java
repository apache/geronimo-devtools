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
package org.apache.geronimo.st.v11.ui.wizards;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.naming.PortCompletionType;
import org.apache.geronimo.j2ee.naming.PortType;
import org.apache.geronimo.j2ee.naming.ServiceCompletionType;
import org.apache.geronimo.j2ee.naming.ServiceRefType;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.ui.wizards.AbstractTreeWizard;
import org.apache.geronimo.j2ee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.j2ee.jaxbmodel.operations.JAXBObjectFactoryImpl;

/**
 * @version $Rev$ $Date$
 */
public class ServiceRefWizard extends AbstractTreeWizard {

    private final int SERVICE_REF = 0;
    private final int PORT = 1;
    private final int PORT_COMPLETION = 2;

    public ServiceRefWizard(AbstractTreeSection section) {
        super(section, 3, 7);
        elementTypes[SERVICE_REF] = "Service Reference";
        elementTypes[PORT] = "Port";
        elementTypes[PORT_COMPLETION] = "Port Completion";
    }

    public class ServiceRefWizardPage extends AbstractTreeWizardPage {

        public ServiceRefWizardPage(String pageName) {
            super(pageName);
        }

        protected void initControl () {
            if (eObject == null) {
                element.select(SERVICE_REF);
                ServiceRefType serviceRef = (ServiceRefType)section.getSelectedObject();
                // use of ports and port completions are mutually exclusive
                if (serviceRef == null) {
                    element.setEnabled(false);
                } else if (serviceRef.getServiceCompletion() == null) {
                    element.remove(PORT_COMPLETION);
                } else {
                    element.remove(PORT);
                }
            }
            else {
                if (ServiceRefType.class.isInstance(eObject)) {
                    textList.get(0).setText(((ServiceRefType)eObject).getServiceRefName());
                    if (((ServiceRefType)eObject).getServiceCompletion() != null) {
                        textList.get(1).setText(((ServiceRefType)eObject).getServiceCompletion().getServiceName());
                    }
                    element.select(SERVICE_REF);
                }
                else if (PortType.class.isInstance(eObject)) {
                    textList.get(0).setText(((PortType)eObject).getPortName());
                    textList.get(1).setText(((PortType)eObject).getProtocol());
                    textList.get(2).setText(((PortType)eObject).getHost());
                    textList.get(3).setText(((PortType)eObject).getPort().toString());
                    textList.get(4).setText(((PortType)eObject).getUri());
                    textList.get(5).setText(((PortType)eObject).getCredentialsName());
                    element.select(PORT);
                }
                else if (PortCompletionType.class.isInstance(eObject)) {
                    textList.get(0).setText(((PortCompletionType)eObject).getPort().getPortName());
                    textList.get(1).setText(((PortCompletionType)eObject).getPort().getProtocol());
                    textList.get(2).setText(((PortCompletionType)eObject).getPort().getHost());
                    textList.get(3).setText(((PortCompletionType)eObject).getPort().getPort().toString());
                    textList.get(4).setText(((PortCompletionType)eObject).getPort().getUri());
                    textList.get(5).setText(((PortCompletionType)eObject).getPort().getCredentialsName());
                    textList.get(6).setText(((PortCompletionType)eObject).getBindingName());
                    element.select(PORT_COMPLETION);
                }
                element.setEnabled(false);
            }
        }
        
        protected void toggleFields (boolean clearFields) {
            if (element.getText().equals(elementTypes[SERVICE_REF])) {
                for (int i = 0; i < maxTextFields; i++) {
                    labelList.get(i).setVisible(i < 2 ? true : false);
                    textList.get(i).setVisible(i < 2 ? true : false);
                    if (clearFields == true) {
                        textList.get(i).setText("");
                    }
                }
                labelList.get(0).setText(CommonMessages.name);
                labelList.get(1).setText(CommonMessages.serviceCompletionName);
                // if we are doing an add, then we need to make sure that the longest
                // text can be handled
                labelList.get(2).setText(CommonMessages.protocol);
                labelList.get(3).setText(CommonMessages.hostName);
                labelList.get(4).setText(CommonMessages.portValue);
                labelList.get(5).setText(CommonMessages.credential);
                labelList.get(6).setText(CommonMessages.bindingName);
            }
            else if (element.getText().equals(elementTypes[PORT])) {
                for (int i = 0; i < maxTextFields; i++) {
                    labelList.get(i).setVisible(i < 6 ? true : false);
                    textList.get(i).setVisible(i < 6 ? true : false);
                    if (clearFields == true) {
                        textList.get(i).setText("");
                    }
                }
                labelList.get(0).setText(CommonMessages.name);
                labelList.get(1).setText(CommonMessages.protocol);
                labelList.get(2).setText(CommonMessages.hostName);
                labelList.get(3).setText(CommonMessages.portValue);
                labelList.get(4).setText(CommonMessages.uri);
                labelList.get(5).setText(CommonMessages.credential);
            }
            else if (element.getText().equals(elementTypes[PORT_COMPLETION])) {
                for (int i = 0; i < maxTextFields; i++) {
                    labelList.get(i).setVisible(true);
                    textList.get(i).setVisible(true);
                    if (clearFields == true) {
                        textList.get(i).setText("");
                    }
                }
                labelList.get(0).setText(CommonMessages.name);
                labelList.get(1).setText(CommonMessages.protocol);
                labelList.get(2).setText(CommonMessages.hostName);
                labelList.get(3).setText(CommonMessages.portValue);
                labelList.get(4).setText(CommonMessages.uri);
                labelList.get(5).setText(CommonMessages.credential);
                labelList.get(6).setText(CommonMessages.bindingName);
            }
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_ServiceRef;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_ServiceRef;
        }
    }

    @Override
    public void addPages() {
        addPage(new ServiceRefWizardPage("Page0"));
    }

    @Override
    public boolean performFinish() {
        ServiceRefType serviceRef;
        if (element.getText().equals(elementTypes[SERVICE_REF])) {
            if (isEmpty(textList.get(0).getText())) {
                return false;
            }
            serviceRef = (ServiceRefType)eObject;
            if (serviceRef == null) {
                serviceRef = (ServiceRefType)getEFactory().create(ServiceRefType.class);
                JAXBElement plan = section.getPlan();
                JAXBModelUtils.getServiceRefs(plan).add(serviceRef);
            }
            serviceRef.setServiceRefName(textList.get(0).getText());
            if (isEmpty(textList.get(1).getText())) {
                serviceRef.setServiceCompletion(null);
            }
            else {
                ServiceCompletionType serviceComp = serviceRef.getServiceCompletion();
                if (serviceComp == null) {
                    serviceComp = (ServiceCompletionType)getEFactory().create(ServiceCompletionType.class);
                    serviceRef.setServiceCompletion(serviceComp);
                }
                serviceRef.getServiceCompletion().setServiceName(textList.get(1).getText());
            }
        }
        else if (element.getText().equals(elementTypes[PORT])) {
            if (isEmpty(textList.get(0).getText()) || isEmpty(textList.get(4).getText())) {
                return false;
            }
            PortType port = (PortType)eObject;
            if (port == null) {
                port = (PortType)getEFactory().create(PortType.class);
                serviceRef = (ServiceRefType)section.getSelectedObject();
                serviceRef.getPort().add(port);
            }
            port.setPortName(textList.get(0).getText());
            port.setProtocol(textList.get(1).getText());
            port.setHost(textList.get(2).getText());
            port.setPort(Integer.valueOf(textList.get(3).getText()));
            port.setUri(textList.get(4).getText());
            port.setCredentialsName(textList.get(5).getText());
        }
        else if (element.getText().equals(elementTypes[PORT_COMPLETION])) { 
            if (isEmpty(textList.get(0).getText()) || isEmpty(textList.get(4).getText()) ||
                isEmpty(textList.get(6).getText())) {
                return false;
            }
            PortCompletionType portComp = (PortCompletionType)eObject;
            if (portComp == null) {
                portComp = (PortCompletionType)getEFactory().create(PortCompletionType.class);
                serviceRef = (ServiceRefType)section.getSelectedObject();
                serviceRef.getServiceCompletion().getPortCompletion().add(portComp);
                PortType port = (PortType)getEFactory().create(PortType.class);
                portComp.setPort (port);
            }
            portComp.getPort().setPortName(textList.get(0).getText());
            portComp.getPort().setProtocol(textList.get(1).getText());
            portComp.getPort().setHost(textList.get(2).getText());
            portComp.getPort().setPort(Integer.valueOf(textList.get(3).getText()));
            portComp.getPort().setUri(textList.get(4).getText());
            portComp.getPort().setCredentialsName(textList.get(5).getText());
            portComp.setBindingName(textList.get(6).getText());
        }
        return true;
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_ServiceRef;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_ServiceRef;
    }
}
