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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                 
 *                     The Tcomponent type is the base type for top-level
 *                     Blueprint components.  The <bean> <reference>, <service>,
 *                     and <reference-list> elements are all derived from
 *                     the Tcomponent type.  This type defines an id attribute
 *                     that is used create references between different components.
 *                     Component elements can also be inlined within other component
 *                     definitions.  The id attribute is not valid when inlined.
 *                 
 *             
 * 
 * <p>Java class for Tcomponent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tcomponent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="activation" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tactivation" />
 *       &lt;attribute name="depends-on" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}TdependsOn" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tcomponent")
@XmlRootElement(name = "component")
public abstract class Tcomponent {

    @XmlAttribute
    protected Tactivation activation;
    @XmlAttribute(name = "depends-on")
    protected List<String> dependsOn;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;

    /**
     * Gets the value of the activation property.
     * 
     * @return
     *     possible object is
     *     {@link Tactivation }
     *     
     */
    public Tactivation getActivation() {
        return activation;
    }

    /**
     * Sets the value of the activation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tactivation }
     *     
     */
    public void setActivation(Tactivation value) {
        this.activation = value;
    }

    /**
     * Gets the value of the dependsOn property. 
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dependsOn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDependsOn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDependsOn() {
        if (dependsOn == null) {
            dependsOn = new ArrayList<String>();
        }
        return this.dependsOn;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
