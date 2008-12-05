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

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @version $Rev$ $Date$
 */
public class ServerPluginManagerDialog extends WizardDialog {
    private ServerPluginManagerWizard wizard;
    public ServerPluginManagerDialog(Shell parentShell, ServerPluginManagerWizard newWizard) {
        super(parentShell, newWizard);
        wizard = newWizard;
    }

    @Override
    protected void nextPressed() {
        wizard.nextPressed();
        super.nextPressed();
    }

    @Override
    protected void backPressed() {
        wizard.backPressed();
        super.backPressed();
    }
}
