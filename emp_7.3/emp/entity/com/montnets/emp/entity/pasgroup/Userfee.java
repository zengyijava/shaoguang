package com.montnets.emp.entity.pasgroup;


/**
 * TableUserfee对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:21:13
 * @description 
 */
public class Userfee implements java.io.Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 9019648025228871824L;
	//SP账号表中UID值(主键)
	private Long uid;
	
	private Long ecid;
	//SP账号
	private String userId;
	//账户余额
	private Long sendNum;
	//预付费短信发送数。默认为0
	private Long sendedNum;
	//后付费短信发送数。默认为0
	private Long postPayUsed;
	
    private Long thresHold;
  
	public Userfee() {
		this.ecid = new Long(0);
		this.userId = " ";
		this.sendNum=new Long(0);
		this.sendedNum=new Long(0);
		this.postPayUsed = new Long(0);
		this.thresHold=new Long(10000);
		
	}
  
	public Long getUid()
	{
		return uid;
	}

	public void setUid(Long uid)
	{
		this.uid = uid;
	}

 
	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public Long getSendNum()
	{
		return sendNum;
	}

	public void setSendNum(Long sendNum)
	{
		this.sendNum = sendNum;
	}

	public Long getSendedNum()
	{
		return sendedNum;
	}

	public void setSendedNum(Long sendedNum)
	{
		this.sendedNum = sendedNum;
	}

	 
	public Long getEcid()
	{
		return ecid;
	}

	public void setEcid(Long ecid)
	{
		this.ecid = ecid;
	}

	public Long getPostPayUsed()
	{
		return postPayUsed;
	}

	public void setPostPayUsed(Long postPayUsed)
	{
		this.postPayUsed = postPayUsed;
	}

	public Long getThresHold()
	{
		return thresHold;
	}

	public void setThresHold(Long thresHold)
	{
		this.thresHold = thresHold;
	}

 
}