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
 *                 The type used for the <type-converters> element.  The
 *                 <type-converters> section is a set of <bean>, <ref>, or
 *                 <reference> elements that identify the type converter components.
 *                 
 *             
 * 
 * <p>Java class for Ttype-converters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Ttype-converters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="bean" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tbean"/>
 *         &lt;element name="reference" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Treference"/>
 *         &lt;element name="ref" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tref"/>
 *         &lt;any/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Ttype-converters", propOrder = {
    "beanOrReferenceOrRef"
})
@XmlRootElement(name = "typeConverters")
public class TtypeConverters {

    @XmlElementRefs({
        @XmlElementRef(name = "bean",      namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = JAXBElement.class),
        @XmlElementRef(name = "reference", namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = JAXBElement.class),
        @XmlElementRef(name = "ref",       namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = JAXBElement.class)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> beanOrReferenceOrRef;

    /**
     * Gets the value of the beanOrReferenceOrRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the beanOrReferenceOrRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBeanOrReferenceOrRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Tbean }{@code >}
     * {@link JAXBElement }{@code <}{@link Tref }{@code >}
     * {@link JAXBElement }{@code <}{@link Treference }{@code >}
     * {@link Object }
     * 
     * 
     */
    public List<Object> getBeanOrReferenceOrRef() {
        if (beanOrReferenceOrRef == null) {
            beanOrReferenceOrRef = new ArrayList<Object>();
        }
        return this.beanOrReferenceOrRef;
    }

}
