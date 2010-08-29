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
package org.apache.geronimo.st.v30.ui.pages;

import org.apache.geronimo.st.ui.editors.AbstractGeronimoDeploymentPlanEditor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

/**
 * @version $Rev$ $Date$
 */
public class DeploymentPlanSourcePage extends StructuredTextEditor {
    
    protected AbstractGeronimoDeploymentPlanEditor editor;

    public DeploymentPlanSourcePage (AbstractGeronimoDeploymentPlanEditor editor) {
        super();
        this.editor = editor;
    }
    
    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        super.doSave(progressMonitor);
        try {
            editor.reloadDeploymentPlan();
        } catch (Exception e) {
            MessageDialog.openError(Display.getCurrent().getActiveShell(),"Error", e.getMessage());
        }
    }
}
