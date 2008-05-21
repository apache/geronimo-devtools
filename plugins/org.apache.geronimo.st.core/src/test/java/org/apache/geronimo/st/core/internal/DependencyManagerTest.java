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

package org.apache.geronimo.st.core.internal;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.geronimo.jee.deployment.Artifact;
import org.apache.geronimo.jee.deployment.ObjectFactory;

/**
 * <b>DependencyManagerTest</b> is used to test various inter-dependencies between artifacts that
 * are used to represent Geronimo-specific deployment plans<p>
 * 
 * @version $Rev$ $Date$ 
 */
public class DependencyManagerTest extends TestCase {

    private DependencyManager dm = new DependencyManager();
    private ObjectFactory deploymentFactory = new ObjectFactory();

    // 
    // Instantiate some artifacts
    // 
    private Artifact artifact1 = deploymentFactory.createArtifact();
    private Artifact artifact2 = deploymentFactory.createArtifact();
    private Artifact artifact3 = deploymentFactory.createArtifact();
    private Artifact artifact4 = deploymentFactory.createArtifact();
    private Artifact artifact5 = deploymentFactory.createArtifact();
    private Artifact artifact6 = deploymentFactory.createArtifact();
    private Artifact artifact7 = deploymentFactory.createArtifact();
    private Artifact artifact8 = deploymentFactory.createArtifact();
    private Artifact artifact9 = deploymentFactory.createArtifact();


    protected void setUp() {

        // 
        // Set the artifacts to a known state before each testcase
        // 
        artifact1.setGroupId("org.apache.geronimo.groupid1");
        artifact1.setArtifactId("artifactid1");
        artifact1.setVersion("version1");
        artifact1.setType("war");

        artifact2.setGroupId("org.apache.geronimo.groupid2");
        artifact2.setArtifactId("artifactid2");
        artifact2.setVersion("version2");
        artifact2.setType("ear");

        artifact3.setGroupId("org.apache.geronimo.groupid3");
        artifact3.setArtifactId("artifactid3");
        artifact3.setVersion("version3");
        artifact3.setType("rar");

        artifact4.setGroupId("org.apache.geronimo.groupid4");
        artifact4.setArtifactId("artifactid4");
        artifact4.setVersion("version4");
        artifact4.setType("jar");

        artifact5.setGroupId("org.apache.geronimo.groupid5");
        artifact5.setArtifactId("artifactid5");
        artifact5.setVersion("version5");
        artifact5.setType("war");

        artifact6.setGroupId(null);
        artifact6.setArtifactId("artifactid6");
        artifact6.setVersion("version6");
        artifact6.setType("ear");

        artifact7.setGroupId("org.apache.geronimo.groupid7");
        artifact7.setArtifactId(null);
        artifact7.setVersion("version7");
        artifact7.setType("rar");

        artifact8.setGroupId("org.apache.geronimo.groupid8");
        artifact8.setArtifactId("artifactid8");
        artifact8.setVersion(null);
        artifact8.setType("war");

        artifact9.setGroupId("org.apache.geronimo.groupid9");
        artifact9.setArtifactId("artifactid9");
        artifact9.setVersion("version9");
        artifact9.setType(null);

        // 
        // Set the dependency manager to a known state between each testcase
        // 
        dm.close();
    }


    /*--------------------------------------------------------------------------------------------*\
    |                                                                                              |
    |  Testcase(s)                                                                                 |
    |                                                                                              |
    \*--------------------------------------------------------------------------------------------*/ 

    public void testSimple() {

        dm.addDependency(artifact1,artifact2);

        Set children = dm.getChildren(artifact1);
        assertEquals(children.size(),0);

        children = dm.getChildren(artifact2);
        assertEquals(children.size(),1);
        assertTrue(children.contains(artifact1));

        Set parents = dm.getParents(artifact1);
        assertEquals(parents.size(),1);
        assertTrue(parents.contains(artifact2));

        parents = dm.getParents(artifact2);
        assertEquals(parents.size(),0);
    }

    public void testSingleParent() {

        dm.addDependency(artifact2,artifact1);
        dm.addDependency(artifact3,artifact1);
        dm.addDependency(artifact4,artifact1);
        dm.addDependency(artifact5,artifact1);
        dm.addDependency(artifact6,artifact1);
        dm.addDependency(artifact7,artifact1);
        dm.addDependency(artifact8,artifact1);
        dm.addDependency(artifact9,artifact1);
        dm.addDependency(artifact9,artifact1);          // Must not change the number of children

        Set children = dm.getChildren(artifact1);
        assertEquals(children.size(),8);
        assertTrue(children.contains(artifact2));
        assertTrue(children.contains(artifact3));
        assertTrue(children.contains(artifact4));
        assertTrue(children.contains(artifact5));
        assertTrue(children.contains(artifact6));
        assertTrue(children.contains(artifact7));
        assertTrue(children.contains(artifact8));
        assertTrue(children.contains(artifact9));   

        Set parents = dm.getParents(artifact9);
        assertEquals(parents.size(),1);
        assertTrue(parents.contains(artifact1));

        dm.removeDependency(artifact9,artifact1); 
        children = dm.getChildren(artifact1);
        assertEquals(children.size(),7);
        assertTrue(children.contains(artifact2));
        assertTrue(children.contains(artifact3));
        assertTrue(children.contains(artifact4));
        assertTrue(children.contains(artifact5));
        assertTrue(children.contains(artifact6));
        assertTrue(children.contains(artifact7));
        assertTrue(children.contains(artifact8));

        parents = dm.getParents(artifact9);
        assertEquals(parents.size(),0);

        dm.removeAllDependencies(artifact7); 
        children = dm.getChildren(artifact1);
        assertEquals(children.size(),6);
        assertTrue(children.contains(artifact2));
        assertTrue(children.contains(artifact3));
        assertTrue(children.contains(artifact4));
        assertTrue(children.contains(artifact5));
        assertTrue(children.contains(artifact6));
        assertTrue(children.contains(artifact8));

        dm.removeDependency(artifact7,artifact1); 
        children = dm.getChildren(artifact1);
        assertEquals(children.size(),6);

        children = dm.getChildren(artifact2);
        assertEquals(children.size(),0);

        dm.removeAllDependencies(artifact5); 
        children = dm.getChildren(artifact1);
        assertEquals(children.size(),5);
        assertTrue(children.contains(artifact2));
        assertTrue(children.contains(artifact3));
        assertTrue(children.contains(artifact4));
        assertTrue(children.contains(artifact6));
        assertTrue(children.contains(artifact8));

        dm.removeAllDependencies(artifact1); 
        dm.removeAllDependencies(artifact2); 
        dm.removeAllDependencies(artifact3); 
        dm.removeAllDependencies(artifact4); 
        dm.removeAllDependencies(artifact5); 
        dm.removeAllDependencies(artifact6); 
        dm.removeAllDependencies(artifact7); 
        dm.removeAllDependencies(artifact8); 
        dm.removeAllDependencies(artifact9); 
        dm.removeDependency(artifact1,artifact1); 
        dm.removeDependency(artifact2,artifact1); 
        dm.removeDependency(artifact3,artifact1); 
        dm.removeDependency(artifact4,artifact1); 
        dm.removeDependency(artifact5,artifact1); 
        dm.removeDependency(artifact6,artifact1); 
        dm.removeDependency(artifact7,artifact1); 
        dm.removeDependency(artifact8,artifact1); 
        dm.removeDependency(artifact7,artifact1); 

        children = dm.getChildren(artifact1);
        assertEquals(children.size(),0);
        children = dm.getChildren(artifact2);
        assertEquals(children.size(),0);
        children = dm.getChildren(artifact3);
        assertEquals(children.size(),0);
        children = dm.getChildren(artifact4);
        assertEquals(children.size(),0);
        children = dm.getChildren(artifact5);
        assertEquals(children.size(),0);
        children = dm.getChildren(artifact6);
        assertEquals(children.size(),0);
        children = dm.getChildren(artifact7);
        assertEquals(children.size(),0);
        children = dm.getChildren(artifact8);
        assertEquals(children.size(),0);
        children = dm.getChildren(artifact9);
        assertEquals(children.size(),0);
    }

    public void testMultipleParents() throws Exception {

        dm.addDependency(artifact1,artifact2);
        dm.addDependency(artifact1,artifact3);
        dm.addDependency(artifact1,artifact4);
        dm.addDependency(artifact1,artifact5);
        dm.addDependency(artifact1,artifact6);
        dm.addDependency(artifact1,artifact7);
        dm.addDependency(artifact1,artifact8);
        dm.addDependency(artifact1,artifact9);
        dm.addDependency(artifact1,artifact9);          // Must not change the number of parents

        Set parents = dm.getParents(artifact1);
        assertEquals(parents.size(),8);
        assertTrue(parents.contains(artifact2));
        assertTrue(parents.contains(artifact3));
        assertTrue(parents.contains(artifact4));
        assertTrue(parents.contains(artifact5));
        assertTrue(parents.contains(artifact6));
        assertTrue(parents.contains(artifact7));
        assertTrue(parents.contains(artifact8));
        assertTrue(parents.contains(artifact9));   

        dm.removeAllDependencies(artifact1); 
        parents = dm.getParents(artifact1);
        assertEquals(parents.size(),0);
        Set children = dm.getChildren(artifact1);
        assertEquals(children.size(),0);

        dm.addDependency(artifact1,artifact2);
        dm.addDependency(artifact1,artifact3);
        dm.addDependency(artifact1,artifact4);
        dm.addDependency(artifact1,artifact5);
        dm.addDependency(artifact6,artifact7);
        dm.addDependency(artifact6,artifact8);
        dm.addDependency(artifact6,artifact9);
        parents = dm.getParents(artifact1);
        assertEquals(parents.size(),4);
        assertTrue(parents.contains(artifact2));
        assertTrue(parents.contains(artifact3));
        assertTrue(parents.contains(artifact4));
        assertTrue(parents.contains(artifact5));
        parents = dm.getParents(artifact6);
        assertEquals(parents.size(),3);
        assertTrue(parents.contains(artifact7));
        assertTrue(parents.contains(artifact8));
        assertTrue(parents.contains(artifact9));

        Set parentSet = new HashSet();
        parentSet.add(artifact2);
        parentSet.add(artifact3);
        parentSet.add(artifact4);
        parentSet.add(artifact5);
        parentSet.add(artifact6);
        parentSet.add(artifact7);
        parentSet.add(artifact8);
        parentSet.add(artifact9);
        dm.addDependencies(artifact1,parentSet);
        parents = dm.getParents(artifact1);
        assertEquals(parents.size(),8);
        assertTrue(parents.contains(artifact2));
        assertTrue(parents.contains(artifact3));
        assertTrue(parents.contains(artifact4));
        assertTrue(parents.contains(artifact5));
        assertTrue(parents.contains(artifact6));
        assertTrue(parents.contains(artifact7));
        assertTrue(parents.contains(artifact8));
        assertTrue(parents.contains(artifact9));   
    }

    public void testMultipleChildrenAndParents() throws Exception {

        dm.addDependency(artifact1,artifact2);
        dm.addDependency(artifact1,artifact3);
        dm.addDependency(artifact1,artifact4);
        dm.addDependency(artifact1,artifact5);
        dm.addDependency(artifact6,artifact9);
        dm.addDependency(artifact7,artifact9);
        dm.addDependency(artifact8,artifact9);

        Set parents = dm.getParents(artifact1);
        assertEquals(parents.size(),4);
        assertTrue(parents.contains(artifact2));
        assertTrue(parents.contains(artifact3));
        assertTrue(parents.contains(artifact4));
        assertTrue(parents.contains(artifact5));

        Set children = dm.getChildren(artifact9);
        assertEquals(children.size(),3);
        assertTrue(children.contains(artifact6));
        assertTrue(children.contains(artifact7));
        assertTrue(children.contains(artifact8));
    }

    public void testCircularDependency() throws Exception {

        dm.addDependency(artifact1,artifact1);
        Set children = dm.getChildren(artifact1);
        Set parents = dm.getParents(artifact1);
        assertEquals(children.size(),1);
        assertTrue(children.contains(artifact1));
        assertEquals(parents.size(),1);
        assertTrue(parents.contains(artifact1));

        dm.addDependency(artifact2,artifact2);
        children = dm.getChildren(artifact2);
        parents = dm.getParents(artifact2);
        assertEquals(children.size(),1);
        assertTrue(children.contains(artifact2));
        assertEquals(parents.size(),1);
        assertTrue(parents.contains(artifact2));

        dm.close();

        dm.addDependency(artifact2,artifact1);
        dm.addDependency(artifact3,artifact2);
        dm.addDependency(artifact4,artifact3);
        dm.addDependency(artifact5,artifact4);
        dm.addDependency(artifact6,artifact5);
        dm.addDependency(artifact7,artifact6);
        dm.addDependency(artifact8,artifact7);
        dm.addDependency(artifact9,artifact8);
        dm.addDependency(artifact1,artifact9);
        children = dm.getChildren(artifact1);
        parents = dm.getParents(artifact1);
        assertEquals(children.size(),1);
        assertTrue(children.contains(artifact2));
        assertEquals(parents.size(),1);
        assertTrue(parents.contains(artifact9));
        children = dm.getChildren(artifact2);
        parents = dm.getParents(artifact3);
        assertEquals(children.size(),1);
        assertTrue(children.contains(artifact3));
        assertEquals(parents.size(),1);
        assertTrue(parents.contains(artifact2));
    }
}
