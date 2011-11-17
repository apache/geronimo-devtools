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

package org.apache.geronimo.st.v21.core.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.geronimo.jee.deployment.Artifact;
import org.apache.geronimo.st.v21.core.Activator;

/**
 * <b>DependencyManager</b> is very closely-based on the similar class in the Geronimo server.
 * DependencyManager is the record keeper of the dependencies in the Geronimo Eclipse Plugin. The
 * DependencyManager does not enforce any dependencies, it is simply a place where components can
 * register their intent to be dependent on another component, and where other components can query
 * those dependencies.
 * 
 * <p>Like the DependencyManager in the Geronimo server, it uses the nomenclature of parent-child
 * where a child is dependent on a parent. The names parent and child have no other meaning are just
 * a convenience to make the code readable.
 * 
 * <p>The initial usage of this DependencyManager in the GEP is somewhat limited but other usages 
 * are possible<p>
 * 
 * @version $Rev$ $Date$
 */
public class DependencyManager {

    //
    // Map from child to a list of parents
    //
    private final Map childToParentMap = new HashMap();

    //
    // Map from parent back to a list of its children
    //
    private final Map parentToChildMap = new HashMap();


    /**
     *
     */
    public void close() {
        childToParentMap.clear();
        parentToChildMap.clear();
    }


    /**
     * Declares a dependency from a child to a parent.
     *
     * @param child the dependent component
     * @param parent the component the child is depending on
     */
    public void addDependency(Artifact child, Artifact parent) {
        Trace.tracePoint("Entry", Activator.traceInternal, "DependencyManager.addDependency", child, parent);

        Set parents = (Set) childToParentMap.get(child);
        if (parents == null) {
            parents = new LinkedHashSet();
            childToParentMap.put(child, parents);
        }
        parents.add(parent);

        Set children = (Set) parentToChildMap.get(parent);
        if (children == null) {
            children = new LinkedHashSet();
            parentToChildMap.put(parent, children);
        }
        children.add(child);

        Trace.tracePoint("Exit ", Activator.traceInternal, "DependencyManager.addDependency", childToParentMap.size() );
        Trace.tracePoint("Exit ", Activator.traceInternal, "DependencyManager.addDependency", parentToChildMap.size() );
    }


    /**
     * Removes a dependency from a child to a parent
     *
     * @param child the dependnet component
     * @param parent the component that the child wil no longer depend on
     */
    public void removeDependency(Artifact child, Artifact parent) {
        Trace.tracePoint("Entry", Activator.traceInternal, "DependencyManager.removeDependency", child, parent);

        Set parents = (Set) childToParentMap.get(child);
        if (parents != null) {
            parents.remove(parent);
        }

        Set children = (Set) parentToChildMap.get(parent);
        if (children != null) {
            children.remove(child);
        }

        Trace.tracePoint("Exit ", Activator.traceInternal, "DependencyManager.addDependency");
    }


    /**
     * Removes all dependencies for a child
     *
     * @param child the component that will no longer depend on anything
     */
    public void removeAllDependencies(Artifact child) {
        Trace.tracePoint("Entry", Activator.traceInternal, "DependencyManager.removeAllDependencies", child);

        Set parents = (Set) childToParentMap.remove(child);
        if (parents == null) {
            return;
        }

        for (Iterator iterator = parents.iterator(); iterator.hasNext();) {
            Artifact parent = (Artifact) iterator.next();
            Set children = (Set) parentToChildMap.get(parent);
            if (children != null) {
                children.remove(child);
            }
        }

        Trace.tracePoint("Exit ", Activator.traceInternal, "DependencyManager.removeAllDependencies");
    }


    /**
     * Adds dependencies from the child to every parent in the parents set
     *
     * @param child the dependent component
     * @param parents the set of components the child is depending on
     */
    public void addDependencies(Artifact child, Set parents) {
        Trace.tracePoint("Entry", Activator.traceInternal, "DependencyManager.addDependencies", child, parents);

        Set existingParents = (Set) childToParentMap.get(child);
        if (existingParents == null) {
            existingParents = new LinkedHashSet(parents);
            childToParentMap.put(child, existingParents);
        }
        else {
            existingParents.addAll(parents);
        }

        for (Iterator i = parents.iterator(); i.hasNext();) {
            Object startParent = i.next();
            Set children = (Set) parentToChildMap.get(startParent);
            if (children == null) {
                children = new LinkedHashSet();
                parentToChildMap.put(startParent, children);
            }
            children.add(child);
        }

        Trace.tracePoint("Exit ", Activator.traceInternal, "DependencyManager.addDependencies");
    }


    /**
     * Gets the set of parents that the child is depending on
     *
     * @param child the dependent component
     * @return a collection containing all of the components the child depends on; will never be null
     */
    public Set getParents(Artifact child) {
        Trace.tracePoint("Entry", Activator.traceInternal, "DependencyManager.getParents", child);

        Set parents = (Set) childToParentMap.get(child);
        if (parents == null) {
            Trace.tracePoint("Exit", Activator.traceInternal, "DependencyManager.getParents", 0);
            return Collections.EMPTY_SET;
        }

        Trace.tracePoint("Exit", Activator.traceInternal, "DependencyManager.getParents", parents.size() );
        return new LinkedHashSet(parents);
    }


    /**
     * Gets all of the children that have a dependency on the specified parent.
     *
     * @param parent the component the returned childen set depend on
     * @return a collection containing all of the components that depend on the parent; will never be null
     */
    public Set getChildren(Artifact parent) {
        Trace.tracePoint("Entry", Activator.traceInternal, "DependencyManager.getChildren", parent);

        Set children = (Set) parentToChildMap.get(parent);
        if (children == null) {
            Trace.tracePoint("Exit ", Activator.traceInternal, "DependencyManager.getChildren", 0);
            return Collections.EMPTY_SET;
        }

        Trace.tracePoint("Exit ", Activator.traceInternal, "DependencyManager.getChildren", children.size() );
        return new LinkedHashSet(children);
    }
}
