package com.montnets.emp.entity.wy;

import java.sql.Timestamp;

/**
 * 网关路由表
 * @description 
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2014-3-27 上午09:17:09
 */
public class AGwroute {

	//区域代码(备用)
	private Integer area;

	//路由绑定序号(备用，主备)
	private Integer gateseq;

	//信息更新时间
	private Timestamp createtime;

	//对应XT_GATE_QUEUE表中的ID
	private Integer gateid;

	//状态(0 启用，1 禁用)
	private Integer status;

	//发送开始时间(备用)
	private String sendtimebegin;

	//(备用1)
	private String p1;

	//(备用2)
	private String p2;

	//SIM卡信息对应的ID(备用)
	private Integer simid;

	//发送结束时间(备用)
	private String sendtimeend;

	//自增ID
	private Integer id;

	//路由类型(0 携号转网，1 特殊号码，2 国际号码)
	private Integer type;

	//运营商类型(备用)
	private Integer unicom;

	public AGwroute(){
	} 

	public Integer getArea(){

		return area;
	}

	public void setArea(Integer area){

		this.area= area;

	}

	public Integer getGateseq(){

		return gateseq;
	}

	public void setGateseq(Integer gateseq){

		this.gateseq= gateseq;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public Integer getGateid(){

		return gateid;
	}

	public void setGateid(Integer gateid){

		this.gateid= gateid;

	}

	public Integer getStatus(){

		return status;
	}

	public void setStatus(Integer status){

		this.status= status;

	}

	public String getSendtimebegin(){

		return sendtimebegin;
	}

	public void setSendtimebegin(String sendtimebegin){

		this.sendtimebegin= sendtimebegin;

	}

	public String getP1(){

		return p1;
	}

	public void setP1(String p1){

		this.p1= p1;

	}

	public String getP2(){

		return p2;
	}

	public void setP2(String p2){

		this.p2= p2;

	}

	public Integer getSimid(){

		return simid;
	}

	public void setSimid(Integer simid){

		this.simid= simid;

	}

	public String getSendtimeend(){

		return sendtimeend;
	}

	public void setSendtimeend(String sendtimeend){

		this.sendtimeend= sendtimeend;

	}

	public Integer getId(){

		return id;
	}

	public void setId(Integer id){

		this.id= id;

	}

	public Integer getType(){

		return type;
	}

	public void setType(Integer type){

		this.type= type;

	}

	public Integer getUnicom(){

		return unicom;
	}

	public void setUnicom(Integer unicom){

		this.unicom= unicom;

	}

}
