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
 * 
 *                 The ConnectionManager configuration.
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
 *         &lt;element name="container-managed-security" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}emptyType" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="no-transaction" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}emptyType"/>
 *           &lt;element name="local-transaction" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}emptyType"/>
 *           &lt;element name="xa-transaction" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}xatransactionType"/>
 *           &lt;element name="transaction-log" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}emptyType"/>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element name="no-pool" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}emptyType"/>
 *           &lt;element name="single-pool" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}singlepoolType"/>
 *           &lt;element name="partitioned-pool" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}partitionedpoolType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
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
public class ConnectionmanagerType {

    @XmlElement(name = "container-managed-security")
    protected EmptyType containerManagedSecurity;
    @XmlElement(name = "no-transaction")
    protected EmptyType noTransaction;
    @XmlElement(name = "local-transaction")
    protected EmptyType localTransaction;
    @XmlElement(name = "xa-transaction")
    protected XatransactionType xaTransaction;
    @XmlElement(name = "transaction-log")
    protected EmptyType transactionLog;
    @XmlElement(name = "no-pool")
    protected EmptyType noPool;
    @XmlElement(name = "single-pool")
    protected SinglepoolType singlePool;
    @XmlElement(name = "partitioned-pool")
    protected PartitionedpoolType partitionedPool;

    /**
     * Gets the value of the containerManagedSecurity property.
     * 
     * @return
     *     possible object is
     *     {@link EmptyType }
     *     
     */
    public EmptyType getContainerManagedSecurity() {
        return containerManagedSecurity;
    }

    /**
     * Sets the value of the containerManagedSecurity property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmptyType }
     *     
     */
    public void setContainerManagedSecurity(EmptyType value) {
        this.containerManagedSecurity = value;
    }

    /**
     * Gets the value of the noTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link EmptyType }
     *     
     */
    public EmptyType getNoTransaction() {
        return noTransaction;
    }

    /**
     * Sets the value of the noTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmptyType }
     *     
     */
    public void setNoTransaction(EmptyType value) {
        this.noTransaction = value;
    }

    /**
     * Gets the value of the localTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link EmptyType }
     *     
     */
    public EmptyType getLocalTransaction() {
        return localTransaction;
    }

    /**
     * Sets the value of the localTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmptyType }
     *     
     */
    public void setLocalTransaction(EmptyType value) {
        this.localTransaction = value;
    }

    /**
     * Gets the value of the xaTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link XatransactionType }
     *     
     */
    public XatransactionType getXaTransaction() {
        return xaTransaction;
    }

    /**
     * Sets the value of the xaTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link XatransactionType }
     *     
     */
    public void setXaTransaction(XatransactionType value) {
        this.xaTransaction = value;
    }

    /**
     * Gets the value of the transactionLog property.
     * 
     * @return
     *     possible object is
     *     {@link EmptyType }
     *     
     */
    public EmptyType getTransactionLog() {
        return transactionLog;
    }

    /**
     * Sets the value of the transactionLog property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmptyType }
     *     
     */
    public void setTransactionLog(EmptyType value) {
        this.transactionLog = value;
    }

    /**
     * Gets the value of the noPool property.
     * 
     * @return
     *     possible object is
     *     {@link EmptyType }
     *     
     */
    public EmptyType getNoPool() {
        return noPool;
    }

    /**
     * Sets the value of the noPool property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmptyType }
     *     
     */
    public void setNoPool(EmptyType value) {
        this.noPool = value;
    }

    /**
     * Gets the value of the singlePool property.
     * 
     * @return
     *     possible object is
     *     {@link SinglepoolType }
     *     
     */
    public SinglepoolType getSinglePool() {
        return singlePool;
    }

    /**
     * Sets the value of the singlePool property.
     * 
     * @param value
     *     allowed object is
     *     {@link SinglepoolType }
     *     
     */
    public void setSinglePool(SinglepoolType value) {
        this.singlePool = value;
    }

    /**
     * Gets the value of the partitionedPool property.
     * 
     * @return
     *     possible object is
     *     {@link PartitionedpoolType }
     *     
     */
    public PartitionedpoolType getPartitionedPool() {
        return partitionedPool;
    }

    /**
     * Sets the value of the partitionedPool property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartitionedpoolType }
     *     
     */
    public void setPartitionedPool(PartitionedpoolType value) {
        this.partitionedPool = value;
    }

}
