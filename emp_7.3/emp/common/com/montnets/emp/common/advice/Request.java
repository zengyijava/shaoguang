
package com.montnets.emp.common.advice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Request complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Request">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BizCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TransID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TimeStamp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ActionCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PtCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TestFlag" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Dealkind" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Priority" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EncryFlag" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SvcCont" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Request", propOrder = {
    "bizCode",
    "transID",
    "timeStamp",
    "actionCode",
    "ptCode",
    "testFlag",
    "dealkind",
    "priority",
    "version",
    "encryFlag",
    "svcCont"
})
public class Request {

    @XmlElement(name = "BizCode")
    protected String bizCode;
    @XmlElement(name = "TransID")
    protected String transID;
    @XmlElement(name = "TimeStamp")
    protected String timeStamp;
    @XmlElement(name = "ActionCode")
    protected int actionCode;
    @XmlElement(name = "PtCode")
    protected String ptCode;
    @XmlElement(name = "TestFlag")
    protected int testFlag;
    @XmlElement(name = "Dealkind")
    protected int dealkind;
    @XmlElement(name = "Priority")
    protected int priority;
    @XmlElement(name = "Version")
    protected String version;
    @XmlElement(name = "EncryFlag")
    protected int encryFlag;
    @XmlElement(name = "SvcCont")
    protected String svcCont;

    /**
     * Gets the value of the bizCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBizCode() {
        return bizCode;
    }

    /**
     * Sets the value of the bizCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBizCode(String value) {
        this.bizCode = value;
    }

    /**
     * Gets the value of the transID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransID() {
        return transID;
    }

    /**
     * Sets the value of the transID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransID(String value) {
        this.transID = value;
    }

    /**
     * Gets the value of the timeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the value of the timeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeStamp(String value) {
        this.timeStamp = value;
    }

    /**
     * Gets the value of the actionCode property.
     * 
     */
    public int getActionCode() {
        return actionCode;
    }

    /**
     * Sets the value of the actionCode property.
     * 
     */
    public void setActionCode(int value) {
        this.actionCode = value;
    }

    /**
     * Gets the value of the ptCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPtCode() {
        return ptCode;
    }

    /**
     * Sets the value of the ptCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPtCode(String value) {
        this.ptCode = value;
    }

    /**
     * Gets the value of the testFlag property.
     * 
     */
    public int getTestFlag() {
        return testFlag;
    }

    /**
     * Sets the value of the testFlag property.
     * 
     */
    public void setTestFlag(int value) {
        this.testFlag = value;
    }

    /**
     * Gets the value of the dealkind property.
     * 
     */
    public int getDealkind() {
        return dealkind;
    }

    /**
     * Sets the value of the dealkind property.
     * 
     */
    public void setDealkind(int value) {
        this.dealkind = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     */
    public void setPriority(int value) {
        this.priority = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the encryFlag property.
     * 
     */
    public int getEncryFlag() {
        return encryFlag;
    }

    /**
     * Sets the value of the encryFlag property.
     * 
     */
    public void setEncryFlag(int value) {
        this.encryFlag = value;
    }

    /**
     * Gets the value of the svcCont property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSvcCont() {
        return svcCont;
    }

    /**
     * Sets the value of the svcCont property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSvcCont(String value) {
        this.svcCont = value;
    }

}
