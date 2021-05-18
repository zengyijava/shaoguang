/**
 * 
 */
package com.montnets.emp.entity.engine;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-28 上午10:11:17
 * @description 
 */

public class LfReply implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7915732150043740414L;
	/**
	 * 
	 */
	//private static final long serialVersionUID = 7915732150043740414L;
	private Long prId;
	private String msgHeader;
	private String msgMain;
	private String msgTail;
	private Long msgLoopId;
	private Long reId;
	
	public LfReply(){}

	public Long getPrId()
	{
		return prId;
	}

	public void setPrId(Long prId)
	{
		this.prId = prId;
	}

	public String getMsgHeader()
	{
		return msgHeader;
	}

	public void setMsgHeader(String msgHeader)
	{
		this.msgHeader = msgHeader;
	}

	public String getMsgMain()
	{
		return msgMain;
	}

	public void setMsgMain(String msgMain)
	{
		this.msgMain = msgMain;
	}

	public String getMsgTail()
	{
		return msgTail;
	}

	public void setMsgTail(String msgTail)
	{
		this.msgTail = msgTail;
	}

	public Long getMsgLoopId()
	{
		return msgLoopId;
	}

	public void setMsgLoopId(Long msgLoopId)
	{
		this.msgLoopId = msgLoopId;
	}

	public Long getReId()
	{
		return reId;
	}

	public void setReId(Long reId)
	{
		this.reId = reId;
	}
	
	
}
