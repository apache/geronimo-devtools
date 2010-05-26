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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                 
 *                 Tmap is the base type used for <map> elements.  A map may have a
 *                 default value type specified, so it inherits from the TtypeCollection
 *                 type.  A key type can also be specified, and the map members are
 *                 created from the entry elements, which require a key/value pair.
 *                 
 *             
 * 
 * <p>Java class for Tmap complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tmap">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.osgi.org/xmlns/blueprint/v1.0.0}TtypedCollection">
 *       &lt;sequence>
 *         &lt;element name="entry" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}TmapEntry" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="key-type" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Ttype" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tmap", propOrder = {
    "entry"
})
@XmlRootElement(name = "map")
public class Tmap extends TtypedCollection {

    protected List<TmapEntry> entry;
    @XmlAttribute(name = "key-type")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String keyType;

    /**
     * Gets the value of the entry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TmapEntry }
     * 
     * 
     */
    public List<TmapEntry> getEntry() {
        if (entry == null) {
            entry = new ArrayList<TmapEntry>();
        }
        return this.entry;
    }

    /**
     * Gets the value of the keyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyType() {
        return keyType;
    }

    /**
     * Sets the value of the keyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyType(String value) {
        this.keyType = value;
    }

}
