package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 模板动态参数类
 * @author moll
 *
 */
public class TempParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -292353160521120099L;
	 //跟据不同的组件，获取不一样的数据
	private String tag;
	//html格式富文本中的动态参数数据
	private List<HashMap<String,String>>args;
	//组件的内容，组件类型为text button时，内容为文本；组件类型为image、qr 时，内容为url地址	
	private String content;
	
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
