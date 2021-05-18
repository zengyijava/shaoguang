package com.montnets.emp.servmodule.txgl.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class LfSpFeeLog implements Serializable {
	/**
	 * @description  
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-13 下午02:33:25
	 */
	private static final long	serialVersionUID	= 1L;
	/**
	 * SP充值/回收日志表
	 */
	//自增ID
	private Long id;
	//SP账号
	private String spuser; 
	// 充值/回收数量
	private Long icount; 
	// 执行结果（0成功，1失败）
	private Integer result; 
	// 操作员id：执行充值/回收的操作员id
	private Long userid;
	// 企业编号
	private String corpcode; 
	// 操作时间：执行充值/回收的时间
	private Timestamp oprtime; 
	// 备注
	private String memo; 

	public LfSpFeeLog() {
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

	public Long getIcount()
	{
		return icount;
	}

	public void setIcount(Long icount)
	{
		this.icount = icount;
	}

	public Integer getResult()
	{
		return result;
	}

	public void setResult(Integer result)
	{
		this.result = result;
	}

	public Long getUserid()
	{
		return userid;
	}

	public void setUserid(Long userid)
	{
		this.userid = userid;
	}

	public String getCorpcode()
	{
		return corpcode;
	}

	public void setCorpcode(String corpcode)
	{
		this.corpcode = corpcode;
	}

	public Timestamp getOprtime()
	{
		return oprtime;
	}

	public void setOprtime(Timestamp oprtime)
	{
		this.oprtime = oprtime;
	}

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}
}
