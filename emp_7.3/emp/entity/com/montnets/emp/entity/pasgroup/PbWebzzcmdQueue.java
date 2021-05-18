package com.montnets.emp.entity.pasgroup;

import java.sql.Timestamp;

 
/**
 * TablePbWebzzcmdQueue对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:20:11
 * @description 
 */
public class PbWebzzcmdQueue implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2464131332774319002L;
	/**
	 * 
	 */
	//自增ID
	private Integer id;
	//源账号ID
	private String sid;
	//命令类型(113充值，213回收)
	private Long cmdType;
	//充值账号
	private String userId;
	//充值条数
	private String cmdParam; 
	private Timestamp exectime;
	//操作者电脑IP
	private String useIp;
	
	private Integer execflag;
	//备注信息
	private String memo; 
	private Integer prodId;
	private String opid;
	private Integer feeType;
	//充值单价 单位为分。
	private Double fee; 
	
    
	public PbWebzzcmdQueue() {
		this.sid=" ";
	    this.cmdParam = "0";
	    this.useIp = " ";
	    this.execflag = 1;
	    this.feeType = 1;
	    this.opid = " ";
	    this.prodId = new Integer(0);
	    this.fee = new Double(0);
	    this.exectime = new Timestamp(System.currentTimeMillis());

	}

    
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

 

	public String getCmdParam()
	{
		return cmdParam;
	}

	public void setCmdParam(String cmdParam)
	{
		this.cmdParam = cmdParam;
	}

	 

	 

	public Timestamp getExectime() {
		return exectime;
	}


	public void setExectime(Timestamp exectime) {
		this.exectime = exectime;
	}


	public String getUseIp()
	{
		return useIp;
	}

	public void setUseIp(String useIp)
	{
		this.useIp = useIp;
	}

	public Integer getExecflag()
	{
		return execflag;
	}

	public void setExecflag(Integer execflag)
	{
		this.execflag = execflag;
	}

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	public Integer getProdId()
	{
		return prodId;
	}

	public void setProdId(Integer prodId)
	{
		this.prodId = prodId;
	}

	public String getOpid()
	{
		return opid;
	}

	public void setOpid(String opid)
	{
		this.opid = opid;
	}

	public Integer getFeeType()
	{
		return feeType;
	}

	public void setFeeType(Integer feeType)
	{
		this.feeType = feeType;
	}

	public Double getFee()
	{
		return fee;
	}

	public void setFee(Double fee)
	{
		this.fee = fee;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getSid()
	{
		return sid;
	}

	public void setSid(String sid)
	{
		this.sid = sid;
	}

	public Long getCmdType()
	{
		return cmdType;
	}

	public void setCmdType(Long cmdType)
	{
		this.cmdType = cmdType;
	}
 




}