package com.montnets.emp.common.biz.receive;

import java.sql.Connection;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IDataAccessDriver;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;
import com.montnets.emp.entity.system.LfMotask;


public class ReceiveDataDealThread extends Thread
{
	public ReceiveDataDealThread()
	{
		this.setName("上行数据处理线程");
		
		IDataAccessDriver dataAccessDriver=new DataAccessDriver();
		empTransDao =dataAccessDriver.getEmpTransDAO();
	}
	
	//线程是否继续运行
	private boolean isThreadExit = true;
	//线程是否已启动，true为已启动
	private boolean isThreadStart = false;
	//线程等待时间
	//private long threadWaitTime = TimerStaticValue.EXECUTEINTERVAL;
	private MoDataStorage moDataStorage = MoDataStorage.getInstance();
	
	private Connection conn = null;
	
	private IEmpTransactionDAO empTransDao ;
	
	public final void run()
	{
		this.isThreadStart = true;
		
		while(this.isThreadExit)
		{
			deal();
		}
		
		//线程跳出了循环，则设置为未启动
		this.isThreadStart = false;
	}
	
	private boolean deal()
	{
		//获取上行对象
		LfMotask moTask = moDataStorage.consume();
		if(moTask == null)
		{
			return false;
		}
		
		//未获取连接或者连接已关闭
		if(!connectionAvailable())
		{
			return false;
		}
		
		try
		{
			//入库
			boolean saveMoresult = empTransDao.save(conn, moTask);
			
			//打印记录
			if(saveMoresult)
			{
				EmpExecutionContext.debug("上行数据处理线程，上行msgid:"+moTask.getPtMsgId()+"，插入LfMotask表成功");
			}
			else 
			{
				EmpExecutionContext.debug("上行数据处理线程，上行msgid:"+moTask.getPtMsgId()+"，插入LfMotask表失败");
			}
			
			return saveMoresult;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行数据处理线程，执行sql，异常。");
			return false;
		}
	}
	
	/**
	 * 
	 * @description 获取数据库连接
	 * @return 异常返回null
	 */
	private boolean connectionAvailable()
	{
		try
		{
			IConnectionManager connectionManager = new ConnectionManagerImp();
			
			while(this.isThreadExit)
			{
				this.conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
				//有连接
				if(conn != null)
				{
					return true;
				}
				//等1s继续
				SleepTime(1000l);
			}
			
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行数据处理线程，获取数据库连接，异常。");
			return false;
		}
	}
	
	private void SleepTime(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
			EmpExecutionContext.info(this.getName()+"，强制退出休眠等待。");
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * 启动线程
	 * @return 成功返回true
	 */
	public boolean StartThread()
	{
		try
		{
			this.start();
			
			EmpExecutionContext.info(this.getName()+" 启动成功。");
			
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, this.getName()+" 启动失败。");
			return false;
		}
	}
	
	/**
	 * 线程是否已启动
	 * @return true为已启动
	 */
	public boolean isThreadStart()
	{
		//未启动，直接返回
		if(!this.isThreadStart)
		{
			return false;
		}
		if(!this.isAlive())
		{
			return false;
		}
		
		return true;
	}

	/**
	 * 停止线程
	 */
	public void StopThread()
	{
		try
		{
			this.isThreadExit = false;
			//中断线程
			interrupt();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "停止" + this.getName() + "，异常。");
		}
	}
	
}
