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
package org.apache.geronimo.st.v30.ui.editors;

import org.apache.geronimo.st.v30.ui.internal.Trace;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.PartInitException;

/**
 * @version $Rev$ $Date$
 */
public class BlueprintFormEditor extends FormEditor {

    public BlueprintFormEditor() {
        super();
        
        Trace.tracePoint("Entry", "BlueprintFormEditor.BlueprintFormEditor");
        
        // TODO
        
        Trace.tracePoint("Exit", "BlueprintFormEditor.BlueprintFormEditor");
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
     */
    public void addPages() {
        Trace.tracePoint("Entry", "BlueprintFormEditor.addPages");
        
        // TODO
        
        Trace.tracePoint("Exit", "BlueprintFormEditor.addPages");
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    public boolean isSaveAsAllowed() {
        Trace.tracePoint("Entry", "BlueprintFormEditor.isSaveAsAllowed");
        
        // TODO
        
        Trace.tracePoint("Exit", "BlueprintFormEditor.isSaveAsAllowed, false");
        return false;
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    public final void doSaveAs() {
        Trace.tracePoint("Entry", "BlueprintFormEditor.doSaveAs");
        
        // TODO
        
        Trace.tracePoint("Exit", "BlueprintFormEditor.doSaveAs");
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave(IProgressMonitor monitor) {
        Trace.tracePoint("Entry", "BlueprintFormEditor.doSave", monitor);
        
        // TODO
        
        Trace.tracePoint("Exit", "BlueprintFormEditor.doSave");
    }
}
