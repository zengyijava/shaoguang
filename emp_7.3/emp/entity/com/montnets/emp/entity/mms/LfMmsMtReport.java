/**
 * 
 */
package com.montnets.emp.entity.mms;

import java.sql.Timestamp;

/**
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-10-14 上午09:38:16
 * @description 彩信下行汇总实体类
 */

public class LfMmsMtReport implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3999984635775196242L;
	//用户账号
	private String userId;
	//任务ID
	private Long taskId;
	//主端口
	private String spgate;
	//年月日
	private Long iymd;
	//小时
	private Long ihour;
	//月份
 	private Long imonth;
 	//发送总数
	private Long icount;
	//成功数包括未返
	private Long rsucc;
	//条件失败
	private Long rfail1;
	//失败2数量(真正的失败数)
	private Long rfail2;
	//未返数
	private Long rnret;
	//开始时间
	private Timestamp startTime;
	//结束时间
	private Timestamp endTime;
	//标识列
	private Long id;
	//年
	private Long y;
	//运营商:(0: 移动,1: 联通,21: 电信)
	private Long spisuncm;
	
	public LfMmsMtReport(){}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public Long getTaskId()
	{
		return taskId;
	}

	public void setTaskId(Long taskId)
	{
		this.taskId = taskId;
	}

	public String getSpgate()
	{
		return spgate;
	}

	public void setSpgate(String spgate)
	{
		this.spgate = spgate;
	}

	public Long getIymd()
	{
		return iymd;
	}

	public void setIymd(Long iymd)
	{
		this.iymd = iymd;
	}

	public Long getIhour()
	{
		return ihour;
	}

	public void setIhour(Long ihour)
	{
		this.ihour = ihour;
	}

	public Long getImonth()
	{
		return imonth;
	}

	public void setImonth(Long imonth)
	{
		this.imonth = imonth;
	}

	public Long getIcount()
	{
		return icount;
	}

	public void setIcount(Long icount)
	{
		this.icount = icount;
	}

	public Long getRsucc()
	{
		return rsucc;
	}

	public void setRsucc(Long rsucc)
	{
		this.rsucc = rsucc;
	}

	public Long getRfail1()
	{
		return rfail1;
	}

	public void setRfail1(Long rfail1)
	{
		this.rfail1 = rfail1;
	}

	public Long getRfail2()
	{
		return rfail2;
	}

	public void setRfail2(Long rfail2)
	{
		this.rfail2 = rfail2;
	}

	public Long getRnret()
	{
		return rnret;
	}

	public void setRnret(Long rnret)
	{
		this.rnret = rnret;
	}

	public Timestamp getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Timestamp startTime)
	{
		this.startTime = startTime;
	}

	public Timestamp getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Timestamp endTime)
	{
		this.endTime = endTime;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getY()
	{
		return y;
	}

	public void setY(Long y)
	{
		this.y = y;
	}

	public Long getSpisuncm()
	{
		return spisuncm;
	}

	public void setSpisuncm(Long spisuncm)
	{
		this.spisuncm = spisuncm;
	}
	
	
	
}
