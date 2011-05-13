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
package org.apache.geronimo.st.v21.ui.sections;

import java.util.List;

import javax.xml.bind.JAXBElement;


import org.apache.geronimo.jee.naming.Port;
import org.apache.geronimo.jee.naming.PortCompletion;
import org.apache.geronimo.jee.naming.ServiceRef;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.v21.ui.Activator;
import org.apache.geronimo.st.v21.ui.wizards.ServiceRefWizard;
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
        return ServiceRef.class;
    }

    @Override
    protected void activateAddButton() {
        if (tree.getSelectionCount() == 0 || tree.getSelection()[0].getParentItem() == null) {
            addButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
        }
    }

    public ServiceRef getSelectedObject () {
        if (tree.getSelection().length == 0) {
            return null;
        }
        return (ServiceRef)tree.getSelection()[0].getData();
    }
    
    @Override
    public void removeItem(Object anItem) {
        if (ServiceRef.class.isInstance(anItem)) {
            getObjectContainer().remove(anItem);
        }
        else if (Port.class.isInstance(anItem)) {
            ServiceRef serviceRef = (ServiceRef)tree.getSelection()[0].getParentItem().getData();
            serviceRef.getPort().remove(anItem);
        }
        else if (PortCompletion.class.isInstance(anItem)) {
            ServiceRef serviceRef = (ServiceRef)tree.getSelection()[0].getParentItem().getData();
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
                if (ServiceRef.class.isInstance(parentElement)) {
                    ServiceRef serviceRef = (ServiceRef)parentElement;
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
                if (ServiceRef.class.isInstance(element)) {
                    ServiceRef serviceRef = (ServiceRef)element;
                    	String retString = Messages.serviceReference + ": " + Messages.name + " = \"" + serviceRef.getServiceRefName() + "\"";
                        if (serviceRef.getServiceCompletion() != null) {
                        	retString += ", " + Messages.serviceCompletionName + " = \"" + serviceRef.getServiceCompletion().getServiceName() + "\"";
                        }
                        return retString;
                }
                else if (Port.class.isInstance(element)) {
                    Port port = (Port)element;
                    return Messages.portName + " = \"" + port.getPortName() + 
                    "\", " + Messages.protocol + " = \"" + port.getProtocol() + 
                    "\", " + Messages.host + " = \"" + port.getHost() + 
                    "\", " + Messages.port + " = \"" + port.getPort() + 
                    "\", " + Messages.uri + " = \"" + port.getUri() + 
                    "\", " + Messages.credential + " = \"" + port.getCredentialsName() + "\"";

                }
                else if (PortCompletion.class.isInstance(element)) {
                    PortCompletion portComp = (PortCompletion)element;
                    return Messages.portCompletionName + " = \"" + portComp.getPort().getPortName() + 
                            "\", " + Messages.protocol + " = \"" + portComp.getPort().getProtocol() + 
                            "\", " + Messages.host + " = \"" + portComp.getPort().getHost() + 
                            "\", " + Messages.port + " = \"" + portComp.getPort().getPort() + 
                            "\", " + Messages.uri + " = \"" + portComp.getPort().getUri() + 
                            "\", " + Messages.credential + " = \"" + portComp.getPort().getCredentialsName() +
                            "\", " + Messages.bindingName + "  = \"" + portComp.getBindingName() + "\"";
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
