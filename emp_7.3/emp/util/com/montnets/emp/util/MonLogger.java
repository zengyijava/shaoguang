package com.montnets.emp.util;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 日志
 * 
 * @author Administrator
 */
public class MonLogger implements Runnable
{
	// logger对象
	private static MonLogger				instance					= null;

	// 保存路径
	private String							savePath;

	// 格式化时间
	private SimpleDateFormat				sdf							= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// 报文文件路径
	SimpleDateFormat						sdf_path					= new SimpleDateFormat("yyyyMMdd");

	private TxtFileUtil						txtfileutil					= new TxtFileUtil();

	// 监控告警队列中的元素
	private StringBuffer					monAlarmResult			= null;

	// 监控告警数据队列
	private ConcurrentLinkedQueue<String>	monAlarmQueue			= new ConcurrentLinkedQueue<String>();

	// 临时存放监控告警数据队列
	private ConcurrentLinkedQueue<String>	monAlarmQueueElement	= new ConcurrentLinkedQueue<String>();
	
	// 程序周期队列中的元素
	private StringBuffer					proCycleResult			= null;

	// 程序周期数据队列
	private ConcurrentLinkedQueue<String>	proCycleQueue			= new ConcurrentLinkedQueue<String>();

	// 临时存放程序周期数据队列
	private ConcurrentLinkedQueue<String>	proCycleQueueElement	= new ConcurrentLinkedQueue<String>();

	private boolean isShutdown = false;
	
	public void shutdown(){
		this.isShutdown = true;
	}
	
	/**
	 * 初始化变量
	 */
	private MonLogger()
	{
		this.savePath = txtfileutil.getWebRoot() + "logger/mon_data/";
		this.monAlarmResult = new StringBuffer();
		this.proCycleResult = new StringBuffer();
	}

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public synchronized static MonLogger get()
	{
		if(instance == null)
		{
			instance = new MonLogger();
		}
		return instance;
	}

	/**
	 * 保存日志信息到日志文件
	 * 
	 * @param info
	 *        日志信息
	 * @param logFlag
	 *        日志标识 0：程序周期;1:监控告警
	 */
	public void saveLog(String info, int logFlag)
	{
		FileWriter writer = null;
		try
		{
			File file = this.getLogFile(logFlag);
			if(file == null)
			{
				return;
			}
			writer = new FileWriter(file, true);
			// 写日志文件
			writer.write(info);
		}
		catch (Exception e)
		{
			// 打印异常信息
			EmpExecutionContext.error(e, "写监控报文到文件失败。");
		}
		finally
		{
			if(writer != null)
			{
				try
				{
					writer.close();
				}
				catch (Exception e)
				{
					// 打印异常信息
					EmpExecutionContext.error(e, "关闭写监控报文到文件失败。");
				}
			}
		}
	}

	/**
	 * 获取日志文件对象
	 * 
	 * @description
	 * @param logFlag
	 *        日志标识 0：程序周期;1:监控告警
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-31 下午04:47:13
	 */
	private File getLogFile(int logFlag)
	{
		try
		{
			// 构造异常日志文件目录（年月日文件夹）
			String dayPath = sdf_path.format(System.currentTimeMillis());
			// 构造异常日志文件目录
			String sbTemp = new StringBuilder(String.valueOf(savePath)).append(dayPath).toString();
			// 创建日常日志文件目录
			txtfileutil.makeDir(sbTemp);

			// 构造日志文件名
			String name = "";
			if(logFlag == 0)
			{
				name = "90_mon_log.txt";
			}
			else if(logFlag == 1)
			{
				name = "10_mon_log.txt";
			}

			// 生成异常日志文件
			File file = new File((new StringBuilder(String.valueOf(savePath))).append(dayPath).append("/").append(name).toString());
			return file;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取监控报文文件对象失败");
			return null;
		}
	}

	/**
	 * 程序周期报文信息加入队列
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-31 下午03:57:12
	 */
	public void putproCycleEvnet(String logInfo)
	{
		try
		{
			proCycleQueue.add(logInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "程序周期报文信息加入队列异常!");
		}
	}

	/**
	 * 监控告警报文信息加入队列
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-31 下午03:57:12
	 */
	public void putMonAlarmEvnet(String logInfo)
	{
		try
		{
			monAlarmQueue.add(logInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控告警报文信息加入队列异常!");
		}
	}

	/**
	 * 获取队列中的所有元素,并清空队列
	 * 
	 * @return 队列中的所有元素
	 */
	private void getEvent()
	{
		try
		{
			/**** 监控告警报文信息 ****************************/
			// 复制队列的元素存放在临时队列中
			monAlarmQueueElement.addAll(monAlarmQueue);
			// 清空队列中的元素
			monAlarmQueue.clear();
			// 队列中存在元素，拼接日志信息
			for (String mongodbinfo : monAlarmQueueElement)
			{
				monAlarmResult.append(mongodbinfo);
			}
			// 清空临时队列元素
			monAlarmQueueElement.clear();
		}
		catch (Exception e)
		{
			// 清空临时请求队列元素
			monAlarmQueueElement.clear();
		}
		try
		{	
			/**** 程序周期报文信息 ****************************/
			// 复制队列的元素存放在临时队列中
			proCycleQueueElement.addAll(proCycleQueue);
			// 清空队列中的元素
			proCycleQueue.clear();
			// 队列中存在元素，拼接日志信息
			for (String proCycleinfo : proCycleQueueElement)
			{
				proCycleResult.append(proCycleinfo);
			}
			// 清空临时队列元素
			proCycleQueueElement.clear();
		}
		catch (Exception e)
		{
			// 清空临时请求队列元素
			proCycleQueueElement.clear();
		}
	}

	/**
	 * 线程方法
	 */
	public void run()
	{
		while(!isShutdown)
		{
			try
			{
				// 获取队列中的元素(日志信息)
				getEvent();
				try
				{
					// 如果元素不为空，写mongodb信息日志
					if(proCycleResult != null && proCycleResult.length() > 0)
					{
						saveLog(proCycleResult.toString(), 0);
					}
					if(proCycleResult != null)
					{
						proCycleResult.setLength(0);
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "写程序周期报文发生异常,异常信息:" + e.getMessage());
				}
				try
				{
					// 如果元素不为空，写mongodb信息日志
					if(monAlarmResult != null && monAlarmResult.length() > 0)
					{
						saveLog(monAlarmResult.toString(), 1);
					}
					if(monAlarmResult != null){
						monAlarmResult.setLength(0);
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "写监控告警报文发生异常,异常信息:" + e.getMessage());
				}
				
				// 线程等待
				Thread.sleep(StaticValue.getMonlogPrintInterval());
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "写监控报文线程run方法发生异常,异常信息:" + e.getMessage());
			}
		}
	}
}
