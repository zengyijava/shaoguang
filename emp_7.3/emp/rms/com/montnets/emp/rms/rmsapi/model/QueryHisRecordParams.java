package com.montnets.emp.rms.rmsapi.model;

import java.io.Serializable;

/**
 * 查询富信历史记录请求参数实体类
 * @author chenly
 *
 */
public class QueryHisRecordParams implements Serializable{
	
	private static final long serialVersionUID = -6068220385454292254L;
	//sp账号(必填)
	private String userid;
//	//密码
//	private String pwd;
	//手机号码(选填)
	private String phone;
	//模板ID(选填)
	private Long tmplid;
	//运营商  0：移动,1：联通,21：电信,5国外，不选则-1：全部
	private Integer mobiletype;
	//富信档位 1：1档，2：2档，3：3档，4：4档，不能超过255，不填则是-1
	private Integer chgrade;
	//发送状态（选填） 0.接收成功(DELIVRD) ，1.失败，-1.全部
	private Integer errcode;
	//开始时间(必填)
	private String starttime;
	//结束时间(必填)
	private String endtime;
	//分页条数(必填)
	private Integer pagesize;
	//当前页数(必填)
	private Integer pageindex;
	//时间戳
	private String timestamp;
	//身份令牌
	private String token;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Long getTmplid() {
		return tmplid;
	}
	public void setTmplid(Long tmplid) {
		this.tmplid = tmplid;
	}
	public Integer getMobiletype() {
		return mobiletype;
	}
	public void setMobiletype(Integer mobiletype) {
		this.mobiletype = mobiletype;
	}
	public Integer getChgrade() {
		return chgrade;
	}
	public void setChgrade(Integer chgrade) {
		this.chgrade = chgrade;
	}
	public Integer getErrcode() {
		return errcode;
	}
	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public Integer getPagesize() {
		return pagesize;
	}
	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}
	public Integer getPageindex() {
		return pageindex;
	}
	public void setPageindex(Integer pageindex) {
		this.pageindex = pageindex;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	/*public String getPwd()
	{
		return pwd;
	}
	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}
	*/
	
	
}
