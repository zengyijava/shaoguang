package com.montnets.emp.appwg.cache;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.montnets.emp.appwg.bean.AppStaticValue;
import com.montnets.emp.appwg.bean.WgMessage;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 
 * @project emp_std_5.0
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-8-28 下午12:02:49
 * @description 接收mt消息缓存队列仓库，单例
 */
public class RecMtCacheStorage
{
	private RecMtCacheStorage(){
		
	}
	
	private static RecMtCacheStorage instance = null;
	
	public static synchronized RecMtCacheStorage getInstance(){
		if(instance == null){
			instance = new RecMtCacheStorage();
		}
		return instance;
	}
	
	// 接收mt消息缓存队列，仓库存储的载体
	private LinkedBlockingQueue<WgMessage> recMtQueue = new LinkedBlockingQueue<WgMessage>();

	
	
	/**
	 * 生产，填数据入缓冲队列
	 */
	public boolean produceRecMt(WgMessage data)
	{
		try
		{
			// 放入产品，自动阻塞
			recMtQueue.put(data);
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
	public boolean produceRecMt(List<WgMessage> dataList)
	{
		try
		{
			for(WgMessage msg : dataList){
				// 放入产品，自动阻塞
				recMtQueue.put(msg);
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
	 * 消费，从队列取东西
	 */
	public WgMessage consumeRecMt()
	{
		try
		{
			// 消费产品，自动阻塞
			return recMtQueue.poll(AppStaticValue.CACHE_READ_WAIT_TIME, TimeUnit.SECONDS);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "消费异常。");
			return null;
		}

	}

	public int getSize(){
		return recMtQueue.size();
	}

	

}
