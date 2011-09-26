/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.st.v30.ui.view;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.geronimo.st.v30.core.GeronimoServerDelegate;
import org.apache.geronimo.st.v30.ui.actions.SSHTerminalConnectAction;
import org.apache.geronimo.st.v30.ui.internal.Messages;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tm.internal.terminal.actions.TerminalActionDisconnect;
import org.eclipse.tm.internal.terminal.actions.TerminalActionScrollLock;
import org.eclipse.tm.internal.terminal.actions.TerminalActionToggleCommandInputField;
import org.eclipse.tm.internal.terminal.control.actions.TerminalActionClearAll;
import org.eclipse.tm.internal.terminal.control.actions.TerminalActionCopy;
import org.eclipse.tm.internal.terminal.control.actions.TerminalActionCut;
import org.eclipse.tm.internal.terminal.control.actions.TerminalActionPaste;
import org.eclipse.tm.internal.terminal.control.actions.TerminalActionSelectAll;
import org.eclipse.tm.internal.terminal.provisional.api.ISettingsStore;
import org.eclipse.tm.internal.terminal.provisional.api.ITerminalConnector;
import org.eclipse.tm.internal.terminal.provisional.api.TerminalState;
import org.eclipse.tm.internal.terminal.view.TerminalView;
import org.eclipse.tm.internal.terminal.view.TerminalViewControlDecorator;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;

/**
 * 
 *
 * @version $Rev$ $Date$
 */
@SuppressWarnings("restriction")
public class KarafShellSSHTerminalView extends TerminalView {
    private TerminalActionScrollLock fActionTerminalScrollLock;
    
    protected GeronimoServerDelegate currentServerDelegate;
    protected ISettingsStore settingsStore;
    protected TerminalViewControlDecorator fCtlDecorator = new TerminalViewControlDecorator();
    
    public static final String SSH_TYPE = "org.eclipse.tm.internal.terminal.ssh.SshConnector";
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);
        settingsStore = new SettingsStore(memento);
    }
    
    
    public ITerminalConnector getConnector() {
        ITerminalConnector[] connectors = fCtlTerminal.getConnectors();
        for(ITerminalConnector c : connectors) {
            if(c.getId().equals(SSH_TYPE)) return c;
        }
        return null;
    }
    
    
    public ISettingsStore getSettingsStore() {
        return settingsStore;
    }
    
    
    public synchronized void doConnect(GeronimoServerDelegate serverDelegate) {
        if (fCtlTerminal.getState()!=TerminalState.CLOSED) {// as only support one active terminal now, should close it first!
            doDisconnect();
        }
        /* Set current server delegate */
        this.currentServerDelegate = serverDelegate;
        
        String host = serverDelegate.getServer().getHost();
        String userName = serverDelegate.getAdminID();
        String password = serverDelegate.getAdminPassword();
        String port = Integer.toString(serverDelegate.getKarafShellPort());
        String timeout = Integer.toString(serverDelegate.getKarafShellTimeout());
        String keepalive = Integer.toString(serverDelegate.getKarafShellKeepAlive());
        
        this.settingsStore.put("Host", host);
        this.settingsStore.put("User", userName);
        this.settingsStore.put("Password", password);
        this.settingsStore.put("Port", port);
        this.settingsStore.put("Timeout", timeout);
        this.settingsStore.put("Keepalive", keepalive);
        
        ITerminalConnector connector = this.getConnector();
        if(connector != null) {
            connector.load(settingsStore);
            this.fCtlTerminal.setConnector(connector);
            this.fCtlTerminal.connectTerminal();
        }
    }
    
    public void doDisconnect() {
        this.onTerminalDisconnect();
    }
    
    @Override
    public void createPartControl(Composite wndParent) {
        super.createPartControl(wndParent);
        fCtlDecorator.setViewContoler(this.fCtlTerminal);
        setPartName(Messages.karafShellTerminalTitle);
    }
    protected void setupActions() {
        fActionTerminalConnect = new SSHTerminalConnectAction(this);
        fActionTerminalDisconnect = new TerminalActionDisconnect(this);
        fActionTerminalScrollLock = new TerminalActionScrollLock(this);
        fActionEditCopy = new TerminalActionCopy(fCtlDecorator);
        fActionEditCut = new TerminalActionCut(fCtlDecorator);
        fActionEditPaste = new TerminalActionPaste(fCtlDecorator);
        fActionEditClearAll = new TerminalActionClearAll(fCtlDecorator);
        fActionEditSelectAll = new TerminalActionSelectAll(fCtlDecorator);
        fActionToggleCommandInputField = new TerminalActionToggleCommandInputField(this);
    }
    protected void setupLocalToolBars() {
        IToolBarManager toolBarMgr = getViewSite().getActionBars().getToolBarManager();

        toolBarMgr.add(fActionTerminalConnect);
        toolBarMgr.add(fActionTerminalDisconnect);
        toolBarMgr.add(fActionTerminalScrollLock);
        toolBarMgr.add(fActionToggleCommandInputField);
    }
    protected void loadContextMenus(IMenuManager menuMgr) {
        menuMgr.add(fActionEditCopy);
        menuMgr.add(fActionEditPaste);
        menuMgr.add(new Separator());
        menuMgr.add(fActionEditClearAll);
        menuMgr.add(fActionEditSelectAll);
        menuMgr.add(new Separator());
        menuMgr.add(fActionToggleCommandInputField);

        // Other plug-ins can contribute there actions here
        menuMgr.add(new Separator("Additions")); //$NON-NLS-1$
    }
    
    public void updateStatus() {
        updateTerminalConnect();
        updateTerminalDisconnect();
        updateTerminalSettings();
        fActionToggleCommandInputField.setChecked(hasCommandInputField());
        fActionTerminalScrollLock.setChecked(isScrollLock());
        updateSummary();
    }
    /* copy from org.eclipse.tm.internal.terminal.view.TerminalView */
    private void setViewSummary(String summary) {
        setContentDescription(summary);
        getViewSite().getActionBars().getStatusLineManager().setMessage(
                summary);
        setTitleToolTip(getPartName()+": "+summary); //$NON-NLS-1$
        
    }
    
    public void updateSummary() {
        String summary = Messages.karafShellNoConnection;
        if(fCtlTerminal.getTerminalConnector() != null && fCtlTerminal.getTerminalConnector().isInitialized()) {
            summary = this.currentServerDelegate.getAdminID() + "@" + this.currentServerDelegate.getServer().getId() + ":" 
                + this.currentServerDelegate.getKarafShellPort();
        }
        setViewSummary(summary);
    }
    /**
     * Copy from org.eclipse.tm.internal.terminal.view.SettingsStore
     * 
     * @version $Rev$ $Date$
     */
    public class SettingsStore implements ISettingsStore {

        private static final String KEYS = "_keys_"; //$NON-NLS-1$
        @SuppressWarnings("rawtypes")
        final private Map fMap=new HashMap();
        @SuppressWarnings("unchecked")
        public SettingsStore(IMemento memento) {
            if(memento==null)
                return;
            // load all keys ever used from the memento
            String keys=memento.getString(KEYS);
            if(keys!=null) {
                String[] keyNames=keys.split(","); //$NON-NLS-1$
                for (int i = 0; i < keyNames.length; i++) {
                    String key=keyNames[i];
                    if(!KEYS.equals(key)) {
                        // get the dot separated elements
                        String[] path=key.split("\\."); //$NON-NLS-1$
                        IMemento m=memento;
                        // iterate over all but the last segment and get the children...
                        for(int iPath=0; m!=null && iPath+1<path.length; iPath++) {
                            m=m.getChild(path[iPath]);
                        }
                        if(m!=null) {
                            // cache the value in the map
                            fMap.put(key,m.getString(path[path.length-1]));
                        }
                    }
                }
            }
        }

        public String get(String key) {
            return get(key,null);
        }
        public String get(String key, String defaultValue) {
            String value = (String) fMap.get(key);
            if ((value == null) || (value.equals(""))) //$NON-NLS-1$
                return defaultValue;

            return value;
        }

        @SuppressWarnings("unchecked")
        public void put(String key, String value) {
            if(!key.matches("^[\\w.]+$")) //$NON-NLS-1$
                throw new IllegalArgumentException("Key '"+key+"' is not alpha numeric or '.'!"); //$NON-NLS-1$ //$NON-NLS-2$
            // null values remove the key from the map
            if ((value == null) || (value.equals(""))) //$NON-NLS-1$
                fMap.remove(key);
            else
                fMap.put(key, value);
        }
        /**
         * Save the state into memento.
         * 
         * @param memento Memento to save state into.
         */
        @SuppressWarnings("unchecked")
        public void saveState(IMemento memento) {
            String[] keyNames=(String[]) fMap.keySet().toArray(new String[fMap.size()]);
            Arrays.sort(keyNames);
            StringBuffer buffer=new StringBuffer();
            for (int i = 0; i < keyNames.length; i++) {
                String key=keyNames[i];
                String[] path=key.split("\\."); //$NON-NLS-1$
                IMemento m=memento;
                // iterate over all but the last segment and get the children...
                for(int iPath=0; iPath+1<path.length; iPath++) {
                    IMemento child=m.getChild(path[iPath]);
                    // if the child does not exist, create it
                    if(child==null)
                        child=m.createChild(path[iPath]);
                    m=child;
                }
                // use the last element in path as key of the child memento
                m.putString(path[path.length-1], (String) fMap.get(key));
                // construct the string for the keys
                if(i>0)
                    buffer.append(","); //$NON-NLS-1$
                buffer.append(key);
            }
            // save the keys we have used.
            memento.putString(KEYS, buffer.toString());
        }
    }
    public GeronimoServerDelegate getCurrentServerDelegate() {
        return currentServerDelegate;
    }
    
}
