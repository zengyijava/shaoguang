package com.montnets.emp.employee.vo;

/**
 * @project sinolife
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-4 下午12:49:58
 * @description 第三方机构vo
 */
public class LfEnterpriseVo implements java.io.Serializable
{
	/**
	 * 第三方机构vo
	 */
	private static final long serialVersionUID = -1803354359119639658L;
	//部门编码
	private String depCode;
	//部门名称
	private String depName;
	//部门级别
	private Integer depLevel;
	//父及机构编码
	private String parentCode;
	
	public LfEnterpriseVo(){}

	public String getDepCode()
	{
		return depCode;
	}

	public void setDepCode(String depCode)
	{
		this.depCode = depCode;
	}

	public String getDepName()
	{
		return depName;
	}

	public void setDepName(String depName)
	{
		this.depName = depName;
	}

	public Integer getDepLevel()
	{
		return depLevel;
	}

	public void setDepLevel(Integer depLevel)
	{
		this.depLevel = depLevel;
	}

	public String getParentCode()
	{
		return parentCode;
	}

	public void setParentCode(String parentCode)
	{
		this.parentCode = parentCode;
	}
}
