package com.montnets.emp.appwg.biz;

import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 
 * @project emp_std_5.0new
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-11-7 上午09:07:44
 * @description 用户同步线程
 */
public class SynUserThread
{
	private UserDealStorage userDealStorage = new UserDealStorage();
	
	private boolean isSynUserThreadExit = true;
	
	//线程是否已启动，true为已启动
	private boolean isThreadStart = false;
	
	/**
	 * 线程是否已启动
	 * @return true为已启动
	 */
	public boolean isThreadStart()
	{
		return isThreadStart;
	}

	public void stopSynUserThread(){
		isSynUserThreadExit = false;
	}
	
	/**
	 * 启动线程
	 */
	public void StartThread(){
		setDealUserThread();
	}
	
	private void setDealUserThread(){
		class _cls1DealUserThread extends Thread
		{
			public final void run()
			{
				while(isSynUserThreadExit){
					//处理一个用户
					userDealStorage.consume();
				}
				
				//线程跳出了循环，则设置为未启动
				isThreadStart = false;
			}
		}

		try
		{
			_cls1DealUserThread dealUserThread = new _cls1DealUserThread();
			dealUserThread.setName("处理用户同步线程");
			dealUserThread.start();
			//设置线程已启动
			isThreadStart = true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动用户同步线程异常。");
		}
	}
}
