/**
 * 
 */
package com.montnets.emp.entity.tailnumber;

import java.sql.Timestamp;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-2 下午06:26:42
 * @description
 */

public class LfSubnoAllotDetail implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6012827896398130371L;

	private Long sudId;
	// 模块编号(暂不使用)
	private String menuCode;
	// 操作员登录名
	private String loginId;
	// 发送账号
	private String spUser;
	// 主通道号
	private String spgate;
	// 子号
	private String subno;
	// 当前正在使用的扩展子号
	private String usedExtendSubno;
	// 全通道号
	private String spNumber;
	// 分配类型(0-固定 1-自动)
	private Integer allotType;
	// 更新时间
	private Timestamp updateTime;
	// 创建时间
	private Timestamp createTime;
	// 业务编码(暂不使用)
	private String busCode;
	// 编码
	private String codes;
	// 编码类型（0-模块编码；1-业务编码；2-产品编码）
	private Integer codeType;
	// 对应子号规则表中的标识ID
	private Long suId;
	//企业编码
	private String corpCode;
	// 任务Id
	private Long taskId;
	// 有效期，单位小时，默认24*7
	private Long validity;
	//机构ID
	private Long depId;
	//有效性(1有效2无效)
	private Integer isValid;
	
	

	//有效性(1有效2无效)
	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	//机构ID
	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	// 当前正在使用的扩展子号
	public String getUsedExtendSubno() {
		return usedExtendSubno;
	}

	public void setUsedExtendSubno(String usedExtendSubno) {
		this.usedExtendSubno = usedExtendSubno;
	}
	// 任务Id
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	// 有效期，单位小时，默认24*7
	public Long getValidity() {
		return validity;
	}

	public void setValidity(Long validity) {
		this.validity = validity;
	}

	public LfSubnoAllotDetail() {
		this.createTime = new Timestamp(System.currentTimeMillis());
		this.updateTime = new Timestamp(System.currentTimeMillis());
	}
	
	//自增ID
	public Long getSudId() {
		return sudId;
	}

	public void setSudId(Long sudId) {
		this.sudId = sudId;
	}

	// 模块编号
	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	// 操作员登录名
	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	// 发送账号
	public String getSpUser() {
		return spUser;
	}

	public void setSpUser(String spUser) {
		this.spUser = spUser;
	}

	// 主通道号
	public String getSpgate() {
		return spgate;
	}

	public void setSpgate(String spgate) {
		this.spgate = spgate;
	}

	// 子号
	public String getSubno() {
		return subno;
	}

	public void setSubno(String subno) {
		this.subno = subno;
	}

	// 全通道号
	public String getSpNumber() {
		return spNumber;
	}

	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}

	// 分配类型(0-固定 1-自动)
	public Integer getAllotType() {
		return allotType;
	}

	public void setAllotType(Integer allotType) {
		this.allotType = allotType;
	}

	// 更新时间
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	// 创建时间
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	// 业务编码(暂不使用)
	public String getBusCode() {
		return busCode;
	}

	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}

	// 编码
	public String getCodes() {
		return codes;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}
	
	// 编码类型（0-模块编码；1-业务编码；2-产品编码）
	public Integer getCodeType() {
		return codeType;
	}

	public void setCodeType(Integer codeType) {
		this.codeType = codeType;
	}

	// 对应子号规则表中的标识ID
	public Long getSuId() {
		return suId;
	}

	public void setSuId(Long suId) {
		this.suId = suId;
	}

	//企业编码
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getCorpCode() {
		return corpCode;
	}

}
