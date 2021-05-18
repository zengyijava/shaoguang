/**
 * 
 */
package com.montnets.emp.entity.mmsmate;

/**
 * 
 * 彩信素材分类实体类
 * 
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-15 下午02:44:08
 * @description 彩信素材分类实体类
 */

public class LfMaterialSort implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6952865445690757715L;
	//素材分类ID（自增） 
	private Integer sortId;
	//分类名称
	private String sortName;
	//上一级素材分类编码
	private String parentCode;
	//素材分类编码
	private String childCode;
	//分类级别
	private Integer sortLevel;
	//企业编码
	private String corpCode;
	
	public LfMaterialSort(){}

	public Integer getSortId()
	{
		return sortId;
	}

	public void setSortId(Integer sortId)
	{
		this.sortId = sortId;
	}

	public String getSortName()
	{
		return sortName;
	}

	public void setSortName(String sortName)
	{
		this.sortName = sortName;
	}

 
	public String getParentCode()
	{
		return parentCode;
	}

	public void setParentCode(String parentCode)
	{
		this.parentCode = parentCode;
	}

	public String getChildCode()
	{
		return childCode;
	}

	public void setChildCode(String childCode)
	{
		this.childCode = childCode;
	}

	public Integer getSortLevel()
	{
		return sortLevel;
	}

	public void setSortLevel(Integer sortLevel)
	{
		this.sortLevel = sortLevel;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getCorpCode() {
		return corpCode;
	}

	 
	
	
}
