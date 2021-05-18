package com.montnets.emp.report.vo;

/**
 * 套餐统计报表
 * @project p_ydyw
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-1-19 上午10:25:25
 * @description
 */
public class ChargeStatsRptVo implements java.io.Serializable,Comparable<ChargeStatsRptVo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3201282192917120629L;
	
	 //年
	private String y;
    //月
	private String imonth;
	//套餐 0   未知 "1"
	private String udtype;
	//套餐名称
	private String taocancode;
	//套餐名称
	private String taocanname;
	//套餐类型（计费类型） 包年包月 vip
	private Integer taocantype;
	//套餐单价
	private Long taocanmoney;
	 //签约人数
	private Long contractcount;
	//扣费成功数
	private Long deductioncount;
	//扣费总金额
	private Long deductiontotalcount;
	//扣费失败数
	private Long deductionfailcount;
	//退费金额
	private Long backmoney;
	//总收入
	private Long totalmoney;
	//按什么查询 0为月  1为年
	private Integer reporttype;
	
	public ChargeStatsRptVo(){}

	
	
	public Long getTaocanmoney()
	{
		return taocanmoney;
	}



	public void setTaocanmoney(Long taocanmoney)
	{
		this.taocanmoney = taocanmoney;
	}



	public String getTaocancode()
	{
		return taocancode;
	}

	public void setTaocancode(String taocancode)
	{
		this.taocancode = taocancode;
	}

	public String getY()
	{
		return y;
	}

	public void setY(String y)
	{
		this.y = y;
	}

	public String getImonth()
	{
		return imonth;
	}

	public void setImonth(String imonth)
	{
		this.imonth = imonth;
	}

	public String getTaocanname()
	{
		return taocanname;
	}

	public void setTaocanname(String taocanname)
	{
		this.taocanname = taocanname;
	}

	public Integer getTaocantype()
	{
		return taocantype;
	}

	public void setTaocantype(Integer taocantype)
	{
		this.taocantype = taocantype;
	}


	public Long getContractcount()
	{
		return contractcount;
	}

	public void setContractcount(Long contractcount)
	{
		this.contractcount = contractcount;
	}

	public Long getDeductioncount()
	{
		return deductioncount;
	}

	public void setDeductioncount(Long deductioncount)
	{
		this.deductioncount = deductioncount;
	}

	public Long getDeductiontotalcount()
	{
		return deductiontotalcount;
	}

	public void setDeductiontotalcount(Long deductiontotalcount)
	{
		this.deductiontotalcount = deductiontotalcount;
	}

	public Long getDeductionfailcount()
	{
		return deductionfailcount;
	}

	public void setDeductionfailcount(Long deductionfailcount)
	{
		this.deductionfailcount = deductionfailcount;
	}

	public Long getBackmoney()
	{
		return backmoney;
	}

	public void setBackmoney(Long backmoney)
	{
		this.backmoney = backmoney;
	}

	public Long getTotalmoney()
	{
		return totalmoney;
	}

	public void setTotalmoney(Long totalmoney)
	{
		this.totalmoney = totalmoney;
	}

	public Integer getReporttype()
	{
		return reporttype;
	}

	public void setReporttype(Integer reporttype)
	{
		this.reporttype = reporttype;
	}


	public String getUdtype()
	{
		return udtype;
	}



	public void setUdtype(String udtype)
	{
		this.udtype = udtype;
	}



	public int compareTo(ChargeStatsRptVo o)
	{
		// TODO Auto-generated method stub
		int i = this.udtype.compareTo(o.getUdtype());
		if(i==0){
			int j = o.getTotalmoney().compareTo(this.totalmoney);
			return j;
		}
		return i;
	}


}
