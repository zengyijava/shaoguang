/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-29 上午09:32:32
 */
package com.montnets.emp.mobilebus.biz;

/**
 * @description 
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-29 上午09:32:32
 */

public class BuckleFeeParams
{
	//流水号
	private String msgId;
	//操作状态，0：成功；1：失败
	private String state;
	//是否最后一次补扣
	private String lastBucleUpFee;
	//操作时间
	private Long time;

	public String getMsgId()
	{
		return msgId;
	}

	public void setMsgId(String msgId)
	{
		this.msgId = msgId;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getLastBucleUpFee()
	{
		return lastBucleUpFee;
	}

	public void setLastBucleUpFee(String lastBucleUpFee)
	{
		this.lastBucleUpFee = lastBucleUpFee;
	}

	public Long getTime()
	{
		return time;
	}

	public void setTime(Long time)
	{
		this.time = time;
	}
	
	
}
