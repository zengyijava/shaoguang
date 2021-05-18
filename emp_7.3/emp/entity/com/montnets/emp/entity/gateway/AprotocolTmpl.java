/**
 * 
 */
package com.montnets.emp.entity.gateway;

/**
 * @project montnets_gateway
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-3 下午06:56:45
 * @description 
 */

public class AprotocolTmpl implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5462311675692158082L;

	private Integer protocolCode;
	
	private String protocol;
	
	private String protocolParam;
	
	public AprotocolTmpl(){}

	public Integer getProtocolCode()
	{
		return protocolCode;
	}

	public void setProtocolCode(Integer protocolCode)
	{
		this.protocolCode = protocolCode;
	}

	public String getProtocol()
	{
		return protocol;
	}

	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	public String getProtocolParam()
	{
		return protocolParam;
	}

	public void setProtocolParam(String protocolParam)
	{
		this.protocolParam = protocolParam;
	}
	
	
}
