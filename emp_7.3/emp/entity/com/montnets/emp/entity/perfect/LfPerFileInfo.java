/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-10-29 上午10:05:12
 */
package com.montnets.emp.entity.perfect;

/**
 * @description 
 * @project p_dxzs
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-10-29 上午10:05:12
 */

public class LfPerFileInfo implements java.io.Serializable
{
	/**
	 * @description  
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-10-29 上午10:06:30
	 */
	private static final long	serialVersionUID	= -5808584557729894598L;
	//任务ID
	private Long taskId;
	//发送文件路径
	private String sendFileName;
	
	public Long getTaskId()
	{
		return taskId;
	}
	public void setTaskId(Long taskId)
	{
		this.taskId = taskId;
	}
	public String getSendFileName()
	{
		return sendFileName;
	}
	public void setSendFileName(String sendFileName)
	{
		this.sendFileName = sendFileName;
	}
	
}
