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
package org.apache.geronimo.osgi.blueprint;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 
 *                 Tcollection is the base schema type for different ordered collection
 *                 types.  This is shared between the <array>, <list>, and <set> elements.
 *                 
 *             
 * 
 * <p>Java class for Tcollection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tcollection">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.osgi.org/xmlns/blueprint/v1.0.0}TtypedCollection">
 *       &lt;group ref="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Gvalue" maxOccurs="unbounded" minOccurs="0"/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tcollection", propOrder = {
    "gvalue"
})
@XmlRootElement(name = "collection")
public class Tcollection extends TtypedCollection {

    @XmlElementRefs({
        @XmlElementRef(name = "list",           namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = Tcollection.class),
        @XmlElementRef(name = "null",           namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = Tnull.class),
        @XmlElementRef(name = "value",          namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = Tvalue.class),
        @XmlElementRef(name = "service",        namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = Tservice.class),
        @XmlElementRef(name = "array",          namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = Tcollection.class),
        @XmlElementRef(name = "reference",      namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = Treference.class),
        @XmlElementRef(name = "idref",          namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = Tref.class),
        @XmlElementRef(name = "bean",           namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = Tbean.class),
        @XmlElementRef(name = "props",          namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = Tprops.class),
        @XmlElementRef(name = "map",            namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = Tmap.class),
        @XmlElementRef(name = "ref",            namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = Tref.class),
        @XmlElementRef(name = "set",            namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = Tcollection.class),
        @XmlElementRef(name = "reference-list", namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = TreferenceList.class)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> gvalue;

    /**
     * Gets the value of the gvalue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gvalue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGvalue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link TinlinedBean }{@code >}
     * {@link JAXBElement }{@code <}{@link Tvalue }{@code >}
     * {@link JAXBElement }{@code <}{@link Tcollection }{@code >}
     * {@link JAXBElement }{@code <}{@link Tmap }{@code >}
     * {@link JAXBElement }{@code <}{@link TinlinedReferenceList }{@code >}
     * {@link JAXBElement }{@code <}{@link Tref }{@code >}
     * {@link JAXBElement }{@code <}{@link TinlinedReference }{@code >}
     * {@link JAXBElement }{@code <}{@link Tcollection }{@code >}
     * {@link JAXBElement }{@code <}{@link Tprops }{@code >}
     * {@link JAXBElement }{@code <}{@link Tref }{@code >}
     * {@link JAXBElement }{@code <}{@link Tnull }{@code >}
     * {@link JAXBElement }{@code <}{@link TinlinedService }{@code >}
     * {@link Object }
     * {@link JAXBElement }{@code <}{@link Tcollection }{@code >}
     * 
     * 
     */
    public List<Object> getGvalue() {
        if (gvalue == null) {
            gvalue = new ArrayList<Object>();
        }
        return this.gvalue;
    }

}
