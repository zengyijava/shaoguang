package com.montnets.emp.sysuser.util;

import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * @Title: XmlParser.java
 * @Package com.montnets.emp.msgcenter.util.decode
 * @Description: 报文解析类
 * @author xial
 * @date 2012-6-28 下午2:16:41
 * @version V1.0
 */
public class XmlParser  {

	public Document getDocument(String xmlStr) {
		StringReader sr = new StringReader(xmlStr);
		InputSource is = new InputSource(sr);
		SAXBuilder sb = new SAXBuilder();
		Document d = null;
		try {
			d = sb.build(is);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "构建xml文档失败！");
		}
		return d;
	}

	public String parseXMLStr(Document document, String name) {
		String value = "";
		try {
			Element element = document.getRootElement();
			String[] tags = name.split("/");
			for (int i = 0; i < tags.length; i++) {
				element = element.getChild(tags[i]);
			}
			value = element.getValue();
		} catch (Exception exception) {
			EmpExecutionContext.error(exception,"解析xml文件失败！");
		}
		return value;
	}

	public String parseXMLStr(Document d, String tag, String subTag, int index) {
		String value = "";
		try {
			int lastIndex = tag.lastIndexOf("/");
			String parentName = tag.substring(0, lastIndex);
			String name = tag.substring(lastIndex + 1);
			Element e = (Element) getElement(d, parentName).getChildren(name).get(index);
			String[] tags = subTag.split("/");
			for (int i = 0; i < tags.length; i++) {
				e = e.getChild(tags[i]);
			}
			value = e.getValue();
		} catch (Exception exception) {
			EmpExecutionContext.error(exception, "解析xml文件失败！");
		}
		return value;
	}

	public int getElementCount(Document d, String tag) {
		int size = 0;
		try {
			int lastIndex = tag.lastIndexOf("/");
			String parentName = tag.substring(0, lastIndex);
			String name = tag.substring(lastIndex + 1);
			size = getElement(d, parentName).getChildren(name).size();
		} catch (Exception e) {
			size = 0;
			EmpExecutionContext.error(e, "解析xml文件失败！");
		}
		return size;
	}

	public String parseXMLStr(Document d, String name, int index) {
		String value = "";
		try {
			Element e = d.getRootElement();
			String[] tags = name.split("/");
			for (int i = 0; i < tags.length; i++) {
				e = e.getChild(tags[i]);
			}
			value = e.getValue();
		} catch (Exception exception) {
			EmpExecutionContext.error(exception, "解析xml文件失败！");
		}
		return value;
	}

	private Element getElement(Document d, String name) {
		Element e = d.getRootElement();
		String[] tags = name.split("/");
		for (int i = 0; i < tags.length; i++) {
			e = e.getChild(tags[i]);
		}
		return e;
	}

	public String parseXMLStr(String xmlStr, String name) {
		String value = "";
		StringReader sr = new StringReader(xmlStr);
		InputSource is = new InputSource(sr);
		SAXBuilder sb = new SAXBuilder();
		try {
			Document d = sb.build(is);
			Element e = d.getRootElement();
			String[] tags = name.split("/");

			for (int i = 0; i < tags.length; i++) {
				e = e.getChild(tags[i]);
			}
			value = e.getValue();
		} catch (Exception e) {
			EmpExecutionContext.error(e, "解析xml文件失败！");
		}
		return value;
	}

	public String getTagBetweenStr(String xmlStr, String tag) {
		String beginTag = "<" + tag + ">";
		int beginIndex = xmlStr.indexOf(beginTag);
		int endIndex = xmlStr.indexOf("</" + tag + ">");
		String result = xmlStr.substring(beginIndex + beginTag.length(), endIndex);
		return result;
	}

	public String removeTagBetweenStr(String xmlStr, String tag) {
		String beginTag = "<" + tag + ">";
		String endTag = "</" + tag + ">";
		int beginIndex = xmlStr.lastIndexOf(beginTag);
		int endIndex = xmlStr.lastIndexOf("</" + tag + ">");
		StringBuffer buffer = new StringBuffer(xmlStr);
		buffer.delete(beginIndex, endIndex + endTag.length());
		return buffer.toString();
	}
}
