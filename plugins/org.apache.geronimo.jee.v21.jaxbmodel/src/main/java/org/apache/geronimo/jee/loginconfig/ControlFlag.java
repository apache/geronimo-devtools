//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.29 at 06:48:36 PM IST 
//


package org.apache.geronimo.jee.loginconfig;

import javax.xml.bind.annotation.XmlEnum;


/**
 * <p>Java class for control-flagType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="control-flagType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="REQUIRED"/>
 *     &lt;enumeration value="REQUISITE"/>
 *     &lt;enumeration value="SUFFICIENT"/>
 *     &lt;enumeration value="OPTIONAL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlEnum
public enum ControlFlag {

    REQUIRED,
    REQUISITE,
    SUFFICIENT,
    OPTIONAL;

    public String value() {
        return name();
    }

    public static ControlFlag fromValue(String v) {
        return valueOf(v);
    }

}
