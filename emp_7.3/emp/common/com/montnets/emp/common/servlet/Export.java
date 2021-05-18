package com.montnets.emp.common.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.util.TxtFileUtil;

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
	private static final String DOWN_PATH = "template";
    /**
     * 初使化方法
     */
	@Override
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
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String realUrl =   new TxtFileUtil().getWebRoot();
	 
		String path = realUrl+"/";
		path += StaticValue.FILE_UPLOAD_PATH;
		
		String filepath = request.getParameter("filepath");
		String fullFilePath = path + filepath;
		File file = new File(fullFilePath);
		if (file.exists())
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
				try {
					inStream = new FileInputStream(file);
					byte[] buf = new byte[4096];
					servletOS = response.getOutputStream();
					int readLength;
					while (((readLength = inStream.read(buf)) != -1))
					{
						servletOS.write(buf, 0, readLength);
					}
					servletOS.flush();
				}catch (Exception e){
                    EmpExecutionContext.error(e, "发现异常！");
				}finally {
					SysuserUtil.closeStream(inStream);
					SysuserUtil.closeStream(servletOS);
				}

			}
		}

	}
	/**
	 * dopost请求
	 */
	@Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}	
}
