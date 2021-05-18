					
package com.montnets.emp.entity.system;

import java.sql.Timestamp;

public class LfSysmttask
{

	private String mobileurl;
	//群发类型（相同1，不同2，动模3）
	private Integer bmttype;

	private Long userid;

	private Timestamp submittime;

	//发送类型（1-文件发送，2-单发）
	private Integer sendtype;

	private String spuser;

	private String corpcode;

	private Integer faicount;

	private Integer subcount;

	private String phone;

	private Integer icount;

	private String msg;

	//信息类型（1-审批提醒；2-上行业务回复；3-找回密码；4-登录动态口令；5-机构余额阀值提醒）
	private Integer mstype;

	private Long id;

	private Integer succount;

	private String title;
	//发送状态。0是未提交网关，1提交网关成功,2提交网关失败,3网关处理完成
	private Integer sendState;
	//任务Id
    private Long taskId;
	//业务编码
    private String busCode;
	//错误编码
	private String errorCodes;
	//传给网关的用户编码
	private String userCode;

	public LfSysmttask(){
	} 
	
	public Integer getSendState()
	{
		return sendState;
	}

	public void setSendState(Integer sendState)
	{
		this.sendState = sendState;
	}

	public Long getTaskId()
	{
		return taskId;
	}

	public void setTaskId(Long taskId)
	{
		this.taskId = taskId;
	}

	public String getBusCode()
	{
		return busCode;
	}

	public void setBusCode(String busCode)
	{
		this.busCode = busCode;
	}

	public String getErrorCodes()
	{
		return errorCodes;
	}

	public void setErrorCodes(String errorCodes)
	{
		this.errorCodes = errorCodes;
	}

	public String getUserCode()
	{
		return userCode;
	}

	public void setUserCode(String userCode)
	{
		this.userCode = userCode;
	}

	public String getMobileurl(){

		return mobileurl;
	}

	public void setMobileurl(String mobileurl){

		this.mobileurl= mobileurl;

	}

	public Integer getBmttype(){

		return bmttype;
	}

	public void setBmttype(Integer bmttype){

		this.bmttype= bmttype;

	}

	public Long getUserid(){

		return userid;
	}

	public void setUserid(Long userid){

		this.userid= userid;

	}

	public Timestamp getSubmittime(){

		return submittime;
	}

	public void setSubmittime(Timestamp submittime){

		this.submittime= submittime;

	}

	public Integer getSendtype(){

		return sendtype;
	}

	public void setSendtype(Integer sendtype){

		this.sendtype= sendtype;

	}

	public String getSpuser(){

		return spuser;
	}

	public void setSpuser(String spuser){

		this.spuser= spuser;

	}

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

	}

	public Integer getFaicount(){

		return faicount;
	}

	public void setFaicount(Integer faicount){

		this.faicount= faicount;

	}

	public Integer getSubcount(){

		return subcount;
	}

	public void setSubcount(Integer subcount){

		this.subcount= subcount;

	}

	public String getPhone(){

		return phone;
	}

	public void setPhone(String phone){

		this.phone= phone;

	}

	public Integer getIcount(){

		return icount;
	}

	public void setIcount(Integer icount){

		this.icount= icount;

	}

	public String getMsg(){

		return msg;
	}

	public void setMsg(String msg){

		this.msg= msg;

	}

	public Integer getMstype(){

		return mstype;
	}

	public void setMstype(Integer mstype){

		this.mstype= mstype;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Integer getSuccount(){

		return succount;
	}

	public void setSuccount(Integer succount){

		this.succount= succount;

	}

	public String getTitle(){

		return title;
	}

	public void setTitle(String title){

		this.title= title;

	}

}

					