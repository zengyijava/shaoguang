package com.montnets.emp.report.vo;

/**
 * 
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:50:14
 * @description
 */
public class OperatorsAreaMtDataReportVo implements java.io.Serializable
{
	private static final long serialVersionUID = 7857327174754855656L;
    //年
	private String y;
    //月
	private String imonth;
	
	private Long icount;//提交总数
	
	private Long rsucc;//接收成功数
	
	private Long rfail1;//发送失败数
	
	private Long rfail2;//接收失败数
	
	private Long rnret;//未返数
	
	//失败数
	private Long rfail;
    //发送sp账号
	private String spID;
	//运营商类型
	private Long spisuncm;
	
	//账户类型 0短信 1彩信
	private Integer mstype;
	//日报表开始时间
	private String startTime;
	//日报表结束时间
	private String endTime;
	//时间
	private String iymd;
	//报表类型
	private Integer reporttype;
	//国家、地区代码
	private String areacode;
	//国家、地区名称
	private String areaname;
	//发送类型
	private Integer sendType;
	//发送sp类型
	private Integer spType;

	//通道号码
	private String spgate;
	//通道名称
	private String gatename;
	
	
	
	public Integer getSpType() {
		return spType;
	}

	public void setSpType(Integer spType) {
		this.spType = spType;
	}
	
	public String getSpgate() {
		return spgate;
	}

	public void setSpgate(String spgate) {
		this.spgate = spgate;
	}

	public String getGatename() {
		return gatename;
	}

	public void setGatename(String gatename) {
		this.gatename = gatename;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public Integer getReporttype() {
		return reporttype;
	}

	public void setReporttype(Integer reporttype) {
		this.reporttype = reporttype;
	}

	public String getIymd() {
		return iymd;
	}

	public void setIymd(String iymd) {
		this.iymd = iymd;
	}

	public Integer getMstype() {
		return mstype;
	}

	public void setMstype(Integer mstype) {
		this.mstype = mstype;
	}


	public Long getSpisuncm() {
		return spisuncm;
	}

	
	public void setSpisuncm(Long spisuncm) {
		this.spisuncm = spisuncm;
	}

	public Long getIcount()
	{
		return icount;
	}

	public void setIcount(Long icount)
	{
		this.icount = icount;
	}

//	public Long getIymd()
//	{
//		return iymd;
//	}
//
//	public void setIymd(Long iymd)
//	{
//		this.iymd = iymd;
//	}

	public String getY()
	{
		return y;
	}

	public void setY(String y)
	{
		this.y = y;
	}

	public String getImonth()
	{
		return imonth;
	}

	public void setImonth(String imonth)
	{
		this.imonth = imonth;
	}

	public String getSpID()
	{
		return spID;
	}

	public void setSpID(String spID)
	{
		this.spID = spID;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getRsucc() {
		return rsucc;
	}

	public void setRsucc(Long rsucc) {
		this.rsucc = rsucc;
	}

	public Long getRfail1() {
		return rfail1;
	}

	public void setRfail1(Long rfail1) {
		this.rfail1 = rfail1;
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

	public Long getRfail() {
		return rfail;
	}

	public void setRfail(Long rfail) {
		this.rfail = rfail;
	}
	
	
//	public String getUserid() {
//		return userid;
//	}
//
//	public void setUserid(String userid) {
//		this.userid = userid;
//	}	
}
