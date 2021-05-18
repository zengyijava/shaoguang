/**
 * 
 */
package com.montnets.emp.entity.sysuser;


 

/**
 * TableLfDep对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-21 下午02:31:18
 * @description 
 */

public class LfDep implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8722522701326358403L;
	/**
	 * 
	 */
	//private static final long serialVersionUID = -7196249578242535734L;
	//机构ID
	private Long depId;		
	//机构名称
	private String depName;	
	//机构联系人
	private String depContact;	
	//机构级别
	private Integer depLevel;	
	//机构编码
	//private String depCode;		
	//机构描述
	private String depResp;		
	//父级ID
	private Long superiorId;	
	//与机构表管理关联的机构编码
	private String depCodeThird;
	//部门负责人
	private Long depDirect;
	//企业编码
	private String corpCode;
	// 1正常 2删除，默认为1
	private Integer depState;
	private String deppath;
	
	//up_depId VARCHAR (64) COLLATE 统一平台机构标识ID
	private String upDepId;

	
	
	public String getUpDepId() {
		return upDepId;
	}

	public void setUpDepId(String upDepId) {
		this.upDepId = upDepId;
	}

	public String getDeppath() {
		return deppath;
	}

	public void setDeppath(String deppath) {
		this.deppath = deppath;
	}

	public Integer getDepState() {
		return depState;
	}

	public void setDepState(Integer depState) {
		this.depState = depState;
	}

	public LfDep(){}

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

	public String getDepContact()
	{
		return depContact;
	}

	public void setDepContact(String depContact)
	{
		this.depContact = depContact;
	}

	public Integer getDepLevel()
	{
		return depLevel;
	}

	public void setDepLevel(Integer depLevel)
	{
		this.depLevel = depLevel;
	}

//	public String getDepCode()
//	{
//		return depCode;
//	}
//
//	public void setDepCode(String depCode)
//	{
//		this.depCode = depCode;
//	}

	public String getDepResp()
	{
		return depResp;
	}

	public void setDepResp(String depResp)
	{
		this.depResp = depResp;
	}

	public Long getSuperiorId()
	{
		return superiorId;
	}

	public void setSuperiorId(Long superiorId)
	{
		this.superiorId = superiorId;
	}

	public String getDepCodeThird()
	{
		return depCodeThird;
	}

	public void setDepCodeThird(String depCodeThird)
	{
		this.depCodeThird = depCodeThird;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}

	public Long getDepDirect() {
		return depDirect;
	}

	public void setDepDirect(Long depDirect) {
		this.depDirect = depDirect;
	}
	

	
}
