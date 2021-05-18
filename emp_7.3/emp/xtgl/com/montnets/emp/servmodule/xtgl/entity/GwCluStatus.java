package com.montnets.emp.servmodule.xtgl.entity;

import java.sql.Timestamp;
/**
 * 
 * @功能概要：集群网关状态表
 * @项目名称： emp_std_201508
 * @初创作者： tanglili <jack860127@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-28 下午02:57:38
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class GwCluStatus
{
	private Long id;
	
	//网关类型
	private Integer gwType;
	
	//网关编号
	private Integer gwNo;
	
	//主用网关编号
	private Integer priGwNo;
	
	//运行状态
	private Integer runstatus;
	
	//网关权值
	private Integer gweight;
	
	//网关运行权值
	private Integer runweight;
	
	//状态更新时间
	private Timestamp updtime;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Integer getGwType()
	{
		return gwType;
	}

	public void setGwType(Integer gwType)
	{
		this.gwType = gwType;
	}

	public Integer getGwNo()
	{
		return gwNo;
	}

	public void setGwNo(Integer gwNo)
	{
		this.gwNo = gwNo;
	}

	public Integer getPriGwNo()
	{
		return priGwNo;
	}

	public void setPriGwNo(Integer priGwNo)
	{
		this.priGwNo = priGwNo;
	}

	public Integer getRunstatus()
	{
		return runstatus;
	}

	public void setRunstatus(Integer runstatus)
	{
		this.runstatus = runstatus;
	}

	public Integer getGweight()
	{
		return gweight;
	}

	public void setGweight(Integer gweight)
	{
		this.gweight = gweight;
	}

	public Integer getRunweight()
	{
		return runweight;
	}

	public void setRunweight(Integer runweight)
	{
		this.runweight = runweight;
	}

	public Timestamp getUpdtime()
	{
		return updtime;
	}

	public void setUpdtime(Timestamp updtime)
	{
		this.updtime = updtime;
	}
	
	
}
