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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                 
 *                 TserviceReference is the base element type used for <reference>
 *                 and <reference-list> elements.  This type defines all of the
 *                 characteristics common to both sorts of references.
 *                 
 *             
 * 
 * <p>Java class for TserviceReference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TserviceReference">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tcomponent">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.osgi.org/xmlns/blueprint/v1.0.0}GserviceReferenceElements"/>
 *       &lt;/sequence>
 *       &lt;attribute name="availability" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tavailability" />
 *       &lt;attribute name="component-name" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tidref" />
 *       &lt;attribute name="filter" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *       &lt;attribute name="interface" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tclass" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TserviceReference", propOrder = {
    "description",
    "referenceListener"
})
@XmlRootElement(name = "serviceReference")
public class TserviceReference extends Tcomponent {

    protected Tdescription description;
    @XmlElement(name = "reference-listener")
    protected List<TreferenceListener> referenceListener;
    @XmlAttribute
    protected Tavailability availability;
    @XmlAttribute(name = "component-name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String componentName;
    @XmlAttribute
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String filter;
    @XmlAttribute(name = "interface")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String _interface;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Tdescription }
     *     
     */
    public Tdescription getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tdescription }
     *     
     */
    public void setDescription(Tdescription value) {
        this.description = value;
    }

    /**
     * Gets the value of the referenceListener property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the referenceListener property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReferenceListener().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TreferenceListener }
     * 
     * 
     */
    public List<TreferenceListener> getReferenceListener() {
        if (referenceListener == null) {
            referenceListener = new ArrayList<TreferenceListener>();
        }
        return this.referenceListener;
    }

    /**
     * Gets the value of the availability property.
     * 
     * @return
     *     possible object is
     *     {@link Tavailability }
     *     
     */
    public Tavailability getAvailability() {
        return availability;
    }

    /**
     * Sets the value of the availability property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tavailability }
     *     
     */
    public void setAvailability(Tavailability value) {
        this.availability = value;
    }

    /**
     * Gets the value of the componentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * Sets the value of the componentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComponentName(String value) {
        this.componentName = value;
    }

    /**
     * Gets the value of the filter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Sets the value of the filter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilter(String value) {
        this.filter = value;
    }

    /**
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterface(String value) {
        this._interface = value;
    }

}
