package com.montnets.emp.rms.upload;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.meditor.tools.String2FileUtil;
import com.montnets.emp.rms.tools.MediaTool;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

public class VideoCropperUploader extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3095515663576448610L;

	private static final String targetVideoFolder;
	private static final String toolPath;
	private static final String rootPath;

	static {
		ResourceBundle bundle = ResourceBundle.getBundle("SystemGlobals");
		toolPath = bundle.getString("montnets.rms.cropper.videoToolPath");
		targetVideoFolder = bundle.getString("montnets.rms.cropper.videoSavePath");
		rootPath = bundle.getString("montnet.rms.nginx.rootPath");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	private String getPath() {
		String realUrl = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String newUrl = "";
		if (realUrl.contains("/WEB-INF/classes")) {
			try {
				newUrl = URLDecoder.decode(realUrl.substring(0, realUrl.indexOf("WEB-INF/classes") - 1), "UTF-8");
			} catch (Exception e) {
				EmpExecutionContext.error(e,"视频裁剪获取项目路径异常！");
			}
		}

		realUrl = newUrl.replace("%20", " ");// 此路径不兼容jboss
		if(!("Windows").equals(MediaTool.toolPathType)){
			realUrl = "/"+realUrl;
		}
		return realUrl;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");//解决乱码
		String path = req.getParameter("src");
		//根据path确定路径
		String basePath = null;
		String relativePath = null;
		String physicalPath = null;
		if(path.indexOf("http")==0){
			//basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/";
			relativePath = path.substring(path.indexOf(rootPath),path.length());
			//relativePath= path.replace(basePath, "");
			physicalPath = getPath() + "/" + relativePath;
		}else{
			basePath = req.getContextPath();
			relativePath = path.replace(basePath, "");
			physicalPath = getPath() + "/" +  relativePath;
		}
		long size = 0L;
		File f = new File(physicalPath.substring(1));
		if (f.exists()) {
			size = f.length();
		}else{
			try {
				String2FileUtil.downloadAV(path);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"下载资源失败");
			}
		}
		int start = 0;// 开始时间
		int end = 0;// 结束时间
		int clarity = 0;// 清晰度(标准，高清，超清)
		int width = 10;// 视频宽度
		int height = 50;// 视频高度
		String type = req.getParameter("type");

		String startTime = req.getParameter("startTime");
		if (startTime != null && startTime.matches("\\d+")) {
			start = Integer.parseInt(startTime);
		} else {
			resp.getWriter().write("{\"size\":\"" + size + "\",\"path\":\"" + path + "\",\"state\":\"-1\",\"msg\":\"开始时间不正确！\"}");
			return;
		}
		String endTime = req.getParameter("endTime");
		if (endTime != null && endTime.matches("\\d+")) {
			end = Integer.parseInt(endTime);
		} else {
			resp.getWriter().write("{\"size\":\"" + size + "\",\"path\":\"" + path + "\",\"state\":\"-2\",\"msg\":\"结束时间不正确！\"}");
			return;
		}
		String clarityStr = req.getParameter("clarity");
		if (clarityStr != null && clarityStr.matches("\\d+")) {
			clarity = Integer.parseInt(clarityStr);
		} else {
			resp.getWriter().write("{\"size\":\"" + size + "\",\"path\":\"" + path + "\",\"state\":\"-3\",\"msg\":\"清晰度不正确！\"}");
			return;
		}
		String widthStr = req.getParameter("width");
		if (widthStr != null && widthStr.matches("\\d+")) {
			width = Integer.parseInt(widthStr);
		} else {
			resp.getWriter().write("{\"size\":\"" + size + "\",\"path\":\"" + path + "\",\"state\":\"-4\",\"msg\":\"视频帧宽不正确！\"}");
			return;
		}
		String heightStr = req.getParameter("height");
		if (heightStr != null && heightStr.matches("\\d+")) {
			height = Integer.parseInt(heightStr);
		} else {
			resp.getWriter().write("{\"size\":\"" + size + "\",\"path\":\"" + path + "\",\"state\":\"-4\",\"msg\":\"视频帧高不正确！\"}");
			return;
		}

		try {
			MediaTool videoTool = new MediaTool();
			// String fileName =
			// relativePath.substring(relativePath.lastIndexOf("/")+1);源文件名称
			String newFileName = UUID.randomUUID().toString();
			String relativeFolder = relativePath.substring(0, relativePath.lastIndexOf("/"));
			String relativeCropperFolder = targetVideoFolder + relativeFolder.substring(relativeFolder.lastIndexOf("/"));
			String cropperFolder = getPath() + relativeCropperFolder;
			relativeCropperFolder = relativeCropperFolder.substring(1);
			String newPath = relativeCropperFolder + "/" + newFileName + ".mp4";
			
			boolean isEncode = false;// 是否需要转码,true:需要转码,false:不需要转码
			if (size * 8 / (end - start) - clarity > 0) {
				isEncode = true;
			}
			int rs = -1;
			if("0".equals(type)){
				rs = videoTool.compressVideo(toolPath, physicalPath.substring(1), cropperFolder.substring(1), newFileName, isEncode, 2, clarity, start, end, width, height);
			}else{
				rs = videoTool.compressVideo(toolPath, physicalPath.substring(1), cropperFolder.substring(1), newFileName, isEncode, 1, clarity, start, end, width, height);
			}
			if (0 == rs) {
				File nf = new File(cropperFolder.substring(1) + "/" + newFileName + ".mp4");
				if (nf.exists()) {
					size = nf.length();
				}
				Encoder encoder = new Encoder();
				MultimediaInfo m = encoder.getInfo(nf);
				Long duration = m.getDuration();
				// String json = "{\"size\":\"" + size + "\",\"path\":\"" +
				// relativeCropperFolder+"/"+fileName +
				// "\",\"state\":\"0\",\"msg\":\"成功！\"}";
				String json = "{\"size\":\"" + size + "\",\"path\":\"" + newPath+ "\",\"duration\":\"" + duration + "\",\"state\":\"0\",\"msg\":\"成功！\"}";

				resp.getWriter().write(json);

			} else {
				resp.getWriter().write("{\"size\":\"" + size + "\",\"path\":\"" + path + "\",\"state\":\"-5\",\"msg\":\"视频裁剪失败！\"}");
				return;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"视频裁剪异常！");
			resp.getWriter().write("{\"size\":\"" + size + "\",\"path\":\"" + path + "\",\"state\":\"-6\",\"msg\":\"视频裁剪失败！\"}");
		}

	}

}
