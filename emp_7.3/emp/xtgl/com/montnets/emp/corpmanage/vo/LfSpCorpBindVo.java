package com.montnets.emp.corpmanage.vo;

/**
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-10-31 下午04:02:21
 * @description 
 */

public class LfSpCorpBindVo implements java.io.Serializable
{
	private static final long serialVersionUID = 819354375960662260L;
    //发送账号
	private String spUser;
	//企业编码
	private String corpCode;
	
	private Integer platFormType;
	//企业名称
	private String corpName;
	
	private Integer corpId;
	//1- validate 0-invalidate
	private Integer isValidate;	
	
	public LfSpCorpBindVo(){}

	public Integer getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(Integer isValidate) {
		this.isValidate = isValidate;
	}

	public String getSpUser()
	{
		return spUser;
	}

	public void setSpUser(String spUser)
	{
		this.spUser = spUser;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}

	public Integer getPlatFormType()
	{
		return platFormType;
	}

	public void setPlatFormType(Integer platFormType)
	{
		this.platFormType = platFormType;
	}

	public String getCorpName()
	{
		return corpName;
	}

	public void setCorpName(String corpName)
	{
		this.corpName = corpName;
	}

	public Integer getCorpId()
	{
		return corpId;
	}

	public void setCorpId(Integer corpId)
	{
		this.corpId = corpId;
	}	
}
