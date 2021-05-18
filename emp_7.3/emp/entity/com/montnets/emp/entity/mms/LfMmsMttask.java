/**
 * 
 */
package com.montnets.emp.entity.mms;

import java.sql.Timestamp;

/**
 * 
 * 彩信下行记录实体类
 * 
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-18 下午06:50:45
 * @description 
 */

public class LfMmsMttask implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4901689847357466779L;
	//自增
	private Long mmsId;
	//操作员ID
	private Long userId;
	//手机号码
	private String phone;
	//彩信内容
	private String mmsMsg;
	//发送状态
	private Integer sendStatus;
	//发送时间
	private Timestamp sendTime;
	
	public LfMmsMttask(){}

	public Long getMmsId()
	{
		return mmsId;
	}

	public void setMmsId(Long mmsId)
	{
		this.mmsId = mmsId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getMmsMsg()
	{
		return mmsMsg;
	}

	public void setMmsMsg(String mmsMsg)
	{
		this.mmsMsg = mmsMsg;
	}

	public Integer getSendStatus()
	{
		return sendStatus;
	}

	public void setSendStatus(Integer sendStatus)
	{
		this.sendStatus = sendStatus;
	}

	public Timestamp getSendTime()
	{
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime)
	{
		this.sendTime = sendTime;
	}
	
	
}
