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

package org.apache.geronimo.jee.naming;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for persistence-unit-refType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="persistence-unit-refType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://geronimo.apache.org/xml/ns/naming-1.2}abstract-naming-entryType">
 *       &lt;sequence>
 *         &lt;element name="persistence-unit-ref-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;choice>
 *           &lt;element name="persistence-unit-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="pattern" type="{http://geronimo.apache.org/xml/ns/naming-1.2}patternType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "persistence-unit-refType", propOrder = {
    "persistenceUnitRefName",
    "persistenceUnitName",
    "pattern"
})
public class PersistenceUnitRef
    extends AbstractNamingEntry
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "persistence-unit-ref-name", required = true)
    protected String persistenceUnitRefName;
    @XmlElement(name = "persistence-unit-name")
    protected String persistenceUnitName;
    protected Pattern pattern;

    /**
     * Gets the value of the persistenceUnitRefName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersistenceUnitRefName() {
        return persistenceUnitRefName;
    }

    /**
     * Sets the value of the persistenceUnitRefName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersistenceUnitRefName(String value) {
        this.persistenceUnitRefName = value;
    }

    /**
     * Gets the value of the persistenceUnitName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    /**
     * Sets the value of the persistenceUnitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersistenceUnitName(String value) {
        this.persistenceUnitName = value;
    }

    /**
     * Gets the value of the pattern property.
     * 
     * @return
     *     possible object is
     *     {@link Pattern }
     *     
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Sets the value of the pattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pattern }
     *     
     */
    public void setPattern(Pattern value) {
        this.pattern = value;
    }

}
