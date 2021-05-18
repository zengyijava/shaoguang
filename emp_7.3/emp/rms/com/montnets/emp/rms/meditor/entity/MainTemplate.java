package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;

/**
 * 模板主体类(终端)
 * @author dell
 *
 */
public class MainTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5280433711067905229L;
	//结构ID，用于标识模板的结构是否一致
	private String sid;
	// 消息的类型(手机端不需要)   1.普通卡片 2.html格式富文本
	private String temptype;
	//模板结构
	private Template template;


	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getTemptype() {
		return temptype;
	}

	public void setTemptype(String temptype) {
		this.temptype = temptype;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}
	
}
