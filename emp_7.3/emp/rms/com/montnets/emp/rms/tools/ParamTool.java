package com.montnets.emp.rms.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.rms.vo.Param;

/**
 * @author Jason Huang
 * @date 2018年4月3日 上午9:41:41
 */

public class ParamTool {

	public List<List<Param>> convertParam(String html) {
		Document doc = Jsoup.parseBodyFragment(html);
		// 获取每一帧的Element,"J-keyframe"是帧的分割标志
		Elements eles = doc.body().getElementsByClass("J-keyframe");
		List<List<Param>> frameList = new ArrayList<List<Param>>();

		for (Element ele : eles) {
			List<Param> paramList = new ArrayList<Param>();
			// 1、文本类型
			if (!ele.getElementsByAttributeValue("data-type", "text").isEmpty()) {
				if (ele.getElementsByClass("J-edit-text").text().trim().length() > 0) {
					findTxtParams(paramList, ele.getElementsByClass("J-edit-text").text().trim());
				}
				// 1.1、文本配图片
				if (!ele.getElementsByAttributeValue("data-type", "image").isEmpty()) {
					// 判断该图是否属于图文类型
					if (!ele.getElementsByClass("J-add-module").isEmpty()) {
						findImgParams(paramList, ele.getElementsByClass("J-module-html").text().trim());
					}
					// 1.2、文本配图表
				} else if (!ele.getElementsByAttributeValue("data-type", "chart").isEmpty()) {
					JSONObject jsonObj = JSONObject.parseObject(ele.getElementsByClass("J-chart-data").text());
					// 静态图表类型,直接当成图片处理,此处只处理动态图表
					if (jsonObj.getInteger("ptType") != 1) {
						Integer type = jsonObj.getInteger("chartType")+2;
						Integer dynamicType = jsonObj.getInteger("ptType");
						Integer colNum = jsonObj.getInteger("colNum");
						Integer rowNum = jsonObj.getInteger("rowNum");
						String color = jsonObj.getString("color");
						String[] chartParams = jsonObj.getString("parmValue").split(",");
						// 后期优化时将chartParams变量的遍历交给调用者，可减少type、dynamicType、colNum、rowNum的重复
						for (String chartParam : chartParams) {
							Param param = new Param();
							param.setType(type);
							param.setDynamicType(dynamicType);
							param.setColNum(colNum);
							param.setRowNum(rowNum);
							param.setColor(color);
							param.setValue(chartParam);
							paramList.add(param);
						}
					}else{//静态图表标题带参数
						Integer type = jsonObj.getInteger("chartType")+2;
						Integer dynamicType = jsonObj.getInteger("ptType");
						Integer colNum = jsonObj.getInteger("colNum");
						Integer rowNum = jsonObj.getInteger("rowNum");
						String color = jsonObj.getString("color");
						String[] chartParams = jsonObj.getString("parmValue").split(",");
						// 后期优化时将chartParams变量的遍历交给调用者，可减少type、dynamicType、colNum、rowNum的重复
						if(!"".equals(jsonObj.getString("parmValue").trim()) && chartParams.length > 0){
							for (String chartParam : chartParams) {
								Param param = new Param();
								param.setType(type);
								param.setDynamicType(dynamicType);
								param.setColNum(colNum);
								param.setRowNum(rowNum);
								param.setColor(color);
								param.setValue(chartParam);
								paramList.add(param);
							}
						}
					}
				}
				// 2、图片类型
			} else if (!ele.getElementsByAttributeValue("data-type", "image").isEmpty()) {
				if (!ele.getElementsByTag("img").isEmpty()) {
					// 判断该图是否属于图文类型
					if (!ele.getElementsByClass("J-add-module").isEmpty()) {
						findImgParams(paramList, ele.getElementsByClass("J-module-html").text().trim());
					}
				}
				// 2.1、图片配文本
				if (ele.getElementsByClass("J-edit-text").text().trim().length() > 0) {
					findTxtParams(paramList, ele.getElementsByClass("J-edit-text").text().trim());
				}
				// 3、图表类型
			} else if (!ele.getElementsByAttributeValue("data-type", "chart").isEmpty()) {
				JSONObject jsonObj = JSONObject.parseObject(ele.getElementsByClass("J-chart-data").text());
				if (!ele.getElementsByTag("img").isEmpty()) {
					// 静态图表类型,直接当成图片处理,此处只处理动态图表
					if (jsonObj.getInteger("ptType") != 1) {
						Integer type = jsonObj.getInteger("chartType")+2;
						Integer dynamicType = jsonObj.getInteger("ptType");
						Integer colNum = jsonObj.getInteger("colNum");
						Integer rowNum = jsonObj.getInteger("rowNum");
						String color = jsonObj.getString("color");
						String[] chartParams = jsonObj.getString("parmValue").split(",");
						// 后期优化时将chartParams变量的遍历交给调用者，可减少type、dynamicType、colNum、rowNum的重复
						for (String chartParam : chartParams) {
							Param param = new Param();
							param.setType(type);
							param.setDynamicType(dynamicType);
							param.setColNum(colNum);
							param.setRowNum(rowNum);
							param.setColor(color);
							param.setValue(chartParam);
							paramList.add(param);
						}
					}else{//静态图表标题带参数
						Integer type = jsonObj.getInteger("chartType")+2;
						Integer dynamicType = jsonObj.getInteger("ptType");
						Integer colNum = jsonObj.getInteger("colNum");
						Integer rowNum = jsonObj.getInteger("rowNum");
						String color = jsonObj.getString("color");
						String[] chartParams = jsonObj.getString("parmValue").split(",");
						// 后期优化时将chartParams变量的遍历交给调用者，可减少type、dynamicType、colNum、rowNum的重复
						if(!"".equals(jsonObj.getString("parmValue").trim()) && chartParams.length > 0){
							for (String chartParam : chartParams) {
								Param param = new Param();
								param.setType(type);
								param.setDynamicType(dynamicType);
								param.setColNum(colNum);
								param.setRowNum(rowNum);
								param.setColor(color);
								param.setValue(chartParam);
								paramList.add(param);
							}
						}
					}
				}
				// 3.1、图表配文
				if (ele.getElementsByClass("J-edit-text").text().trim().length() > 0) {
					Integer type = jsonObj.getInteger("chartType") + 2;
					Integer dynamicType = jsonObj.getInteger("ptType");
					Integer colNum = jsonObj.getInteger("colNum");
					Integer rowNum = jsonObj.getInteger("rowNum");
					String color = jsonObj.getString("color");
					Param param = new Param();
					param.setDynamicType(dynamicType);
					param.setColNum(colNum);
					param.setRowNum(rowNum);
					param.setColor(color);
					findTxtParams(paramList, ele.getElementsByClass("J-edit-text").text().trim(), param);
				}
			}
			frameList.add(paramList);
		}

		return frameList;
	}

	private void findTxtParams(List<Param> paramList, String str) {
		String regex = "\\{#参数\\d+#\\}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			Param param = new Param();
			param.setType(1);
			param.setValue(matcher.group());
			paramList.add(param);
		}
	}

	private void findTxtParams(List<Param> paramList, String str, Param param) {
		String regex = "\\{#参数\\d+#\\}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			param.setType(1);
			param.setValue(matcher.group());
			paramList.add(param);
		}
	}

	private void findImgParams(List<Param> paramList, String str) {
		String regex = "\\{#图参\\d+#\\}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			Param param = new Param();
			param.setType(2);
			param.setValue(matcher.group());
			paramList.add(param);
		}
	}

/*	public static void main(String[] args) {
		ParamTool paramTool = new ParamTool();
		List<List<Param>> frameList = paramTool
				.convertParam("<div class=\"editor-content J-editor-content\"><div class=\"editor-keyframe J-keyframe active\" data-type=\"chart\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-img J-editor-img\" data-type=\"chart\" data-size=\"14698\"><img src=\"pythonPicture/201804/bar03170738772.png\"><div class=\"J-chart-data\" style=\"display: none;\">{\"chartType\":\"2\",\"chartTitle\":\"标题\",\"ptType\":\"2\",\"pictureUrl\":\"pythonPicture/201804/bar03170738772.png\",\"pictureSize\":14698,\"barRowName\":\"列标题1,列标题2,列标题3\",\"barColName\":\"行标题1,行标题2,行标题3\",\"barValue\":\"1,1,1@1,1,1@1,1,1\",\"barTableVal\":\",列标题1,列标题2,列标题3@行标题1,1,1,1@行标题2,1,1,1@行标题3,1,1,1\",\"parmValue\":\"#1行1列#,#1行2列#,#1行3列#,#2行1列#,#2行2列#,#2行3列#,#3行1列#,#3行2列#,#3行3列#\"}</div></div><div class=\"editor-text J-edit-text\" contenteditable=\"true\">{#参数1#}{#参数2#}{#参数3#}{#参数5#}{#参数6#}</div></div><div class=\"keyframe-edit-content J-keyframe-edit-content\"><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"dele\"><p class=\"edit-icon J-edit-icon dele\"></p></div><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"up\"><p class=\"edit-icon J-edit-icon up\"></p></div><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"down\"><p class=\"edit-icon J-edit-icon down\"></p></div><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"params\">参数</div><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"text\">配文</div></div></div><div class=\"editor-keyframe J-keyframe\" data-type=\"chart\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-img J-editor-img\" data-type=\"chart\" data-size=\"17383\"><img src=\"pythonPicture/201804/line03170756333.png\"><div class=\"J-chart-data\" style=\"display: none;\">{\"chartType\":\"3\",\"chartTitle\":\"标题\",\"ptType\":\"3\",\"pictureUrl\":\"pythonPicture/201804/line03170756333.png\",\"pictureSize\":17383,\"barRowName\":\"{#列标题1#},{#列标题2#},{#列标题3#}\",\"barColName\":\"{#行标题1#},{#行标题2#},{#行标题3#}\",\"barValue\":\"1,1,1@1,1,1@1,1,1\",\"barTableVal\":\",{#列标题1#},{#列标题2#},{#列标题3#}@{#行标题1#},1,1,1@{#行标题2#},1,1,1@{#行标题3#},1,1,1\",\"parmValue\":\"#行标题1#,#行标题2#,#行标题3#,#列标题1#,#列标题2#,#列标题3#,#1行1列#,#1行2列#,#1行3列#,#2行1列#,#2行2列#,#2行3列#,#3行1列#,#3行2列#,#3行3列#\"}</div></div></div></div></div>");
		System.out.println("size = " + frameList.size());
		for (List<Param> list : frameList) {
			System.out.println("******************************");
			for (Param param : list) {
				System.out.println(param.toString());
			}
		}
	}*/

}
