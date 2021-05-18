package com.montnets.emp.rms.report.vo;

/**
 * 原始数据对象VO
 * @功能概要：
 * @项目名称： p_ydcx
 * @初创作者： lvxin
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间：2018-1-15
 */
public class CountReportVo implements java.io.Serializable
{
	private static final long serialVersionUID = 7857327174754855656L;
	//提交总数
	private Long icount;
	//接收成功数
	private Long rsucc;
	//发送失败数
	private Long rfail1;
	//接收失败数
	private Long rfail2;
	//未返数
	private Long rnret;
	
	public Long getIcount()
	{
		return icount;
	}
	public void setIcount(Long icount)
	{
		this.icount = icount;
	}

	public Long getRsucc()
	{
		return rsucc;
	}
	public void setRsucc(Long rsucc)
	{
		this.rsucc = rsucc;
	}
	public Long getRfail1()
	{
		return rfail1;
	}
	public void setRfail1(Long rfail1)
	{
		this.rfail1 = rfail1;
	}
	public Long getRfail2()
	{
		return rfail2;
	}
	public void setRfail2(Long rfail2)
	{
		this.rfail2 = rfail2;
	}
	public Long getRnret()
	{
		return rnret;
	}
	public void setRnret(Long rnret)
	{
		this.rnret = rnret;
	}
}
