package com.montnets.emp.common.cache;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.montnets.emp.common.context.EmpExecutionContext;


public class CommDataDealStorage
{
	private CommDataDealStorage(){}
	
	private static CommDataDealStorage instance = new CommDataDealStorage();
	
	/**
	 * 队列无数据等待时间，单位秒。5s
	 */
	private static final int CACHE_POLL_WAIT_TIME = 5;
	
	public static CommDataDealStorage getInstance()
	{
		return instance;
	}
	
	// 数据队列
	private LinkedBlockingQueue<String> dataQueue = new LinkedBlockingQueue<String>();

	
	
	/**
	 * 生产，填数据入缓冲队列
	 */
	public boolean produce(String data)
	{
		try
		{
			// 放入产品，自动阻塞
			dataQueue.put(data);
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "生产异常。");
			return false;
		}
	}
	
	/**
	 * 生产，填数据入缓冲队列
	 */
	public boolean produce(List<String> dataList)
	{
		try
		{
			for(String msg : dataList)
			{
				// 放入产品，自动阻塞
				dataQueue.put(msg);
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "生产异常。");
			return false;
		}
	}
	
	/**
	 * 消费，从队列取东西，队列为空则阻塞5s后返回null
	 */
	public String consume()
	{
		try
		{
			// 消费产品，自动阻塞
			return dataQueue.poll(CACHE_POLL_WAIT_TIME, TimeUnit.SECONDS);
		}
		catch(InterruptedException e)
		{
			EmpExecutionContext.info("队列等待取数据被中断。");
			Thread.currentThread().interrupt();
			return null;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "消费异常。");
			return null;
		}
	}

	public int getSize()
	{
		return dataQueue.size();
	}

}
