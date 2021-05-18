package com.montnets.emp.rms.tools;

import com.alibaba.fastjson.JSONArray;
import com.montnets.emp.common.context.EmpExecutionContext;
import org.apache.commons.collections4.map.HashedMap;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Jason Huang
 * @date 2018年1月12日 下午7:05:48
 */

public class IMGTool {
	// 装了ImageMagick还报FileNotFoundException:convert,需要在安装目录将magick.exe复制为convert.exe
	private static ResourceBundle bundle = ResourceBundle.getBundle("SystemGlobals");
	private static final String toolPath = bundle.getString("montnets.rms.cropper.imgToolPath");
	private static final String operatingSystem = bundle.getString("montnets.rms.videoEncoder.system");

	/**
	 *生成缩略图
	 * @param width   宽
	 * @param height  高
	 * @param srcImg  原图路径
	 * @param newPath 生成的缩略图路径
	 */
	public void createThumbnail(Integer width, Integer height, String srcImg,String newPath) {
		IMOperation op = new IMOperation();
		op.addImage(srcImg);
		// 仅当原宽高大于设定值时(以原宽或原高大于设定值的较大者为准)才进行缩放-->改为直接缩放成给定宽高
		//op.adaptiveResize(width, height,"!");
		op.resize(width, height,">!");
		op.addImage(newPath);
		ConvertCmd cmd = new ConvertCmd();
		// Windows需要设置,Linux不需要
		if(operatingSystem.equals("Windows")) cmd.setSearchPath(toolPath);
		try {
			EmpExecutionContext.info("图片裁剪: "+op.toString());
			cmd.run(op);
		} catch (Exception e) {
			System.out.println("ImageMagick工具执行异常!极大可能没有配置正确的工具路径!");
			EmpExecutionContext.error(e, "ImageMagick工具执行异常!");
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String,String> buildCompositeIMG(Elements outsideEles, String srcImg, String imgName, Integer width, Integer height) {
		Map<String,String> resultMap= new HashedMap<String,String>();
		FormatFontTool formatFontTool = new FormatFontTool();
		List<Map<String, Object>> list = formatFontTool.convertHTML(outsideEles);
		List<Map<String, String>> paragraphList = new ArrayList<Map<String, String>>();
		boolean isDynamic = false;
		int x = 1;// 命名计数
		String path = srcImg.substring(0, srcImg.lastIndexOf("/"));

		// 调整图片大小
		convertCmd(resizeOperation(width, height, srcImg));

		IMOperation compositeOp = new IMOperation();
		compositeOp.addImage(srcImg);

		for (Map<String, Object> map : list) {
			// 如果该段文字含有参数,则将map添加到数组用以生成报文,否则生成合成图片
			if ((Boolean) map.get("isDynamic")) {
				paragraphList.add((Map<String, String>) map.get("styleMap"));
				if (!isDynamic) {
					isDynamic = true;
				}
			} else {
				String fontImg = path + "/font" + (x++) + ".png";
				Map<String, String> styleMap = (Map<String, String>) map.get("styleMap");
				convertCmd(fontOperation(map.get("pango").toString(), styleMap, fontImg));

				compositeOp.addImage(fontImg);
				compositeOp.addRawArgs("-gravity", "northwest");// 对齐方式
				compositeOp.addRawArgs("-geometry", "+" + styleMap.get("left") + "+" + styleMap.get("top"));// 偏移坐标
				compositeOp.composite();
			}
		}
		String resultImg = srcImg.substring(0, srcImg.lastIndexOf(".")) + "-c.png";
		srcImg = resultImg;
		compositeOp.addImage(srcImg);
		convertCmd(compositeOp);

		if (isDynamic) {
			MessageTool messageTool = new MessageTool();
			String message = messageTool.buildFontMessage(paragraphList, imgName + ".jpg");
			resultMap.put("message", message);
		}
		if (srcImg.contains(".png")) {
			String jpgImg = srcImg.substring(0, srcImg.lastIndexOf("png")) + "jpg";
			convertCmd(typeOperation(srcImg, jpgImg));
			srcImg = jpgImg;
		}

		resultMap.put("srcImg", srcImg);
		return resultMap;
	}
	public Map<String,String> buildCompositeIMG(JSONArray jsonArray, String srcImg, String imgName, Integer width, Integer height) {
		Map<String,String> resultMap= new HashedMap<String,String>();
		FormatFontTool formatFontTool = new FormatFontTool();
		List<Map<String, Object>> list = formatFontTool.convertJSON(jsonArray,new StringBuilder());
		List<Map<String, String>> paragraphList = new ArrayList<Map<String, String>>();
		boolean isDynamic = false;
		int x = 1;// 命名计数
		String path = srcImg.substring(0, srcImg.lastIndexOf("/"));

		// 调整图片大小
		convertCmd(resizeOperation(width, height, srcImg));

		IMOperation compositeOp = new IMOperation();
		compositeOp.addImage(srcImg);

		for (Map<String, Object> map : list) {
			// 如果该段文字含有参数,则将map添加到数组用以生成报文,否则生成合成图片
			if ((Boolean) map.get("isDynamic")) {
				paragraphList.add((Map<String, String>) map.get("styleMap"));
				if (!isDynamic) {
					isDynamic = true;
				}
			} else {
				String fontImg = path + "/font" + (x++) + ".png";
				Map<String, String> styleMap = (Map<String, String>) map.get("styleMap");
				convertCmd(fontOperation(map.get("pango").toString(), styleMap, fontImg));

				compositeOp.addImage(fontImg);
				compositeOp.addRawArgs("-gravity", "northwest");// 对齐方式
				compositeOp.addRawArgs("-geometry", "+" + styleMap.get("left") + "+" + styleMap.get("top"));// 偏移坐标
				compositeOp.composite();
			}
		}
		String resultImg = srcImg.substring(0, srcImg.lastIndexOf(".")) + "-c.png";
		srcImg = resultImg;
		compositeOp.addImage(srcImg);
		convertCmd(compositeOp);

		if (isDynamic) {
			MessageTool messageTool = new MessageTool();
			String message = messageTool.buildFontMessage(paragraphList, imgName + ".jpg");
			resultMap.put("message", message);
		}
		if (srcImg.contains(".png")) {
			String jpgImg = srcImg.substring(0, srcImg.lastIndexOf("png")) + "jpg";
			convertCmd(typeOperation(srcImg, jpgImg));
			srcImg = jpgImg;
		}

		resultMap.put("srcImg", srcImg);
		return resultMap;
	}

	public void convertCmd(IMOperation op) {
		// GraphicsMagick具有许多优势,其中最突出的是它的卓越性能,可能部分命令不兼容原因报错
		// ConvertCmd cmd = new ConvertCmd(true);
		ConvertCmd cmd = new ConvertCmd();
		// Windows需要设置,Linux不需要
		if("Windows".equalsIgnoreCase(operatingSystem)) cmd.setSearchPath(toolPath);
		try {
			cmd.run(op);
		} catch (Exception e) {
			System.out.println("ImageMagick工具执行异常!极大可能没有配置正确的工具路径!");
			EmpExecutionContext.error(e, "ImageMagick工具执行异常!");
		}
	}

	private IMOperation resizeOperation(Integer width, Integer height, String srcImg) {
		IMOperation op = new IMOperation();
		op.addImage(srcImg);
		// 仅当原宽高大于设定值时(以原宽或原高大于设定值的较大者为准)才进行缩放-->修改为：直接缩放成给定的宽高
		op.adaptiveResize(width, height,"!");// ! 为 不按照原图的宽高比例进行缩放，而是按照实际给的宽高进行缩放
		op.addImage(srcImg);
//System.out.println(op.getCmdArgs());
		return op;
	}

	private IMOperation fontOperation(String pango, Map<String, String> map, String fontImg) {
		IMOperation op = new IMOperation();
		op.addRawArgs("-background", "transparent");// 透明背景色
		if (map.containsKey("gravity")) {
			// 左对齐不设置参数
			if (map.get("gravity").equals("center")) {
				op.addRawArgs("-gravity", "center");// 对齐方式
			} else if (map.get("gravity").equals("right")) {
				// TODO 目前右对齐方式有问题
			}
		}
		if (map.containsKey("size")) {
			op.addRawArgs("-size", map.get("size"));// 文字块宽度(即图片宽度)
		}
		if (map.containsKey("rotate")) {
			op.addRawArgs("-rotate", map.get("rotate"));// 旋转角度
		}
		op.addRawArgs(pango);
		// 生成图片的路径,文字图片只能是png格式,否则无法背景色无法透明
		op.addRawArgs(fontImg);
		// System.out.println(op.getCmdArgs());
		return op;
	}

	public IMOperation compositeOperation(String srcImg, String fontImg, String resultImg, String left, String top) {
		IMOperation op = new IMOperation();
		op.addRawArgs(srcImg);// 背景图
		op.addRawArgs(fontImg);// 文字图
		op.addRawArgs("-gravity", "northwest");// 对齐方式
		op.addRawArgs("-geometry", "+" + left + "+" + top);// 偏移坐标
		// 生成合成图片的路径,合成图片必须为png格式,否则文字将变得模糊,如需jpg格式可之后再转
		op.addRawArgs("-composite", resultImg);
		// System.out.println(op.getCmdArgs());
		return op;
	}

	private IMOperation typeOperation(String srcImg, String resultImg) {
		IMOperation op = new IMOperation();
		op.addImage(srcImg);
		op.addImage(resultImg);
		return op;
	}

	/**
	 * 图片压缩处理方法
	 * @param srcPath 音视频源文件路径(包含文件名,不能使用反斜杠"\\")
	 * @param targetPath 压缩、裁剪后文件存放路径(不包含文件名,目标文件夹不存在会自动创建,不能使用反斜杠"\\",结尾处不需要"/")
	 * @param name 目标文件名(不需要文件类型扩展名)
	 * @param quality 图片质量0.0-100.0
	 * @param toolPath Windows下ImageMagick的安装路径,如:D:\\ImageMagick-7.0.7-Q16
	 * @return 压缩后的图片大小(单位B)
	 */
	public long compressIMG(String srcPath, String targetPath, String name, double quality, String toolPath) {
		// 确保目标文件夹存在
		File folder = new File(targetPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		String tempPath = targetPath + "/" + name + ".jpg";
		ConvertCmd cmd = new ConvertCmd();
		IMOperation operation = new IMOperation();

		// Windows需要设置,Linux不需要
		if(operatingSystem.equals("Windows")) cmd.setSearchPath(toolPath);

		operation.addImage(srcPath);
		operation.strip(); // 去除拍摄参数
		// operation.crop(1344, 756, 168, 157); //裁剪图片:宽,高,X,Y
		operation.adaptiveResize(500, null, ">");// 宽度大于500px时才进行裁剪
		operation.adaptiveResize(null, 505, ">");//高度大于500px时才进行裁剪
		operation.addRawArgs("-quality", (int)quality+"%");
		operation.addImage(tempPath);
		EmpExecutionContext.info("裁剪图片指令:"+operation.toString());
		try {
			cmd.run(operation);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "图片压缩异常！");
		}
		return new File(tempPath).length();
	}

	public IMOperation fontOperation(String str, String fontType, String fontSize, String colorStr, String width, String backgroundColorStr, String fbold, String fagen, String outfile) {
		IMOperation op = new IMOperation();
		op.addRawArgs("-background", backgroundColorStr);// 透明背景色
//		String gravity = "";
//		switch (fagen) {
//		case 0:
//			gravity = "WestGravity";
//			break;
//		case 1:
//			gravity = "CenterGravity";
//			break;
//		case 2:
//			gravity = "EastGravity";
//			break;
//		default:
//			break;
//		}
//		op.addRawArgs("-gravity", gravity);// 对齐方式
		op.addRawArgs("-gravity", "center");
		op.addRawArgs("-size", "200x");// 文字块宽度(即图片宽度)
		op.addRawArgs("-rotate", "0");// 旋转角度
		String weight = fbold.equals("1")?"bold":"normal";
//		op.addRawArgs("pango:<span font='"+fontSize+"px' weight='"+weight+"' face='"+fontType+"' foreground='"+colorStr+"' background='"+backgroundColorStr+"'>"+str+"</span>");
		op.addRawArgs("pango:<span font='"+fontSize+"px' weight='"+weight+"' face='"+fontType+"' foreground='"+colorStr+"' >"+str+"</span>");
		op.addRawArgs(outfile);// 生成图片的路径
//System.out.println(op.getCmdArgs());
		return op;
	}


	/**
	 * Json数据解析放到图片合成器缓存内
	 * @param jsonArray 页面json数据
	 * @param srcImg 底图路径
	 * @param imgName 最终底图名称
	 * @param width 底图最终宽度
	 * @param height 底图最终高度
	 * @return
	 */
	public Map<String,String> buildCompositeECIMG(JSONArray jsonArray, String srcImg, String imgName, Integer width, Integer height,StringBuilder keyWordBuilder) {
		Map<String,String> resultMap= new HashedMap<String,String>();
		List<Map<String, String>> paragraphList = new ArrayList<Map<String, String>>();

		// 调整图片大小
		convertCmd(resizeOperation(width, height, srcImg));

		IMOperation compositeOp = new IMOperation();
		compositeOp.addImage(srcImg);

		// 将文字转换成对应的图片
		boolean isDynamic = putJson2IMOperation(jsonArray, paragraphList, srcImg, compositeOp,keyWordBuilder);

		String resultImg = srcImg.substring(0, srcImg.lastIndexOf(".")) + "-c.png";
		srcImg = resultImg;
		compositeOp.addImage(srcImg);
		convertCmd(compositeOp);

		if (isDynamic) {
			MessageTool messageTool = new MessageTool();
			String message = messageTool.buildFontMessage(paragraphList, imgName + ".jpg");
			resultMap.put("message", message);
		}
		if (srcImg.contains(".png")) {
			String jpgImg = srcImg.substring(0, srcImg.lastIndexOf("png")) + "jpg";
			convertCmd(typeOperation(srcImg, jpgImg));
			srcImg = jpgImg;
		}

		resultMap.put("srcImg", srcImg);
		return resultMap;
	}

	/**
	 * 将json数据缓存到图片合成器缓存内
	 * @param jsonArray 除了底图之外的json数据
	 * @param paragraphList 文本数据数据
	 * @param srcImg 底图路径，用于替换合成图片的绝对路径
	 * @param compositeOp 图片缓存器
	 * @return
	 */
	private boolean putJson2IMOperation(JSONArray jsonArray,
										List<Map<String, String>> paragraphList,
										String srcImg, IMOperation compositeOp,StringBuilder keyWordBuilder) {
		FormatFontTool formatFontTool = new FormatFontTool();
		String path = srcImg.substring(0, srcImg.lastIndexOf("/"));
		boolean isDynamic = false;
		List<Map<String, Object>> list = formatFontTool.convertJSON(jsonArray,keyWordBuilder);
		int x = 1;// 命名计数
		for (Map<String, Object> map : list) {
			// 如果该段文字含有参数,则将map添加到数组用以生成报文,否则生成合成图片
			if ((Boolean) map.get("isDynamic")) {
				paragraphList.add((Map<String, String>) map.get("styleMap"));
				if (!isDynamic) {
					isDynamic = true;
				}
			} else if("text".equals(map.get("type"))){
				String fontImg = path + "/font" + (x++) + ".png";
//System.out.println("fontImg:"+fontImg);
				Map<String, String> styleMap = (Map<String, String>) map.get("styleMap");
				// 将文字生成对应的图片
				convertCmd(fontOperation(map.get("pango").toString(), styleMap, fontImg));

				// 文字图片文件缓存到图片合成器
				compositeOp.addImage(fontImg);
				compositeOp.addRawArgs("-gravity", "northwest");// 对齐方式
				compositeOp.addRawArgs("-geometry", "+" + styleMap.get("left") + "+" + styleMap.get("top"));// 偏移坐标
				compositeOp.composite();
			}else if("image".equals(map.get("type"))){
				String imgPath = (String)map.get("imgPath");
//				imgPath = imgPath.substring(imgPath.lastIndexOf("/"));
//				imgPath = imgPath.substring(imgPath.substring(1).indexOf("/")+1);
				imgPath = getPath()+"/"+imgPath;
				// 调整添加的图片宽高，适应底图
				convertCmd(resizeOperation(Integer.parseInt((String)map.get("w")), Integer.parseInt((String)map.get("h")), imgPath));

				// 添加的图片文件缓存到图片合成器
				compositeOp.addImage(imgPath);
				compositeOp.addRawArgs("-gravity", "northwest");// 对齐方式
				compositeOp.addRawArgs("-geometry", "+" + map.get("left") + "+" + map.get("top"));// 偏移坐标
				compositeOp.composite();
			}
		}
		return isDynamic;
	}

	private String getPath() {
		String realUrl = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String newUrl = "";
		if (realUrl.contains("/WEB-INF/classes")) {
			try {
				newUrl = URLDecoder.decode(realUrl.substring(0, realUrl.indexOf("WEB-INF/classes") - 1), "UTF-8");
			} catch (Exception e) {
				EmpExecutionContext.error(e,"图片裁剪获取项目路径异常！");
			}
		}

		realUrl = newUrl.replace("%20", " ");// 此路径不兼容jboss
		if(!("Windows").equals(MediaTool.toolPathType)){
			realUrl = "/"+realUrl;
		}else if(("Windows").equals(MediaTool.toolPathType) && realUrl.indexOf("/") != -1){
			realUrl = realUrl.substring(1);
		}
		return realUrl;
	}

}
