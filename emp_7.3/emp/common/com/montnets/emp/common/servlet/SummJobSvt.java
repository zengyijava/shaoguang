package com.montnets.emp.common.servlet;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.JobBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.TimerManagerBiz;
import com.montnets.emp.common.timer.biz.TimerHeartbeatBiz;
import com.montnets.emp.common.timer.entity.LfTimeSerCtrl;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;

/**
 * @author EMP白天汇总用定时调度，
 */
public class SummJobSvt extends HttpServlet
{
	/**
	 * 汇总调用定时器处理逻辑
	 */
	private static final long		serialVersionUID	= 6239579462538089216L;

	// 时间格式化对象
	private final  SimpleDateFormat	format				= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static Timer timer = null;
	//定时任务对象
	private static TimerTask task = null;
	//定时汇总biz
	JobBiz jobbiz=new JobBiz();
	
	/**
	 * 初使化方法
	 */
	public void init() throws ServletException
	{
		// 是否运行定时任务，1代表运行，0代表不运行
		if(StaticValue.ISRUNTIMEJOB == 1)
		{
			// 获取第一次执行时间
			int[] time = getRunTime();
			//获取小时
			int hour = time[0];
			//获取分钟
			int minute = time[1];
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, 0);
			// 获取当前定时任务的启动时间
			Date date = new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DATE), hour, minute, 0);
			EmpExecutionContext.info("EMP汇总JOB第一次执行时间：" + date.toLocaleString());
			//获取系统配置时间间隔
			String summTimeInterval = SystemGlobals.getSysParam("SummTimeInterval");
			//默认6小时
			int interval = 6;
			//设置时间间隔为系统配置
			if(summTimeInterval != null && !"".equals(summTimeInterval))
			{
				interval = Integer.parseInt(summTimeInterval);
			}
			//初始化节点汇总普通控制记录
			//jobbiz.UpdateSumCtrl(1);
			// 创建一个定时器
			timer = new Timer();
			task = new TimerTask()
			{// 定时开启
				public void run()
				{
					
					TimerManagerBiz timerBiz = TimerManagerBiz.getTMInstance();
					TimerHeartbeatBiz heartbeatBiz = new TimerHeartbeatBiz();
					//查询定时服务控制表，获取当前定时服务信息
					LfTimeSerCtrl serCtrl = heartbeatBiz.getTimeSerCtrl();
					if(serCtrl == null)
					{
						EmpExecutionContext.error("白天汇总定时服务心跳，获取定时服务控制表记录为空。");
						return;
					}
					//当前定时服务就是本机定时服务，则只更新活动时间
					if(serCtrl.getTimeServerID().equals(timerBiz.getProperty().getTimeServerID()))
					{
						// 更新成功则直接运行定时
						runDayContent();
					}
					
					
//					// 定时run方法开启
//					// 获取本地配置文件中节点编号，将节点编号与本地取出来的节点编号相同、汇总类型为白天汇总且是主控记录的执行时间更新为当前时间，获取更新条数
//					if(jobbiz.UpdateMainRecord(1))
//					{
//						// 更新成功则直接运行定时
//						runDayContent();
//					}
//					else
//					{
//						//休眠1分钟  为了能让前面的更新能先行
//						try
//						{
//							Thread.sleep(60000);
//						}
//						catch (InterruptedException e)
//						{
//							EmpExecutionContext.error("延时部分时间让update先执行这样更准确");
//						}
//						// 查询出LF_SUMCTRL白天定时主控制数据以及相关url地址
//						LfSumCtrlVo sumctrl = jobbiz.getLfSumCtrl(1);
//						if(sumctrl!=null)
//						{
//							// 判断是否超时
//							if(!jobbiz.IsTimeOut(1, sumctrl.getDbcurrenttime(), sumctrl.getUpdateTime()))
//							{
//								//未超时
//							}else{
//								// 如果已经超时则判断连接是否通 大于400为连接失败
//								if(HttpUtil.checkState(sumctrl.getSerLocalURL())<400)
//								{
//									//连接是通的
//								}else{
//									//连接不通则切换为备用tomcat
//									// 更新节点为之前的主节点编号为本节点编号
//									if(jobbiz.ChangeMainCtrl(1, sumctrl.getNodeId()))
//									{
//										// 如果切换成功 则执行定时内容
//										// 更新成功则直接运行定时
//										runDayContent();
//									}else{
//										//切换失败可能是已被其他tomcat抢
//									}
//								}
//							}
//						}
//						else
//						{
//							EmpExecutionContext.error("查询出LF_SUMCTRL白天定时主控制数据以及相关url地址 对象未取到数据");
//						}
//					}
//					// 执行完主辅记录同时更新时间
//					jobbiz.UpdateRecord(1);
					
					// 定时run方法结束
				}
				// 定时结束
			};
			timer.schedule(task, date, interval * 3600000L);
		}
	}

	
	public void runDayContent(){
		// 判断是否执行调度汇总
		Connection conn = null;
		CallableStatement cstmt = null;
		String isSumm = SystemGlobals.getSysParam("isSumm");
		if("0".equals(isSumm))
		{// 判断汇总总开关开启
			String isHdataFilish = SystemGlobals.getSysParam("isHdataFilish");
			if("0".equals(isHdataFilish))
			{
				Calendar calendar = Calendar.getInstance();
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				// 获取当前时间的小时，分，秒转换为秒数
				// int timeValue = hour * 60 * 60 + minute * 60
				// + second;
				// 如果当前时间为00:00:00到04:59:59秒，则不进行汇总。
				// if(timeValue > (4 * 60 * 60 + 59 * 60 + 59))
				if(hour > 5)
				{// 判断执行时间范围为为早上六点之后开始
					
					String isSummFilish = SystemGlobals.getSysParam("isSummFilish");
					// 判断短信汇总是否正在执行中
					if("0".equals(isSummFilish))
					{// 判断是否运行中开始
						// 执行emp汇总
						runPrcSumm(conn, cstmt);
						//执行网优
						runPrcSummWY(conn, cstmt);
					}
					else
					{
						EmpExecutionContext.info("EMP短信汇总存储过程正在执行中此次未调用！");
					}

					String ismSummFilish = SystemGlobals.getSysParam("ismSummFilish");
					// 判断彩信汇总是否正在执行中
					if("0".equals(ismSummFilish))
					{
						// EMP PRC_MMSSUMM存储过程
						runPrcMmsSumm(conn, cstmt);
						// 判断彩信是否运行中结束
					}
					else
					{
						EmpExecutionContext.info("EMP彩信汇总存储过程正在执行中此次未调用！");
					}
					// 判断执行时间范围为为早上六点之后结束
				}
				// 判断白天汇总是否执行完
			}
			// 判断汇总总开关结束
			
			String isAppSummFilish = SystemGlobals.getSysParam("isAppSummFilish");
			// 判断彩信汇总是否正在执行中
			if("0".equals(isAppSummFilish))
			{
				// EMP PRC_SUMMAPP存储过程
				runPRCSUMMAPP(conn, cstmt);
			}
			else
			{
				EmpExecutionContext.info("EMP APP汇总存储过程正在执行中此次未调用！");
			}
			
		}
	}
	
	/**
	 * 短信白天汇总
	 * 
	 * @param conn
	 * @param cstmt
	 */
	public void runPrcSumm(Connection conn, CallableStatement cstmt)
	{

		// EMP PRC_SUMM存储过程
		try
		{
			IConnectionManager connectionManager = new ConnectionManagerImp();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			// 调用存储过程
			String procedure = "{call PRC_SUMMV1()}";
			cstmt = conn.prepareCall(procedure);
			Calendar s = Calendar.getInstance();
			System.out.println("[" + format.format(s.getTime()) + "] PRC_SUMMV1()开始执行！");
			EmpExecutionContext.info("EMPWEB汇总：PRC_SUMMV1()开始执行！");
			cstmt.execute();
			s = Calendar.getInstance();
			System.out.println("[" + format.format(s.getTime()) + "] PRC_SUMMV1()结束执行！");
			EmpExecutionContext.info("EMPWEB汇总：PRC_SUMMV1()结束执行！");

		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "PRC_SUMMV1()执行异常。");

		}
		finally
		{
			// 关闭连接
			try
			{
				if(cstmt != null)
				{
					cstmt.close();
				}
				if(conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "PRC_SUMM关闭连接异常");
			}
		}
	}
	
	/**
	 * app白天汇总
	 * 
	 * @param conn
	 * @param cstmt
	 */
	public void runPRCSUMMAPP(Connection conn, CallableStatement cstmt)
	{

		// EMP PRC_SUMM存储过程
		try
		{
			IConnectionManager connectionManager = new ConnectionManagerImp();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			// 调用存储过程
			String procedure = "{call PRC_SUMMAPP()}";
			cstmt = conn.prepareCall(procedure);
			Calendar s = Calendar.getInstance();
			System.out.println("[" + format.format(s.getTime()) + "] PRC_SUMMAPP()开始执行！");
			EmpExecutionContext.info("EMPWEB汇总：PRC_SUMMAPP()开始执行！");
			cstmt.execute();
			s = Calendar.getInstance();
			System.out.println("[" + format.format(s.getTime()) + "] PRC_SUMMAPP()结束执行！");
			EmpExecutionContext.info("EMPWEB汇总：PRC_SUMMAPP()结束执行！");

		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "PRC_SUMMAPP()执行异常。");

		}
		finally
		{
			// 关闭连接
			try
			{
				if(cstmt != null)
				{
					cstmt.close();
				}
				if(conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "PRC_SUMMAPP关闭连接异常");
			}
		}
	}

	/**
	 * 彩信白天汇总
	 * 
	 * @param conn
	 * @param cstmt
	 */
	public void runPrcMmsSumm(Connection conn, CallableStatement cstmt)
	{
		try
		{
			IConnectionManager connectionManager = new ConnectionManagerImp();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			// 调用存储过程
			String procedure = "{call PRC_MMSSUMM()}";
			cstmt = conn.prepareCall(procedure);
			Calendar s = Calendar.getInstance();
			System.out.println("[" + format.format(s.getTime()) + "] PRC_MMSSUMM()开始执行！");
			EmpExecutionContext.info("EMPWEB汇总：PRC_MMSSUMM()开始执行！");
			cstmt.execute();
			s = Calendar.getInstance();
			System.out.println("[" + format.format(s.getTime()) + "] PRC_MMSSUMM()结束执行！");
			EmpExecutionContext.info("EMPWEB汇总：PRC_MMSSUMM()结束执行！");

		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "PRC_MMSSUMM()执行异常。");

		}
		finally
		{
			// 关闭连接
			try
			{
				if(cstmt != null)
				{
					cstmt.close();
				}
				if(conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "PRC_MMSSUMM关闭连接异常");
			}
		}
	}

	/**
	 * 网优短信白天汇总
	 * 
	 * @param conn
	 * @param cstmt
	 */
	public void runPrcSummWY(Connection conn, CallableStatement cstmt)
	{
		//判断是否要执行
		if(!new JobBiz().isExcWYProc()){
			return;
		}
		// EMP PRC_SUMMWY存储过程
		try
		{
			IConnectionManager connectionManager = new ConnectionManagerImp();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			// 调用存储过程
			String procedure = "{call PRC_SUMMWYV1()}";
			cstmt = conn.prepareCall(procedure);
			Calendar s = Calendar.getInstance();
			System.out.println("[" + format.format(s.getTime()) + "] PRC_SUMMWYV1()开始执行！");
			EmpExecutionContext.info("EMPWEB汇总：PRC_SUMMWYV1()开始执行！");
			cstmt.execute();
			s = Calendar.getInstance();
			System.out.println("[" + format.format(s.getTime()) + "] PRC_SUMMWYV1()结束执行！");
			EmpExecutionContext.info("EMPWEB汇总：PRC_SUMMWYV1()结束执行！");

		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "PRC_SUMMWYV1()执行异常。");

		}
		finally
		{
			// 关闭连接
			try
			{
				if(cstmt != null)
				{
					cstmt.close();
				}
				if(conn != null)
				{
					conn.close();
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "PRC_SUMMWYV1关闭连接异常");
			}
		}
	}
	
	/**
	 * 获取执行时间的方法
	 * 
	 * @return
	 */
	public int[] getRunTime()
	{

		Calendar cal = Calendar.getInstance(); // 获取当前时间
		cal.add(Calendar.HOUR_OF_DAY, 3); // 当前时间加3个小时
		int[] time = new int[2];
		time[0] = cal.get(Calendar.HOUR_OF_DAY);
		time[1] = cal.get(Calendar.MINUTE);
		time[1] = time[1] + 1;// 延迟到启动web服务后，当期时间晚一分钟后启动该定时任务
		return time;
	}
	
	/**
	 * 停止调度线程
	 */
	public void stopSummJob(){
		// 是否运行定时任务，1代表运行，0代表不运行
		if(StaticValue.ISRUNTIMEJOB == 1)
		{
			if(task != null){
				task.cancel();
				task = null;
			}
			if(timer != null){
				timer.cancel();
				timer = null;
			}
		}
	}

	/**
	 * 初使化方法
	 */
	public SummJobSvt()
	{
		super();
	}

	/**
	 * 摧毁方法
	 */
	public void destroy()
	{
		super.destroy();
		stopSummJob();
	}

	/**
	 * doget请求
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	}

	/**
	 * dopost请求
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	}

}
