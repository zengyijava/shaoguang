package com.montnets.emp.common.biz;

import java.io.File;
import java.util.Calendar;
import java.util.Map;

import com.montnets.emp.common.atom.UserdataAtom;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 定时处理内存中的存放数据的类
 * @author Administrator
 *
 */
public class TimerLoadCache implements Runnable{
	
	//黑名单重新全部同步的时间
	private Calendar reSetTime = getNextResetTme();
	
	FrameControlBiz frameBiz = new FrameControlBiz();
	
	private BlackListAtom blAtom = new BlackListAtom();
	
	private SystemInitBiz systemInitBiz = new SystemInitBiz();
	public void run() 
	{
		try
		{
			//当前时间
			Calendar currCalTime = Calendar.getInstance();
			
			//当天1点
			Calendar calTime1 = Calendar.getInstance();
			calTime1.set(Calendar.HOUR_OF_DAY, 1);
			calTime1.set(Calendar.MINUTE, 0);
			calTime1.set(Calendar.SECOND, 0);
			calTime1.set(Calendar.MILLISECOND, 0);
			//当天5点30
			Calendar calTime5 = Calendar.getInstance();
			calTime5.set(Calendar.HOUR_OF_DAY, 5);
			calTime5.set(Calendar.MINUTE, 30);
			calTime5.set(Calendar.SECOND, 0);
			calTime5.set(Calendar.MILLISECOND, 0);
			
			//小于1点或大于5点半
			if(currCalTime.before(calTime1) || currCalTime.after(calTime5))
			{
				//是否重载,当前时间大于重置时间时
				boolean isReset = currCalTime.after(reSetTime);
				//初始化关键字\黑名单\发送账号\号段信息
				initKeyBlackSpHD(isReset);
				//记录EMP服务器信息
				printEmpServerInfo();
				if(isReset)
				{
					//设置日期延后一天
					calTime5.add(Calendar.DAY_OF_MONTH, 1);
					reSetTime = calTime5;
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时处理内存存放数据异常！");
		}
	}

	
	private static Calendar getNextResetTme()
	{
		//当天5点30
		Calendar calTime5 = Calendar.getInstance();
		calTime5.set(Calendar.HOUR_OF_DAY, 5);
		calTime5.set(Calendar.MINUTE, 30);
		calTime5.set(Calendar.SECOND, 0);
		calTime5.set(Calendar.MILLISECOND, 0);
		
		calTime5.add(Calendar.DAY_OF_MONTH, 1);
		
		return calTime5;
	}
	
	/**
	 * 初始化关键字\黑名单\发送账号\号段信息
	 * @description    
	 * @param isReset  是否全量加载黑名单 turn:是;false:否     			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-16 下午03:53:05
	 */
	public void initKeyBlackSpHD(boolean isReset)
	{
		/*//是否运行定时任务，1代表运行，0代表不运行
		if(StaticValue.ISRUNTIMEJOB==1)
		{
			//根据上行指令设置黑名单
			blAtom.setBlackByMoTaskOrder();
		}*/
		if(isReset)
		{
			//删掉无效的黑名单
			blAtom.delInValidBlackList();
			
			//重新设置所有短信黑名单
			blAtom.SetAllBlackList();
			
			//重新设置所有彩信黑名单
			blAtom.SetMmsBlackList();
			
			//记录EMP服务器信息
			printEmpServerInfo();
		}
		else
		{
			//打印版本信息
			this.printVservionInfo();
			//定时加载短信黑名单
			blAtom.loadSmsBlist(false);
			
			//定时加载彩信黑名单
			blAtom.loadMmsBlist(false);
		}
		//定时设置关键字
		new KeyWordAtom().setKeyWordMap(null);
		
		//定时设置号段值 
		new WgMsgConfigBiz().setHaoduan();
		EmpExecutionContext.info("定时设置号段值完成。");
		
		//定时设置所有发送账号
		new UserdataAtom().setAllUserdata();
		
		//定时设置全局变量
		systemInitBiz.setGlobalVariable();
	}
	
	/**
	 * 记录EMP服务器信息
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-9-11 下午04:42:59
	 */
	private void printEmpServerInfo()
	{
		Runtime runtime = Runtime.getRuntime();
		//最大内存空间
		long max = runtime.maxMemory() / 1024 / 1024;
		//已划出内存空间
		long total = runtime.totalMemory()/ 1024 / 1024;
		//划出内存剩余空间
		long free = runtime.freeMemory()/ 1024 / 1024;
		
		//磁盘剩余量
		long DiskFree = getProceDiskFree();
		
		EmpExecutionContext.info("JVM最大内存:"+max+"，已划出:"+total+"，已使用:"+(total - free)+"，剩余：" + (max-total)+"，磁盘剩余量："+DiskFree+"G");
		
	}
	
	/**
	 * 
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-5-4 上午11:50:54
	 */
	public void printVservionInfo()
	{
		try
		{
			//系统启动时，版本号还未获取到，在增量加载时记录版本号
			Map<String,String[]> versions = frameBiz.getVersionInfos(null);
			String EMP_WEB = versions.get("1000")[0];
			String EMP_GATEWAY = versions.get("2000")[0];
			String SMT_SPGATE = versions.get("3000")[0];
			String DB_VERSION = frameBiz.getDbVersion();
			EmpExecutionContext.info("EMP_VERSION:"+EMP_WEB+"，EMP_GATEWAY:"+EMP_GATEWAY
									+"，SMT_SPGATE："+SMT_SPGATE+"，DB_VERSION:"+DB_VERSION);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取EMP、网关、SPGATE、数据库版本号异常！");
		}
	}
	
	/**
	 * EMP程序所在磁盘剩余量
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-11 下午01:34:17
	 */
	private long getProceDiskFree()
	{
		// 磁盘剩余
		long free = 0;
		try
		{
			// 获取文件所在磁盘剩余
			TxtFileUtil fileUtil = new TxtFileUtil();
			File file = new File(fileUtil.getWebRoot() + "WEB-INF/classes/SystemGlobals.properties");
			// 磁盘剩余 单位为G
			free = file.getFreeSpace() / 1024 / 1024 / 1024;
			return free;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取EMP程序磁盘剩余量异常!");
			return free;
		}
	}
}
