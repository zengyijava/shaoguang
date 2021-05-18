package com.montnets.emp.entity.sms;

import java.sql.Timestamp;

/**
 * 
 * 
 * @功能概要： 超大文件实体类
 * @项目名称： emp6.9
 * @初创作者：caizuguang <caizuguang@montnets.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2018-11-30 上午11:39:56
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class LfBigFile implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5170375706212219815L;

	/**
	 * 主键ID
	 */
	private Long id;
	
	/**
	 * 文件批次名称 
	 */
	private String fileName;
	/**
	 * 提交总数 
	 */
	private Long subCount;
	
	/**
	 * 有效号码数 
	 */
	private Long effCount;
	/**
	 * 黑名单数量 
	 */
	private Long blaCount;
	/**
	 * 重复号码数 
	 */
	private Long repCount;
	/**
	 * 格式错误号码数 
	 */
	private Long errCount;
	
	/**
	 *1.上传中 2.上传成功(有效号码、黑名单、重复号码、格式错误号码已处理完成) 3.上传失败  
	 */
	private Integer handleStatus;
	/**
	 * 1.启用 2.禁用 
	 */
	private Integer fileStatus;
	/**
	 *  发送过该超大文件的任务号，用逗号分隔 
	 */
	private String taskId;
	/**
	 * 原始上次文件文件个数 
	 */
	private Integer uploadNum;
	/**
	 * 拆分出来的有效号码文件个数 
	 */
	private Integer effNum;
	/**
	 *  按照移动,联通,电信,国际的顺序用英文逗号分隔  
	 */
	private String oprNum;
	/**
	 *  上传WEB节点 
	 */
	private String handleNode;
	/**
	 * 操作员ID 
	 */
	private Long userId;
	/**
	 *  操作员名称 
	 */
	private String userName;
	/**
	 * 业务ID 
	 */
	private Long busId;
	/**
	 *  业务名称 
	 */
	private String busName;
	/**
	 *  业务编码 
	 */
	private String busCode;
	/**
	 * 机构ID 
	 */
	private Long depId;
	/**
	 * 上传时间 
	 */
	private Timestamp createTime; 
	/**
	 * 修改时间 
	 */
	private Timestamp updateTime; 
	/**
	 *  有效拆分文件1
	 */
	private String fileUrl;
	
	/**
	 *  有效拆分文件2
	 */
	private String fileUrl2;
	/**
	 *  有效拆分文件3  
	 */
	private String fileUrl3;
	/**
	 *   有效拆分文件4
	 */
	private String fileUrl4;
	/**
	 *   有效拆分文件5
	 */
	private String fileUrl5;
	/**
	 *   有效拆分文件6
	 */
	private String fileUrl6;
	/**
	 *   有效拆分文件7
	 */
	private String fileUrl7;
	/**
	 *   有效拆分文件8
	 */
	private String fileUrl8;
	/**
	 *   有效拆分文件9
	 */
	private String fileUrl9;
	/**
	 *  预览号码文件路径(只预览10个号码) 
	 */
	private String viewUrl;
	/**
	 *  错误号码文件路径（黑名单、重复号码、格式错误） 
	 */
	private String badUrl;
	/**
	 *  企业编码 
	 */
	private String corpCode; 
	/**
	 *  备注
	 */
	private String remark;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getSubCount() {
		return subCount;
	}
	public void setSubCount(Long subCount) {
		this.subCount = subCount;
	}
	public Long getEffCount() {
		return effCount;
	}
	public void setEffCount(Long effCount) {
		this.effCount = effCount;
	}
	public Long getBlaCount() {
		return blaCount;
	}
	public void setBlaCount(Long blaCount) {
		this.blaCount = blaCount;
	}
	public Long getRepCount() {
		return repCount;
	}
	public void setRepCount(Long repCount) {
		this.repCount = repCount;
	}
	public Long getErrCount() {
		return errCount;
	}
	public void setErrCount(Long errCount) {
		this.errCount = errCount;
	}
	public Integer getHandleStatus() {
		return handleStatus;
	}
	public void setHandleStatus(Integer handleStatus) {
		this.handleStatus = handleStatus;
	}
	public Integer getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(Integer fileStatus) {
		this.fileStatus = fileStatus;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public Integer getUploadNum() {
		return uploadNum;
	}
	public void setUploadNum(Integer uploadNum) {
		this.uploadNum = uploadNum;
	}
	public Integer getEffNum() {
		return effNum;
	}
	public void setEffNum(Integer effNum) {
		this.effNum = effNum;
	}
	public String getHandleNode() {
		return handleNode;
	}
	public void setHandleNode(String handleNode) {
		this.handleNode = handleNode;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getDepId() {
		return depId;
	}
	public void setDepId(Long depId) {
		this.depId = depId;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	public String getFileUrl2() {
		return fileUrl2;
	}
	public void setFileUrl2(String fileUrl2) {
		this.fileUrl2 = fileUrl2;
	}
	public String getFileUrl3() {
		return fileUrl3;
	}
	public void setFileUrl3(String fileUrl3) {
		this.fileUrl3 = fileUrl3;
	}
	public String getFileUrl4() {
		return fileUrl4;
	}
	public void setFileUrl4(String fileUrl4) {
		this.fileUrl4 = fileUrl4;
	}
	public String getFileUrl5() {
		return fileUrl5;
	}
	public void setFileUrl5(String fileUrl5) {
		this.fileUrl5 = fileUrl5;
	}
	public String getFileUrl6() {
		return fileUrl6;
	}
	public void setFileUrl6(String fileUrl6) {
		this.fileUrl6 = fileUrl6;
	}
	public String getFileUrl7() {
		return fileUrl7;
	}
	public void setFileUrl7(String fileUrl7) {
		this.fileUrl7 = fileUrl7;
	}
	public String getFileUrl8() {
		return fileUrl8;
	}
	public void setFileUrl8(String fileUrl8) {
		this.fileUrl8 = fileUrl8;
	}
	public String getFileUrl9() {
		return fileUrl9;
	}
	public void setFileUrl9(String fileUrl9) {
		this.fileUrl9 = fileUrl9;
	}
	public String getViewUrl() {
		return viewUrl;
	}
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}
	public String getBadUrl() {
		return badUrl;
	}
	public void setBadUrl(String badUrl) {
		this.badUrl = badUrl;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOprNum() {
		return oprNum;
	}
	public void setOprNum(String oprNum) {
		this.oprNum = oprNum;
	}
	public Long getBusId() {
		return busId;
	}
	public void setBusId(Long busId) {
		this.busId = busId;
	}
	public String getBusName() {
		return busName;
	}
	public void setBusName(String busName) {
		this.busName = busName;
	}
	public String getBusCode() {
		return busCode;
	}
	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
	
	
	
	
	
	
	
	
	
	
	
}
