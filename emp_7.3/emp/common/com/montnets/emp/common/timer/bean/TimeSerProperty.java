package com.montnets.emp.common.timer.bean;

/**
 * 
 * @功能概要：定时服务属性
 * @项目名称： emp_std_192.169.1.81_2new
 * @初创作者： huangzb <huangzb@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-3-24 下午02:19:22
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class TimeSerProperty
{
	private String timeServerID;
	
	private long timerActiveTime;
	
	private String nodeId;
	
	private String serverIp;
	
	private Integer serverPort;
	
	//控制状态，0-未处理；1-处理中
	private Integer dealState;
	
	//本机web访问URL，SystemGlobals.properties所配置
	private String serverURL;
	
	//接收处理权是否被其他定时服务接管，被接管则为true；未被接管则为false
	private boolean isOtherCtrl;
	
	/**
	 * 
	 * @description 接收处理权是否被其他定时服务接管，被接管则为true；未被接管则为false
	 * @return       			 
	 */
	public boolean getIsOtherCtrl()
	{
		return isOtherCtrl;
	}

	/**
	 * 
	 * @description 接收处理权是否被其他定时服务接管，被接管则为true；未被接管则为false
	 * @param isOtherCtrl       			 
	 */
	public void setIsOtherCtrl(boolean isOtherCtrl)
	{
		this.isOtherCtrl = isOtherCtrl;
	}

	public String getTimeServerID()
	{
		return timeServerID;
	}

	public void setTimeServerID(String timeServerID)
	{
		this.timeServerID = timeServerID;
	}

	public long getTimerActiveTime()
	{
		return timerActiveTime;
	}

	public void setTimerActiveTime(long timerActiveTime)
	{
		this.timerActiveTime = timerActiveTime;
	}

	public String getServerIp()
	{
		return serverIp;
	}

	public void setServerIp(String serverIp)
	{
		this.serverIp = serverIp;
	}

	public Integer getServerPort()
	{
		return serverPort;
	}

	public void setServerPort(Integer serverPort)
	{
		this.serverPort = serverPort;
	}

	public String getNodeId()
	{
		return nodeId;
	}

	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}

	/**
	 * 
	 * @return 控制状态，0-未处理；1-处理中
	 */
	public Integer getDealState()
	{
		return dealState;
	}

	/**
	 * 
	 * @param dealState 控制状态，0-未处理；1-处理中
	 */
	public void setDealState(Integer dealState)
	{
		this.dealState = dealState;
	}

	public String getServerURL()
	{
		return serverURL;
	}

	public void setServerURL(String serverURL)
	{
		this.serverURL = serverURL;
	}
	
}
