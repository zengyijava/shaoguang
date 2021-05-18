package com.montnets.emp.entity.monitor;


import java.sql.Timestamp;

/**
 * 
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-26 下午01:57:18
 */
public class MMonDhost
{

	//虚拟内存
	private Integer vmemuse;

	//主板温度
	private Integer boardtemp;

	//持续时间控制
	private Timestamp acttime;

	//虚拟内存使用量
	private Integer vmemusage;

	//修改时间
	private Timestamp updatetime;

	//CPU占用时间
	private Integer cpusj;

	//Cpu温度
	private Integer cputemp;

	//磁盘剩余量
	private Integer diskfreespace;

	//主机状态
	private Integer hoststatus;

	//物理内存使用量
	private Integer memusage;

	//主机编号
	private Integer hostid;

	//CPU占用量
	private Integer cpuusage;

	//进程总数
	private Integer processcnt;

	//物理内存
	private Integer memuse;

	//CPU占用比率
	private Integer cpubl;

	//磁盘温度
	private Integer disktemp;

	//最低磁盘剩余量
	private Integer diskfree;

	public MMonDhost(){
	} 

	public Integer getVmemuse(){

		return vmemuse;
	}

	public void setVmemuse(Integer vmemuse){

		this.vmemuse= vmemuse;

	}

	public Integer getBoardtemp(){

		return boardtemp;
	}

	public void setBoardtemp(Integer boardtemp){

		this.boardtemp= boardtemp;

	}

	public Timestamp getActtime(){

		return acttime;
	}

	public void setActtime(Timestamp acttime){

		this.acttime= acttime;

	}

	public Integer getVmemusage(){

		return vmemusage;
	}

	public void setVmemusage(Integer vmemusage){

		this.vmemusage= vmemusage;

	}

	public Timestamp getUpdatetime(){

		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime){

		this.updatetime= updatetime;

	}

	public Integer getCpusj(){

		return cpusj;
	}

	public void setCpusj(Integer cpusj){

		this.cpusj= cpusj;

	}

	public Integer getCputemp(){

		return cputemp;
	}

	public void setCputemp(Integer cputemp){

		this.cputemp= cputemp;

	}

	public Integer getDiskfreespace(){

		return diskfreespace;
	}

	public void setDiskfreespace(Integer diskfreespace){

		this.diskfreespace= diskfreespace;

	}

	public Integer getHoststatus(){

		return hoststatus;
	}

	public void setHoststatus(Integer hoststatus){

		this.hoststatus= hoststatus;

	}

	public Integer getMemusage(){

		return memusage;
	}

	public void setMemusage(Integer memusage){

		this.memusage= memusage;

	}

	public Integer getHostid(){

		return hostid;
	}

	public void setHostid(Integer hostid){

		this.hostid= hostid;

	}

	public Integer getCpuusage(){

		return cpuusage;
	}

	public void setCpuusage(Integer cpuusage){

		this.cpuusage= cpuusage;

	}

	public Integer getProcesscnt(){

		return processcnt;
	}

	public void setProcesscnt(Integer processcnt){

		this.processcnt= processcnt;

	}

	public Integer getMemuse(){

		return memuse;
	}

	public void setMemuse(Integer memuse){

		this.memuse= memuse;

	}

	public Integer getCpubl(){

		return cpubl;
	}

	public void setCpubl(Integer cpubl){

		this.cpubl= cpubl;

	}

	public Integer getDisktemp(){

		return disktemp;
	}

	public void setDisktemp(Integer disktemp){

		this.disktemp= disktemp;

	}

	public Integer getDiskfree(){

		return diskfree;
	}

	public void setDiskfree(Integer diskfree){

		this.diskfree= diskfree;

	}

}

					