package com.montnets.emp.common.constant;

import java.sql.Connection;

/**获取尾号方法getSubnoDetail对应的参数类
 * @author Administrator
 *
 */
public class SMParams 
{
	//发送账号
	private String spUserid;
	//编码-------------------------(必填)
	private String codes;
	//编码类型(0模块编码;1业务编码;2产品编码;3机构id;4操作员guid;5任务id)-------------------(必填)
	private Integer codeType;
	//企业编码----------------------------(必填)
	private String corpCode;
	//分配类型(0固定;1自动)，为null表示固定尾号-------------------(必填)
	private Integer allotType;
	//数据库连接，事务用，可以为null
	private Connection conn;
	//尾号是否有效，true表示有效，false表示无效(无效尾号建立后设置有效期为1个小时，过期删除)默认为true
	private boolean subnoVali=true;
	//模块编号
	private String menuCode;
	//业务编码
	private String busCode;
	//机构ID
	private Long depId;
	//操作员guid
	private String loginId;
	//任务Id
	private Long taskId;
	
	//获取发送账户
	public String getSpUserid() {
		return spUserid;
	}

	public void setSpUserid(String spUserid) {
		this.spUserid = spUserid;
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
    //获取企业编码
	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public Integer getAllotType() {
		return allotType;
	}

	public void setAllotType(Integer allotType) {
		this.allotType = allotType;
	}
    //获取连接
	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public boolean isSubnoVali() {
		return subnoVali;
	}
    //获取尾号 
	public void setSubnoVali(boolean subnoVali) {
		this.subnoVali = subnoVali;
	}
	
	public boolean getSubnoVali()
	{
		return this.subnoVali;
	}
    //获取模块编码
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
    //获取模块业务类型
	public String getBusCode() {
		return busCode;
	}

	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
    //获取机构id
	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}
    //获取登录用户
	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
    //获取任务id 
	public Long  getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	
	
}
