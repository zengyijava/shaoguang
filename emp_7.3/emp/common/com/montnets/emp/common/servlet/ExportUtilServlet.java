package com.montnets.emp.common.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.TxtFileUtil;

@SuppressWarnings("serial")
public class ExportUtilServlet extends HttpServlet{

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//输入流
		InputStream inStream = null;
		//读取文本
		BufferedReader reader = null;
		//输出流
		ServletOutputStream servletOS = null;
		//文件地址
		String url = "";
		try
		{
			//换行符
			String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);
			//文件地址
			url = request.getParameter("u");
            //正则验证 文件路径不能包含../或..\或 WEB-INF 或 以.js或.jsp为扩展名的文件
            Pattern p = Pattern.compile("(\\.{2}[/\\\\])|WEB-INF|(\\.(js|jsp)$)",Pattern.CASE_INSENSITIVE);
            if(url == null || p.matcher(url).find()){
                EmpExecutionContext.error("文件路径存在非法字符，u:"+url);
                return;
            }
            //验证URL地址合法性
            if(url.length() < 6 || (!"file".equals(url.substring(0,4)) && !"common".equals(url.substring(0,6))))
            {
            	 EmpExecutionContext.error("文件下载路径为非法路径，u:"+url);
                 return;
            }
            File webRoot = new File(new TxtFileUtil().getWebRoot());
			File file = new File(webRoot,url);
            //限制文件在根目录下 且 文件存在
			if (file.getAbsolutePath().startsWith(webRoot.getAbsolutePath()) && file.exists())
			{
				String filename = URLEncoder.encode(file.getName(), "utf-8");
				response.reset();
				response.setContentType("application/x-msdownload");
				response.addHeader("Content-Disposition", "attachment; filename=\""
						+ filename + "\"");
				int fileLength = (int) file.length();
				//response.setContentLength(fileLength);
				//设置字符编码格式
				response.setCharacterEncoding("GBK");
				//文件类型
				String fileType="";
				if(filename!=null)
				{
					
					fileType=filename.substring(filename.lastIndexOf("."));
				}
				//如果是linux下的服务器（解决linux下换行文本显示问题）
				if("\n".equals(line)&&".txt".equals(fileType))
				{
					String tmp;
					servletOS = response.getOutputStream();
					byte[] buf ;
					if (fileLength != 0)
					{
						//输入流
						inStream = new FileInputStream(file);
						reader = new BufferedReader(
								new InputStreamReader(inStream,"GBK"));
						while ((tmp = reader.readLine()) != null)
						{
							//\r\n换行
							tmp = tmp + "\r\n";
							buf = tmp.getBytes("GBK");
							servletOS.write(buf, 0, buf.length);
						}
					}
				}
				else
				{
					if (fileLength != 0)
					{
						inStream = new FileInputStream(file);
						// 一次读多个字节
						byte[] buf = new byte[4096];
						servletOS = response.getOutputStream();
						int readLength;
						// 读入多个字节到字节数组中
						while (((readLength = inStream.read(buf)) != -1))
						{
							servletOS.write(buf, 0, readLength);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			if (e instanceof SocketException){
				if(e.getMessage().indexOf("Software caused connection abort: socket write error") >= 0){
					EmpExecutionContext.error(e,"客户端浏览器取消下载，文件下载终止，文件地址："+url);
					//请求日志
					EmpExecutionContext.logRequestUrl(request, "后台请求");
				}
			}else{
				//异常信息打印
				EmpExecutionContext.error(e, "文件下载异常。文件地址:" + url);
				//请求日志
				EmpExecutionContext.logRequestUrl(request, "后台请求");
			}
		}
		finally
		{
			if(inStream != null)
			{
				//关闭流
				try
				{
					inStream.close();
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "文件下载关闭输入流异常！");
				}
			}
			if(reader != null)
			{
				try
				{
					reader.close();
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "文件下载关闭读取流异常！");
				}
			}
			if(servletOS != null)
			{
				try
				{
					//刷新流(下载过程中客户端关闭，会出现异常)
					//servletOS.flush();
					servletOS.close();
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "文件下载关闭输出流异常！");
				}
			}
		}
	}

	//doget方法
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		//调用doPost方法
		doPost(request,response);
	}


}
