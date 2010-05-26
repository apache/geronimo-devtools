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
package org.apache.geronimo.osgi.blueprint;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                 
 *                 The type definition for a <bean> component.  The <bean> 
 *                 attributes provide the characteristics for how to create a
 *                 bean instance.  Constructor arguments and injected properties
 *                 are specified via child <argument> and <property> elements.
 *                 
 *             
 * 
 * <p>Java class for Tbean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tbean">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tcomponent">
 *       &lt;group ref="{http://www.osgi.org/xmlns/blueprint/v1.0.0}GbeanElements"/>
 *       &lt;attribute name="class" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tclass" />
 *       &lt;attribute name="destroy-method" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tmethod" />
 *       &lt;attribute name="factory-method" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tmethod" />
 *       &lt;attribute name="factory-ref" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tidref" />
 *       &lt;attribute name="init-method" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tmethod" />
 *       &lt;attribute name="scope" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tscope" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tbean", propOrder = {
    "description",
    "argumentOrPropertyOrAny"
})
@XmlRootElement(name = "bean")
public class Tbean extends Tcomponent {

    protected Tdescription description;
    @XmlElementRefs({
        @XmlElementRef(name = "property", namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = JAXBElement.class),
        @XmlElementRef(name = "argument", namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", type = JAXBElement.class)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> argumentOrPropertyOrAny;
    @XmlAttribute(name = "class")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String clazz;
    @XmlAttribute(name = "destroy-method")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String destroyMethod;
    @XmlAttribute(name = "factory-method")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String factoryMethod;
    @XmlAttribute(name = "factory-ref")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String factoryRef;
    @XmlAttribute(name = "init-method")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String initMethod;
    @XmlAttribute
    protected String scope;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Tdescription }
     *     
     */
    public Tdescription getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tdescription }
     *     
     */
    public void setDescription(Tdescription value) {
        this.description = value;
    }

    /**
     * Gets the value of the argumentOrPropertyOrAny property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the argumentOrPropertyOrAny property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArgumentOrPropertyOrAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Tproperty }{@code >}
     * {@link Object }
     * {@link JAXBElement }{@code <}{@link Targument }{@code >}
     * 
     * 
     */
    public List<Object> getArgumentOrPropertyOrAny() {
        if (argumentOrPropertyOrAny == null) {
            argumentOrPropertyOrAny = new ArrayList<Object>();
        }
        return this.argumentOrPropertyOrAny;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClazz(String value) {
        this.clazz = value;
    }

    /**
     * Gets the value of the destroyMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestroyMethod() {
        return destroyMethod;
    }

    /**
     * Sets the value of the destroyMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestroyMethod(String value) {
        this.destroyMethod = value;
    }

    /**
     * Gets the value of the factoryMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFactoryMethod() {
        return factoryMethod;
    }

    /**
     * Sets the value of the factoryMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFactoryMethod(String value) {
        this.factoryMethod = value;
    }

    /**
     * Gets the value of the factoryRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFactoryRef() {
        return factoryRef;
    }

    /**
     * Sets the value of the factoryRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFactoryRef(String value) {
        this.factoryRef = value;
    }

    /**
     * Gets the value of the initMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitMethod() {
        return initMethod;
    }

    /**
     * Sets the value of the initMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitMethod(String value) {
        this.initMethod = value;
    }

    /**
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScope(String value) {
        this.scope = value;
    }

}
