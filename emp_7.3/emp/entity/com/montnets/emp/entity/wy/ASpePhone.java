package com.montnets.emp.entity.wy;

import java.sql.Timestamp;

/**
 * 特殊号码管理实体类
 * @description 
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2014-3-28 上午08:43:23
 */
public class ASpePhone
{

	//手机号码
	private String phone;

	//自增id
	private Integer id;

	//
	private Integer custid;

	//记录修改时间
	private Timestamp createtime;

	private Integer spectype;

	//操作类型(0 添加，1 删除)
	private Integer opttype;

	//所属运营商 0移动 1联通 2电信
	private Integer unicom;

	//用户id
	private String userid;

	public ASpePhone(){
	} 

	public String getPhone(){

		return phone;
	}

	public void setPhone(String phone){

		this.phone= phone;

	}

	public Integer getId(){

		return id;
	}

	public void setId(Integer id){

		this.id= id;

	}

	public Integer getCustid(){

		return custid;
	}

	public void setCustid(Integer custid){

		this.custid= custid;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public Integer getSpectype(){

		return spectype;
	}

	public void setSpectype(Integer spectype){

		this.spectype= spectype;

	}

	public Integer getOpttype(){

		return opttype;
	}

	public void setOpttype(Integer opttype){

		this.opttype= opttype;

	}

	public Integer getUnicom(){

		return unicom;
	}

	public void setUnicom(Integer unicom){

		this.unicom= unicom;

	}

	public String getUserid(){

		return userid;
	}

	public void setUserid(String userid){

		this.userid= userid;

	}

}

