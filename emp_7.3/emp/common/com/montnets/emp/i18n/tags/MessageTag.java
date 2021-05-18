package com.montnets.emp.i18n.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.montnets.emp.i18n.util.MessageUtils;

public class MessageTag extends SimpleTagSupport {
	// 自定义标签属性，key值
	private String key;
	// 自定义标签属性，标签描述
	private String fileName;
	private String defVal;

	/**
	 * 处理jsp页面自定义标签，将页面的标签对应的国际化值替换到页面中
	 */
	@Override
	public void doTag() throws JspException, IOException {
		// 使用标签属性值
		if (key != null) {
			JspWriter out = getJspContext().getOut();
			PageContext pageContext = (PageContext) getJspContext();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			String msg = MessageUtils.extractMessage(fileName, key, request);
			if (null != msg) {
				out.print(filter(msg));
			} else {
				out.print(defVal);
			}
		}
	}

	/**
	 * 过滤html、js脚本
	 * @param str
	 * @return
	 */
	private static String filter(String str){
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setDefVal(String defVal) {
		this.defVal = defVal;
	}

}
