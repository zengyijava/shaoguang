package com.montnets.emp.common.vo;

import java.sql.Timestamp;

public class LfSubnoAllotVo implements java.io.Serializable
{
	/**
	 * 尾号vo
	 */
	private static final long serialVersionUID = -6940578017336718238L;
    //主键
	private Long suId;

//	private String menuCode;

//	private String loginId;
    //发送账号
	private String spUser;
    //尾号
	private String subno;
	//分配类型
	private Integer allotType;
    //扩展尾号开始值
	private String extendSubnoBegin;
    //扩展尾号结束值
	private String extendSubnoEnd;
    //可用扩展尾号
	private String usedExtendSubno;
	//全通道号
	private String spNumber;
    //修改时间
	private Timestamp updateTime;
    //创建时间
	private Timestamp createTime;
	//路由id（暂不用）
	private Long routeId;
	
//	private String busCode;
	//编码（暂不用）
	private String codes;
	//编码类型（暂不用）
	private Integer codeType;
	//模块名称 
	private String menuName;
	//业务类型名称
	private String busName;
	//产品名称（暂不用）
	private String productName;
	//用户名称
	private String userName;
	//机构名称
	private String depName;
	//用户名称
	private String name;
	//企业编码
	private String corpCode;	
	//获取企业编码
	public String getCorpCode() {
		return corpCode;
	}
	//设置企业编码
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public Long getSuId()
	{
		return suId;
	}

	public void setSuId(Long suId)
	{
		this.suId = suId;
	}

	public String getSpUser()
	{
		return spUser;
	}

	public void setSpUser(String spUser)
	{
		this.spUser = spUser;
	}

	public String getSubno()
	{
		return subno;
	}

	public void setSubno(String subno)
	{
		this.subno = subno;
	}

	public Integer getAllotType()
	{
		return allotType;
	}

	public void setAllotType(Integer allotType)
	{
		this.allotType = allotType;
	}

	public String getExtendSubnoBegin()
	{
		return extendSubnoBegin;
	}

	public void setExtendSubnoBegin(String extendSubnoBegin)
	{
		this.extendSubnoBegin = extendSubnoBegin;
	}

	public String getExtendSubnoEnd()
	{
		return extendSubnoEnd;
	}

	public void setExtendSubnoEnd(String extendSubnoEnd)
	{
		this.extendSubnoEnd = extendSubnoEnd;
	}

	public String getUsedExtendSubno()
	{
		return usedExtendSubno;
	}

	public void setUsedExtendSubno(String usedExtendSubno)
	{
		this.usedExtendSubno = usedExtendSubno;
	}

	public String getSpNumber()
	{
		return spNumber;
	}

	public void setSpNumber(String spNumber)
	{
		this.spNumber = spNumber;
	}

	public Timestamp getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}

	public Long getRouteId()
	{
		return routeId;
	}

	public void setRouteId(Long routeId)
	{
		this.routeId = routeId;
	}

	public String getMenuName()
	{
		return menuName;
	}

	public void setMenuName(String menuName)
	{
		this.menuName = menuName;
	}

	public String getBusName()
	{
		return busName;
	}

	public void setBusName(String busName)
	{
		this.busName = busName;
	}

	public String getCodes()
	{
		return codes;
	}

	public void setCodes(String codes)
	{
		this.codes = codes;
	}

	public Integer getCodeType()
	{
		return codeType;
	}

	public void setCodeType(Integer codeType)
	{
		this.codeType = codeType;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getDepName()
	{
		return depName;
	}

	public void setDepName(String depName)
	{
		this.depName = depName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}	
}
