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

package org.apache.geronimo.jee.connector;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for singlepoolType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="singlepoolType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="max-size" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="min-size" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="blocking-timeout-milliseconds" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="idle-timeout-minutes" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="match-one" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}emptyType"/>
 *           &lt;element name="match-all" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}emptyType"/>
 *           &lt;element name="select-one-assume-match" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}emptyType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "singlepoolType", propOrder = {
    "maxSize",
    "minSize",
    "blockingTimeoutMilliseconds",
    "idleTimeoutMinutes",
    "matchOne",
    "matchAll",
    "selectOneAssumeMatch"
})
public class Singlepool
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "max-size")
    protected Integer maxSize;
    @XmlElement(name = "min-size")
    protected Integer minSize;
    @XmlElement(name = "blocking-timeout-milliseconds")
    protected Integer blockingTimeoutMilliseconds;
    @XmlElement(name = "idle-timeout-minutes")
    protected Integer idleTimeoutMinutes;
    @XmlElement(name = "match-one")
    protected Empty matchOne;
    @XmlElement(name = "match-all")
    protected Empty matchAll;
    @XmlElement(name = "select-one-assume-match")
    protected Empty selectOneAssumeMatch;

    /**
     * Gets the value of the maxSize property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxSize() {
        return maxSize;
    }

    /**
     * Sets the value of the maxSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxSize(Integer value) {
        this.maxSize = value;
    }

    /**
     * Gets the value of the minSize property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMinSize() {
        return minSize;
    }

    /**
     * Sets the value of the minSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMinSize(Integer value) {
        this.minSize = value;
    }

    /**
     * Gets the value of the blockingTimeoutMilliseconds property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBlockingTimeoutMilliseconds() {
        return blockingTimeoutMilliseconds;
    }

    /**
     * Sets the value of the blockingTimeoutMilliseconds property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBlockingTimeoutMilliseconds(Integer value) {
        this.blockingTimeoutMilliseconds = value;
    }

    /**
     * Gets the value of the idleTimeoutMinutes property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdleTimeoutMinutes() {
        return idleTimeoutMinutes;
    }

    /**
     * Sets the value of the idleTimeoutMinutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdleTimeoutMinutes(Integer value) {
        this.idleTimeoutMinutes = value;
    }

    /**
     * Gets the value of the matchOne property.
     * 
     * @return
     *     possible object is
     *     {@link Empty }
     *     
     */
    public Empty getMatchOne() {
        return matchOne;
    }

    /**
     * Sets the value of the matchOne property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty }
     *     
     */
    public void setMatchOne(Empty value) {
        this.matchOne = value;
    }

    /**
     * Gets the value of the matchAll property.
     * 
     * @return
     *     possible object is
     *     {@link Empty }
     *     
     */
    public Empty getMatchAll() {
        return matchAll;
    }

    /**
     * Sets the value of the matchAll property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty }
     *     
     */
    public void setMatchAll(Empty value) {
        this.matchAll = value;
    }

    /**
     * Gets the value of the selectOneAssumeMatch property.
     * 
     * @return
     *     possible object is
     *     {@link Empty }
     *     
     */
    public Empty getSelectOneAssumeMatch() {
        return selectOneAssumeMatch;
    }

    /**
     * Sets the value of the selectOneAssumeMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty }
     *     
     */
    public void setSelectOneAssumeMatch(Empty value) {
        this.selectOneAssumeMatch = value;
    }

}
