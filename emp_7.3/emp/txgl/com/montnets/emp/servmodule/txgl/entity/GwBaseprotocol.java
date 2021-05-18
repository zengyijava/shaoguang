package com.montnets.emp.servmodule.txgl.entity;

import java.sql.Timestamp;

/***
 * 企业基本协议表
 * @author Administrator
 *
 */
public class GwBaseprotocol
{
	//方法类型：例如：cstd/jd0001  主动推送给用户时，此值为空(‘’)
	private String funtype;
	//客户方法名 例如：csingle_send
	private String cfunname;
	//记录最后修改时间
	private Timestamp modiytm;
	//例如：single_send,batch_send,multi_send等
	//主动推送给用户时，上行值为'MO'，状态报告值为'RPT'
	private String funname;
	//请求或响应格式
	private String fmtmsg;
	//请求类型1请求  ,2全成功回应 ,3全失败回应,4部分成功部分失败回应,5回应详细信息
	private Integer cmdtype;
	//ID 主键
	private Integer id;
	//客户接口名称
	private String custintfname;
	//接口状态,单个接口状态(无论请求或回应统一种状态)0：禁用1：启用
	private Integer status;
	//记录创建时间
	private Timestamp createtm;
	//请求或响应数据格式0:未知1:xml2:json4:urlencode
	private Integer rettype;
	//企业ID
	private Integer ecid;

	public GwBaseprotocol(){
	} 

	public String getFuntype(){

		return funtype;
	}

	public void setFuntype(String funtype){

		this.funtype= funtype;

	}

	public String getCfunname(){

		return cfunname;
	}

	public void setCfunname(String cfunname){

		this.cfunname= cfunname;

	}

	public Timestamp getModiytm(){

		return modiytm;
	}

	public void setModiytm(Timestamp modiytm){

		this.modiytm= modiytm;

	}

	public String getFunname(){

		return funname;
	}

	public void setFunname(String funname){

		this.funname= funname;

	}

	public String getFmtmsg(){

		return fmtmsg;
	}

	public void setFmtmsg(String fmtmsg){

		this.fmtmsg= fmtmsg;

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

	public String getCustintfname(){

		return custintfname;
	}

	public void setCustintfname(String custintfname){

		this.custintfname= custintfname;

	}

	public Integer getStatus(){

		return status;
	}

	public void setStatus(Integer status){

		this.status= status;

	}

	public Timestamp getCreatetm(){

		return createtm;
	}

	public void setCreatetm(Timestamp createtm){

		this.createtm= createtm;

	}

	public Integer getRettype(){

		return rettype;
	}

	public void setRettype(Integer rettype){

		this.rettype= rettype;

	}

	public Integer getEcid(){

		return ecid;
	}

	public void setEcid(Integer ecid){

		this.ecid= ecid;

	}

}


