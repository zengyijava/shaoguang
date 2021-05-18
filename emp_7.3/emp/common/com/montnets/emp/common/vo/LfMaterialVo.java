/**
 * 
 */
package com.montnets.emp.common.vo;

import java.sql.Timestamp;

/**
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-16 下午02:46:51
 * @description 
 */

public class LfMaterialVo implements java.io.Serializable
{
/**
	 * 
	 */
	private static final long serialVersionUID = -9027893278233284012L;

private Integer mtalId;
	
	private String mtalName;
	
	private String mtalType;
	
	private String mtalSize;
	
	private Long userId;
	
	private Timestamp mtalUptime;
	
	private String comments;
	
	private Integer sortId;
	
	private String mtalAddress;
	
	private Integer mtalWidth;
	
	private Integer mtalHeight;
	
	private String userName;
	
	private Integer userState;
	
	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}
	
	public LfMaterialVo()
	{
		mtalUptime = new Timestamp(System.currentTimeMillis());
	}

	public Integer getMtalId()
	{
		return mtalId;
	}

	public void setMtalId(Integer mtalId)
	{
		this.mtalId = mtalId;
	}

	public String getMtalName()
	{
		return mtalName;
	}

	public void setMtalName(String mtalName)
	{
		this.mtalName = mtalName;
	}

	 

	public String getMtalType() {
		return mtalType;
	}

	public void setMtalType(String mtalType) {
		this.mtalType = mtalType;
	}

	 
	public String getMtalSize()
	{
		return mtalSize;
	}

	public void setMtalSize(String mtalSize)
	{
		this.mtalSize = mtalSize;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Timestamp getMtalUptime()
	{
		return mtalUptime;
	}

	public void setMtalUptime(Timestamp mtalUptime)
	{
		this.mtalUptime = mtalUptime;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public Integer getSortId()
	{
		return sortId;
	}

	public void setSortId(Integer sortId)
	{
		this.sortId = sortId;
	}

	public String getMtalAddress()
	{
		return mtalAddress;
	}

	public void setMtalAddress(String mtalAddress)
	{
		this.mtalAddress = mtalAddress;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public Integer getMtalWidth()
	{
		return mtalWidth;
	}

	public void setMtalWidth(Integer mtalWidth)
	{
		this.mtalWidth = mtalWidth;
	}

	public Integer getMtalHeight()
	{
		return mtalHeight;
	}

	public void setMtalHeight(Integer mtalHeight)
	{
		this.mtalHeight = mtalHeight;
	}

 
 
}
