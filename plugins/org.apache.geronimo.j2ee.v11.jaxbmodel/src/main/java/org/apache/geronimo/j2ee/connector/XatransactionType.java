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


package org.apache.geronimo.j2ee.connector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for xatransactionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="xatransactionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="transaction-caching" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}emptyType" minOccurs="0"/>
 *         &lt;element name="thread-caching" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}emptyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xatransactionType", propOrder = {
    "transactionCaching",
    "threadCaching"
})
public class XatransactionType {

    @XmlElement(name = "transaction-caching")
    protected EmptyType transactionCaching;
    @XmlElement(name = "thread-caching")
    protected EmptyType threadCaching;

    /**
     * Gets the value of the transactionCaching property.
     * 
     * @return
     *     possible object is
     *     {@link EmptyType }
     *     
     */
    public EmptyType getTransactionCaching() {
        return transactionCaching;
    }

    /**
     * Sets the value of the transactionCaching property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmptyType }
     *     
     */
    public void setTransactionCaching(EmptyType value) {
        this.transactionCaching = value;
    }

    /**
     * Gets the value of the threadCaching property.
     * 
     * @return
     *     possible object is
     *     {@link EmptyType }
     *     
     */
    public EmptyType getThreadCaching() {
        return threadCaching;
    }

    /**
     * Sets the value of the threadCaching property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmptyType }
     *     
     */
    public void setThreadCaching(EmptyType value) {
        this.threadCaching = value;
    }

}
