package com.montnets.emp.shorturl.surlmanage.entity;

import java.sql.Timestamp;


/**
 * 
 * 
 * @功能概要：
 * @项目名称： p_customer
 * @初创作者：caizuguang <caizuguang@montnets.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2017-10-9 上午09:05:56
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class LfUrlTask {
	
	/**
	 * 自定id
	 */
	private Long id;
	/**
	 * 任务批次号
	 */
	private Integer taskId;
	/**
	 * sp账号
	 */
	private String spUser;
	/**
	 * 企业编码
	 */
	private String corpCode;
	/**
	 * 发送主题
	 */
	private String title;
	/**
	 * 短信内容
	 */
	private String msg;
	/**
	 * 发送类型
	 */
	private Integer utype;
	/**
	 * 定时时间
	 */
	private Timestamp plantime;
	/**
	 * 提交时间
	 */
	private Timestamp submittm;
	/**
	 * 发送时间
	 */
	private Timestamp sendtm;
	/**
	 * 处理到的行数
	 */
	private Integer handleLine;
	/**
	 * 处理节点
	 */
	private String handleNode;
	/**
	 * 提交个数
	 */
	private Integer subCount;
	/**
	 * 文件本地路径（唯一的）
	 */
	private String srcfile;
	/**
	 * 文件相对路径
	 */
	private String urlfile;
	/**
	 * 处理状态
	 * 0：未处理
	   1：处理中
	   2：处理完成
	   3 : 处理失败
	 */
	private Integer status;
	
	/**
	 * 创建人员ID
	 */
	private Integer createUid;
	/**
	 * 创建时间
	 */
	private Timestamp createtm;
	/**
	 * 修改人员ID
	 */
	private Integer updateUid;
	/**
	 * 修改时间
	 */
	private Timestamp updatetm;
	/**
	 * 企业长地址
	 */
	private String netUrl;
	/**
	 * 冗余审核表ID
	 * 
	 */
	private Long netUrlId;
	
	/**
	 * 域名id
	 * 
	 */
	private Long domainId;
	
	/**
	 * 短地址
	 * 
	 */
	private String domainUrl;
	/**
	 * 短地址扩展部分长度
	 */
	private Integer domainExten;
	/**
	 * 短地址总长度
	 */
	private Integer domainLen;
	/**
	 * 有效期
	 */
	private Integer validDays;
	/**
	 * 扩展字段
	 */
	private String remark;
	/**
	 * 扩展字段2
	 */
	private String remark1;
	/**
	 * strTaskId
	 */
	private String strTaskId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getSpUser() {
		return spUser;
	}
	public void setSpUser(String spUser) {
		this.spUser = spUser;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getUtype() {
		return utype;
	}
	public void setUtype(Integer utype) {
		this.utype = utype;
	}
	public Timestamp getPlantime() {
		return plantime;
	}
	public void setPlantime(Timestamp plantime) {
		this.plantime = plantime;
	}
	public Timestamp getSubmittm() {
		return submittm;
	}
	public void setSubmittm(Timestamp submittm) {
		this.submittm = submittm;
	}
	public Timestamp getSendtm() {
		return sendtm;
	}
	public void setSendtm(Timestamp sendtm) {
		this.sendtm = sendtm;
	}
	public Integer getHandleLine() {
		return handleLine;
	}
	public void setHandleLine(Integer handleLine) {
		this.handleLine = handleLine;
	}
	public String getHandleNode() {
		return handleNode;
	}
	public void setHandleNode(String handleNode) {
		this.handleNode = handleNode;
	}
	public Integer getSubCount() {
		return subCount;
	}
	public void setSubCount(Integer subCount) {
		this.subCount = subCount;
	}
	public String getSrcfile() {
		return srcfile;
	}
	public void setSrcfile(String srcfile) {
		this.srcfile = srcfile;
	}
	public String getUrlfile() {
		return urlfile;
	}
	public void setUrlfile(String urlfile) {
		this.urlfile = urlfile;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCreateUid() {
		return createUid;
	}
	public void setCreateUid(Integer createUid) {
		this.createUid = createUid;
	}
	public Timestamp getCreatetm() {
		return createtm;
	}
	public void setCreatetm(Timestamp createtm) {
		this.createtm = createtm;
	}
	public Integer getUpdateUid() {
		return updateUid;
	}
	public void setUpdateUid(Integer updateUid) {
		this.updateUid = updateUid;
	}
	public Timestamp getUpdatetm() {
		return updatetm;
	}
	public void setUpdatetm(Timestamp updatetm) {
		this.updatetm = updatetm;
	}
	public String getNetUrl() {
		return netUrl;
	}
	public void setNetUrl(String netUrl) {
		this.netUrl = netUrl;
	}
	public Long getNetUrlId() {
		return netUrlId;
	}
	public void setNetUrlId(Long netUrlId) {
		this.netUrlId = netUrlId;
	}
	public Long getDomainId() {
		return domainId;
	}
	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}
	public String getDomainUrl() {
		return domainUrl;
	}
	public void setDomainUrl(String domainUrl) {
		this.domainUrl = domainUrl;
	}
	public Integer getDomainExten() {
		return domainExten;
	}
	public void setDomainExten(Integer domainExten) {
		this.domainExten = domainExten;
	}
	public Integer getDomainLen() {
		return domainLen;
	}
	public void setDomainLen(Integer domainLen) {
		this.domainLen = domainLen;
	}
	public Integer getValidDays() {
		return validDays;
	}
	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		if("".equals(remark)){
			this.remark = "";
		}else{
			this.remark = remark;
		}
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		if("".equals(remark1)){
			this.remark1 = " ";
		}else{
			this.remark1 = remark1;
		}
	}
	public String getStrTaskId() {
		return strTaskId;
	}
	public void setStrTaskId(String strTaskId) {
		this.strTaskId = strTaskId;
	}
	
	

	
	
	
	

}
