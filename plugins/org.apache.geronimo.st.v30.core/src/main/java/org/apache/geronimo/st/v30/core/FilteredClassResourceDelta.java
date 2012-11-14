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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.server.core.model.IModuleFile;
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.model.IModuleResourceDelta;

public class FilteredClassResourceDelta implements IModuleResourceDelta {
    
    private final IModuleResourceDelta delta;
    private final boolean includeModifiedClasses;
    private IModuleResourceDelta[] children;

    public FilteredClassResourceDelta(IModuleResourceDelta delta, boolean includeModifiedClasses) {
        this.delta = delta;
        this.includeModifiedClasses = includeModifiedClasses;
    }

    @Override
    public IModuleResourceDelta[] getAffectedChildren() {
        if (children == null) {
            if (includeModifiedClasses) {
                children = filterIn(delta.getAffectedChildren());
            } else {
                children = filterOut(delta.getAffectedChildren());
            }
        }
        return children;
    }

    private static IModuleResourceDelta[] filterOut(IModuleResourceDelta[] deltaArray) {
        if (deltaArray == null) {
            return null;
        }
        List<IModuleResourceDelta> deltaList = new ArrayList<IModuleResourceDelta>();
        for (IModuleResourceDelta delta : deltaArray) {
            IModuleResource resource = delta.getModuleResource();
            int kind = delta.getKind();
            if (resource instanceof IModuleFile) {
                // file
                if (kind == IModuleResourceDelta.CHANGED && resource.getName().endsWith(".class")) {
                    // filter out changed .class file
                } else {
                    deltaList.add(delta);
                }
            } else {
                // directory
                deltaList.add(new FilteredClassResourceDelta(delta, false));
            }
        }
        return deltaList.toArray(new IModuleResourceDelta[deltaList.size()]);
    }

    
    private static IModuleResourceDelta[] filterIn(IModuleResourceDelta[] deltaArray) {
        if (deltaArray == null) {
            return null;
        }
        List<IModuleResourceDelta> deltaList = new ArrayList<IModuleResourceDelta>();
        for (IModuleResourceDelta delta : deltaArray) {
            IModuleResource resource = delta.getModuleResource();
            int kind = delta.getKind();
            if (resource instanceof IModuleFile) {
                // file
                if (kind == IModuleResourceDelta.CHANGED && resource.getName().endsWith(".class")) {
                    // filter in changed .class file only
                    deltaList.add(delta);
                }
            } else {
                // directory
                if (kind == IModuleResourceDelta.NO_CHANGE || kind == IModuleResourceDelta.CHANGED) {
                    // filter in changed or non-changed directories
                    deltaList.add(new FilteredClassResourceDelta(delta, true));
                }
            }
        }
        return deltaList.toArray(new IModuleResourceDelta[deltaList.size()]);
    }

    @Override
    public int getKind() {
        return delta.getKind();
    }

    @Override
    public IPath getModuleRelativePath() {
        return delta.getModuleRelativePath();
    }

    @Override
    public IModuleResource getModuleResource() {
        return delta.getModuleResource();
    }    
    
    public static IModuleResourceDelta[] getChangedClassIncludeDelta(IModuleResourceDelta[] deltaArray) {
        return filterIn(deltaArray);
    }
    
    public static IModuleResourceDelta[] getChangedClassExcludeDelta(IModuleResourceDelta[] deltaArray) {
        return filterOut(deltaArray);
    }
    
}
