package com.montnets.emp.engine.vo;

import java.sql.Timestamp;

/**
 * 上行服务业务vo类
 * @project testofsinolife
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-31 上午10:05:27
 * @description 上行服务业务vo类
 */
public class LfServiceVo implements java.io.Serializable
{
	/**
	 * 上行服务业务vo类
	 */
	private static final long serialVersionUID = -2680011553958261218L;
	//业务
	private Long serId;
	//业务名称
	private String serName;
	//运行状态
	private Integer runState;
	//业务类型
	private Integer serType;
	//指令代码
	private String orderCode;
	//创建时间
	private Timestamp createTime;
	//模块编码
	private String menuCode;
	//命令
	private String commnets;	
	//操作员名称
	private String userName;
	//拥有者名称
	private String ownerUserName;
	//姓名
	private String name;
	//拥有者姓名
	private String ownerName;
	//状态
	private Integer userState;
	//识别模式。空和1-使用尾号；2-使用指令
	private Integer identifyMode;
	//企业编码
	private String corpCode;
	//业务id密文
	private String serIdCipher;
	
	
	public String getCorpCode()
	{
		return corpCode;
	}
	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
	public Integer getIdentifyMode()
	{
		return identifyMode;
	}
	public void setIdentifyMode(Integer identifyMode)
	{
		this.identifyMode = identifyMode;
	}
	//获取状态
	public Integer getUserState()
	{
		return userState;
	}
	//设置状态
	public void setUserState(Integer userState)
	{
		this.userState = userState;
	}	
	
	public Long getSerId()
	{
		return serId;
	}
	public void setSerId(Long serId)
	{
		this.serId = serId;
	}
	public String getSerName()
	{
		return serName;
	}
	public void setSerName(String serName)
	{
		this.serName = serName;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getOwnerUserName()
	{
		return ownerUserName;
	}
	public void setOwnerUserName(String ownerUserName)
	{
		this.ownerUserName = ownerUserName;
	}
	public Integer getRunState()
	{
		return runState;
	}
	public void setRunState(Integer runState)
	{
		this.runState = runState;
	}
	public String getOrderCode()
	{
		return orderCode;
	}
	public void setOrderCode(String orderCode)
	{
		this.orderCode = orderCode;
	}
	public Timestamp getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getOwnerName()
	{
		return ownerName;
	}
	public void setOwnerName(String ownerName)
	{
		this.ownerName = ownerName;
	}
	public Integer getSerType()
	{
		return serType;
	}
	public void setSerType(Integer serType)
	{
		this.serType = serType;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getCommnets() {
		return commnets;
	}
	public void setCommnets(String commnets) {
		this.commnets = commnets;
	}
	/**
	 * 
	 * @description 业务id密文
	 * @return 业务id密文
	 */
	public String getSerIdCipher()
	{
		return serIdCipher;
	}
	/**
	 * 
	 * @description 业务id密文
	 * @param serIdCipher 业务id密文
	 */
	public void setSerIdCipher(String serIdCipher)
	{
		this.serIdCipher = serIdCipher;
	}	
	
}
