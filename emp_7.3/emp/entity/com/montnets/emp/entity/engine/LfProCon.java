/**
 * 
 */
package com.montnets.emp.entity.engine;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 下午02:16:07
 * @description 
 */

public class LfProCon implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5097785389445890617L;
	/**
	 * 
	 */
	//private static final long serialVersionUID = 7754381090605772612L;
	private Long dbconId;
	private Long prId;
	private String conExpress;
	private String conOperate;
	private String conValue;
	private String comments;
	private Long  UsedPrId;
	
	public LfProCon(){}
	
	
	public Long getDbconId()
	{
		return dbconId;
	}

	public void setDbconId(Long dbconId)
	{
		this.dbconId = dbconId;
	}

	public Long getPrId()
	{
		return prId;
	}

	public void setPrId(Long prId)
	{
		this.prId = prId;
	}

	public String getConExpress()
	{
		return conExpress;
	}

	public void setConExpress(String conExpress)
	{
		this.conExpress = conExpress;
	}

	public String getConOperate()
	{
		return conOperate;
	}

	public void setConOperate(String conOperate)
	{
		this.conOperate = conOperate;
	}

	public String getConValue()
	{
		return conValue;
	}

	public void setConValue(String conValue)
	{
		this.conValue = conValue;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}


	public Long getUsedPrId() {
		return UsedPrId;
	}


	public void setUsedPrId(Long usedPrId) {
		UsedPrId = usedPrId;
	}
	
	
	 
}
