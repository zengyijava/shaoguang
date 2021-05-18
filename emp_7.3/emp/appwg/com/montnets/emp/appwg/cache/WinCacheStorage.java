package com.montnets.emp.appwg.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.montnets.emp.appwg.bean.WgMessage;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 
 * @project emp_std_5.0
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-8-28 下午02:22:30
 * @description 发送窗口仓库，单例
 */
public class WinCacheStorage
{
	private WinCacheStorage(){
		
	}
	
	private static WinCacheStorage instance = null;
	
	private final ReentrantLock lock = new ReentrantLock();
	
	public static synchronized WinCacheStorage getInstance(){
		if(instance == null){
			instance = new WinCacheStorage();
		}
		return instance;
	}
	
	// 发送窗口
	private LinkedBlockingQueue<Window> mtWinQueue = new LinkedBlockingQueue<Window>(10);
	
	
	/**
	 * 添加信的窗口
	 * @param
	 * @return 0:添加成功;2:窗口已满;1:添加失败
	 */
	public int addWindow(WgMessage message){
				Window win = new Window(message.getPacketId(), message, new Date());
				try {
					boolean addRes = mtWinQueue.offer(win, 20, TimeUnit.SECONDS);
					if(addRes){
						
						return 0;
					}
					else{
						return -1;
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return -99;
				}
			
	}
	
	/**
	 * 0:删除成功;1:不存在此sequenceId
	 * @param
	 * @return
	 */
	public int removeWindow(WgMessage message){
		//lock.lock();
		try{
			boolean flag = false;
			Window w = new Window(message.getPacketId(), message, new Date());
			flag = mtWinQueue.remove(w);
			if(flag)
				return 0;
			else{
				return 1;
			}
		}finally{
			//lock.unlock();
		}

	}
	
	/**
	 * 0:删除成功;1:不存在此sequenceId;-1异常
	 * @param sequenceId
	 * @return
	 */
	public int removeWindow(String sequenceId){
		//lock.lock();
		try{
			boolean flag = false;
			Window w = new Window(sequenceId, null, new Date());
			flag = mtWinQueue.remove(w);
			if(flag)
				return 0;
			else{
				return 1;
			}
		}
		catch(Exception e){
			EmpExecutionContext.error(e, "删除窗口异常。");
			return -1;
		}
		finally{
			//lock.unlock();
		}

	}
	
	public Window getWindow(String sequence){
		for (Window w : mtWinQueue) {
			String seq = w.getSequenceId();
			if(sequence.equals(seq))
				return w;
		}
		return null;
	}
	
	/**
	 * 以当前时间为终点，删除超时的
	 * @param timeout 超时时间，此参数必须为大于60000（60s）的值。
	 * @return -1：timeout时间太短； N：N>=0,表示删除的窗口的个数
	 */
	public List<Window> removeTimeOutWindows(long timeout){
		try{
			long now = System.currentTimeMillis();
			List<Window> winList = new ArrayList<Window>();
			for(Window win : mtWinQueue){
				Date d = win.getTime();
				//过期的
				if(now - d.getTime() > timeout){
					winList.add(win);
					boolean state = mtWinQueue.remove(win);
					if(!state){
						EmpExecutionContext.error("删除异常。");
					}
				}
			}
			return winList;
		}catch(Exception e){
			
			return null;
		}
	}

}
