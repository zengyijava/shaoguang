/**
 * 
 */
package com.montnets.emp.entity.gateway;

/**
 * @project montnets_gateway
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-3 下午05:59:34
 * @description 网关参数配置表
 */

public class AgwParamConf implements java.io.Serializable
{


	/**
	 * 
	 */
	private static final long serialVersionUID = 886772103696017342L;

	private String paramItem;
	
	private String paramName;
	
	private Integer paramAttribute;
	
	private String paramMemo;
	
	private String defaultValue;
	
	private String valueRange;
	
	private Integer controltyp;
	
	private Integer gwType;
	
	private String HKParamName;
	
	private String HKParamMemo;
	
	private String ENParamName;
	
	private String ENParamMemo;
	
	public AgwParamConf(){
 	}

	public String getParamItem()
	{
		return paramItem;
	}

	public void setParamItem(String paramItem)
	{
		this.paramItem = paramItem;
	}

	public String getParamName()
	{
		return paramName;
	}

	public void setParamName(String paramName)
	{
		this.paramName = paramName;
	}

	public Integer getParamAttribute()
	{
		return paramAttribute;
	}

	public void setParamAttribute(Integer paramAttribute)
	{
		this.paramAttribute = paramAttribute;
	}

	public String getParamMemo()
	{
		return paramMemo;
	}

	public void setParamMemo(String paramMemo)
	{
		
		this.paramMemo = paramMemo;
	}

	public String getDefaultValue()
	{
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue)
	{
		if(defaultValue==null || "".equals(defaultValue))
		{
			defaultValue = " ";
		}
		this.defaultValue = defaultValue;
	}
	
	public String getValueRange()
	{
		
		return valueRange;
	}

	public void setValueRange(String valueRange)
	{
		if(valueRange==null || "".equals(valueRange))
		{
			valueRange = " ";
		}
		this.valueRange = valueRange;
	}

	public Integer getControltyp()
	{
		return controltyp;
	}

	public void setControltyp(Integer controltyp)
	{
		this.controltyp = controltyp;
	}

	public Integer getGwType() {
		return gwType;
	}

	public void setGwType(Integer gwType) {
		this.gwType = gwType;
	}

	public String getHKParamName() {
		return HKParamName;
	}

	public void setHKParamName(String hKParamName) {
		HKParamName = hKParamName;
	}

	public String getHKParamMemo() {
		return HKParamMemo;
	}

	public void setHKParamMemo(String hKParamMemo) {
		HKParamMemo = hKParamMemo;
	}

	public String getENParamName() {
		return ENParamName;
	}

	public void setENParamName(String eNParamName) {
		ENParamName = eNParamName;
	}

	public String getENParamMemo() {
		return ENParamMemo;
	}

	public void setENParamMemo(String eNParamMemo) {
		ENParamMemo = eNParamMemo;
	}
	
	
}
