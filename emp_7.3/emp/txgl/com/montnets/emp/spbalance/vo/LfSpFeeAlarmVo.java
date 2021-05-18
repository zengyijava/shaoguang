package com.montnets.emp.spbalance.vo;


public class LfSpFeeAlarmVo implements java.io.Serializable{
	
	/**
	 * @description  
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-17 上午09:00:25
	 */
	private static final long	serialVersionUID	= 1L;
	//通知人姓名
	private String noticename;
	//告警手机号码
	private String alarmphone;
	//企业编码
	private String corpcode;
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
	public String getCorpcode()
	{
		return corpcode;
	}
	public void setCorpcode(String corpcode)
	{
		this.corpcode = corpcode;
	}
	
	


}
