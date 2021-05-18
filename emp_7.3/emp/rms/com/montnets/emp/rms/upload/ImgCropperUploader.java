package com.montnets.emp.rms.upload;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.tools.IMGTool;
import com.montnets.emp.rms.tools.MediaTool;
import com.montnets.emp.util.TxtFileUtil;

import sun.misc.BASE64Decoder;

public class ImgCropperUploader extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3102772976889578762L;

	private static final String targetImgFolder;
	private static final String toolPath;
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("SystemGlobals");
		toolPath = bundle.getString("montnets.rms.cropper.imgToolPath");
		targetImgFolder = bundle.getString("montnets.rms.cropper.imgSavePath");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// String path = req.getParameter("src");
			//req.setCharacterEncoding("UTF-8");
			resp.setContentType("text/html;charset=utf-8");//解决乱码
//		String clarity = req.getParameter("clarity");
//		String data = req.getParameter("data");
			StringBuilder sb = new StringBuilder();
			BufferedReader reader = req.getReader();
			char[] buff = new char[1024];
			int len;
			while ((len = reader.read(buff)) != -1) {
				sb.append(buff, 0, len);
			}
			reader.close();
			String requestString = sb.toString();
			String[] ss = requestString.split("&");
			String clarity = ss[0].split("=")[1];//取數字
			String data = ss[2];
			data = URLDecoder.decode(data, "UTF-8");
			String[] rs = saveImg(data, clarity);
			File imgFile = new File(new TxtFileUtil().getWebRoot()+rs[1]);
            EmpExecutionContext.info("图片裁剪返回路径:"+imgFile.getPath());
			//读取图片对象
			BufferedImage img = ImageIO.read(imgFile);
			//获得图片的宽
			int width = img.getWidth();
			//获得图片的高
			int height = img.getHeight();

			String ratio = String.valueOf(new BigDecimal(width).divide(new BigDecimal(height),2,RoundingMode.HALF_UP));

			String json = "{\"size\":\"" + rs[0] + "\",\"path\":\"" + rs[1] + "\",\"state\":\"0\",\"msg\":\"成功！\",\"width\":"+width+",\"height\":"+height+",\"ratio\":"+ratio+"}";
			resp.getWriter().write(json);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"图片裁剪异常！");
		}
	}

	private String[] saveImg(String data, String clarity) {
		double d = 50.0;
		if (clarity != null && clarity.matches("\\d+")) {
			d = Double.parseDouble(clarity);
		}
		Date date = new Date();
		SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfHMS = new SimpleDateFormat("HHmmss");
		String oldPath = "/new/" + sdfYMD.format(date) + "/" + sdfHMS.format(date);
		File f1 = new File(getPath() + oldPath);
		if (!f1.exists()) {
			f1.mkdirs();
		}

		String[] rs = new String[2];
		BASE64Decoder decoder = new BASE64Decoder();
		String[] arr = data.split("base64,");
		// 数据中：data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABI4AAAEsCAYAAAClh/jbAAA
		// ... 在"base64,"之后的才是图片信息
		String newFileName = UUID.randomUUID().toString();
		String newPath = targetImgFolder.substring(1) + "/" + newFileName + ".jpg";
		oldPath = oldPath + "/" + UUID.randomUUID().toString() + ".jpg";
		String picPath = getPath() + oldPath;
		String len = "0";
		try {
			byte[] buffer = decoder.decodeBuffer(arr[1]);
			OutputStream os = null;
			try{
				os = new FileOutputStream(picPath);
				os.write(buffer);
			}finally{
				if(os != null){
					try{
						os.close();
					}catch(IOException e){
						EmpExecutionContext.error(e,"IO异常");
					}
				}
			}
			File f = new File(picPath);
			if (f.exists()) {
				len = "" + f.length();
				String sourcePath = picPath;
				String targetPath = getPath() + targetImgFolder;
				File file = new File(targetPath);
				if (!file.exists()) {
					file.mkdirs();
				}
				if(("Windows").equals(MediaTool.toolPathType)){
					if (sourcePath.startsWith("/")) {
						sourcePath = sourcePath.substring(1);
					}
					if (targetPath.startsWith("/")) {
						targetPath = targetPath.substring(1);
					}
				}
				len = new IMGTool().compressIMG(sourcePath, targetPath, newFileName, d, toolPath) + "";
			}
		} catch (IOException e) {
			EmpExecutionContext.error(e,"图片裁剪异常！");
			throw new RuntimeException();
		}
		rs[0] = len;
		rs[1] = newPath;
		return rs;

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
/*		if(!("Windows").equals(MediaTool.toolPathType)){
			realUrl = "/"+realUrl;
		}*/
		return realUrl;
	}
}
