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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 
 *                 The outbound-resourceadapterType specifies information about
 *                 an outbound resource adapter. The information includes fully
 *                 qualified names of classes/interfaces required as part of
 *                 the connector architecture specified contracts for
 *                 connection management, level of transaction support
 *                 provided, one or more authentication mechanisms supported
 *                 and additional required security permissions.
 * 
 *                 If there is no authentication-mechanism specified as part of
 *                 resource adapter element then the resource adapter does not
 *                 support any standard security authentication mechanisms as
 *                 part of security contract. The application server ignores
 *                 the security part of the system contracts in this case.
 * 
 *             
 * 
 * <p>Java class for outbound-resourceadapterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="outbound-resourceadapterType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="connection-definition" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}connection-definitionType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "outbound-resourceadapterType", propOrder = {
    "connectionDefinition"
})
public class OutboundResourceadapterType {

    @XmlElement(name = "connection-definition", required = true)
    protected List<ConnectionDefinitionType> connectionDefinition;

    /**
     * Gets the value of the connectionDefinition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the connectionDefinition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConnectionDefinition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConnectionDefinitionType }
     * 
     * 
     */
    public List<ConnectionDefinitionType> getConnectionDefinition() {
        if (connectionDefinition == null) {
            connectionDefinition = new ArrayList<ConnectionDefinitionType>();
        }
        return this.connectionDefinition;
    }

}
