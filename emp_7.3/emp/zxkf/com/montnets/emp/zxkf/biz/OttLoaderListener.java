package com.montnets.emp.zxkf.biz;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import com.montnets.emp.appwg.initbuss.HandleMsgBiz;
import com.montnets.emp.appwg.initbuss.IInterfaceBuss;
import com.montnets.emp.ottbase.constant.WXStaticValue;
import com.montnets.emp.ottbase.util.TxtFileUtil;
import com.montnets.emp.znly.biz.CustomStatusBiz;
import com.montnets.emp.znly.biz.GetAppMsgImpl;
import com.montnets.emp.znly.biz.GetAppMsgRptImpl;



public class OttLoaderListener extends HttpServlet implements ServletContextListener {

	private static final long serialVersionUID = -1550181542455735913L;
	
	// @Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// 启动客服定时
		new OttScheduledBiz().startTimer();
		CustomStatusBiz statusBiz = new CustomStatusBiz();
        // 加载所有客服状态
		statusBiz.loadAllcustomStatus();
        // 添加app消息监听 
		IInterfaceBuss commuteBiz = new HandleMsgBiz();
        commuteBiz.SetIProcPMessage(new GetAppMsgImpl());
        //状态报告监听
        commuteBiz.SetIProcRptMessage(new GetAppMsgRptImpl());
        initFolder();
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * 初始化系统的一些全局路径
	 */
	private void initFolder() {
		List<String> foldersPath = new ArrayList<String>();
		foldersPath.add(WXStaticValue.FILEDIRNAME);
		foldersPath.add(WXStaticValue.FILE_UPLOAD_PATH);
		foldersPath.add(WXStaticValue.MANUAL_SMSTXT);
		foldersPath.add(WXStaticValue.WEIX_ACCOUNT);
		foldersPath.add(WXStaticValue.WEIX_QRCODE);
		foldersPath.add(WXStaticValue.WEIX_RIMG);
		foldersPath.add(WXStaticValue.WEIX_MEDIA_IMG);
		foldersPath.add(WXStaticValue.WEIX_MEDIA_SOUND);
		foldersPath.add(WXStaticValue.WEIX_MEDIA_VIDEO);
	    foldersPath.add(WXStaticValue.FILE_LBS_IMG);
        foldersPath.add(WXStaticValue.WEIX_USER);
        foldersPath.add(WXStaticValue.WEIX_REQUEST);
        foldersPath.add(WXStaticValue.WEIX_RESPONSE);
        foldersPath.add(WXStaticValue.WEIX_HTTPREQUEST);
        foldersPath.add(WXStaticValue.WZGL_FORMHTML_URL);

	    TxtFileUtil fileUtil =  new TxtFileUtil();
		
		//循环初始化文件夹路径
		for (int i = 0; i < foldersPath.size(); i++) {
			String dir = fileUtil.getWebRoot()+foldersPath.get(i);
			dir = dir.replace('\\', '/');
			File dirFile = new File(dir);
			dirFile.mkdirs();
		}
	}
}
