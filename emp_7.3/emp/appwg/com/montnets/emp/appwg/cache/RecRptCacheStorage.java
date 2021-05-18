package com.montnets.emp.appwg.cache;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.montnets.emp.appwg.bean.AppStaticValue;
import com.montnets.emp.appwg.bean.WgMessage;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 
 * @project emp_std_5.0new
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-9-3 下午07:59:27
 * @description 接收rpt消息缓存队列仓库，单例
 */
public class RecRptCacheStorage
{
	private RecRptCacheStorage(){
		
	}
	
	private static RecRptCacheStorage instance = null;
	
	public static synchronized RecRptCacheStorage getInstance(){
		if(instance == null){
			instance = new RecRptCacheStorage();
		}
		return instance;
	}
	
	// 接收mt消息缓存队列，仓库存储的载体
	private LinkedBlockingQueue<WgMessage> recRptQueue = new LinkedBlockingQueue<WgMessage>();

	
	
	/**
	 * 生产，填数据入缓冲队列
	 */
	public boolean produceRecRpt(WgMessage data)
	{
		try
		{
			// 放入产品，自动阻塞
			recRptQueue.put(data);
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "rpt生产异常。");
			return false;
		}

	}
	
	
	/**
	 * 生产，填数据入缓冲队列
	 */
	public boolean produceRecRpt(List<WgMessage> dataList)
	{
		try
		{
			for(WgMessage msg : dataList){
				// 放入产品，自动阻塞
				recRptQueue.put(msg);
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "rpt生产异常。");
			return false;
		}

	}

	/**
	 * 消费，从队列取东西
	 */
	public WgMessage consumeRecRpt()
	{
		try
		{
			// 消费产品，自动阻塞
			return recRptQueue.poll(AppStaticValue.CACHE_READ_WAIT_TIME, TimeUnit.SECONDS);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "rpt消费异常。");
			return null;
		}

	}

	public int getSize(){
		return recRptQueue.size();
	}

	

}
