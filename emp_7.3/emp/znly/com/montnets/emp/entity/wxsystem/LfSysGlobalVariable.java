/**
 * 
 */
package com.montnets.emp.entity.wxsystem;

/**
 * 全局变量表
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-2 下午06:33:56
 * @description
 */

public class LfSysGlobalVariable implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4407570907890256982L;
	//自增ID
	private Integer globalId;
	//KEY键
	private String globalKey;
	//VALUE值
	private Long globalValue;
	//字符串值
 	private String globalStrValue;
 	
	public LfSysGlobalVariable()
	{
	}

	public Integer getGlobalId()
	{
		return globalId;
	}

	public void setGlobalId(Integer globalId)
	{
		this.globalId = globalId;
	}

	public String getGlobalKey()
	{
		return globalKey;
	}

	public void setGlobalKey(String globalKey)
	{
		this.globalKey = globalKey;
	}

	public Long getGlobalValue()
	{
		return globalValue;
	}

	public void setGlobalValue(Long globalValue)
	{
		this.globalValue = globalValue;
	}

	public String getGlobalStrValue() {
		return globalStrValue;
	}

	public void setGlobalStrValue(String globalStrValue) {
		this.globalStrValue = globalStrValue;
	}

 
}
