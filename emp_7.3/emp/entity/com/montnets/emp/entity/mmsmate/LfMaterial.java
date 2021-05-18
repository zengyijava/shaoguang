/**
 * 
 */
package com.montnets.emp.entity.mmsmate;

import java.sql.Timestamp;

/**
 * 彩信材料实体类
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-15 下午02:31:18
 * @description 彩信材料实体类
 */

public class LfMaterial implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3065510901087299134L;
	//素材ID（自增）
	private Integer mtalId;
	//素材名称
	private String mtalName;
	//素材类型（比如jpg，gif等）
	private String mtalType;
	//素材大小（单位kb)
	private String mtalSize;
	//素材添加者
	private Long userId;
	//素材添加时间
	private Timestamp mtalUptime;
	//备注
	private String comments;
	//素材分类ID
	private Integer sortId;
	//素材存放地址
	private String mtalAddress;
	//素材图片宽度
	private Integer mtalWidth;
	//素材图片高度
	private Integer mtalHeight;
	//企业编码
	private String corpCode;
	
	
	
	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public LfMaterial()
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
