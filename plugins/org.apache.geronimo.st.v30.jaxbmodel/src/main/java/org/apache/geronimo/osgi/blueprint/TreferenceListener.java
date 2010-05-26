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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                 
 *                 TReferenceListener defines a reference listener that is attached
 *                 to a <reference> or <reference-list> element.  The listener
 *                 object can be specified as a <ref> or as an inline <bean> or
 *                 <reference> component.  Listener events are mapped to the indicated
 *                 bind or unbind methods.
 *                 
 *             
 * 
 * <p>Java class for TreferenceListener complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TreferenceListener">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.osgi.org/xmlns/blueprint/v1.0.0}GtargetComponent" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="bind-method" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tmethod" />
 *       &lt;attribute name="ref" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tidref" />
 *       &lt;attribute name="unbind-method" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tmethod" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TreferenceListener", propOrder = {
    "bean",
    "reference",
    "ref",
    "any"
})
@XmlRootElement(name = "referenceListener")
public class TreferenceListener {

    protected TinlinedBean bean;
    protected TinlinedReference reference;
    protected Tref ref;
    @XmlAnyElement(lax = true)
    protected Object any;
    @XmlAttribute(name = "bind-method")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String bindMethod;
    @XmlAttribute(name = "ref")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String refAttribute;
    @XmlAttribute(name = "unbind-method")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String unbindMethod;

    /**
     * Gets the value of the bean property.
     * 
     * @return
     *     possible object is
     *     {@link TinlinedBean }
     *     
     */
    public TinlinedBean getBean() {
        return bean;
    }

    /**
     * Sets the value of the bean property.
     * 
     * @param value
     *     allowed object is
     *     {@link TinlinedBean }
     *     
     */
    public void setBean(TinlinedBean value) {
        this.bean = value;
    }

    /**
     * Gets the value of the reference property.
     * 
     * @return
     *     possible object is
     *     {@link TinlinedReference }
     *     
     */
    public TinlinedReference getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     * 
     * @param value
     *     allowed object is
     *     {@link TinlinedReference }
     *     
     */
    public void setReference(TinlinedReference value) {
        this.reference = value;
    }

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link Tref }
     *     
     */
    public Tref getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tref }
     *     
     */
    public void setRef(Tref value) {
        this.ref = value;
    }

    /**
     * Gets the value of the any property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getAny() {
        return any;
    }

    /**
     * Sets the value of the any property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setAny(Object value) {
        this.any = value;
    }

    /**
     * Gets the value of the bindMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBindMethod() {
        return bindMethod;
    }

    /**
     * Sets the value of the bindMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBindMethod(String value) {
        this.bindMethod = value;
    }

    /**
     * Gets the value of the refAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefAttribute() {
        return refAttribute;
    }

    /**
     * Sets the value of the refAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefAttribute(String value) {
        this.refAttribute = value;
    }

    /**
     * Gets the value of the unbindMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnbindMethod() {
        return unbindMethod;
    }

    /**
     * Sets the value of the unbindMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnbindMethod(String value) {
        this.unbindMethod = value;
    }

}
