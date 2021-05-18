package com.montnets.emp.rms.wbs.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.wbs.model.SvcCont;

/**
 * ParseXML此类完成SOAP请求报文的解析任务
 * 
 * @ClassName ParseXML
 * @Description TODO
 * @author zhouxiangxian 203492752@qq.com
 * @date 2018年1月11日
 */
public class ParseXML {
	// 使用单例模式
	private static ParseXML parseXml = new ParseXML();

	private ParseXML() {
	}

	public static synchronized ParseXML getInstance() {
		if (null == parseXml) {
			return new ParseXML();
		}
		return parseXml;
	}

	/**
	 * 
	 * 解析xml数据，将解析出的数据包装到List<SvcCont>中
	 */
	@SuppressWarnings("unchecked")
	public List<SvcCont> parseXmlToModel(String xml) {
		List<SvcCont> list = new ArrayList<SvcCont>();
		Long startTime = StringUtils.getLongTime();// 获取时间
		try {
			
			EmpExecutionContext.info("接收到报文：" + xml);
			Document doc = DocumentHelper.parseText(xml);
			Element service = doc.getRootElement();
			Iterator<Element> bodys = service.elementIterator("class");// 获取class元素集合的迭代器
			boolean dangerFlag = false;
			while (bodys.hasNext()) {
				SvcCont cont = new SvcCont();// 实例化对象，用户包装数据
				Element body = bodys.next();
				//攻击内容判断，如果传递过来的class属性resultcode和resultmsg有值，认为是攻击内容，则将dangerFlag设置为true;若无内容则dangerFlag默认为false
				String resultCode = body.attributeValue("resultcode");
				String resultMsg = body.attribute("resultmsg").getText();
				if (resultCode != null && resultCode.length() > 0 || resultMsg != null && resultMsg.length() > 0) {
					dangerFlag = true;//设置报警危险标记
				}
				// 业务报文,进行解析
				Iterator<Element> datas = body.elementIterator("field");// 获取field元素的迭代器
				while (datas.hasNext()) {
					Element data = datas.next();
					// 取出column字段名称
					String column = data.attribute("column").getText();
					// 取出column字段内容
					String text = data.getText();
					try {
						// 将数据解析出，为属性赋值
						setObjVal(cont, column, text);
					} catch (IllegalArgumentException e) {
						EmpExecutionContext.error(e, "反射写入实体类参数报错,xml:" + xml);
						// 解析出错
						cont.setResultCode(1);
						break;
					} catch (IllegalAccessException e) {
						EmpExecutionContext.error(e, "反射写入实体类报错,xml:" + xml);
						// 解析出错
						cont.setResultCode(1);
						break;
					}
				}
				// 将解析出的数据添加到list中
				list.add(cont);
				if(dangerFlag){
					list.clear();//出现攻击报文，清空list
				}
			}
		} catch (DocumentException e) {
			EmpExecutionContext.error(e, "dom4j解析xml文档异常,xml:" + xml);
			// 将list集合清空
			list.clear();
		}
		Long endTime = StringUtils.getLongTime();
		// 解析话费的时间
		EmpExecutionContext.info("解析xml耗时" + (endTime - startTime) + "ms");
		// 将解析的内容进行返回
		return list;
	}

	/**
	 * 反射赋值处理
	 */
	private static void setObjVal(Object obj, String field, String text)
			throws IllegalArgumentException, IllegalAccessException {
		// 获取Field[]数据对象
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field f : fields) {
			if (field.equalsIgnoreCase(f.getName())) {
				f.setAccessible(true);// 设置可访问
				f.set(obj, text);
			}
		}
	}

	/**
	 * 根据插入数据的结果返回响应报文
	 */
	public String getSvcContXml(String xml, List<Integer> errorLocation) {
		// 实例化StringBuffer
		StringBuffer result = new StringBuffer();
		// 拼凑响应报文的格式，准备添加resultcode="x"
		String[] str = xml.split("resultcode=\"\"");
		for (int i = 0; i < str.length; i++) {
			if (errorLocation.contains(i)) {
				// 在执行错误的地方添加错误的标记
				result.append(str[i]).append("resultcode=\"1\"");
			} else {
				// 执行正确则添加正确标记
				result.append(str[i]).append("resultcode=\"0\"");
			}
		}
		xml = result.toString().substring(0, result.length() - 14);
		// 准备拼凑resultmsg
		String[] strmsg = xml.split("resultmsg=\"\"");
		result = result.delete(0, result.length());// 清空内容
		for (int i = 0; i < strmsg.length; i++) {
			if (errorLocation.contains(i)) {
				// 在执行错误的地方添加错误的标记
				result.append(strmsg[i]).append("resultmsg=\"失败\"");
			} else {
				// 执行正确则添加正确标记
				result.append(strmsg[i]).append("resultmsg=\"成功\"");
			}
		}
		// 将最后多余的resultmsg="y"删除
		return result.toString().substring(0, result.length() - 14);
	}

	/**
	 * 
	 * 生成所有请求为失败的处理
	 */
	public String getAllErrorXml(String xml) {
		StringBuffer result = new StringBuffer();
		// 将请求报文进行分割，并添加resultcode=1返回
		String[] str = xml.split("resultcode=\"\"");
		for (int i = 0; i < str.length; i++) {
			// 添加错误标记
			result.append(str[i]).append("resultcode=\"1\"");
		}
		// 生成resultmsg信息
		xml = result.toString().substring(0, result.length() - 14);
		// 准备拼凑resultmsg
		String[] strmsg = xml.split("resultmsg=\"\"");
		result = result.delete(0, result.length());// 清空内容
		for (int i = 0; i < strmsg.length; i++) {
			// 给每一条消息添加失败说明
			result.append(strmsg[i]).append("resultmsg=\"失败\"");
		}
		// 将最后多余的resultmsg="y"删除
		return result.toString().substring(0, result.length() - 14);
	}

}
