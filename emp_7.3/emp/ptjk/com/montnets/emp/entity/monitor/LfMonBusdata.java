package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;
/**
 * 业务监控数据信息表
 * @description 
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-11-23 上午10:29:13  
 */
public class LfMonBusdata
{
	//自增ID
	private Long id;
	//偏离率（高）
	private Integer deviathigh;
	//开始时间
	private Integer beginhour;
	//结束时间
	private Integer endhour;
	//业务ID
	private Long busbaseid;
	//最后一次告警时间
	private Integer monlasttime;
	//偏离率（低）
	private Integer deviatlow;
	//MT已发告警值
	private Integer mthavesnd;
	//创建时间
	private Timestamp createtime;

	//企业编码
	private String corpcode;
	//最后更新记录的操作员ID
	private Long modiuserid;
	
	public LfMonBusdata(){
		
	} 



	public Integer getDeviathigh(){

		return deviathigh;
	}

	public void setDeviathigh(Integer deviathigh){

		this.deviathigh= deviathigh;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}


	public Long getBusbaseid() {
		return busbaseid;
	}

	public void setBusbaseid(Long busbaseid) {
		this.busbaseid = busbaseid;
	}


	public Integer getDeviatlow(){

		return deviatlow;
	}

	public void setDeviatlow(Integer deviatlow){

		this.deviatlow= deviatlow;

	}

	public Integer getMthavesnd(){

		return mthavesnd;
	}

	public void setMthavesnd(Integer mthavesnd){

		this.mthavesnd= mthavesnd;

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



	public Integer getMonlasttime() {
		return monlasttime;
	}



	public void setMonlasttime(Integer monlasttime) {
		this.monlasttime = monlasttime;
	}



	public Long getModiuserid() {
		return modiuserid;
	}



	public void setModiuserid(Long modiuserid) {
		this.modiuserid = modiuserid;
	}
	
	

	
}

							