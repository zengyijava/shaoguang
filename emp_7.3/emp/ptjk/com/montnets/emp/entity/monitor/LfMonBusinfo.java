package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * 业务监控告警详情表
 * @description 
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-11-23 上午10:29:13  
 */
public class LfMonBusinfo
{
	//自增ID
	private Long id;
	//业务编码
	private String buscode;
	//MT已发（条）
	private Integer mtsendcount;
	//监控开始时间
	private Timestamp begintime;
	//业务名称
	private String busname;
	//区域   多个区域以,隔开
	private String areacode;
	//监控结果时间
	private Timestamp endtime;
	//告警偏离率（告警描述）
	private String mondeviat;
	//告警通知手机号
	private String monphone;
//	//告警通知邮件
//	private String monemail;
	//MT已发告警值
	private Integer mthavesnd;
	//告警级别告警级别0：正常1：警告2：严重
	private Integer evttype;
	//创建时间
	private Timestamp createtime;
	//企业编码
	private String corpcode;
	//开始时间段
	private Integer beginhour;
	//结束时间段
	private Integer endhour;
	//告警说明
	private String mondes; 
	
	
	public LfMonBusinfo(){
	} 
	
	

	public Integer getBeginhour() {
		return beginhour;
	}



	public void setBeginhour(Integer beginhour) {
		this.beginhour = beginhour;
	}



	public Integer getEndhour() {
		return endhour;
	}



	public void setEndhour(Integer endhour) {
		this.endhour = endhour;
	}



	public String getMondes() {
		return mondes;
	}



	public void setMondes(String mondes) {
		this.mondes = mondes;
	}



	public String getBuscode(){

		return buscode;
	}

	public void setBuscode(String buscode){

		this.buscode= buscode;

	}

	public Integer getMtsendcount(){

		return mtsendcount;
	}

	public void setMtsendcount(Integer mtsendcount){

		this.mtsendcount= mtsendcount;

	}

	public Timestamp getBegintime(){

		return begintime;
	}

	public void setBegintime(Timestamp begintime){

		this.begintime= begintime;

	}

	public String getBusname(){

		return busname;
	}

	public void setBusname(String busname){

		this.busname= busname;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public String getAreacode(){

		return areacode;
	}

	public void setAreacode(String areacode){

		this.areacode= areacode;

	}

	public Timestamp getEndtime(){

		return endtime;
	}

	public void setEndtime(Timestamp endtime){

		this.endtime= endtime;

	}

	public String getMondeviat(){

		return mondeviat;
	}

	public void setMondeviat(String mondeviat){

		this.mondeviat= mondeviat;

	}

	public String getMonphone(){

		return monphone;
	}

	public void setMonphone(String monphone){

		this.monphone= monphone;

	}

	public Integer getMthavesnd(){

		return mthavesnd;
	}

	public void setMthavesnd(Integer mthavesnd){

		this.mthavesnd= mthavesnd;

	}

	public Integer getEvttype() {
		return evttype;
	}

	public void setEvttype(Integer evttype) {
		this.evttype = evttype;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public String getCorpcode() {
		return corpcode;
	}

	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}



//	public String getMonemail() {
//		return monemail;
//	}
//
//	public void setMonemail(String monemail) {
//		this.monemail = monemail;
//	}

	
}

							