/**
 * 
 */
package com.montnets.emp.entity.wxkeywords;


/**
 * TableLfKeywords对应的实体类
 * 关键字
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 上午10:53:53
 * @description 
 */

public class LfSysKeywords implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3558189489856103495L;
	/**
	 * 
	 */
	//private static final long serialVersionUID = 3558189489856103495L;
	//主键
	private Long kwId; 
	//关键字名称
	private String keyWord; 
	//关键字级别(一级:1，二级:2，三级:3)，暂时没用
	private Integer kwLevel; 
	//状态（1.启用;0.停用）
	private Integer kwState; 
	//备注
	private String comments;  
	//企业编码
	private String corpCode;
	
	public LfSysKeywords(){}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public Long getKwId()
	{
		return kwId;
	}


	public void setKwId(Long kwId)
	{
		this.kwId = kwId;
	}


	public String getKeyWord()
	{
		return keyWord;
	}


	public Integer getKwState()
	{
		return kwState;
	}


	public void setKwState(Integer kwState)
	{
		this.kwState = kwState;
	}


	public void setKeyWord(String keyWord)
	{
		this.keyWord = keyWord;
	}


	public Integer getKwLevel()
	{
		return kwLevel;
	}


	public void setKwLevel(Integer kwLevel)
	{
		this.kwLevel = kwLevel;
	}


	public String getComments()
	{
		return comments;
	}


	public void setComments(String comments)
	{
		this.comments = comments;
	}


}
