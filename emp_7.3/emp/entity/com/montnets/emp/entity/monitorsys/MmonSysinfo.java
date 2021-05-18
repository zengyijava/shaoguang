package com.montnets.emp.entity.monitorsys;

import java.sql.Timestamp;

public class MmonSysinfo implements java.io.Serializable {

	private static final long serialVersionUID = 5850704232378115430L;

	private String ptcode;
	
	private Integer cpuUsage;
	
	private Integer memUsage;
	
	private Integer vmemUsage;
	
	private Integer diskFreeSpace;
	//修改时间
	private Timestamp updateTime;
	
	public MmonSysinfo(){}
	 
	public String getPtcode() {
		return ptcode;
	}

	public void setPtcode(String ptcode) {
		this.ptcode = ptcode;
	}

	public Integer getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(Integer cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public Integer getMemUsage() {
		return memUsage;
	}

	public void setMemUsage(Integer memUsage) {
		this.memUsage = memUsage;
	}

	public Integer getVmemUsage() {
		return vmemUsage;
	}

	public void setVmemUsage(Integer vmemUsage) {
		this.vmemUsage = vmemUsage;
	}

	public Integer getDiskFreeSpace() {
		return diskFreeSpace;
	}

	public void setDiskFreeSpace(Integer diskFreeSpace) {
		this.diskFreeSpace = diskFreeSpace;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}	
}
