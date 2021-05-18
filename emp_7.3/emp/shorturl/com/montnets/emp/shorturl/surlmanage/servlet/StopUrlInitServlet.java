package com.montnets.emp.shorturl.surlmanage.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.montnets.emp.shorturl.surlmanage.thread.NoticeStopThread;
import com.montnets.emp.shorturl.surlmanage.util.HttpClientUtil;

/**
 * 启动扫描Lf_neturl 表线程
 * @author Administrator
 *
 */
public class StopUrlInitServlet extends HttpServlet{
	 private static final long serialVersionUID = -5461118023014488169L;
	 
	 public void init() throws ServletException {
		 try {
			 //启动线程
			 //扫描lf_neturl表将未禁用的url 传给地址中心禁用
			new NoticeStopThread().StartThread();		 
		} catch (Exception e) {
			
		}
	 }
	 
	/**
     * 销毁
     */
    public void destroy() {
        super.destroy();
        HttpClientUtil.setSYSTEM_THREAD_RUN_FLAG(false);
    }
}
