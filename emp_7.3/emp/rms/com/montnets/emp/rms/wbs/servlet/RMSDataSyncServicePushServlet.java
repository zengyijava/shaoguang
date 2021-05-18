package com.montnets.emp.rms.wbs.servlet;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.wbs.server.IRmsService;
import com.montnets.emp.rms.wbs.server.impl.RmsServiceImpl;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.ResourceBundle;

/**
 * 富信同步接口
 */
public class RMSDataSyncServicePushServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		EmpExecutionContext.info("Mboss富信同步接口,开始发布WebService服务....");
		JaxWsServerFactoryBean server = new JaxWsServerFactoryBean();
		ResourceBundle rb = ResourceBundle.getBundle("SystemGlobals");
		String url = rb.getString("montnets.rms.mboss.push.url");
		server.setAddress(url);
		server.setServiceClass(IRmsService.class);
		server.setServiceBean(new RmsServiceImpl());
		server.create();
		EmpExecutionContext.info("Mboss富信同步接口,发布地址：" + url);
		EmpExecutionContext.info("Mboss富信同步接口,服务发布成功...");
	}
}