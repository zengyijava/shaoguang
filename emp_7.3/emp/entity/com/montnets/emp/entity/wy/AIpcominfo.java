
package com.montnets.emp.entity.wy;

import java.sql.Timestamp;

public class AIpcominfo
{
	//备注
	private String common;
	//端口
	private Integer port;
	//通道名称
	private String gatename;
	//企业签名
	private String corpsign;
	//ip
	private String ip;
	//ip
	private String ptip;
	//端口
	private Integer ptport;
	//自增id
	private Integer id;
	//通道id
	private Integer gateid;
	//创建时间
	private Timestamp createtime;

	public AIpcominfo(){
	} 

	
	
	public String getPtip()
	{
		return ptip;
	}



	public void setPtip(String ptip)
	{
		this.ptip = ptip;
	}



	public Integer getPtport()
	{
		return ptport;
	}



	public void setPtport(Integer ptport)
	{
		this.ptport = ptport;
	}



	public Timestamp getCreatetime()
	{
		return createtime;
	}



	public void setCreatetime(Timestamp createtime)
	{
		this.createtime = createtime;
	}



	public String getCommon(){

		return common;
	}

	public void setCommon(String common){

		this.common= common;

	}

	public Integer getPort(){

		return port;
	}

	public void setPort(Integer port){

		this.port= port;

	}

	public String getGatename(){

		return gatename;
	}

	public void setGatename(String gatename){

		this.gatename= gatename;

	}

	public String getCorpsign(){

		return corpsign;
	}

	public void setCorpsign(String corpsign){

		this.corpsign= corpsign;

	}

	public String getIp(){

		return ip;
	}

	public void setIp(String ip){

		this.ip= ip;

	}

	public Integer getId(){

		return id;
	}

	public void setId(Integer id){

		this.id= id;

	}

	public Integer getGateid(){

		return gateid;
	}

	public void setGateid(Integer gateid){

		this.gateid= gateid;

	}

}
