package com.montnets.emp.servmodule.txgl.entity;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 接口参数基本表
 * @author Administrator
 *
 */
public class GwBasePara 
{
	//自增ID
	private Long id;
	
	//方法名，和CMDTYPE、ARGNAME一起作为主键。
	private String funName;
	
	//请求类型 1 请求 2 全成功回应 3 全失败回应 4 部分成功部分失败回应  5 回应详细信息
	private Integer cmdType;
	
	//参数名称，梦网标准接口参数例如：userid,pwd,msgid等
	private String argName;
	
	//参数长度。例如：msg值的长度为1000
	private Integer argValueLen;
	
	//参数描述。即参数的中文名称，例如：帐号，密码
	private String argDes;
	
	//参数类型。1：字符串，2：int， 3：tinyint，4：大整形
	private Integer argType;
	
//	//创建时间
	private Timestamp createTime;
//	//最后修改记录时间
	
	private Timestamp modifTime;
	
	//预留
	private String reserve;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFunName() {
		return funName;
	}

	public void setFunName(String funName) {
		this.funName = funName;
	}

	public Integer getCmdType() {
		return cmdType;
	}

	public void setCmdType(Integer cmdType) {
		this.cmdType = cmdType;
	}

	public String getArgName() {
		return argName;
	}

	public void setArgName(String argName) {
		this.argName = argName;
	}

	public Integer getArgValueLen() {
		return argValueLen;
	}

	public void setArgValueLen(Integer argValueLen) {
		this.argValueLen = argValueLen;
	}

	public String getArgDes() {
		return argDes;
	}

	public void setArgDes(String argDes) {
		this.argDes = argDes;
	}

	public Integer getArgType() {
		return argType;
	}

	public void setArgType(Integer argType) {
		this.argType = argType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getModifTime() {
		return modifTime;
	}

	public void setModifTime(Timestamp modifTime) {
		this.modifTime = modifTime;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	
}
