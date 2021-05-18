package com.montnets.emp.entity.system;

import java.sql.Timestamp;

/**
 * 
 * @author Administrator
 *
 */
public class LfDeppwdReceiver implements java.io.Serializable
{
	private static final long serialVersionUID = -453720090586239004L;

	//标识列ID
	private Long dprid;
	//部门id
	private Long depid;
	//用户userid
	private Long userid;
	//创建时间
	private Timestamp createtime;
	
	public Long getDprid() {
		return dprid;
	}
	public void setDprid(Long dprid) {
		this.dprid = dprid;
	}
	public Long getDepid() {
		return depid;
	}
	public void setDepid(Long depid) {
		this.depid = depid;
	}

	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	
	
		
}