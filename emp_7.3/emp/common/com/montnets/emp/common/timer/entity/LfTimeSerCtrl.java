package com.montnets.emp.common.timer.entity;

import java.sql.Timestamp;

/**
 * 
 * @功能概要：定时服务控制表
 * @项目名称： emp_std_192.169.1.81_2new
 * @初创作者： huangzb <huangzb@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-3-23 下午04:23:57
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class LfTimeSerCtrl
{
	//主键，格式：节点id+yyMMddHHmmss+6位随机数
	private String timeServerID;
	
	//通过不断更新这个时间来证明服务是活动状态。用数据库的当前时间来填，不能用节点自己的值来填。
	private Timestamp updateTime;
	
	//SystemGlobals.properties的cluster.server.number的值，标识每个EMP节点的ID。目前无作用
	private String nodeId;
	
	//定时服务所在主机的IP，有多个则用英文逗号分隔，最多保存10个ip。目前无作用，记录ip方便维护
	private String serverIP;
	
	//定时服务主机端口
	private Integer serverPort;
	
	//数据库当前时间
	private Timestamp dbCurrentTime;
	
	
	public Timestamp getDbCurrentTime()
	{
		return dbCurrentTime;
	}
	public void setDbCurrentTime(Timestamp dbCurrentTime)
	{
		this.dbCurrentTime = dbCurrentTime;
	}
	public String getTimeServerID()
	{
		return timeServerID;
	}
	public void setTimeServerID(String timeServerID)
	{
		this.timeServerID = timeServerID;
	}
	public Timestamp getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}
	public String getNodeId()
	{
		return nodeId;
	}
	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}
	public String getServerIP()
	{
		return serverIP;
	}
	public void setServerIP(String serverIP)
	{
		this.serverIP = serverIP;
	}
	public Integer getServerPort()
	{
		return serverPort;
	}
	public void setServerPort(Integer serverPort)
	{
		this.serverPort = serverPort;
	}
	
}