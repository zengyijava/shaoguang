package com.montnets.emp.greport.vo;
/**
 * 区域对比图形报表实体类
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-20 下午05:00:53
 * @description
 */
public class AreaGreportVo implements java.io.Serializable{

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
	
	private String province;
	
	
	public AreaGreportVo(){
		
	}
	
		
	public String getProvince()
	{
		return province;
	}



	public void setProvince(String province)
	{
		this.province = province;
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
