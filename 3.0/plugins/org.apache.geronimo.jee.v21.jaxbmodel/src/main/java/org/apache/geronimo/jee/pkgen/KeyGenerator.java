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

package org.apache.geronimo.jee.pkgen;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *               Primary Key generation element.
 * 
 *               If this is present, a key generator GBean will be created
 *               and configured to generate IDs for the surrounding object.
 *             
 * 
 * <p>Java class for key-generatorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="key-generatorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="uuid" type="{http://openejb.apache.org/xml/ns/pkgen-2.1}emptyType"/>
 *         &lt;element name="sequence-table" type="{http://openejb.apache.org/xml/ns/pkgen-2.1}sequence-tableType"/>
 *         &lt;element name="auto-increment-table" type="{http://openejb.apache.org/xml/ns/pkgen-2.1}auto-increment-tableType"/>
 *         &lt;element name="sql-generator" type="{http://openejb.apache.org/xml/ns/pkgen-2.1}sql-generatorType"/>
 *         &lt;element name="custom-generator" type="{http://openejb.apache.org/xml/ns/pkgen-2.1}custom-generatorType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "key-generatorType", propOrder = {
    "uuid",
    "sequenceTable",
    "autoIncrementTable",
    "sqlGenerator",
    "customGenerator"
})
public class KeyGenerator
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    protected Empty uuid;
    @XmlElement(name = "sequence-table")
    protected SequenceTable sequenceTable;
    @XmlElement(name = "auto-increment-table")
    protected AutoIncrementTable autoIncrementTable;
    @XmlElement(name = "sql-generator")
    protected SqlGenerator sqlGenerator;
    @XmlElement(name = "custom-generator")
    protected CustomGenerator customGenerator;

    /**
     * Gets the value of the uuid property.
     * 
     * @return
     *     possible object is
     *     {@link Empty}
     *     
     */
    public Empty getUuid() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty}
     *     
     */
    public void setUuid(Empty value) {
        this.uuid = value;
    }

    /**
     * Gets the value of the sequenceTable property.
     * 
     * @return
     *     possible object is
     *     {@link SequenceTable}
     *     
     */
    public SequenceTable getSequenceTable() {
        return sequenceTable;
    }

    /**
     * Sets the value of the sequenceTable property.
     * 
     * @param value
     *     allowed object is
     *     {@link SequenceTable}
     *     
     */
    public void setSequenceTable(SequenceTable value) {
        this.sequenceTable = value;
    }

    /**
     * Gets the value of the autoIncrementTable property.
     * 
     * @return
     *     possible object is
     *     {@link AutoIncrementTable}
     *     
     */
    public AutoIncrementTable getAutoIncrementTable() {
        return autoIncrementTable;
    }

    /**
     * Sets the value of the autoIncrementTable property.
     * 
     * @param value
     *     allowed object is
     *     {@link AutoIncrementTable}
     *     
     */
    public void setAutoIncrementTable(AutoIncrementTable value) {
        this.autoIncrementTable = value;
    }

    /**
     * Gets the value of the sqlGenerator property.
     * 
     * @return
     *     possible object is
     *     {@link SqlGenerator}
     *     
     */
    public SqlGenerator getSqlGenerator() {
        return sqlGenerator;
    }

    /**
     * Sets the value of the sqlGenerator property.
     * 
     * @param value
     *     allowed object is
     *     {@link SqlGenerator}
     *     
     */
    public void setSqlGenerator(SqlGenerator value) {
        this.sqlGenerator = value;
    }

    /**
     * Gets the value of the customGenerator property.
     * 
     * @return
     *     possible object is
     *     {@link CustomGenerator}
     *     
     */
    public CustomGenerator getCustomGenerator() {
        return customGenerator;
    }

    /**
     * Sets the value of the customGenerator property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomGenerator}
     *     
     */
    public void setCustomGenerator(CustomGenerator value) {
        this.customGenerator = value;
    }

}
