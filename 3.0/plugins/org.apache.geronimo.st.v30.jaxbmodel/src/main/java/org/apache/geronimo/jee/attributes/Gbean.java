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

package org.apache.geronimo.jee.attributes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 Element used by the plugin system to list individual GBeans.
 *                 Note that the name attribute for a gbean element may hold either
 *                 the full GBeanName, or only the value for the "name=" portion of
 *                 the GBeanName. If there are multiple GBeans in the module with
 *                 manageable attributes and the same "name=" portion of the
 *                 GBeanName, then all must be listed and all must be listed with a
 *                 full GBeanName.
 *             
 * 
 * <p>Java class for gbeanType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="gbeanType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://geronimo.apache.org/xml/ns/attributes-1.2}attribute"/>
 *           &lt;element name="reference" type="{http://geronimo.apache.org/xml/ns/attributes-1.2}referenceType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="gbeanInfo" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="load" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gbeanType", propOrder = {
    "comment",
    "attributeOrReference"
})
public class Gbean
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    protected String comment;
    @XmlElements({
        @XmlElement(name = "attribute", type = Attribute.class),
        @XmlElement(name = "reference", type = Reference.class)
    })
    protected List<Serializable> attributeOrReference;
    @XmlAttribute
    protected String gbeanInfo;
    @XmlAttribute
    protected Boolean load;
    @XmlAttribute(required = true)
    protected String name;

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the attributeOrReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributeOrReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributeOrReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Attribute }
     * {@link Reference }
     * 
     * 
     */
    public List<Serializable> getAttributeOrReference() {
        if (attributeOrReference == null) {
            attributeOrReference = new ArrayList<Serializable>();
        }
        return this.attributeOrReference;
    }

    /**
     * Gets the value of the gbeanInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGbeanInfo() {
        return gbeanInfo;
    }

    /**
     * Sets the value of the gbeanInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGbeanInfo(String value) {
        this.gbeanInfo = value;
    }

    /**
     * Gets the value of the load property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isLoad() {
        if (load == null) {
            return true;
        } else {
            return load;
        }
    }

    /**
     * Sets the value of the load property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLoad(Boolean value) {
        this.load = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
