package com.montnets.emp.rms.meditor.entity;

import java.util.List;

public class TemplateTotalParam {
	//主题
	private String subject;
	
	private List<TemplateParam> param;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<TemplateParam> getParam() {
		return param;
	}

	public void setParam(List<TemplateParam> param) {
		this.param = param;
	}
	
}
