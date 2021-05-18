package com.montnets.emp.greport.vo;
/**
 * SP账号图形报表实体类
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-20 下午05:01:58
 * @description
 */
public class SpuserGreportVo implements java.io.Serializable{

	private static final long serialVersionUID = -6044320532839571992L;

	//年
	private String y;
    //月
	private String imonth;
	//企业编码
	private String corpCode;
	//账号类型
	private Integer mstype;	
	//提交总数
	private Long icount;
	
	private String userid;
	
	private String orderby;
	
	
	public SpuserGreportVo(){
		
	}
	
	
	public String getOrderby()
	{
		return orderby;
	}



	public void setOrderby(String orderby)
	{
		this.orderby = orderby;
	}



	public String getUserid()
	{
		return userid;
	}



	public void setUserid(String userid)
	{
		this.userid = userid;
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
