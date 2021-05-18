/**
 * 
 */
package com.montnets.emp.entity.corp;

/**
 * @project sinolife
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-16 03:59:02
 * @description 
 */

public class LfCorpConf implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9178965451703257968L;

	private Long ccId;
	
	//企业编码
	private String corpCode;
	//
	private String paramKey;
	//
	private String paramValue;
	
	
	
	public LfCorpConf(){}



	public Long getCcId() {
		return ccId;
	}



	public void setCcId(Long ccId) {
		this.ccId = ccId;
	}



	public String getCorpCode() {
		return corpCode;
	}



	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}



	public String getParamKey() {
		return paramKey;
	}



	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}



	public String getParamValue() {
		return paramValue;
	}



	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

}
