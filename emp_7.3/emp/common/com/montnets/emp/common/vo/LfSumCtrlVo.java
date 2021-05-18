package com.montnets.emp.common.vo;

import java.sql.Timestamp;

/**
 * 汇总控制记录表
 * @功能概要：
 * @项目名称： p_xtgl
 * @初创作者： liuxiangheng <xiaokanrensheng1012@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-3-30 上午11:47:34
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class LfSumCtrlVo
{
	//自增ID
	private Integer id;
	//SystemGlobals.properties的cluster.server.number的值，标识每个EMP节点的ID。目前无作用
	private String nodeId;
	
	//是否主控制记录（1主控制记录0各个节点）
	private Integer isMain;
	
	//0代表晚上汇总，1代表白天汇总
	private Integer sumType;
	
	//执行时间通过不断更新这个时间来证明服务是活动状态。用数据库的当前时间来填，不能用节点自己的值来填。
	private Timestamp updateTime;
	
	//数据库当前时间
	private Timestamp dbcurrenttime;
	
	//tomcat集群内网地址
	private String serLocalURL;

	public String getSerLocalURL()
	{
		return serLocalURL;
	}

	public void setSerLocalURL(String serLocalURL)
	{
		this.serLocalURL = serLocalURL;
	}

	public Timestamp getDbcurrenttime()
	{
		return dbcurrenttime;
	}

	public void setDbcurrenttime(Timestamp dbcurrenttime)
	{
		this.dbcurrenttime = dbcurrenttime;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getNodeId()
	{
		return nodeId;
	}

	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}

	public Integer getIsMain()
	{
		return isMain;
	}

	public void setIsMain(Integer isMain)
	{
		this.isMain = isMain;
	}

	public Integer getSumType()
	{
		return sumType;
	}

	public void setSumType(Integer sumType)
	{
		this.sumType = sumType;
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