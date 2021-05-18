package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

public class LfMonSpgatebuf
{

	//RPT发送等待缓冲
	private Integer rptsdwaitbuf;

	//MO发送缓冲
	private Integer mosdbuf;

	//RPT发送缓冲
	private Integer rptsdbuf;

	//MT数据库更新缓冲
	private Integer mtupdbuf;

	//MT发送缓冲
	private Integer mtsdbuf;

	//上行状态报告接收速度
	private Timestamp updatetime;

	//程序编号
	private Long proceid;

	//MO发送等待缓冲
	private Integer mosdwaitbuf;

	//RPT接收量
	private Integer rptrvcnt;

	private Long id;

	//MT发送量
	private Integer mtsdcnt;

	//MT发送速度(3秒)
	private Integer mtspd1;

	//MO接收量
	private Integer morvcnt;

	//MT发送 (1分钟)
	private Integer mtspd2;

	//上行状态报告接收速度
	private Integer morptspd;

	//MO接收缓冲
	private Integer rptrvbuf;

	//MO接收缓冲
	private Integer morvbuf;

	//网关编号
	private Long gatewayid;

	//MT接收量
	private Integer mtrvcnt;

	//MT发送等待缓冲
	private Integer mtsdwaitbuf;

	public LfMonSpgatebuf(){
	} 

	public Integer getRptsdwaitbuf(){

		return rptsdwaitbuf;
	}

	public void setRptsdwaitbuf(Integer rptsdwaitbuf){

		this.rptsdwaitbuf= rptsdwaitbuf;

	}

	public Integer getMosdbuf(){

		return mosdbuf;
	}

	public void setMosdbuf(Integer mosdbuf){

		this.mosdbuf= mosdbuf;

	}

	public Integer getRptsdbuf(){

		return rptsdbuf;
	}

	public void setRptsdbuf(Integer rptsdbuf){

		this.rptsdbuf= rptsdbuf;

	}

	public Integer getMtupdbuf(){

		return mtupdbuf;
	}

	public void setMtupdbuf(Integer mtupdbuf){

		this.mtupdbuf= mtupdbuf;

	}

	public Integer getMtsdbuf(){

		return mtsdbuf;
	}

	public void setMtsdbuf(Integer mtsdbuf){

		this.mtsdbuf= mtsdbuf;

	}

	public Timestamp getUpdatetime(){

		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime){

		this.updatetime= updatetime;

	}

	public Long getProceid(){

		return proceid;
	}

	public void setProceid(Long proceid){

		this.proceid= proceid;

	}

	public Integer getMosdwaitbuf(){

		return mosdwaitbuf;
	}

	public void setMosdwaitbuf(Integer mosdwaitbuf){

		this.mosdwaitbuf= mosdwaitbuf;

	}

	public Integer getRptrvcnt(){

		return rptrvcnt;
	}

	public void setRptrvcnt(Integer rptrvcnt){

		this.rptrvcnt= rptrvcnt;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Integer getMtsdcnt(){

		return mtsdcnt;
	}

	public void setMtsdcnt(Integer mtsdcnt){

		this.mtsdcnt= mtsdcnt;

	}

	public Integer getMtspd1(){

		return mtspd1;
	}

	public void setMtspd1(Integer mtspd1){

		this.mtspd1= mtspd1;

	}

	public Integer getMorvcnt(){

		return morvcnt;
	}

	public void setMorvcnt(Integer morvcnt){

		this.morvcnt= morvcnt;

	}

	public Integer getMtspd2(){

		return mtspd2;
	}

	public void setMtspd2(Integer mtspd2){

		this.mtspd2= mtspd2;

	}

	public Integer getMorptspd(){

		return morptspd;
	}

	public void setMorptspd(Integer morptspd){

		this.morptspd= morptspd;

	}

	public Integer getRptrvbuf(){

		return rptrvbuf;
	}

	public void setRptrvbuf(Integer rptrvbuf){

		this.rptrvbuf= rptrvbuf;

	}

	public Integer getMorvbuf(){

		return morvbuf;
	}

	public void setMorvbuf(Integer morvbuf){

		this.morvbuf= morvbuf;

	}

	public Long getGatewayid(){

		return gatewayid;
	}

	public void setGatewayid(Long gatewayid){

		this.gatewayid= gatewayid;

	}

	public Integer getMtrvcnt(){

		return mtrvcnt;
	}

	public void setMtrvcnt(Integer mtrvcnt){

		this.mtrvcnt= mtrvcnt;

	}

	public Integer getMtsdwaitbuf(){

		return mtsdwaitbuf;
	}

	public void setMtsdwaitbuf(Integer mtsdwaitbuf){

		this.mtsdwaitbuf= mtsdwaitbuf;

	}
}
