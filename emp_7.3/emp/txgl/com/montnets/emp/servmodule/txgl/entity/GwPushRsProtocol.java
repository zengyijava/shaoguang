package com.montnets.emp.servmodule.txgl.entity;
/**
 * 推送用户回应协议表
 * @author Administrator
 *
 */
public class GwPushRsProtocol 
{
	//ID
	private Long id;
	
	//企业ID
	private Integer ecid;
	
	//用户名
	private String userid;
	
	//返回命令 1-MO回应   2-RPT回应
	private Integer rspCmd;
	
	//客户参数名 客户回应包中的用来匹分该回应包为成功,失败,部分成功失败的字段名 如:STATUS
	private String cargName;
	
	//回应状态 2全成功回应  3全失败回应 4部分成功部分失败回应 5回应详细信息
	private Integer rspStatus;
	
	//回应格式 0:未知 1:xml 2:json 4:urlencode
	private Integer crspfmt;
	
	//客户字段值
	private String cargValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEcid() {
		return ecid;
	}

	public void setEcid(Integer ecid) {
		this.ecid = ecid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getRspCmd() {
		return rspCmd;
	}

	public void setRspCmd(Integer rspCmd) {
		this.rspCmd = rspCmd;
	}

	public String getCargName() {
		return cargName;
	}

	public void setCargName(String cargName) {
		this.cargName = cargName;
	}

	public Integer getRspStatus() {
		return rspStatus;
	}

	public void setRspStatus(Integer rspStatus) {
		this.rspStatus = rspStatus;
	}

	public Integer getCrspfmt() {
		return crspfmt;
	}

	public void setCrspfmt(Integer crspfmt) {
		this.crspfmt = crspfmt;
	}

	public String getCargValue() {
		return cargValue;
	}

	public void setCargValue(String cargValue) {
		this.cargValue = cargValue;
	}
	
	
}
