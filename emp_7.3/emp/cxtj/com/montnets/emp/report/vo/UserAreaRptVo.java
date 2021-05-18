package com.montnets.emp.report.vo;

/**
 * 报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:49:33
 * @description
 */
public class UserAreaRptVo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3201282192917120629L;
	
	//国家地区代码
	private String areacode;
	//国家地区名称
	private String areaname;
	//开始时间
	private String sendtime;
	//结束时间 
	private String endtime;
	//账户类型 0短信 1彩信
	private Integer mstype;
	//报表类型
	private Integer reporttype;
	//运营商
	private Integer spnumtype;
	//部门id
	private Long userid;
	
	public UserAreaRptVo(){}

	
	
	public Long getUserid()
	{
		return userid;
	}



	public void setUserid(Long userid)
	{
		this.userid = userid;
	}



	public String getAreacode()
	{
		return areacode;
	}

	public void setAreacode(String areacode)
	{
		this.areacode = areacode;
	}

	public String getAreaname()
	{
		return areaname;
	}

	public void setAreaname(String areaname)
	{
		this.areaname = areaname;
	}

	public String getSendtime()
	{
		return sendtime;
	}

	public void setSendtime(String sendtime)
	{
		this.sendtime = sendtime;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	public Integer getMstype()
	{
		return mstype;
	}

	public void setMstype(Integer mstype)
	{
		this.mstype = mstype;
	}


	public Integer getReporttype()
	{
		return reporttype;
	}

	public void setReporttype(Integer reporttype)
	{
		this.reporttype = reporttype;
	}

	public Integer getSpnumtype()
	{
		return spnumtype;
	}

	public void setSpnumtype(Integer spnumtype)
	{
		this.spnumtype = spnumtype;
	}
	
		

}
