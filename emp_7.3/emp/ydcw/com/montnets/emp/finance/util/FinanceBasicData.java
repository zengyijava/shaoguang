package com.montnets.emp.finance.util;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;

import com.montnets.emp.finance.biz.ElecPayrollCommon;

/**
 *  业务基础数据VO
 * @author Jinny.Ding (djhsun@sina.com)
 * @version 1.0
 * @company ShenZhen Montnets Technology CO.,LTD. 
 * @date May 14, 2012
 */
public class FinanceBasicData {
	private ElecPayrollCommon epcObject;
	private String isCheckTime;
	private FileItem fileItem;
	private List<String> list;
	//模板
	private String template;
	//SP账号
	private	String spAccount;
	//路径
	private String url;
	private String determineTime;
	//企业编码
	private String corpCode;
	//验证码
	private String verifycode;
	//操作员id
	private String userId;
	//业务类型
	private String busCode;
	//短信标题
	private String smsTitle;
	private HttpSession session;
	//模板id
	private String templateIds;
	//1:txt, 2:excel, 3:手工入录
	private int textOrExcel;
	private String tempFileName;
	//文件路径
	private String filePath;
	//是否需要回复
	private Integer isReply;
	//尾号
	private String subNo;
	//任务id
	private Long taskId;
	public ElecPayrollCommon getEpcObject() {
		return epcObject;
	}
	public String getIsCheckTime() {
		return isCheckTime;
	}
	public FileItem getFileItem() {
		return fileItem;
	}
	public String getTemplate() {
		return template;
	}
	public String getSpAccount() {
		return spAccount;
	}
	public String getUrl() {
		return url;
	}
	public String getDetermineTime() {
		return determineTime;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public String getVerifycode() {
		return verifycode;
	}
	public String getUserId() {
		return userId;
	}
	public String getBusCode() {
		return busCode;
	}
	public String getSmsTitle() {
		return smsTitle;
	}
	public HttpSession getSession() {
		return session;
	}
	public String getTemplateIds() {
		return templateIds;
	}
	public int getTextOrExcel() {
		return textOrExcel;
	}
	public String getTempFileName() {
		return tempFileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public List<String> getList() {
		return list;
	}
	public void setEpcObject(ElecPayrollCommon epcObject) {
		this.epcObject = epcObject;
	}
	public void setIsCheckTime(String isCheckTime) {
		this.isCheckTime = isCheckTime;
	}
	public void setFileItem(FileItem fileItem) {
		this.fileItem = fileItem;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public void setSpAccount(String spAccount) {
		this.spAccount = spAccount;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setDetermineTime(String determineTime) {
		this.determineTime = determineTime;
	}
	//企业编码
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}
	//用户id
	public void setUserId(String userId) {
		this.userId = userId;
	}
	//业务编码
	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
	public void setSmsTitle(String smsTitle) {
		this.smsTitle = smsTitle;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	public void setTemplateIds(String templateIds) {
		this.templateIds = templateIds;
	} 
	public void setTextOrExcel(int textOrExcel) {
		this.textOrExcel = textOrExcel;
	}
	public void setTempFileName(String tempFileName) {
		this.tempFileName = tempFileName;
	}
	//文件路径 
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	//是否需要回复
	public Integer getIsReply() {
		return isReply;
	}
	public void setIsReply(Integer isReply) {
		this.isReply = isReply;
	}
	//尾号
	public String getSubNo() {
		return subNo;
	}
	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}
	//任务id
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}	
}
