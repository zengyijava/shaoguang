package com.montnets.emp.greport.vo;
/**
 * 总趋势图形报表实体类
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-19 上午10:39:05
 * @description
 */
public class TotalGreportVo implements java.io.Serializable{

	private static final long serialVersionUID = -6044320532839571992L;

	private Long iymd;
	//年
	private String y;
    //月
	private String imonth;
	//图形报表类型
	private Integer reporttype;
	//企业编码
	private String corpCode;
	//账号类型
	private Integer mstype;	
	//提交总数
	private Long icount;
	private Integer ismuti;
	
	
	public TotalGreportVo(){
		
	}

	
		
	public Integer getIsmuti()
	{
		return ismuti;
	}



	public void setIsmuti(Integer ismuti)
	{
		this.ismuti = ismuti;
	}



	public String getY()
	{
		return y;
	}



	public void setY(String y)
	{
		this.y = y;
	}



	public Integer getReporttype()
	{
		return reporttype;
	}



	public void setReporttype(Integer reporttype)
	{
		this.reporttype = reporttype;
	}



	public Long getIymd()
	{
		return iymd;
	}

	public void setIymd(Long iymd)
	{
		this.iymd = iymd;
	}

	public String getImonth()
	{
		return imonth;
	}

	public void setImonth(String imonth)
	{
		this.imonth = imonth;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}

	public Integer getMstype()
	{
		return mstype;
	}

	public void setMstype(Integer mstype)
	{
		this.mstype = mstype;
	}

	public Long getIcount()
	{
		return icount;
	}

	public void setIcount(Long icount)
	{
		this.icount = icount;
	}
	

}
