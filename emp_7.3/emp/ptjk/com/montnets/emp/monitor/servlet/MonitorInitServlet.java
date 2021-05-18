/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-31 下午05:15:05
 */
package com.montnets.emp.monitor.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.monitor.biz.AccoutBaseInfoBiz;
import com.montnets.emp.monitor.biz.MonBusAreaAnalysisBiz;
import com.montnets.emp.monitor.biz.MonDataAnalysisBiz;
import com.montnets.emp.monitor.biz.MonDbConnection;
import com.montnets.emp.monitor.biz.MonitorBaseInfoBiz;
import com.montnets.emp.monitor.biz.SetMonInfoThread;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.util.TxtFileUtil;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-31 下午05:15:05
 */

public class MonitorInitServlet extends HttpServlet
{
	/**
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-31 下午05:16:14
	 */
	private static final long	serialVersionUID	= 1L;
	
	MonitorBaseInfoBiz monBaseInfo = new MonitorBaseInfoBiz();

	/**
	 * 监控初始化方法
	 */
	public void init() throws ServletException
	{
		try
		{
			// 是否加载监控模块，16：监控
			if(new SmsBiz().isWyModule("16"))
			{
				//设置加载状态为执行
				MonitorStaticValue.setLoadFinish(false);
				MonitorStaticValue.setMonThreadState(true);
				EmpExecutionContext.info("系统启动，设置监控线程状态为:"+MonitorStaticValue.isMonThreadState());
				monBaseInfo.getServerName(getServletContext().getServerInfo());
				//加载动态库文件,兼容websphere
				String libPath = new TxtFileUtil().getWebRoot()+"WEB-INF/lib";
				System.setProperty("java.library.path", libPath);
				//获取数据库服务器时间
				MonitorStaticValue.setCurDbServerTime(monBaseInfo.getDbServerTime(MonDbConnection.getInstance().getConnection()));
				// 设置监控基础信息
				getMonitorBaseInfo();
				// 设置SP账号和通道账号信息
				new AccoutBaseInfoBiz().getSpAndGateInfo();
				// 启动数据分析线程
				new Thread(new MonDataAnalysisBiz()).start();
				//执行监控定时任务
				monBaseInfo.executeMonTimeJob();
				//业务区域数据分析线程
				new Thread(new MonBusAreaAnalysisBiz()).start();
				//删除本节点在线用户详情记录
				monBaseInfo.resetOnlineUser();
				//监控信息线程
				new Thread(new SetMonInfoThread()).start();
				//设置加载状态为完成
				MonitorStaticValue.setLoadFinish(true);
				EmpExecutionContext.info("系统启动监控初始化完成，监控线程状态:"+MonitorStaticValue.isMonThreadState()+"数据库服务器当前时间："+MonitorStaticValue.getCurDbServerTime());
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控初始化异常！");
		}finally
		{
			//关闭数据库连接
			MonDbConnection.getInstance().closeConnection();
		}
	}

	/**
	 * 设置监控基础信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午02:26:47
	 */
	private void getMonitorBaseInfo()
	{
		// 设置监控配置信息，包括页面刷新时间 、数据分析间隔时间、网络异常时间间隔
		monBaseInfo.setMonConfig();
		// 在线用户告警信息
		monBaseInfo.setOnlineCfgInfo();
		// 主机监控基本信息
		monBaseInfo.setHostBaseInfo();
		//设置主机动态信息
		monBaseInfo.setDHostInfo(1);
		// 程序监控基本信息
		monBaseInfo.setProceBaseInfo();
		// 设置程序动态信息
		monBaseInfo.setDProceInfo(1);
		// SP账号监控基本信息
		monBaseInfo.setspAcBaseInfo();
		// 设置SP账号监控动态信息
		monBaseInfo.setDspAcInfo(1);
		// 通道账号监控基本信息
		monBaseInfo.setGateAcBaseInfo();
		//设置通道账号监控动态信息
		monBaseInfo.setDGateAcInfo(1);
		//设置SPGATE缓存动态信息
		monBaseInfo.setDGateBufInfo();
		// 实时告警信息
		monBaseInfo.setMonErrorInfo();
		// 设置通道账号余额
		monBaseInfo.setGateAccountFee();
		// 设置业务区域发送数据
		//MonBaseInfo.setBusAreaSendCache();
		// 设置数据库监控动态信息
		monBaseInfo.setDataBaseMonInfo();
		// 设置主机网络监控信息
		monBaseInfo.setHostNetMonInfo();
		// 新增数据库和程序监控信息
		monBaseInfo.addDbAndProceMonInfo();
	}

	/**
	 * 退出结束线程
	 */
	public void destroy() {
		super.destroy();
		MonitorStaticValue.setMonThreadState(false);
		EmpExecutionContext.info("系统关闭，设置监控线程状态为:"+MonitorStaticValue.isMonThreadState());
		//删除本节点在线用户详情记录
		monBaseInfo.resetOnlineUser();
	}
}
