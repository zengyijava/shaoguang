package com.montnets.emp.appwg.initappplat;

import java.util.List;

import com.montnets.emp.appwg.cache.WinCacheStorage;
import com.montnets.emp.appwg.cache.Window;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 
 * @project emp_std_5.0
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-8-28 下午12:21:53
 * @description 发送窗口处理线程
 */
public class WinMtThread extends Thread
{
	public WinMtThread(){
		this.setName("发送窗口处理线程");
		
		//wgDao = new WgDAO();
		respBiz = new HandleRespMsgBIz();
	}
	
	//private WgDAO wgDao;
	private HandleRespMsgBIz respBiz;
	
	private boolean isWinThreadExit = true;
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
	
	/**
	 * 设置线程是否已启动，true为已启动
	 */
	public void setThreadStart(boolean isThreadStart)
	{
		this.isThreadStart = isThreadStart;
	}
	
	public void StopWinMtThread(){
		isWinThreadExit = false;
	}
	
	public final void run()
	{
		dealTimeOut();
		
		//线程跳出了循环，则设置为未启动
		isThreadStart = false;
	}
	
	private void dealTimeOut(){
		while(isWinThreadExit){
			try
			{
				//处理过期未收到响应的窗口
				List<Window> winList = WinCacheStorage.getInstance().removeTimeOutWindows(70000);
				if(winList == null || winList.size() == 0){
					Thread.sleep(1*1000L);
					continue;
				}
				
				//处理过期未收到响应的发送信息
				for(Window win : winList){
					respBiz.mtRespFailDeal(win.getMessage());
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "mt消息窗口处理线程，处理异常。");
				
			}
		}
	}
	
	/*private boolean mtRespFailDeal(WgMessage wgMsg){
		try
		{
			boolean res = false;
			//缓冲已满
			if(AppStaticValue.REC_MT_QUEUE_SIZE - RecMtCacheStorage.getInstance().getSize() == 0){
				//更新数据库记录为失败
				res = wgDao.updateSendResult(wgMsg.getId().toString(), "3", "1");
			}else{
				//再把消息放到发送队列，等下次发送
				RecMtCacheStorage.getInstance().produceRecMt(wgMsg);
				//更新数据库记录为失败
				res = wgDao.updateSendResult(wgMsg.getId().toString(), "3", "2");
			}
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理超时未响应的mt异常。");
			return false;
			
		}
	}*/
}
