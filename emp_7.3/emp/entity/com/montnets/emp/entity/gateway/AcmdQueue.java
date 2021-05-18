/**
 * 
 */
package com.montnets.emp.entity.gateway;

import java.sql.Timestamp;

/**
 * @project montnets_gateway
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-3 下午06:43:09
 * @description
 */

public class AcmdQueue implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5387717704313494189L;

	private Integer id;

	private Integer gwNo;

	private Integer gwType;

	private String cmdInfo;

	private Integer cmdType;
	
	private String cmdParam;

	private Integer dealStatus;

	private String resultCode;

	private Timestamp reqTime;

	private Timestamp doneTime;

	public AcmdQueue()
	{
		this.cmdType = new Integer (0);
		this.gwNo = new Integer(99);
		this.gwType = new Integer(4000);
		this.cmdInfo = " ";
		this.cmdParam = " ";
		this.dealStatus = new Integer(1);
		this.resultCode = "SUCCESS";
		this.reqTime = new Timestamp(System.currentTimeMillis());
		this.doneTime = new Timestamp(System.currentTimeMillis());
		
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getGwNo()
	{
		return gwNo;
	}

	public void setGwNo(Integer gwNo)
	{
		this.gwNo = gwNo;
	}

	public Integer getGwType()
	{
		return gwType;
	}

	public void setGwType(Integer gwType)
	{
		this.gwType = gwType;
	}

	public String getCmdInfo()
	{
		return cmdInfo;
	}

	public void setCmdInfo(String cmdInfo)
	{
		this.cmdInfo = cmdInfo;
	}

	public String getCmdParam()
	{
		return cmdParam;
	}

	public void setCmdParam(String cmdParam)
	{
		this.cmdParam = cmdParam;
	}

	public Integer getDealStatus()
	{
		return dealStatus;
	}

	public void setDealStatus(Integer dealStatus)
	{
		this.dealStatus = dealStatus;
	}

	public String getResultCode()
	{
		return resultCode;
	}

	public void setResultCode(String resultCode)
	{
		this.resultCode = resultCode;
	}

	public Timestamp getReqTime()
	{
		return reqTime;
	}

	public void setReqTime(Timestamp reqTime)
	{
		this.reqTime = reqTime;
	}

	public Timestamp getDoneTime()
	{
		return doneTime;
	}

	public void setDoneTime(Timestamp doneTime)
	{
		this.doneTime = doneTime;
	}

	public Integer getCmdType() {
		return cmdType;
	}

	public void setCmdType(Integer cmdType) {
		this.cmdType = cmdType;
	}

}
