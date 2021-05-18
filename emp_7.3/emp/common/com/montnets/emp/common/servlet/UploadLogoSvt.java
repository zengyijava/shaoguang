package com.montnets.emp.common.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.TxtFileUtil;

@SuppressWarnings("serial")
public class UploadLogoSvt extends BaseServlet{
	
	private static final String LOGO_PATH="/frame/frame3.0/img/";
	
	
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		String lgcorpcode = request.getParameter("lgcorpcode");
		if(lgcorpcode == null || "".equals(lgcorpcode)){
			lgcorpcode = "10001";
		}
		try {
			request.setAttribute("lgcorpcode", lgcorpcode);
			request.getRequestDispatcher("/frame/"+SystemGlobals.getValue(StaticValue.EMP_WEB_FRAME)+"/uploadLogo.jsp")
					.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "上传logo的find方法异常。");
		}
	}
	
	
	public void upload(HttpServletRequest request, HttpServletResponse response)
	{
		String dirUrl = new TxtFileUtil().getWebRoot();
		response.setHeader("Charset","UTF-8");
	    response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = null;
	    
		try {
			//String lgcorpcode = request.getParameter("lgcorpcode");
			//String corpcodedir = lgcorpcode + "/";
			out = response.getWriter(); 
			int width = 0;
			int height = 0;
			String imgUrl = "";
			//String tmsUrl = "";
			String fileName = "logo";  //文件服务器名称
			new TxtFileUtil().makeDir(dirUrl + LOGO_PATH);
			new TxtFileUtil().makeDir(dirUrl + LOGO_PATH );
			
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(100*1024);          
			factory.setRepository(new File(dirUrl + LOGO_PATH));
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = upload.parseRequest(request); 
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
			    FileItem fileItem = (FileItem) iter.next();
			   if (!fileItem.isFormField()
						&& fileItem.getName().length() > 0) {
			    	imgUrl = fileName+fileItem.getName().substring(fileItem.getName().lastIndexOf("."));   
			    	//图片的名称，加入图片格式
			    	String name = dirUrl + LOGO_PATH + imgUrl;
			    	fileItem .write(new File(name));		
			    	//写到服务器
			        BufferedImage bi = ImageIO.read(new File(name));
			    	width = bi.getWidth();					
			    	//宽
			    	height = bi.getHeight();				
			    	//高
				}
			}
			out.print(true);
		} catch (Exception e) {
			if(out != null){
				out.print(false);
			}
			EmpExecutionContext.error(e, "上传logo的upload方法异常。");
		}
	}
}
