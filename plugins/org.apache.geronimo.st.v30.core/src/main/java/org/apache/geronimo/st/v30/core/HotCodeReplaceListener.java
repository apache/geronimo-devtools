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
package org.apache.geronimo.st.v30.core;

import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaHotCodeReplaceListener;

public class HotCodeReplaceListener implements IJavaHotCodeReplaceListener {

    public void hotCodeReplaceFailed(IJavaDebugTarget target, DebugException exception) {
        Trace.log(Trace.INFO, "Eclipse hot code replace failed - will restart bundle", exception);
    }

    public void hotCodeReplaceSucceeded(IJavaDebugTarget target) {
        Trace.trace(Trace.INFO, "Eclipse hot code replace was successful", Activator.traceCore);
    }

    public void obsoleteMethods(IJavaDebugTarget target) {
        Trace.trace(Trace.INFO, "Obsolete methods detected", Activator.traceCore);
    } 
    
}
