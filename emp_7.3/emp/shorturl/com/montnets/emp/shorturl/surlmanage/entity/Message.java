package com.montnets.emp.shorturl.surlmanage.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.montnets.emp.shorturl.comm.constant.UrlGlobals;

public class Message {
	// 用户账号
	private String	userid;
	
	// 用户密码
	private String	pwd;
	
	// 时间戳,格式为:MMDDHHMMSS,即月日时分秒,定长10位,月日时分秒不足2位时左补0.时间戳请获取您真实的服务器时间,不要填写固定的时间,否则pwd参数起不到加密作用
	private String	timestamp;
	
	private String apikey;
	
	
	//长地址
	private String longaddr;
	
	//1 启用、2 禁用
	private String status;

	//短地址域名
	private String sadomain;
	//手机号
	private String mobile;
	//有效天数
	private Integer validday;
	//批次号
	private String custid;
	//业务类型
	private String svrtype;
	//扩展字段
	private String exdata;
	
	
	public String getSadomain() {
		return sadomain;
	}

	public void setSadomain(String sadomain) {
		this.sadomain = sadomain;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getValidday() {
		return validday;
	}

	public void setValidday(Integer validday) {
		this.validday = validday;
	}

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getSvrtype() {
		return svrtype;
	}

	public void setSvrtype(String svrtype) {
		this.svrtype = svrtype;
	}

	public String getExdata() {
		return exdata;
	}

	public void setExdata(String exdata) {
		this.exdata = exdata;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getLongaddr() {
		return longaddr;
	}

	public void setLongaddr(String longaddr) {
		this.longaddr = longaddr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		String json ="";
		if (UrlGlobals.getIsExdataEncode() ==0){
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			json =gson.toJson(this);
		}else {
			Gson gson = new Gson();
			json =gson.toJson(this);
		}

		return json;
	}
}
