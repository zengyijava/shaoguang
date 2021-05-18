
package com.montnets.emp.common.advice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SubUnionInterfaceResult" type="{http://www.mboss.com}Response" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "subUnionInterfaceResult"
})
@XmlRootElement(name = "SubUnionInterfaceResponse")
public class SubUnionInterfaceResponse {

    @XmlElement(name = "SubUnionInterfaceResult")
    protected Response subUnionInterfaceResult;

    /**
     * Gets the value of the subUnionInterfaceResult property.
     * 
     * @return
     *     possible object is
     *     {@link Response }
     *     
     */
    public Response getSubUnionInterfaceResult() {
        return subUnionInterfaceResult;
    }

    /**
     * Sets the value of the subUnionInterfaceResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Response }
     *     
     */
    public void setSubUnionInterfaceResult(Response value) {
        this.subUnionInterfaceResult = value;
    }

}
