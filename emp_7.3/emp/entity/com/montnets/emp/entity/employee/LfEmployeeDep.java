/**
 * 
 */
package com.montnets.emp.entity.employee;

/**
 * @project sinolife
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-16 03:59:02
 * @description 
 */

public class LfEmployeeDep implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9178965451703257968L;
	//机构ID
	private Long depId;
	//机构编码
	//private String depCode;
	//机构名称
	private String depName;
	//
	//private String depPcode;
	//
	private String depEffStatus;
	//机构级别
	private Integer depLevel;
	//
	private Integer addType;
	//企业编码
	private String corpCode;
	//父级ID
	private Long parentId;	
	//up_depId VARCHAR (64) COLLATE 统一平台机构标识ID
	private String updepid;
	
	public String getUpdepid() {
		return updepid;
	}

	public void setUpdepid(String updepid) {
		this.updepid = updepid;
	}

	public Integer getDepLevel() {
		return depLevel;
	}

	public void setDepLevel(Integer depLevel) {
		this.depLevel = depLevel;
	}

	private String depcodethird;	
	private String deppath;
	public LfEmployeeDep(){}
	
	public String getDeppath() {
		return deppath;
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

	/*public String getDepCode()
	{
		return depCode;
	}

	public void setDepCode(String depCode)
	{
		this.depCode = depCode;
	}*/

	public String getDepName()
	{
		return depName;
	}

	public void setDepName(String depName)
	{
		this.depName = depName;
	}

	/*public String getDepPcode()
	{
		return depPcode;
	}

	public void setDepPcode(String depPcode)
	{
		this.depPcode = depPcode;
	}*/

	public String getDepEffStatus()
	{
		return depEffStatus;
	}

	public void setDepEffStatus(String depEffStatus)
	{
		this.depEffStatus = depEffStatus;
	}

	/*public Integer getDepLevel()
	{
		return depLevel;
	}

	public void setDepLevel(Integer depLevel)
	{
		this.depLevel = depLevel;
	}*/

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
