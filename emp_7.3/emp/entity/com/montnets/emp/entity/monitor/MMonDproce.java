package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * 监控程序动态信息
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-26 下午03:56:41
 */
public class MMonDproce 
{

	//已上传流量
	private Long upflow;

	//虚拟内存使用量
	private Integer vmemusage;

	//已下载流量 
	private Long downflow;

	//物理内存使用量
	private Integer memusage;

	//CPU占用量
	private Integer cpuusage;

	//程序编号
	private Integer proceid;

	//更新时间
	private Timestamp updatetime;

	//最低磁盘剩余量
	private Integer diskfree;

	public MMonDproce() {
	}

	public Long getUpflow() {

		return upflow;
	}

	public void setUpflow(Long upflow) {

		this.upflow = upflow;

	}

	public Integer getVmemusage() {

		return vmemusage;
	}

	public void setVmemusage(Integer vmemusage) {

		this.vmemusage = vmemusage;

	}

	public Long getDownflow() {

		return downflow;
	}

	public void setDownflow(Long downflow) {

		this.downflow = downflow;

	}

	public Integer getMemusage() {

		return memusage;
	}

	public void setMemusage(Integer memusage) {

		this.memusage = memusage;

	}

	public Integer getCpuusage() {

		return cpuusage;
	}

	public void setCpuusage(Integer cpuusage) {

		this.cpuusage = cpuusage;

	}

	public Integer getProceid() {

		return proceid;
	}

	public void setProceid(Integer proceid) {

		this.proceid = proceid;

	}

	public Timestamp getUpdatetime() {

		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {

		this.updatetime = updatetime;

	}

	public Integer getDiskfree() {

		return diskfree;
	}

	public void setDiskfree(Integer diskfree) {

		this.diskfree = diskfree;

	}

}
