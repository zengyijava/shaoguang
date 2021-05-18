/**
 * 
 */
package com.montnets.emp.entity.gateway;

/**
 * @project montnets_gateway
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-3 下午03:16:40
 * @description 网关运行参数表
 */

public class AgwParamValue implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5727474857528125155L;

	//网关编号
	private Integer gwNo;
	
	//网关类型
	private Integer gwType;
	
	//参数项
	private String paramItem;
	
	//参数值
	private String paramValue;
	
	public AgwParamValue()
	{
		gwNo =  new Integer(99);
		gwType =  new Integer(4000);
		paramItem = "PARAMITEM";
		paramValue = "PARAMVALUE";
	}

	public Integer getGwNo()
	{
		return gwNo;
	}

	public void setGwNo(Integer gwNo)
	{
		this.gwNo = gwNo;
	}

	public Integer getGwType()
	{
		return gwType;
	}

	public void setGwType(Integer gwType)
	{
		this.gwType = gwType;
	}

	public String getParamItem()
	{
		return paramItem;
	}

	public void setParamItem(String paramItem)
	{
		this.paramItem = paramItem;
	}

	public String getParamValue()
	{
		return paramValue;
	}

	public void setParamValue(String paramValue)
	{
		if(paramValue==null || "".equals(paramValue))
		{
			paramValue="PARAMVALUE'";
		}
		
		this.paramValue = paramValue;
	}
	
	
}
