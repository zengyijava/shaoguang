/**
 * 
 */
package com.montnets.emp.entity.sysuser;

import java.sql.Timestamp;


/**
 * 审核开关表
 * @project p_tabl
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-7-19 上午11:00:06
 * @description
 */
public class LfReviewSwitch implements java.io.Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3329505469582265514L;
	
	//主键，自增id
	private Long id;
	//模块编码。
	private String menuCode;
	//开关状态。1：开；2：关
	private Integer switchType;
	//审批阀值，默认值0，即没阀值，针对发送模块
	private Integer msgCount;
	//信息类型。1：短信发送；2：彩信发送；3：短信模板；4：彩信模板；6:网讯模板
	private Integer infoType;
	//企业编码
	private String corpCode;
	//修改时间
	private Timestamp updateTime;
	
	public LfReviewSwitch()
	{
		
	}
	/**
	 * 实体类构造方法
	 * @param switchType 开关状态
	 * @param msgCount 审核阀值
	 * @param infoType 信息类型
	 * @param corpCode 企业编码
	 */
	public LfReviewSwitch(Integer switchType,Integer msgCount,Integer infoType,String corpCode)
	{
		this.switchType = switchType;
		this.msgCount = msgCount;
		this.infoType = infoType;
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
	public String getMenuCode()
	{
		return menuCode;
	}
	public void setMenuCode(String menuCode)
	{
		this.menuCode = menuCode;
	}
	public Integer getSwitchType()
	{
		return switchType;
	}
	public void setSwitchType(Integer switchType)
	{
		this.switchType = switchType;
	}
	public Integer getMsgCount()
	{
		return msgCount;
	}
	public void setMsgCount(Integer msgCount)
	{
		this.msgCount = msgCount;
	}
	public Integer getInfoType()
	{
		return infoType;
	}
	public void setInfoType(Integer infoType)
	{
		this.infoType = infoType;
	}
	public String getCorpCode()
	{
		return corpCode;
	}
	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
	public Timestamp getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}
	
	
	

	
}
