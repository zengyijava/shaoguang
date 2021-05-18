package com.montnets.emp.report.vo;

/**
 * 报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:49:33
 * @description
 */
public class MtDataReportVo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3201282192917120629L;
	
	
	private String spUserId;//发送账号
	
	private Long userId;//操作员ID，如果为空的话
	
	private Long depId;//机构ID
	
	private String userName;//操作员名称,如果改值为空，则显示"未知操作员"
	
	private String depName;//机构名称，如果该值为空，则显示"未知机构"
	
	private Long icount;//提交总数
	
	private Long rsucc;//接收成功数
	
	private Long rfail1;//发送失败数
	
	private Long rfail2;//接收失败数
	
	private Long rnret;//未返数
	
	private String sendTime;//开始时间
	
	private String endTime;//结束时间 
	
	private Integer userState;//操作员状态
	
	private String secretId;
	
	//账户类型 0短信 1彩信
	private Integer mstype;
	//数据源类型
	private Integer datasourcetype;
	//运营商类型  0国内 1国外 -1全部
	private Integer spnumtype;
	
	public MtDataReportVo(){}
	
	public Long getRfail1()
	{
		return rfail1;
	}

	public void setRfail1(Long rfail1)
	{
		this.rfail1 = rfail1;
	}



	public Long getRnret()
	{
		return rnret;
	}



	public void setRnret(Long rnret)
	{
		this.rnret = rnret;
	}



	public Integer getSpnumtype()
	{
		return spnumtype;
	}

	public void setSpnumtype(Integer spnumtype)
	{
		this.spnumtype = spnumtype;
	}

	public Integer getDatasourcetype()
	{
		return datasourcetype;
	}
	public void setDatasourcetype(Integer datasourcetype)
	{
		this.datasourcetype = datasourcetype;
	}

	public Integer getMstype() {
		return mstype;
	}


	public void setMstype(Integer mstype) {
		this.mstype = mstype;
	}


	public String getSpUserId() {
		return spUserId;
	}

	public void setSpUserId(String spUserId) {
		this.spUserId = spUserId;
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

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public Long getRsucc()
	{
		return rsucc;
	}

	public void setRsucc(Long rsucc)
	{
		this.rsucc = rsucc;
	}

	public Long getRfail2() {
		return rfail2;
	}

	public void setRfail2(Long rfail2) {
		this.rfail2 = rfail2;
	}

	public Long getIcount() {
		return icount;
	}

	public void setIcount(Long icount) {
		this.icount = icount;
	}
 
	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}
	
}
