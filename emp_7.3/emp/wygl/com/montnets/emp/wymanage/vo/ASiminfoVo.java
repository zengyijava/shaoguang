
package com.montnets.emp.wymanage.vo;


public class ASiminfoVo implements Comparable<ASiminfoVo>
{

	//SIM卡号
	private String phoneno;

	//卡序号
	private Integer simno;

	//所属国别
	private Integer mobilearea;
	
	//所属国别名称
	private String areaname;

	//运营商id
	private Integer unicom;
	//运营商名称
	private String unicomname;

	public ASiminfoVo(){
	}

	public String getPhoneno()
	{
		return phoneno;
	}

	public void setPhoneno(String phoneno)
	{
		this.phoneno = phoneno;
	}

	public Integer getSimno()
	{
		return simno;
	}

	public void setSimno(Integer simno)
	{
		this.simno = simno;
	}

	public Integer getMobilearea()
	{
		return mobilearea;
	}

	public void setMobilearea(Integer mobilearea)
	{
		this.mobilearea = mobilearea;
	}

	public String getAreaname()
	{
		return areaname;
	}

	public void setAreaname(String areaname)
	{
		this.areaname = areaname;
	}

	public Integer getUnicom()
	{
		return unicom;
	}

	public void setUnicom(Integer unicom)
	{
		this.unicom = unicom;
	}

	public String getUnicomname()
	{
		return unicomname;
	}

	public void setUnicomname(String unicomname)
	{
		this.unicomname = unicomname;
	}

	public int compareTo(ASiminfoVo o)
	{
		// TODO Auto-generated method stub
		return this.simno.compareTo(o.getSimno());
	} 


}
