package com.montnets.emp.rms.rmstask.biz;

public class ReplyParams {
	
	//手机号
	private String phone = "";
	
	//姓名
	private String name = "";
	
	//回复内容
	private String content = "";
	
	//回复时间
	private String time = "";

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
