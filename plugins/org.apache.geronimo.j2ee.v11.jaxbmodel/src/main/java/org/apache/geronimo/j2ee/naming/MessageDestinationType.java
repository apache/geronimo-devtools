//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.07.17 at 01:03:24 AM CST 
//


package org.apache.geronimo.j2ee.naming;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for message-destinationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="message-destinationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="message-destination-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;choice>
 *           &lt;element name="pattern" type="{http://geronimo.apache.org/xml/ns/naming-1.1}patternType"/>
 *           &lt;sequence>
 *             &lt;element name="admin-object-module" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *             &lt;element name="admin-object-link" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;/sequence>
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
@XmlType(name = "message-destinationType", propOrder = {
    "messageDestinationName",
    "pattern",
    "adminObjectModule",
    "adminObjectLink"
})
public class MessageDestinationType {

    @XmlElement(name = "message-destination-name", required = true)
    protected String messageDestinationName;
    protected PatternType pattern;
    @XmlElement(name = "admin-object-module")
    protected String adminObjectModule;
    @XmlElement(name = "admin-object-link")
    protected String adminObjectLink;

    /**
     * Gets the value of the messageDestinationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageDestinationName() {
        return messageDestinationName;
    }

    /**
     * Sets the value of the messageDestinationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageDestinationName(String value) {
        this.messageDestinationName = value;
    }

    /**
     * Gets the value of the pattern property.
     * 
     * @return
     *     possible object is
     *     {@link PatternType }
     *     
     */
    public PatternType getPattern() {
        return pattern;
    }

    /**
     * Sets the value of the pattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link PatternType }
     *     
     */
    public void setPattern(PatternType value) {
        this.pattern = value;
    }

    /**
     * Gets the value of the adminObjectModule property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdminObjectModule() {
        return adminObjectModule;
    }

    /**
     * Sets the value of the adminObjectModule property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdminObjectModule(String value) {
        this.adminObjectModule = value;
    }

    /**
     * Gets the value of the adminObjectLink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdminObjectLink() {
        return adminObjectLink;
    }

    /**
     * Sets the value of the adminObjectLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdminObjectLink(String value) {
        this.adminObjectLink = value;
    }

}