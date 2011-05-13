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
package org.apache.geronimo.st.v30.ui.internal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.geronimo.st.v30.core.GeronimoRuntimeDelegate;
import org.apache.geronimo.st.v30.ui.Activator;
import org.apache.geronimo.st.v30.ui.CommonMessages;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstall2;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.window.Window;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.target.provisional.ITargetDefinition;
import org.eclipse.pde.internal.core.target.provisional.ITargetHandle;
import org.eclipse.pde.internal.core.target.provisional.ITargetPlatformService;
import org.eclipse.pde.internal.core.target.provisional.LoadTargetDefinitionJob;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.internal.browser.WorkbenchBrowserSupport;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.TaskModel;
import org.eclipse.wst.server.core.model.RuntimeDelegate;
import org.eclipse.wst.server.ui.internal.SWTUtil;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;
import org.eclipse.wst.server.ui.wizard.WizardFragment;
import org.osgi.framework.Bundle;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoRuntimeWizardFragment extends WizardFragment {

    // serverName-N.N or serverName-N.N.N
    public static final Pattern SERVER_NAME_VERSION_PATTERN = Pattern.compile("(.*-)((\\d+\\.\\d+)(\\.(\\d+))?)");
    
    private static final String GERONIMO_SERVER_LOCATION_VARIABLE_NAME = "geronimo30_server_location";
    
    private static final String  LOCAL_TARGET_DIRECTORY = ".metadata/.plugins/org.eclipse.pde.core/.local_targets/";
    
    private static final String TARGET_FILE_EXTENSION = ".target";
    
    private static final String TARGET_TEMPLATE_FILE = "targets/Geronimo30.target";
    
    private static final String TARGET_FILE_LOCATIONS_PLACEHOLDER = "@locations_placeholder@";
    
    private static final String[] bundleDirectories = { "repository/org/apache/geronimo/specs",
        "repository/org/apache/geronimo/javamail/geronimo-javamail_1.4_mail",
        "repository/org/apache/geronimo/bundles/jaxb-impl", "repository/org/apache/geronimo/bundles/jstl",
        "repository/org/apache/myfaces/core/myfaces-bundle",
        "repository/org/apache/geronimo/framework/geronimo-jdbc",
        "repository/org/osgi"};
    
    private static final String LOCATION_LINE_TEMPLATE = "<location path=\"${geronimo30_server_location}GERONIMO_BUNDLE_FOLDER_NAME\" type=\"Directory\"/>";

    private GeronimoRuntimeDelegate geronimoRuntime;

    protected Text installDir;

    private IWizardHandle fWizard;

    protected List<IVMInstall> installedJREs;

    protected String[] jreNames;

    protected Combo combo;

    public GeronimoRuntimeWizardFragment() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.ui.wizard.WizardFragment#hasComposite()
     */
    public boolean hasComposite() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.wst.server.ui.wizard.WizardFragment#createComposite(org.eclipse
     * .swt.widgets.Composite, org.eclipse.wst.server.ui.wizard.IWizardHandle)
     */
    public Composite createComposite(Composite parent, IWizardHandle handle) {
        this.fWizard = handle;
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout grid = new GridLayout(1, false);
        grid.marginWidth = 0;
        container.setLayout(grid);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        handle.setImageDescriptor(Activator.getImageDescriptor((Activator.IMG_WIZ_GERONIMO)));
        handle.setTitle(Messages.bind(Messages.runtimeWizardTitle, getRuntimeName()));
        String name = getGeronimoRuntime().getRuntime().getRuntimeType().getName();
        //handle.setDescription(Messages.bind(Messages.runtimeWizardDescription, name));
        createContent(container, handle);
        return container;
    }

    public void createContent(Composite parent, IWizardHandle handle) {

        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3, false);
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        addJRESelection(composite);
        addInstallDirSection(composite);
        addDownloadServerSection(composite);
    }

    protected void addDownloadServerSection(final Composite composite) {
        FormText downloadServerText = new FormText(composite, SWT.WRAP);
        IRuntime runtime = getRuntimeDelegate().getRuntime();
        String runtimeName = runtime.getRuntimeType().getName();
        String text = "<form>"
                + Messages.bind(Messages.DownloadServerText,
                        Messages.DownloadServerURL, runtimeName) + "</form>";
        downloadServerText.setText(text, true, true);
        GridData data = new GridData();
        data.horizontalSpan = 3;
        downloadServerText.setLayoutData(data);
        downloadServerText.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(HyperlinkEvent hyperlinkEvent) {
                String url = hyperlinkEvent.getHref().toString();
                Trace.trace(Trace.INFO, "Hyperlink " + url + ".");
                try {
                    int style = IWorkbenchBrowserSupport.AS_EXTERNAL
                            | IWorkbenchBrowserSupport.STATUS;
                    IWebBrowser browser = WorkbenchBrowserSupport.getInstance()
                            .createBrowser(style, Messages.downloadServer,
                                    Messages.getServer, Messages.tooltip);
                    browser.openURL(new URL(url));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected GeronimoRuntimeDelegate getGeronimoRuntime() {
        if (geronimoRuntime == null)
            geronimoRuntime = (GeronimoRuntimeDelegate) getRuntimeDelegate().getRuntime().loadAdapter(
                    GeronimoRuntimeDelegate.class, null);
        return geronimoRuntime;
    }

    protected void addInstallDirSection(Composite composite) {
        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.installDir);
        GridData data = new GridData();
        data.horizontalSpan = 3;
        label.setLayoutData(data);
        String tooltipLoc = Messages.bind(Messages.tooltipLoc, getRuntimeName());
        label.setToolTipText(tooltipLoc);

        installDir = new Text(composite, SWT.BORDER);

        IPath currentLocation = getRuntimeDelegate().getRuntimeWorkingCopy().getLocation();
        if (currentLocation != null) {
            installDir.setText(currentLocation.toOSString());
        }

        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        installDir.setLayoutData(data);
        installDir.setToolTipText(tooltipLoc);
        installDir.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getRuntimeDelegate().getRuntimeWorkingCopy().setLocation(new Path(installDir.getText()));
                validate();
            }
        });

        final Composite browseComp = composite;
        Button browse = SWTUtil.createButton(composite, Messages.browse);
        browse.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent se) {
                DirectoryDialog dialog = new DirectoryDialog(browseComp.getShell());
                dialog.setMessage(Messages.installDir);
                dialog.setFilterPath(installDir.getText());
                String selectedDirectory = dialog.open();
                if (selectedDirectory != null)
                    installDir.setText(selectedDirectory);
            }
        });
    }

    protected void addJRESelection(final Composite composite) {
        updateJREs();

        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.installedJRE);
        label.setLayoutData(new GridData());

        combo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setItems(jreNames);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        combo.setLayoutData(data);

        combo.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                // if the first item in the list is selected, then pass null
                // to setVMInstall to use the default JRE.
                // otherwise the array list of JRE's is one off from what is
                // in the combo; subtract 1 from the selection to get the
                // correct JRE.
                int sel = combo.getSelectionIndex();
                IVMInstall vmInstall = null;
                if (sel > 0) {
                    vmInstall = (IVMInstall) installedJREs.get(sel - 1);
                }
                getRuntimeDelegate().setVMInstall(vmInstall);
                validate();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });

        Button button = SWTUtil.createButton(composite, Messages.installedJREs);
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String currentVM = combo.getText();
                if (showPreferencePage(composite)) {
                    updateJREs();
                    combo.setItems(jreNames);
                    combo.setText(currentVM);
                    if (combo.getSelectionIndex() == -1)
                        combo.select(0);
                }
            }
        });

        if (getRuntimeDelegate() != null) {
            if (getRuntimeDelegate().isUsingDefaultJRE()) {
                combo.select(0);
            } else {
                combo.setText(getRuntimeDelegate().getVMInstall().getName());
            }
        }
    }

    protected boolean showPreferencePage(Composite composite) {
        PreferenceManager manager = PlatformUI.getWorkbench().getPreferenceManager();
        IPreferenceNode node = manager.find("org.eclipse.jdt.ui.preferences.JavaBasePreferencePage").findSubNode(
                "org.eclipse.jdt.debug.ui.preferences.VMPreferencePage");
        PreferenceManager manager2 = new PreferenceManager();
        manager2.addToRoot(node);
        final PreferenceDialog dialog = new PreferenceDialog(composite.getShell(), manager2);
        final boolean[] result = new boolean[] { false };
        BusyIndicator.showWhile(composite.getDisplay(), new Runnable() {
            public void run() {
                dialog.create();
                if (dialog.open() == Window.OK)
                    result[0] = true;
            }
        });
        return result[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.ui.wizard.WizardFragment#isComplete()
     */
    public boolean isComplete() {
        IRuntimeWorkingCopy runtimeWC = getRuntimeDelegate().getRuntimeWorkingCopy();
        IStatus status = runtimeWC.validate(null);
        return status == null || status.getSeverity() != IStatus.ERROR;
    }

    protected void validate() {

        IRuntime runtime = getRuntimeDelegate().getRuntime();

        String runtimeName = runtime.getRuntimeType().getName();

        IWizardHandle wizard = getWizard();

        if (runtime == null) {
            wizard.setMessage("", IMessageProvider.ERROR);
            return;
        }

        IRuntimeWorkingCopy runtimeWC = getRuntimeDelegate().getRuntimeWorkingCopy();
        getRuntimeDelegate().setInstanceProperty("serverRootDirectory", installDir.getText());

        if (installDir.getText() == null || installDir.getText().length() == 0) {
            // installDir field has not been entered
            wizard.setMessage(Messages.bind(Messages.installDirInfo, runtimeName), IMessageProvider.NONE);
        } else {
            IStatus status = runtimeWC.validate(null);
            if (status == null || status.isOK()) {
                // a valid install found
                wizard.setMessage(Messages.bind(Messages.serverDetected, runtimeName), IMessageProvider.NONE);
            } else if (status.getCode() == GeronimoRuntimeDelegate.INCORRECT_VERSION) {
                if (status.getSeverity() == IStatus.ERROR) {
                    wizard.setMessage(status.getMessage(), IMessageProvider.ERROR);
                    return;
                }
                wizard.setMessage(status.getMessage(), IMessageProvider.WARNING);
            } else if (status.getCode() == GeronimoRuntimeDelegate.PARTIAL_IMAGE) {
                wizard.setMessage(status.getMessage(), IMessageProvider.ERROR);
                return;
            } else {
                File file = new Path(installDir.getText()).toFile();
                if (file.isDirectory()) {
                    String message = file.canWrite() ? Messages.noImageFound : Messages.cannotInstallAtLocation;
                    message = Messages.bind(message, runtimeName);
                    wizard.setMessage(message, IMessageProvider.ERROR);
                } else {
                    wizard.setMessage(Messages.noSuchDir, IMessageProvider.ERROR);
                }
                return;
            }

            if (!isValidVM()) {
                wizard.setMessage(Messages.bind(Messages.jvmWarning, runtimeName), IMessageProvider.WARNING);
            }
        }
    }

    private boolean isValidVM() {
        IVMInstall vmInstall = getRuntimeDelegate().getVMInstall();
        if (vmInstall instanceof IVMInstall2) {
            String javaVersion = ((IVMInstall2) vmInstall).getJavaVersion();
            return javaVersion != null && (javaVersion.startsWith("1.5") || javaVersion.startsWith("1.6"));
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.ui.wizard.WizardFragment#enter()
     */
    public void enter() {
        if (getRuntimeDelegate() != null)
            getRuntimeDelegate().getRuntimeWorkingCopy().setName(createName());
        validate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.ui.wizard.WizardFragment#exit()
     */
    public void exit() {
        validate();
    }


    public void performFinish(IProgressMonitor monitor) throws CoreException {

        ITargetPlatformService service = 
            (ITargetPlatformService) PDECore.getDefault().acquireService(ITargetPlatformService.class.getName());
        
        ITargetHandle geronimoTargetHandle = findGeronimoTargetDefinition(service);
        
        if (geronimoTargetHandle == null) {
            // target definition not found - generate one 
            
            String geronimoServerLocation = getRuntimeDelegate().getRuntime().getLocation().toString();
            if (geronimoServerLocation.endsWith("/")) {
                geronimoServerLocation = geronimoServerLocation.substring(0, geronimoServerLocation.length()-1);
            }
            
            // add or update string substitution variable named ${geronimo_30_server_location} 
            IStringVariableManager varManager = VariablesPlugin.getDefault().getStringVariableManager();
            IValueVariable v = varManager.getValueVariable(GERONIMO_SERVER_LOCATION_VARIABLE_NAME);
            if (v == null) {
                v = varManager.newValueVariable(GERONIMO_SERVER_LOCATION_VARIABLE_NAME,
                                                "The server location of Geronimo v3.0", 
                                                false, 
                                                geronimoServerLocation);
                varManager.addVariables(new IValueVariable[] { v });
            } else {
                v.setValue(geronimoServerLocation);
            }
                        
            // copy the target file for target platform into workspace
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IPath rootLocation = root.getLocation();
            String timestamp = Long.toString(System.currentTimeMillis());           
            String append = LOCAL_TARGET_DIRECTORY + timestamp + TARGET_FILE_EXTENSION;
            IPath destination = rootLocation.append(append);
            
            Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
            URL template = bundle.getEntry(TARGET_TEMPLATE_FILE);
            
            copyFile(template, destination, getLocationEntryLists(geronimoServerLocation));
                        
            geronimoTargetHandle = findGeronimoTargetDefinition(service);                    
        }
        
        ITargetHandle activeTargetHandle = service.getWorkspaceTargetHandle();
        
        if (geronimoTargetHandle != null) {
            if (activeTargetHandle == null || !activeTargetHandle.equals(geronimoTargetHandle)) {
                ITargetDefinition geronimoTarget = geronimoTargetHandle.getTargetDefinition();
                LoadTargetDefinitionJob.load(geronimoTarget);
            }            
        }
    }
    
    private ITargetHandle findGeronimoTargetDefinition(ITargetPlatformService service) throws CoreException {
        ITargetHandle[] targetHandles = service.getTargets(null);
        for (ITargetHandle cur : targetHandles) {
            if (cur.getTargetDefinition().getName().equals(Messages.serverVersion)) {
                return cur;
            }
        }
        return null;
    }
    
    protected void copyFile(URL source, IPath dest, String replacement) throws CoreException {
        Reader reader = null;
        Writer writer = null;
        
        try {
            try {
                reader = new InputStreamReader(source.openStream());
                
                StringBuilder builder = new StringBuilder();
                char [] buf = new char [2048];
                int count = 0;
                
                while ( (count = reader.read(buf)) >= 0) {
                    builder.append(buf, 0, count);
                }
                
                String template = builder.toString();
                String text = template.replace(TARGET_FILE_LOCATIONS_PLACEHOLDER, replacement);
                
                dest.toFile().getParentFile().mkdirs();
                writer = new FileWriter(dest.toFile());
                
                writer.write(text);
                
            } finally {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            }
        } catch (IOException ex) {
            throw new CoreException(new Status(IStatus.WARNING, Activator.PLUGIN_ID, Messages.copyTargetFileFailed, ex));
        }
    }
    
    
    protected static String getLocationEntryLists(String serverLocation) {

        StringBuffer buf = new StringBuffer(1024);
        buf.append(System.getProperty("line.separator"));
        for (String bundleDir : bundleDirectories) {
            File directory = new File(serverLocation, bundleDir);
            listFile(directory, buf, serverLocation);
        }
        return buf.toString();
    }

    private static void listFile(File file, StringBuffer buf, String serverLocation) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File subfile : files) {
                listFile(subfile, buf, serverLocation);
            }
        } else {
            String filename = file.getAbsolutePath();
            if (filename.endsWith(".jar")) {
                String parentPath = file.getParent();
                String bundleFolder = parentPath.substring(serverLocation.length());
                buf.append(LOCATION_LINE_TEMPLATE.replaceAll("GERONIMO_BUNDLE_FOLDER_NAME",
                        Matcher.quoteReplacement(bundleFolder)));
                buf.append(System.getProperty("line.separator"));
            }
        }
    }
    
    private GeronimoRuntimeDelegate getRuntimeDelegate() {
        IRuntimeWorkingCopy wc = (IRuntimeWorkingCopy) getTaskModel().getObject(TaskModel.TASK_RUNTIME);
        if (wc == null)
            return null;
        return (GeronimoRuntimeDelegate) wc.loadAdapter(GeronimoRuntimeDelegate.class, new NullProgressMonitor());
    }

    private String createName() {
        RuntimeDelegate dl = getRuntimeDelegate();
        IRuntimeType runtimeType = dl.getRuntime().getRuntimeType();
        String name = runtimeType.getName();
        IRuntime[] list = ServerCore.getRuntimes();
        int suffix = 1;
        String suffixName = name;
        for (int i = 0; i < list.length; i++) {
            if ((list[i].getName().equals(name) || list[i].getName().equals(suffixName))
                    && !list[i].equals(dl.getRuntime()))
                suffix++;
            suffixName = name + " " + suffix;
        }

        if (suffix > 1)
            return suffixName;
        return name;
    }

    protected String getRuntimeName() {
        if (getRuntimeDelegate() != null && getRuntimeDelegate().getRuntime() != null)
            return getRuntimeDelegate().getRuntime().getName();
        return null;
    }

    protected void updateJREs() {
        installedJREs = new ArrayList<IVMInstall>();
        IVMInstallType[] vmInstallTypes = JavaRuntime.getVMInstallTypes();
        int size = vmInstallTypes.length;
        for (int i = 0; i < size; i++) {
            IVMInstall[] vmInstalls = vmInstallTypes[i].getVMInstalls();
            int size2 = vmInstalls.length;
            for (int j = 0; j < size2; j++) {
                installedJREs.add(vmInstalls[j]);
            }
        }

        // The Default JRE will always be the first item in the combo. This is
        // an assumption that is made by the combo selection listener and that
        // all
        // other installed JREs are listed afterwards in the same order that
        // they
        // are found in the list of installed JREs
        size = installedJREs.size();
        jreNames = new String[size + 1];
        jreNames[0] = Messages.runtimeDefaultJRE;
        for (int i = 0; i < size; i++) {
            IVMInstall vmInstall = (IVMInstall) installedJREs.get(i);
            jreNames[i + 1] = vmInstall.getName();
        }
    }

    public IWizardHandle getWizard() {
        return fWizard;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.wst.server.ui.wizard.WizardFragment#createChildFragments(
     * java.util.List)
     */
    protected void createChildFragments(List list) {
        list.add(new GeronimoRuntimeSourceWizardFragment());
    }

    /**
     * Code for testing server name determination code in the
     * updateInstallDir(IPath) method of addInstallableRuntimeSection.
     * 
     * @param args
     */
    public static void main(String[] args) {
        Pattern SERVER_NAME_VERSION_PATTERN = Pattern.compile("(.*-)((\\d+\\.\\d+)(\\.(\\d+))?)");
        for (String path : args) {
            StringBuffer installPath = new StringBuffer();
            Matcher matcher = SERVER_NAME_VERSION_PATTERN.matcher(path);
            if (matcher.find()) {
                String serverName = matcher.group(1);
                String serverVersion = matcher.group(2);
                installPath = installPath.append(serverName + serverVersion);
                System.out.println("path = " + path + ", serverVersion = " + serverVersion + ", installPath = "
                        + installPath);
            } else {
                System.out.println("No version found in path = " + path);
            }
        }
    }
}