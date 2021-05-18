/**
 * 
 */
package com.montnets.emp.entity.template;

import java.sql.Timestamp;



public class LfTmplRela implements java.io.Serializable
{
	/**
	 * @description  
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-11-13 下午04:13:42
	 */
	private static final long	serialVersionUID	= -5185840264315135252L;
	/**
	 * 
	 */
	//自增id
	private Long id;
	//模板ID
	private Long templId;
	//模板共享类型，1-机构，2--操作员
	private Integer toUserType=2;
	//操作员或机构唯一编码，TYPE为2时，此字段为操作员ID，1时为机构ID
	private Long toUser;
	//新增时间
	private Timestamp createTime;
	//模板类型 1：短信模板；2：彩信模板；3:网讯模板
	private Integer templType=1;
	//是否为共享 0不共享 1 共享
	private Integer shareType=0;
	//模板创建者Id
	private Long createrId;
	//企业编号
	private String corpCode;
	public Long getTemplId()
	{
		return templId;
	}
	public void setTemplId(Long templId)
	{
		this.templId = templId;
	}
	public Integer getToUserType()
	{
		return toUserType;
	}
	public void setToUserType(Integer toUserType)
	{
		this.toUserType = toUserType;
	}
	public Long getToUser()
	{
		return toUser;
	}
	public void setToUser(Long toUser)
	{
		this.toUser = toUser;
	}
	public Timestamp getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}
	public Integer getTemplType()
	{
		return templType;
	}
	public void setTemplType(Integer templType)
	{
		this.templType = templType;
	}
	public Integer getShareType()
	{
		return shareType;
	}
	public void setShareType(Integer shareType)
	{
		this.shareType = shareType;
	}
	public Long getCreaterId()
	{
		return createrId;
	}
	public void setCreaterId(Long createrId)
	{
		this.createrId = createrId;
	}
	public String getCorpCode()
	{
		return corpCode;
	}
	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	
}
