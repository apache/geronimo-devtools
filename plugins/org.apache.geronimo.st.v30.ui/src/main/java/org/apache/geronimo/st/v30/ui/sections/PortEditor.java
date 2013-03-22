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
package org.apache.geronimo.st.v30.ui.sections;

import org.apache.geronimo.st.v30.core.GeronimoServerDelegate;
import org.apache.geronimo.st.v30.ui.NumericVerifyListener;
import org.apache.geronimo.st.v30.ui.internal.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.server.core.IServerWorkingCopy;

public abstract class PortEditor {
    
    private IServerWorkingCopy server;
    private Text portOffset;
    private Text httpPort;
    private Text rmiPort;
    private Label rmiPortLabel;
    private Label httpPortLabel;

    public PortEditor(IServerWorkingCopy server) { 
        this.server = server;
    }
    
    public void init(Composite composite) {

        final GeronimoServerDelegate delegate = getGeronimoServer();
        
        // ------- Label and text field for the port offset -------
        createLabel(composite, Messages.portOffset);

        portOffset = createText(composite, String.valueOf(delegate.getPortOffset()), SWT.BORDER);
        portOffset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        portOffset.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if (portOffset.getData() == null) {
                    setPortOffset(portOffset);
                } else {
                    portOffset.setData(null);
                }
                httpPortLabel.setText(getActualHttpPortText());
                rmiPortLabel.setText(getActualRmiPortText());
            }
        });
        portOffset.addVerifyListener(new NumericVerifyListener());
        
        createLabel(composite, "");
        
        
        // ------- Label and text field for the http port -------
        createLabel(composite, Messages.httpPort);

        httpPort = createText(composite, delegate.getHTTPPort(), SWT.BORDER);
        httpPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        httpPort.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if (httpPort.getData() == null) {
                    setHttpPort(httpPort);
                } else {
                    httpPort.setData(null);
                }
                httpPortLabel.setText(getActualHttpPortText());
            }
        });
        httpPort.addVerifyListener(new NumericVerifyListener());
        
        httpPortLabel = createItalicLabel(composite, getActualHttpPortText());
        httpPortLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        
        // ------- Label and text field for the rmi port -------
        createLabel(composite, Messages.rmiPort);

        rmiPort = createText(composite, delegate.getRMINamingPort(), SWT.BORDER);
        rmiPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        rmiPort.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if (rmiPort.getData() == null) {
                    setRmiPort(rmiPort);
                } else {
                    rmiPort.setData(null);
                }
                rmiPortLabel.setText(getActualRmiPortText());
            }
        });
        rmiPort.addVerifyListener(new NumericVerifyListener());
        
        rmiPortLabel = createItalicLabel(composite, getActualRmiPortText());
        rmiPortLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    }

    private String getActualRmiPortText() {
        GeronimoServerDelegate delegate = getGeronimoServer();
        return "Actual: " + delegate.getActualRMINamingPort() + "  ";
    }
    
    private String getActualHttpPortText() {
        GeronimoServerDelegate delegate = getGeronimoServer();
        return "Actual: " + delegate.getActualHTTPPort() + "  ";
    }
    
    protected abstract void setPortOffset(Text portOffset);
    
    protected abstract void setRmiPort(Text rmiPort);

    protected abstract void setHttpPort(Text httpPort);
    
    

    protected Label createItalicLabel(Composite parent, String text) {
        Label l =  createLabel(parent, text);
        FontData fontData = l.getFont().getFontData()[0];
        Font font = new Font(l.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.ITALIC));
        l.setFont(font);
        return l;
    }
    
    protected abstract Label createLabel(Composite parent, String text);
    
    protected abstract Text createText(Composite parent, String value, int style);
     
    protected GeronimoServerDelegate getGeronimoServer() {
        GeronimoServerDelegate gs = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
        if (gs == null)
            gs = (GeronimoServerDelegate) server.loadAdapter(GeronimoServerDelegate.class, null);
        return gs;
    }

}   
