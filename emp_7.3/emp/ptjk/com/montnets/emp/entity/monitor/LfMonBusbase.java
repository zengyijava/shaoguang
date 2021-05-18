package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * 业务监控基础信息表 
 * @description 
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-11-23 上午10:29:13  
 */
public class LfMonBusbase
{
	//自增ID
	private Long id;
	//业务名称
	private String busname;
	//业务编码
	private String buscode;
	//监控开始时间
	private Timestamp begintime;
	//区域
	private String areacode;
	//监控结果时间
	private Timestamp endtime;
	//告警通知手机号
	private String monphone;
	//告警通知邮箱
	private String monemail;
	//监控状态
	private Integer monstate;
	//创建时间
	private Timestamp createtime;
	//更新时间
	private Timestamp updatetime;
	//企业编码
	private String corpcode;
	//最后更新记录的操作员ID
	private Long modiuserid;
	
	public LfMonBusbase(){
	} 

	public String getBuscode(){

		return buscode;
	}

	public void setBuscode(String buscode){

		this.buscode= buscode;

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

	public String getMonphone(){

		return monphone;
	}

	public void setMonphone(String monphone){

		this.monphone= monphone;

	}

	public Integer getMonstate() {
		return monstate;
	}

	public void setMonstate(Integer monstate) {
		this.monstate = monstate;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public String getCorpcode() {
		return corpcode;
	}

	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}

	public Long getModiuserid() {
		return modiuserid;
	}

	public void setModiuserid(Long modiuserid) {
		this.modiuserid = modiuserid;
	}

	public String getMonemail() {
		return monemail;
	}

	public void setMonemail(String monemail) {
		this.monemail = monemail;
	}
	
}