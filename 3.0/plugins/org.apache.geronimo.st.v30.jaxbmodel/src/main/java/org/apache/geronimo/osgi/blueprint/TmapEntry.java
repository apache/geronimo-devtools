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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                 
 *                 TmapEntry is used for <entry> elements nested inside of a <map> element.
 *                 Each <entry> instance defines a key/value pair that will be added to the
 *                 Map.  Both the keys and values may be arbitrary types.  Keys must not
 *                 be <null> but <null> is permitted for entry values.  A default type
 *                 can be specified for both the keys and the values, but individual keys
 *                 or values can override the default.
 *                 
 *             
 * 
 * <p>Java class for TmapEntry complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TmapEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="key" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tkey" minOccurs="0"/>
 *         &lt;group ref="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Gvalue" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="key-ref" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tidref" />
 *       &lt;attribute name="key" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}TstringValue" />
 *       &lt;attribute name="value-ref" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tidref" />
 *       &lt;attribute name="value" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}TstringValue" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TmapEntry", propOrder = {
    "keyAttribute",
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
@XmlRootElement(name = "mapEntry")
public class TmapEntry {

    @XmlElement(name = "key")
    protected Tkey keyAttribute;
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
    @XmlAttribute(name = "key-ref")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String keyRef;
    @XmlAttribute
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String key;
    @XmlAttribute(name = "value-ref")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String valueRef;
    @XmlAttribute(name = "value")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String valueAttribute;

    /**
     * Gets the value of the keyAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link Tkey }
     *     
     */
    public Tkey getKeyAttribute() {
        return keyAttribute;
    }

    /**
     * Sets the value of the keyAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tkey }
     *     
     */
    public void setKeyAttribute(Tkey value) {
        this.keyAttribute = value;
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
     * Gets the value of the keyRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyRef() {
        return keyRef;
    }

    /**
     * Sets the value of the keyRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyRef(String value) {
        this.keyRef = value;
    }

    /**
     * Gets the value of the key property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * Gets the value of the valueRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValueRef() {
        return valueRef;
    }

    /**
     * Sets the value of the valueRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValueRef(String value) {
        this.valueRef = value;
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
