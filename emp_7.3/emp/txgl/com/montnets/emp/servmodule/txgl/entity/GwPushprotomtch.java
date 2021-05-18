package com.montnets.emp.servmodule.txgl.entity;

public class GwPushprotomtch {

	//客户字段类型
	private Integer cargtype;
	
	//预留
	private String reserve;
	
	//上级节点 例如：第一级节点为空字符串，其他为上级节点；所属mos
	private String belong;
	
	//固定字段值
	private String cargvalue;
	
	//命令类型1请求
	private Integer cmdtype;
	
	//自增ID
	private Integer id;
	
	//梦网字段名称
	private String margname;
	
	//是否有下级节点，例如：0：无子节点，1：有下级子节点
	private Integer belongtype;
	
	//客户字段名
	private String cargname;
	
	//推送类型
	private Integer pushflag;
	
	//企业ID
	private Integer ecid;
	
	//用户ID
	private String userid;

	public GwPushprotomtch(){
	} 

	public Integer getCargtype(){

		return cargtype;
	}

	public void setCargtype(Integer cargtype){

		this.cargtype= cargtype;

	}

	public String getReserve(){

		return reserve;
	}

	public void setReserve(String reserve){

		this.reserve= reserve;

	}

	public String getBelong(){

		return belong;
	}

	public void setBelong(String belong){

		this.belong= belong;

	}

	public String getCargvalue(){

		return cargvalue;
	}

	public void setCargvalue(String cargvalue){

		this.cargvalue= cargvalue;

	}

	public Integer getCmdtype(){

		return cmdtype;
	}

	public void setCmdtype(Integer cmdtype){

		this.cmdtype= cmdtype;

	}

	public Integer getId(){

		return id;
	}

	public void setId(Integer id){

		this.id= id;

	}

	public String getMargname(){

		return margname;
	}

	public void setMargname(String margname){

		this.margname= margname;

	}

	public Integer getBelongtype(){

		return belongtype;
	}

	public void setBelongtype(Integer belongtype){

		this.belongtype= belongtype;

	}

	public String getCargname(){

		return cargname;
	}

	public void setCargname(String cargname){

		this.cargname= cargname;

	}

	public Integer getPushflag(){

		return pushflag;
	}

	public void setPushflag(Integer pushflag){

		this.pushflag= pushflag;

	}

	public Integer getEcid(){

		return ecid;
	}

	public void setEcid(Integer ecid){

		this.ecid= ecid;

	}

	public String getUserid(){

		return userid;
	}

	public void setUserid(String userid){

		this.userid= userid;

	}
}
