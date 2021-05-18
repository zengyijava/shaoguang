/**
 * Program  : AppHomeImgLoaderServlet.java
 * Author   : zousy
 * Create   : 2014-6-20 上午10:21:41
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.appmage.filter;

import com.montnets.emp.appwg.biz.WgMwFileBiz;
import com.montnets.emp.common.context.EmpExecutionContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-20 上午10:21:41
 */
public class AppHomeImgLoaderFilter implements Filter
{
	WgMwFileBiz fileBiz = new WgMwFileBiz();
	String allImgExt = ".jpg|.jpeg|.gif|.bmp|.png|";

	@Override
    public void destroy()
	{
		
	}

	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		//请求地址相对路径 websphere下获取不到值
		String path = req.getServletPath();
		if("".equals(path)){
			path = req.getRequestURI();
			//过滤掉前面的项目名
			int index = path.substring(1).indexOf("/");
			path = path.substring(index+1);
		}
		//请求文件真实路径
		String realPath = req.getRealPath(path);
		String extName = realPath.substring(realPath.lastIndexOf(".")).toLowerCase();
		File f = new File(realPath);
		if(allImgExt.indexOf(extName)!=-1){
			if(!f.exists()){
				if(!f.getParentFile().exists()){
					f.getParentFile().mkdirs();
				}
				String bakPath = req.getParameter("bak");
				//本地服务器不存在文件则从远程文件服务器下载
				fileBiz.downloadFromMwFileSer(f.getPath(),bakPath);
			}
		}
		if(f.exists()){
			response.setContentType("image/"+extName.replaceAll("\\.", ""));
			OutputStream out =  null;
			FileInputStream fiInputStream = null;
			try{
				out = response.getOutputStream();
				fiInputStream = new FileInputStream(f);
				byte[] tempbytes = new byte[4096];
				int byteread = 0;
				// 读入多个字节到字节数组中，byteread为一次读入的字节数
				while ((byteread = fiInputStream.read(tempbytes)) != -1) {
					out.write(tempbytes);
				}
			}catch(Exception e){
                EmpExecutionContext.error(e, "发现异常");
			}finally{
				if(out != null){
					out.close();
				}
				if(fiInputStream != null){
					try{
						fiInputStream.close();
					}catch(Exception e){
						EmpExecutionContext.error(e,"关闭资源出错");
					}
				}
			}
		}
	}

	@Override
    public void init(FilterConfig filterConfig) throws ServletException
	{
		
	}
}

