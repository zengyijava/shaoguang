package com.montnets.emp.entity.report;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * TableMtDatareport对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:28:15
 * @description 
 */
public class MtDatareport implements Serializable{

 	/**
	 * 
	 */
	private static final long serialVersionUID = 4780180968578312984L;

	private String userId;
 	
	private Long taskId;
	
	private String spgate;
	
	private Long iymd;
	
	private Long ihour;
	
	//private String ptCode;
	private Long imonth;
	
	private Long icount;
	
	/*private Long succ;
	private Long fail1;
	private Long fail2;
	private Long fail3;
	private Long nret;*/
	private Long rsucc;
	
	private Long rfail1;
	
	private Long rfail2;
	
	//接收失败数
	private Long recfail;
	
	private Long rnret;
	
	private Timestamp startTime;
	
	private Timestamp endTime;
	
	private Long id;
	
	private Long y;
	
	private Long spisuncm;
	
	private String spID;
	
	private String svrType;
	
	private String p1;
	
	private String p2;
	
	private String p3;
	
	private String p4;
	
	//批次ID
	private Long batchID;

	private Integer mobileArea;

	private Integer areaCode;

	public Long getBatchID()
	{
		return batchID;
	}

	public void setBatchID(Long batchID)
	{
		this.batchID = batchID;
	}

	public MtDatareport(){}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public Long getTaskId()
	{
		return taskId;
	}

	public void setTaskId(Long taskId)
	{
		this.taskId = taskId;
	}

	public String getSpgate()
	{
		return spgate;
	}

	public void setSpgate(String spgate)
	{
		this.spgate = spgate;
	}

	public Long getIymd()
	{
		return iymd;
	}

	public void setIymd(Long iymd)
	{
		this.iymd = iymd;
	}

	public Long getIhour()
	{
		return ihour;
	}

	public void setIhour(Long ihour)
	{
		this.ihour = ihour;
	}

	public Long getImonth()
	{
		return imonth;
	}

	public void setImonth(Long imonth)
	{
		this.imonth = imonth;
	}

	public Long getIcount()
	{
		return icount;
	}

	public void setIcount(Long icount)
	{
		this.icount = icount;
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

	public Timestamp getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Timestamp startTime)
	{
		this.startTime = startTime;
	}

	public Timestamp getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Timestamp endTime)
	{
		this.endTime = endTime;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getY()
	{
		return y;
	}

	public void setY(Long y)
	{
		this.y = y;
	}

	public Long getSpisuncm()
	{
		return spisuncm;
	}

	public void setSpisuncm(Long spisuncm)
	{
		this.spisuncm = spisuncm;
	}

	public String getSpID()
	{
		return spID;
	}

	public void setSpID(String spID)
	{
		this.spID = spID;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public String getP3() {
		return p3;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}

	public String getP4() {
		return p4;
	}

	public void setP4(String p4) {
		this.p4 = p4;
	}

	public String getSvrType() {
		return svrType;
	}

	public void setSvrType(String svrType) {
		this.svrType = svrType;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public Long getRecfail() {
		return recfail;
	}

	public void setRecfail(Long recfail) {
		this.recfail = recfail;
	}

	public Integer getMobileArea() {
		return mobileArea;
	}

	public void setMobileArea(Integer mobileArea) {
		this.mobileArea = mobileArea;
	}

	public Integer getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(Integer areaCode) {
		this.areaCode = areaCode;
	}
}
