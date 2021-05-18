package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-2 下午04:01:28
 */

public class MMonOnlcfg
{
	//在线用户数
	private Long maxonline;
	//刷新时间
	private Long monfreq;
	//最大在线用户数
	private Long onlinenum;
	//自增ID
	private Long id;

	public MMonOnlcfg(){
	} 

	public Long getMaxonline(){

		return maxonline;
	}

	public void setMaxonline(Long maxonline){

		this.maxonline= maxonline;

	}

	public Long getMonfreq(){

		return monfreq;
	}

	public void setMonfreq(Long monfreq){

		this.monfreq= monfreq;

	}

	public Long getOnlinenum(){

		return onlinenum;
	}

	public void setOnlinenum(Long onlinenum){

		this.onlinenum= onlinenum;

	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

}
