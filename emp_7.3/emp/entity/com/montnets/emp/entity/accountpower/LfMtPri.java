package com.montnets.emp.entity.accountpower;
import java.sql.Timestamp;


/*
 * 下行记录查询权限
 */
public class LfMtPri {
	/*操作员USERID*/
	private Long userid;
	/*标识列ID*/
	private Long id;
	/*创建时间*/
	private Timestamp createtime;
	/*企业编码*/
	private String corpcode;
	/*创建者USERID*/
	private Long createuserid;
	/*发送账号*/
	private String spuserid;

	public LfMtPri(){
	} 

	public Long getUserid(){

		return userid;
	}

	public void setUserid(Long userid){

		this.userid= userid;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

	}

	public Long getCreateuserid(){

		return createuserid;
	}

	public void setCreateuserid(Long createuserid){

		this.createuserid= createuserid;

	}

	public String getSpuserid(){

		return spuserid;
	}

	public void setSpuserid(String spuserid){

		this.spuserid= spuserid;

	}
}
