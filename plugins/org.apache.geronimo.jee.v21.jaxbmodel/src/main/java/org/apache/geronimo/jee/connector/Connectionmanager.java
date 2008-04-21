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
 * 
 *                 The connectionmanager element specifies the connection specific
 *                 settings like transaction, security, and connection pool.
 *             
 * 
 * <p>Java class for connectionmanagerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="connectionmanagerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="container-managed-security" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}emptyType" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="no-transaction" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}emptyType"/>
 *           &lt;element name="local-transaction" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}emptyType"/>
 *           &lt;element name="xa-transaction" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}xatransactionType"/>
 *           &lt;element name="transaction-log" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}emptyType"/>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element name="no-pool" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}emptyType"/>
 *           &lt;element name="single-pool" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}singlepoolType"/>
 *           &lt;element name="partitioned-pool" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}partitionedpoolType"/>
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
@XmlType(name = "connectionmanagerType", propOrder = {
    "containerManagedSecurity",
    "noTransaction",
    "localTransaction",
    "xaTransaction",
    "transactionLog",
    "noPool",
    "singlePool",
    "partitionedPool"
})
public class Connectionmanager
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "container-managed-security")
    protected Empty containerManagedSecurity;
    @XmlElement(name = "no-transaction")
    protected Empty noTransaction;
    @XmlElement(name = "local-transaction")
    protected Empty localTransaction;
    @XmlElement(name = "xa-transaction")
    protected Xatransaction xaTransaction;
    @XmlElement(name = "transaction-log")
    protected Empty transactionLog;
    @XmlElement(name = "no-pool")
    protected Empty noPool;
    @XmlElement(name = "single-pool")
    protected Singlepool singlePool;
    @XmlElement(name = "partitioned-pool")
    protected Partitionedpool partitionedPool;

    /**
     * Gets the value of the containerManagedSecurity property.
     * 
     * @return
     *     possible object is
     *     {@link Empty }
     *     
     */
    public Empty getContainerManagedSecurity() {
        return containerManagedSecurity;
    }

    /**
     * Sets the value of the containerManagedSecurity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty }
     *     
     */
    public void setContainerManagedSecurity(Empty value) {
        this.containerManagedSecurity = value;
    }

    /**
     * Gets the value of the noTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link Empty }
     *     
     */
    public Empty getNoTransaction() {
        return noTransaction;
    }

    /**
     * Sets the value of the noTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty }
     *     
     */
    public void setNoTransaction(Empty value) {
        this.noTransaction = value;
    }

    /**
     * Gets the value of the localTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link Empty }
     *     
     */
    public Empty getLocalTransaction() {
        return localTransaction;
    }

    /**
     * Sets the value of the localTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty }
     *     
     */
    public void setLocalTransaction(Empty value) {
        this.localTransaction = value;
    }

    /**
     * Gets the value of the xaTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link Xatransaction }
     *     
     */
    public Xatransaction getXaTransaction() {
        return xaTransaction;
    }

    /**
     * Sets the value of the xaTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Xatransaction }
     *     
     */
    public void setXaTransaction(Xatransaction value) {
        this.xaTransaction = value;
    }

    /**
     * Gets the value of the transactionLog property.
     * 
     * @return
     *     possible object is
     *     {@link Empty }
     *     
     */
    public Empty getTransactionLog() {
        return transactionLog;
    }

    /**
     * Sets the value of the transactionLog property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty }
     *     
     */
    public void setTransactionLog(Empty value) {
        this.transactionLog = value;
    }

    /**
     * Gets the value of the noPool property.
     * 
     * @return
     *     possible object is
     *     {@link Empty }
     *     
     */
    public Empty getNoPool() {
        return noPool;
    }

    /**
     * Sets the value of the noPool property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty }
     *     
     */
    public void setNoPool(Empty value) {
        this.noPool = value;
    }

    /**
     * Gets the value of the singlePool property.
     * 
     * @return
     *     possible object is
     *     {@link Singlepool }
     *     
     */
    public Singlepool getSinglePool() {
        return singlePool;
    }

    /**
     * Sets the value of the singlePool property.
     * 
     * @param value
     *     allowed object is
     *     {@link Singlepool }
     *     
     */
    public void setSinglePool(Singlepool value) {
        this.singlePool = value;
    }

    /**
     * Gets the value of the partitionedPool property.
     * 
     * @return
     *     possible object is
     *     {@link Partitionedpool }
     *     
     */
    public Partitionedpool getPartitionedPool() {
        return partitionedPool;
    }

    /**
     * Sets the value of the partitionedPool property.
     * 
     * @param value
     *     allowed object is
     *     {@link Partitionedpool }
     *     
     */
    public void setPartitionedPool(Partitionedpool value) {
        this.partitionedPool = value;
    }

}
