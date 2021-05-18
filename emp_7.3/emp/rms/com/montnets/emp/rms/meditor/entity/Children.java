package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.util.List;


/**
 * @author xuty
 * @version V1.0
 * @ClassName: Children
 * @Description: 卡片组件类
 * @date 2018-7-26 上午11:45:34
 */
public class Children implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 9157578000933988399L;
    //组件类型
    private String type;
    //组件tag
    private String tag;
    //组件属性
    private Attr attr;
    //组件布局
    private Layout layout;
    //按钮
    private Button event;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
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

	public Button getEvent() {
		return event;
	}

	public void setEvent(Button event) {
		this.event = event;
	}
	
}
