package com.montnets.emp.rms.rmsapi.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PackageHeader implements Serializable
{
	
	private static final long serialVersionUID = 1969102494732480912L;
	//用户账号
	private String userid;
	//用户密码
	private String pwd;
	//用户唯一标识
	private String apikey;
	//时间戳,24小时格式：MMDDHHmmss
	private String timestamp;
	//模板id
	private String tmplid;

	public PackageHeader()
	{
		super();
		timestamp=new SimpleDateFormat("MMddHHmmss").format(new Date());
	}
	
	public String getUserid()
	{
		return userid;
	}
	
	public void setUserid(String userid)
	{
		this.userid = userid;
	}
	
	public String getPwd()
	{
		return pwd;
	}
	
	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getTmplid() {
		return tmplid;
	}

	public void setTmplid(String tmplid) {
		this.tmplid = tmplid;
	}

	public String getApikey()
	{
		return apikey;
	}

	public void setApikey(String apikey)
	{
		this.apikey = apikey;
	}
	
}
