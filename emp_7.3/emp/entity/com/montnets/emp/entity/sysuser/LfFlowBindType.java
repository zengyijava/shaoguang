/**
 * 
 */
package com.montnets.emp.entity.sysuser;

import java.sql.Timestamp;


/**
 * 审核流程绑定类型
 * @project p_tabl
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-7-19 上午11:05:45
 * @description
 */
public class LfFlowBindType implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8021597232895841786L;
	//主键，自增id
	private Long id;
	//审核流ID，应用lf_flow的fid
	private Long FId; 
	//模块编码。
	private String menuCode;
	//信息类型。1：短信发送；2：彩信发送；3：短信模板；4：彩信模板；
	private Integer infoType;
	//修改时间
	private Timestamp updateTime;
	//新增时间
	private Timestamp createTime;
	
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Long getFId()
	{
		return FId;
	}
	public void setFId(Long fId)
	{
		FId = fId;
	}
	public String getMenuCode()
	{
		return menuCode;
	}
	public void setMenuCode(String menuCode)
	{
		this.menuCode = menuCode;
	}
	public Integer getInfoType()
	{
		return infoType;
	}
	public void setInfoType(Integer infoType)
	{
		this.infoType = infoType;
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
	
	
	
	
	

	
}
