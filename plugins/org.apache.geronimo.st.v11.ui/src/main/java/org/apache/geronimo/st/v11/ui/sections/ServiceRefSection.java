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
package org.apache.geronimo.st.v11.ui.sections;

import java.util.List;

import javax.xml.bind.JAXBElement;


import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.v11.ui.Activator;
import org.apache.geronimo.st.v11.ui.wizards.ServiceRefWizard;
import org.apache.geronimo.xml.ns.naming_1.PortCompletionType;
import org.apache.geronimo.xml.ns.naming_1.PortType;
import org.apache.geronimo.xml.ns.naming_1.ServiceRefType;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class ServiceRefSection extends AbstractTreeSection {
    public ServiceRefSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, List serviceRefs) {
        super(plan, parent, toolkit, style);
        this.objectContainer = serviceRefs;
        createClient();
    }

    @Override
    public String getTitle() {
        return CommonMessages.editorServiceRefTitle;
    }

    @Override
    public String getDescription() {
        return CommonMessages.editorServiceRefDescription;
    }

    @Override
    public Wizard getWizard() {
        return new ServiceRefWizard(this);
    }

    @Override
    public Class getTableEntryObjectType() {
        return ServiceRefType.class;
    }

    @Override
    protected void activateAddButton() {
        if (tree.getSelectionCount() == 0 || tree.getSelection()[0].getParentItem() == null) {
            addButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
        }
    }

    public ServiceRefType getSelectedObject () {
        if (tree.getSelection().length == 0) {
            return null;
        }
        return (ServiceRefType)tree.getSelection()[0].getData();
    }
    
    @Override
    public void removeItem(Object anItem) {
        if (ServiceRefType.class.isInstance(anItem)) {
            getObjectContainer().remove(anItem);
        }
        else if (PortType.class.isInstance(anItem)) {
        	ServiceRefType serviceRef = (ServiceRefType)tree.getSelection()[0].getParentItem().getData();
            serviceRef.getPort().remove(anItem);
        }
        else if (PortCompletionType.class.isInstance(anItem)) {
        	ServiceRefType serviceRef = (ServiceRefType)tree.getSelection()[0].getParentItem().getData();
            serviceRef.getServiceCompletion().getPortCompletion().remove(anItem);
        }
    }
    
    @Override
    public Object getInput() {
        if (objectContainer != null) {
            return objectContainer;
        }
        return super.getInput();
    }

    @Override
    public ITreeContentProvider getContentProvider() {
        return new ContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                return getChildren(inputElement);
            }

            @Override
            public Object[] getChildren(Object parentElement) {
                if (List.class.isInstance(parentElement)) {
                    return ((List)parentElement).toArray();
                }
                if (ServiceRefType.class.isInstance(parentElement)) {
                    ServiceRefType serviceRef = (ServiceRefType)parentElement;
                    Object[] portList = serviceRef.getPort().toArray();
                    Object[] compList = new Object[0];
                    if (serviceRef.getServiceCompletion() != null) {
                        compList = serviceRef.getServiceCompletion().getPortCompletion().toArray();
                    }
                    Object[] fullList = new Object[portList.length + compList.length];
                    System.arraycopy(portList, 0, fullList, 0, portList.length);
                    System.arraycopy(compList, 0, fullList, portList.length, compList.length);
                    return fullList;
                }
                return new String[] {};
            }
        };
    }

    @Override
    public ILabelProvider getLabelProvider() {
        return new LabelProvider() {
            @Override
            public String getText(Object element) {
                if (ServiceRefType.class.isInstance(element)) {
                	ServiceRefType serviceRef = (ServiceRefType)element;
                        String retString = "Service Ref: name = \"" + serviceRef.getServiceRefName() + "\"";
                        if (serviceRef.getServiceCompletion() != null) {
                            retString += ", service completion name = \"" + serviceRef.getServiceCompletion().getServiceName() + "\"";
                        }
                        return retString;
                }
                else if (PortType.class.isInstance(element)) {
                	PortType port = (PortType)element;
                    return "Port: name = \"" + port.getPortName() + 
                            "\", protocol = \"" + port.getProtocol() + 
                            "\", host = \"" + port.getHost() + 
                            "\", port = \"" + port.getPort() + 
                            "\", uri = \"" + port.getUri() + 
                            "\", credential = \"" + port.getCredentialsName() + "\"";
                }
                else if (PortCompletionType.class.isInstance(element)) {
                	PortCompletionType portComp = (PortCompletionType)element;
                    return "Port Completion: name = \"" + portComp.getPort().getPortName() + 
                            "\", protocol = \"" + portComp.getPort().getProtocol() + 
                            "\", host = \"" + portComp.getPort().getHost() + 
                            "\", port = \"" + portComp.getPort().getPort() + 
                            "\", uri = \"" + portComp.getPort().getUri() + 
                            "\", credential = \"" + portComp.getPort().getCredentialsName() +
                            "\", binding name = \"" + portComp.getBindingName() + "\"";
                }

                return null;
            }

            @Override
            public Image getImage(Object arg0) {
                return Activator.imageDescriptorFromPlugin("org.eclipse.jst.j2ee",
                        "icons/full/obj16/module_web_obj.gif").createImage();
            }
        };
    }
}
