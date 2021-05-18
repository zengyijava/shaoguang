/**
 * 
 */
package com.montnets.emp.entity.tailnumber;

import java.sql.Timestamp;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-2 下午06:16:39
 * @description
 */

public class LfSubnoAllot implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8424416800635352568L;
	//自增id
	private Long suId;
	//模块编号(暂不使用)
	private String menuCode;
	//操作员登录名
	private String loginId;
	//发送账号
	private String spUser;
	//子号
	private String subno;
	//分配类型(0-固定 1-自动)
	private Integer allotType;
	//起始扩展子号位
	private String extendSubnoBegin;
	//结束扩展子号位
	private String extendSubnoEnd;
	//当前正在使用的扩展子号
	private String usedExtendSubno;
	//运营商
	private String spNumber;
	//更新时间
	private Timestamp updateTime;
	//创建时间
	private Timestamp createTime;
	//路由ID
	private Long routeId;
	//业务编码(暂不使用)
	private String busCode;
	//机构ID
	private Long depId;
	//分配类型 1为机构分配子号，2为操作员分配子号
	private Integer shareType;
	//编码
	private String codes;
	//编码类型（0-模块编码；1-业务编码；2-产品编码）
	private Integer codeType;
	//企业编码
	private String corpCode;
	//任务Id
	private Long taskId;
	//有效期，单位小时，默认24*7
	private Long validity;
	//1有效，2无效(尾号有效性标示字段)
	private Integer isValid;
	


	public LfSubnoAllot()
	{
	}
	
	//1有效，2无效(尾号有效性标示字段)
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public String getExtendSubnoBegin() {
		return extendSubnoBegin;
	}

	public void setExtendSubnoBegin(String extendSubnoBegin) {
		this.extendSubnoBegin = extendSubnoBegin;
	}

	public String getExtendSubnoEnd() {
		return extendSubnoEnd;
	}

	public void setExtendSubnoEnd(String extendSubnoEnd) {
		this.extendSubnoEnd = extendSubnoEnd;
	}

	public String getUsedExtendSubno() {
		return usedExtendSubno;
	}

	public void setUsedExtendSubno(String usedExtendSubno) {
		this.usedExtendSubno = usedExtendSubno;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	
	//有效期，单位小时，默认24*7
	public Long getValidity() {
		return validity;
	}

	public void setValidity(Long validity) {
		this.validity = validity;
	}
	
	//自增id
	public Long getSuId()
	{
		return suId;
	}

	public void setSuId(Long suId)
	{
		this.suId = suId;
	}

	//模块编号
	public String getMenuCode()
	{
		return menuCode;
	}

	public void setMenuCode(String menuCode)
	{
		this.menuCode = menuCode;
	}

	
	//操作员登录名
	public String getLoginId()
	{
		return loginId;
	}

	public void setLoginId(String loginId)
	{
		this.loginId = loginId;
	}

	
	//发送账号
	public String getSpUser()
	{
		return spUser;
	}

	public void setSpUser(String spUser)
	{
		this.spUser = spUser;
	}

	//子号
	public String getSubno()
	{
		return subno;
	}

	public void setSubno(String subno)
	{
		this.subno = subno;
	}

	//分配类型(0-固定 1-自动)
	public Integer getAllotType()
	{
		return allotType;
	}

	public void setAllotType(Integer allotType)
	{
		this.allotType = allotType;
	}

	//更新时间
	public Timestamp getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}

	//创建时间
	public Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}

	//运营商
	public String getSpNumber()
	{
		return spNumber;
	}

	public void setSpNumber(String spNumber)
	{
		this.spNumber = spNumber;
	}

	//路由ID
	public Long getRouteId()
	{
		return routeId;
	}

	public void setRouteId(Long routeId)
	{
		this.routeId = routeId;
	}

	
	//业务编码
	public String getBusCode()
	{
		return busCode;
	}

	public void setBusCode(String busCode)
	{
		this.busCode = busCode;
	}

	//机构ID
	public Long getDepId()
	{
		return depId;
	}

	public void setDepId(Long depId)
	{
		this.depId = depId;
	}

	
	//分配类型 1为机构分配子号，2为操作员分配子号
	public Integer getShareType()
	{
		return shareType;
	}

	public void setShareType(Integer shareType)
	{
		this.shareType = shareType;
	}

	//编码
	public String getCodes()
	{
		return codes;
	}

	public void setCodes(String codes)
	{
		this.codes = codes;
	}

	//编码类型（0-模块编码；1-业务编码；2-产品编码）
	public Integer getCodeType()
	{
		return codeType;
	}

	public void setCodeType(Integer codeType)
	{
		this.codeType = codeType;
	}

	
	//企业编码
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getCorpCode() {
		return corpCode;
	}

	
	
}
