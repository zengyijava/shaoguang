package com.montnets.emp.i18n.tags;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ForEachTag extends SimpleTagSupport {
	//自定义标签属性，迭代器
	private String var;
	private Collection<Object> collection;

	@SuppressWarnings("unchecked")
	public void setItems(Object items) { 
		if (items instanceof Collection) {
			collection = (Collection<Object>) items;
		}
		if (items instanceof Map) {
			@SuppressWarnings("rawtypes")
			Map map = (Map) items;
			collection = map.entrySet();
		}
		if (items.getClass().isArray()) {
			this.collection = new ArrayList<Object>();
			int length = Array.getLength(items);
			for (int i = 0; i < length; i++) {
				Object value = Array.get(items, i);
				collection.add(value);
			}
		}
	}

	/**
	 * 处理jsp页面自定义标签，将页面的标签对应的国际化值替换到页面中
	 */
	@Override
	public void doTag() throws JspException, IOException {
		Iterator<Object> it = this.collection.iterator();
		while (it.hasNext()) {
			Object value = it.next();
			this.getJspContext().setAttribute(var, value);
			this.getJspBody().invoke(null);
		}
	}
	public void setVar(String var) {
		this.var = var;
	}
}
