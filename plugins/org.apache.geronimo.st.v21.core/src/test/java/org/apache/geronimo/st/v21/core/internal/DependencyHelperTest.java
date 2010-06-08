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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import junit.framework.TestCase;

import org.apache.geronimo.st.v21.core.internal.DependencyHelper;
import org.xml.sax.InputSource;

/**
 * <b>DependencyHelperTest</b> is used to test various inter-dependencies between various 
 * Geronimo-specific deployment plans (represented by JAXBElements)<p> 
 * 
 * @version $Rev: 685643 $ $Date: 2008-08-14 03:24:11 +0800 (Thu, 14 Aug 2008) $ 
 */
public class DependencyHelperTest extends TestCase {

    private DependencyHelper dh = new DependencyHelper();
    private Object jaxbElement1;
    private Object jaxbElement2;
    private Object jaxbElement3;
    private Object jaxbElement4;
    private Object jaxbElement5;
    private Object jaxbElement6;
    private Object jaxbElement7;
    private Object jaxbElement8;
    private Object jaxbElement9;
    private Object jaxbElement10;
    private Object jaxbElement11;
    private Object jaxbElement12;
    private Object jaxbElement13;
    private List<JAXBElement> jaxbElements = new ArrayList<JAXBElement>();
    private List<JAXBElement> jaxbReordered = new ArrayList<JAXBElement>();

    // 
    // JAXBContext instantiation is costly - should be done only once 
    // 
    private static final JAXBContext jaxbContext = newJAXBContext();
    private static JAXBContext newJAXBContext() {
        try {
            return JAXBContext.newInstance(
                                          "org.apache.geronimo.jee.connector:" +
                                          "org.apache.geronimo.jee.openejb:" +
                                          "org.apache.geronimo.jee.web:" +
                                          "org.apache.geronimo.jee.application:" +
                                          "org.apache.geronimo.jee.applicationclient:" +
                                          "org.apache.geronimo.jee.deployment:" +
                                          "org.apache.geronimo.jee.naming:" +
                                          "org.apache.geronimo.jee.security", DependencyHelperTest.class.getClassLoader() );
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 
    // Set the JAXBElements to a known state 
    // 
    // TODO: Need to execute only once (i.e., TestSuite)
    // 
    public void setUp() throws Exception {

        // 
        // Create unmarshaller 
        // 
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // 
        // Read XML files 
        // 
        InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream("dependencyhelper/geronimo-application-example-1.xml");
        InputStream inputStream2 = this.getClass().getClassLoader().getResourceAsStream("dependencyhelper/geronimo-ra-example-2.xml");
        InputStream inputStream3 = this.getClass().getClassLoader().getResourceAsStream("dependencyhelper/geronimo-web-example-3.xml");
        InputStream inputStream4 = this.getClass().getClassLoader().getResourceAsStream("dependencyhelper/geronimo-web-example-4.xml");
        InputStream inputStream5 = this.getClass().getClassLoader().getResourceAsStream("dependencyhelper/geronimo-web-example-5.xml");
        InputStream inputStream6 = this.getClass().getClassLoader().getResourceAsStream("dependencyhelper/geronimo-web-example-6.xml");
        InputStream inputStream7 = this.getClass().getClassLoader().getResourceAsStream("dependencyhelper/geronimo-web-example-7.xml");
        InputStream inputStream8 = this.getClass().getClassLoader().getResourceAsStream("dependencyhelper/geronimo-web-example-8.xml");
        InputStream inputStream9 = this.getClass().getClassLoader().getResourceAsStream("dependencyhelper/openejb-jar-example-9.xml");
        InputStream inputStream10 = this.getClass().getClassLoader().getResourceAsStream("dependencyhelper/openejb-jar-example-10.xml");
        InputStream inputStream11 = this.getClass().getClassLoader().getResourceAsStream("dependencyhelper/geronimo-ra-example-11.xml");
        InputStream inputStream12 = this.getClass().getClassLoader().getResourceAsStream("dependencyhelper/geronimo-web-example-12.xml");
        InputStream inputStream13 = this.getClass().getClassLoader().getResourceAsStream("dependencyhelper/geronimo-web-example-13.xml");
        String file1 = readContent(inputStream1);
        String file2 = readContent(inputStream2);
        String file3 = readContent(inputStream3);
        String file4 = readContent(inputStream4);
        String file5 = readContent(inputStream5);
        String file6 = readContent(inputStream6);
        String file7 = readContent(inputStream7);
        String file8 = readContent(inputStream8);
        String file9 = readContent(inputStream9);
        String file10 = readContent(inputStream10);
        String file11 = readContent(inputStream11);
        String file12 = readContent(inputStream12);
        String file13 = readContent(inputStream13);

        // 
        // Unmarshall all files
        // 
        jaxbElement1 = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(file1.getBytes())));
        jaxbElement2 = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(file2.getBytes())));
        jaxbElement3 = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(file3.getBytes())));
        jaxbElement4 = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(file4.getBytes())));
        jaxbElement5 = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(file5.getBytes())));
        jaxbElement6 = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(file6.getBytes())));
        jaxbElement7 = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(file7.getBytes())));
        jaxbElement8 = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(file8.getBytes())));
        jaxbElement9 = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(file9.getBytes())));
        jaxbElement10 = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(file10.getBytes())));
        jaxbElement11 = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(file11.getBytes())));
        jaxbElement12 = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(file12.getBytes())));
        jaxbElement13 = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(file13.getBytes())));

        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();
    }


    /*--------------------------------------------------------------------------------------------*\
    |                                                                                              |
    |  Testcase(s)                                                                                 |
    |                                                                                              |
    \*--------------------------------------------------------------------------------------------*/ 

    public void testSingleParent() {

        //---------+--------------------------------+-----------------------------------------------
        //  JAXB   |                                | 
        // ELEMENT | ARTIFACT                       | PARENTS
        //---------+--------------------------------+-----------------------------------------------
        //    3    | default/geronimo-web-1/1.0/car | 
        //---------+--------------------------------+-----------------------------------------------
        //    4    | default/geronimo-web-2/1.0/car | default/geronimo-web-1/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 1
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement3
        assertEquals( jaxbReordered.get(1),jaxbElements.get(1) );       // jaxbElement4
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 1
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(1) );       // jaxbElement4
        assertEquals( jaxbReordered.get(1),jaxbElements.get(0) );       // jaxbElement3
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        //---------+--------------------------------+-----------------------------------------------
        //  JAXB   |                                | 
        // ELEMENT | ARTIFACT                       | PARENTS
        //---------+--------------------------------+-----------------------------------------------
        //    5    | default/geronimo-web-3/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    6    | default/geronimo-web-4/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 1
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement5
        assertEquals( jaxbReordered.get(1),jaxbElements.get(1) );       // jaxbElement6
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 1
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(1) );       // jaxbElement6
        assertEquals( jaxbReordered.get(1),jaxbElements.get(0) );       // jaxbElement5
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();
    }

    public void testMultipleParents() throws Exception {

        //---------+--------------------------------+-----------------------------------------------
        //  JAXB   |                                | 
        // ELEMENT | ARTIFACT                       | PARENTS
        //---------+--------------------------------+-----------------------------------------------
        //    3    | default/geronimo-web-1/1.0/car |
        //---------+--------------------------------+-----------------------------------------------
        //    4    | default/geronimo-web-2/1.0/car | default/geronimo-web-1/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    5    | default/geronimo-web-3/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    6    | default/geronimo-web-4/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-3/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    7    | default/geronimo-web-5/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    8    | default/geronimo-web-6/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-5/1.0/car
        //         |                                | default/geronimo-web-4/1.0/car
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement8 );                  // Element 5
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );       
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement3
        assertEquals( jaxbReordered.get(1),jaxbElements.get(1) );       // jaxbElement4
        assertEquals( jaxbReordered.get(2),jaxbElements.get(2) );       // jaxbElement5
        assertEquals( jaxbReordered.get(3),jaxbElements.get(3) );       // jaxbElement6
        assertEquals( jaxbReordered.get(4),jaxbElements.get(4) );       // jaxbElement7
        assertEquals( jaxbReordered.get(5),jaxbElements.get(5) );       // jaxbElement8
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        jaxbElements.add( (JAXBElement)jaxbElement8 );                  // Element 0  
        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 5
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(1) );       // jaxbElement7
        assertEquals( jaxbReordered.get(1),jaxbElements.get(3) );       // jaxbElement5
        assertEquals( jaxbReordered.get(2),jaxbElements.get(2) );       // jaxbElement6
        assertEquals( jaxbReordered.get(3),jaxbElements.get(5) );       // jaxbElement3
        assertEquals( jaxbReordered.get(4),jaxbElements.get(4) );       // jaxbElement4
        assertEquals( jaxbReordered.get(5),jaxbElements.get(0) );       // jaxbElement8
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();
    }

    public void testMultipleChildrenAndParents1() throws Exception {

        //---------+--------------------------------+-----------------------------------------------
        //  JAXB   |                                | 
        // ELEMENT | ARTIFACT                       | PARENTS
        //---------+--------------------------------+-----------------------------------------------
        //    1    | default/geronimo-app-1/2.2/ear | org.apache.geronimo.configs/system-database//car
        //         |                                | default/geronimo-web-3/1.0/car
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    3    | default/geronimo-web-1/1.0/car |
        //---------+--------------------------------+-----------------------------------------------
        //    4    | default/geronimo-web-2/1.0/car | default/geronimo-web-1/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    5    | default/geronimo-web-3/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    6    | default/geronimo-web-4/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-3/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    7    | default/geronimo-web-5/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    8    | default/geronimo-web-6/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-5/1.0/car
        //         |                                | default/geronimo-web-4/1.0/car
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        jaxbElements.add( (JAXBElement)jaxbElement1 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 5
        jaxbElements.add( (JAXBElement)jaxbElement8 );                  // Element 6
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement1
        assertEquals( jaxbReordered.get(1),jaxbElements.get(1) );       // jaxbElement3
        assertEquals( jaxbReordered.get(2),jaxbElements.get(2) );       // jaxbElement4
        assertEquals( jaxbReordered.get(3),jaxbElements.get(3) );       // jaxbElement5
        assertEquals( jaxbReordered.get(4),jaxbElements.get(4) );       // jaxbElement6
        assertEquals( jaxbReordered.get(5),jaxbElements.get(5) );       // jaxbElement7
        assertEquals( jaxbReordered.get(6),jaxbElements.get(6) );       // jaxbElement8
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        jaxbElements.add( (JAXBElement)jaxbElement8 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 5
        jaxbElements.add( (JAXBElement)jaxbElement1 );                  // Element 6
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(1) );       // jaxbElement7
        assertEquals( jaxbReordered.get(1),jaxbElements.get(3) );       // jaxbElement5
        assertEquals( jaxbReordered.get(2),jaxbElements.get(2) );       // jaxbElement6
        assertEquals( jaxbReordered.get(3),jaxbElements.get(5) );       // jaxbElement3
        assertEquals( jaxbReordered.get(4),jaxbElements.get(4) );       // jaxbElement4
        assertEquals( jaxbReordered.get(5),jaxbElements.get(0) );       // jaxbElement8
        assertEquals( jaxbReordered.get(6),jaxbElements.get(6) );       // jaxbElement1
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement8 );                  // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement1 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 5
        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 6
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement7
        assertEquals( jaxbReordered.get(1),jaxbElements.get(2) );       // jaxbElement5
        assertEquals( jaxbReordered.get(2),jaxbElements.get(4) );       // jaxbElement6
        assertEquals( jaxbReordered.get(3),jaxbElements.get(5) );       // jaxbElement3
        assertEquals( jaxbReordered.get(4),jaxbElements.get(6) );       // jaxbElement4
        assertEquals( jaxbReordered.get(5),jaxbElements.get(1) );       // jaxbElement8
        assertEquals( jaxbReordered.get(6),jaxbElements.get(3) );       // jaxbElement1
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();
    }

    public void testMultipleChildrenAndParents2() throws Exception {

        //---------+--------------------------------+-----------------------------------------------
        //  JAXB   |                                | 
        // ELEMENT | ARTIFACT                       | PARENTS
        //---------+--------------------------------+-----------------------------------------------
        //    1    | default/geronimo-app-1/2.2/ear | org.apache.geronimo.configs/system-database//car
        //         |                                | default/geronimo-web-3/1.0/car
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    2    | null                           | org.apache.derby/derby//jar
        //---------+--------------------------------+-----------------------------------------------
        //    3    | default/geronimo-web-1/1.0/car |
        //---------+--------------------------------+-----------------------------------------------
        //    4    | default/geronimo-web-2/1.0/car | default/geronimo-web-1/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    5    | default/geronimo-web-3/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    6    | default/geronimo-web-4/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-3/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    7    | default/geronimo-web-5/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    8    | default/geronimo-web-6/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-5/1.0/car
        //         |                                | default/geronimo-web-4/1.0/car
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        jaxbElements.add( (JAXBElement)jaxbElement1 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement2 );                  // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 5
        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 6
        jaxbElements.add( (JAXBElement)jaxbElement8 );                  // Element 7
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement1
        assertEquals( jaxbReordered.get(1),jaxbElements.get(1) );       // jaxbElement2
        assertEquals( jaxbReordered.get(2),jaxbElements.get(2) );       // jaxbElement3
        assertEquals( jaxbReordered.get(3),jaxbElements.get(3) );       // jaxbElement4
        assertEquals( jaxbReordered.get(4),jaxbElements.get(4) );       // jaxbElement5
        assertEquals( jaxbReordered.get(5),jaxbElements.get(5) );       // jaxbElement6
        assertEquals( jaxbReordered.get(6),jaxbElements.get(6) );       // jaxbElement7
        assertEquals( jaxbReordered.get(7),jaxbElements.get(7) );       // jaxbElement8
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        jaxbElements.add( (JAXBElement)jaxbElement8 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 5
        jaxbElements.add( (JAXBElement)jaxbElement2 );                  // Element 6
        jaxbElements.add( (JAXBElement)jaxbElement1 );                  // Element 7
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(1) );       // jaxbElement7
        assertEquals( jaxbReordered.get(1),jaxbElements.get(3) );       // jaxbElement5
        assertEquals( jaxbReordered.get(2),jaxbElements.get(2) );       // jaxbElement6
        assertEquals( jaxbReordered.get(3),jaxbElements.get(5) );       // jaxbElement3
        assertEquals( jaxbReordered.get(4),jaxbElements.get(4) );       // jaxbElement4
        assertEquals( jaxbReordered.get(5),jaxbElements.get(0) );       // jaxbElement8
        assertEquals( jaxbReordered.get(6),jaxbElements.get(6) );       // jaxbElement2
        assertEquals( jaxbReordered.get(7),jaxbElements.get(7) );       // jaxbElement1
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement8 );                  // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement1 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement2 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 5
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 6
        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 7
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement7
        assertEquals( jaxbReordered.get(1),jaxbElements.get(2) );       // jaxbElement5
        assertEquals( jaxbReordered.get(2),jaxbElements.get(5) );       // jaxbElement6
        assertEquals( jaxbReordered.get(3),jaxbElements.get(6) );       // jaxbElement3
        assertEquals( jaxbReordered.get(4),jaxbElements.get(7) );       // jaxbElement4
        assertEquals( jaxbReordered.get(5),jaxbElements.get(1) );       // jaxbElement8
        assertEquals( jaxbReordered.get(6),jaxbElements.get(3) );       // jaxbElement1
        assertEquals( jaxbReordered.get(7),jaxbElements.get(4) );       // jaxbElement2
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();
    }

    public void testCircularDependency() throws Exception {

        //---------+--------------------------------+-----------------------------------------------
        //  JAXB   |                                | 
        // ELEMENT | ARTIFACT                       | PARENTS
        //---------+--------------------------------+-----------------------------------------------
        //    1    | default/geronimo-app-1/2.2/ear | org.apache.geronimo.configs/system-database//car
        //         |                                | default/geronimo-web-3/1.0/car
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    2    | null                           | org.apache.derby/derby//jar
        //---------+--------------------------------+-----------------------------------------------
        //    3    | default/geronimo-web-1/1.0/car |
        //---------+--------------------------------+-----------------------------------------------
        //    4    | default/geronimo-web-2/1.0/car | default/geronimo-web-1/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    5    | default/geronimo-web-3/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    6    | default/geronimo-web-4/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-3/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    7    | default/geronimo-web-5/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    8    | default/geronimo-web-6/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-5/1.0/car
        //         |                                | default/geronimo-web-4/1.0/car
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    9    | default/openejb-jar-1/2.0/ear  | org.apache.geronimo.testsuite/agent-ds/2.2-SNAPSHOT/car
        //         |                                | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //   10    | default/openejb-jar-2/2.0/ear  | org.apache.geronimo.testsuite/agent-ds/2.2-SNAPSHOT/car
        //         |                                | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-5/1.0/car
        //         |                                | default/geronimo-ra-2/1.0/car 
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //   11    | default/geronimo-ra-2/1.0/car  | org.apache.derby/derby//jar 
        //         |                                | default/openejb-jar-2/2.0/ear
        //---------+--------------------------------+-----------------------------------------------
        jaxbElements.add( (JAXBElement)jaxbElement1 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement2 );                  // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 5
        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 6
        jaxbElements.add( (JAXBElement)jaxbElement8 );                  // Element 7
        jaxbElements.add( (JAXBElement)jaxbElement9 );                  // Element 8
        jaxbElements.add( (JAXBElement)jaxbElement10 );                 // Element 9
        jaxbElements.add( (JAXBElement)jaxbElement11 );                 // Element 10
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement1
        assertEquals( jaxbReordered.get(1),jaxbElements.get(1) );       // jaxbElement2
        assertEquals( jaxbReordered.get(2),jaxbElements.get(2) );       // jaxbElement3
        assertEquals( jaxbReordered.get(3),jaxbElements.get(3) );       // jaxbElement4
        assertEquals( jaxbReordered.get(4),jaxbElements.get(4) );       // jaxbElement5
        assertEquals( jaxbReordered.get(5),jaxbElements.get(5) );       // jaxbElement6
        assertEquals( jaxbReordered.get(6),jaxbElements.get(6) );       // jaxbElement7
        assertEquals( jaxbReordered.get(7),jaxbElements.get(7) );       // jaxbElement8
        assertEquals( jaxbReordered.get(8),jaxbElements.get(8) );       // jaxbElement9
        assertEquals( jaxbReordered.get(9),jaxbElements.get(9) );       // jaxbElement10
        assertEquals( jaxbReordered.get(10),jaxbElements.get(10) );     // jaxbElement11
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        jaxbElements.add( (JAXBElement)jaxbElement11 );                 // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement10 );                 // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement9 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement8 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 5
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 6
        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 7
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 8
        jaxbElements.add( (JAXBElement)jaxbElement2 );                  // Element 9
        jaxbElements.add( (JAXBElement)jaxbElement1 );                  // Element 10
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(4) );       // jaxbElement7
        assertEquals( jaxbReordered.get(1),jaxbElements.get(0) );       // jaxbElement3
        assertEquals( jaxbReordered.get(2),jaxbElements.get(8) );       // jaxbElement4
        assertEquals( jaxbReordered.get(3),jaxbElements.get(7) );       // jaxbElement11
        assertEquals( jaxbReordered.get(4),jaxbElements.get(1) );       // jaxbElement10
        assertEquals( jaxbReordered.get(5),jaxbElements.get(2) );       // jaxbElement9
        assertEquals( jaxbReordered.get(6),jaxbElements.get(6) );       // jaxbElement5
        assertEquals( jaxbReordered.get(7),jaxbElements.get(5) );       // jaxbElement6
        assertEquals( jaxbReordered.get(8),jaxbElements.get(3) );       // jaxbElement8
        assertEquals( jaxbReordered.get(9),jaxbElements.get(9) );       // jaxbElement2
        assertEquals( jaxbReordered.get(10),jaxbElements.get(10) );     // jaxbElement1
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement8 );                  // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement9 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement1 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement2 );                  // Element 5
        jaxbElements.add( (JAXBElement)jaxbElement11 );                 // Element 6
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 7
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 8
        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 9
        jaxbElements.add( (JAXBElement)jaxbElement10 );                 // Element 10
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement7
        assertEquals( jaxbReordered.get(1),jaxbElements.get(2) );       // jaxbElement5
        assertEquals( jaxbReordered.get(2),jaxbElements.get(7) );       // jaxbElement6
        assertEquals( jaxbReordered.get(3),jaxbElements.get(8) );       // jaxbElement3
        assertEquals( jaxbReordered.get(4),jaxbElements.get(9) );       // jaxbElement4
        assertEquals( jaxbReordered.get(5),jaxbElements.get(1) );       // jaxbElement8
        assertEquals( jaxbReordered.get(6),jaxbElements.get(3) );       // jaxbElement9
        assertEquals( jaxbReordered.get(7),jaxbElements.get(4) );       // jaxbElement1
        assertEquals( jaxbReordered.get(8),jaxbElements.get(5) );       // jaxbElement2
        assertEquals( jaxbReordered.get(9),jaxbElements.get(6) );       // jaxbElement11
        assertEquals( jaxbReordered.get(10),jaxbElements.get(10) );     // jaxbElement10
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();
    }


    public void testCircularDependencyOnSelf() throws Exception {

        //---------+--------------------------------+-----------------------------------------------
        //  JAXB   |                                | 
        // ELEMENT | ARTIFACT                       | PARENTS
        //---------+--------------------------------+-----------------------------------------------
        //   12    | default/geronimo-web-7/1.0/car | default/geronimo-web-7/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        jaxbElements.add( (JAXBElement)jaxbElement12 );                 // Element 0
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement12
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        //---------+--------------------------------+-----------------------------------------------
        //  JAXB   |                                | 
        // ELEMENT | ARTIFACT                       | PARENTS
        //---------+--------------------------------+-----------------------------------------------
        //    3    | default/geronimo-web-1/1.0/car |
        //---------+--------------------------------+-----------------------------------------------
        //   12    | default/geronimo-web-7/1.0/car | default/geronimo-web-7/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement12 );                 // Element 1
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement3
        assertEquals( jaxbReordered.get(1),jaxbElements.get(1) );       // jaxbElement12
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        //---------+--------------------------------+-----------------------------------------------
        //  JAXB   |                                | 
        // ELEMENT | ARTIFACT                       | PARENTS
        //---------+--------------------------------+-----------------------------------------------
        //    1    | default/geronimo-app-1/2.2/ear | org.apache.geronimo.configs/system-database//car
        //         |                                | default/geronimo-web-3/1.0/car
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    2    | null                           | org.apache.derby/derby//jar
        //---------+--------------------------------+-----------------------------------------------
        //    3    | default/geronimo-web-1/1.0/car |
        //---------+--------------------------------+-----------------------------------------------
        //    4    | default/geronimo-web-2/1.0/car | default/geronimo-web-1/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    5    | default/geronimo-web-3/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    6    | default/geronimo-web-4/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-3/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    7    | default/geronimo-web-5/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    8    | default/geronimo-web-6/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-5/1.0/car
        //         |                                | default/geronimo-web-4/1.0/car
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    9    | default/openejb-jar-1/2.0/ear  | org.apache.geronimo.testsuite/agent-ds/2.2-SNAPSHOT/car
        //         |                                | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //   10    | default/openejb-jar-2/2.0/ear  | org.apache.geronimo.testsuite/agent-ds/2.2-SNAPSHOT/car
        //         |                                | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-5/1.0/car
        //         |                                | default/geronimo-ra-2/1.0/car 
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //   11    | default/geronimo-ra-2/1.0/car  | org.apache.derby/derby//jar 
        //         |                                | default/openejb-jar-2/2.0/ear
        //---------+--------------------------------+-----------------------------------------------
        //   12    | default/geronimo-web-7/1.0/car | default/geronimo-web-7/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        jaxbElements.add( (JAXBElement)jaxbElement1 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement2 );                  // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement4 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 5
        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 6
        jaxbElements.add( (JAXBElement)jaxbElement8 );                  // Element 7
        jaxbElements.add( (JAXBElement)jaxbElement9 );                  // Element 8
        jaxbElements.add( (JAXBElement)jaxbElement10 );                 // Element 9
        jaxbElements.add( (JAXBElement)jaxbElement11 );                 // Element 10
        jaxbElements.add( (JAXBElement)jaxbElement12 );                 // Element 11
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement1
        assertEquals( jaxbReordered.get(1),jaxbElements.get(1) );       // jaxbElement2
        assertEquals( jaxbReordered.get(2),jaxbElements.get(2) );       // jaxbElement3
        assertEquals( jaxbReordered.get(3),jaxbElements.get(3) );       // jaxbElement4
        assertEquals( jaxbReordered.get(4),jaxbElements.get(4) );       // jaxbElement5
        assertEquals( jaxbReordered.get(5),jaxbElements.get(5) );       // jaxbElement6
        assertEquals( jaxbReordered.get(6),jaxbElements.get(6) );       // jaxbElement7
        assertEquals( jaxbReordered.get(7),jaxbElements.get(7) );       // jaxbElement8
        assertEquals( jaxbReordered.get(8),jaxbElements.get(8) );       // jaxbElement9
        assertEquals( jaxbReordered.get(9),jaxbElements.get(9) );       // jaxbElement10
        assertEquals( jaxbReordered.get(10),jaxbElements.get(10) );     // jaxbElement11
        assertEquals( jaxbReordered.get(11),jaxbElements.get(11) );     // jaxbElement12
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        //---------+--------------------------------+-----------------------------------------------
        //  JAXB   |                                | 
        // ELEMENT | ARTIFACT                       | PARENTS
        //---------+--------------------------------+-----------------------------------------------
        //    1    | default/geronimo-app-1/2.2/ear | org.apache.geronimo.configs/system-database//car
        //         |                                | default/geronimo-web-3/1.0/car
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    2    | null                           | org.apache.derby/derby//jar
        //---------+--------------------------------+-----------------------------------------------
        //    3    | default/geronimo-web-1/1.0/car |
        //---------+--------------------------------+-----------------------------------------------
        //    4    | default/geronimo-web-7/1.0/car | default/geronimo-web-7/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    5    | default/geronimo-web-3/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    6    | default/geronimo-web-4/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-3/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    7    | default/geronimo-web-5/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    8    | default/geronimo-web-6/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-5/1.0/car
        //         |                                | default/geronimo-web-4/1.0/car
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    9    | default/openejb-jar-1/2.0/ear  | org.apache.geronimo.testsuite/agent-ds/2.2-SNAPSHOT/car
        //         |                                | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //   10    | default/openejb-jar-2/2.0/ear  | org.apache.geronimo.testsuite/agent-ds/2.2-SNAPSHOT/car
        //         |                                | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-5/1.0/car
        //         |                                | default/geronimo-ra-2/1.0/car 
        //         |                                | default/geronimo-web-2/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //   11    | default/geronimo-ra-2/1.0/car  | org.apache.derby/derby//jar 
        //         |                                | default/openejb-jar-2/2.0/ear
        //---------+--------------------------------+-----------------------------------------------
        jaxbElements.add( (JAXBElement)jaxbElement1 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement2 );                  // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement12 );                 // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 5
        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 6
        jaxbElements.add( (JAXBElement)jaxbElement8 );                  // Element 7
        jaxbElements.add( (JAXBElement)jaxbElement9 );                  // Element 8
        jaxbElements.add( (JAXBElement)jaxbElement10 );                 // Element 9
        jaxbElements.add( (JAXBElement)jaxbElement11 );                 // Element 10
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement1
        assertEquals( jaxbReordered.get(1),jaxbElements.get(1) );       // jaxbElement2
        assertEquals( jaxbReordered.get(2),jaxbElements.get(2) );       // jaxbElement3
        assertEquals( jaxbReordered.get(3),jaxbElements.get(3) );       // jaxbElement12
        assertEquals( jaxbReordered.get(4),jaxbElements.get(4) );       // jaxbElement5
        assertEquals( jaxbReordered.get(5),jaxbElements.get(5) );       // jaxbElement6
        assertEquals( jaxbReordered.get(6),jaxbElements.get(6) );       // jaxbElement7
        assertEquals( jaxbReordered.get(7),jaxbElements.get(7) );       // jaxbElement8
        assertEquals( jaxbReordered.get(8),jaxbElements.get(8) );       // jaxbElement9
        assertEquals( jaxbReordered.get(9),jaxbElements.get(9) );       // jaxbElement10
        assertEquals( jaxbReordered.get(10),jaxbElements.get(10) );     // jaxbElement11
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();
                        
        //---------+--------------------------------+-----------------------------------------------
        //  JAXB   |                                | 
        // ELEMENT | ARTIFACT                       | PARENTS
        //---------+--------------------------------+-----------------------------------------------
        //    3    | default/geronimo-web-1/1.0/car |
        //---------+--------------------------------+-----------------------------------------------
        //   12    | default/geronimo-web-7/1.0/car | default/geronimo-web-7/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    5    | default/geronimo-web-3/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //    6    | default/geronimo-web-4/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-3/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        //    7    | default/geronimo-web-5/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //---------+--------------------------------+-----------------------------------------------
        //   13    | default/geronimo-web-8/1.0/car | org.apache.geronimo.configs/tomcat6/2.2-SNAPSHOT/car
        //         |                                | default/geronimo-web-5/1.0/car
        //         |                                | default/geronimo-web-4/1.0/car
        //         |                                | default/geronimo-web-7/1.0/car
        //---------+--------------------------------+-----------------------------------------------
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 0
        jaxbElements.add( (JAXBElement)jaxbElement12 );                 // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement13 );                 // Element 5
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );       
        assertEquals( jaxbReordered.get(0),jaxbElements.get(0) );       // jaxbElement3
        assertEquals( jaxbReordered.get(1),jaxbElements.get(1) );       // jaxbElement12
        assertEquals( jaxbReordered.get(2),jaxbElements.get(2) );       // jaxbElement5
        assertEquals( jaxbReordered.get(3),jaxbElements.get(3) );       // jaxbElement6
        assertEquals( jaxbReordered.get(4),jaxbElements.get(4) );       // jaxbElement7
        assertEquals( jaxbReordered.get(5),jaxbElements.get(5) );       // jaxbElement13
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();

        jaxbElements.add( (JAXBElement)jaxbElement13 );                 // Element 0  
        jaxbElements.add( (JAXBElement)jaxbElement7 );                  // Element 1
        jaxbElements.add( (JAXBElement)jaxbElement6 );                  // Element 2
        jaxbElements.add( (JAXBElement)jaxbElement5 );                  // Element 3
        jaxbElements.add( (JAXBElement)jaxbElement12 );                 // Element 4
        jaxbElements.add( (JAXBElement)jaxbElement3 );                  // Element 5
        jaxbReordered = dh.reorderJAXBElements( jaxbElements );
        assertEquals( jaxbReordered.size(),jaxbElements.size() );
        assertEquals( jaxbReordered.get(0),jaxbElements.get(1) );       // jaxbElement12
        assertEquals( jaxbReordered.get(1),jaxbElements.get(3) );       // jaxbElement7
        assertEquals( jaxbReordered.get(2),jaxbElements.get(2) );       // jaxbElement5
        assertEquals( jaxbReordered.get(3),jaxbElements.get(4) );       // jaxbElement6
        assertEquals( jaxbReordered.get(4),jaxbElements.get(0) );       // jaxbElement13
        assertEquals( jaxbReordered.get(5),jaxbElements.get(5) );       // jaxbElement2
        jaxbElements.clear();
        jaxbReordered.clear();
        dh.close();
    }


    /*--------------------------------------------------------------------------------------------*\
    |                                                                                              |
    |  Private method(s)                                                                           |
    |                                                                                              |
    \*--------------------------------------------------------------------------------------------*/ 

    private static String readContent(InputStream in) throws IOException {
        StringBuffer sb = new StringBuffer();
        in = new BufferedInputStream(in);
        int i = in.read();
        while (i != -1) {
            sb.append((char) i);
            i = in.read();
        }
        String content = sb.toString();
        return content;
    }
}
