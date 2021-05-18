package com.montnets.emp.servmodule.txgl.entity;

import java.sql.Timestamp;

/**
 * SP费用阀值告警信息表
 * @功能概要：
 * @项目名称： p_txgl
 * @初创作者： liuxiangheng <xiaokanrensheng1012@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-10-13 下午03:32:12
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class LfSpFeeAlarm  implements java.io.Serializable
{

	private static final long	serialVersionUID	= -2685671673710792506L;
	// 自增ID
	private Long id;
	//  SP账号
	private String spuser;
	// 通知人姓名
	private String noticename;
	// 告警手机号码
	private String alarmphone;
	// 最后更新记录的操作员ID
	private Long modiuserid;
	//  企业编码
	private String corpcode;
	//创建时间
	private Timestamp createtime;
	//更新时间
	private Timestamp updatetime;
	//告警次数
	private Integer alarmedcount;
	
	public LfSpFeeAlarm(){
		
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getSpuser()
	{
		return spuser;
	}

	public void setSpuser(String spuser)
	{
		this.spuser = spuser;
	}

	public String getNoticename()
	{
		return noticename;
	}

	public void setNoticename(String noticename)
	{
		this.noticename = noticename;
	}

	public String getAlarmphone()
	{
		return alarmphone;
	}

	public void setAlarmphone(String alarmphone)
	{
		this.alarmphone = alarmphone;
	}

	public Long getModiuserid()
	{
		return modiuserid;
	}

	public void setModiuserid(Long modiuserid)
	{
		this.modiuserid = modiuserid;
	}

	public String getCorpcode()
	{
		return corpcode;
	}

	public void setCorpcode(String corpcode)
	{
		this.corpcode = corpcode;
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

	public Integer getAlarmedcount()
	{
		return alarmedcount;
	}

	public void setAlarmedcount(Integer alarmedcount)
	{
		this.alarmedcount = alarmedcount;
	}
	
}
