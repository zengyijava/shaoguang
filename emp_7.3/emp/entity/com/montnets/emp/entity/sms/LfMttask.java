package com.montnets.emp.entity.sms;

import java.sql.Timestamp;
import java.util.Date;

/**
 * TableLfMttask对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:23:36
 * @description 下行短信任务
 */
public class LfMttask implements java.io.Serializable {


 	/**
	 * 
	 */
	private static final long serialVersionUID = -5433629112658826778L;
	//标识列
	private Long mtId;
	//操作员ID
	private Long userId;
	//主题
	private String title;
	//短信内容
	private String msg;
	//1-相同内容短信 2－动态模板短信；3－文件内容短信
	private Integer msgType;
	//发送开始时间
	private Timestamp biginTime;
	//发送结束时间
	private Timestamp endTime;
	//优先级(0表示优先处理，1-9表示按优先及别处理，1是最优，9是最后)
	private Integer sendLevel;
	//提交时间
	private Timestamp submitTime;
	//任务说明
	private String taskName;
	//提交状态(创建中1，提交2，撤销3,冻结4)
	private Integer subState;
	//审批状态(无需审批:0，未审批:-1，同意:1，拒绝:2)
	private Integer reState;
	//0是未发送，1是已发送到网关,2发送失败,3网关处理完成,  4发送中,5超时未发送
	private Integer sendstate;
	//已达到审批级数(从1级开始，一共5级，1级：已审批了1级，2级：已审批了1级和2级，如此类推，一直到5级)
	private Long reLevel;
	//提交数
	private Long subCount;
	//有效号码总数
	private Long effCount;
	//成功发送总数
	private String sucCount;
	//失败总数
	private String faiCount;
	//群发类型（相同1，不同2，动模3）
	//如果是彩信的话，10普通彩信,11静态模板彩信,12动态模板彩信
	private Integer bmtType;
	//内容
	private String content;
	//号码文件地址
	private String mobileUrl;
	//（文件上传1或手工输入0）
	private Integer mobileType;
	//内容类型-0直接输入，1用txt上传
	private Integer txtType;
	//备注
	private String comments;
	//SP账号
	private String spUser;
	//SP账号密码
	private String spPwd;
	//预发送条数
	private String icount;
	//信息类型 1-短信， 2-彩信，3为短信模板，4为彩信模板，5-移动财务 6-网讯 7-智能引擎下行业务 8-APP短信 9-员工生日祝福  10-客户生日祝福
    private Integer msType;
    //短信定时发送时间
    private Timestamp timerTime;
    //是否定时发送  1-是 0-否
    private Integer timerStatus;
    //业务编码
    private String busCode;
    //用来存放参数
    private String params;
    //企业编码
	private String corpCode;
	//错误编码
	private String errorCodes;
	//是否回复，1：操作员固定尾号，2：回复本次任务
	private Integer isReply;
	//通道号
	private String spNumber;
	//尾号
	private String subNo;
	//是否重发  1-已重发 0-未重发
    private Integer isRetry ;
    //真正失败数
    private Long rfail2; 
    //未反数
    private Long rnret;  
    //网关发送总数
    private String icount2;
    //任务Id
    private Long taskId;
    //模板ID
    private Long tempid;
    //模板参数个数
    private Integer paramcount;
    //模板路径
    private String tmplPath;
    
    //批量任务ID  taskType为1时，batchID就是taskid;taskType为2时，batchID就是网关batch_mt_req表的batchID。
    private Long batchID;
    
    //taskType发送类型   1-EMP界面发送  2-接口发送  taskType+batchID加了唯一约束。
    private Integer taskType;
    
    //网优发送信息，值格式为：提交信息数/发送成功数 /提交失败数/接收失败数
    private String wySendInfo;
    
    //发送文件URL
    private String fileuri;
    
    //内容编码类型 15：普通，未编码；35：base64编码
    private Integer msgedcodetype = 15;
    
    //内容时效性 ，单位：小时，默认 48小时
    
    private  Integer validtm = 48;
	//11富信12卡片13富文本
	private Integer tempType = 0;

	/**
	 * 	本次任务的手机号数量(即需要下发的数量),以后新增的值都给-1
	 */
	private Integer phoneNum = 0;
	/**
	 * 	已下发的短信数
	 */
	private Integer sendNum = 0;

	/**
	 * 下发完成时间
	 */
	private Timestamp finishTime = new Timestamp(System.currentTimeMillis());

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(Integer phoneNum) {
		this.phoneNum = phoneNum;
	}

	public Integer getSendNum() {
		return sendNum;
	}

	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
	}

	public String getWySendInfo()
	{
		return wySendInfo;
	}

	public void setWySendInfo(String wySendInfo)
	{
		this.wySendInfo = wySendInfo;
	}

	public Long getBatchID()
	{
		return batchID;
	}

	public void setBatchID(Long batchID)
	{
		this.batchID = batchID;
	}

	public Integer getTaskType()
	{
		return taskType;
	}

	public void setTaskType(Integer taskType)
	{
		this.taskType = taskType;
	}

	public String getTmplPath()
	{
		return tmplPath;
	}

	public void setTmplPath(String tmplPath)
	{
		this.tmplPath = tmplPath;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}



	public String getIcount2() {
		return icount2;
	}



	public void setIcount2(String icount2) {
		this.icount2 = icount2;
	}



	public LfMttask() 
	{
		submitTime = new Timestamp(System.currentTimeMillis());
		taskType=1;
		batchID=0L;
	}



	public Long getMtId() {
		return mtId;
	}
	public void setMtId(Long mtId) {
		this.mtId = mtId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	public Timestamp getBiginTime() {
		return biginTime;
	}
	public void setBiginTime(Timestamp biginTime) {
		this.biginTime = biginTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public Integer getSendLevel() {
		return sendLevel;
	}
	public void setSendLevel(Integer sendLevel) {
		this.sendLevel = sendLevel;
	}
	public Timestamp getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Timestamp submitTime) {
		this.submitTime = submitTime;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Integer getSubState() {
		return subState;
	}
	public void setSubState(Integer subState) {
		this.subState = subState;
	}
	public Integer getReState() {
		return reState;
	}
	public void setReState(Integer reState) {
		this.reState = reState;
	}
	public Integer getSendstate() {
		return sendstate;
	}
	public void setSendstate(Integer sendstate) {
		this.sendstate = sendstate;
	}
	public Long getReLevel() {
		return reLevel;
	}
	public void setReLevel(Long reLevel) {
		this.reLevel = reLevel;
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
	public String getSucCount() {
		return sucCount;
	}
	public void setSucCount(String sucCount) {
		this.sucCount = sucCount;
	}
	public String getFaiCount() {
		return faiCount;
	}
	public void setFaiCount(String faiCount) {
		this.faiCount = faiCount;
	}
	public Integer getBmtType() {
		return bmtType;
	}
	public void setBmtType(Integer bmtType) {
		this.bmtType = bmtType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMobileUrl() {
		return mobileUrl;
	}
	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}
	public Integer getMobileType() {
		return mobileType;
	}
	public void setMobileType(Integer mobileType) {
		this.mobileType = mobileType;
	}
	public Integer getTxtType() {
		return txtType;
	}
	public void setTxtType(Integer txtType) {
		this.txtType = txtType;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getSpUser() {
		return spUser;
	}
	public void setSpUser(String spUser) {
		this.spUser = spUser;
	}
	public String getSpPwd() {
		return spPwd;
	}
	public void setSpPwd(String spPwd) {
		this.spPwd = spPwd;
	}
	public String getIcount() {
		return icount;
	}
	public void setIcount(String icount) {
		this.icount = icount;
	}
	public Integer getMsType() {
		return msType;
	}
	public void setMsType(Integer msType) {
		this.msType = msType;
	}
	public Timestamp getTimerTime() {
		return timerTime;
	}
	public void setTimerTime(Timestamp timerTime) {
		this.timerTime = timerTime;
	}
	public Integer getTimerStatus() {
		return timerStatus;
	}
	public void setTimerStatus(Integer timerStatus) {
		this.timerStatus = timerStatus;
	}
	public String getBusCode() {
		return busCode;
	}
	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getErrorCodes() {
		return errorCodes;
	}
	public void setErrorCodes(String errorCodes) {
		this.errorCodes = errorCodes;
	}
	public Integer getIsReply() {
		return isReply;
	}
	public void setIsReply(Integer isReply) {
		this.isReply = isReply;
	}
	public String getSpNumber() {
		return spNumber;
	}
	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}
	public String getSubNo() {
		return subNo;
	}
	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}
	public Integer getIsRetry() {
		return isRetry;
	}
	public void setIsRetry(Integer isRetry) {
		this.isRetry = isRetry;
	}

	public Long getRfail2() {
		return rfail2;
	}
	public void setRfail2(Long rfail2) {
		this.rfail2 = rfail2;
	}
	public Long getRnret() {
		return rnret;
	}
	public void setRnret(Long rnret) {
		this.rnret = rnret;
	}

	public Long getTempid() {
		return tempid;
	}

	public void setTempid(Long tempid) {
		this.tempid = tempid;
	}

	public Integer getParamcount() {
		return paramcount;
	}

	public void setParamcount(Integer paramcount) {
		this.paramcount = paramcount;
	}

	public String getFileuri()
	{
		return fileuri;
	}

	public void setFileuri(String fileuri)
	{
		this.fileuri = fileuri;
	}

	public Integer getMsgedcodetype()
	{
		return msgedcodetype;
	}

	public void setMsgedcodetype(Integer msgedcodetype)
	{
		this.msgedcodetype = msgedcodetype;
	}

	public Integer getValidtm() {
		return validtm;
	}

	public void setValidtm(Integer validtm) {
		this.validtm = validtm;
	}


	public Integer getTempType() {
		return tempType;
	}

	public void setTempType(Integer tempType) {
		this.tempType = tempType;
	}
}