package com.montnets.emp.rms.report.vo;

/**
 * 容量套餐统计报表实体类
 * 
 * @project p_rltc
 * @author lvxin
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2018-1-13
 * @description
 */
public class LfRmsReportVo implements java.io.Serializable {

private static final long serialVersionUID = -3061571851898922012L;
	//提交总数
	private Long icount;
	//接收成功数
	private Long rsucc;
	//企业编码
	private String corpCode;
	//档位
	private Integer degree;
	//失败数
	private Long rfail;
    //年
	private String y;
    //月
	private String imonth;
    //发送账号
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
	//是否详情
	private Boolean isDes;
	
	public Boolean getIsDes() {
		return isDes;
	}

	public void setIsDes(Boolean isDes) {
		this.isDes = isDes;
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

	

	public Long getRfail() {
		return rfail;
	}

	public void setRfail(Long rfail) {
		this.rfail = rfail;
	}

	public Integer getDegree() {
		return degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}


	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
