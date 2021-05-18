package com.montnets.emp.rms.rmsapi.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * 上传模板参数实体类
 * @author chenly
 *
 */
public class UploadTempParams implements Serializable
{
	private static final long serialVersionUID = -1524282214824814095L;
	
	private String userid;
	private String pwd;
	private String apikey;
	private String timestamp;
	private String sign;
	private List<TempParams> content;
	private String smcontent;
	private String smparamreg;
	private String title;
	private String tmplver;
	private String origin;



	public UploadTempParams() {
		super();
		timestamp=new SimpleDateFormat("MMDDHHmmss").format(new Date());
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
	public String getApikey() {
		return apikey;
	}
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public List<TempParams> getContent() {
		return content;
	}
	public void setContent(List<TempParams> content) {
		this.content = content;
	}

	public String getSmcontent() {
		return smcontent;
	}

	public void setSmcontent(String smcontent) {
		this.smcontent = smcontent;
	}

	public String getSmparamreg() {
		return smparamreg;
	}

	public void setSmparamreg(String smparamreg) {
		this.smparamreg = smparamreg;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTmplver() {
		return tmplver;
	}

	public void setTmplver(String tmplver) {
		this.tmplver = tmplver;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
}

