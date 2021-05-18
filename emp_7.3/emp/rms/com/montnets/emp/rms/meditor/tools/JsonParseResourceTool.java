package com.montnets.emp.rms.meditor.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.meditor.entity.*;
import com.montnets.emp.rms.tools.IMGTool;
import com.montnets.emp.rms.tools.MediaTool;
import com.montnets.emp.rms.tools.MessageTool;
import com.montnets.emp.util.IOUtils;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 功能描述: JSON 转换为资源文件工具类
 *
 * @auther: xuty
 * @date: 2018/7/31 17:01
 */
public class JsonParseResourceTool {
	private static final String TEMP_FILE_PATH="file/templates/";
	String targetPath, projectPath,resourcePath;
	String imgSrc = SystemGlobals.getValue("montnets.rms.cropper.imgSavePath","");
	String videoSrc = SystemGlobals.getValue("montnets.rms.cropper.videoSavePath","");
	//上传的服务器IP地址
	private static final String uploadPath = SystemGlobals.getValue("montnet.rms.uploadPath").trim();
	//访问的外网地址
	private static final String visitPath = SystemGlobals.getValue("montnet.rms.visitPath").trim();

	/**
	 * 解析JSON，生成RMS相关资源文件存储到src目录
	 * @return
	 */
	public Map<String, Object> storeRmsResourceFile(String targetPath,String projectPath, JSONArray  jsonArr,String frontJson, String langName,int subType,StringBuilder keyWordBuilder){
		this.targetPath = targetPath + "/src/";
        resourcePath = targetPath + "/resource/";
		this.projectPath = projectPath;
		if(!("Windows").equalsIgnoreCase(MediaTool.toolPathType)){//Linux 前多加一个 斜杠，因为后面 projectPath.substring(1) 有此操作，故在这里前加一个 / ，改动比较小
			projectPath = "/" + projectPath;
		}
		buildFolder();// 创建src文件夹
        buildResouceFolder();//创建 resouce 文件夹
		int x = 1;// 命名计数
		String smilEnd = "</body></smil>";
		String parEnd = "</par>";
		StringBuilder smil = new StringBuilder("<smil><head><layout><region id=\"Text\" width=\"100%\" height=\"120\" fit=\"scroll\"/><region id=\"Image\" width=\"100%\" height=\"100%\" fit=\"meet\" /><region id=\"Video\" width=\"100%\" height=\"100%\" fit=\"meet\"/><region id=\"Audio\" width=\"100%\" height=\"100%\" fit=\"meet\"/></layout></head><body>");
		StringBuilder textTag = new StringBuilder("<text src=\".txt\" region=\"text\"/>");
		StringBuilder imgTag = new StringBuilder("<img src=\".jpg\" region=\"image\"/>");
        StringBuilder gifTag = new StringBuilder("<img src=\".gif\" region=\"image\"/>");
		StringBuilder pngTag = new StringBuilder("<img src=\".png\" region=\"image\"/>");
		StringBuilder videoTag = new StringBuilder("<video src=\".mp4\" region=\"video\"/>");
		StringBuilder audioTag = new StringBuilder("<audio src=\".mp3\" region=\"audio\"/>");
		StringBuilder parBuilder = new StringBuilder("<par dur=\"1200s\">");
		StringBuilder textBuilder = new StringBuilder();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		//图片合成工具类
		IMGTool imgTool = new IMGTool();
		//报表操作工具类
		MessageTool messageTool = new MessageTool();
		Map<String,String> srcMap = new HashMap<String, String>();
		ArrayList<File> realSrcList = new ArrayList<File>();
		for(int i = 0;i < jsonArr.size();i++){
			JSONObject jsonObject = jsonArr.getJSONObject(i);
			//文本
			if(jsonObject.containsKey("type") && "text".equals(jsonObject.getString("type"))){
				String name = String.format("%02d", x++);
				String text = replaceParam(jsonObject.getString("text"), langName);
				//去掉前端文本里的<input>标签
                text = TemplateUtil.getTextP(text);
				textBuilder.append(text);
				parBuilder.append(new StringBuilder(textTag).insert(11, name));
				buildText(name + ".txt", text);

				Map<String, String> parMap = new HashMap<String, String>();
				parMap.put("fileType", "3");
				list.add(parMap);
				keyWordBuilder.append(text);
				// 文本配图
				if(jsonObject.containsKey("image") && StringUtils.isNotBlank(jsonObject.getJSONObject("image").getString("src"))){

					JSONObject imgJsonObj = jsonObject.getJSONObject("image");
					// 判断该图是否属于图文类型
					if (imgJsonObj.containsKey("textEditable") && null != imgJsonObj.getJSONArray("textEditable")
							&& imgJsonObj.getJSONArray("textEditable").size() > 0) { //带参数的图片
						// 进行合成图片生成报文等一系列处理操作
						name = String.format("%02d", x++);
						//判断图片文件类型，只判断 jpg 和 gif 两种类型，不是gif类型的图片，全改为.jpg后缀
						int position = imgJsonObj.getString("src").lastIndexOf(".");
						String imgType = imgJsonObj.getString("src").substring(position);
						if(".gif".equalsIgnoreCase(imgType)){
							parBuilder.append(new StringBuilder(gifTag).insert(10, name));
						}else{
							parBuilder.append(new StringBuilder(imgTag).insert(10, name));
						}

						//背景图片的宽高
						Integer width = imgJsonObj.getInteger("width");
						Integer height = imgJsonObj.getInteger("height");
						//背景图片
						String srcImg = projectPath.substring(1) + getSourceFilePath(imgJsonObj.getString("src"));
						//将上传目录的文件复制到 file/temlates/企业编码/模板ID/rms/resource/ 目录下
						JSONArray textEdiArr = imgJsonObj.getJSONArray("textEditable");
						Map<String, String> map = imgTool.buildCompositeECIMG(textEdiArr, srcImg, name, width, height,keyWordBuilder);
						String path = map.get("srcImg").replace(projectPath.substring(1), "");
						if(".gif".equalsIgnoreCase(imgType)){
							buildFile(name + ".gif", path,realSrcList);
						}else{
							buildFile(name + ".jpg", path,realSrcList);
						}
						parMap = new HashMap<String, String>();
						// 判断是否属于带参数图文,不属于则当成图片写入RMS
						if (map.containsKey("message")) {
							parMap.put("fileType", "6");
							parMap.put("message", map.get("message"));
						} else {
							parMap.put("fileType", "2");
						}
						list.add(parMap);
						//将上传目录的背景文件复制到 file/temlates/企业编码/模板ID/rms/resource/ 目录下
						File srcImgFile = new File(srcImg);
						String resourcePath = this.resourcePath +srcImgFile.getName();
						buildResouceFile(srcImg,resourcePath);
						// 将图片上配的图片 复制 到 file/temlates/企业编码/模板ID/rms/resource/ 目录下
						copyImgOnBgPictureFile(textEdiArr,srcMap);
						//替换背景图片的src 为resource 目录下的图片
						srcMap.put(this.resourcePath.replace(new TxtFileUtil().getWebRoot(),"")+srcImgFile.getName(),getSourceFilePath(imgJsonObj.getString("src")));
					}else{ //配纯图
						JSONObject imgObj = jsonObject.getJSONObject("image");
						name = String.format("%02d", x++);
						//判断图片文件类型，只判断 jpg 和 gif 两种类型，不是gif类型的图片，全改为.jpg后缀
						int position = imgObj.getString("src").lastIndexOf(".");
						String imgType = imgObj.getString("src").substring(position);
						if(".gif".equalsIgnoreCase(imgType)){
							parBuilder.append(new StringBuilder(gifTag).insert(10, name));
							buildFile(name + ".gif", getSourceFilePath(imgObj.getString("src")),realSrcList);
						}else{
							parBuilder.append(new StringBuilder(imgTag).insert(10, name));
							buildFile(name + ".jpg", getSourceFilePath(imgObj.getString("src")),realSrcList);
						}
						//将上传目录的背景文件复制到 file/temlates/企业编码/模板ID/rms/resource/ 目录下
						String srcImg = new TxtFileUtil().getWebRoot() +getSourceFilePath(imgObj.getString("src"));
						File srcImgFile = new File(srcImg);
						String resourcePath = this.resourcePath +srcImgFile.getName();
						buildResouceFile(srcImg,resourcePath);
						parMap = new HashMap<String, String>();
						parMap.put("fileType", "2");
						list.add(parMap);
						srcMap.put(this.resourcePath.replace(new TxtFileUtil().getWebRoot(),"")+ srcImgFile.getName(),getSourceFilePath(imgObj.getString("src")));
					}
				} else if(jsonObject.containsKey("audio") && StringUtils.isNotBlank(jsonObject.getJSONObject("audio").getString("src"))) {
					// 文本配音频
					name = String.format("%02d", x++);
					parBuilder.append(new StringBuilder(audioTag).insert(12, name));
					buildFile(name + ".mp3", getSourceFilePath(jsonObject.getJSONObject("audio").getString("src")), realSrcList);
					//将上传目录的背景文件复制到 file/temlates/企业编码/模板ID/rms/resource/ 目录下
					String srcFilePath = String2FileUtil.getWebRoot() + getSourceFilePath(jsonObject.getString("src"));
					File srcFile = new File(srcFilePath);
					String resourcePath = this.resourcePath + srcFile.getName();
					buildResouceFile(srcFilePath, resourcePath);
					srcMap.put(this.resourcePath.replace(String2FileUtil.getWebRoot(), "") + srcFile.getName(), getSourceFilePath(jsonObject.getString("src")));
					Map<String, String> audioMap = new HashMap<String, String>();
					audioMap.put("fileType", "5");
					list.add(audioMap);

				} else if(jsonObject.containsKey("video") && StringUtils.isNotBlank(jsonObject.getJSONObject("video").getString("src"))) {
					// 文本配视频
					name = String.format("%02d", x++);
					parBuilder.append(new StringBuilder(videoTag).insert(12, name));
					buildFile(name + ".mp4", getSourceFilePath(jsonObject.getJSONObject("video").getString("src")),realSrcList);
					//将上传目录的背景文件复制到 file/temlates/企业编码/模板ID/rms/resource/ 目录下
					String srcFilePath = String2FileUtil.getWebRoot() + getSourceFilePath(jsonObject.getString("src"));
					File srcFile = new File(srcFilePath);
					String resourcePath = this.resourcePath + srcFile.getName();
					buildResouceFile(srcFilePath, resourcePath);
					srcMap.put(this.resourcePath.replace(String2FileUtil.getWebRoot(), "") + srcFile.getName(), getSourceFilePath(jsonObject.getString("src")));
					Map<String, String> videoMap = new HashMap<String, String>();
					videoMap.put("fileType", "4");
					list.add(videoMap);

				} else if(jsonObject.containsKey("chart") && !StringUtils.isBlank(jsonObject.getJSONObject("chart").getString("pictureUrl"))){
					//文本配图表
					// 静态图表类型,直接当成图片处理
					JSONObject chartObj = jsonObject.getJSONObject("chart");
					if (chartObj.getInteger("ptType") == 1) {
						name = String.format("%02d", x++);
						parBuilder.append(new StringBuilder(imgTag).insert(10, name));
						buildFile(name + ".jpg", getChartSourceFilePath(chartObj.getString("pictureUrl")),realSrcList);
						srcMap.put(this.resourcePath.replace(new TxtFileUtil().getWebRoot(),"")+name+".jpg",getChartSourceFilePath(chartObj.getString("pictureUrl")));
						parMap = new HashMap<String, String>();
						parMap.put("fileType", "2");
						list.add(parMap);
					} else {
						name = String.format("%02d", x++);
						parBuilder.append(new StringBuilder(pngTag).insert(10, name));

						parMap = new HashMap<String, String>();
						parMap.put("fileType", "7");
						parMap.put("message", messageTool.buildChartMessage(chartObj, name + ".png"));
						list.add(parMap);
					}
				}

			} else if(jsonObject.containsKey("type") && jsonObject.getString("type").equals("image")) {

				Map<String, String> parMap = new HashMap<String, String>();
				// 判断该图是否属于图配文类型
				if (jsonObject.containsKey("textEditable") && jsonObject.getJSONArray("textEditable").size() > 0 ) { //带参数的图片
					// 进行合成图片生成报文等一系列处理操作
					String name = String.format("%02d", x++);
					parBuilder.append(new StringBuilder(imgTag).insert(10, name));
					//背景图片的宽高
					Integer width = jsonObject.getInteger("width");
					Integer height = jsonObject.getInteger("height");
					//背景图片
					String srcImg = projectPath.substring(1) +  getSourceFilePath(jsonObject.getString("src"));
					JSONArray textEdiArr = jsonObject.getJSONArray("textEditable");
					Map<String, String> map = imgTool.buildCompositeECIMG(textEdiArr, srcImg, name, width, height,keyWordBuilder);
					String path = map.get("srcImg").replace(projectPath.substring(1), "");
					buildFile(name + ".jpg", path,realSrcList);
					// 判断是否属于带参数图文,不属于则当成图片写入RMS
					if (map.containsKey("message")) {
						parMap.put("fileType", "6");
						parMap.put("message", map.get("message"));
					} else {
						parMap.put("fileType", "2");
					}
					list.add(parMap);

					//将上传目录的背景文件复制到 file/temlates/企业编码/模板ID/rms/resource/ 目录下
					File srcImgFile = new File(srcImg);
					String resourcePath = this.resourcePath +srcImgFile.getName();
					buildResouceFile(srcImg,resourcePath);
					// 将图片上配的图片 复制 到 file/temlates/企业编码/模板ID/rms/resource/ 目录下
					copyImgOnBgPictureFile(textEdiArr,srcMap);
					//替换背景图片的src 为resource 目录下的图片
					srcMap.put(this.resourcePath.replace(new TxtFileUtil().getWebRoot(),"")+srcImgFile.getName(),getSourceFilePath(jsonObject.getString("src")));
				} else{ //配纯图
					if(!"".equals(jsonObject.getString("src").trim())) {
						String name = String.format("%02d", x++);
						//判断图片文件类型，只判断 jpg 和 gif 两种类型，不是gif类型的图片，全改为.jpg后缀
						int position = jsonObject.getString("src").lastIndexOf(".");
						String imgType = jsonObject.getString("src").substring(position);
						if (".gif".equalsIgnoreCase(imgType)) {
							parBuilder.append(new StringBuilder(gifTag).insert(10, name));
							buildFile(name + ".gif", getSourceFilePath(jsonObject.getString("src")), realSrcList);
						} else {
							parBuilder.append(new StringBuilder(imgTag).insert(10, name));
							buildFile(name + ".jpg", getSourceFilePath(jsonObject.getString("src")), realSrcList);
						}
						//将上传目录的背景文件复制到 file/temlates/企业编码/模板ID/rms/resource/ 目录下
						String srcImg = new TxtFileUtil().getWebRoot() + getSourceFilePath(jsonObject.getString("src"));
						File srcImgFile = new File(srcImg);
						String resourcePath = this.resourcePath + srcImgFile.getName();
						buildResouceFile(srcImg, resourcePath);
						srcMap.put(this.resourcePath.replace(new TxtFileUtil().getWebRoot(), "") + srcImgFile.getName(), getSourceFilePath(jsonObject.getString("src")));
						parMap = new HashMap<String, String>();
						parMap.put("fileType", "2");
						list.add(parMap);
					}
				}

			} else if(jsonObject.containsKey("type") && jsonObject.getString("type").equals("chart") && !StringUtils.isBlank(jsonObject.getString("pictureUrl"))){
				// 图表
				// 静态图表类型,直接当成图片处理
				if (Integer.parseInt(jsonObject.getString("ptType")) == 1) {
					String name = String.format("%02d", x++);
					parBuilder.append(new StringBuilder(imgTag).insert(10, name));
					buildFile(name + ".jpg", getChartSourceFilePath(jsonObject.getString("pictureUrl")),realSrcList);
					srcMap.put(this.resourcePath.replace(new TxtFileUtil().getWebRoot(),"")+name+".jpg",getChartSourceFilePath(jsonObject.getString("pictureUrl")));
					Map<String, String> parMap = new HashMap<String, String>();
					parMap.put("fileType", "2");
					list.add(parMap);
				} else {
					// 动态图表
					String name = String.format("%02d", x++);
					parBuilder.append(new StringBuilder(pngTag).insert(10, name));

					Map<String, String> parMap = new HashMap<String, String>();
					parMap.put("fileType", "7");
					parMap.put("message", messageTool.buildChartMessage(jsonObject, name + ".png"));
					list.add(parMap);
				}

			} else if (jsonObject.containsKey("type") && jsonObject.getString("type").equals("audio")) {
				// 音频
				String name = String.format("%02d", x++);
				parBuilder.append(new StringBuilder(audioTag).insert(12, name));
				buildFile(name + ".mp3", getSourceFilePath(jsonObject.getString("src")), realSrcList);
				//将上传目录的背景文件复制到 file/temlates/企业编码/模板ID/rms/resource/ 目录下
				String srcFilePath = String2FileUtil.getWebRoot() + getSourceFilePath(jsonObject.getString("src"));
				File srcFile = new File(srcFilePath);
				String resourcePath = this.resourcePath + srcFile.getName();
				buildResouceFile(srcFilePath, resourcePath);
				srcMap.put(this.resourcePath.replace(String2FileUtil.getWebRoot(), "") + srcFile.getName(), getSourceFilePath(jsonObject.getString("src")));
				Map<String, String> parMap = new HashMap<String, String>();
				parMap.put("fileType", "5");
				list.add(parMap);

			} else if (jsonObject.containsKey("type") && jsonObject.getString("type").equals("video")) {
				// 视频
				String name = String.format("%02d", x++);
				parBuilder.append(new StringBuilder(videoTag).insert(12, name));
				buildFile(name + ".mp4", getSourceFilePath(jsonObject.getString("src")), realSrcList);
				//将上传目录的背景文件复制到 file/temlates/企业编码/模板ID/rms/resource/ 目录下
				String srcFilePath = String2FileUtil.getWebRoot() + getSourceFilePath(jsonObject.getString("src"));
				File srcFile = new File(srcFilePath);
				String resourcePath = this.resourcePath + srcFile.getName();
				buildResouceFile(srcFilePath, resourcePath);
				srcMap.put(this.resourcePath.replace(String2FileUtil.getWebRoot(), "") + srcFile.getName(), getSourceFilePath(jsonObject.getString("src")));

				Map<String, String> parMap = new HashMap<String, String>();
				parMap.put("fileType", "4");
				list.add(parMap);

			}
			if(jsonObject.containsKey("type") && (
					jsonObject.getString("type").equals("image") ||
							jsonObject.getString("type").equals("chart") ||
							jsonObject.getString("type").equals("audio") ||
							jsonObject.getString("type").equals("video"))) {

				// 是否配文
				if(jsonObject.containsKey("text") && StringUtils.isNotBlank(jsonObject.getString("text"))){
					String name = String.format("%02d", x++);
					String text =  replaceParam(jsonObject.getString("text"),langName);
					text = TemplateUtil.getTextP(text);
					textBuilder.append(text);
					parBuilder.append(new StringBuilder(textTag).insert(11, name));
					buildText(name + ".txt", text);

					HashMap<String, String> parMap = new HashMap<String, String>();
					parMap.put("fileType", "3");
					list.add(parMap);
					keyWordBuilder.append(text);
				}
			}

			smil.append(parBuilder.append(parEnd));
			parBuilder = new StringBuilder("<par dur=\"1200s\">");

		}
		smil.append(smilEnd);
		// 创建RMS文件
		buildText("00.smil", smil.toString());
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("result", true);
		objectMap.put("text", textBuilder.toString());
		objectMap.put("list", list);
		//替换前端JSON 中的资源src
		String fontJson = replaceSrc(srcMap,frontJson,subType);
		objectMap.put("fontJson", fontJson);
		//删除不是模板JSON 里的资源文件
		File dir = new File(this.targetPath);
		File[] files = dir.listFiles();
		List<File> filePaths = Arrays.asList(files);
		List<File> deleFiles = getDiffList(filePaths,realSrcList);
		for(File f : deleFiles){
			if(f.exists() && f.isFile()){
				if(f.getName().contains("00.smil") || f.getName().contains(".txt")){
					continue;
				}
				boolean flag = f.delete();
				if (!flag) {
					EmpExecutionContext.error("删除文件失败！");
				}
			}
		}
		return objectMap;

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
	private void buildResouceFolder() {
		File folder = new File(resourcePath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	/**
	 * 清空文件夹
	 */
	private void emptyFolder(String path){
		File dir = new File(path);
		File[] files = dir.listFiles();
		for(File f :files){
			if(f.exists() && f.isFile()){
                boolean flag = f.delete();
                if (!flag) {
                    EmpExecutionContext.error("删除文件失败！");
                }
			}
		}
	}

	/**
	 * 生成文件
	 * @param name
	 * @param src
	 */
	private void buildFile(String name, String src,ArrayList<File> realSrcList) {
		src = projectPath + src;
		String target = targetPath + name;
		realSrcList.add(new File(target));
		// 如果路径相同,不允许自己复制自己
		if (!target.equals(src)) {
			copyFile(target, src);
		}
	}

    /**
     * 复制到 resoucce 目录下
     * @param sourcePath 原文件路径
     * @param targetPath 生成 到resouce 目录下
     */
	private void buildResouceFile(String sourcePath, String targetPath) {
		// 如果路径相同,不允许自己复制自己
		File sourceFile  = new File(sourcePath);
		File targetFile  = new File(targetPath);
		sourcePath = sourceFile.getAbsolutePath();
		targetPath = targetFile.getAbsolutePath();
		if (!targetPath.equals(sourcePath)) {
			copyFile(targetPath, sourcePath);
		}
	}
	/**
	 * 解析JSON串，将其中包含的资源文件存储到src目录下
	 */

	public  Map<String,Object> storeOttResourceFile(String corpCode,Long tmid,TempContent tmpContent,String frontJson,int subType) {
		boolean flag = false;
		Map<String,Object>  resultMap = new HashMap<String, Object>();
		Map<String,String> srcMap = new HashMap<String, String>();
		ArrayList<File> realSrcList = new ArrayList<File>();
		try {
			// 背景图片
			String rootUrl = new TxtFileUtil().getWebRoot();
			String oldPath = "";
			String newPath ="";
			//源文件路径
			String src = tmpContent.getBgSrc();
			if(StringUtils.isNotBlank(src)){
				//TODO 背景图片路径待定
				oldPath = rootUrl+src.substring(src.indexOf("rms/"));
				//新路径TEMPLATEPATH + tmid+"/" + corpCode + "/ott/src/
				newPath = rootUrl+TEMP_FILE_PATH + corpCode+"/" + tmid +  "/ott/src/"+getFileName(oldPath);
				//复制背景图片
				copyFile(newPath, oldPath);
				realSrcList.add(new File(newPath));
			}

			//复制组件中的所有资源
			TempDataElement elements = tmpContent.getElements();
			if(null != elements){
				List<TempElement> images = elements.getImages();
				if(null != images && images.size() > 0){//图片
					for(TempElement t : images){
						oldPath = rootUrl + getSourceFilePath(t.getSrc());
						newPath = rootUrl+TEMP_FILE_PATH + corpCode+"/" + tmid +  "/ott/src/"+getFileName(oldPath);
						if(!newPath.equals(oldPath)){
							copyFile(newPath, oldPath);
							srcMap.put(newPath.replace(rootUrl,""),oldPath.replace(rootUrl,""));
						}
						realSrcList.add(new File(newPath));
					}
				}
				List<TempElement> audios = elements.getAudios();
				if(null != audios && audios.size() > 0){
					for(TempElement t : audios){//音频
						oldPath = rootUrl + getSourceFilePath(t.getSrc());
						newPath = rootUrl+TEMP_FILE_PATH + corpCode+"/" + tmid +  "/ott/src/"+getFileName(oldPath);
						if(!newPath.equals(oldPath)){
							copyFile(newPath, oldPath);
							srcMap.put(newPath.replace(rootUrl,""),oldPath.replace(rootUrl,""));
						}
					}
					realSrcList.add(new File(newPath));
				}
				List<TempElement> vedios = elements.getVideos();
				if(null != vedios && vedios.size() > 0){
					for(TempElement t : vedios){//视频
						oldPath = rootUrl + getSourceFilePath(t.getSrc());
						newPath = rootUrl+TEMP_FILE_PATH + corpCode+"/" + tmid +  "/ott/src/"+getFileName(oldPath);
						if(!newPath.equals(oldPath)){
							copyFile(newPath, oldPath);
							srcMap.put(newPath.replace(rootUrl,""),oldPath.replace(rootUrl,""));
						}
					}
					realSrcList.add(new File(newPath));
				}
				List<TempElement> qrcodes = elements.getQrcodes();
				if(null != qrcodes && qrcodes.size() > 0){
					for(TempElement t : qrcodes){//二维码
						oldPath = rootUrl +  getSourceFilePath(t.getSrc());
						newPath = rootUrl+TEMP_FILE_PATH + corpCode+"/" + tmid +  "/ott/src/"+getFileName(oldPath);
						if(!newPath.equals(oldPath)){
							copyFile(newPath, oldPath);
							srcMap.put(newPath.replace(rootUrl,""),oldPath.replace(rootUrl,""));
						}
					}
					realSrcList.add(new File(newPath));

				}
			}
			//删除不是模板JSON 里的资源文件
			File dir = new File(rootUrl+TEMP_FILE_PATH + corpCode+"/" + tmid +  "/ott/src/");
			File[] files = dir.listFiles();
			List<File> filePaths = Arrays.asList(files);
			List<File> deleFiles = getDiffList(filePaths, realSrcList);
			//删除多余的源文件
			for(File f : deleFiles){
				if(f.exists() && f.isFile()){
					if(f.getName().contains("ott.mrcsl")){
						continue;
					}
                    boolean status = f.delete();
                    if (!status) {
                        EmpExecutionContext.error("删除文件失败！");
                    }
				}
			}
			flag = true;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"存储资源文件出现异常！");
		}
		frontJson = replaceSrc(srcMap,frontJson,subType);
		resultMap.put("result","success");
		resultMap.put("frontJson",frontJson);
		return resultMap;

	}
	/**
	 * 存储H5相关资源文件-封面有图片资源需要存储
	 * @param corpCode
	 * @param tmid
	 * @param frontJson
	 * @return
	 */
	public  Map<String,Object> storeH5ResourceFile(String corpCode, Long tmid, TempData tempData, String frontJson,int subType) {
		Map<String,Object>  resultMap = new HashMap<String, Object>();
		Map<String,String> srcMap = new HashMap<String, String>();
		ArrayList<File> realSrcList = new ArrayList<File>();
		try {
			// 背景图片
			String rootUrl = new TxtFileUtil().getWebRoot();
			String oldPath = "";
			String newPath ="";
			//复制组件中的所有资源
			if(null != tempData && null !=tempData.getApp()){
				H5App app = tempData.getApp();
				H5DataElement cover = app.getCover();
				oldPath = rootUrl + getSourceFilePath(cover.getSrc());
				newPath = rootUrl+TEMP_FILE_PATH + corpCode+"/" + tmid +  "/ott/src/"+getFileName(oldPath);
				if(!newPath.equals(oldPath)){
					copyFile(newPath, oldPath);
					srcMap.put(newPath.replace(rootUrl,""),oldPath.replace(rootUrl,""));
					realSrcList.add(new File(newPath));
				}
			}

			//删除不是模板JSON 里的资源文件
			File dir = new File(rootUrl+TEMP_FILE_PATH + corpCode+"/" + tmid +  "/ott/src/");
			File[] files = dir.listFiles();
			List<File> filePaths = Arrays.asList(files);
			List<File> deleFiles = getDiffList(filePaths, realSrcList);
			//删除多余的源文件
			for(File f : deleFiles){
				if(f.exists() && f.isFile()){
					if(f.getName().contains("ott.mrcsl")){
						continue;
					}
                    boolean flag = f.delete();
                    if (!flag) {
                        EmpExecutionContext.error("删除文件失败！");
                    }
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"存储资源文件出现异常！");
		}
		H5App app = tempData.getApp();
		H5DataElement cover = app.getCover();
		if(!StringUtils.IsNullOrEmpty(cover.getSrc())){
			frontJson = replaceSrc(srcMap,frontJson,subType);
		}
		resultMap.put("result","success");
		resultMap.put("frontJson",frontJson);
		return resultMap;

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
//			fos.close();
		} catch (IOException e) {
			EmpExecutionContext.error(e, "文本文件生成异常！");
		}finally{
        	try {
				IOUtils.closeIOs(null, fos, null, null, getClass());
			} catch (IOException e) {
				EmpExecutionContext.error(e, "文件关闭异常！");
			}
        }
	}

	/**
	 * 复制文件
	 *
	 * @param newPath
	 * @param oldPath
	 */
	private void copyFile(String newPath, String oldPath) {
		InputStream inStream = null;
		FileOutputStream foutStream =null;
		try {
			int byteread = 0;
			File newfile = new File(newPath);
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				inStream = new FileInputStream(oldPath);
				File folder = new File(newfile.getParent());
				if(!folder.exists()){//新文件
					folder.mkdirs();
				}
				foutStream = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					foutStream.write(buffer, 0, byteread);
				}
//				inStream.close();
//				foutStream.close();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "文件复制异常！");
		} finally{
	    	try {
				IOUtils.closeIOs(inStream, foutStream, null, null, getClass());
			} catch (IOException e) {
				EmpExecutionContext.error(e, "文件关闭异常！");
			}
		}
    }
	/**
	 * 获取文件名
	 * @param filePath
	 * @return
	 */
	private String getFileName(String filePath){
		File file = new File(filePath);
		return file.getName();
	}

	/**
	 * 获取 文件上传的相对路径 ：u
	 * @param filePath
	 * @return
	 */
	private String getSourceFilePath(String filePath){
		String path ="";
		if(filePath.contains("ueditor")){//首次编辑时的目录 ueditor/....
			path = filePath.substring(filePath.indexOf("ueditor"));
		}
//		else if(filePath.contains(imgSrc)){//图片裁剪后存放的路径
//			path = filePath.substring(filePath.indexOf(imgSrc));
//			if(path.startsWith("/")){
//				path = path.substring(1,path.length());
//			}
//		}else if(filePath.contains(videoSrc)){//视屏、音频裁剪后存放的路径
//			path = filePath.substring(filePath.indexOf(videoSrc));
//			if(path.startsWith("/")){
//				path = path.substring(1,path.length());
//			}
		else if(filePath.contains("file")){//再次编辑后的资源路径是存在 file/templates/企业编码/模板ID/目录下
			path = filePath.substring(filePath.indexOf("file"));
		}else{
			path = filePath;
		}
		return  path;
	}
	private String getChartSourceFilePath(String filePath){
		String path ="";
		if(filePath.contains("pythonPicture")){

			path = filePath.substring(filePath.indexOf("pythonPicture"));
		}else{
			path = filePath.substring(filePath.indexOf("file"));
		}
		return  path;
	}

	/**
	 * 替换参数字符串
	 * @param str 替换前的字符串
	 * @return 替换后字符串
	 */
	private String replaceParam(String str, String langName) {
		String param = "zh_TW".equals(langName) ? "參數" : "zh_HK".equals(langName) ? "param":"参数";
		int paramPreLen = "zh_HK".equals(langName) ? 7 : 4;
		String regex = "\\{#"+ param +"\\d+#}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			String no = str.substring(matcher.start() + paramPreLen, matcher.end() - 2);
			str = str.replaceFirst(regex, "#P_" + no + "#");
			matcher = pattern.matcher(str);
		}
		str = replaceDiv(str);
		return str;
	}

	private String replaceSrc(Map<String,String> map,String frontJson,int subType){
		for(Map.Entry<String,String> en : map.entrySet()){
			frontJson = frontJson.replace(en.getValue(),en.getKey());
			if(subType == 1 && en.getValue().contains("ueditor")&&!en.getValue().equals(en.getKey())){
				deleteFile(en.getValue());
			}
		}
		return frontJson;
	}

	private void deleteFile(String filePath){
		filePath =new TxtFileUtil().getWebRoot()+filePath;
		File file = new File(filePath);
		if(file.exists() && file.isFile()){
            boolean flag = file.delete();
            if (!flag) {
                EmpExecutionContext.error("删除文件失败！");
            }
		}
	}


	private List<File> getDiffList(List<File> list1,List<File> list2){
		List list = new ArrayList(Arrays.asList(new Object[list1.size()]));
		Collections.copy(list, list1);
		list.removeAll(list2);
		return list;
	}

    /**
     *  复制图片上配的图片资源到 resource 目录下
     * @param textEdiArr
     */
    private void copyImgOnBgPictureFile(JSONArray textEdiArr,Map<String,String> srcMap) {
        for(int i = 0 ; i< textEdiArr.size();i++){
            JSONObject object = textEdiArr.getJSONObject(i);
            if(object.containsKey("type") && "image".equals(object.getString("type"))){//图片类型的才有src
                String src = projectPath + getSourceFilePath(object.getString("src"));
                File file = new File(src);
                String target = resourcePath +file.getName();
                buildResouceFile(src,target);
                srcMap.put(resourcePath.replace(new TxtFileUtil().getWebRoot(),"")+file.getName(),getSourceFilePath(object.getString("src")));
            }
        }
    }
	/**
	 * 利用Dom解析H5模板html文件，解析json渲染生成完整html文件
	 *
	 * @param object 前端H5 模板内容对象
	 * @param path   转换HTML后文件存储路径
	 * @return 成功与否
	 */
	public boolean parseH5Html(JSONObject object, String path,StringBuilder keyWordBuilder) {
		boolean flag = false;
		try{

			//利用Dom解析H5的html模板文件
			TxtFileUtil fileUtil = new TxtFileUtil();
//			String allHtml = this.parseH5HtmlTool(object,path,"rms/meditor/file/h5.html");
            String allHtml = assemableHtmlFromJson(object,path,"rms/meditor/file/h5_V1.html",keyWordBuilder);
			File folder = new File(new File(path).getParent());
			if (!folder.exists()) {
				//新文件
				if (!folder.mkdirs()) {
					throw new EMPException("创建目录失败:" + folder.getName());
				}
			}
			fileUtil.writeToTxtFileUTF8(path, allHtml);
			flag = true;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"利用Dom解析H5模板html文件异常，方法：com.montnets.emp.rms.meditor.tools.JsonParseResourceTool.parseH5Html");
		}
		return flag;
	}

	/**
	 * 利用Dom解析H5模板html文件，解析json渲染生成完整html文件
	 *
	 * @param object 前端H5 模板内容对象
	 * @param path   转换HTML后文件存储路径
	 * @return 成功与否
	 */
	public  String parseH5HtmlTool(JSONObject object, String path, String templatePath) {
		String h5html = null;
		try {
			//常规校验
			if (object == null || org.apache.commons.lang.StringUtils.isEmpty(path)) {
				throw new EMPException("参数为null");
			}
			//利用Dom解析H5的html模板文件
			TxtFileUtil fileUtil = new TxtFileUtil();
			String h5Html = fileUtil.readFileByLinesUTF8(templatePath);
			Document doc = Jsoup.parse(h5Html);
			Elements swiperWrapper = doc.getElementsByClass("swiper-wrapper");
			//两次遍历，第一次遍历pages获取所有page 第二次遍历page里的elements获取所有元素对象
			//1.获取过渡动画效果
			JSONObject animation = (JSONObject) object.get("animation");
			//StringBuilder animateHtml = null;
			if (animation != null) {
				//持续时间
				String duration = animation.getString("duration");
				//过渡效果
				String effect = animation.getString("effect");
				//延时时间
				String delay = animation.getString("delay");
				//<div swiper-animate-effect="default" swiper-animate-duration="1s" swiper-animate-delay="0">
//                animateHtml = new StringBuilder("<div swiper-animate-effect=\"")
//                        .append(effect).append("\" swiper-animate-duration=\"")
//                        .append(duration).append("\" swiper-animate-delay=\"").append(delay).append("\">");
			}
			//2.music  背景音乐

			JSONObject music = (JSONObject) object.get("music");
			if (music != null) {
				String msrc = (String) music.get("src");
				if(!StringUtils.IsNullOrEmpty(msrc)){
					StringBuilder musicContent = new StringBuilder("<audio id=\"music\" type=\"audio/mpeg\" src=\"");

					Boolean autoPlay = (Boolean) music.get("autoPlay");
					Boolean loop = (Boolean) music.get("loop");
					musicContent.append(msrc).append("\"").append("autoplay=\"").append(autoPlay).append("\"").append("loop=\"").append(loop).append("\"");
//                String tag = (String) music.get("tag");
//                String filename = (String) music.get("filename");
//                Boolean active = (Boolean) music.get("active");
//                Boolean reused = (Boolean) music.get("reused");
					musicContent.append("class=\"ele\" ");

					JSONObject musicElement = (JSONObject) music.get("element");
					if (musicElement != null) {
						Integer mw = (Integer) musicElement.get("w");
//                    String eduration = (String) musicElement.get("duration");
//                    String eactive = (String) musicElement.get("active");
//                    String etype = (String) musicElement.get("type");
						Integer mh = (Integer) musicElement.get("h");
						Integer my = (Integer) musicElement.get("y");
						Integer mx = (Integer) musicElement.get("x");
						//String mz = (String) musicElement.get("z");
						musicContent.append("style=\"z-index:").append(1000).append(";");
						musicContent.append("width:").append(mw).append("px;");
						musicContent.append("height:").append(mh).append("px;");
						musicContent.append("left:").append(mx).append("px;");
						musicContent.append("top:").append(my).append("px;");
						musicContent.append("\"></audio>");
						swiperWrapper.append(musicContent.toString());
					}
				}
			}


			//3.pages
			JSONArray pages = (JSONArray) object.get("pages");

			//遍历pages
			for (Object pageObject : pages) {
				//是否添加背景
				boolean addBg = false;
				StringBuilder htmlContent = new StringBuilder(" <section class=\"swiper-slide flat\"> <div class=\"page-view\">");


				JSONObject page = (JSONObject) pageObject;
				//获取背景图片的src
				String bgSrc = (String) page.get("bgSrc");
				//获取所有的元素集合
				JSONArray elements = (JSONArray) page.get("elements");
				//遍历elements
				for (Object elementObject : elements) {

					StringBuilder elementContent = new StringBuilder("<div class=\"content\" style=\"");
					JSONObject element = (JSONObject) elementObject;
					//类型 text, pic, button, bg
					String type = (String) element.get("type");
					//路径
					String src = (String) element.get("src");
					//具体样式
					JSONObject style = (JSONObject) element.get("style");
					//获取样式
					String fontSize = "''";
					String fontWeight = "''";
					String textAlign = "''";
					String color = "''";
					String backgroundColor = "''";
					String borderRadius = "''";
					if (null != style) {
						fontSize = style.getString("fontSize");
						fontWeight = style.getString("fontWeight");
						if (StringUtils.isEmpty(fontWeight)) {
							fontWeight = "normal";
						}
						textAlign = style.getString("textAlign");
						color = style.getString("color");
						backgroundColor = style.getString("backgroundColor");
						borderRadius = style.getString("borderRadius");
					}
					String tag = (String) element.get("tag");
					Integer eborderRadius = (Integer) element.get("borderRadius");
					String z = element.get("z") == null ? "''" : element.getString("z");
					String w = element.get("w") == null ? "''" : element.getString("w");
					String x = element.get("x") == null ? "''" : element.getString("x");
					String y = element.get("y") == null ? "''" : element.getString("y");
					String h = element.get("h") == null ? "''" : element.getString("h");
					String text = element.get("text") == null ? "''" : element.getString("text");
					h = "auto".equals(h) ? "auto;" : h + "px;";
					elementContent.append("z-index:").append(z).append(";");
					elementContent.append("width:").append(w).append("px;");
					elementContent.append("height:").append(h);
					elementContent.append("left:").append(x).append("px;");
					elementContent.append("top:").append(y).append("px;");
					elementContent.append("\">");
					//如果为背景
					if ("bg".equals(type)) {
						addBg = true;
					} else if ("image".equals(type)) {
						StringBuilder imageStr = new StringBuilder();
						imageStr.append("<img  src=\"" + src + "\" class=\"ele\" tag=\"'" + tag + "'\" style=\"width:" + w + "px;height:" + h + ";border-radius:" + eborderRadius + "px;").append("\"/>");
						elementContent.append(imageStr);
					} else if ("button".equals(type)) {
						StringBuilder buttonStr = new StringBuilder();
						//<div class="text" style="color:'+ element.style.color +';text-align:'+ element.style.textAlign+';font-size:'+ element.style.fontSize+';
						// width:'+element.w+'px; height:'+ element.h+'px;background-color:'+element.style.backgroundColor+';font-weight: '+(element.style.fontWeight||'normal')+';">'+element.text+'</div>
						buttonStr.append("<div class=\"text\" style=\"color:" + color).append(";text-align:").append(textAlign).append(";font-size:")
								.append(fontSize).append("width:").append(w).append("px;").append("height:").append(h).append("background-color:").
								append(backgroundColor).append("font-weight: ").append(fontWeight).append(";\">").append(text).append("</div>");
						elementContent.append(buttonStr);
					} else if ("text".equals(type)) {
						//<div class="text" style="color:'+ element.style.color +';text-align:'+ element.style.textAlign+';font-size:'+ element.style.fontSize+';
						// white-space: pre-line;font-weight: '+(element.style.fontWeight||'normal')+';">'+element.text+'</div>
						StringBuilder textStr = new StringBuilder();
						textStr.append("<div class=\"text\" style=\"color:" + color).append(";text-align:").append(textAlign).append(";font-size:")
								.append(fontSize).append(";white-space: pre-line;font-weight: ").append(fontWeight).append(";\">").append(text).append("</div>");
						elementContent.append(textStr);
					} else if ("audio".equals(type)) {
						StringBuilder audioStr = new StringBuilder();
						audioStr.append("<audio  src=\"" + src + "\" type=\"audio/mpeg\"  controls class=\"ele\">您的浏览器不支持 audio 元素。</audio>");
						elementContent.append(audioStr);
					} else if ("video".equals(type)) {
						StringBuilder videoStr = new StringBuilder();
						videoStr.append("<video  src=\"" + src + "\"  width=" + w + " controls id=\"'" + tag + "'\" >您的浏览器不支持Video标签。</video>");
						elementContent.append(videoStr);
					} else if ("music".equals(type)) {
						StringBuilder musicStr = new StringBuilder("<i class=\"icon-music\" ></i>");
						elementContent.append(musicStr);
					}

					elementContent.append(" </div>");
					htmlContent.append(elementContent);

				}
//                //插入背景
//                if (addBg) {
//                    String str = "<img class=\"ele-bg\" pre-src=\"" + bgSrc + "\">";
//                    htmlContent.insert("<section class=\"swiper-slide flat\">".length(), str);
//                }
				htmlContent.append("</div></section>");
				swiperWrapper.append(htmlContent.toString());
			}
			//生成一个新的html文件到指定路径
			h5html = doc.toString();
		} catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
		}
		return h5html;
	}
	/**
	 * 替换前端未处理的DIV及特殊字符
	 * @param content
	 */
	public String replaceDiv(String content){
		if(content.contains("<p>")){
			content = getStringForP(content);
		}else  if(content.contains("<div>")){
			content = getStringForDiv(content);
		}
		//二次处理替换特殊字符
		content = replaceHtmlSpecialSign(content);
		//删除最后一个\n
		if(content.lastIndexOf("\n") != -1){
			content = content.substring(0,content.lastIndexOf("\n"));
		}
		return  content;
	}

	/**
	 * /**
	 * 	 * 替换Chrome浏览器富媒体文本中主动换行浏览器自动添加的<p>标签
	 * 	 * @param content
	 * 	 * @return
	 *
	 * @return
	 */
	private  String getStringForDiv(String content){
		//字符串中一定是有div的
		StringBuilder sb = new StringBuilder();

		//如果字符串不是以div开头的,则需要先进行替换,br一定是以div或者P包裹
		if (content.indexOf("<div>") != 0) {
			String tempContent = content.substring(0, content.indexOf("<div>"));
			regexSpan(tempContent, sb);
			content = content.substring(content.indexOf("<div>"), content.length());
		}

		String regex = "<div.*?>(.*?)</div>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			//将div中的值赋StringBuilder
			setString(matcher.group(1), sb);
			//去除已经遍历过的div块
			content = content.substring(content.indexOf("</div>") + 6, content.length());
			//如果下面紧接着是div,则不做以下处理
			if (content.indexOf("<div>") == 0) {
				continue;
			}
			//如果content中不含有div,,则循环结束
			else if (content.indexOf("<div>") == -1) {
				regexSpan(content, sb);
				break;
			}
			//取下一个div之前的内容,进行添加处理
			String tempSpan = content.substring(0, content.indexOf("<div>"));
			//如果div之前的字段中没有span则直接插入
			if (tempSpan.indexOf("<span style=\"background-color: initial;\">") == -1) {
				sb.append(tempSpan);
				sb.append("\n");
			} else {
				regexSpan(tempSpan, sb);
			}
		}
		return sb.toString();
	}

	/**
	 * 替换IE系列浏览器富媒体文本中主动换行浏览器自动添加的<p>标签
	 * @param content
	 * @return
	 */
	private  String getStringForP(String content){
		//字符串中一定是有p的
		StringBuilder sb = new StringBuilder();

		//如果字符串不是以p开头的,则需要先进行替换,br一定是以div或者P包裹
		if (content.indexOf("<p>") != 0) {
			String tempContent = content.substring(0, content.indexOf("<p>"));
			regexSpan(tempContent, sb);
			content = content.substring(content.indexOf("<p>"), content.length());
		}

		String regex = "<p.*?>(.*?)</p>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			//将p中的值赋StringBuilder
			setString(matcher.group(1), sb);
			//去除已经遍历过的p块
			content = content.substring(content.indexOf("</p>") + 4, content.length());
			//如果下面紧接着是p,则不做以下处理
			if (content.indexOf("<p>") == 0) {
				continue;
			}
			//如果content中不含有p,,则循环结束
			else if (content.indexOf("<p>") == -1) {
				regexSpan(content, sb);
				break;
			}
			//取下一个p之前的内容,进行添加处理
			String tempSpan = content.substring(0, content.indexOf("<p>"));
			//如果p之前的字段中没有span则直接插入
			if (tempSpan.indexOf("<span style=\"background-color: initial;\">") == -1) {
				sb.append(tempSpan);
				sb.append("\n");
			} else {
				regexSpan(tempSpan, sb);
			}
		}
		return sb.toString();
	}

	private void regexSpan(String tempString,StringBuilder stringBuilder){
		String tempIndexSpan = "<span style=\"background-color: initial;\">";
		//如果是空则不做任何处理
		if(tempString == null || "".equals(tempString)){
			return;
		}
		//如果字符串中没有span
		if (tempString.indexOf(tempIndexSpan) == -1) {
			stringBuilder.append(tempString);
			stringBuilder.append("\n");
			return;
		}
		//如果首帧不是span,那么首先需要换行
		else if (tempString.indexOf(tempIndexSpan) != 0) {
			setFirstSpan(tempString, stringBuilder);
		}

		//获取span中的内容,插入stringBuilder中
		String regex = "<span.*?>(.*?)</span>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(tempString);
		while (matcher.find()) {
			String tempSpanStr = matcher.group(1);
			//将Span中的内容插入StringBuilder
			setString(tempSpanStr, stringBuilder);
			//去除已经遍历过的span内容
			tempString = tempString.substring(tempString.indexOf("</span>") + 7, tempString.length());
			//将第一个span之前的字符串插入StringBuilder中
			setFirstSpan(tempString, stringBuilder);
			//如果tempString还有span,则将span之前的截取掉
			if (tempString.contains(tempIndexSpan)) {
				tempString = tempString.substring(tempString.indexOf(tempIndexSpan), tempString.length());
			}
		}
	}

	/**
	 * 将第一span之前的字符串插入StringBuilder中
	 * @param tempString
	 * @param stringBuilder
	 */
	private void setFirstSpan(String tempString,StringBuilder stringBuilder){
		String tempIndexSpan = "<span style=\"background-color: initial;\">";
		//如果字符串中含有span
		if (tempString.contains(tempIndexSpan) && tempString.indexOf(tempIndexSpan) !=0){
			String tempContent = tempString.substring(0,tempString.indexOf(tempIndexSpan));
			stringBuilder.append(tempContent);
			stringBuilder.append("\n");
			//如果字符中没有span了
		}else if(!tempString.contains(tempIndexSpan) && !"".equals(tempString)){
			stringBuilder.append(tempString);
			stringBuilder.append("\n");
		}
	}


	/**
	 * 替换字符串中的特殊转义字符
	 * @param tempString
	 * @param sbContent
	 */
	public  void setString(String tempString,StringBuilder sbContent){
		sbContent.append(tempString+"\n");
	}
	/**
	 * 二次处理特殊字符
	 */
	public String replaceHtmlSpecialSign(String content){
		String tempString = content.replace("<span style=\"background-color: initial;\">","");
		tempString = tempString.replace("</span>","");
		tempString = tempString.replace("<div>","");
		tempString = tempString.replace("</div>","");
		tempString = tempString.replace("<p>","");
		tempString = tempString.replace("</p>","");
		tempString = tempString.replace("<br>","");
		tempString = tempString.replace("&nbsp;"," ");
		tempString = tempString.replace("&amp;", "&");
		tempString = tempString.replace("&lt;", "<");
		tempString = tempString.replace("&gt;", ">");
		tempString = tempString.replace("&quot;", "\"");
		return  tempString;
	}

    public String assemableHtmlFromJson(JSONObject object, String path, String templatePath,StringBuilder keyWordBuilder) {

        String h5html = null;
        try {
            //常规校验
            if (object == null || org.apache.commons.lang.StringUtils.isEmpty(path)) {
                throw new EMPException("参数为null");
            }
            //利用Dom解析H5的html模板文件
            TxtFileUtil fileUtil = new TxtFileUtil();
            String h5Html = fileUtil.readFileByLinesUTF8NoSplit(templatePath);
            Document doc = Jsoup.parse(h5Html);
//            Elements swiperWrapper = doc.getElementsByClass("swiper-wrapper");
            Element container = doc.getElementById("wrap");

            StringBuffer containerBuffer = new StringBuffer();

            //1.背景音乐
            JSONObject music = object.getJSONObject("music");
            if(null != music) {
                String src = music.getString("src");
                String autoPlay = music.getString("autoPlay");
                String loop = music.getString("loop");

                StringBuffer musicBuffer = new StringBuffer();
                musicBuffer.append("<audio type=\"audio/mpeg\" src=\"")
                .append(src)
                .append("\" autoplay=\"")
                .append(autoPlay)
                .append("\" loop=\"")
                .append(loop)
                .append("\" id=\"music\"></audio>");

                containerBuffer.append(musicBuffer.toString());
            }

            //2.swiper
            JSONObject swiperObj = object.getJSONObject("swiper");
            String direction = "";
            String effect = "";
            Object autoPlay;
            boolean loop = false;
            String delay = "";
            if(null != swiperObj) {
                direction = swiperObj.getString("direction");
                effect = swiperObj.getString("effect");

                autoPlay = swiperObj.get("autoPlay");
                if(!(autoPlay instanceof Boolean)) {
                    if(((JSONObject)autoPlay).containsKey("delay")) {
                        delay = ((JSONObject)autoPlay).getString("delay");
                    }
                }

                loop = swiperObj.getBoolean("loop");
            }

            //3.pages
            JSONArray pages = (JSONArray) object.get("pages");

            boolean multiple = pages.size() > 1;

            StringBuffer divBuffer = new StringBuffer();
            divBuffer.append("<div class=\"swiper-container\" data-direction=\"")
                    .append(direction).append("\" data-effect=\"").append(effect)
                    .append("\" data-delay=\"").append(delay).append("\" data-loop=\"")
                    .append(loop).append("\" data-allow-touch-move=\"")
                    .append(multiple).append("\">");

            containerBuffer.append(divBuffer.toString());

            StringBuffer wrapperBuffer = new StringBuffer("<div class=\"swiper-wrapper\">");

            //遍历pages
            for (Object pageObject : pages) {
                StringBuilder htmlContent = new StringBuilder("<section class=\"swiper-slide\">");
                JSONObject page = (JSONObject) pageObject;
                JSONObject bgJsonObj = page.getJSONObject("background");

                if(bgJsonObj != null) {
                    //背景透明度
                    float transparency = bgJsonObj.getFloatValue("transparency");
                    //背景颜色
                    String color = bgJsonObj.getString("color");
                    //背景图片
                    String pictureSrc = bgJsonObj.getString("src");
                    if(!StringUtils.IsNullOrEmpty(pictureSrc)) {
                        JSONObject cropObj = bgJsonObj.getJSONObject("crop");
                        StringBuffer bgPic = new StringBuffer();
                        if(cropObj != null) {
                            int w = cropObj.getInteger("w");
                            int h = cropObj.getInteger("h");
                            int left = cropObj.getInteger("left");
                            int top = cropObj.getInteger("top");
                            bgPic.append("<div class=\"bg-img-preview\" data-cropw=\"")
                                    .append(w)
                                    .append("\" data-croph=\"")
                                    .append(h)
                                    .append("\" style=\"opacity: ")
                                    .append(1 - transparency)
                                    .append("\">")
                                    .append("<img src=\"")
                                    .append(pictureSrc)
                                    .append("\" style=\"")
                                    .append(imgStyle(cropObj))
                                    .append("\" /></div>");
                        }
                        htmlContent.append(bgPic.toString());
                    } else if(!StringUtils.IsNullOrEmpty(color)) {
                        String colorDiv = "<div class=\"bg-color-preview\" style=\"background-color: " + color + "; opacity: " + (1 - transparency) + "\"></div>";
                        htmlContent.append(colorDiv);
                    }
                }

                htmlContent.append("<div class=\"page-view\">");

                //获取所有的元素集合
                JSONArray elements = (JSONArray) page.get("elements");
                //遍历elements
                for (Object elementObject : elements) {

                    StringBuilder elementContent = new StringBuilder("<div class=\"content\" style=\"");
                    JSONObject element = (JSONObject) elementObject;
                    elementContent.append(contentStyle(element)).append("\">");
                    //类型 text, pic, button, bg
                    String type = element.getString("type");
                    int x = element.getInteger("x");
                    int y = element.getInteger("y");
                    int z = element.getInteger("z");
                    int w = element.getInteger("w");

                    if("text".equals(type)) {
                        String text = element.getString("text");
                        elementContent.append("<div class=\"rich-text\" style=\"width: ")
                        .append(w)
                        .append("px;\">")
                        .append(text.trim())
                        .append("</div>");
						keyWordBuilder.append(text.trim());
                    } else if("image".equals(type)) {
                        int h = element.getInteger("h");
                        int borderRadius = element.getInteger("borderRadius");
                        String src = element.getString("src");
                        elementContent.append("<img src=\"")
                        .append(src)
                        .append("\" style=\"width: ")
                        .append(w)
                        .append("px; height: ")
                        .append(h)
                        .append("px; border-radius: ")
                        .append(borderRadius)
                        .append("px;\" />");
                    } else if("audio".equals(type)) {
                        String src = element.getString("src");
                        elementContent.append("<div class=\"audio\">")
                        .append("<audio src=\"")
                        .append(src)
                        .append("\" type=\"audio/mpeg\" controls>")
                        .append("您的浏览器不支持 audio 元素。</audio></div>");
                    } else if("music".equals(type)) {
                        boolean rotate = element.getBooleanValue("rotate");
                        elementContent.append("<div class=\"music\">")
                        .append("<i class=\"music-icon");
                        elementContent.append(rotate ? " rotate" : "")
                        .append("\"></i></div>");
                    } else if("video".equals(type)) {
                        int h = element.getInteger("h");
                        String src = element.getString("src");
                        String fistFramePath = element.getString("fistFramePath");
                        elementContent.append("<div class=\"video\">")
                        .append("<video width=\"")
                        .append(w)
                        .append("px\" height=\"")
                        .append(h)
                        .append("px\" src=\"")
                        .append(src)
                        .append("\" poster=\"")
                        .append(fistFramePath)
                        .append("\"> 您的浏览器不支持Video标签。</video>")
                        .append("<img class=\"video-btn\"")
                        .append("src=\"../../V1/image/play_icon.png\"")
                        .append("style=\"top: ")
                        .append((h - 46) / 2)
                        .append("px; left: ")
                        .append((w - 46) / 2)
                        .append("px;\"/></div>");
                    } else if("button".equals(type)) {
                        int h = element.getInteger("h");
                        String text = element.getString("text");
                        elementContent.append("<div class=\"btn\" style=\"width: ")
                        .append(w)
                        .append("px; height: ")
                        .append(h)
                        .append("px;\">")
                        .append("<div class=\"text\">")
                        .append(text).append("</div></div>");
                    }

                    elementContent.append("</div>");
                    htmlContent.append(elementContent);
                }

                htmlContent.append("</div></section>");
                wrapperBuffer.append(htmlContent.toString());
            }

            containerBuffer.append(wrapperBuffer.toString()).append("</div>");

            String pageAlign = swiperObj.getString("pageAlign");
            StringBuffer pageAlignStr = new StringBuffer();
            if(!StringUtils.IsNullOrEmpty(pageAlign)) {
                pageAlignStr.append("<div class=\"swiper-pagination swiper-pagination-fraction\" style=\"text-align:")
                        .append(pageAlign).append("\"></div>");
            }

            pageAlignStr.append("</div>");

            StringBuffer multipleStr = new StringBuffer();
            if(multiple) {
                multipleStr.append("<img src=\"../../V1/image/switch-guide.png\" class=\"switch-guide ")
                        .append(direction).append("\"></div>");
            }

            containerBuffer.append(pageAlignStr.toString()).append(multipleStr.toString());

            container.append(containerBuffer.toString());

            //生成一个新的html文件到指定路径
            h5html = doc.toString();
        } catch (Exception e) {
            EmpExecutionContext.error(e,"利用Dom解析H5模板html文件异常，方法：com.montnets.emp.rms.meditor.tools.JsonParseResourceTool.parseH5Html");
        }
        return h5html;
    }

    /**
     * 仿照前端js方法生成样式
     * @param jsonObject
     * @return
     */
    private String imgStyle(JSONObject jsonObject) {
        StringBuffer result = new StringBuffer("width: ");
        if(null != jsonObject) {
            JSONObject crop = jsonObject.getJSONObject("crop");
            if(null != crop) {
                JSONObject style = crop.getJSONObject("style");
                if(null != style) {
                    result.append(style.getInteger("width"))
                            .append("; height: ")
                            .append(style.getInteger("height"))
                            .append("; transform: ")
                            .append(style.getString("transform"));
                }
            }
        }
        return result.toString();
    }

    /**
     * 仿照前端js方法获取content样式(根据JsonObject对象生成带样式的html)
     * @param jsonObject
     * @return json的key:paddingLeft -> css的key: padding-left
     */
    private String contentStyle(JSONObject jsonObject) {
        StringBuffer result = new StringBuffer();
        StringBuffer temp = new StringBuffer();
        if(null != jsonObject) {
            JSONObject style = jsonObject.getJSONObject("style");
            if(null != style) {
                for(Map.Entry<String, Object> map : style.entrySet()) {
                    temp.setLength(0);
                    final int index = indexOfUpperChar(map.getKey());
                    //key对应无大写，如 "color:#fff"
                    if(0 == index) {
                        temp.append(map.getKey());
                    } else {
                        final String key = map.getKey().toLowerCase();
                        temp.append(key, 0, index)
                                .append("-").append(key, index, key.length());
                    }
                   result.append(temp).append(" : ").append(StringUtils.IsNullOrEmpty(map.getValue().toString()) ? "''" : map.getValue())
                    .append("; ");
                }
            }
            result.append("z-index: ").append(jsonObject.getString("z"))
            .append("; left: ").append(jsonObject.getString("x"))
            .append("px; top: ").append(jsonObject.getString("y"))
            .append("px;");
        }
        return result.toString();
    }

    /**
     * 首字母大写的位置
     * @param str
     * @return
     */
    private int indexOfUpperChar(String str) {
        int index = 0;
        if(StringUtils.IsNullOrEmpty(str)) {
            return index;
        }
        for(int i = 0; i < str.length(); i++) {
            if(Character.isUpperCase(str.charAt(i))) {
                index = i;
                break;
            }
        }
        return index;
    }


}
