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
package org.apache.geronimo.st.v21.ui.wizards;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.naming.Port;
import org.apache.geronimo.jee.naming.PortCompletion;
import org.apache.geronimo.jee.naming.ServiceCompletion;
import org.apache.geronimo.jee.naming.ServiceRef;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.ui.wizards.AbstractTreeWizard;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBObjectFactoryImpl;

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
                ServiceRef serviceRef = (ServiceRef)section.getSelectedObject();
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
                if (ServiceRef.class.isInstance(eObject)) {
                    textList.get(0).setText(((ServiceRef)eObject).getServiceRefName());
                    if (((ServiceRef)eObject).getServiceCompletion() != null) {
                        textList.get(1).setText(((ServiceRef)eObject).getServiceCompletion().getServiceName());
                    }
                    element.select(SERVICE_REF);
                }
                else if (Port.class.isInstance(eObject)) {
                    textList.get(0).setText(((Port)eObject).getPortName());
                    textList.get(1).setText(((Port)eObject).getProtocol());
                    textList.get(2).setText(((Port)eObject).getHost());
                    textList.get(3).setText(((Port)eObject).getPort().toString());
                    textList.get(4).setText(((Port)eObject).getUri());
                    textList.get(5).setText(((Port)eObject).getCredentialsName());
                    element.select(PORT);
                }
                else if (PortCompletion.class.isInstance(eObject)) {
                    textList.get(0).setText(((PortCompletion)eObject).getPort().getPortName());
                    textList.get(1).setText(((PortCompletion)eObject).getPort().getProtocol());
                    textList.get(2).setText(((PortCompletion)eObject).getPort().getHost());
                    textList.get(3).setText(((PortCompletion)eObject).getPort().getPort().toString());
                    textList.get(4).setText(((PortCompletion)eObject).getPort().getUri());
                    textList.get(5).setText(((PortCompletion)eObject).getPort().getCredentialsName());
                    textList.get(6).setText(((PortCompletion)eObject).getBindingName());
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
        ServiceRef serviceRef;
        if (element.getText().equals(elementTypes[SERVICE_REF])) {
            if (isEmpty(textList.get(0).getText())) {
                return false;
            }
            serviceRef = (ServiceRef)eObject;
            if (serviceRef == null) {
                serviceRef = (ServiceRef)getEFactory().create(ServiceRef.class);
                JAXBElement plan = section.getPlan();
                JAXBModelUtils.getServiceRefs(plan).add(serviceRef);
            }
            serviceRef.setServiceRefName(textList.get(0).getText());
            if (isEmpty(textList.get(1).getText())) {
                serviceRef.setServiceCompletion(null);
            }
            else {
                ServiceCompletion serviceComp = serviceRef.getServiceCompletion();
                if (serviceComp == null) {
                    serviceComp = (ServiceCompletion)getEFactory().create(ServiceCompletion.class);
                    serviceRef.setServiceCompletion(serviceComp);
                }
                serviceRef.getServiceCompletion().setServiceName(textList.get(1).getText());
            }
        }
        else if (element.getText().equals(elementTypes[PORT])) {
            if (isEmpty(textList.get(0).getText()) || isEmpty(textList.get(4).getText())) {
                return false;
            }
            Port port = (Port)eObject;
            if (port == null) {
                port = (Port)getEFactory().create(Port.class);
                serviceRef = (ServiceRef)section.getSelectedObject();
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
            PortCompletion portComp = (PortCompletion)eObject;
            if (portComp == null) {
                portComp = (PortCompletion)getEFactory().create(PortCompletion.class);
                serviceRef = (ServiceRef)section.getSelectedObject();
                serviceRef.getServiceCompletion().getPortCompletion().add(portComp);
                Port port = (Port)getEFactory().create(Port.class);
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
