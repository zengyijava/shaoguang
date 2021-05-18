/**
 * 
 */
package com.montnets.emp.entity.engine;

import java.sql.Timestamp;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-28 上午08:49:28
 * @description
 */

public class LfService implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2490782610219363131L;

	/**
	 * 
	 */
	// private static final long serialVersionUID = -5129046027358376884L;
	private Long serId;

	private Long userId;

	private String serName;

	private String commnets;

	private String orderCode;

	private String subNo;

	private String spUser;

	private String spPwd;

	private Integer runState;

	private Integer serType;

	private String msgSeparated;

	private Long ownerId;

	private Timestamp createTime;

	private Long busId;

	private String cpno;

	private String busCode;

	private String menuCode;
	
	private String corpCode;
	//识别模式。空和1-使用尾号；2-使用指令
	private Integer identifyMode;
	
	//指令类型。1-动态指令（模糊）；0-静态指令（精确）
	private Integer orderType;
	
	//指令和分隔符，如CX#
	private String structcode;
	
	public LfService()
	{
		createTime = new Timestamp(System.currentTimeMillis());
		cpno = " ";
	}
	
	
	public Integer getOrderType()
	{
		return orderType;
	}

	public void setOrderType(Integer orderType)
	{
		this.orderType = orderType;
	}


	public String getStructcode()
	{
		return structcode;
	}


	public void setStructcode(String structcode)
	{
		this.structcode = structcode;
	}


	public Integer getIdentifyMode()
	{
		return identifyMode;
	}

	public void setIdentifyMode(Integer identifyMode)
	{
		this.identifyMode = identifyMode;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public Long getSerId()
	{
		return serId;
	}

	public void setSerId(Long serId)
	{
		this.serId = serId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getSerName()
	{
		return serName;
	}

	public void setSerName(String serName)
	{
		this.serName = serName;
	}

	public String getCommnets()
	{
		return commnets;
	}

	public void setCommnets(String commnets)
	{
		this.commnets = commnets;
	}

	public String getOrderCode()
	{
		return orderCode;
	}

	public void setOrderCode(String orderCode)
	{
		this.orderCode = orderCode;
	}

	public String getSubNo()
	{
		return subNo;
	}

	public void setSubNo(String subNo)
	{
		this.subNo = subNo;
	}

	public String getSpUser()
	{
		return spUser;
	}

	public void setSpUser(String spUser)
	{
		this.spUser = spUser;
	}

	public String getSpPwd()
	{
		return spPwd;
	}

	public void setSpPwd(String spPwd)
	{
		this.spPwd = spPwd;
	}

	public Integer getRunState()
	{
		return runState;
	}

	public void setRunState(Integer runState)
	{
		this.runState = runState;
	}

	public Integer getSerType()
	{
		return serType;
	}

	public void setSerType(Integer serType)
	{
		this.serType = serType;
	}

	public String getMsgSeparated()
	{
		return msgSeparated;
	}

	public void setMsgSeparated(String msgSeparated)
	{
		this.msgSeparated = msgSeparated;
	}

	public Long getOwnerId()
	{
		return ownerId;
	}

	public void setOwnerId(Long ownerId)
	{
		this.ownerId = ownerId;
	}

	public Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}

	public Long getBusId()
	{
		return busId;
	}

	public void setBusId(Long busId)
	{
		this.busId = busId;
	}

	public String getCpno()
	{
		return cpno;
	}

	public void setCpno(String cpno)
	{
		this.cpno = cpno;
	}

	public String getBusCode()
	{
		return busCode;
	}

	public void setBusCode(String busCode)
	{
		this.busCode = busCode;
	}

	public String getMenuCode()
	{
		return menuCode;
	}

	public void setMenuCode(String menuCode)
	{
		this.menuCode = menuCode;
	}

}
