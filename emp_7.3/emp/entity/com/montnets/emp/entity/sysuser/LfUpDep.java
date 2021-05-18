package com.montnets.emp.entity.sysuser;

import java.util.Date;

public class LfUpDep  implements java.io.Serializable
{
	/* 机构代码 */
	private Long depCode;
	/* 机构名称 */
	private String depName;
	/* 机构简称 */
	private String depShortName;
	/* 机构级次 */
	private String depLevel;
	/* 上级机构代码 */
	private Long depPcode;
	/* 对应外部机构编码 */
	private String depCodeThird;
	/* 企业代码 */
	private String corpCode;

	/* 创建时间 */
	private Date createDate;
	/* 更新时间 */
	private Date updateDate;
	
	/* 操作类型 */
	private String  optType;
	/* 是否修改 */
	private String  isUpdate;
	/*自增ID */
	private Long  udId;
	
	
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public String getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
	public Long getUdId() {
		return udId;
	}
	public void setUdId(Long udId) {
		this.udId = udId;
	}

	
	public Long getDepCode()
	{
		return depCode;
	}
	public void setDepCode(Long depCode)
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
	public String getDepShortName()
	{
		return depShortName;
	}
	public void setDepShortName(String depShortName)
	{
		this.depShortName = depShortName;
	}
	public String getDepLevel()
	{
		return depLevel;
	}
	public void setDepLevel(String depLevel)
	{
		this.depLevel = depLevel;
	}
	public Long getDepPcode()
	{
		return depPcode;
	}
	public void setDepPcode(Long depPcode)
	{
		this.depPcode = depPcode;
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
	public Date getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}
	public Date getUpdateDate()
	{
		return updateDate;
	}
	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}
	
}
