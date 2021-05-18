/**
 * 
 */
package com.montnets.emp.entity.client;

/**
 * @project sinolife
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-16 下午03:16:00
 * @description 
 */

public class LfClientDep implements java.io.Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8488764581564635398L;
	//标识列ID
	private Long depId;
	
	//private String depCode;
	//机构名称
	private String depName;
	
	//private String depPcode;
	//级别
	private Integer depLevel;
	//类别
	private Integer addType;
	//企业编码 
	private String corpCode;
	//父级ID
	private Long parentId;	
	//
	private String depcodethird;	
	//机构级别
	private String deppath;
	
	
	public LfClientDep(){}
	
	public String getDeppath() {
		return deppath;
	}

	public Integer getDepLevel() {
		return depLevel;
	}

	public void setDepLevel(Integer depLevel) {
		this.depLevel = depLevel;
	}

	public void setDeppath(String deppath) {
		this.deppath = deppath;
	}
	
	public Long getDepId()
	{
		return depId;
	}

	public void setDepId(Long depId)
	{
		this.depId = depId;
	}

	public String getDepName()
	{
		return depName;
	}

	public void setDepName(String depName)
	{
		this.depName = depName;
	}

	public Integer getAddType()
	{
		return addType;
	}

	public void setAddType(Integer addType)
	{
		this.addType = addType;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}

	
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public String getDepcodethird() {
		return depcodethird;
	}

	public void setDepcodethird(String depcodethird) {
		this.depcodethird = depcodethird;
	}



	
}
