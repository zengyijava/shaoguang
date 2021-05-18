
package com.montnets.emp.entity.wy;

import java.sql.Timestamp;

public class ASiminfo
{
	//月上限
	private Integer monthlimit;
	//时上限
	private Integer hourlimit;
	//SIM卡号
	private String phoneno;
	//备注
	private String description;
	//日上限
	private Integer daylimit;
	//sim卡序号
	private Integer simno;
	//所属地区
	private Integer mobilearea;
	//自增id
	private Integer id;
	//创建时间
	private Timestamp createtime;
	//通道ID
	private Integer gateid;
	//运营商
	private Integer unicom;

	public ASiminfo(){
	} 

	public Integer getMonthlimit(){

		return monthlimit;
	}

	public void setMonthlimit(Integer monthlimit){

		this.monthlimit= monthlimit;

	}

	public Integer getHourlimit(){

		return hourlimit;
	}

	public void setHourlimit(Integer hourlimit){

		this.hourlimit= hourlimit;

	}

	public String getPhoneno(){

		return phoneno;
	}

	public void setPhoneno(String phoneno){

		this.phoneno= phoneno;

	}

	public String getDescription(){

		return description;
	}

	public void setDescription(String description){

		this.description= description;

	}

	public Integer getDaylimit(){

		return daylimit;
	}

	public void setDaylimit(Integer daylimit){

		this.daylimit= daylimit;

	}

	public Integer getSimno(){

		return simno;
	}

	public void setSimno(Integer simno){

		this.simno= simno;

	}

	public Integer getMobilearea(){

		return mobilearea;
	}

	public void setMobilearea(Integer mobilearea){

		this.mobilearea= mobilearea;

	}

	public Integer getId(){

		return id;
	}

	public void setId(Integer id){

		this.id= id;

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

	public Integer getUnicom(){

		return unicom;
	}

	public void setUnicom(Integer unicom){

		this.unicom= unicom;

	}

}
