package com.montnets.emp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 导出
 * @author Administrator
 *
 */
public class Export extends HttpServlet
{
	private static final long serialVersionUID = 342590465839109906L;

	private String contentType = "application/x-msdownload";

	private String enc = "utf-8";

	private String fileRoot = "";
    /**
     * 初使化方法
     */
	public void init(ServletConfig config) throws ServletException
	{
		String tempStr = config.getInitParameter("contentType");
		if (tempStr != null && !tempStr.equals(""))
		{
			contentType = tempStr;
		}
		tempStr = config.getInitParameter("enc");
		if (tempStr != null && !tempStr.equals(""))
		{
			enc = tempStr;
		}
		tempStr = config.getInitParameter("fileRoot");
		if (tempStr != null && !tempStr.equals(""))
		{
			fileRoot = tempStr;
		}
	}
    /**
     * doget请求
     */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		//String path = realUrl+"/";
		//path += StaticValue.FILE_UPLOAD_PATH;
		String filepath = request.getParameter("filepath");
        //正则验证 文件路径不能包含../或..\或 WEB-INF 或 以.js、.jsp、.properties、.xml、.css为扩展名的文件
        Pattern p = Pattern.compile("(\\.{2}[/\\\\])|WEB-INF|(\\.(js|css|jsp|java|properties|xml)$)",Pattern.CASE_INSENSITIVE);
		//正则验证 文件路径只能包含 zip、txt、xls、xlsx为扩展名的文件
        //Pattern p = Pattern.compile("\\.(zip|txt|xls|xlsx|rar)$");
        if(filepath == null || p.matcher(filepath).find()){
            EmpExecutionContext.error("文件路径存在非法字符，filepath:"+filepath);
            return;
        }
        File webRoot = new File(new TxtFileUtil().getWebRoot());
        File file = new File(webRoot,filepath);
        //限制文件在根目录下 且 文件存在
		if (file.getAbsolutePath().startsWith(webRoot.getAbsolutePath()) && file.exists())
		{
			String filename = URLEncoder.encode(file.getName(), enc);
			response.reset();
			response.setContentType(contentType);
			response.addHeader("Content-Disposition", "attachment; filename=\""
					+ filename + "\"");
			int fileLength = (int) file.length();
			response.setContentLength(fileLength);
			if (fileLength != 0)
			{
				InputStream inStream = null;
				ServletOutputStream servletOS = null;
				try{
					inStream = new FileInputStream(file);
					byte[] buf = new byte[4096];
					servletOS = response.getOutputStream();
					int readLength;
					while (((readLength = inStream.read(buf)) != -1))
					{
						servletOS.write(buf, 0, readLength);
					}
				}finally{
					if(null != inStream)
						inStream.close();
					if(null != servletOS){
						servletOS.flush();
						servletOS.close();
					}
				}
			}
		}

	}
	/**
	 * dopost请求
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}	
}
