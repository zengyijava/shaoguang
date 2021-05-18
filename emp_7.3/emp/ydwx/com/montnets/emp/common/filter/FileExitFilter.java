package com.montnets.emp.common.filter;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 * @project p_ydwx
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-10-29 下午02:14:19
 * @description
 */

public class FileExitFilter implements Filter
{

	public void destroy()
	{
		// TODO Auto-generated method stub
		
	}
	//主要处理预览中下载文件跳转到登录页面的处理
	public void doFilter(ServletRequest servletRequest,ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
	{
		//请求
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		//响应
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String path = request.getContextPath();
		String iPath = request.getScheme() + "://"+ request.getServerName() + ":"+ request.getServerPort();
		String strPath = iPath+ request.getRequestURI();
		//txt,doc,xls 
		if(request.getParameter("validate")!=null){
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}
		if(strPath.indexOf("file/wx/mater")>-1&&(strPath.lastIndexOf(".txt")>-1||strPath.lastIndexOf(".doc")>-1||strPath.lastIndexOf(".xls")>-1)){
	    	if(!isStaticFileExist(strPath)){ 
				response.sendRedirect(iPath+path+"/ydwx/wap/404.jsp");//用于页面显示错误页面
			}else{
				filterChain.doFilter(servletRequest, servletResponse);	
			}
		}else{
			filterChain.doFilter(servletRequest, servletResponse);
		}

	}

		public void init(FilterConfig arg0) throws ServletException
		{
			
		}
		
	//判断文件是否存在
	public boolean isStaticFileExist(String strPath) throws IOException {
		boolean blisExist = true;
		URL url=new URL(strPath+"?validate=1");
		
		String msg=url.openConnection().getHeaderField(0);
				if(msg.indexOf("404")!=-1){
					blisExist=false;
				}
		return blisExist;
	}
	

}
