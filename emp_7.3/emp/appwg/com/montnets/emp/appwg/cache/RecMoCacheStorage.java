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
 * @datetime 2014-8-31 下午04:40:36
 * @description 接收mo消息缓存队列仓库，单例
 */
public class RecMoCacheStorage
{
	private RecMoCacheStorage(){
		
	}
	
	private static RecMoCacheStorage instance = null;
	
	public static synchronized RecMoCacheStorage getInstance(){
		if(instance == null){
			instance = new RecMoCacheStorage();
		}
		return instance;
	}
	
	// 接收mt消息缓存队列，仓库存储的载体
	private LinkedBlockingQueue<WgMessage> recMoQueue = new LinkedBlockingQueue<WgMessage>();

	
	
	/**
	 * 生产，填数据入缓冲队列
	 */
	public boolean produceRecMo(WgMessage data)
	{
		try
		{
			// 放入产品，自动阻塞
			recMoQueue.put(data);
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "mo生产异常。");
			return false;
		}

	}
	
	
	/**
	 * 生产，填数据入缓冲队列
	 */
	public boolean produceRecMo(List<WgMessage> dataList)
	{
		try
		{
			for(WgMessage msg : dataList){
				// 放入产品，自动阻塞
				recMoQueue.put(msg);
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "mo生产异常。");
			return false;
		}

	}

	/**
	 * 消费，从队列取东西
	 */
	public WgMessage consumeRecMo()
	{
		try
		{
			// 消费产品，自动阻塞
			return recMoQueue.poll(AppStaticValue.CACHE_READ_WAIT_TIME, TimeUnit.SECONDS);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "mo消费异常。");
			return null;
		}

	}

	public int getSize(){
		return recMoQueue.size();
	}

	

}
