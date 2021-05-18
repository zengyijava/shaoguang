/**
 * 
 */
package com.montnets.emp.entity.system;

/**
 * 业务流程实体类
 * 
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-16 上午08:56:10
 * @description 业务流程实体类
 */

public class LfBusProcess implements java.io.Serializable {

	/**
	 * */
	private static final long serialVersionUID = 5878282477836745574L;
	//业务流程ID（自增） 
	private Long busProId;
	//业务编码
	private String busCode;
	//菜单编码
	private String menuCode;
	//类名
	private String className;
	//注册类型（0-mo上行；1-rpt报告）
	private Integer regType;
	//编码
	private String codes;
	//编码类型（0-模块编码；1-业务编码；2-产品编码;3-机构编码；4-操作员编码）
	private Integer codeType;
	//
	private String httpUrl;
	//发送类型
	private Integer sendType;

	public LfBusProcess() {
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Long getBusProId() {
		return busProId;
	}

	public void setBusProId(Long busProId) {
		this.busProId = busProId;
	}

	public String getBusCode() {
		return busCode;
	}

	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getRegType() {
		return regType;
	}

	public void setRegType(Integer regType) {
		this.regType = regType;
	}

	public String getCodes() {
		return codes;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}

	public Integer getCodeType() {
		return codeType;
	}

	public void setCodeType(Integer codeType) {
		this.codeType = codeType;
	}

}
