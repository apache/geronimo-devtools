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
 * <p>Java class for partitionedpoolType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="partitionedpoolType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}singlepoolType">
 *       &lt;sequence>
 *         &lt;element name="partition-by-subject" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}emptyType" minOccurs="0"/>
 *         &lt;element name="partition-by-connectionrequestinfo" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}emptyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "partitionedpoolType", propOrder = {
    "partitionBySubject",
    "partitionByConnectionrequestinfo"
})
public class PartitionedpoolType
    extends SinglepoolType
{

    @XmlElement(name = "partition-by-subject")
    protected EmptyType partitionBySubject;
    @XmlElement(name = "partition-by-connectionrequestinfo")
    protected EmptyType partitionByConnectionrequestinfo;

    /**
     * Gets the value of the partitionBySubject property.
     * 
     * @return
     *     possible object is
     *     {@link EmptyType }
     *     
     */
    public EmptyType getPartitionBySubject() {
        return partitionBySubject;
    }

    /**
     * Sets the value of the partitionBySubject property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmptyType }
     *     
     */
    public void setPartitionBySubject(EmptyType value) {
        this.partitionBySubject = value;
    }

    /**
     * Gets the value of the partitionByConnectionrequestinfo property.
     * 
     * @return
     *     possible object is
     *     {@link EmptyType }
     *     
     */
    public EmptyType getPartitionByConnectionrequestinfo() {
        return partitionByConnectionrequestinfo;
    }

    /**
     * Sets the value of the partitionByConnectionrequestinfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmptyType }
     *     
     */
    public void setPartitionByConnectionrequestinfo(EmptyType value) {
        this.partitionByConnectionrequestinfo = value;
    }

}
