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

package org.apache.geronimo.devtools.helloworld;

import junit.framework.TestCase;

import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * @version $Rev$ $Date$
 */
public class PerspectiveTests extends TestCase {

    public void testDebugPerspective() {
        DebugUIPlugin.getStandardDisplay().syncExec(new Runnable() {
            public void run() {
                IWorkbench workbench = PlatformUI.getWorkbench();
                IPerspectiveDescriptor descriptor = workbench.getPerspectiveRegistry().findPerspectiveWithId(IDebugUIConstants.ID_DEBUG_PERSPECTIVE);
                workbench.getActiveWorkbenchWindow().getActivePage().setPerspective(descriptor);
            }
        });
    }
}
