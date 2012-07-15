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
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                 
 *                 Tservice is the type for services exported by this blueprint bundle.
 *                 Services are sourced by either a <ref> to a <bean> component or an
 *                 <inline> bean component.
 *                 
 *             
 * 
 * <p>Java class for Tservice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tservice">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tcomponent">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.osgi.org/xmlns/blueprint/v1.0.0}GserviceElements"/>
 *       &lt;/sequence>
 *       &lt;attribute name="auto-export" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}TautoExportModes" default="disabled" />
 *       &lt;attribute name="interface" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tclass" />
 *       &lt;attribute name="ranking" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *       &lt;attribute name="ref" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tidref" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tservice", propOrder = {
    "description",
    "interfaces",
    "serviceProperties",
    "registrationListener",
    "bean",
    "reference",
    "ref",
    "any"
})
@XmlRootElement(name = "service")
public class Tservice extends Tcomponent {

    protected Tdescription description;
    protected Tinterfaces interfaces;
    @XmlElement(name = "service-properties")
    protected TserviceProperties serviceProperties;
    @XmlElement(name = "registration-listener")
    protected List<TregistrationListener> registrationListener;
    protected TinlinedBean bean;
    protected TinlinedReference reference;
    protected Tref ref;
    @XmlAnyElement(lax = true)
    protected Object any;
    @XmlAttribute(name = "auto-export")
    protected TautoExportModes autoExport;
    @XmlAttribute(name = "interface")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String _interface;
    @XmlAttribute
    protected Integer ranking;
    @XmlAttribute(name = "ref")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String refAttribute;

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
     * Gets the value of the interfaces property.
     * 
     * @return
     *     possible object is
     *     {@link Tinterfaces }
     *     
     */
    public Tinterfaces getInterfaces() {
        return interfaces;
    }

    /**
     * Sets the value of the interfaces property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tinterfaces }
     *     
     */
    public void setInterfaces(Tinterfaces value) {
        this.interfaces = value;
    }

    /**
     * Gets the value of the serviceProperties property.
     * 
     * @return
     *     possible object is
     *     {@link TserviceProperties }
     *     
     */
    public TserviceProperties getServiceProperties() {
        return serviceProperties;
    }

    /**
     * Sets the value of the serviceProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link TserviceProperties }
     *     
     */
    public void setServiceProperties(TserviceProperties value) {
        this.serviceProperties = value;
    }

    /**
     * Gets the value of the registrationListener property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the registrationListener property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegistrationListener().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TregistrationListener }
     * 
     * 
     */
    public List<TregistrationListener> getRegistrationListener() {
        if (registrationListener == null) {
            registrationListener = new ArrayList<TregistrationListener>();
        }
        return this.registrationListener;
    }

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
     * Gets the value of the autoExport property.
     * 
     * @return
     *     possible object is
     *     {@link TautoExportModes }
     *     
     */
    public TautoExportModes getAutoExport() {
        if (autoExport == null) {
            return TautoExportModes.DISABLED;
        } else {
            return autoExport;
        }
    }

    /**
     * Sets the value of the autoExport property.
     * 
     * @param value
     *     allowed object is
     *     {@link TautoExportModes }
     *     
     */
    public void setAutoExport(TautoExportModes value) {
        this.autoExport = value;
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

    /**
     * Gets the value of the ranking property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getRanking() {
        if (ranking == null) {
            return  0;
        } else {
            return ranking;
        }
    }

    /**
     * Sets the value of the ranking property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRanking(Integer value) {
        this.ranking = value;
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

}
