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

package org.apache.geronimo.jee.deployment;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for environmentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="environmentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="moduleId" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}artifactType" minOccurs="0"/>
 *         &lt;element name="dependencies" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}dependenciesType" minOccurs="0"/>
 *         &lt;element name="hidden-classes" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}classFilterType" minOccurs="0"/>
 *         &lt;element name="non-overridable-classes" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}classFilterType" minOccurs="0"/>
 *         &lt;element name="inverse-classloading" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}emptyType" minOccurs="0"/>
 *         &lt;element name="suppress-default-environment" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}emptyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "environmentType", propOrder = {
    "moduleId",
    "dependencies",
    "hiddenClasses",
    "nonOverridableClasses",
    "inverseClassloading",
    "suppressDefaultEnvironment"
})
public class Environment
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    protected Artifact moduleId;
    protected Dependencies dependencies;
    @XmlElement(name = "hidden-classes")
    protected ClassFilter hiddenClasses;
    @XmlElement(name = "non-overridable-classes")
    protected ClassFilter nonOverridableClasses;
    @XmlElement(name = "inverse-classloading")
    protected Empty inverseClassloading;
    @XmlElement(name = "suppress-default-environment")
    protected Empty suppressDefaultEnvironment;

    /**
     * Gets the value of the moduleId property.
     * 
     * @return
     *     possible object is
     *     {@link Artifact }
     *     
     */
    public Artifact getModuleId() {
        return moduleId;
    }

    /**
     * Sets the value of the moduleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Artifact }
     *     
     */
    public void setModuleId(Artifact value) {
        this.moduleId = value;
    }

    /**
     * Gets the value of the dependencies property.
     * 
     * @return
     *     possible object is
     *     {@link Dependencies }
     *     
     */
    public Dependencies getDependencies() {
        return dependencies;
    }

    /**
     * Sets the value of the dependencies property.
     * 
     * @param value
     *     allowed object is
     *     {@link Dependencies }
     *     
     */
    public void setDependencies(Dependencies value) {
        this.dependencies = value;
    }

    /**
     * Gets the value of the hiddenClasses property.
     * 
     * @return
     *     possible object is
     *     {@link ClassFilter }
     *     
     */
    public ClassFilter getHiddenClasses() {
        return hiddenClasses;
    }

    /**
     * Sets the value of the hiddenClasses property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassFilter }
     *     
     */
    public void setHiddenClasses(ClassFilter value) {
        this.hiddenClasses = value;
    }

    /**
     * Gets the value of the nonOverridableClasses property.
     * 
     * @return
     *     possible object is
     *     {@link ClassFilter }
     *     
     */
    public ClassFilter getNonOverridableClasses() {
        return nonOverridableClasses;
    }

    /**
     * Sets the value of the nonOverridableClasses property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassFilter }
     *     
     */
    public void setNonOverridableClasses(ClassFilter value) {
        this.nonOverridableClasses = value;
    }

    /**
     * Gets the value of the inverseClassloading property.
     * 
     * @return
     *     possible object is
     *     {@link Empty }
     *     
     */
    public Empty getInverseClassloading() {
        return inverseClassloading;
    }

    /**
     * Sets the value of the inverseClassloading property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty }
     *     
     */
    public void setInverseClassloading(Empty value) {
        this.inverseClassloading = value;
    }

    /**
     * Gets the value of the suppressDefaultEnvironment property.
     * 
     * @return
     *     possible object is
     *     {@link Empty }
     *     
     */
    public Empty getSuppressDefaultEnvironment() {
        return suppressDefaultEnvironment;
    }

    /**
     * Sets the value of the suppressDefaultEnvironment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty }
     *     
     */
    public void setSuppressDefaultEnvironment(Empty value) {
        this.suppressDefaultEnvironment = value;
    }

}
