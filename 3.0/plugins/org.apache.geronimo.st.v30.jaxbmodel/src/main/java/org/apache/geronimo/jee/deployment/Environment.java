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
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="bundle-activator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bundle-classPath" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="import-package" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="export-package" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="require-bundle" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="hidden-classes" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}classFilterType" minOccurs="0"/>
 *         &lt;element name="non-overridable-classes" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}classFilterType" minOccurs="0"/>
 *         &lt;element name="private-classes" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}classFilterType" minOccurs="0"/>
 *         &lt;element name="inverse-classloading" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}emptyType" minOccurs="0"/>
 *         &lt;element name="suppress-default-environment" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}emptyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "environmentType", propOrder = {
    "moduleId",
    "dependencies",
    "bundleActivator",
    "bundleClassPath",
    "importPackage",
    "exportPackage",
    "requireBundle",
    "hiddenClasses",
    "nonOverridableClasses",
    "privateClasses",
    "inverseClassloading",
    "suppressDefaultEnvironment"
})
public class Environment
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    protected Artifact moduleId;
    protected Dependencies dependencies;
    @XmlElement(name = "bundle-activator")
    protected String bundleActivator;
    @XmlElement(name = "bundle-classPath")
    protected List<String> bundleClassPath;
    @XmlElement(name = "import-package")
    protected List<String> importPackage;
    @XmlElement(name = "export-package")
    protected List<String> exportPackage;
    @XmlElement(name = "require-bundle")
    protected List<String> requireBundle;
    @XmlElement(name = "hidden-classes")
    protected ClassFilter hiddenClasses;
    @XmlElement(name = "non-overridable-classes")
    protected ClassFilter nonOverridableClasses;
    @XmlElement(name = "private-classes")
    protected ClassFilter privateClasses;
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
     * Gets the value of the bundleActivator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBundleActivator() {
        return bundleActivator;
    }

    /**
     * Sets the value of the bundleActivator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBundleActivator(String value) {
        this.bundleActivator = value;
    }

    /**
     * Gets the value of the bundleClassPath property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bundleClassPath property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBundleClassPath().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getBundleClassPath() {
        if (bundleClassPath == null) {
            bundleClassPath = new ArrayList<String>();
        }
        return this.bundleClassPath;
    }

    /**
     * Gets the value of the importPackage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the importPackage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImportPackage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getImportPackage() {
        if (importPackage == null) {
            importPackage = new ArrayList<String>();
        }
        return this.importPackage;
    }

    /**
     * Gets the value of the exportPackage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the exportPackage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExportPackage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getExportPackage() {
        if (exportPackage == null) {
            exportPackage = new ArrayList<String>();
        }
        return this.exportPackage;
    }

    /**
     * Gets the value of the requireBundle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requireBundle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequireBundle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRequireBundle() {
        if (requireBundle == null) {
            requireBundle = new ArrayList<String>();
        }
        return this.requireBundle;
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
     * Gets the value of the privateClasses property.
     * 
     * @return
     *     possible object is
     *     {@link ClassFilter }
     *     
     */
    public ClassFilter getPrivateClasses() {
        return privateClasses;
    }

    /**
     * Sets the value of the privateClasses property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassFilter }
     *     
     */
    public void setPrivateClasses(ClassFilter value) {
        this.privateClasses = value;
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
