package com.montnets.emp.report.vo;

/**
 * SP帐号统计报表实体类
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:50:55
 * @description
 */
public class SpMtDataDetailVo implements java.io.Serializable{

	private static final long serialVersionUID = -6044320532839571992L;

    //年
	private String y;
    //月
	private String imonth;
	//用户id
	private String userid;

	private Long icount;//提交总数
	
	private Long rsucc;//接收成功数
	
	private Long rfail1;//发送失败数
	
	private Long rfail2;//接收失败数
	
	private Long rnret;//未返数
	//企业编码
	private String corpCode;
	//发送账号
	private String spUsers;	
	//信息类型
	private Integer mstype;
	//帐号类型
	private Integer sptype;
	//账户名称
	private String staffname;
	//发送账号
	private String sptypes;	
	//发送类型
	private Integer sendtype;	
	//发送类型条件
	private String sendtypes;	
	
	private String sendTime;//开始时间
	
	private String endTime;//结束时间 
	
	private String iymd;
	
	
	
	
	
	public String getIymd() {
		return iymd;
	}

	public void setIymd(String iymd) {
		this.iymd = iymd;
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

	public Integer getSendtype()
	{
		return sendtype;
	}

	public void setSendtype(Integer sendtype)
	{
		this.sendtype = sendtype;
	}

	public String getSendtypes()
	{
		return sendtypes;
	}

	public void setSendtypes(String sendtypes)
	{
		this.sendtypes = sendtypes;
	}

	public String getSptypes()
	{
		return sptypes;
	}

	public void setSptypes(String sptypes)
	{
		this.sptypes = sptypes;
	}

	public String getStaffname()
	{
		return staffname;
	}

	public void setStaffname(String staffname)
	{
		this.staffname = staffname;
	}

	public Integer getSptype()
	{
		return sptype;
	}

	public void setSptype(Integer sptype)
	{
		this.sptype = sptype;
	}

	public Integer getMstype() {
		return mstype;
	}

	public void setMstype(Integer mstype) {
		this.mstype = mstype;
	}

	public String getSpUsers() {
		return spUsers;
	}

	public void setSpUsers(String spUsers) {
		this.spUsers = spUsers;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public Long getIcount() {
		return icount;
	}

	public void setIcount(Long icount) {
		this.icount = icount;
	}

//	public Long getIymd() {
//		return iymd;
//	}
//
//	public void setIymd(Long iymd) {
//		this.iymd = iymd;
//	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getImonth() {
		return imonth;
	}

	public void setImonth(String imonth) {
		this.imonth = imonth;
	}

	

	public Long getRsucc()
	{
		return rsucc;
	}

	public void setRsucc(Long rsucc)
	{
		this.rsucc = rsucc;
	}

	public Long getRfail1()
	{
		return rfail1;
	}

	public void setRfail1(Long rfail1)
	{
		this.rfail1 = rfail1;
	}

	public Long getRfail2()
	{
		return rfail2;
	}

	public void setRfail2(Long rfail2)
	{
		this.rfail2 = rfail2;
	}

	public Long getRnret()
	{
		return rnret;
	}

	public void setRnret(Long rnret)
	{
		this.rnret = rnret;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}	
}
