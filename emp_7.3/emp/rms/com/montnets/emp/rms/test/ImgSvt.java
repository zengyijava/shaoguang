package com.montnets.emp.rms.test;

import com.montnets.emp.common.context.EmpExecutionContext;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.UUID;

public class ImgSvt extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8026288538564048603L;

	private static final String IMG_PATH = "/ueditor/jsp/upload/image";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String json="{\"size\":\"0\",\"path\":\"\"}";
		String imgInfo = req.getParameter("imgInfo");
		String empPath = req.getRequestURI().substring(1);
		empPath = empPath.substring(0, empPath.indexOf("/"));
		if (imgInfo != null && !"".equals(imgInfo)) {
			String[] s = decodeBase64(imgInfo);
			json = "{\"size\":\""+s[0]+"\",\"path\":\""+s[1].substring(1)+"\"}";
//			json = "{\"size\":\""+s[0]+"\",\"path\":\""+"/" + empPath + s[1]+"\"}";
		} else {
			System.out.println("图片信息为空!");
		}
//		resp.getWriter().write("/" + empPath + rsPath);
		resp.getWriter().write(json);
	}

	private String[] decodeBase64(String base64Info) {
		String[] rs = new String[2];
		BASE64Decoder decoder = new BASE64Decoder();
		String[] arr = base64Info.split("base64,");
		// 数据中：data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABI4AAAEsCAYAAAClh/jbAAA
		// ... 在"base64,"之后的才是图片信息
		String picPath = IMG_PATH + "/" + UUID.randomUUID().toString() + ".png";
		String len = "0";
		try {
			byte[] buffer = decoder.decodeBuffer(arr[1]);
			OutputStream os =null;
			try{
				os= new FileOutputStream(getPath() + picPath);
				os.write(buffer);
			}finally{
				if(os != null){
					os.close();
				}
			}
			File f = new File(getPath() + picPath);
			if(f.exists()){
				len=""+f.length();
		        /*BufferedImage sourceImg =ImageIO.read(new FileInputStream(f)); 
		        System.out.println(String.format("%.1f",f.length()/1024.0));
		        System.out.println(sourceImg.getWidth());
		        System.out.println(sourceImg.getHeight());*/
			}
		} catch (IOException e) {
			throw new RuntimeException();
		}
		rs[0]=len;
		rs[1]=picPath;
		return rs;
	}

	private String getPath() {
		String realUrl = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String newUrl = "";
		if (realUrl.contains("/WEB-INF/classes")) {
			try {
				newUrl = URLDecoder.decode(realUrl.substring(0, realUrl.indexOf("WEB-INF/classes") - 1), "UTF-8");
			} catch (Exception e) {
                EmpExecutionContext.error(e, "发现异常");
			}
		}

		realUrl = newUrl.replace("%20", " ");// 此路径不兼容jboss
		return realUrl;
	}
}
