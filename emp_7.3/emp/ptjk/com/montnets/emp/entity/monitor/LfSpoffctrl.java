/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-3-23 下午02:50:06
 */
package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-3-23 下午02:50:06
 *        <p>
 *        修改记录1：
 *        </p>
 * 
 *        <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */

public class LfSpoffctrl
{
	private Integer		id;

	private String		spaccountid;

	private String		monofflineprd;

	private String		alarmtime;
	
	private String		alarmflag;
	
	private String		alarmemailflag;

	private Timestamp	createtime;

	private Timestamp	updatetime;

	private String		corpcode;
	
	public LfSpoffctrl()
	{
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getSpaccountid()
	{
		return spaccountid;
	}

	public void setSpaccountid(String spaccountid)
	{
		this.spaccountid = spaccountid;
	}

	public String getMonofflineprd()
	{
		return monofflineprd;
	}

	public void setMonofflineprd(String monofflineprd)
	{
		this.monofflineprd = monofflineprd;
	}

	public String getAlarmflag()
	{
		return alarmflag;
	}

	public void setAlarmflag(String alarmflag)
	{
		this.alarmflag = alarmflag;
	}

	public Timestamp getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(Timestamp createtime)
	{
		this.createtime = createtime;
	}

	public Timestamp getUpdatetime()
	{
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime)
	{
		this.updatetime = updatetime;
	}

	public String getCorpcode()
	{
		return corpcode;
	}

	public void setCorpcode(String corpcode)
	{
		this.corpcode = corpcode;
	}

	public String getAlarmtime()
	{
		return alarmtime;
	}

	public void setAlarmtime(String alarmtime)
	{
		this.alarmtime = alarmtime;
	}

	public String getAlarmemailflag() {
		return alarmemailflag;
	}

	public void setAlarmemailflag(String alarmemailflag) {
		this.alarmemailflag = alarmemailflag;
	}
	
}
