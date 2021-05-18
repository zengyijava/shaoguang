package com.montnets.emp.rms.tools;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * @author Jason Huang
 * @date 2018年3月28日 下午2:30:14
 * @description 报文处理工具类
 */

public class MessageTool {
	public String buildChartMessage(JSONObject jsonObj, String imgName) {
		StringBuilder builder = new StringBuilder("{");
		try {
			builder.append("\"fina\":").append("\"").append(imgName).append("\",");
			builder.append("\"type\":").append("\"").append(jsonObj.getInteger("chartType")).append("\",");
			builder.append("\"picsize\":").append("\"-1\",");
			builder.append("\"qualy\":").append("\"100\",");
			if (jsonObj.getInteger("ptType") == 2) {// 数值动态类型
				if (jsonObj.getInteger("chartType") == 1) {// 饼状图类型,没有行标题
					builder.append("\"colattr\":").append("\"-1\",");
					builder.append("\"colinfo\":").append("\"-1\",");
				} else {// 暂时假设除饼状图以外的其他类型都有列标题
					builder.append("\"colattr\":").append("\"0\",");
					builder.append("\"colinfo\":\"");
					String[] colInfos = jsonObj.getString("barColName").split(",");
					for (int j = 0; j < colInfos.length; j++) {
						builder.append(new String(Base64.encodeBase64(colInfos[j].getBytes("utf-8"))));
						if (j < colInfos.length - 1) {
							builder.append(",");
						}
					}
					builder.append("\",");
				}
				builder.append("\"rowattr\":").append("\"0\",");
				builder.append("\"rowinfo\":\"");
				String[] rowInfos = jsonObj.getString("barRowName").split(",");
				for (int j = 0; j < rowInfos.length; j++) {
					builder.append(new String(Base64.encodeBase64(rowInfos[j].getBytes("utf-8"))));
					if (j < rowInfos.length - 1) {
						builder.append(",");
					}
				}
				builder.append("\",");
			} else if (jsonObj.getInteger("ptType") == 3) {// 全动态类型
				builder.append("\"rowattr\":").append("\"1\",");
				builder.append("\"colattr\":").append("\"1\",");
			}

			builder.append("\"tiinfo\":").append("\"{")
					.append(new String(Base64.encodeBase64(replaceParam(jsonObj.getString("chartTitle").replace("{#标题#}", "#P_1#")).getBytes("utf-8"))))
					.append(",-1,-1,-1,-1,-1,-1,-1,-1}\",");
			builder.append("\"rownas\":\"").append(jsonObj.getInteger("rowNum"))
					.append(",-1,-1,-1,-1,-1,-1,-1,-1,-1\",");
			builder.append("\"colnas\":\"").append(jsonObj.getInteger("colNum")).append(",-1,-1,-1,-1,-1,-1,-1,-1\",");
			builder.append("\"dataunit\":").append("\"-1\"");
			if (jsonObj.getInteger("chartType") == 1) {
				builder.append(",\"datacor\":").append("\"").append(jsonObj.getString("color")).append("\"");
			}
			builder.append("}");
		} catch (UnsupportedEncodingException e) {
			EmpExecutionContext.error(e, "构建动态图表Json,Base64转化异常!");
		}
		// System.out.println(builder);
		return builder.toString();
	}

	public String buildFontMessage(List<Map<String, String>> list, String imgName) {
		StringBuilder builder = new StringBuilder("{");
		builder.append("\"fina\":").append("\"").append(imgName).append("\",");
		builder.append("\"parcts\":").append("\"").append(list.size()).append("\",");
		builder.append("\"qualy\":").append("\"-1\",");
		builder.append("\"ctrinfos\":").append("[");
		for (int i = 0; i < list.size(); i++) {
			builder.append(buildParagraphJson(list.get(i)));
			if (i < list.size() - 1) {
				builder.append(",");
			}
		}
		builder.append("]}");
		return builder.toString();
	}

	private String buildParagraphJson(Map<String, String> map) {
		String content64 = null;
		try {
			content64 = new String(Base64.encodeBase64(map.get("content").getBytes("utf-8")));
		} catch (UnsupportedEncodingException e) {
			EmpExecutionContext.error(e, "构建动态图文Json,Base64转化异常!");
		}
		StringBuilder builder = new StringBuilder("{\"ctrinfo\":\"");
		builder.append(content64);
		builder.append("-").append(map.get("left")).append("*").append(map.get("top"));
		builder.append("-").append(map.get("size").substring(0, map.get("size").indexOf("x"))).append("*0");
		builder.append("-").append(map.get("face"));
		builder.append("-").append(map.get("foreground"));
		builder.append("-").append(map.get("background"));
		builder.append("-").append(map.get("font").substring(0, map.get("font").indexOf("px")));
		if (map.containsKey("weight")) {
			builder.append("-").append(1);
		} else {
			builder.append("-").append(0);
		}
		if (map.get("gravity").equals("left")) {
			builder.append("-").append(0);
		} else if (map.get("gravity").equals("center")) {
			builder.append("-").append(1);
		} else {
			builder.append("-").append(2);
		}
		builder.append("-").append(map.get("rotate"));
		builder.append("\"}");
		return builder.toString();
	}
	
	/**
	 * 替换参数字符串
	 * @param str 替换前的字符串
	 * @return 替换后字符串
	 */
	private String replaceParam(String str) {
		String regex = "\\{#参数\\d+#\\}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			String no = str.substring(matcher.start() + 4, matcher.end() - 2);
			str = str.replaceFirst(regex, "#P_" + no + "#");
			matcher = pattern.matcher(str);
		}
		return str;
	}

}
