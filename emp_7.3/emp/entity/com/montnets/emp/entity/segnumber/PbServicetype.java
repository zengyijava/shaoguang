package com.montnets.emp.entity.segnumber;


/**
 * TablePbServicetype对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:19:42
 * @description 
 */
public class PbServicetype implements java.io.Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -2202559574594028436L;
	//运营商（0移动  1联通 21 电信）
	private Integer spisuncm;
	//号段内容，多个号段以逗号分割，如：135,136
	private String serviceno;
	//号段描述
	private String serviceinfo;

	public PbServicetype() {
		//初始化
		this.serviceno=" ";
		this.serviceinfo=" ";
	}

	public PbServicetype(String serviceno, String serviceinfo) {
		this.serviceno = serviceno;
		this.serviceinfo = serviceinfo;
	}
   
	public Integer getSpisuncm()
	{
		return spisuncm;
	}

	public void setSpisuncm(Integer spisuncm)
	{
		this.spisuncm = spisuncm;
	}

	public String getServiceno()
	{
		return serviceno;
	}

	public void setServiceno(String serviceno)
	{
		this.serviceno = serviceno;
	}

	public String getServiceinfo()
	{
		return serviceinfo;
	}

	public void setServiceinfo(String serviceinfo)
	{
		this.serviceinfo = serviceinfo;
	}

	 

 

}