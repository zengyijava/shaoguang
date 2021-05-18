package com.montnets.emp.rms.tools;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jason Huang
 * @date 2018年1月15日 下午7:15:33
 */

public class HTMLTool {
	// 装了ImageMagick还报FileNotFoundException:convert,需要在安装目录将magick.exe复制为convert.exe
	private static final ResourceBundle bundle = ResourceBundle.getBundle("SystemGlobals");
	private static final String operatingSystem = bundle.getString("montnets.rms.videoEncoder.system");
	// 项目名/file/rms/templates/模板号/
	String targetPath, projectPath;

	/**
	 * html处理方法
	 * @param projectPath 项目路径(不能使用反斜杠"\\",结尾处需要"/")
	 * @param targetPath 目标路径(不能使用反斜杠"\\",结尾处需要"/")
	 * @param html html代码
	 * @return resutlt:true||false,text:文本内容,fileType,文件类型字符串(以逗号分割)
	 */
	public Map<String, Object> htmlHandle(String projectPath, String targetPath, String html) {
		this.targetPath = targetPath + "src/";
		this.projectPath = projectPath;
		buildFolder();// 创建文件夹
		int x = 1;// 命名计数
		String smilEnd = "</body></smil>";
		String parEnd = "</par>";
		StringBuilder smil = new StringBuilder("<smil><head><layout><region id=\"Text\" width=\"100%\" height=\"120\" fit=\"scroll\"/><region id=\"Image\" width=\"100%\" height=\"100%\" fit=\"meet\" /><region id=\"Video\" width=\"100%\" height=\"100%\" fit=\"meet\"/><region id=\"Audio\" width=\"100%\" height=\"100%\" fit=\"meet\"/></layout></head><body>");
		StringBuilder textTag = new StringBuilder("<text src=\".txt\" region=\"text\"/>");
		StringBuilder imgTag = new StringBuilder("<img src=\".jpg\" region=\"image\"/>");
		StringBuilder pngTag = new StringBuilder("<img src=\".png\" region=\"image\"/>");
		StringBuilder videoTag = new StringBuilder("<video src=\".mp4\" region=\"video\"/>");
		StringBuilder audioTag = new StringBuilder("<audio src=\".mp3\" region=\"audio\"/>");
		StringBuilder parBuilder = new StringBuilder("<par dur=\"1200s\">");
		StringBuilder textBuilder = new StringBuilder();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		IMGTool imgTool = new IMGTool();
		MessageTool messageTool = new MessageTool();

		Document doc = Jsoup.parseBodyFragment(html);
		// 获取每一帧的Element,"J-keyframe"是帧的分割标志
		Elements eles = doc.body().getElementsByClass("J-keyframe");

		for (Element ele : eles) {
			if (!ele.getElementsByAttributeValue("data-type", "text").isEmpty()) {
				if (ele.getElementsByClass("J-edit-text").text().trim().length() > 0) {
					String name = String.format("%02d", x++);
					String text = getTextByHTML(ele.getElementsByClass("J-edit-text").html());
					textBuilder.append(text);
					parBuilder.append(new StringBuilder(textTag).insert(11, name));
					buildText(name + ".txt", text);

					Map<String, String> parMap = new HashMap<String, String>();
					parMap.put("fileType", "3");
					list.add(parMap);
				}
				// 文本配纯图格式
				if (!ele.getElementsByAttributeValue("data-type", "image").isEmpty()) {
					// 判断该图是否属于图文类型
					if (!ele.getElementsByClass("J-add-module").isEmpty()) {
						// 进行合成图片生成报文等一系列处理操作
						String name = String.format("%02d", x++);
						parBuilder.append(new StringBuilder(imgTag).insert(10, name));
						String[] WH = ele.getElementsByTag("img").first().attr("data-val").split(";");
						Integer width = Integer.parseInt(WH[0].substring(WH[0].indexOf(":") + 1));
						Integer height = Integer.parseInt(WH[1].substring(WH[1].indexOf(":") + 1));
						String srcImg = "";
						if(operatingSystem.toUpperCase().equals("WINDOWS")){
							srcImg = projectPath.substring(1) + ele.getElementsByTag("img").attr("src");
						}else{
							srcImg = projectPath+ ele.getElementsByTag("img").attr("src");
						}
						Map<String, String> map = imgTool.buildCompositeIMG(ele.getElementsByClass("J-add-module"), srcImg, name, width, height);
						String path = map.get("srcImg").replace(projectPath.substring(1), "");
						buildFile(name + ".jpg", path);

						Map<String, String> parMap = new HashMap<String, String>();
						// 判断是否属于带参数图文,不属于则当成图片写入RMS
						if (map.containsKey("message")) {
							parMap.put("fileType", "6");
							parMap.put("message", map.get("message"));
						} else {
							parMap.put("fileType", "2");
						}
						list.add(parMap);
					} else {
						String name = String.format("%02d", x++);
						parBuilder.append(new StringBuilder(imgTag).insert(10, name));
						buildFile(name + ".jpg", ele.getElementsByTag("img").attr("src"));

						Map<String, String> parMap = new HashMap<String, String>();
						parMap.put("fileType", "2");
						list.add(parMap);
					}
					// 文本配图表格式
				} else if (!ele.getElementsByAttributeValue("data-type", "chart").isEmpty()) {
					JSONObject jsonObj = JSONObject.parseObject(ele.getElementsByClass("J-chart-data").text());
					// 静态图表类型,直接当成图片处理
					if (jsonObj.getInteger("ptType") == 1) {
						String name = String.format("%02d", x++);
						parBuilder.append(new StringBuilder(imgTag).insert(10, name));
						buildFile(name + ".jpg", ele.getElementsByTag("img").attr("src"));

						Map<String, String> parMap = new HashMap<String, String>();
						parMap.put("fileType", "2");
						list.add(parMap);
					} else {
						String name = String.format("%02d", x++);
						parBuilder.append(new StringBuilder(pngTag).insert(10, name));

						Map<String, String> parMap = new HashMap<String, String>();
						parMap.put("fileType", "7");
						parMap.put("message", messageTool.buildChartMessage(jsonObj, name + ".png"));
						list.add(parMap);
					}
				}
				// 纯图配文本格式
			} else if (!ele.getElementsByAttributeValue("data-type", "image").isEmpty()) {
				if (!ele.getElementsByTag("img").isEmpty()) {
					// 判断该图是否属于图文类型
					if (!ele.getElementsByClass("J-add-module").isEmpty()) {
						// 进行合成图片生成报文等一系列处理操作
						String name = String.format("%02d", x++);
						parBuilder.append(new StringBuilder(imgTag).insert(10, name));
						String[] WH = ele.getElementsByTag("img").first().attr("data-val").split(";");
						Integer width = Integer.parseInt(WH[0].substring(WH[0].indexOf(":") + 1));
						Integer height = Integer.parseInt(WH[1].substring(WH[1].indexOf(":") + 1));
						String srcImg = "";
						if(operatingSystem.toUpperCase().equals("WINDOWS")){
							srcImg = projectPath.substring(1) + ele.getElementsByTag("img").attr("src");
						}else{
							srcImg = projectPath+ ele.getElementsByTag("img").attr("src");
						}
						Map<String, String> map = imgTool.buildCompositeIMG(ele.getElementsByClass("J-add-module"), srcImg, name, width, height);
						String path = map.get("srcImg").replace(projectPath.substring(1), "");
						buildFile(name + ".jpg", path);

						Map<String, String> parMap = new HashMap<String, String>();
						// 判断是否属于带参数图文,不属于则当成图片写入RMS
						if (map.containsKey("message")) {
							parMap.put("fileType", "6");
							parMap.put("message", map.get("message"));
						} else {
							parMap.put("fileType", "2");
						}
						list.add(parMap);
					} else {
						String name = String.format("%02d", x++);
						parBuilder.append(new StringBuilder(imgTag).insert(10, name));
						buildFile(name + ".jpg", ele.getElementsByTag("img").attr("src"));

						Map<String, String> parMap = new HashMap<String, String>();
						parMap.put("fileType", "2");
						list.add(parMap);
					}
				}
				if (ele.getElementsByClass("J-edit-text").text().trim().length() > 0) {
					String name = String.format("%02d", x++);
					String text = getTextByHTML(ele.getElementsByClass("J-edit-text").html());
					textBuilder.append(text);
					parBuilder.append(new StringBuilder(textTag).insert(11, name));
					buildText(name + ".txt", text);

					Map<String, String> parMap = new HashMap<String, String>();
					parMap.put("fileType", "3");
					list.add(parMap);
				}
				// 图表配文本格式
			} else if (!ele.getElementsByAttributeValue("data-type", "chart").isEmpty()) {
				if (!ele.getElementsByTag("img").isEmpty()) {
					JSONObject jsonObj = JSONObject.parseObject(ele.getElementsByClass("J-chart-data").text());
					// 静态图表类型,直接当成图片处理
					if (jsonObj.getInteger("ptType") == 1) {
						String name = String.format("%02d", x++);
						parBuilder.append(new StringBuilder(imgTag).insert(10, name));
						buildFile(name + ".jpg", ele.getElementsByTag("img").attr("src"));

						Map<String, String> parMap = new HashMap<String, String>();
						parMap.put("fileType", "2");
						list.add(parMap);
					} else {
						String name = String.format("%02d", x++);
						parBuilder.append(new StringBuilder(pngTag).insert(10, name));

						Map<String, String> parMap = new HashMap<String, String>();
						parMap.put("fileType", "7");
						parMap.put("message", messageTool.buildChartMessage(jsonObj, name + ".png"));
						list.add(parMap);
					}
				}
				if (ele.getElementsByClass("J-edit-text").text().trim().length() > 0) {
					String name = String.format("%02d", x++);
					String text = getTextByHTML(ele.getElementsByClass("J-edit-text").html());
					textBuilder.append(text);
					parBuilder.append(new StringBuilder(textTag).insert(11, name));
					buildText(name + ".txt", text);

					Map<String, String> parMap = new HashMap<String, String>();
					parMap.put("fileType", "3");
					list.add(parMap);
				}
			} else if (!ele.getElementsByAttributeValue("data-type", "video").isEmpty()) {
				String name = String.format("%02d", x++);
				parBuilder.append(new StringBuilder(videoTag).insert(12, name));
				buildFile(name + ".mp4", ele.getElementsByTag("video").attr("src"));

				Map<String, String> parMap = new HashMap<String, String>();
				parMap.put("fileType", "4");
				list.add(parMap);
			} else if (!ele.getElementsByAttributeValue("data-type", "audio").isEmpty()) {
				String name = String.format("%02d", x++);
				parBuilder.append(new StringBuilder(audioTag).insert(12, name));
				buildFile(name + ".mp3", ele.getElementsByTag("audio").attr("src"));

				Map<String, String> parMap = new HashMap<String, String>();
				parMap.put("fileType", "5");
				list.add(parMap);
			}
			smil.append(parBuilder.append(parEnd));
			parBuilder = new StringBuilder("<par dur=\"1200s\">");
		}
		smil.append(smilEnd);
		// 创建RMS文件
		buildText("00.smil", smil.toString());
		// 创建HTML文件
		buildHtmlAndResource("fuxin.html", html);
		
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("result", true);
		objectMap.put("text", textBuilder.toString());
		objectMap.put("list", list);
		return objectMap;
	}

	/**
	 * 针对不同浏览器生成的html进行不同的text处理
	 * @param html html字符串
	 * @return 处理后的String字符串
	 */
	private String getTextByHTML(String html) {
		String text = "";
		if (html != null && !html.trim().equals("")) {
			text = StringEscapeUtils.unescapeHtml(html).replaceAll("\n", "");
			if (text.contains("<div>")) {
				text = text.replaceAll("<div> ", "\r\n").replaceAll("<div>", "\r\n").replaceAll("</div>", "").replaceAll("<br>", "");
			} else if (text.contains("<p>")) {
				text = text.replaceAll("<p>", "").replaceAll("</p>", "\r\n").replaceAll("<br>", "");
			} else if (text.contains("<br>")) {
				text = text.replaceAll("<br>", "\r\n");
			} else if (text.contains("</br>")) {
				text = text.replaceAll("</br>", "\r\n");
			}
		}
		return replaceParam(text);
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

	/**
	 * 生成文件夹
	 */
	private void buildFolder() {
		File folder = new File(targetPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	/**
	 * 生成文本文件
	 * @param name
	 * @param string
	 */
	private void buildText(String name, String string) {
		File file = new File(targetPath, name);
		FileOutputStream fos = null;
		try {
			if (!file.exists()) {
                boolean flag = file.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
			}
			fos = new FileOutputStream(file.getAbsoluteFile());
			fos.write(string.getBytes("UTF-8"));
			
		} catch (IOException e) {
			EmpExecutionContext.error(e, "文本文件生成异常！");
		} finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "文件关闭异常！");
				}
			}
		}
	}
	
	/**
	 * 生成文本文件
	 * @param path
	 * @param name
	 * @param string
	 */
	private void buildText(String path, String name, String string) {
		File file = new File(path, name);
		FileOutputStream fos = null;
		try {
			if (!file.exists()) {
                boolean flag = file.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
			}
			fos = new FileOutputStream(file.getAbsoluteFile());
			fos.write(string.getBytes("UTF-8"));
//			fos.close();
		} catch (IOException e) {
			EmpExecutionContext.error(e, "文本文件生成异常！");
		} finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "文件关闭异常！");
				}
			}
		}
	}

	/**
	 * 生成html文件,与buildText()主要是目标路径不同
	 * @param name
	 * @param html
	 */
	private void buildHtmlAndResource(String name, String html) {
		String path = targetPath.substring(0, targetPath.lastIndexOf("src"));
		String resourcePath = path + "resource/";
		String relativePath = resourcePath.substring(targetPath.indexOf("file"));

		String regex = "src=\"[^\"]*\"";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			String srcPath = matcher.group().substring(5, matcher.group().lastIndexOf("\""));
			String srcName = srcPath.substring(srcPath.lastIndexOf("/") + 1);
			// System.out.println("1、资源路径"+srcPath);
			// System.out.println("2、资源名称"+srcName);
			// System.out.println("3、新文件相对路径：" + relativePath + srcName);
			// System.out.println("4、新文件绝对路径：" + resourcePath + srcName);
			html = html.replace(srcPath, relativePath + srcName);// 一定要记得赋值操作!!!
			autoCopyFile(resourcePath + srcName, projectPath + srcPath);
		}
		buildText(path, name, html);
		// 创建第一帧HTML文件
		// Document doc = Jsoup.parseBodyFragment(html);
		// 获取每一帧的Element,"J-keyframe"是帧的分割标志
		// Elements eles = doc.body().getElementsByClass("J-keyframe");
		String header = "<%@ page language=\"java\" pageEncoding=\"UTF-8\"%>\r\n";
		//将contenteditable 属性设置为false,不可编辑
		html = html.replaceAll("contenteditable=\"true\"", "contenteditable=\"false\"");
		buildText(path, "firstframe.jsp", header + html);
	}

	/**
	 * 复制图片、视频、音频并重命名
	 * @param name
	 * @param src
	 */
	private void buildFile(String name, String src) {
		src = projectPath + src;
		String target = targetPath + name;
		// 如果路径相同,不允许自己复制自己
		if (!target.equals(src)) {
			copyFile(target, src);
		}
	}
	
	/**
	 * 复制文件
	 * @param newPath
	 * @param oldPath
	 */
	private void autoCopyFile(String newPath, String oldPath) {
		try {
			// 如果路径相同,不允许自己复制自己
			if(newPath.equals(oldPath)) {
                return;
            }
			
			File newFile = new File(newPath);
			File fileParent = newFile.getParentFile();
			if (!fileParent.exists()) {
				fileParent.mkdirs();
			}

			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream  = null;
				FileOutputStream foutStream = null;
				try{
					inStream = new FileInputStream(oldPath);
					foutStream = new FileOutputStream(newPath);
					byte[] buffer = new byte[1024];
					while ((byteread = inStream.read(buffer)) != -1) {
						foutStream.write(buffer, 0, byteread);
					}
				}finally{
					IOUtils.closeIOs(inStream, foutStream, null, null, getClass());
				}
//				inStream.close();
//				foutStream.close();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "文件复制异常！");
		}
	}
	/**
	 * 复制文件
	 * @param newPath
	 * @param oldPath
	 */
	private void copyFile(String newPath, String oldPath) {
//EmpExecutionContext.info("newPath:"+newPath+",oldPath:"+oldPath);		
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream  = null;
				FileOutputStream foutStream = null;
				try{
					inStream = new FileInputStream(oldPath);
					foutStream = new FileOutputStream(newPath);
					byte[] buffer = new byte[1024];
					while ((byteread = inStream.read(buffer)) != -1) {
						foutStream.write(buffer, 0, byteread);
					}
				}finally{
					IOUtils.closeIOs(inStream, foutStream, null, null, getClass());
				}					
//				inStream.close();
//				foutStream.close();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "文件复制异常！");
		}
	}
	
/*	public static void main(String[] args) {
		HTMLTool htmlTool = new HTMLTool();
		htmlTool.htmlHandle("/", "/","<div class=\"editor-keyframe J-keyframe active\" data-type=\"image\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-img J-editor-img\" data-size=\"174897\" data-type=\"image\"><img class=\"J-main-img\" src=\"D:/img/Bat.jpg\" data-val=\"width:446;height:594\"><div class=\"J-module-html\"><div id=\"TEXT_MODULE_0_show\" class=\"J-add-module\" style=\"position: absolute; top: 168px; left: 14px; z-index: 39; transform-origin: center center 0px; transform: rotate(0deg); width: 422px;\" data-val=\"top:195;left:17;width:422;line-height:18;font-size:18;\"><div id=\"TEXT_MODULE_0_inner_show\" class=\"editor-resize-text\" style=\"font-family: 黑体; font-size: 15px; line-height: 15px; font-weight: 400; font-style: normal; text-decoration: none; text-align: left; color: rgb(160, 6, 255); background-color: transparent; width: 364px; height: auto;\">活动细则：<div>1、	本次活动权限活动期间绑定官方微信的*行卡客户参与，活动期间每人最多获得1张代金券，代金券以券码的形式发放。解除绑定或取消关注则无法获得礼品；</div><div>2、	代金券仅限在**商城使用，不限商品，订单总额需超过299元才能使用；</div><div>3、	代金券有效至2018年12月30日，逾期未使用则视为自动放弃；</div><div>4、	活动最终解释权归**银行信用卡中心所有。</div><div><br></div></div></div></div></div></div></div>");
		htmlTool.htmlHandle("", "D:/img-result/targetPath/", "<div class=\"editor-content J-editor-content\"><div class=\"editor-keyframe J-keyframe\" data-type=\"chart\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-img J-editor-img\" data-size=\"20949\" data-type=\"chart\"><img src=\"D:/img-temp/font-2.png\" /><div class=\"J-chart-data\" style=\"display:none;\">       {\"chartType\":\"3\",\"chartTitle\":\"示例图\",\"ptType\":\"1\",\"pictureUrl\":\"/pythonPicture/201803/line27140030103.png\",\"pictureSize\":26888,\"barRowName\":\"1月,2月,3月\",\"barColName\":\"示例一,示例二,示例三\",\"barValue\":\"1,2,3@2,3,4@3,4,5\",\"barTableVal\":\",1月,2月,3月@示例一,1,2,3@示例二,2,3,4@示例三,3,4,5\"}</div></div><div class=\"editor-text J-edit-text\" contenteditable=\"true\">      请问请问群</div></div></div><div class=\"editor-keyframe J-keyframe active\" data-type=\"text\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-text J-edit-text\" contenteditable=\"true\">      驱蚊器</div><div class=\"editor-img J-editor-img\" data-size=\"16798\" data-type=\"chart\"><img src=\"D:/img-temp/font-3.png\" /><div class=\"J-chart-data\" style=\"display:none;\">       {\"chartType\":\"3\",\"chartTitle\":\"示例图\",\"ptType\":\"2\",\"pictureUrl\":\"/pythonPicture/201803/line27140030103.png\",\"pictureSize\":26888,\"barRowName\":\"1月,2月,3月\",\"barColName\":\"示例一,示例二,示例三\",\"barValue\":\"1,2,3@2,3,4@3,4,5\",\"barTableVal\":\",1月,2月,3月@示例一,1,2,3@示例二,2,3,4@示例三,3,4,5\"}</div></div></div></div></div><div class=\"editor-content J-editor-content\"><div class=\"editor-keyframe J-keyframe\" data-type=\"text\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-text J-edit-text\" contenteditable=\"true\">      啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊！</div><div class=\"editor-img J-editor-img\" data-size=\"35203\" data-type=\"image\"><img class=\"J-main-img\" src=\"D:/img-temp/font-500.png\" /></div></div></div><div class=\"editor-keyframe J-keyframe active\" data-type=\"image\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-img J-editor-img\" data-size=\"35203\" data-type=\"image\"><img class=\"J-main-img\" src=\"D:/img-temp/font-600.png\" /></div><div class=\"editor-text J-edit-text\" contenteditable=\"true\">      呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃！！</div></div></div></div>");
		}*/

}
