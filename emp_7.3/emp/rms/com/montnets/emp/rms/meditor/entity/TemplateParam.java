package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/***
 * 发送参数类(终端)
 * @author dell
 *
 */
public class TemplateParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8261276634625139958L;
	//组件ID
	private String tag;
	//html格式富文本中的动态参数数据
	private List<HashMap<String,String>> args;
	//卡片参数
	private String content;
	//事件
	private Button event;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public List<HashMap<String, String>> getArgs() {
		return args;
	}

	public void setArgs(List<HashMap<String, String>> args) {
		this.args = args;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Button getEvent() {
		return event;
	}

	public void setEvent(Button event) {
		this.event = event;
	}
}
