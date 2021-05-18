/**
 * 
 */
package com.montnets.emp.entity.pasroute;

import java.sql.Timestamp;

/**
 * 彩信企业账户绑定表
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-10 下午01:40:20
 * @description 
 */

public class LfMmsAccbind implements java.io.Serializable
{
	private static final long serialVersionUID = 7573393314887186904L;
	//标识列ID
	private Long id;
	//彩信账号 
	private String mmsUser;
	//操作员ID
	private Long userId;
	//企业编码
	private String corpCode;
	//是否有效(0失效，1起效)
	private Integer isValidate;
	//创建时间
	private Timestamp createTime;
	//备注
	private String description;
	
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LfMmsAccbind(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMmsUser() {
		return mmsUser;
	}

	public void setMmsUser(String mmsUser) {
		this.mmsUser = mmsUser;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public Integer getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(Integer isValidate) {
		this.isValidate = isValidate;
	}
	
}
