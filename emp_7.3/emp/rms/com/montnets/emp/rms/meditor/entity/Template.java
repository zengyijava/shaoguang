package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.util.List;

/**
 *@ClassName: Tempalte
 * @Description: 卡片模板主体类
 * @author xuty
 * @date 2018-7-26 上午11:46:01
 * @version V1.0
 */
public class Template implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1706729231023483238L;
	//frame组件类型，用于描述模板结构
	private String type;
	//卡片属性
	private Attr attr;
	//卡片布局
	private Layout layout;
	//HFIVE专用
	private Event event;
	//模板组件
	private List<Children> children;

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Attr getAttr() {
		return attr;
	}

	public void setAttr(Attr attr) {
		this.attr = attr;
	}

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public List<Children> getChildren() {
		return children;
	}

	public void setChildren(List<Children> children) {
		this.children = children;
	}


}
