/**
 * 
 */
package com.montnets.emp.servmodule.txgl.entity;

import java.sql.Timestamp;

/**
 * @project montnets_gateway
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-3 下午06:51:53
 * @description 
 */

public class AgwAccount implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4118594897723468991L;

	private Integer gwNo;
	
	private Integer ptAccUid;
	
	private String ptAccId;
	
	private String ptAccpwd;
	
	private String ptAccName;
	
	private String spAccid;

	private String spAccPwd;
	
	private String spId;
	
	private String serviceType;
	
	private Integer feeUserType;
	
	private String ptIp;
	
	private Integer ptPort;
	
	private String spIp;
	
	private Integer spPort;
	
	private Integer speedLimit;
	
	private Integer protocolCode;
	
	private String protocolParam;
	
	private Integer spType;
	//运营商余额获取地址
	private String feeUrl ;
	//运营商余额
	private Long balance;
	//余额查询阀值
	private Long balanceTh ;
	//更新时间
	private Timestamp updateTime ;
	//运营商账户付费类型(1:预付费,2:后付费)
	private Integer spFeeFlag;

    //网关节点
    private String ptNode;

	public AgwAccount(){}

	public Integer getGwNo()
	{
		return gwNo;
	}

	public void setGwNo(Integer gwNo)
	{
		this.gwNo = gwNo;
	}

	public Integer getPtAccUid()
	{
		return ptAccUid;
	}

	public void setPtAccUid(Integer ptAccUid)
	{
		this.ptAccUid = ptAccUid;
	}

	public String getPtAccName()
	{
		return ptAccName;
	}

	public void setPtAccName(String ptAccName)
	{
		this.ptAccName = ptAccName;
	}

	public String getSpAccid()
	{
		return spAccid;
	}

	public void setSpAccid(String spAccid)
	{
		this.spAccid = spAccid;
	}

	public String getSpAccPwd()
	{
		return spAccPwd;
	}

	public void setSpAccPwd(String spAccPwd)
	{
		this.spAccPwd = spAccPwd;
	}

	public String getSpId()
	{
		return spId;
	}

	public void setSpId(String spId)
	{
		this.spId = spId;
	}

	public String getServiceType()
	{
		return serviceType;
	}

	public void setServiceType(String serviceType)
	{
		this.serviceType = serviceType;
	}

	public Integer getFeeUserType()
	{
		return feeUserType;
	}

	public void setFeeUserType(Integer feeUserType)
	{
		this.feeUserType = feeUserType;
	}

	public String getPtIp()
	{
		return ptIp;
	}

	public void setPtIp(String ptIp)
	{
		this.ptIp = ptIp;
	}

	public Integer getPtPort()
	{
		return ptPort;
	}

	public void setPtPort(Integer ptPort)
	{
		this.ptPort = ptPort;
	}

	public String getSpIp()
	{
		return spIp;
	}

	public void setSpIp(String spIp)
	{
		this.spIp = spIp;
	}

	public Integer getSpPort()
	{
		return spPort;
	}

	public void setSpPort(Integer spPort)
	{
		this.spPort = spPort;
	}

	public Integer getSpeedLimit()
	{
		return speedLimit;
	}

	public void setSpeedLimit(Integer speedLimit)
	{
		this.speedLimit = speedLimit;
	}

	public Integer getProtocolCode()
	{
		return protocolCode;
	}

	public void setProtocolCode(Integer protocolCode)
	{
		this.protocolCode = protocolCode;
	}

	public String getProtocolParam()
	{
		return protocolParam;
	}

	public void setProtocolParam(String protocolParam)
	{
		this.protocolParam = protocolParam;
	}

	public String getPtAccpwd()
	{
		return ptAccpwd;
	}

	public void setPtAccpwd(String ptAccpwd)
	{
		this.ptAccpwd = ptAccpwd;
	}

	public String getPtAccId() {
		return ptAccId;
	}

	public void setPtAccId(String ptAccId) {
		this.ptAccId = ptAccId;
	}

	public Integer getSpType() {
		return spType;
	}

	public void setSpType(Integer spType) {
		this.spType = spType;
	}

	public String getFeeUrl() {
		return feeUrl;
	}

	public void setFeeUrl(String feeUrl) {
		this.feeUrl = feeUrl;
	}

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public Long getBalanceTh() {
		return balanceTh;
	}

	public void setBalanceTh(Long balanceTh) {
		this.balanceTh = balanceTh;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getSpFeeFlag() {
		return spFeeFlag;
	}

	public void setSpFeeFlag(Integer spFeeFlag) {
		this.spFeeFlag = spFeeFlag;
	}

    public String getPtNode() {
        return ptNode;
    }

    public void setPtNode(String ptNode) {
        this.ptNode = ptNode;
    }
}
