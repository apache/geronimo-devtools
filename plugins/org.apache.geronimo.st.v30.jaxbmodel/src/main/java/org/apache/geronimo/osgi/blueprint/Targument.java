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

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                 
 *                 An argument used to create an object defined by a <bean>
 *                 component.  The <argument> elements are the arguments for the
 *                 bean class constructor or passed to the bean factory method.
 * 
 *                 The type, if specified, is used to disambiguate the constructor
 *                 or method signature.  Arguments may also be matched up with
 *                 arguments by explicitly specifying the index position.  If the
 *                 index is used, then all <argument> elements for the bean must
 *                 also specify the index.
 * 
 *                 The value and ref attributes are convenience shortcuts to make
 *                 the <argument> tag easier to code.  A fuller set of injected
 *                 values and types can be specified using one of the "value"
 *                 type elements.
 *                 
 *             
 * 
 * <p>Java class for Targument complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Targument">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tdescription" minOccurs="0"/>
 *         &lt;group ref="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Gvalue" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="index" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="ref" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tidref" />
 *       &lt;attribute name="type" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Ttype" />
 *       &lt;attribute name="value" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}TstringValue" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Targument", propOrder = {
    "description",
    "service",
    "referenceList",
    "bean",
    "reference",
    "ref",
    "any",
    "idref",
    "value",
    "list",
    "set",
    "map",
    "array",
    "props",
    "_null"
})
@XmlRootElement(name = "argument")
public class Targument {

    protected Tdescription description;
    protected TinlinedService service;
    @XmlElement(name = "reference-list")
    protected TinlinedReferenceList referenceList;
    protected TinlinedBean bean;
    protected TinlinedReference reference;
    protected Tref ref;
    @XmlAnyElement(lax = true)
    protected Object any;
    protected Tref idref;
    protected Tvalue value;
    protected Tcollection list;
    protected Tcollection set;
    protected Tmap map;
    protected Tcollection array;
    protected Tprops props;
    @XmlElement(name = "null")
    protected Tnull _null;
    @XmlAttribute
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger index;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;
    @XmlAttribute(name = "ref")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String refAttribute;
    @XmlAttribute(name = "value")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String valueAttribute;

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
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link TinlinedService }
     *     
     */
    public TinlinedService getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link TinlinedService }
     *     
     */
    public void setService(TinlinedService value) {
        this.service = value;
    }

    /**
     * Gets the value of the referenceList property.
     * 
     * @return
     *     possible object is
     *     {@link TinlinedReferenceList }
     *     
     */
    public TinlinedReferenceList getReferenceList() {
        return referenceList;
    }

    /**
     * Sets the value of the referenceList property.
     * 
     * @param value
     *     allowed object is
     *     {@link TinlinedReferenceList }
     *     
     */
    public void setReferenceList(TinlinedReferenceList value) {
        this.referenceList = value;
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
     * Gets the value of the idref property.
     * 
     * @return
     *     possible object is
     *     {@link Tref }
     *     
     */
    public Tref getIdref() {
        return idref;
    }

    /**
     * Sets the value of the idref property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tref }
     *     
     */
    public void setIdref(Tref value) {
        this.idref = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link Tvalue }
     *     
     */
    public Tvalue getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tvalue }
     *     
     */
    public void setValue(Tvalue value) {
        this.value = value;
    }

    /**
     * Gets the value of the list property.
     * 
     * @return
     *     possible object is
     *     {@link Tcollection }
     *     
     */
    public Tcollection getList() {
        return list;
    }

    /**
     * Sets the value of the list property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tcollection }
     *     
     */
    public void setList(Tcollection value) {
        this.list = value;
    }

    /**
     * Gets the value of the set property.
     * 
     * @return
     *     possible object is
     *     {@link Tcollection }
     *     
     */
    public Tcollection getSet() {
        return set;
    }

    /**
     * Sets the value of the set property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tcollection }
     *     
     */
    public void setSet(Tcollection value) {
        this.set = value;
    }

    /**
     * Gets the value of the map property.
     * 
     * @return
     *     possible object is
     *     {@link Tmap }
     *     
     */
    public Tmap getMap() {
        return map;
    }

    /**
     * Sets the value of the map property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tmap }
     *     
     */
    public void setMap(Tmap value) {
        this.map = value;
    }

    /**
     * Gets the value of the array property.
     * 
     * @return
     *     possible object is
     *     {@link Tcollection }
     *     
     */
    public Tcollection getArray() {
        return array;
    }

    /**
     * Sets the value of the array property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tcollection }
     *     
     */
    public void setArray(Tcollection value) {
        this.array = value;
    }

    /**
     * Gets the value of the props property.
     * 
     * @return
     *     possible object is
     *     {@link Tprops }
     *     
     */
    public Tprops getProps() {
        return props;
    }

    /**
     * Sets the value of the props property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tprops }
     *     
     */
    public void setProps(Tprops value) {
        this.props = value;
    }

    /**
     * Gets the value of the null property.
     * 
     * @return
     *     possible object is
     *     {@link Tnull }
     *     
     */
    public Tnull getNull() {
        return _null;
    }

    /**
     * Sets the value of the null property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tnull }
     *     
     */
    public void setNull(Tnull value) {
        this._null = value;
    }

    /**
     * Gets the value of the index property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIndex(BigInteger value) {
        this.index = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
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
     * Gets the value of the valueAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValueAttribute() {
        return valueAttribute;
    }

    /**
     * Sets the value of the valueAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValueAttribute(String value) {
        this.valueAttribute = value;
    }

}
