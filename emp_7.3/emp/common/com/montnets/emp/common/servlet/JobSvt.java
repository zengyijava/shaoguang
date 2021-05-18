package com.montnets.emp.common.servlet;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.JobBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SmsSpecialDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.timer.TimerManagerBiz;
import com.montnets.emp.common.timer.biz.TimerHeartbeatBiz;
import com.montnets.emp.common.timer.entity.LfTimeSerCtrl;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;
import com.montnets.emp.entity.sms.MtTaskC;

public class JobSvt extends HttpServlet
{
	private static final long		serialVersionUID	= 1332710808009282120L;

	private SimpleDateFormat	format				= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	SmsBiz							smsBiz				= new SmsBiz();
	//定时汇总biz
	JobBiz jobbiz=new JobBiz();

	SmsSpecialDAO					smsSpecialDAO		= new SmsSpecialDAO();

	private static Timer			timer				= null;

	// 定时任务对象
	private static TimerTask		task				= null;

	public void init() throws ServletException
	{
		// 是否运行定时任务，1代表运行，0代表不运行
		if(StaticValue.ISRUNTIMEJOB == 1)
		{
			// 获取第一次执行时间
			int[] time = getRunTime();
			// 获取小时
			int hour = time[0];
			// 获取分钟
			int minute = time[1];
			Calendar cal = Calendar.getInstance();
			// 0点到三点半之间执行不加1
			// 小时数
			int dhour = cal.get(Calendar.HOUR_OF_DAY);
			// 分钟数
			int dminute = cal.get(Calendar.MINUTE);
			// 如果小时大于汇总执行时间或者小时等于执行小时分钟大于执行分钟的时候
			if(dhour > hour || (dhour == hour && dminute > minute))
			{
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
			Date date = new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DATE), hour, minute, 0);
			EmpExecutionContext.info("JOB第一次执行时间：" + date.toLocaleString());
			
			//初始化节点汇总普通控制记录
			//jobbiz.UpdateSumCtrl(0);
			
			timer = new Timer();

			task = new TimerTask()
				{
					public void run()
					{

						TimerManagerBiz timerBiz = TimerManagerBiz.getTMInstance();
						TimerHeartbeatBiz heartbeatBiz = new TimerHeartbeatBiz();
						//查询定时服务控制表，获取当前定时服务信息
						LfTimeSerCtrl serCtrl = heartbeatBiz.getTimeSerCtrl();
						if(serCtrl == null)
						{
							EmpExecutionContext.error("晚上定时服务心跳，获取定时服务控制表记录为空。");
							return;
						}
						//当前定时服务就是本机定时服务，则执行定时内容
						if(serCtrl.getTimeServerID().equals(timerBiz.getProperty().getTimeServerID()))
						{
							// 更新成功则直接运行定时
							runNightContent();
						}
//						 获取本地配置文件中节点编号，将节点编号与本地取出来的节点编号相同、汇总类型为晚上汇总且是主控记录的执行时间更新为当前时间，获取更新条数
//						if(jobbiz.UpdateMainRecord(0))
//						{
//						// 更新成功则直接运行定时
//						runNightContent();
//						}
//						else
//						{	
//							//未更新到
//							//延时五分钟
//							try
//							{
//								Thread.sleep(5*60*1000);
//							}
//							catch (InterruptedException e)
//							{
//								EmpExecutionContext.error("延时部分时间让update先执行这样更准确");
//							}
//							// 查询出LF_SUMCTRL晚上定时主控制数据以及相关url地址
//							LfSumCtrlVo sumctrl = jobbiz.getLfSumCtrl(0);
//							if(sumctrl!=null)
//							{
//								// 判断是否超时
//								if(!jobbiz.IsTimeOut(0, sumctrl.getDbcurrenttime(), sumctrl.getUpdateTime()))
//								{
//									//未超时
//								}else{
//									// 如果已经超时则判断连接是否通  小于400为连接成功
//									if(HttpUtil.checkState(sumctrl.getSerLocalURL())< 400)
//									{
//										//连接是通的
//									}else{
//										//连接不通则切换为备用tomcat
//										// 更新节点为之前的主节点编号为本节点编号
//										if(jobbiz.ChangeMainCtrl(0, sumctrl.getNodeId()))
//										{
//											// 如果切换成功 则执行定时内容
//											// 更新成功则直接运行定时
//											runNightContent();
//										}else{
//											//切换失败可能是已被其他tomcat抢
//										}
//									}
//								}
//							}
//							else
//							{
//								EmpExecutionContext.error("查询出LF_SUMCTRL晚上定时主控制数据以及相关url地址 对象未取到数据");
//							}
//							// 执行完主辅记录同时更新时间
//							jobbiz.UpdateRecord(0);
//						}
//						
					}
				};

			timer.schedule(task, date, 86400000L);
		}
	}

	
	
	private void runNightContent(){
		Calendar	s	= null;
		// 处理下行滞留记录，需放在调度前
		processOverdueMttaskc();

		Connection conn = null;
		CallableStatement cstmt = null;
		// 判断是否执行调度汇总
		String isHdata = SystemGlobals.getSysParam("isHdata");
		if("0".equals(isHdata))
		{
			// 判断调度汇总是否正在执行中
			// String isHdataFilish =
			// SystemGlobals.getSysParam("isHdataFilish");
			// if("0".equals(isHdataFilish))
			// {
			// 设置运行中
			// SystemGlobals.setSysParam("isHdataFilish", "1");
			// 短信调度汇总
			// runHdata(conn, cstmt, s);
			try
			{
				// 彩信调度汇总
				runHmdata(conn, cstmt, s);
				// EMP彩信汇总
				runPrcmmsummone(conn, cstmt);
				// 定义时间变量
				int[] runtime = getEndHour();
				int endhour = runtime[0];
				int endminute = runtime[1];
				Calendar calcur = Calendar.getInstance();
				// 小时数
				// 当前小时数
				int runhour = calcur.get(Calendar.HOUR_OF_DAY);
				// 当前分钟数
				int runminute = calcur.get(Calendar.MINUTE);
				// 循环检查网关汇总是否执行完
				// 如果当前小时数小于配置小时数 或者当前小时数与配置小时数相等且分钟数小于配置数则继续循环
				while(runhour < endhour || (runhour == endhour && runminute < endminute))
				{
					// 判断网关是否执行完成
					if("1".equals(jobbiz.IsDataFilish()))
					{
						// EMP短信汇总
						runPrcsummone(conn, cstmt);
						// EMP网优短信汇总
						runPrcsummoneWY(conn, cstmt);
						//双层保障退出循环
						runhour=endhour+1;
						break;
					}
					else
					{
						// 休眠5分钟
						try
						{
							Thread.sleep(5*60*1000L);
						}
						catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							EmpExecutionContext.error(e, "汇总定时检查网关是否执行完休眠异常");
							Thread.currentThread().interrupt();
						}
					}
					calcur = Calendar.getInstance();
					// 小时数
					runhour = calcur.get(Calendar.HOUR_OF_DAY);
					// 分钟数
					runminute = calcur.get(Calendar.MINUTE);
				}
				// 设置运行完成
				// SystemGlobals.setSysParam("isHdataFilish",
				// "0");
				// }
				// else
				// {
				// EmpExecutionContext.info("短彩信调度汇总存储过程正在执行中此次未调用！");
				// }
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "短信彩信网关以及EMP整体汇总出现异常");
			}
		}
		else
		{
			EmpExecutionContext.info("短彩信调度汇总开关处于关闭状态！");
		}
	}
	
	
	
	/**
	 * 获取结束时间
	 * @description    
	 * @return       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2015-12-28 下午07:17:00
	 */
	public int[] getEndHour()
	{
		// 默认小时数分钟数
		int hour = 6;
		//默认分钟数
		int minute = 0;
		String endhour = SystemGlobals.getSysParam("ENDHOUR");
		if(endhour==null||"".equals(endhour)){
			endhour="6:00";
		}
		String hourStr = "6";
		String minuteStr = "0";
		if(endhour.contains(":"))
		{// 分割时间 获取分钟数 时间数
			hourStr = endhour.split(":")[0];
			minuteStr = endhour.split(":")[1];
		}
		try
		{
			hour = Integer.parseInt(hourStr);
			//大于23 小于0都属于异常小时数 遇到这种设置为默认
			if(hour > 23 || hour < 0)
			{
				hour = 6;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "转换执行时间(小时)异常，hourStr:" + hourStr);
		}
		try
		{
			minute = Integer.parseInt(minuteStr);
			if(minute > 59 || minute < 0)
			{
				minute = 0;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "转换执行时间(分钟)异常，minuteStr:" + minuteStr);
		}
		int[] time = new int[2];
		time[0] = hour;
		time[1] = minute;
		return time;
	}

	

	/**
	 * 停止调度线程
	 */
	public void stopJob()
	{
		// 是否运行定时任务，1代表运行，0代表不运行
		if(StaticValue.ISRUNTIMEJOB == 1)
		{
			if(task != null)
			{
				task.cancel();
				task = null;
			}
			if(timer != null)
			{
				timer.cancel();
				timer = null;
			}
		}
	}

	/**
	 * 执行网关短信调度汇总
	 * 
	 * @param conn
	 * @param cstmt
	 * @param s
	 */
	public void runHdata(Connection conn, CallableStatement cstmt, Calendar s)
	{
		try
		{
			IConnectionManager connectionManager = new ConnectionManagerImp();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			// GW_H_DATATRANV1
			// String procedure = "{call H_DATATRANSFER()}";
			String procedure = "{call GW_H_DATATRANV1(?,?,?,?)}";
			int[] parray = getP1234();
			cstmt = conn.prepareCall(procedure);
			cstmt.setInt(1, parray[0]);
			cstmt.setInt(2, parray[1]);
			cstmt.setInt(3, parray[2]);
			cstmt.setInt(4, parray[3]);

			Calendar calen = Calendar.getInstance();
			System.out.println("[" + format.format(calen.getTime()) + "] 短信调度汇总存储过程GW_H_DATATRANV1开始执行！");
			EmpExecutionContext.info("短信调度汇总存储过程GW_H_DATATRANV1开始执行！P1:" + parray[0] + ",P2:" + parray[1] + ",P3:" + parray[2] + ",P4:" + parray[3]);
			cstmt.execute();
			Calendar s2 = Calendar.getInstance();
			System.out.println("[" + format.format(s2.getTime()) + "] 短信调度汇总存储过程GW_H_DATATRANV1结束执行！");
			EmpExecutionContext.info("短信调度汇总存储过程GW_H_DATATRANV1结束执行！");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "短信调度汇总的存储过程执行异常。");
		}
		finally
		{
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
				EmpExecutionContext.error(e, "GW_H_DATATRANV1关闭数据连接失败");
			}
		}
	}

	/**
	 * 彩信调度汇总
	 * 
	 * @param conn
	 * @param cstmt
	 * @param s
	 */
	public void runHmdata(Connection conn, CallableStatement cstmt, Calendar s)
	{
		try
		{
			IConnectionManager connectionManager = new ConnectionManagerImp();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			// String procedure = "{call H_MMSDATATRANSF(3,?,?,?,?)}";
			String procedure = "{call GW_H_MMSDATATV1(3,?,?,?,?)}";
			int[] parray = getP1234();
			cstmt = conn.prepareCall(procedure);
			cstmt.setInt(1, parray[0]);
			cstmt.setInt(2, parray[1]);
			cstmt.setInt(3, parray[2]);
			cstmt.setInt(4, parray[3]);
			Calendar calen = Calendar.getInstance();
			System.out.println("[" + format.format(calen.getTime()) + "] 彩信调度汇总存储过程GW_H_MMSDATATV1开始执行！");
			EmpExecutionContext.info("彩信调度汇总存储过程GW_H_MMSDATATV1开始执行！P1:" + parray[0] + ",P2:" + parray[1] + ",P3:" + parray[2] + ",P4:" + parray[3]);
			cstmt.execute();
			Calendar s2 = Calendar.getInstance();
			System.out.println("[" + format.format(s2.getTime()) + "] 彩信调度汇总存储过程GW_H_MMSDATATV1结束执行！");
			EmpExecutionContext.info("彩信调度汇总存储过程GW_H_MMSDATATV1结束执行！");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "彩信调度汇总的存储过程执行异常。");
		}
		finally
		{
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
				EmpExecutionContext.error(e, "H_MMSDATATRANSF关闭数据连接失败");
			}
		}
	}

	/**
	 * 短信EMP汇总
	 * 
	 * @param conn
	 * @param cstmt
	 */
	public void runPrcsummone(Connection conn, CallableStatement cstmt)
	{
		// EMP存储过程
		try
		{
			String smscount = "0";
			String sql = "select count(*) icount from LF_TASKREPORT";
			List<DynaBean> dybeans = new SuperDAO().getListDynaBeanBySql(sql);
			if(dybeans != null && dybeans.size() > 0)
			{
				DynaBean dybean = dybeans.get(0);
				if(dybean.get("icount") != null)
				{
					smscount = dybean.get("icount").toString();
				}
			}

			if("0".equals(smscount))
			{

				IConnectionManager connectionManager = new ConnectionManagerImp();
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
				String procedure = "{call PRC_SUMMONE()}";
				cstmt = conn.prepareCall(procedure);
				EmpExecutionContext.info("EMPWEB汇总：PRC_SUMMONE()开始执行！");
				cstmt.execute();
				EmpExecutionContext.info("EMPWEB汇总：PRC_SUMMONE()结束执行！");
			}
		}
		catch (Exception e)
		{
			try
			{
				new SuperDAO().executeBySQL("delete from LF_TASKREPORT", StaticValue.EMP_POOLNAME);
			}
			catch (Exception e1)
			{
				EmpExecutionContext.error(e1, "LF_TASKREPORT删除失败！");
			}
			EmpExecutionContext.error(e, "PRC_SUMMONE()执行出错！");
		}
		finally
		{
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
				EmpExecutionContext.error(e, "PRC_SUMMONE关闭数据连接失败");
			}
		}
	}

	/**
	 * 彩信EMP汇总
	 * 
	 * @param conn
	 * @param cstmt
	 */
	public void runPrcmmsummone(Connection conn, PreparedStatement cstmt)
	{
		String mmscount = "0";
		String msql = "select count(*) icount from LF_MMSTASKREPORT";
		List<DynaBean> mmsdybeans = new SuperDAO().getListDynaBeanBySql(msql);
		if(mmsdybeans != null && mmsdybeans.size() > 0)
		{
			DynaBean mmsdybean = mmsdybeans.get(0);
			if(mmsdybean.get("icount") != null)
			{
				mmscount = mmsdybean.get("icount").toString();
			}
		}

		if("0".equals(mmscount))
		{
			// EMP存储过程
			try
			{
				IConnectionManager connectionManager = new ConnectionManagerImp();
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
				String procedure = "{call PRC_MMSSUMMONE()}";
				cstmt = conn.prepareCall(procedure);
				EmpExecutionContext.info("EMPWEB汇总：PRC_MMSSUMMONE()开始执行！");
				cstmt.execute();
				EmpExecutionContext.info("EMPWEB汇总：PRC_MMSSUMMONE()结束执行！");
			}
			catch (Exception e)
			{
				try
				{
					new SuperDAO().executeBySQL("delete from LF_MMSTASKREPORT", StaticValue.EMP_POOLNAME);
				}
				catch (Exception e1)
				{
					EmpExecutionContext.error(e1, "LF_MMSTASKREPORT删除失败！");
				}
				EmpExecutionContext.error(e, "PRC_MMSSUMMONE()执行出错！");
			}
			finally
			{
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
					EmpExecutionContext.error(e, "PRC_MMSSUMMONE关闭数据连接失败");
				}
			}
		}
	}

	/**
	 * 网优短信EMP汇总
	 * 
	 * @param conn
	 * @param cstmt
	 */
	public void runPrcsummoneWY(Connection conn, CallableStatement cstmt)
	{
		if(!new JobBiz().isExcWYProc())
		{
			return;
		}

		String smscount = "0";
		String sql = "select count(*) icount from LF_TASKREPORT";
		List<DynaBean> dybeans = new SuperDAO().getListDynaBeanBySql(sql);
		if(dybeans != null && dybeans.size() > 0)
		{
			DynaBean dybean = dybeans.get(0);
			if(dybean.get("icount") != null)
			{
				smscount = dybean.get("icount").toString();
			}
		}

		if("0".equals(smscount))
		{
			// EMP存储过程
			try
			{
				IConnectionManager connectionManager = new ConnectionManagerImp();
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
				String procedure = "{call PRC_SUMMONEWY()}";
				cstmt = conn.prepareCall(procedure);
				EmpExecutionContext.info("EMPWEB汇总：PRC_SUMMONEWY()开始执行！");
				cstmt.execute();
				EmpExecutionContext.info("EMPWEB汇总：PRC_SUMMONEWY()结束执行！");
			}
			catch (Exception e)
			{
				try
				{
					new SuperDAO().executeBySQL("delete from LF_TASKREPORT", StaticValue.EMP_POOLNAME);
				}
				catch (Exception e1)
				{
					EmpExecutionContext.error(e1, "LF_TASKREPORT删除失败！");
				}
				EmpExecutionContext.error(e, "PRC_SUMMONEWY()执行出错！");
			}
			finally
			{
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
					EmpExecutionContext.error(e, "PRC_SUMMONEWY关闭数据连接失败");
				}
			}
		}
	}

	/**
	 * 处理超期的下行滞留记录
	 */
	public void processOverdueMttaskc()
	{
		try
		{
			EmpExecutionContext.info("开始处理过期的下行滞留记录。");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 计算超时时间点
			Calendar curTime = Calendar.getInstance();
			// 距离当前时间24小时前的时间点
			curTime.add(Calendar.HOUR, -StaticValue.MTTASKC_OVERDUE);
			// 只要小于这个时间点，都是过期
			conditionMap.put("sendTime&<", format.format(curTime.getTime()));
			conditionMap.put("sendStatus", "208");

			Calendar updateCurTime = Calendar.getInstance();
			// 使记录有效期过期，让网关删除
			updateCurTime.add(Calendar.DAY_OF_MONTH, -1);

			SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("sendStatus", "0");
			objectMap.put("validTime", df2.format(updateCurTime.getTime()));
			BaseBiz baseBiz = new BaseBiz();
			int resultCount = baseBiz.updateBySymbolsCondition(MtTaskC.class, objectMap, conditionMap);
			EmpExecutionContext.info("成功处理过期的下行滞留记录" + resultCount + "条。");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理过期的下行滞留记录异常。");
		}
	}

	/**
	 * 获取 p1 p2 p3 p4 汇总设置
	 * 
	 * @return
	 */
	public int[] getP1234()
	{
		// 定义并设置p1 p2 p3 p4 默认值
		int p1 = 1;
		int p2 = 1;
		int p3 = 0;
		int p4 = 0;
		// 从配置文件读取设置值 逗号隔开字符串
		String pstr = SystemGlobals.getValue(StaticValue.MON_EMP_P1234);
		// 定义并设置p1 p2 p3 p4 字符串默认值
		String p1str = "1";
		String p2str = "1";
		String p3str = "0";
		String p4str = "0";
		// 判断取出来的值是否合法
		if(pstr != null && pstr.contains(",") && pstr.split(",").length == 4)
		{
			// 合法则赋值
			p1str = pstr.split(",")[0];
			p2str = pstr.split(",")[1];
			p3str = pstr.split(",")[2];
			p4str = pstr.split(",")[3];
		}
		// 设置p1
		try
		{
			p1 = Integer.parseInt(p1str);
			p2 = Integer.parseInt(p2str);
			p3 = Integer.parseInt(p3str);
			p4 = Integer.parseInt(p4str);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "转换P参数设置汇总设置值异常，pstr:" + p1str);
		}

		// 定义并给数组赋值
		int[] parray = new int[4];
		parray[0] = p1;
		parray[1] = p2;
		parray[2] = p3;
		parray[3] = p4;
		return parray;
	}

	/**
	 * 获取执行时间
	 * 
	 * @return
	 */
	public int[] getRunTime()
	{
		int hour = 3;
		int minute = 30;
		String timeStr = SystemGlobals.getValue(StaticValue.DB_JOB_TIME);
		String hourStr = "3";
		String minuteStr = "30";
		if(timeStr.contains(":"))
		{
			hourStr = timeStr.split(":")[0];
			minuteStr = timeStr.split(":")[1];
		}
		try
		{
			hour = Integer.parseInt(hourStr);
			if(hour > 23 || hour < 0)
			{
				hour = 3;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "转换执行时间(小时)异常，hourStr:" + hourStr);
		}
		try
		{
			minute = Integer.parseInt(minuteStr);
			if(minute > 59 || minute < 0)
			{
				minute = 30;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "转换执行时间(分钟)异常，minuteStr:" + minuteStr);
		}
		int[] time = new int[2];
		time[0] = hour;
		time[1] = minute;
		return time;
	}

	public JobSvt()
	{
		super();
	}

	public void destroy()
	{
		super.destroy();
		stopJob();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	}

}
