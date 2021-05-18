package com.montnets.emp.util;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * 日志
 * @author Administrator
 *
 */
public class Logger implements Runnable
{
	public static final int LEVEL_OFF = 900;
	public static final int LEVEL_FATAL = 500;
	public static final int LEVEL_ERROR = 400;
	public static final int LEVEL_WARN = 300;
	public static final int LEVEL_INFO = 200;
	public static final int LEVEL_DEBUG = 100;
	public static final int LEVEL_ALL = 0;
	
	//logger对象
	private static Logger instance = null;
	//保存路径
	private String savePath;
	//打印信息级别
	private int printFilterLevel;
	//日志文件级别
	private int saveFilterLevel;
	
	//格式化时间
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//修改文件时间
	private final SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
			
	//是否显示sql
	private boolean isShowSql;
	
	private final TxtFileUtil txtfileutil = new TxtFileUtil();
	
	//队列
	private final ConcurrentLinkedQueue<String> queueData = new ConcurrentLinkedQueue<String>();
	//临时存放队列
	private final ConcurrentLinkedQueue<String> queueElement = new ConcurrentLinkedQueue<String>();
	//请求日志队列
	private final ConcurrentLinkedQueue<String> requestQueue = new ConcurrentLinkedQueue<String>();;
	//临时存放请求日志队列
	private final ConcurrentLinkedQueue<String> requestQueueElement = new ConcurrentLinkedQueue<String>();
	
	//队列中的元素
	private StringBuffer result = null;
	
	//请求队列中的元素
	private StringBuffer requestResult = null;
	
	//APP请求队列中的元素
	private StringBuffer appRequestResult = null;
	
	//APP请求日志队列
	private final ConcurrentLinkedQueue<String> appRequestQueue = new ConcurrentLinkedQueue<String>();;
	
	//临时存放APP请求日志队列
	private final ConcurrentLinkedQueue<String> appRequestQueueElement = new ConcurrentLinkedQueue<String>();
	
	//监控消息请求队列中的元素
	private StringBuffer monMsgResult = null;
	
	//监控消息请求日志队列
	private final ConcurrentLinkedQueue<String> monMsgResultQueue = new ConcurrentLinkedQueue<String>();;
	
	//临时存放监控消息请求日志队列
	private final ConcurrentLinkedQueue<String> monMsgResultQueueElement = new ConcurrentLinkedQueue<String>();
	
	private boolean isShutdown = false;

	public void shutdown(){
        isShutdown = true;
	}


	/**
	 * 初始化变量
	 */
	private Logger()
	{
		savePath = "";
		printFilterLevel = 0;
		saveFilterLevel = 0;
		result = new StringBuffer();
		requestResult = new StringBuffer();
		appRequestResult = new StringBuffer();
		monMsgResult = new StringBuffer();
	}
	
	/**
	 * 单例模式
	 * @return
	 */
    public synchronized static Logger get()
    {   
	     if(instance == null) 
	     {
			 instance = new Logger();
	     }
       return instance;
    }

    /**
     * 初始化方法
     * @param savePath
     * @param bufferSize
     */
	public void init(String savePath, int bufferSize)
	{
		this.savePath = savePath;
	}

	public void setPrintFilter(int filterLevel)
	{
        printFilterLevel = filterLevel;
	}

	public int getPrintFilter()
	{
		return printFilterLevel;
	}
	
	public void setSaveFilter(int filterLevel)
	{
        saveFilterLevel = filterLevel;
	}

	public int getSaveFilter()
	{
		return saveFilterLevel;
	}

	public void setShowSql(boolean isShowSql)
	{
		this.isShowSql = isShowSql;
	}
	
	public boolean getShowSql()
	{
		return isShowSql;
	}

	/**
	 * 保存异常信息到日志文件(暂不使用)
	 * @param e 异常对象
	 * @param info 描述信息
	 */
	public void saveErrorLog(String info, Exception e){
		PrintStream ps = null;
		try{
			File file = getLogFile(0);
			if(file == null){
				return;
			}

			//保存到日志文件
			ps = new PrintStream(new FileOutputStream(file, true),true);
			ps.println(new SimpleDateFormat("yyyy_MM_dd hh:mm:ss").format(new Date())+ " " + info);
			e.printStackTrace(ps);
		}
		catch(Exception e1){
			System.out.println("保存异常信息到日志文件失败，失败信息：" + e1.getMessage());
		}finally{
			if(ps != null){
				ps.close();
			}
		}
	}
	
	/**
	 * 保存日志信息到日志文件
	 * @param info 日志信息
	 * @param logFlag 日志标识 0:一般日志;1:请求日志;2：APP请求日志
	 */
	public void saveLog(String info, int logFlag)
	{
		FileWriter writer = null;
		try
		{
			File file = getLogFile(logFlag);
			if(file == null){
				return;
			}
			writer = new FileWriter(file, true);
			//写日志文件
			writer.write(info);
		}
		catch (Exception e)
		{
			//打印异常信息
			System.out.println("保存日志信息到日志文件失败，失败信息：" + e.getMessage());
		}
		finally{
			if(writer != null){
				try
				{
					writer.close();
				} catch (IOException e)
				{
					//打印异常信息
					System.out.println("关闭日志文件失败，失败信息：" + e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 获取日志文件对象
	 * @description    
	 * @param logFlag 日志标识 0:一般日志;1:请求日志;2：APP请求日志
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-31 下午04:47:13
	 */
	private File getLogFile(int logFlag){
		try{
			//构造异常日志文件目录（年月日文件夹）
			String dayPath = (new SimpleDateFormat("yyyy/MM/dd/")).format(Calendar
					.getInstance().getTime());
			//构造异常日志文件目录
			String sbTemp = new StringBuilder(String.valueOf(savePath)).append(dayPath).toString();
			//创建日常日志文件目录
			txtfileutil.makeDir(sbTemp);
			
			//构造日志文件名
			String name = "";
			if(logFlag == 0)
			{
				name = "_error.log";
			}
			else if(logFlag == 1)
			{
				name = "_smshttp.txt";
			}
			else if(logFlag == 2)
			{
				name = "_apphttp.txt";
			}
			else if(logFlag == 3)
			{
				name = "_monmsg.log";
			}
			String fname = (new StringBuilder(StaticValue.getServerNumber()).append("_").append(String.valueOf((new SimpleDateFormat(
			"yyyy_MM_dd")).format(Calendar.getInstance().getTime()))))
			.append(name).toString();
			
			//生成异常日志文件
			File file = new File((new StringBuilder(String.valueOf(savePath)))
					.append(dayPath).append("/").append(fname).toString());
			if (!file.exists()){
				//文件不存在则创建
                boolean flag = file.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
            }
			else
			{
				//文件大于等于10M
				if(file.length() >= 10 * 1024 * 1024)
				{
					//重命名日志文件
					boolean rn=file.renameTo(new File((new StringBuilder(String.valueOf(savePath))).append(dayPath).append("/")
							.append(fname.substring(0, fname.length()-4)).append("(").append(sdfTime.format(Calendar.getInstance().getTime()))
							.append(")").append(fname.substring(fname.length()-4)).toString()));
					if(!rn){
	                    EmpExecutionContext.error("重命名日志文件失败！");
					}
					//重新创建新的日志文件
					boolean cf=file.createNewFile();
					if(!cf){
	                    EmpExecutionContext.error("重新创建新的日志文件失败！");
					}
				}
			}
			return file;
			
		}catch(Exception e){
			System.out.println("获取日志文件对象失败,失败信息:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 日志信息加入队列
	 */
	public void putEvent(String logInfo)
	{
		try
		{
			queueData.add(sdf.format(System.currentTimeMillis()) + " " + logInfo);
		}
		catch (Exception e)
		{
			System.out.println("日志信息加入队列异常!");
		}
	}
	
	/**
	 * 请求日志信息加入队列
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-31 下午03:57:12
	 */
	public void putRequestEvnet(String logInfo)
	{
		try
		{
			requestQueue.add(logInfo);
		}
		catch (Exception e)
		{
			System.out.println("请求日志信息加入队列异常!");
		}
	}
	
	/**
	 * 请求APP日志信息加入队列
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-31 下午03:57:12
	 */
	public void putAppRequestEvnet(String logInfo)
	{
		try
		{
			appRequestQueue.add(sdf.format(System.currentTimeMillis()) + " " +logInfo);
		}
		catch (Exception e)
		{
			System.out.println("APP请求日志信息加入队列异常!");
		}
	}
	
	/**
	 * 监控消息
	 * @description    
	 * @param logInfo       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-31 下午08:51:34
	 */
	public void putMonMsgEvnet(String logInfo)
	{
		try
		{
			monMsgResultQueue.add(sdf.format(System.currentTimeMillis()) + " " +logInfo);
		}
		catch (Exception e)
		{
			System.out.println("监控消息信息加入队列异常!");
		}
	}
	
	/**
	 * 获取队列中的所有元素,并清空队列
	 * @return 队列中的所有元素
	 */
	private void getEvent()
	{
			try
			{
				/****请求日志信息****************************/
				//复制队列的元素存放在临时队列中
				requestQueueElement.addAll(requestQueue);
				//清空队列中的元素
				requestQueue.clear();
				//队列中存在元素，拼接日志信息
				for(String requestInfo:requestQueueElement)
				{
					requestResult.append(requestInfo);
				}
				//清空临时队列元素
				requestQueueElement.clear();
			}
			catch (Exception e)
			{
				//清空临时请求队列元素
				requestQueueElement.clear();
			}
			try
			{
				/****普通日志信息****************************/
				//复制队列的元素存放在临时队列中
				queueElement.addAll(queueData);
				//清空队列中的元素
				queueData.clear();
				//队列中存在元素，拼接日志信息
				for(String info:queueElement)
				{
					result.append(info);
				}
				//清空临时队列元素
				queueElement.clear();
			}
			catch (Exception e)
			{
				//清空临时队列元素
				queueElement.clear();
			}
			try
			{
				/****APP请求日志信息****************************/
				//复制队列的元素存放在临时队列中
				appRequestQueueElement.addAll(appRequestQueue);
				//清空队列中的元素
				appRequestQueue.clear();
				//队列中存在元素，拼接日志信息
				for(String appRequestinfo:appRequestQueueElement)
				{
					appRequestResult.append(appRequestinfo);
				}
				//清空临时队列元素
				appRequestQueueElement.clear();
			}
			catch (Exception e)
			{
				//清空临时请求队列元素
				appRequestQueueElement.clear();
			}
			try
			{
				/****监控日志信息****************************/
				//复制队列的元素存放在临时队列中
				monMsgResultQueueElement.addAll(monMsgResultQueue);
				//清空队列中的元素
				monMsgResultQueue.clear();
				//队列中存在元素，拼接日志信息
				for(String monMsgRequestinfo:monMsgResultQueueElement)
				{
					monMsgResult.append(monMsgRequestinfo);
				}
				//清空临时队列元素
				monMsgResultQueueElement.clear();
			}
			catch (Exception e)
			{
				//清空临时请求队列元素
				monMsgResultQueueElement.clear();
			}
		}
	
	/**
	 * 线程方法
	 */
	@Override
    public void run()
	{
		while(!isShutdown)
		{
			try
			{
				//获取队列中的元素(日志信息)
				getEvent();
				try
				{
					//如果元素不为空，则写日志
					if(result != null && result.length() > 0)
					{
						saveLog(result.toString(), 0);
					}
					//清空已获取的队列元素
					result.setLength(0);
				}
				catch (Exception e)
				{
					System.out.println("写普通日志发生异常,异常信息:" + e.getMessage());	
				}
				
				try
				{
					//如果元素不为空，写请求信息日志
					if(requestResult != null && requestResult.length() > 0)
					{
						saveLog(requestResult.toString(), 1);
					}
					if(requestResult != null )
					{
						//清空已获取的队列元素
						requestResult.setLength(0);
					}
				}
				catch (Exception e)
				{
					System.out.println("写请求日志发生异常,异常信息:" + e.getMessage());	
				}
				
				try
				{
					//如果元素不为空，写APP请求信息日志
					if(appRequestResult != null && appRequestResult.length() > 0)
					{
						saveLog(appRequestResult.toString(), 2);
					}
					if(appRequestResult != null )
					{
						appRequestResult.setLength(0);
					}
				}
				catch (Exception e)
				{
					System.out.println("写APP请求日志发生异常,异常信息:" + e.getMessage());	
				}
				
				try
				{
					//如果元素不为空，写监控消息信息日志
					if(monMsgResult != null && monMsgResult.length() > 0)
					{
						saveLog(monMsgResult.toString(), 3);
					}
					if(monMsgResult != null )
					{
						monMsgResult.setLength(0);
					}
				}
				catch (Exception e)
				{
					System.out.println("写监控消息日志发生异常,异常信息:" + e.getMessage());	
				}
				//线程等待
				Thread.sleep(StaticValue.getLogPrintInterval());
			}
			catch (Exception e)
			{
				System.out.println("日志线程run方法发生异常,异常信息:" + e.getMessage());	
			}
		}
	}
}
