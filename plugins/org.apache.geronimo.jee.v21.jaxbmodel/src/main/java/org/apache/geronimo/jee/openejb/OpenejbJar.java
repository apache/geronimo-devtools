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

package org.apache.geronimo.jee.openejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.jee.application.AbstractSecurity;
import org.apache.geronimo.jee.deployment.AbstractService;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.naming.MessageDestination;
import org.apache.geronimo.jee.naming.ResourceLocator;


/**
 * <p>Java class for openejb-jarType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="openejb-jarType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}environment" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.2}cmp-connection-factory" minOccurs="0"/>
 *         &lt;element name="ejb-ql-compiler-factory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="db-syntax-factory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enforce-foreign-key-constraints" type="{http://openejb.apache.org/xml/ns/openejb-jar-2.2}emptyType" minOccurs="0"/>
 *         &lt;element name="enterprise-beans">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element name="session" type="{http://openejb.apache.org/xml/ns/openejb-jar-2.2}session-beanType"/>
 *                   &lt;element name="entity" type="{http://openejb.apache.org/xml/ns/openejb-jar-2.2}entity-beanType"/>
 *                   &lt;element name="message-driven" type="{http://openejb.apache.org/xml/ns/openejb-jar-2.2}message-driven-beanType"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="relationships" type="{http://openejb.apache.org/xml/ns/openejb-jar-2.2}relationshipsType" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.2}message-destination" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}security" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}service" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "openejb-jarType", propOrder = {
    "environment",
    "cmpConnectionFactory",
    "ejbQlCompilerFactory",
    "dbSyntaxFactory",
    "enforceForeignKeyConstraints",
    "enterpriseBeans",
    "relationships",
    "messageDestination",
    "security",
    "service"
})
public class OpenejbJar
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2")
    protected Environment environment;
    @XmlElement(name = "cmp-connection-factory", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected ResourceLocator cmpConnectionFactory;
    @XmlElement(name = "ejb-ql-compiler-factory")
    protected String ejbQlCompilerFactory;
    @XmlElement(name = "db-syntax-factory")
    protected String dbSyntaxFactory;
    @XmlElement(name = "enforce-foreign-key-constraints")
    protected Empty enforceForeignKeyConstraints;
    @XmlElement(name = "enterprise-beans", required = true)
    protected OpenejbJar.EnterpriseBeans enterpriseBeans;
    protected Relationships relationships;
    @XmlElement(name = "message-destination", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<MessageDestination> messageDestination;
    @XmlElementRef(name = "security", namespace = "http://geronimo.apache.org/xml/ns/j2ee/application-2.0", type = JAXBElement.class)
    protected JAXBElement<? extends AbstractSecurity> security;
    @XmlElementRef(name = "service", namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractService>> service;

    /**
     * Gets the value of the environment property.
     * 
     * @return
     *     possible object is
     *     {@link Environment}
     *     
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Sets the value of the environment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Environment}
     *     
     */
    public void setEnvironment(Environment value) {
        this.environment = value;
    }

    /**
     * Gets the value of the cmpConnectionFactory property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLocator}
     *     
     */
    public ResourceLocator getCmpConnectionFactory() {
        return cmpConnectionFactory;
    }

    /**
     * Sets the value of the cmpConnectionFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLocator}
     *     
     */
    public void setCmpConnectionFactory(ResourceLocator value) {
        this.cmpConnectionFactory = value;
    }

    /**
     * Gets the value of the ejbQlCompilerFactory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEjbQlCompilerFactory() {
        return ejbQlCompilerFactory;
    }

    /**
     * Sets the value of the ejbQlCompilerFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEjbQlCompilerFactory(String value) {
        if (value == null || value.length() == 0)
            this.ejbQlCompilerFactory = null;
        else
            this.ejbQlCompilerFactory = value;
    }

    /**
     * Gets the value of the dbSyntaxFactory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDbSyntaxFactory() {
        return dbSyntaxFactory;
    }

    /**
     * Sets the value of the dbSyntaxFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDbSyntaxFactory(String value) {
        if (value == null || value.length() == 0)
            this.dbSyntaxFactory = null;
        else
            this.dbSyntaxFactory = value;
    }

    /**
     * Gets the value of the enforceForeignKeyConstraints property.
     * 
     * @return
     *     possible object is
     *     {@link Empty}
     *     
     */
    public Empty getEnforceForeignKeyConstraints() {
        return enforceForeignKeyConstraints;
    }

    /**
     * Sets the value of the enforceForeignKeyConstraints property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty}
     *     
     */
    public void setEnforceForeignKeyConstraints(Empty value) {
        this.enforceForeignKeyConstraints = value;
    }

    /**
     * Gets the value of the enterpriseBeans property.
     * 
     * @return
     *     possible object is
     *     {@link OpenejbJar.EnterpriseBeans }
     *     
     */
    public OpenejbJar.EnterpriseBeans getEnterpriseBeans() {
        return enterpriseBeans;
    }

    /**
     * Sets the value of the enterpriseBeans property.
     * 
     * @param value
     *     allowed object is
     *     {@link OpenejbJar.EnterpriseBeans }
     *     
     */
    public void setEnterpriseBeans(OpenejbJar.EnterpriseBeans value) {
        this.enterpriseBeans = value;
    }

    /**
     * Gets the value of the relationships property.
     * 
     * @return
     *     possible object is
     *     {@link Relationships}
     *     
     */
    public Relationships getRelationships() {
        return relationships;
    }

    /**
     * Sets the value of the relationships property.
     * 
     * @param value
     *     allowed object is
     *     {@link Relationships}
     *     
     */
    public void setRelationships(Relationships value) {
        this.relationships = value;
    }

    /**
     * Gets the value of the messageDestination property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageDestination property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageDestination().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessageDestination}
     * 
     * 
     */
    public List<MessageDestination> getMessageDestination() {
        if (messageDestination == null) {
            messageDestination = new ArrayList<MessageDestination>();
        }
        return this.messageDestination;
    }

    /**
     * Gets the value of the security property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AbstractSecurity}{@code >}
     *     {@link JAXBElement }{@code <}{@link
     *     org.apache.geronimo.xml.ns.security_1.Security}{@code >}
     *     {@link JAXBElement }{@code <}{@link
     *     org.apache.geronimo.xml.ns.security_2.Security}{@code >}
     *     {@link JAXBElement }{@code <}{@link
     *     org.apache.geronimo.xml.ns.subject_info_1.Security}{@code >}
     *     
     */
    public JAXBElement<? extends AbstractSecurity> getSecurity() {
        return security;
    }

    /**
     * Sets the value of the security property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AbstractSecurity}{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.xml.ns.security_1.Security }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.xml.ns.security_2.Security }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.xml.ns.subject_info_1.Security
     *     }{@code >}
     *     
     */
    public void setSecurity(JAXBElement<? extends AbstractSecurity> value) {
        this.security = ((JAXBElement<? extends AbstractSecurity> ) value);
    }

    /**
     * Gets the value of the service property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the service property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AbstractService}{@code >} 
     * {@link JAXBElement }{@code <}{@link Gbean}{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractService>> getService() {
        if (service == null) {
            service = new ArrayList<JAXBElement<? extends AbstractService>>();
        }
        return this.service;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice maxOccurs="unbounded" minOccurs="0">
     *         &lt;element name="session" type="{http://openejb.apache.org/xml/ns/openejb-jar-2.2}session-beanType"/>
     *         &lt;element name="entity" type="{http://openejb.apache.org/xml/ns/openejb-jar-2.2}entity-beanType"/>
     *         &lt;element name="message-driven" type="{http://openejb.apache.org/xml/ns/openejb-jar-2.2}message-driven-beanType"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "sessionOrEntityOrMessageDriven"
    })
    public static class EnterpriseBeans
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElements({
            @XmlElement(name = "message-driven", type = MessageDrivenBean.class),
            @XmlElement(name = "entity", type = EntityBean.class),
            @XmlElement(name = "session", type = SessionBean.class)
        })
        protected List<Object> sessionOrEntityOrMessageDriven;

        /**
         * Gets the value of the sessionOrEntityOrMessageDriven property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the sessionOrEntityOrMessageDriven property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSessionOrEntityOrMessageDriven().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link MessageDrivenBean} 
         * {@link EntityBean} 
         * {@link SessionBean}
         * 
         * 
         */
        public List<Object> getSessionOrEntityOrMessageDriven() {
            if (sessionOrEntityOrMessageDriven == null) {
                sessionOrEntityOrMessageDriven = new ArrayList<Object>();
            }
            return this.sessionOrEntityOrMessageDriven;
        }

    }

}
