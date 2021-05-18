package com.montnets.emp.wyquery.vo;

/**
 * SP帐号统计报表实体类
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:50:55
 * @description
 */
public class WyReportVo implements java.io.Serializable{

	private static final long serialVersionUID = -6044320532839571992L;

    //年
	private String y;
    //月
	private String imonth;
	//年月日
	private String iymd;
	//接收失败数
	private Long rfail2;
	//提交总数
	private Long icount;
	//接受成功数
	private Long rsucc;
	//发送失败数
	private Long rfail1;
	//未返数
	private Long rnret;
	
	//企业编码
	private String corpCode;
	
	private String spgate;
	
	//通道名称
	private String gateName;
	//开始时间
	private String sendtime;
	//结束时间 
	private String endtime;
	
	public String getSendtime()
	{
		return sendtime;
	}

	public void setSendtime(String sendtime)
	{
		this.sendtime = sendtime;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
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

	public String getIymd() {
		return iymd;
	}

	public void setIymd(String iymd) {
		this.iymd = iymd;
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

	public Long getRnret() {
		return rnret;
	}

	public void setRnret(Long rnret) {
		this.rnret = rnret;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public String getGateName()
	{
		return gateName;
	}

	public void setGateName(String gateName)
	{
		this.gateName = gateName;
	}

	public String getSpgate()
	{
		return spgate;
	}

	public void setSpgate(String spgate)
	{
		this.spgate = spgate;
	}
	
	
	
}
